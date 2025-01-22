package objects.items;

import java.util.ArrayList;
import java.util.List;

import objects.GameElement;
import objects.animations.Explosion;
import objects.attackers.entities.Entity;
import objects.attackers.entities.Manel;
import objects.interfaces.Usable;
import objects.staticElements.StaticElement;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.utils.Point2D;

public class Bomb extends Catchable implements Usable {

        private final int bombTimer = 5;
        private final int cookingTime = 1;

        private int clock;
        private boolean dropped = false;
        private List<Point2D> neighbours = new ArrayList<>();

        public Bomb(Point2D position){
                super("Bomb",position,true);
                neighbours.add(position);
        }

        @Override
        public boolean canBeTransposedBy(GameElement element){
                return !dropped;
        }

        @Override
        public void update(){
                room.getElementsAt(
                        neighbours.toArray(new Point2D[0]))
                        .forEach(
                                e->interact(e,e.getPosition())
                        );
        }

        @Override
        public void interact(GameElement element, Point2D position){
                if( isCooked() && hasTimerRunOut() ) explode();
                else super.interact(element,position);
        }

        @Override
        protected boolean canInteract(Entity element){
                return neighbours.contains(element.getPosition());
        }

        @Override
        protected boolean actionBy(Entity element){
                if(!dropped)
                return catchedBy(element);
                else
                return explode();
        }

        private boolean catchedBy(Entity element){
                if( !getPosition().equals(element.getPosition()) || !(element instanceof Manel) )
                return false;
                return ((Manel)element).addToInventory(this);
        }

        private boolean explode(){
                if( !(isCooked()) )
                return false;
                //animation
                neighbours.forEach(p->new Explosion(p));

                room.removeIf(e->neighbours.contains(e.getPosition()) &&
                        !(e instanceof StaticElement) );
                return true;
        }

        private boolean isCooked(){
                return dropped && GameEngine.getInstance().getTicks()>cookingTime+clock;
        }

        private boolean hasTimerRunOut(){
                return clock+bombTimer<GameEngine.getInstance().getTicks();
        }

        @Override
        public boolean usedBy(Entity entity){
                if ( entity == null ) return false;
                setPosition(entity.getPosition());
                this.neighbours = getPosition().getWideNeighbourhoodPoints();
                this.neighbours.add(getPosition());
                room.addElement(this);

                clock = GameEngine.getInstance().getTicks();
                dropped = true;
                return true;
        }


}
