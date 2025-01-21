package pt.iscte.poo.game;


import objects.attackers.entities.Manel;
import objects.items.Bomb;
import pt.iscte.poo.AnimationList;
import pt.iscte.poo.PlayerScore;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.FileUtil;
import pt.iscte.poo.utils.Time;

public class GameEngine implements Observer {

        private static final int WIDTH = 10;
        private static final int HEIGHT = 10;
        private static GameEngine instance;


        //lista de salas
        private Room room;
        private int lastTickProcessed = 0;
        private Manel manel;

        private final ImageGUI gui = ImageGUI.getInstance();
        private final AnimationList animations = AnimationList.getInstance();

        private GameEngine() {
                gui.registerObserver(this);
                gui.update();
        }

        public static GameEngine getInstance() {
                if ( instance == null )
                instance = new GameEngine();
                return instance;
        }

        private void setupRoom(String roomsDir) {
                room = new Room(roomsDir);
                room.nextLevel();
        }
        private void setupGUI() {
                animations.removeAll();
                gui.clearImages();
                gui.setName("DonkeyGong - Projeto de POO");
                gui.go();
        }

        public void startGame(String roomsDir) {
                setupGUI();
                setupRoom(roomsDir);
                manel = Manel.getInstance();
        }

        public void restartGame(){
                String roomsDir = room.getRoomsDir();
                room = null;
                startGame(roomsDir);
        }

        public void endGame() {
                registerScore();
                room = null;
                gui.dispose();
                System.exit(0);
        }

        public void abort() {
                System.out.println("abort");
                room = null;
                gui.dispose();
                System.exit(0);
        }

        public void registerScore() {
                int firstTick = room.getFirstTick();
                String roomsDir = room.getRoomsDir();
                Time time = new Time(getTicks()-firstTick);
                String name = ImageGUI.getInstance().askUser("Name:");
                if ( name == null ) name = "~~~~~";
                FileUtil.savePlayerScore(new PlayerScore(name,time),roomsDir);
        }

        public int getTicks() {
                return lastTickProcessed;
        }

        public int getWidth() {
                return WIDTH;
        }

        public int getHeight() {
                return HEIGHT;
        }

        public Room getCurrentRoom() {
                return room;
        }

        @Override
        public void update(Observed source) {
                if( room == null ) return;
                animations.removeAll();

                if (gui.wasKeyPressed()) {
                        int key = gui.keyPressed();
                        handleKeyPressed(key);
                        room.update();
                }


                int t = gui.getTicks();
                while (lastTickProcessed < t) {
                        processTick();
                }
                animations.displayAll();
                gui.update();
        }

        private void processTick() {
                room.processTick();
                if (room.isWon())
                        room.nextLevel();
                lastTickProcessed++;
        }

        private void handleKeyPressed(int key) {
                if ( Direction.isDirection(key) ) {
                        manel.move(Direction.directionFor(key));
                } else if ( key == 66 ) {
                        manel.useItem(Bomb.class);
                }
        }















}
