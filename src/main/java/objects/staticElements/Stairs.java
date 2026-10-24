package objects.staticElements;

import objects.GameElement;
import objects.Layer;
import pt.iscte.poo.utils.Point2D;

public class Stairs extends StaticElement {

	public Stairs(Point2D position) {
		super("Stairs", position, Layer.Transposable);
	}

    @Override
    public void interact(GameElement element,Point2D position) {}

}
