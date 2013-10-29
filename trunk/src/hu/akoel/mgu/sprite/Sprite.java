package hu.akoel.mgu.sprite;

import java.util.ArrayList;

import hu.akoel.mgu.MGraphics;
import hu.akoel.mgu.values.TranslateValue;

public class Sprite {
	private TranslateValue translate = new TranslateValue(0,0);
	private ArrayList<SpriteElement> elements = new ArrayList<SpriteElement>();

	public Sprite(){
		
	}
	
	public void addElement( SpriteElement element ){
		elements.add(element);
	}
	
	public void setTranslate( TranslateValue translate ){
		this.translate.setX(translate.getX());
		this.translate.setY(translate.getY());
	}
	
	public void setTranslate( double translateX, double translateY ){
		this.translate.setX( translateX );
		this.translate.setY( translateY );
	}
	
	public void draw( MGraphics g2 ){
		for( SpriteElement element: elements){
			element.setTranslate(translate);
			element.draw(g2);
		}
	}
}
