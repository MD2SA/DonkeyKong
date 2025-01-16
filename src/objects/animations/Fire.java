package objects.animations;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Fire extends Animation {

	public Fire(Direction direction, Point2D position) {
		super("Fire"+direction.name(), position);
	}

}
