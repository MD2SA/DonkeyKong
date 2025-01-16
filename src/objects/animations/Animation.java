package objects.animations;

import objects.Layer;
import pt.iscte.poo.AnimationList;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Animation implements ImageTile {

	private final String name;
	private final Point2D position;

	public Animation(String name, Point2D position) {
		this.position = position;
		this.name = name;
        AnimationList.getInstance().addAnimation(this);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return Layer.Animation.layer;
	}

}
