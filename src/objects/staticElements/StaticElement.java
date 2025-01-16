package objects.staticElements;

import objects.GameElement;
import objects.Layer;
import pt.iscte.poo.utils.Point2D;

public class StaticElement extends GameElement {
	
	public StaticElement(String name, Point2D position, Layer layer) {
		super(name,position,layer);
	}

	@Override
	public void interact(GameElement element, Point2D position) {}

}
