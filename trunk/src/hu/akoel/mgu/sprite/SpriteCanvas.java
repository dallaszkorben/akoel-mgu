package hu.akoel.mgu.sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
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
	
	private SpriteInFocusListener spriteInFocusListener = new SpriteInFocusListener();
	private SpriteDragListener spriteDragListener = new SpriteDragListener();
	
	private boolean needFocus = true;

	public SpriteCanvas(Border borderType, Color background, PossiblePixelPerUnits possiblePixelPerUnits, TranslateValue positionToMiddle ) {
		super(borderType, background, possiblePixelPerUnits, positionToMiddle );		
		commonConstructor();
	}
	
	public SpriteCanvas(Border borderType, Color background, PossiblePixelPerUnits possiblePixelPerUnits, TranslateValue positionToMiddle, SizeValue boundSize ) {
		super(borderType, background, possiblePixelPerUnits, positionToMiddle, boundSize );
		commonConstructor();
	}

	private void commonConstructor(){
		
		//Azt figyeli, hogy egy Sprite fokuszba kerult-e
		this.addMouseMotionListener(spriteInFocusListener);
		
		//A kozepso reteget hasznaljuk a sprite-ok megjelenitesere
		addPainterListenerToMiddle(new SpritePainterListener(), Level.ABOVE );
		
		//Egy SPrite mozgatasat figyeli
		this.addMouseInputListener( spriteDragListener );
		
	}
	
	
	
	
	
	public void fireMouseDragged(){
		
		int x = MouseInfo.getPointerInfo().getLocation().x-getCoreCanvasLocationOnScreen().x;
		int y = MouseInfo.getPointerInfo().getLocation().y-getCoreCanvasLocationOnScreen().y;
		
		MouseEvent me = new MouseEvent(coreCanvas, 11, 0, 0, x+1, y, 1, false);
		
		spriteDragListener.mouseDragged(me);	
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
		
		//Azert kell, hogy a zoom utan kovetkezo ujrarajzolas miatt eltuno fokuszban levo Sprite ujra bekeruljon a temporary listaba
		fireMouseDragged();
		//repaintCoreCanvas();
	}
	
	public void zoomOut(double xCenter, double yCenter, int xPoint, int yPoint){
		super.zoomOut(xCenter, yCenter, xPoint, yPoint);
		
		//Azert kell, hogy a zoom utan kovetkezo ujrarajzolas miatt eltuno fokuszban levo Sprite ujra bekeruljon a temporary listaba
		fireMouseDragged();
		//repaintCoreCanvas();
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

		private boolean draggStarted = false;
		private Sprite sprite;
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
		}
		@Override
		public void mousePressed(MouseEvent e) {
			
			//Mar egy elindult Drag folyamatot nem indithatok ujra el
			if( draggStarted ){
				return;
			}
			
			draggStarted = false;
			
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
									
						//Rogton letiltom a fokusz mukodeset TODO erre azt hiszem nincs szukseg
						setNeedFocus(false);						
						
						//Elteszem a mozgatando sprite-ot, hogy a drag muvelet tudja mivel dolgozzon
						this.sprite = sprite;
												
						//El kell menteni az eredeti poziciojat
						originalPosition = sprite.getPosition();
					
						//Megallapitani a kulonbseget a Sprite pozicioja es a kurzor kozott
						initialDelta = new DeltaValue( xValue - originalPosition.getX(), yValue - originalPosition.getY() );
						
						//Eltavolitom a permanens listabol
						removeSprite(sprite);
						
						//El kell helyezni az atmeneti taroloba az uj pozicioval
						
						addTemporarySprite(sprite);	
						sprite.setPosition(new PositionValue(xValue-initialDelta.getX(), yValue-initialDelta.getY()));
						revalidateAndRepaintCoreCanvas();
						//repaintCoreCanvas();

						
						draggStarted = true;
					
						
						break;

					}					
				}				
			}
			
		}
		@Override
		public void mouseReleased(MouseEvent e) {	


			if( draggStarted ){
			
				//Engedelyezem a fokusz mukodeset
				setNeedFocus(true);
			
				//Ha nem csatlakozik mas elemhez de nincs engedelyezve kapcsolat nelkuli lehelyezesre
				if( !sprite.isConnected() && !sprite.isEnableToPlaceWithoutConnection() ){

					//Akkor vissza kell helyezni az elozo pozicioba
					//Visszairom az eredeti poziciot
					sprite.setPosition( originalPosition );
					
				}
					
				//El kell helyezni a vegleges taroloba az uj pozicioval			
				addSprite(sprite);
			
				//Es meg kell jeleniteni
				revalidateAndRepaintCoreCanvas();
			
				//Es mivel fokuszban lesz ezert egy egermozgast is szimulalni kell
				fireMouseMoved();
			}
			
			//Es jelzem, hogy vege a dragg-nek. MINDENKEPPEN
			draggStarted = false;
			
		}
		@Override
		public void mouseEntered(MouseEvent e) {			
		}
		
		@Override
		public void mouseExited(MouseEvent e) {	

			boolean needToPrint = false;
			
			//Ha elindult mar a mozgatas
			if( draggStarted ){
				
				//Ujra engedelyezem a fokusz mukodeset
				setNeedFocus(true);
				
				//Jelzem, hogy vege a dragg-nek
				draggStarted = false;
			
				//Visszairom az eredeti poziciot
				sprite.setPosition( originalPosition );
				
				//Visszahelyezem a vegleges taroloba az eredeti pozicioval			
				addSprite(sprite);
				
				//Es meg kell jeleniteni
				revalidateAndRepaintCoreCanvas();
				
			//Csak siman kisetalt a kurzor a kepernyorol
			}else{
				
				//Vegig az osszes Sprite-on
				for( Sprite sprite: spriteList){
					
					//Ha az adot Sprite fokuszban volt
					if( sprite.isInFocus() ){
						
						//Kiszedjuk a fokuszbol
						sprite.setInFocus(false);
						
						//Es jelzem, hogy legalabb egy elem volt fokuszban
						needToPrint = true;						
							
					}										
				}
				
				if( needToPrint ){			
				
					//Azert kell, hogy az esetlegesen fokuszban levo Sprite-rol eltunjon a fokusz
					repaintCoreCanvas();

				}			
			}			
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
		
			if( draggStarted ){
				
				//Uj pozicioi kiszamitasa
				double xCursorPosition = getWorldXByPixel(e.getX() );
				double yCursorPosition = getWorldYByPixel(e.getY());
				
				//A mozgatando Sprite Elozetes uj pozicioba helyezese
				sprite.setPosition(new PositionValue(xCursorPosition-initialDelta.getX(), yCursorPosition-initialDelta.getY()));
				
				boolean hasConnection = false;
				
				//Vegig megyek a mozgatott sprite magnesein
				for( Magnet draggedMagnet: sprite.getMagnetList() 	){
					
					//Az aktualis magnes tulajdonsagai
					double magnetXRange = getWorldXLengthByPixel( draggedMagnet.getRangeInPixel().getX() );
					double magnetYRange = getWorldYLengthByPixel( draggedMagnet.getRangeInPixel().getY() );
					
					//Megnezem, hogy az aktualis magnes hatotavolsagaban, van-e egyaltalan masik sprite
					double xMagnetPosition = sprite.getPosition().getX() + draggedMagnet.getPosition().getX();
					double yMagnetPosition = sprite.getPosition().getY() + draggedMagnet.getPosition().getY();
					
					//Vegig az osszes Sprite-on
					for( Sprite possibleToConnectSprite: spriteList){
						
						SizeValue boundBox = possibleToConnectSprite.getBoundBox();
							
						//Ha Van a vonzaskorzetben egyaltalan Sprite
						if( 
								( (xMagnetPosition - magnetXRange >= boundBox.getXMin() &&
								xMagnetPosition - magnetXRange <= boundBox.getXMax() ) ||
								( xMagnetPosition + magnetXRange >= boundBox.getXMin() &&
								xMagnetPosition + magnetXRange <= boundBox.getXMax() ) )&&
										
								( (yMagnetPosition - magnetYRange >= boundBox.getYMin() &&
								yMagnetPosition - magnetYRange <= boundBox.getYMax() ) ||
								( yMagnetPosition + magnetYRange >= boundBox.getYMin() &&
								yMagnetPosition + magnetYRange <= boundBox.getYMax() ) )
						){
							
							//Akkor vegig megyek a megtalalt Sprite Magnet-jeint
							for( Magnet possibleToConnectMagnet : possibleToConnectSprite.getMagnetList() ){
								
								MagnetType possibleToConnectType = possibleToConnectMagnet.getType();
								double possibleToConnectXPosition = possibleToConnectSprite.getPosition().getX() + possibleToConnectMagnet.getPosition().getX();
								double possibleToConnectYPosition = possibleToConnectSprite.getPosition().getY() + possibleToConnectMagnet.getPosition().getY();
								
								//Es megnezem, hogy a ket magnet kompatibilis-e es megfelelo pozicioban van-e valamint nincs-e racsatlakoztatva mas
								if( 
										draggedMagnet.getPossibleMagnetTypeToConnect().contains( possibleToConnectType ) &&
										possibleToConnectMagnet.getPossibleMagnetTypeToConnect().contains( draggedMagnet.getType() ) &&
										( Math.abs( draggedMagnet.getDirection() - possibleToConnectMagnet.getDirection() ) == 180.0 ) &&
										possibleToConnectXPosition >= xMagnetPosition - magnetXRange &&
										possibleToConnectXPosition <= xMagnetPosition + magnetXRange &&
										possibleToConnectYPosition >= yMagnetPosition - magnetYRange &&
										possibleToConnectYPosition <= yMagnetPosition + magnetYRange &&
										( null == possibleToConnectMagnet.getConnectedTo() || possibleToConnectMagnet.getConnectedTo().equals(draggedMagnet) )
										
										
								){
									
									//double differenceX = xMagnetPosition - possibleToConnectXPosition;
									//double differenceY = yMagnetPosition - possibleToConnectYPosition;
																		
									//A mozgatando Sprite Elozetes uj pozicioba helyezese
									//sprite.setPosition(new PositionValue(possibleToConnectXPosition - magnet.getPosition().getX(), possibleToConnectYPosition - magnet.getPosition().getY() ));
									
									draggedMagnet.setConnectedTo( possibleToConnectMagnet );
									
									hasConnection = true;
									break;
									
								}								
							}
						}
						
						if( hasConnection ){
							break;
						}
					}
					
					if( hasConnection){
						break;
					}					
									
				}
				
				if( !hasConnection ){
					
					for( Magnet magnet: sprite.getMagnetList()){
						magnet.setConnectedTo(null);
					}
					
					
				}
				
				//A mozgatando Sprite uj pozicioval elhelyezese az atmeneti taroloban
				addTemporarySprite(sprite);
				
				//Csak az atmeneti tarolo ujrarajzolasa az uj pozicioban levo Sprite miatt
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
				//if( sprite.isConnected() )
				//	sprite.drawConnected(g2);
				//else
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
