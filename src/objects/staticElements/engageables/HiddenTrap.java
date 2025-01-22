package objects.staticElements.engageables;

import pt.iscte.poo.utils.Point2D;

public class HiddenTrap extends Trap {

	private boolean hidden;

	public HiddenTrap(Point2D position) {
		super(position);
		hidden = true;
	}

	@Override
	public String getName() {
		return hidden?"Wall":"Trap";
	}

	@Override
	public boolean activateTrap() {
                boolean active = super.activateTrap();
		hidden = hidden && !active;
                return active;
	}

}
