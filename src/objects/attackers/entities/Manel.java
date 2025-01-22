package objects.attackers.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


import objects.interfaces.Usable;
import objects.items.Catchable;
import objects.items.unstackables.*;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.gui.ImageGUI;

public class Manel extends Entity {

        private static Manel instance;
        private static final double health = 125;
        private static final double attack = 10;

        private Point2D initalPosition;
        private int lifes;

        private final Map<Class<? extends Catchable>, List<Catchable>> stackableItems = new HashMap<>();
        private final Set<Class<? extends Catchable>> unstackableItems = new HashSet<>();

        private Manel(Point2D initialPosition) {
                super("JumpMan", initialPosition, attack, health, false);
                this.initalPosition = initialPosition;
                lifes = 3;
        }

        public static Manel getInstance() {
                if (instance == null)
                        instance = new Manel(new Point2D(2, 8));
                return instance;
        }

        @Override
        public void move() {
                if( isFallingAt(getPosition()) )
                        super.move(Direction.DOWN);
        }

        @Override
        public void terminate() {
                lifes--;
                setHealth(getMaxHealth());
                if (lifes <= 0) {
                        lifes = 3;
                        clearInventory();
                        ImageGUI.getInstance().setStatusMessage("Game loss. Restarting ...");
                        GameEngine.getInstance().restartGame();
                        room = GameEngine.getInstance().getCurrentRoom();
                } else {
                        ImageGUI.getInstance().setStatusMessage("JumpMan died...");
                        setPosition(initalPosition);
                }
        }

        public void setInitialPosition(Point2D position) {
                this.initalPosition = position;
                setPosition(position);
        }

        @Override
        public String getName(){
                return hasItem(Sword.class)?super.getName()+"WithSword":super.getName();
        }


        public boolean addToInventory(Catchable c) {
                if (c == null)
                return false;

                return addCommonItem(c) || addCustomItem(c);
        }

        private boolean addCommonItem(Catchable c) {
                if (c == null || !(c instanceof Unstackable) )
                        return false;
                if( unstackableItems.contains(c.getClass()) ){
                        ImageGUI.getInstance().setStatusMessage("Already has "+c.getName());
                        return false;
                }
                ImageGUI.getInstance().setStatusMessage("JumpMan catched a new item: "+c.getName());
                return unstackableItems.add(c.getClass());
        }

        private boolean addCustomItem(Catchable c) {
                if (c == null || !c.isStackable() )
                        return false;
                List<Catchable> list = stackableItems.computeIfAbsent(c.getClass(), k -> new LinkedList<>());
                ImageGUI.getInstance().setStatusMessage("JumpMan catched a "+c.getName()+"!");
                return list.add(c);
        }

        public void useItem(Class<? extends Catchable> c) {
                List<Catchable> items = stackableItems.get(c);
                if (items == null || items.isEmpty())
                        return;
                Catchable item = items.removeFirst();
                if (item instanceof Usable){
                        ImageGUI.getInstance().setStatusMessage("JumpMan used a "+c.getName()+"!");
                        ((Usable) item).usedBy(this);
                }
        }

        public boolean hasItem(Class<? extends Catchable> item) {
                if (unstackableItems.contains(item))
                        return true;
                return (stackableItems.get(item) != null && !stackableItems.get(item).isEmpty());
        }

        public void clearInventory() {
                stackableItems.clear();
                unstackableItems.clear();
        }
}
