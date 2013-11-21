package hu.akoel.mgu.sprite;

import java.util.ArrayList;
import java.util.HashSet;

import hu.akoel.mgu.MGraphics;
import hu.akoel.mgu.values.PositionValue;
import hu.akoel.mgu.values.RangeValueInPixel;
import hu.akoel.mgu.values.SizeValue;

public class Magnet {

	private Sprite parent;
	private PositionValue ralativePositionToSpriteZeroPoint;
	private RangeValueInPixel rangeInPixel;
	private double direction;
	private MagnetType type;	
	private HashSet<MagnetType> possibleMagnetTypeToConnect = new HashSet<>();
	private ArrayList<SpriteElement> elements = new ArrayList<SpriteElement>();
	private Magnet magnetToConnected;
		
	public Magnet( Sprite parent, MagnetType type, double direction, RangeValueInPixel rangeInPixel, PositionValue relativePositionToSpriteZero ){
		this.parent = parent;
		this.type = type;
		this.direction = direction;
		this.rangeInPixel = rangeInPixel;
		this.ralativePositionToSpriteZeroPoint = relativePositionToSpriteZero;
	}
	
	Sprite getParent(){
		return parent;
	}
	
	public HashSet<MagnetType> getPossibleMagnetTypeToConnect(){
		return possibleMagnetTypeToConnect;
	}
	
	public PositionValue getRelativePositionToSpriteZeroPoint() {
		return new PositionValue(ralativePositionToSpriteZeroPoint.getX(), ralativePositionToSpriteZeroPoint.getY());
	}

	public void setRelativePositionToSpriteZeroPoint(PositionValue realtivePositionToSpriteZeroPoint) {
		this.ralativePositionToSpriteZeroPoint.setX(realtivePositionToSpriteZeroPoint.getX());
		this.ralativePositionToSpriteZeroPoint.setY(realtivePositionToSpriteZeroPoint.getY());
	}
	
	public void addPossibleMagnetTypeToConnect( MagnetType possibleConnectionTo ){
		this.possibleMagnetTypeToConnect.add(possibleConnectionTo);
	}
	
	public PositionValue getPosition(){
		return new PositionValue(
				parent.getPosition().getX() + getRelativePositionToSpriteZeroPoint().getX(),
				parent.getPosition().getY() + getRelativePositionToSpriteZeroPoint().getY()
		);
	}
	
	public void addElement( SpriteElement element ){
		elements.add(element);
	}
	
	public PositionValue getPossibleSpritePosition( Magnet magnetToConnect ){

		//A bazis Sprite pozicioja - Ellenoldal
		PositionValue baseSpritePostion = magnetToConnect.parent.getPosition();
		
		//Kozos magnes pozicioja
		PositionValue magnetPosition = new PositionValue(baseSpritePostion.getX() + magnetToConnect.getRelativePositionToSpriteZeroPoint().getX(), baseSpritePostion.getY() + magnetToConnect.getRelativePositionToSpriteZeroPoint().getY() );
		
		//Ennek a magnesnek a szulo-Sprite pozicioja
		PositionValue parentSpritePosition = new PositionValue( magnetPosition.getX() - this.getRelativePositionToSpriteZeroPoint().getX(), magnetPosition.getY() - this.getRelativePositionToSpriteZeroPoint().getY() );

		return parentSpritePosition;
	}
	
	public void setConnectedTo( Magnet magnetToConnect ){
		
		//Ha meg akarom szuntentni a magnes kapcsolatat
		if( null == magnetToConnect ){
			
			//Ha volt kapcsolata ennek a magnesnek
			if( null != this.magnetToConnected ){
				
				//akkor a parjanak a kapcsolatat, ami feltehetoleg ez a magnes, is torli
				this.magnetToConnected.magnetToConnected = null;
			
				//es torli ennek a magnesnek a kapcsolatat
				this.magnetToConnected = magnetToConnect;
			}
			
		//Ha ezt a magnetet csatlakoztathatom a masikhoz es a masikat is csatlakoztathatom ehhez
		}else if( 
				this.getPossibleMagnetTypeToConnect().contains(magnetToConnect.getType()) &&
				magnetToConnect.getPossibleMagnetTypeToConnect().contains(this.getType() )
		){
			//A mozgatott Sprite Magnet-jenek kotese a bazis Sprite Magnet-jehez
			this.magnetToConnected = magnetToConnect;
			
			//A bazis Sprite Magnet-jenek kotese a Mozgatott Sprite Magnet-jehz
			magnetToConnect.magnetToConnected = this;
/*			
			//A bazis Sprite pozicioja - Ellenoldal
			PositionValue baseSpritePostion = magnetToConnect.parent.getPosition();
			
			//Kozos magnes pozicioja
			PositionValue magnetPosition = new PositionValue(baseSpritePostion.getX() + magnetToConnect.getRelativePositionToSpriteZero().getX(), baseSpritePostion.getY() + magnetToConnect.getRelativePositionToSpriteZero().getY() );
			
			//Ennek a magnesnek a szulo-Sprite pozicioja
			PositionValue parentSpritePosition = new PositionValue( magnetPosition.getX() - this.getRelativePositionToSpriteZero().getX(), magnetPosition.getY() - this.getRelativePositionToSpriteZero().getY() );
*/		
			PositionValue parentSpritePosition = getPossibleSpritePosition(magnetToConnect);
			
			//Mozgatott Sprite poziciojanak igazitasa az allo Sprite-hoz
			parent.setPosition(parentSpritePosition);
		}
	}
	
	public Magnet getConnectedTo(){
		return magnetToConnected;
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
			element.setPositionX(parent.getPosition().getX() + getRelativePositionToSpriteZeroPoint().getX() );
			element.setPositionY(parent.getPosition().getY() + getRelativePositionToSpriteZeroPoint().getY() );
			element.draw(g2);
		}
	}
	
	public void drawConnected( MGraphics g2){
		for( SpriteElement element: elements){
			element.setPositionX(parent.getPosition().getX() + getRelativePositionToSpriteZeroPoint().getX() );
			element.setPositionY(parent.getPosition().getY() + getRelativePositionToSpriteZeroPoint().getY() );
			element.drawConnected(g2);
		}
	}
	
	public void drawFocus( MGraphics g2){
		for( SpriteElement element: elements){
			element.setPositionX(parent.getPosition().getX() + getRelativePositionToSpriteZeroPoint().getX() );
			element.setPositionY(parent.getPosition().getY() + getRelativePositionToSpriteZeroPoint().getY() );
			element.drawFocus(g2);
		}
	}
	
	
	public String toString(){
		return "Type: " + getType() + " Direction: " + getDirection() + " Range in pixel: " + rangeInPixel;
	}
}
