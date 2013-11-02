package hu.akoel.mgu.sprite;

import java.util.ArrayList;
import java.util.HashSet;

import hu.akoel.mgu.MGraphics;
import hu.akoel.mgu.values.PositionValue;
import hu.akoel.mgu.values.RangeValueInPixel;
import hu.akoel.mgu.values.SizeValue;

public class Magnet {

	private Sprite parent;
	private PositionValue position;
	private RangeValueInPixel rangeInPixel;
	private double direction;
	private MagnetType type;	
	private HashSet<MagnetType> possibleMagnetTypeToConnect = new HashSet<>();
	private ArrayList<SpriteElement> elements = new ArrayList<SpriteElement>();
	private Magnet connectedTo;
		
	public Magnet( Sprite parent, MagnetType type, double direction, RangeValueInPixel rangeInPixel, PositionValue position ){
		this.parent = parent;
		this.type = type;
		this.direction = direction;
		this.rangeInPixel = rangeInPixel;
		this.position = position;
	}
	
	public HashSet<MagnetType> getPossibleMagnetTypeToConnect(){
		return possibleMagnetTypeToConnect;
	}
	
	public PositionValue getPosition() {
		return new PositionValue(position.getX(), position.getY());
	}

	public void setPosition(PositionValue position) {
		this.position.setX(position.getX());
		this.position.setY(position.getY());
	}
	
	public void addPossibleMagnetTypToConnect( MagnetType possibleConnectionTo ){
		this.possibleMagnetTypeToConnect.add(possibleConnectionTo);
	}
	
	public void addElement( SpriteElement element ){
		elements.add(element);
	}
	
	public void setConnectedTo( Magnet magnetToConnect ){
		
		if( null == magnetToConnect ){
			
			if( null != this.connectedTo ){
				this.connectedTo.connectedTo = null;
			}
			this.connectedTo = magnetToConnect;
			
		//Ha ezt a magnetet csatlakoztathatom a masikhoz es a masikat is csatlakoztathatom ehhez
		}else if( 
				this.getPossibleMagnetTypeToConnect().contains(magnetToConnect.getType()) &&
				magnetToConnect.getPossibleMagnetTypeToConnect().contains(this.getType() )
		){
			//A mozgatott Sprite Magnet-jenek kotese a bazis Sprite Magnet-jehez
			this.connectedTo = magnetToConnect;
			
			//A bazis Sprite Magnet-jenek kotese a Mozgatott Sprite Magnet-jehz
			magnetToConnect.connectedTo = this;
			
			//A bazis Sprite pozicioja
			PositionValue baseSpritePostion = magnetToConnect.parent.getPosition();
			
			//Kozos magnas pozicioja
			PositionValue magnetPosition = new PositionValue(baseSpritePostion.getX() + magnetToConnect.getPosition().getX(), baseSpritePostion.getY() + magnetToConnect.getPosition().getY() );
			PositionValue parentSpritePosition = new PositionValue( magnetPosition.getX() - this.getPosition().getX(), magnetPosition.getY() - this.getPosition().getY() );
		
			parent.setPosition(parentSpritePosition);
		}
	}
	
	public Magnet getConnectedTo(){
		return connectedTo;
	}
	
	public MagnetType getType(){
		return type;
	}
	
	public double getDirection(){
		return direction;
	}
	
	public RangeValueInPixel getRangeInPixel(){
		return rangeInPixel;
	}
	
	
	public void draw( MGraphics g2){
		for( SpriteElement element: elements){
			element.setPosition(parent.getPosition());
			element.draw(g2);
		}
	}
	
	public void drawConnected( MGraphics g2){
		for( SpriteElement element: elements){
			element.setPosition(parent.getPosition());
			element.drawConnected(g2);
		}
	}
	
	public void drawFocus( MGraphics g2){
		for( SpriteElement element: elements){
			element.setPosition(parent.getPosition());
			element.drawFocus(g2);
		}
	}
	
	
	public String toString(){
		return "Type: " + getType() + " Direction: " + getDirection() + " Range in pixel: " + rangeInPixel;
	}
}
