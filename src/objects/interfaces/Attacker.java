package objects.interfaces;

public interface Attacker {

    public void attack(Attackable element);

    public double getAttack();

    public void boostAttack(double boost);

}
