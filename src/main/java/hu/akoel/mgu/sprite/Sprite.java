package hu.akoel.mgu.sprite;

import java.util.ArrayList;
import java.util.HashSet;

import hu.akoel.mgu.MGraphics;
import hu.akoel.mgu.sprite.elements.ASpriteElement;
import hu.akoel.mgu.values.PositionValue;
import hu.akoel.mgu.values.SizeValue;

public class Sprite {
	private Integer level;
	private boolean enableToPlaceWithoutConnection = true;
	private boolean inFocus = false;
	private boolean isSelected = false;
	private boolean isShadow = false;
	private SizeValue boundBox;
	//private PositionValue permanentPosition = new PositionValue(0,0);
	//private PositionValue temporaryPosition = new PositionValue(0,0);
	private PositionValue position = new PositionValue(0,0);
	private ArrayList<ASpriteElement> elements = new ArrayList<ASpriteElement>();
	private ArrayList<Magnet> magnetList = new ArrayList<Magnet>();
	private HashSet<ChangeSizeListener> changeWidthListenerList = new HashSet<ChangeSizeListener>();
	private HashSet<ChangeSizeListener> changeHeightListenerList = new HashSet<ChangeSizeListener>();

	public Sprite( SizeValue boundBox ){
		commonConstructor( 0, boundBox, true);
	}
	
	public Sprite( Integer level, SizeValue boundBox ){
		commonConstructor( level, boundBox, true);
	}
	
	public Sprite( SizeValue boundBox, boolean enableToPlaceWithoutConnection ){
		commonConstructor( 0, boundBox, enableToPlaceWithoutConnection );
	}
	
	public Sprite( Integer level, SizeValue boundBox, boolean enableToPlaceWithoutConnection ){
		commonConstructor( level, boundBox, enableToPlaceWithoutConnection );
	}
	
	private void commonConstructor( Integer level, SizeValue boundBox, boolean enableToPlaceWithoutConnection ){
		this.level = level;
		this.boundBox = boundBox;
		this.enableToPlaceWithoutConnection = enableToPlaceWithoutConnection;
	}
	
	public void setBoundBoxXMin( double xMin ){
		this.boundBox.setXMin(xMin);
	}
	
	public void setBoundBoxXMax( double xMax ){
		this.boundBox.setXMax(xMax);
	}
	
	public void setBoundYMin( double yMin ){
		this.boundBox.setYMin(yMin);
	}
	
	public void setBoundYMax( double yMax ){
		this.boundBox.setYMax(yMax);
	}
		
	public void setBoudBox( SizeValue boundBox ){
		this.boundBox = boundBox;
	}
	
	public void addChangeWidthListener( ChangeSizeListener changeWidthListener ){
		this.changeWidthListenerList.add( changeWidthListener );
	}

	public void addChangeHeightListener( ChangeSizeListener changeHeightListener ){
		this.changeHeightListenerList.add( changeHeightListener );
	}

	public boolean isEnableToPlaceWithoutConnection() {
		return enableToPlaceWithoutConnection;
	}

	public void setEnableToPlaceWithoutConnection( boolean enableToPlaceWithoutConnection ) {
		this.enableToPlaceWithoutConnection = enableToPlaceWithoutConnection;
	}
	
	public SizeValue getBoundBoxAbsolute(){
		//return new SizeValue(this.boundBox.getXMin() + permanentPosition.getX(), this.boundBox.getYMin() + permanentPosition.getY(), this.boundBox.getXMax() + permanentPosition.getX(), this.boundBox.getYMax() + permanentPosition.getY());
		return new SizeValue(this.boundBox.getXMin() + position.getX(), this.boundBox.getYMin() + position.getY(), this.boundBox.getXMax() + position.getX(), this.boundBox.getYMax() + position.getY());
	}
	
	public void addMagnet( Magnet magnet ){
		this.magnetList.add(magnet);
	}
	
	public ArrayList<Magnet> getMagnetList(){
		return magnetList;
	}
	
	public void addElement( ASpriteElement element ){
		elements.add(element);
	}
	
	public void setPosition( PositionValue position ){
		this.position.setX( position.getX() );
		this.position.setY( position.getY() );
	}
	
	public void setPosition( double positionX, double positionY ){
		this.position.setX( positionX );
		this.position.setY( positionY );
	}
	
	public PositionValue getPosition(){
		return new PositionValue( position.getX(), position.getY() );
	}	
	
	public void changeWidthTo( double xMin, double xMax ){
		
		this.setBoundBoxXMin(xMin);
		this.setBoundBoxXMax(xMax);
		for( ChangeSizeListener changeWidthListener: changeWidthListenerList ){
			changeWidthListener.changed( xMin, xMax );
		}
	}
	
	public void changeHeightTo( double yMin, double yMax ){
		for( ChangeSizeListener changeHeightListener: changeHeightListenerList ){
			changeHeightListener.changed( yMin, yMax );
		}		
	}
	
	/**
	 * 
	 * @param g2
	 */
	public void draw( MGraphics g2 ){
		
		if( isShadow() ){
			//for( ASpriteElement element: elements){
			//	element.setPosition(position);
			//	element.drawShadow(g2);
			//}			
		}else if( isSelected() ){
			
			for( ASpriteElement element: elements){
				element.setPosition(position);
				element.drawSelected(g2);
			}
			
			for( Magnet magnet: magnetList){
				if( null == magnet.getConnectedTo() ){
					magnet.draw(g2);
				}else{
					magnet.drawSelected(g2);
				}
			}			
		}else if( isConnected() ){
			
			for( ASpriteElement element: elements){
				element.setPosition(position);
				element.drawConnected(g2);
			}
			
			for( Magnet magnet: magnetList){
				if( null == magnet.getConnectedTo() ){
					magnet.draw(g2);
				}else{
					magnet.drawConnected(g2);
				}
			}			
		}else{
			for( ASpriteElement element: elements){
				element.setPosition(position);
				element.draw(g2);
			}			
			for( Magnet magnet: magnetList){		
					magnet.draw(g2);				
			}
		}
	}
	
	/**
	 * 
	 * @param g2
	 */
	public void drawTemporary( MGraphics g2){
		if( isInFocus() ){		
			for( ASpriteElement element: elements){
				element.setPosition(position);
				element.drawFocus(g2);
			}
			for( Magnet magnet: magnetList){
				if( null == magnet.getConnectedTo() ){
					magnet.drawFocus(g2);
				}else{
					magnet.drawConnected(g2);
				}
			}
		}else{
			if( isSelected() ){
			
				for( ASpriteElement element: elements){
					element.setPosition(position);
					element.drawSelected(g2);
				}
				
				for( Magnet magnet: magnetList){
					if( null == magnet.getConnectedTo() ){
						magnet.draw(g2);
					}else{
						magnet.drawSelected(g2);
					}
				}	
			}else{
				for( ASpriteElement element: elements){
					element.setPosition(position);
					element.draw(g2);
				}
				for( Magnet magnet: magnetList){
					if( null == magnet.getConnectedTo() ){
						magnet.draw(g2);
					}else{
						magnet.drawConnected(g2);
					}
				}
			}
		}		
	}
	

/*	public void drawConnected( MGraphics g2){
		for( SpriteElement element: elements){
			element.setPosition(position);
			element.drawConnected(g2);
		}
	}
*/	
	
	public boolean isShadow(){
		return isShadow;
	}
	
	public void setIsShadow( boolean isShadow ){
		this.isShadow = isShadow;
	}
	
	public boolean isSelected(){
		return isSelected;
	}
	
	public void setIsSelected( boolean isSelected ){
		this.isSelected = isSelected;
	}
	
	public boolean isInFocus(){
		return inFocus;
	}
	
	public void setInFocus( boolean inFocus ){
		this.inFocus = inFocus;
	}
	
	public boolean isConnected(){
		boolean isConnected = false;
		for( Magnet magnet: getMagnetList()){
			if( null != magnet.getConnectedTo() ){
				isConnected = true;
				break;
			}
		}
		return isConnected;
	}
	
	public Integer getLevel(){
		return level;
	}
}
