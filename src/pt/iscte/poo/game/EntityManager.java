package pt.iscte.poo.game;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import objects.GameElement;
import objects.interfaces.WinVerifier;
import objects.attackers.entities.Manel;
import objects.staticElements.Floor;
import objects.items.Bomb;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.gui.ImageGUI;

public class EntityManager {

        private final GameEngine engine = GameEngine.getInstance();

        private final Map<Point2D, List<GameElement>> map = new HashMap<>();
        private final List<GameElement> gameElements = new ArrayList<>();
        private final List<GameElement> toAdd = new ArrayList<>();
        private final List<GameElement> toRemove = new ArrayList<>();

        private WinVerifier winElement;

        public EntityManager() {
                cleanGame();
        }

        // -----------GETTERS------------
        public Map<Point2D, List<GameElement>> getRoomMap() {
                return map;
        }

        public List<GameElement> getElements() {
                return gameElements;
        }

        public List<GameElement> getElementsAt(Point2D... position) {
                List<GameElement> elements = new ArrayList<>();
                for (Point2D pos : position)
                        elements.addAll(map.get(pos));
                return elements;
        }

        public WinVerifier getWinningObject() {
                return winElement;
        }

        public boolean isWon() {
                return winElement != null && winElement.isWon();
        }

        // -----------Updates------------
        public void update() {
                mergeNewElements();
                clearInvalid();
        }

        public void processTick() {
                for (GameElement element : gameElements)
                        if (engine.isWithinBounds(element.getPosition()))
                                element.update();
                        else
                                toRemove.add(element);
                update();
        }

        // -----------Manipulations------------
        public void addElement(GameElement element) {
                if ( element == null || !GameEngine.getInstance().isWithinBounds(element.getPosition()) )
                        return;
                toAdd.add(element);
        }

        public void removeElement(GameElement element) {
                if ( element == null || !GameEngine.getInstance().isWithinBounds(element.getPosition()) )
                        return;
                toRemove.add(element);
        }

        private void mergeNewElements() {
                for (GameElement element : toAdd)
                        if (gameElements.add(element) && map.get(element.getPosition()).add(element))
                                ImageGUI.getInstance().addImage(element);

                toAdd.clear();
        }

        //if i try to apply the same logic as in mergeNewElements it return false on remove of gameELements for some reason
        //still need to figure out why
        private void clearInvalid() {
                for( GameElement element : toRemove  ){
                        gameElements.remove(element);
                        map.get(element.getPosition()).remove(element);
                }
                ImageGUI.getInstance().removeImages(toRemove);
                toRemove.clear();
        }

        // -----------Helpers------------
        public boolean canTranspose(GameElement element, Point2D position) {
                if (element == null || !engine.isWithinBounds(position))
                        return false;

                for (GameElement e : map.get(position))
                        if (!e.equals(element) && !e.canBeTransposedBy(element))
                                return false;

                return true;
        }

        public boolean hasElement(Class<? extends GameElement> clx, Point2D position) {
                if (!engine.isWithinBounds(position))
                        return false;
                for (GameElement element : map.get(position))
                        if (element.getClass().equals(clx))
                                return true;
                return false;
        }

        public void cleanGame() {
                loadMap();
                gameElements.clear();
                toAdd.clear();
                toRemove.clear();
                ImageGUI.getInstance().clearImages();
                winElement = null;
        }

        private void loadMap() {
                for (int i = -1; i < GameEngine.getInstance().getWidth() + 1; i++)
                        for (int j = -1; j < GameEngine.getInstance().getHeight() + 1; j++)
                                map.put(new Point2D(i, j), new ArrayList<>());
        }

        public void handleNewFileElement(GameElement element) {
                ImageGUI.getInstance().addImage(element);
                if (!(element instanceof Floor))
                        ImageGUI.getInstance().addImage(new Floor(element.getPosition()));
                if (element instanceof WinVerifier)
                        winElement = (WinVerifier) element;
                if (!(element instanceof Manel))
                        map.get(element.getPosition()).add(element);
                gameElements.add(element);
        }
}
