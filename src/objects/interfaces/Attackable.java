package objects.interfaces;

public interface Attackable {

	void attacked(Attacker element);

	double getHealth();

	double getMaxHealth();

	void boostHealth(double boost);
}
