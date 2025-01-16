package objects.attackers;

import objects.GameElement;
import objects.Layer;
import objects.interfaces.Attackable;
import objects.interfaces.Attacker;
import objects.interfaces.Movable;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public abstract class MovableAttacker extends GameElement implements Movable,Attacker {

    private double attack;

    public MovableAttacker(String name, Point2D position, Layer layer, double attack){
        super(name,position,layer);
        this.attack = attack;
    }

    public void attack(Attackable element){
    	if( element == null ) return;
    	element.attacked(this);
    }

    public double getAttack(){
        return attack;
    }

    public void boostAttack(double boost){
        attack+=boost;
        ImageGUI.getInstance().setStatusMessage(super.getName()+" got a boost of attack: "+boost);
    }

}
