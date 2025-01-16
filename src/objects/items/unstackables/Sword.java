package objects.items.unstackables;

import objects.attackers.entities.Entity;

import pt.iscte.poo.utils.Point2D;

public class Sword extends Unstackable {

    private static final double attack = 15;

	public Sword(Point2D position) {
		super("Sword", position);
	}

    @Override
    protected boolean actionBy(Entity element) {
        boolean actionBy = super.actionBy(element);
        if( !actionBy ) return false;
        element.boostAttack(attack);
        return true;
    }
}

