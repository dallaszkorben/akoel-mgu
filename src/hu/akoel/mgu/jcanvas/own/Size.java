package hu.akoel.mgu.jcanvas.own;

import java.math.BigDecimal;

public class Size {

	private BigDecimal width;
	private BigDecimal height;
	
	public Size( BigDecimal width, BigDecimal height ){
		this.width = width;
		this.height = height;
	}
	
	public Size( double width, double height ){
		this.width = BigDecimal.valueOf(width);
		this.height = BigDecimal.valueOf(height);
	}

	public BigDecimal getWidth() {
		return width;
	}

	public void setWidth(BigDecimal width) {
		this.width = width;
	}

	public BigDecimal getHeight() {
		return height;
	}

	public void setHeight(BigDecimal height) {
		this.height = height;
	}
}
