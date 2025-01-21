package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;


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
        private final Map<Point2D,List<GameElement>> toAdd = new HashMap<>();
        private final Map<Point2D,List<GameElement>> toRemove = new HashMap<>();


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

        public Map<Point2D,List<GameElement>> getElements(){
                return map;
        }

        public List<GameElement> getElements(Point2D position){
                if( !isWithinBounds(position) )
                        return null;
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
                map.clear();
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
                int y = -1;
                try(Scanner scanner = new Scanner(file)){
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
                                        map.put(position, list);

                                        GameElement element = GameElement.createFrom(c,position);
                                        ImageGUI.getInstance().addImage(new Floor(position));

                                        if ( element instanceof Floor ) continue;

                                        ImageGUI.getInstance().addImage(element);

                                        if(element instanceof WinVerifier)
                                        winElement = (WinVerifier)element;

                                        list.add(element);
                                }
                                y++;
                        }
                        if( y != engine.getHeight() ) {
                                engine.abort();
                        }
                        // create empty arraylist at the borders
                        addBoundaryPoints(-1, width, -1, true);
                        addBoundaryPoints(-1, width, y, true);
                        addBoundaryPoints(-1, y, -1, false);
                        addBoundaryPoints(-1, y, width, false);
                        scanner.close();
                } catch( FileNotFoundException e){
                        System.err.println("Error loading file: "+file.getName());
                        loadFrom(FileUtil.askUserFile());
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

        public void processTick(){
                for( Map.Entry<Point2D,List<GameElement>> entry : map.entrySet() ) {
                        for ( GameElement element : entry.getValue())
                                element.update();
                }
                update();
        }

        public void update(){
                manageInteractions();
                mergeNewElements();
                clearInvalid();
        }

        //mudar isto para ser cada um a chamar
        public void manageInteractions(){
                for( Map.Entry<Point2D,List<GameElement>> entry : map.entrySet() ) {
                        for( GameElement element : entry.getValue() ) {

                                Point2D posToInteract = element.getPositionToInteract();
                                if ( posToInteract == null || !map.containsKey(posToInteract) )
                                        continue;

                                for( GameElement elementToInteract : map.get(posToInteract) ){
                                        if( elementToInteract.equals(element) )
                                                continue;
                                        elementToInteract.interact(element, posToInteract);
                                        element.setPositionToInteract(null);
                                }

                        }
                }
        }

        private void mergeNewElements(){
                for ( Map.Entry<Point2D,List<GameElement>> entry : toAdd.entrySet() ) {
                        if( !map.containsKey(entry.getKey()) )
                                continue;
                        map.get(entry.getKey()).addAll(entry.getValue());
                        System.out.println(entry.getValue().size());
                        ImageGUI.getInstance().addImages(entry.getValue());
                }
                toAdd.clear();
        }

        private void clearInvalid() {
                for ( Map.Entry<Point2D,List<GameElement>> entry : toAdd.entrySet() ) {
                        if( !map.containsKey(entry.getKey()) )
                                continue;
                        map.get(entry.getKey()).removeAll(entry.getValue());
                        ImageGUI.getInstance().removeImages(entry.getValue());
                }
                toRemove.clear();
        }

        public void changePosition(GameElement element, Point2D newPosition) {
                map.get(element.getPosition()).remove(element);
                map.get(newPosition).add(element);
        }


        public void addElement(GameElement element) {
                if ( element == null || !isWithinBounds(element.getPosition()) )
                        return;
                List<GameElement> list = toAdd.getOrDefault(element.getPosition(), new ArrayList<>());
                list.add(element);
                map.put(element.getPosition(),list);
        }

        public void removeElement(GameElement element) {
                if ( element == null || !isWithinBounds(element.getPosition()) )
                        return;
                List<GameElement> list = toRemove.getOrDefault(element.getPosition(), new ArrayList<>());
                list.add(element);
                map.put(element.getPosition(),list);
        }

        public boolean canTranspose(GameElement element,Point2D position) {
                if( element == null  || !isWithinBounds(position) ) return false;

                for(GameElement e : map.get(position) )
                        if( !e.equals(element) && !e.canBeTransposedBy(element) )
                                return false;

                return true;
        }

        public boolean hasElement( Class<? extends GameElement> clx, Point2D position) {
                if( !isWithinBounds(position) ) return false;
                for(GameElement element : map.get(position) )
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
