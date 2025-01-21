package objects.staticElements.engageables;

import java.util.ArrayList;
import java.util.List;

import objects.GameElement;
import objects.Layer;
import objects.animations.Fire;
import objects.attackers.entities.Manel;
import objects.interfaces.Attackable;
import objects.interfaces.Attacker;
import objects.staticElements.StaticElement;
import pt.iscte.poo.utils.Point2D;

public class Trap extends StaticElement implements Attacker {

	private static final double attack = 5;

    private final List<Point2D> neighbours;

	public Trap(Point2D position) {
		super("Trap", position, Layer.Support);
        this.neighbours = getPosition().getNeighbourhoodPoints();
	}

	protected void activateTrap() {
		List<GameElement> gameElements = new ArrayList<>();
		Point2D pos = getPosition();
		for( Point2D position : neighbours ){
			new Fire(pos.directionTo(position),position);
                        gameElements.addAll(room.getElements(position));
                }

		for( GameElement element : gameElements )
			if( neighbours.contains(element.getPosition()) && element instanceof Attackable )
				attack(((Attackable)element));
	}

    @Override
    public void interact(GameElement element,Point2D position){
        if ( !(element instanceof Manel) || !neighbours.contains(element.getPosition()) )
        	return;
        activateTrap();
    }

	@Override
	public void attack(Attackable element) {
		if( element != null )
		element.attacked(this);
	}

	@Override
	public double getAttack() {
		return attack;
	}

	@Override
	public void boostAttack(double boost) { /* cant boost trap damage */}

}
