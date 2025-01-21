package objects;

import java.util.List;

import objects.attackers.entities.BadGuy;
import objects.attackers.entities.Bat;
import objects.attackers.entities.DonkeyKong;
import objects.attackers.entities.Manel;
import objects.interfaces.Interactable;
import objects.items.Bomb;
import objects.items.Meat;
import objects.items.unstackables.Key;
import objects.items.unstackables.Sword;
import objects.staticElements.Floor;
import objects.staticElements.Stairs;
import objects.staticElements.Wall;
import objects.staticElements.engageables.Door;
import objects.staticElements.engageables.HiddenTrap;
import objects.staticElements.engageables.LockedDoor;
import objects.staticElements.engageables.Princess;
import objects.staticElements.engageables.Trap;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public abstract class GameElement implements ImageTile, Interactable{

        private String name;
        private Point2D position;
        private final int layer;
        protected Room room;

        public GameElement(String name, Point2D position, Layer layer) {
                this.name = name;
                this.position = position;
                this.layer = layer.layer;
                room = GameEngine.getInstance().getCurrentRoom();
        }

        @Override
        public String toString() {
                return name +", "+position;
        }

        @Override
        public String getName() {
                return name;
        }

        protected void setName(String name){
                this.name = name;
        }

        @Override
        public Point2D getPosition() {
                return position;
        }

        // the day this throws a concurrency exception is because an interaction is changing a element from place
        protected void setPosition(Point2D position) {
                room.getElementsAt(this.position).remove(this);
                this.position = position;
                List<GameElement> list = room.getElementsAt(position);
                if( list == null ) System.out.println(position);
                list.add(this);
        }

        @Override
        public int getLayer() {
                return layer;
        }

        public boolean canBeTransposedBy(GameElement element){
                return element.getLayer()>getLayer();
        }

        public abstract void update();

        public void terminate(){
                room.removeElement(this);
                // setPosition(Room.TERMINATE_POSITION);
        }

        public abstract void interact(GameElement element, Point2D position);

        public static GameElement createFrom(char c, Point2D position) {
                switch(c) {
                        case ' ': return new Floor(position);
                        case 'W': return new Wall(position);
                        case 'S': return new Stairs(position);
                        case 't': return new Trap(position);
                        case 'G': return new DonkeyKong(position);
                        case 'H':
                        Manel manel = Manel.getInstance();
                        manel.setInitialPosition(position);
                        return manel;
                        case 'P': return new Princess(position);
                        case 'h': return new HiddenTrap(position);
                        case 'B': return new Bat(position);
                        case 'b': return new Bomb(position);
                        case 'g': return new BadGuy(position);
                        case 'k': return new Key(position);
                        case 's': return new Sword(position);
                        case 'm': return new Meat(position);
                        case '0': return new Door(position);
                        case '.': return new LockedDoor(position);
                        default : {
                                System.err.println("Character at position "+position+" not recognized as a GameElement");
                                return new Floor(position);
                        }
                }
        }

















}
