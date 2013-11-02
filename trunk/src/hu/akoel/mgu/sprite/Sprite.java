package hu.akoel.mgu.sprite;

import java.util.ArrayList;
import java.util.HashSet;

import hu.akoel.mgu.MGraphics;
import hu.akoel.mgu.values.PositionValue;
import hu.akoel.mgu.values.SizeValue;
import hu.akoel.mgu.values.TranslateValue;

public class Sprite {
	private boolean enableToPlaceWithoutConnection = true;
	private boolean inFocus = false;
	private SizeValue boundBox;
	private PositionValue position = new PositionValue(0,0);
	private ArrayList<SpriteElement> elements = new ArrayList<SpriteElement>();
	private HashSet<Magnet> magnetList = new HashSet<Magnet>();

	public Sprite( SizeValue boundBox ){
		commonConstructor(boundBox, true);
	}
	
	public Sprite( SizeValue boundBox, boolean enableToPlaceWithoutConnection ){
		commonConstructor(boundBox, enableToPlaceWithoutConnection);
	}
	
	private void commonConstructor( SizeValue boundBox, boolean enableToPlaceWithoutConnection ){
		this.boundBox = boundBox;
		this.enableToPlaceWithoutConnection = enableToPlaceWithoutConnection;
	}
	
	public SizeValue getBoundBox(){
		return new SizeValue(this.boundBox.getXMin() + position.getX(), this.boundBox.getYMin() + position.getY(), this.boundBox.getXMax() + position.getX(), this.boundBox.getYMax() + position.getY());
	}
	
	public void addMagnet( Magnet magnet ){
		this.magnetList.add(magnet);
	}
	
	public HashSet<Magnet> getMagnetList(){
		return magnetList;
	}
	
	public void addElement( SpriteElement element ){
		elements.add(element);
	}
	
	public void setPosition( PositionValue translate ){
		this.position.setX(translate.getX());
		this.position.setY(translate.getY());
	}
	
	public void setPosition( double positionX, double positionY ){
		this.position.setX( positionX );
		this.position.setY( positionY );
	}
	
	public PositionValue getPosition(){
		return new PositionValue(position.getX(), position.getY());
	}
	
	public void draw( MGraphics g2 ){
		for( SpriteElement element: elements){
			element.setPosition(position);
			element.draw(g2);
		}
	}
	
	public void drawFocus( MGraphics g2){
		for( SpriteElement element: elements){
			element.setPosition(position);
			element.drawFocus(g2);
		}
	}
	

	public void drawGhost( MGraphics g2){
		for( SpriteElement element: elements){
			element.setPosition(position);
			element.drawGhost(g2);
		}
	}
	
	public boolean isInFocus(){
		return inFocus;
	}
	
	public void setInFocus( boolean inFocus ){
		this.inFocus = inFocus;
	}
}
