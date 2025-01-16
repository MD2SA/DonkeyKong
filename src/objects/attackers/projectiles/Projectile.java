package objects.attackers.projectiles;

import objects.GameElement;
import objects.Layer;
import objects.attackers.MovableAttacker;
import objects.interfaces.Attackable;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Projectile extends MovableAttacker {

	private Direction direction;

	public Projectile(String name, Point2D position, double attack, Direction direction) {
		super(name, position, Layer.Projectile, attack);
		this.direction = direction;
	}

	@Override
	public boolean canBeTransposedBy(GameElement element) { return true;}

	@Override
	public void interact(GameElement element,Point2D position) {
		if( element instanceof Attackable && getPosition().equals(position) )
			attack((Attackable)element);
	}

    @Override
    public void attack(Attackable element){
        if ( element == null ) return;
        element.attacked(this);
        terminate();
    }

	@Override
	public void move(Direction direction) {}

	@Override
	public void move() {
		setPosition(getPosition().plus(direction.asVector()));
		room.interactWith(this, getPosition());
	}

	@Override
	public boolean isFallingAt(Point2D position) { return false;}

	@Override
	public boolean canMove(Direction direction) { return true; }

}
