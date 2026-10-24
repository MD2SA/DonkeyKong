package objects.attackers.projectiles;

import objects.interfaces.Attackable;
import objects.attackers.entities.Manel;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Banana extends Projectile {

	private static final double attack = 10;

	public Banana(Point2D position,Direction direction) {
		super("Banana",position,attack,direction);
	}

    @Override
    public void attack(Attackable attackable){
        if ( !(attackable instanceof Manel) ) return;
        super.attack(attackable);
    }

}
