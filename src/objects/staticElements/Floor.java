package objects.staticElements;

import objects.Layer;
import pt.iscte.poo.utils.Point2D;

public class Floor extends StaticElement {

	public Floor(Point2D position) {
		super("Floor", position, Layer.Filler);
	}

}
