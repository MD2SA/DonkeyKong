package objects.items.unstackables;

import objects.items.Catchable;
import objects.attackers.entities.Entity;
import objects.attackers.entities.Manel;

import pt.iscte.poo.utils.Point2D;

public class Unstackable extends Catchable {

    public Unstackable(String name, Point2D position){
        super(name,position,false);
    }

    @Override
    protected boolean canInteract(Entity element){
        return element instanceof Manel && getPosition().equals(element.getPosition());
    }

    @Override
    protected boolean actionBy(Entity element) {
        return ((Manel)element).addToInventory(this);
    }

}
