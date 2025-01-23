package pt.iscte.poo.game;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

import objects.GameElement;
import pt.iscte.poo.utils.*;

public class RoomLoader {


        private static final int NUMBER_OF_TRYS = 5;

        public static String loadRoom(Room room) {
                String dirPath = room.getRoomsDir();
                String filePath = room.getRoomFilePath();
                File file = new File(dirPath+filePath);
                String nextRoomPath = loadFrom(room.getEntityManager(), file, NUMBER_OF_TRYS);
                if( nextRoomPath == null || nextRoomPath.isEmpty() ) throw new RuntimeException();
                return nextRoomPath;
        }

        private static String loadFrom(EntityManager eManager, File file, int trys){
                if( trys == 0 ) throw new RuntimeException();
                try (Scanner scanner = new Scanner(file) ) {
                        return processFile(eManager, scanner);
                } catch ( FileNotFoundException e ) {
                        System.err.println("Error loading file: "+file.getName());
                        return loadFrom(eManager,FileUtil.askUserFile(),--trys);
                }
        }

        private static String processFile(EntityManager eManager, Scanner scanner) {
                String nextRoomPath = "";
                int y = -1;
                while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        if( line.isEmpty() ) continue;


                        if( line.startsWith(Room.INIT_CHAR) )
                                nextRoomPath = parseRoomConfig(line);

                        //its skipping last line
                        if( y == -1 )  {
                                nextRoomPath = parseRoomConfig(line);
                                if( Room.END_GAME.equals(nextRoomPath) )
                                        parseGameLine(eManager, line, ++y);
                        } else {
                                parseGameLine(eManager,line,y);
                        }
                        y++;
                }
                if( y != GameEngine.getInstance().getHeight() ) {
                        GameEngine.getInstance().abort();
                }
                return nextRoomPath;
        }

        private static String parseRoomConfig(String line) {
                if ( !line.startsWith(Room.INIT_CHAR) ) {
                        return Room.END_GAME;
                }
                try {
                        String[] parts = line.split(";");
                        String nextRoomPath = parts[1];
                        return nextRoomPath;
                } catch( IndexOutOfBoundsException e ) {
                        GameEngine.getInstance().abort();
                }
                return null;
        }


        private static void parseGameLine(EntityManager eManager, String line,int y) {
                int width = GameEngine.getInstance().getWidth();

                if( line.length() < width)
                        System.err.println("Line nº"+y+" is smaller than expected");

                for(int i = 0; i<width; i++) {
                        char c = (line.length()>i)?line.charAt(i):' ';

                        Point2D position = new Point2D(i,y);
                        GameElement element = GameElement.createFrom(c,position);

                        eManager.handleNewFileElement(element);
                }
        }
}
