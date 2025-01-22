package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

import objects.GameElement;
import objects.attackers.entities.Manel;
import objects.interfaces.WinVerifier;
import objects.staticElements.Floor;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.FileUtil;
import pt.iscte.poo.utils.Point2D;

public class Room {

        private static final String defaultDir = "rooms/default/";
        private static final String END_GAME = "endGame";
        private static final String INIT_CHAR = "#";
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

        public List<GameElement> getElementsAt(Point2D... position){
                List<GameElement> elements = new ArrayList<>();
                for( Point2D pos : position)
                        elements.addAll(map.get(pos));
                return elements;
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
                if (nextRoomPath.contains(END_GAME)) {
                        engine.endGame();
                        return;
                }
                cleanGame();

                File file = new File(roomsDir+nextRoomPath);
                loadFrom(file);
                level++;
        }

        private void cleanGame(){
                map.clear();
                gameElements.clear();
                toAdd.clear();
                toRemove.clear();
                ImageGUI.getInstance().clearImages();
                winElement = null;
        }

        private void loadFrom(File file){
                try (Scanner scanner = new Scanner(file) ) {
                        loadBoundaries();
                        processFile(scanner);
                } catch ( FileNotFoundException e ) {
                        System.err.println("Error loading file: "+file.getName());
                        loadFrom(FileUtil.askUserFile());
                }
        }

        private void loadBoundaries() {
                int width = engine.getWidth();
                int height = engine.getHeight();
                addBoundaryPoints(-1, width, -1, true);
                addBoundaryPoints(-1, width, height, true);
                addBoundaryPoints(-1, height, -1, false);
                addBoundaryPoints(-1, height, width, false);
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

        private void processFile(Scanner scanner) {
                int y = -1;
                while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        if( line.isEmpty() ) continue;

                        if( y == -1 )
                                parseRoomConfig(line);
                        else
                                parseGameLine(line,y);
                        y++;
                }
                if( y != engine.getHeight() ) {
                        engine.abort();
                }
        }

        private void parseRoomConfig(String line) {
                if ( line.startsWith(INIT_CHAR) ) {
                        nextRoomPath = END_GAME;
                        return;
                }
                try {
                        String[] parts = line.split(";");
                        level = Integer.parseInt(parts[0].substring(1));
                        nextRoomPath = parts[1];
                } catch( IndexOutOfBoundsException | NumberFormatException e ) {
                        engine.abort();
                }
        }


        private void parseGameLine(String line,int y) {
                int x = engine.getWidth();

                if( line.length() < x)
                        System.err.println("Line nº"+y+" is smaller than expected");

                for(int i = 0; i<x; i++) {
                        char c = (line.length()>i)?line.charAt(i):' ';

                        Point2D position = new Point2D(i,y);
                        map.computeIfAbsent(position, l->new ArrayList<>());
                        GameElement element = GameElement.createFrom(c,position);

                        handleNewFileElement(element,element.getPosition());
                }
        }

        private void handleNewFileElement(GameElement element,Point2D position){
                ImageGUI.getInstance().addImage(element);

                if ( !(element instanceof Floor) )
                        ImageGUI.getInstance().addImage(new Floor(position));
                if(element instanceof WinVerifier)
                        winElement = (WinVerifier)element;
                if( !(element instanceof Manel) )
                        map.get(position).add(element);
                gameElements.add(element);
        }



        public void processTick(){
                for ( GameElement element : gameElements )
                        if( isWithinBounds(element.getPosition()) )
                                element.update();
                        else
                                toRemove.add(element);
                update();
        }

        public void update(){
                mergeNewElements();
                clearInvalid();
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

        public boolean canTranspose(GameElement element,Point2D position) {
                if( element == null  || !isWithinBounds(position) ) return false;

                for( GameElement e : map.get(position) )
                        if( !e.equals(element) && !e.canBeTransposedBy(element) )
                                return false;

                return true;
        }

        public boolean hasElement( Class<? extends GameElement> clx, Point2D position) {
                if( !isWithinBounds(position) ) return false;
                for( GameElement element : map.get(position) )
                        if( element.getClass().equals(clx) )
                                return true;
                return false;
        }

        public boolean isWithinBounds(Point2D position) {
                return position != null &&
                        position.getX()>=0 && position.getX()<engine.getWidth() &&
                        position.getY()>=0 && position.getY()<engine.getHeight();
        }

}
