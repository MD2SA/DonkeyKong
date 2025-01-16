package objects.attackers.projectiles;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Direction;

public class FireBall extends Projectile {

	private final static double attack = 10;

	public FireBall(Point2D position, Direction direction) {
		super("FireBall",position,attack,direction);
	}

}
