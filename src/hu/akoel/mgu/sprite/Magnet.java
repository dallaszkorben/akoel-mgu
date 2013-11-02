package hu.akoel.mgu.sprite;

import java.util.HashSet;

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
	
	public void setConnectedTo( Magnet connectedTo ){
		this.connectedTo = connectedTo;
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
	
	public String toString(){
		return "Type: " + getType() + " Direction: " + getDirection() + " Range in pixel: " + rangeInPixel;
	}
}
