package hu.akoel.mgu.jcanvas.own;

import java.math.BigDecimal;

public class Offset {

	private BigDecimal x;
	private BigDecimal y;
	
	public Offset( BigDecimal x, BigDecimal y ){
		this.x = x;
		this.y = y;
	}
	
	public Offset( double x, double y ){
		this.x = BigDecimal.valueOf(x);
		this.y = BigDecimal.valueOf(y);
	}

	public BigDecimal getX() {
		return x;
	}

	public void setX(BigDecimal x) {
		this.x = x;
	}

	public BigDecimal getY() {
		return y;
	}

	public void setY(BigDecimal y) {
		this.y = y;
	}
	
	public double getXDouble(){
		return getX().doubleValue();
	}
	
	public double getYDouble(){
		return getY().doubleValue();
	}
}
