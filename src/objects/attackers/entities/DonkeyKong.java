package objects.attackers.entities;

import objects.GameElement;
import objects.attackers.projectiles.Banana;
import objects.interfaces.Attacker;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class DonkeyKong extends Entity {

    private static final double health = 100;
    private static final double attack = 10;

	public DonkeyKong(Point2D position) {
		super("DonkeyKong", position,attack,health,false);
	}

    public void throwBanana(){
   	    if ( Math.random()<0.3)
   		    room.addElement(new Banana(getPosition(),Direction.DOWN));
    }

    @Override
    public void attacked(Attacker element){
        if( element instanceof Banana ) return;
        super.attacked(element);
    }

    @Override
    public void interact(GameElement element,Point2D position) {
    	if( element instanceof Banana) return;
    	super.interact(element,position);
    }

    @Override
    public void move(Direction direction) {
        if( direction.equals(Direction.UP) || direction.equals(Direction.DOWN))
            direction = Math.random()>0.5?Direction.RIGHT:Direction.LEFT;
        super.move(direction);
        throwBanana();
    }

    @Override
    public boolean canMove(Direction direction) {
    	return super.canMove(direction) && !isFallingAt(getPosition().plus(direction.asVector()));
    }

}
