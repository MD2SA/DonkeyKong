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
	protected void activateTrap() {
		hidden = false;
		super.activateTrap();
	}

}
