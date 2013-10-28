package hu.akoel.mgu.jcanvas.own;

public abstract class Value2D {

	private double x;
	private double y;
	
	public Value2D( double x, double y ){
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public String toString(){
		return "[" + x + ", " + y + "]";
	}
}
