package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import objects.GameElement;
import objects.interfaces.Movable;
import objects.interfaces.WinVerifier;
import objects.staticElements.Floor;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.FileUtil;
import pt.iscte.poo.utils.Point2D;

public class Room {

        public static final Point2D TERMINATE_POSITION = new Point2D(-1,-1);
        private static final String defaultDir = "rooms/default/";
        private String roomsDir;

        private final List<GameElement> gameElements = new ArrayList<>();
        private final List<GameElement> toAdd = new ArrayList<>();

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

        public List<GameElement> getElements(){
                return gameElements;
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
                                        GameElement element = GameElement.createFrom(c,position);
                                        ImageGUI.getInstance().addImage(new Floor(position));

                                        if ( "Floor".equals(element.getName())) continue;

                                        ImageGUI.getInstance().addImage(element);

                                        if(element instanceof WinVerifier)
                                        winElement = (WinVerifier)element;

                                        gameElements.add(element);
                                }
                                y++;
                        }
                        if( y != engine.getHeight() ) {
                                engine.abort();
                        }

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

        public void moveEntities() {
                for(GameElement element : gameElements)
                if ( element instanceof Movable){
                        Movable movable = ((Movable)element);
                        if(movable.isFallingAt(element.getPosition()))
                        movable.move(Direction.DOWN);
                        else
                        movable.move();
                }
                update();
        }

        public void update(){
                mergeNewElements();
                clearInvalid();
        }

        public void interactWith(GameElement gameElement, Point2D position) {
                if( gameElement == null || !isWithinBounds(position) ) return;

                for( GameElement element : gameElements)
                if( !(gameElement.equals(element)) )
                element.interact(gameElement,position);
        }

        private void mergeNewElements(){
                toAdd.removeIf(element->{
                        if(element != null && gameElements.add(element))
                        ImageGUI.getInstance().addImage(element);
                        return true;
                });
        }

        private void clearInvalid() {
                gameElements.removeIf(element->{
                        if( element == null || TERMINATE_POSITION.equals(element.getPosition()) ) {
                                ImageGUI.getInstance().removeImage(element);
                                return true;
                        }
                        return false;
                });
        }

        public void addElement(GameElement element) {
                if ( element == null || !isWithinBounds(element.getPosition()) )
                return;
                toAdd.add(element);
        }

        public void removeIf(Predicate<GameElement> predicate) {
                for( GameElement element:gameElements)
                if( predicate.test(element) )
                element.terminate();
        }

        public boolean canTranspose(GameElement element,Point2D position) {
                if( element == null  || !isWithinBounds(position) ) return false;

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
