package objects.items;

import objects.GameElement;
import objects.Layer;
import objects.attackers.entities.Entity;
import pt.iscte.poo.utils.Point2D;

public abstract class Catchable extends GameElement {

        private final boolean stackable;

        public Catchable(String name, Point2D position, boolean stackable) {
                super(name, position, Layer.Catchable);
                this.stackable = stackable;
        }

        @Override
        public void update(){
                room.getElementsAt(getPosition()).forEach(e->interact(e,e.getPosition()));
        }

        @Override
        public boolean canBeTransposedBy(GameElement element) { return true; }

        @Override
        public void interact(GameElement element,Point2D position) {
                if( !(element instanceof Entity) || !canInteract((Entity)element) )
                        return;
                boolean used = actionBy((Entity)element);
                if( used ) terminate();
        }

        protected abstract boolean canInteract(Entity element);

        protected abstract boolean actionBy(Entity element);

        public final boolean isStackable(){ return stackable; }
}
