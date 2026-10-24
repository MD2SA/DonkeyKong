package objects;

public enum Layer {

	Filler(0), Transposable(1), Entity(2), Support(3), Catchable(4), Projectile(5), Animation(6);

	public final int layer;

	private Layer(int layer){ this.layer = layer; }
}
