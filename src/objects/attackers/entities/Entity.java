package objects.attackers.entities;

import java.util.Random;

import objects.GameElement;
import objects.Layer;
import objects.animations.Blood;
import objects.attackers.MovableAttacker;
import objects.interfaces.Attackable;
import objects.interfaces.Attacker;
import objects.staticElements.Stairs;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class Entity extends MovableAttacker implements Attackable {

    private double maxHealth;
    private boolean canFly;
    private double health;
    private char looking = 'R';

    public Entity(String name, Point2D position, double attack, double health, boolean canFly) {
        super(name, position, Layer.Entity, attack);
        this.health = health;
        this.maxHealth = health;
        this.canFly = canFly;
    }

    @Override
    public String getName(){
        return super.getName()+looking;
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public double getMaxHealth(){
        return maxHealth;
    }

    protected void setHealth(double health) {
        this.health = health;
    }

    @Override
    public void boostHealth(double boost) {
        double newHealth = health+boost;
        if( newHealth <= maxHealth )
        	health = newHealth;
        else
        	health = maxHealth;
        ImageGUI.getInstance().setStatusMessage("Effect on "+super.getName()+" health: "+boost);
    }

    @Override
    public void attacked(Attacker element) {
        health -= element.getAttack();
        new Blood(getPosition());
        ImageGUI.getInstance().setStatusMessage(super.getName() + " got attacked." + health + "/" + maxHealth);
        if (health <= 0)
            terminate();
    }

    @Override
    public void interact(GameElement element, Point2D position) {
        if (!getPosition().equals(position) || element == null)
            return;
        if (element instanceof Attacker)
            ((Attacker) element).attack(this);
    }

    @Override
    public boolean isFallingAt(Point2D position) {
        boolean hasSupport = !room.canTranspose(this, position.plus(Direction.DOWN.asVector()));
        return !hasSupport && !canFly && (!room.hasElement(Stairs.class, position) &&
                !room.hasElement(Stairs.class, position.plus(Direction.DOWN.asVector())));
    }

    @Override
    public void move() {
        int level = room.getLevel();
        Point2D currentPos = getPosition();
        Point2D manelPos = Manel.getInstance().getPosition();

        // if same pos interact with position
        if ( currentPos.equals(manelPos) ){
            room.interactWith(this, manelPos);
            return;
        }

        // random chance of random move according to level
        if( new Random().nextDouble()>(level/10.0)+0.45 ){
            move(Direction.random());
            return;
        }

        Direction toManel = currentPos.directionTo(manelPos);
        if( canMove(toManel) ){
            move(toManel);
            return;
        }

        //find the best direction to move to pos of manel
        int dx = Integer.signum(manelPos.getX() - currentPos.getX());
        int dy = Integer.signum(manelPos.getY() - currentPos.getY());
        Vector2D v = new Vector2D(dx, 0);
        if( toManel.asVector().getX() == 0)
            if( dx != 0)
                move(Direction.forVector(v));
        else
            if( dy != 0 )
                move(Direction.forVector(new Vector2D(0, dy)));
    }

    @Override
    public void move(Direction direction) {
        Point2D newPosition = getPosition().plus(direction.asVector());
        looking = lookingDirection(direction);

        if (canMove(direction))
            setPosition(newPosition);

        room.interactWith(this, newPosition);
    }

    @Override
    public boolean canMove(Direction direction) {
        Point2D newPosition = getPosition().plus(direction.asVector());

        boolean isValidPosition = room.isWithinBounds(newPosition);
        boolean canTranspose = room.canTranspose(this, newPosition);

        if (Direction.UP.equals(direction))
            return isValidPosition && canTranspose && (canFly || room.hasElement(Stairs.class, getPosition()));

        return isValidPosition && canTranspose;
    }

    public char lookingDirection(Direction direction){
        switch(direction){
            case Direction.LEFT: return 'L';
            case Direction.RIGHT: return 'R';
            default: return looking;
        }
    }

}
