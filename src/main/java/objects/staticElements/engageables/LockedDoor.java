package objects.staticElements.engageables;

import objects.attackers.entities.Manel;
import objects.items.unstackables.Key;
import pt.iscte.poo.utils.Point2D;


public class LockedDoor extends Door {

	public LockedDoor(Point2D position) {
		super(position);
	}

	@Override
	public String getName() {
		return isOpen()?"DoorOpen":"DoorLocked";
	}

    @Override
    public void openDoor(Manel manel){
        if( !manel.hasItem(Key.class) ) return;
        manel.useItem(Key.class);
        super.openDoor(manel);
    }

}
