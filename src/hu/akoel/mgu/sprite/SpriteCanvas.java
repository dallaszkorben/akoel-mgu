package hu.akoel.mgu.sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;

import hu.akoel.mgu.MCanvas;
import hu.akoel.mgu.MGraphics;
import hu.akoel.mgu.PainterListener;
import hu.akoel.mgu.PossiblePixelPerUnits;
import hu.akoel.mgu.values.DeltaValue;
import hu.akoel.mgu.values.PositionValue;
import hu.akoel.mgu.values.SizeValue;
import hu.akoel.mgu.values.TranslateValue;

public class SpriteCanvas extends MCanvas{

	private static final long serialVersionUID = -4187978793520279190L;
	
	private ArrayList<Sprite> spriteList = new ArrayList<Sprite>();
	private ArrayList<Sprite> temporarySpriteList = new ArrayList<Sprite>();
	
	private boolean needFocus = true;

	public SpriteCanvas(Border borderType, Color background, PossiblePixelPerUnits possiblePixelPerUnits, TranslateValue positionToMiddle ) {
		super(borderType, background, possiblePixelPerUnits, positionToMiddle );		
		commonConstructor();
	}
	
	public SpriteCanvas(Border borderType, Color background, PossiblePixelPerUnits possiblePixelPerUnits, TranslateValue positionToMiddle, SizeValue boundSize ) {
		super(borderType, background, possiblePixelPerUnits, positionToMiddle, boundSize );
		commonConstructor();
	}
	

	public SpriteCanvas(Border borderType, Color background, SizeValue worldSize) {
		super(borderType, background, worldSize);
		commonConstructor();
	}

	private void commonConstructor(){
		
		//Azt figyeli, hogy egy Sprite fokuszba kerult-e
		this.addMouseMotionListener(new SpriteInFocusListener());
		
		//A kozepso reteget hasznaljuk a sprite-ok megjelenitesere
		addPainterListenerToMiddle(new SpritePainterListener(), Level.ABOVE );
				
		this.addMouseInputListener( new SpriteDragListener() );
		
	}
	
	public void setNeedFocus( boolean needFocus){
		this.needFocus = needFocus;
	}
	
	public boolean needFocus(){
		return needFocus;
	}
	
	public void addSprite( Sprite sprite ){

		if( !spriteList.contains(sprite)){
			spriteList.add(sprite);
		}		
	}
	
	public void removeSprite( Sprite sprite ){
		spriteList.remove( sprite );
	}
	
	public void addTemporarySprite( Sprite sprite ){
		
		//Temporary reteget hasznaljuk a fokus megjelenitesre
		//Minden megjelenites utan torlodik a listener, ezert kell mindig hozzaadni
		addPainterListenerToTemporary(new TemporarySpritePainterListener(), Level.UNDER);	
		
		if( !temporarySpriteList.contains(sprite)){
			temporarySpriteList.add(sprite);
		}	
	}
	
	public void zoomIn(double xCenter, double yCenter, int xPoint, int yPoint){
		super.zoomIn(xCenter, yCenter, xPoint, yPoint);
		
		//Azert kell, hogy a zoom utan kovetkezo ujrarajzolas miatt eltuno fokusz jelzes ujbol kirajzolodjon
		fireMouseMoved();
	}
	
	public void zoomOut(double xCenter, double yCenter, int xPoint, int yPoint){
		super.zoomOut(xCenter, yCenter, xPoint, yPoint);
		
		//Azert kell, hogy a zoom utan kovetkezo ujrarajzolas miatt eltuno fokusz jelzes ujbol kirajzolodjon
		fireMouseMoved();
	}
	
	
	/**
	 * Egy Sprite mozgatasaert felelos osztaly
	 * 
	 * @author akoel
	 *
	 */
	class SpriteDragListener implements MouseInputListener{
		private PositionValue originalPosition;
		private DeltaValue initialDelta;

		private boolean canBeDragged = false;
		private Sprite sprite;
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
		}
		@Override
		public void mousePressed(MouseEvent e) {
			
			canBeDragged = false;
			
			//Ha a baleger gombot nyomtam be
			if( e.getButton() == MouseEvent.BUTTON1){
				
				double xValue = getWorldXByPixel(e.getX() );
				double yValue = getWorldYByPixel(e.getY());
				
				//Akkor meg kell nezni, hogy van-e alatta sprite
				for( Sprite sprite: spriteList){
					
					SizeValue boundBox = sprite.getBoundBox();
						
					//Igen, van alatta sprite
					if( 
							xValue >= boundBox.getXMin() &&
							xValue <= boundBox.getXMax() &&
							yValue >= boundBox.getYMin() &&
							yValue <= boundBox.getYMax()
							){
									
						//Rogton letiltom a fokusz mukodeset
						setNeedFocus(false);
						
						//Elteszem a mozgatando sprite-ot, hogy a drag muvelet tudja mivel dolgozzon
						this.sprite = sprite;
												
						//El kell menteni az eredeti poziciojat
						originalPosition = sprite.getPosition();
						
						//Megallapitani a kulonbseget a Sprite pozicioja es a kurzor kozott
						initialDelta = new DeltaValue( xValue - originalPosition.getX(), yValue - originalPosition.getY() );
						
						//El kell tavolitani a.... ghost...
						removeSprite(sprite);
						
						//El kell helyezni az atmeneti taroloba az uj pozicioval
						
						addTemporarySprite(sprite);	
						sprite.setPosition(new PositionValue(xValue-initialDelta.getX(), yValue-initialDelta.getY()));
						revalidateAndRepaintCoreCanvas();
						//repaintCoreCanvas();

						
						canBeDragged = true;
					
						
						break;

					}					
				}				
			}
			
		}
		@Override
		public void mouseReleased(MouseEvent e) {	

			//Jelzem, hogy vege a dragg-nek
			canBeDragged = false;
			
			//Engedelyezem a fokusz mukodeset
			setNeedFocus(true);
			
			//El kell helyezni a vegleges taroloba az uj pozicioval			
			addSprite(sprite);
			
			//Es meg kell jeleniteni
			revalidateAndRepaintCoreCanvas();
			
			//Es mivel fokuszban lesz ezert egy egermozgast is szimulalni kell
			fireMouseMoved();

			
		}
		@Override
		public void mouseEntered(MouseEvent e) {			
		}
		@Override
		public void mouseExited(MouseEvent e) {	
		}
		@Override
		public void mouseDragged(MouseEvent e) {
		
			if( canBeDragged ){
				
				double xValue = getWorldXByPixel(e.getX() );
				double yValue = getWorldYByPixel(e.getY());
				
				addTemporarySprite(sprite);	
				sprite.setPosition(new PositionValue(xValue-initialDelta.getX(), yValue-initialDelta.getY()));
				repaintCoreCanvas();
			}
			
		}
		@Override
		public void mouseMoved(MouseEvent e) {
		}
		
	}
	
	/**
	 * Azt figyeli, hogy egy Sprite fokuszba kerult-e
	 * @author akoel
	 *
	 */
	class SpriteInFocusListener implements MouseMotionListener{
			
			@Override
			public void mouseMoved(MouseEvent e) {
				
				
				if( needFocus() ){
			
					double xValue = getWorldXByPixel(e.getX() );
					double yValue = getWorldYByPixel(e.getY());
					boolean needToPrint = false;

					for( Sprite sprite: spriteList){
				
						SizeValue boundBox = sprite.getBoundBox();
						
			
						if( 
							xValue >= boundBox.getXMin() &&
							xValue <= boundBox.getXMax() &&
							yValue >= boundBox.getYMin() &&
							yValue <= boundBox.getYMax()
							){
											
							addTemporarySprite(sprite);						
							needToPrint = true;
							sprite.setInFocus(true);

						}else{
							if( sprite.isInFocus() ){
								needToPrint = true;						
								sprite.setInFocus(false);
							}
						}					
					}
					if( needToPrint ){
						repaintCoreCanvas();
					}
				}
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {				
			}		
	}
	
	/**
	 * Sprite-ok kirajzolasaert felelos osztaly
	 * 
	 * @author akoel
	 *
	 */
	class SpritePainterListener implements PainterListener{

		@Override
		public void paintByWorldPosition(MCanvas canvas, MGraphics g2) {
			for( Sprite sprite: spriteList){
				sprite.draw(g2);
			}			
		}
		@Override
		public void paintByViewer(MCanvas canvas, Graphics2D g2) {}
		
	}
	
	/**
	 * Az atmeneti retegben elhelyezett sprite-ok kirajzolasaert felelos
	 * 
	 * @author akoel
	 *
	 */
	class TemporarySpritePainterListener implements PainterListener{

		@Override
		public void paintByWorldPosition(MCanvas canvas, MGraphics g2) {
			for( Sprite sprite: temporarySpriteList){
				sprite.drawFocus(g2);
			}
			temporarySpriteList.clear();
		}		

		@Override
		public void paintByViewer(MCanvas canvas, Graphics2D g2) {}
		
	}
}
