package objects.animations;

import java.util.Random;

import pt.iscte.poo.utils.Point2D;

public class Blood extends Animation {

	public Blood(Point2D position) {
		super("Blood"+new Random().nextInt(1, 5), position);
	}

}
