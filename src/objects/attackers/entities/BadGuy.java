package objects.attackers.entities;

import pt.iscte.poo.utils.Point2D;

public class BadGuy extends Entity {

	private static final double health = 100;
	private static final double attack = 15;

	public BadGuy(Point2D position) {
		super("BadGuy", position, attack, health, false);
	}

}
