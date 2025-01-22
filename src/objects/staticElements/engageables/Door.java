package objects.staticElements.engageables;

import objects.GameElement;
import objects.Layer;
import objects.attackers.entities.Manel;
import objects.interfaces.WinVerifier;
import objects.staticElements.StaticElement;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class Door extends StaticElement implements WinVerifier {

    private boolean isOpen;
    private boolean won = false;
    private final int level;

	public Door(Point2D position) {
		super("DoorClosed",position, Layer.Transposable);
        this.level = room.getLevel();
	}

	@Override
	public String getName() {
		return isOpen?"DoorOpen":"DoorClosed";
	}

        @Override
        public boolean canBeTransposedBy(GameElement element){
                if ( element instanceof Manel )
                        return isOpen;
                return super.canBeTransposedBy(element);
        }

        @Override
        public boolean isWon() {
                return won;
        }
        protected boolean isOpen(){
                return isOpen;
        }

        public int getLevel(){
                return level;
        }

	protected void openDoor(Manel manel) {
		isOpen = true;
                ImageGUI.getInstance().setStatusMessage("JumpMan opened the door!");
	}

        @Override
        public void interact(GameElement entity,Point2D position) {
                if ( !(entity instanceof Manel) )
                        return;
                if(isOpen && getPosition().equals(entity.getPosition()) )
                        won = true;
                else if ( getPosition().equals(position))
                        openDoor((Manel)entity);
        }

}
