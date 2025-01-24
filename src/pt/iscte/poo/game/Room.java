package pt.iscte.poo.game;

import java.io.File;

public class Room {

        private static final String defaultDir = "rooms/default/";
        public static final String END_GAME = "endGame";
        public static final String INIT_CHAR = "#";

        private int level = -1;
        private String nextRoomPath;
        private String roomsDir;

        private GameEngine engine = GameEngine.getInstance();
        private final int firstTick = engine.getTicks();
        private EntityManager eManager = new EntityManager();

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

        public void nextLevel(){
                if (nextRoomPath.contains(END_GAME)) {
                        engine.endGame();
                        return;
                } else if ( level != -1 ) {
                        eManager.cleanGame();
                }
                nextRoomPath = RoomLoader.loadRoom(this);
                level++;
        }

        public int getLevel() {
                return level;
        }
        public int getFirstTick(){
                return firstTick;
        }

        public String getRoomFilePath() {
                return nextRoomPath;
        }
        public String getRoomsDir() {
                return roomsDir;
        }

        public EntityManager getEntityManager(){
                return eManager;
        }
        public void update(){
                if( eManager == null ) return;
                eManager.update();
        }
        public void processTick(){
                if( eManager == null ) return;
                else if( eManager.isWon() )
                        nextLevel();
                else
                        eManager.processTick();
        }


}
