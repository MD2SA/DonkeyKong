package objects.items;

import objects.attackers.entities.Entity;
import objects.attackers.entities.Manel;
import objects.interfaces.Attackable;
import objects.attackers.entities.Entity;
import objects.interfaces.Attacker;

import pt.iscte.poo.utils.Point2D;

public class Arrow extends Catchable implements Attacker {

    private int arrows = 5;
    private double damage = 10;
    private boolean shoot = false;

    public Arrow(Point2D position){
        super("Arrow", position, true);
    }

    @Override
    protected boolean canInteract(Entity element){
        return element instanceof Entity && element.getPosition().equals(getPosition());
    }

    @Override
	protected boolean actionBy(Entity element){
        if(shoot){
            element.attacked(this);
            return true;
        }
        else
        return true;
    }

    @Override
    public void attack(Attackable element){
        element.attacked(this);
    }

    @Override
    public double getAttack(){
        return damage;
    }

    @Override
    public void boostAttack(double boost){
        damage+=boost;
    }


}
