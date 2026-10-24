package objects.interfaces;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public interface Movable {

	void move();

	void move(Direction direction);
	
	boolean canMove(Direction direction);

	boolean isFallingAt(Point2D position);
}
