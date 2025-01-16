package objects.staticElements.engageables;

import objects.GameElement;
import objects.Layer;
import objects.attackers.entities.Manel;
import objects.interfaces.WinVerifier;
import objects.staticElements.StaticElement;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class Princess extends StaticElement implements WinVerifier {

    private boolean safe;
    private boolean isWon;

    public Princess(Point2D position) {
        super("Princess", position, Layer.Transposable);
    }

    @Override
    public String getName() {
        return safe ? "HeartPrincess" : "Princess";
    }

    @Override
    public boolean canBeTransposedBy(GameElement element) {
        if (element instanceof Manel)
            return safe;
        return false;
    }

    @Override
    public boolean isWon() {
        return isWon;
    }

    @Override
    public void interact(GameElement element, Point2D position) {
        if (!(element instanceof Manel))
            return;
        if (safe && getPosition().equals(element.getPosition()) )
            isWon = true;
        else if ( getPosition().equals(position) ){
            safe = true;
            ImageGUI.getInstance().setStatusMessage("Princess got saved!");
        }
    }

}
