package objects.interfaces;

import objects.GameElement;
import pt.iscte.poo.utils.Point2D;

public interface Interactable {

	void interact(GameElement element, Point2D position);
}
