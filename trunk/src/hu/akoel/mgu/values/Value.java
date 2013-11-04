package hu.akoel.mgu.values;

public abstract class Value {

	private double x;
	private double y;
	
	public Value( double x, double y ){
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
	
	public boolean equals( Object o ){
		Value other = (Value)o;
		if( other.getX() == this.getX() && other.getY() == this.getY() ){
			return true;
		}else{
			return false;
		}
	}
}
