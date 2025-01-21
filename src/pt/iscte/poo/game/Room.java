package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Predicate;

import objects.GameElement;
import objects.interfaces.WinVerifier;
import objects.staticElements.Floor;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.FileUtil;
import pt.iscte.poo.utils.Point2D;

public class Room {

        public static final Point2D TERMINATE_POSITION = new Point2D(-1,-1);
        private static final String defaultDir = "rooms/default/";
        private String roomsDir;

        private final Map<Point2D,List<GameElement>> map = new HashMap<>();
        private final List<GameElement> gameElements = new ArrayList<>();
        private final List<GameElement> toAdd = new ArrayList<>();
        private final List<GameElement> toRemove = new ArrayList<>();

        private int level = -1;
        private String nextRoomPath;
        private WinVerifier winElement;

        private GameEngine engine = GameEngine.getInstance();
        private final int firstTick = engine.getTicks();

        public Room(String roomsDir) {
                nextRoomPath = "room0.txt";

                if( roomsDir == null ) {
                        this.roomsDir = defaultDir;
                        return;
                }
                File dir = new File(roomsDir);
                if( dir.isDirectory() )
                this.roomsDir = roomsDir;
                else {
                        System.err.println("Unable to find provided directory. Default will be used");
                        this.roomsDir = defaultDir;
                }
        }

        public Map<Point2D,List<GameElement>> getRoomMap(){
                return map;
        }

        public List<GameElement> getElements(){
                return gameElements;
        }

        public List<GameElement> getElementsAt(Point2D position){
                return map.get(position);
        }

        public int getLevel() {
                return level;
        }
        public int getFirstTick(){
                return firstTick;
        }
        public String getRoomsDir() {
                return roomsDir;
        }
        public WinVerifier getWinningObject(){
                return winElement;
        }

        public boolean isWon() {
                if( winElement == null ) return false;
                return ((WinVerifier)winElement).isWon();
        }

        public void nextLevel() {
                if (nextRoomPath.contains("endGame")) {
                        engine.endGame();
                        return;
                }
                gameElements.clear();
                toAdd.clear();
                ImageGUI.getInstance().clearImages();
                winElement = null;

                File file;
                file = new File(roomsDir+nextRoomPath);

                System.out.println(file);
                loadFrom(file);
                level++;
        }

        private void loadFrom(File file) {
                int width = engine.getWidth();
                int height = engine.getHeight();
                int y = -1;
                try(Scanner scanner = new Scanner(file)){
                        addBoundaryPoints(-1, width, -1, true);
                        addBoundaryPoints(-1, width, height, true);
                        addBoundaryPoints(-1, height, -1, false);
                        addBoundaryPoints(-1, height, width, false);
                        while (scanner.hasNextLine()) {
                                String linha = scanner.nextLine();

                                if( linha == null || linha.isEmpty() )
                                continue;
                                if( y == -1 ){
                                        y = 0;
                                        if ( linha.startsWith("#") ) {
                                                scanInitialLine(linha);
                                                continue;
                                        } else {
                                                nextRoomPath = "endGame";
                                        }
                                }

                                if( linha.length() < width) System.err.println("Line nº"+y+" is smaller than expected");
                                for(int i = 0; i<width; i++) {

                                        char c;
                                        if( linha.length()>i)
                                        c = linha.charAt(i);
                                        else
                                        c = ' ';

                                        Point2D position = new Point2D(i,y);
                                        List<GameElement> list = new ArrayList<>();
                                        map.put(position,list);
                                        GameElement element = GameElement.createFrom(c,position);
                                        ImageGUI.getInstance().addImage(element);

                                        if ( !(element instanceof Floor) )
                                                ImageGUI.getInstance().addImage(new Floor(position));

                                        if(element instanceof WinVerifier)
                                                winElement = (WinVerifier)element;

                                        list.add(element);
                                        gameElements.add(element);
                                }
                                y++;
                        }
                        if( y != height ) {
                                engine.abort();
                        }
                        scanner.close();
                } catch( FileNotFoundException e){
                        System.err.println("Error loading file: "+file.getName());
                        loadFrom(FileUtil.askUserFile());
                }
        }

        private void addBoundaryPoints(int start, int end, int fixedCoordinate, boolean isHorizontal) {
                for (int i = start; i <= end; i++) {
                        Point2D position;
                        if (isHorizontal) {
                                position = new Point2D(i, fixedCoordinate);
                        } else {
                                position = new Point2D(fixedCoordinate, i);
                        }
                        map.put(position, new ArrayList<>());
                }
        }

        private void scanInitialLine(String line) {
                try {
                        String[] parts = line.split(";");
                        level = Integer.parseInt(parts[0].substring(1));
                        nextRoomPath = parts[1];
                } catch( IndexOutOfBoundsException | NumberFormatException e ) {
                        engine.abort();
                }
        }

        public void processTick(){
                for ( GameElement element : gameElements )
                        element.update();
                update();
        }

        public void update(){
                // manageInteractions();
                mergeNewElements();
                clearInvalid();
        }

        public void interactionOf(GameElement element, Point2D position) {
                if( element == null || !isWithinBounds(position) ) return ;
                for( GameElement e : map.get(position) )
                        if( !element.equals(e) )
                                e.interact(element, position);
        }

        private void mergeNewElements(){
                for( GameElement element : toAdd )
                        if( gameElements.add(element) && map.get(element.getPosition()).add(element) )
                                ImageGUI.getInstance().addImage(element);
                toAdd.clear();
        }

        private void clearInvalid() {
                for( GameElement element : toRemove  ){
                        gameElements.remove(element);
                        map.get(element.getPosition()).remove(element);
                }
                ImageGUI.getInstance().removeImages(toRemove);
                toRemove.clear();
        }

        public void addElement(GameElement element) {
                if ( element == null || !isWithinBounds(element.getPosition()) )
                        return;
                toAdd.add(element);
        }

        public void removeElement(GameElement element) {
                if ( element == null || !isWithinBounds(element.getPosition()) )
                        return;
                toRemove.add(element);
        }

        public void removeIf(Predicate<GameElement> predicate) {
                for( GameElement element:gameElements)
                        if( predicate.test(element) )
                                element.terminate();
        }

        public boolean canTranspose(GameElement element,Point2D position) {
                if( element == null  || !isWithinBounds(position) ) return false;

                // for( GameElement e : map.get(position) )
                //         if( !e.equals(element) && !e.canBeTransposedBy(element) )
                //                 return false;
                for(GameElement e : gameElements)
                        if( !e.equals(element) &&
                            e.getPosition().equals(position) && !e.canBeTransposedBy(element) )
                                return false;

                return true;
        }

        public boolean hasElement( Class<? extends GameElement> clx, Point2D position) {
                if( !isWithinBounds(position) ) return false;
                for(GameElement element : gameElements)
                        if ( position.equals(element.getPosition()) && element.getClass().equals(clx) )
                                return true;
                return false;
        }

        public boolean isWithinBounds(Point2D position) {
                return position != null &&
                        position.getX()>=0 && position.getX()<engine.getWidth() &&
                        position.getY()>=0 && position.getY()<engine.getHeight();
        }
















}
