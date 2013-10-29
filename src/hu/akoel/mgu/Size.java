package hu.akoel.mgu;

public class Size {

	double xMin;
	double yMin;
	double xMax;
	double yMax;
	
	public Size( double xMin, double yMin, double xMax, double yMax ){
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;
	}
	
	public double getWidth() {
		return xMax - xMin;
	}

	public double getHeight() {
		return yMax - yMin;
	}
	
	public double getXMin(){
		return xMin;
	}
	
	public double getYMin(){
		return yMin;
	}
	
	public double getXMax(){
		return xMax;
	}
	
	public double getYMax(){
		return yMax;
	}

	public String toString(){
		return xMin + ", " + yMin + ", " + xMax + ", " + yMax;
	}
}
