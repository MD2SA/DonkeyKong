package objects.attackers.entities;

import objects.interfaces.Attackable;
import objects.interfaces.Attacker;
import objects.staticElements.Stairs;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Bat extends Entity {

        private static final double health = 100;
        private static final double attack = 25;

        public Bat(Point2D position) {
                super("Bat", position, attack, health, true);
        }

        @Override
        public void move() {
                if( getEManager().hasElement(Stairs.class,getPosition().plus(Direction.DOWN.asVector())) )
                move(Direction.DOWN);
                else
                move(Math.random()<0.5?Direction.RIGHT:Direction.LEFT);
        }

        @Override
        public void attacked(Attacker element) {
                if ( element instanceof Attackable)
                attack((Attackable)element);
                terminate();
        }

        @Override
        public void attack(Attackable element){
                super.attack(element);
                terminate();
        }

}
