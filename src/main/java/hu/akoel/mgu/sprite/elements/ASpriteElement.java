package hu.akoel.mgu.sprite.elements;

import hu.akoel.mgu.MGraphics;
import hu.akoel.mgu.values.PositionValue;


public abstract class ASpriteElement {
	private PositionValue position = new PositionValue(0,0);	

	public void setPositionX( double x ){
		this.position.setX(x);
	}
	
	public void setPositionY( double y ){
		this.position.setY(y);
		
	}
	
	/**
	 * Set the Center of the Element
	 * @param translate
	 */
	public void setPosition( PositionValue translate ){
		this.position.setX(translate.getX());
		this.position.setY(translate.getY());
	}
	
	public double getPositionX(){
		return position.getX();
	}
	
	public double getPositionY(){
		return position.getY();
	}
	
	public abstract void draw( MGraphics g2 );
	
	public abstract void drawFocus( MGraphics g2);
	
	public abstract void drawConnected( MGraphics g2);
	
	public abstract void drawSelected( MGraphics g2);
	
}
