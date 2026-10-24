package objects.staticElements.engageables;

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

        @Override
        public void update(){
                activateTrap();
        }

	protected boolean activateTrap() {
		List<GameElement> gameElements = getEManager().getElementsAt(neighbours.toArray(new Point2D[0]));

                boolean found = false;
                for( GameElement element : gameElements ){
                        if(  element instanceof Attackable ){
                                found = true;
                                attack(((Attackable)element));
                        }
                }
                //animations
                if( found ) {
                        Point2D pos = getPosition();
                        for( Point2D position : neighbours )
                        new Fire(pos.directionTo(position),position);
                }
                return found;
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
