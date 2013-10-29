package hu.akoel.mgu.sprite;

import hu.akoel.mgu.MGraphics;
import hu.akoel.mgu.values.TranslateValue;


public abstract class SpriteElement {
	private TranslateValue translate = new TranslateValue(0,0);
	
	public void setTranslate( TranslateValue translate ){
		this.translate.setX(translate.getX());
		this.translate.setY(translate.getY());
	}
	
	public double getTranslateX(){
		return translate.getX();
	}
	
	public double getTranslateY(){
		return translate.getY();
	}
	
	public abstract void draw( MGraphics g2 );
}
