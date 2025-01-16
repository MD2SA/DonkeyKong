package objects.items;

import objects.attackers.entities.Entity;
import objects.attackers.entities.Manel;
import objects.interfaces.Attackable;
import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.utils.Point2D;

public class Meat extends Catchable {

	private static final int expiration = 20;

    private final int firstTick;
    private boolean isGood = true;
    private final double effect = 25;

    public Meat(Point2D position){
        super("GoodMeat",position,false);
        this.firstTick = GameEngine.getInstance().getTicks();
    }

    @Override
    public String getName() {
    	isGood =  GameEngine.getInstance().getTicks()-expiration < firstTick;
    	return isGood?"GoodMeat":"BadMeat";
    }

    @Override
    protected boolean canInteract(Entity element){
        return element instanceof Manel && getPosition().equals(element.getPosition());
    }

    @Override
	protected boolean actionBy(Entity element) {
        double healthBonus = isGood?effect:-effect;
		Attackable e = ((Attackable)element);
		e.boostHealth(healthBonus);
        return true;
	}

}
