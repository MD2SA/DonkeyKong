package objects.staticElements;

import objects.GameElement;
import objects.Layer;
import pt.iscte.poo.utils.Point2D;

public class Wall extends StaticElement {

        public Wall(Point2D position) {
                super("Wall", position, Layer.Support);
        }

        @Override
        public void interact(GameElement element,Point2D position) {}

}
