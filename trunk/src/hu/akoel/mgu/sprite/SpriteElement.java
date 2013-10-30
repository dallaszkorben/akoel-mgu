package hu.akoel.mgu.sprite;

import hu.akoel.mgu.MGraphics;
import hu.akoel.mgu.values.PositionValue;
import hu.akoel.mgu.values.TranslateValue;


public abstract class SpriteElement {
	private PositionValue position = new PositionValue(0,0);
	
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
	
	public abstract void drawGhost( MGraphics g2);
}
