package hu.akoel.mgu.sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashSet;

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
	
	
	
	
/*	
	public void fireMouseDragged(){		
		int x = MouseInfo.getPointerInfo().getLocation().x-getCoreCanvasLocationOnScreen().x;
		int y = MouseInfo.getPointerInfo().getLocation().y-getCoreCanvasLocationOnScreen().y;		
		MouseEvent me = new MouseEvent(coreCanvas, 11, 0, 0, x, y, 1, false);		
		spriteDragListener.mouseDragged(me);	
	}
*/	
	
	
	
	
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
		
		fireMouseMoved();
		spriteDragListener.loadSpriteToTemporary();
		repaintCoreCanvas();
	}
	
	public void zoomOut(double xCenter, double yCenter, int xPoint, int yPoint){
		super.zoomOut(xCenter, yCenter, xPoint, yPoint);
		
		//Azert kell, hogy a zoom utan kovetkezo ujrarajzolas miatt eltuno fokuszban levo Sprite ujra bekeruljon a temporary listaba

		fireMouseMoved();
		spriteDragListener.loadSpriteToTemporary();
		repaintCoreCanvas();
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
		
		public void loadSpriteToTemporary(){
			if( draggStarted ){
				addTemporarySprite(sprite);	
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			
			//Mar egy elindult Drag folyamatot nem indithatok ujra el
			if( draggStarted ){
				return;
			}
			
			//draggStarted = false;
			
			//Ha a bal-eger gombot nyomtam be
			if( e.getButton() == MouseEvent.BUTTON1){
				
				double xValue = getWorldXByPixel(e.getX() );
				double yValue = getWorldYByPixel(e.getY());
				
				//Akkor meg kell nezni, hogy van-e alatta sprite
				for( Sprite sprite: spriteList){
					
					SizeValue boundBox = sprite.getBoundBoxAbsolute();
						
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
						//sprite.setPosition(new PositionValue(xValue-initialDelta.getX(), yValue-initialDelta.getY()));
						sprite.setPosition( new PositionValue(originalPosition.getX(), originalPosition.getY()) );

						
						addTemporarySprite(sprite);	
						
						revalidateAndRepaintCoreCanvas();
						
						draggStarted = true;
						
						break;

					}					
				}				
			}
			
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {	

			if( draggStarted ){
						
				//Ha nem csatlakozik mas elemhez de nincs engedelyezve kapcsolat nelkuli lehelyezesre
				if( !sprite.isConnected() && !sprite.isEnableToPlaceWithoutConnection() ){

					//Akkor vissza kell helyezni az elozo pozicioba
					//Visszairom az eredeti poziciot
//					sprite.setPosition( originalPosition );
					
					//General egy Drag muveletet vissza az eredeti pozicioba
					MouseEvent me = new MouseEvent(coreCanvas, 11, 0, 0, Math.round((float)getMouseXPositionByWorld(originalPosition.getX() + initialDelta.getX())), Math.round((float)getMouseYPositionByWorld(originalPosition.getY() + initialDelta.getY())), 1, false);									
					spriteDragListener.mouseDragged(me);					
				
				}
					
				//El kell helyezni a vegleges taroloba az uj pozicioval			
				addSprite(sprite);
			
				//Engedelyezem a fokusz mukodeset
				setNeedFocus(true);
				
				//Es ujra kell rajzoltatni a Permanens lista elemeit
				revalidateAndRepaintCoreCanvas();
			
				//Es mivel fokuszbanlehet meg tovabbra is, ezert egy eger-mozgast is szimulalni kell
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
						
			//Ha elindult mar a drag es igy hagyom el a Canvas-t
			if( draggStarted ){
				
				mouseReleased( e );
				
			//Csak siman kisetalt a kurzor a kepernyorol, akkor minden Sprite-rol toroljuk a fokuszt
			}else{
				
				boolean needToReprint = false;
				
				//Vegig az osszes Sprite-on
				for( Sprite sprite: spriteList){
					
					//Ha az adot Sprite fokuszban volt
					if( sprite.isInFocus() ){
						
						//Kiszedjuk a fokuszbol
						sprite.setInFocus(false);
						
						//Es jelzem, hogy legalabb egy elem volt fokuszban
						needToReprint = true;						
							
					}										
				}
				
				if( needToReprint ){			
				
					//Azert kell, hogy az esetlegesen fokuszban levo Sprite-rol eltunjon a fokusz
					repaintCoreCanvas();

				}			
			}						
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
		
			if( draggStarted ){
				
				//
				//Azt vizsgalja, hogy az adott Sprite elmozdithato-e egyaltalan a helyerol
				//
				boolean moveable = true;
				int number;
				HashSet<Sprite> checkedSpriteList;
				
				//vegig a Sprite minden magnesen
				for( Magnet magnet: sprite.getMagnetList() ){
					
					//Hogyha van kapcsolata az aktualis magnet-nek
					if( null != magnet.getConnectedTo() ){
						number = 0;
						checkedSpriteList = new HashSet<Sprite>();
						checkedSpriteList.add(sprite);
						
						//Akkor megnezi, hogy hagy db onalloan elhelyezheto Sprite letezik Sprite adott magnes feloli oldalan
						number = getNumbersOfEnableToPlaceWithoutConnection(magnet, number, checkedSpriteList );
						
						//Ha a kerdeses Sprite-on kivul nem letezik onalloan lehelyezheto Sprite a magnes feloli oldalon
						if( number == 0 ){
							
							//Akkor megtiltja az mozgatni kivant Sprite elmozditasat
							moveable = false;
							break;
						}
					}
				}
				if( !moveable ){
					return;
				}
				
				
				//Uj pozicioi kiszamitasa
				double xCursorPosition = getWorldXByPixel(e.getX() );
				double yCursorPosition = getWorldYByPixel(e.getY());
				
				//A mozgatando Sprite Elozetes uj pozicioba helyezese
				sprite.setPosition(new PositionValue(xCursorPosition-initialDelta.getX(), yCursorPosition-initialDelta.getY()));
				
				boolean needToRepaintPermanent = false;
				boolean hasBeenFoundPairForTheSprite = false;

			//Eloszor is torlom a mozgatot Sprite minden kapcsolatat					
				if( sprite.isConnected() ){

					//Az osszes magnesen vegig megyek
					for( Magnet magnet: sprite.getMagnetList()){
						
						//Es megszuntetem a kapcsolatait
						magnet.setConnectedTo(null);
					}
						
					//Es mivel egesz biztos, hogy volt kapcsolata amit torloltem (sprite.isConnected()), jelzem, hogy ujra kell rajzolni a permanens listat
					needToRepaintPermanent = true;
				
				}
				
				//Vegig megyek a mozgatott Sprite magnesein
				for( Magnet draggedMagnet: sprite.getMagnetList() 	){
					
					//Az aktualis magnes tulajdonsagai
					double magnetXRange = getWorldXLengthByPixel( draggedMagnet.getRangeInPixel().getX() );
					double magnetYRange = getWorldYLengthByPixel( draggedMagnet.getRangeInPixel().getY() );
					
					//Megnezem, hogy az aktualis magnes hatotavolsagaban, van-e egyaltalan masik sprite
					double xMagnetPosition = sprite.getPosition().getX() + draggedMagnet.getRelativePositionToSpriteZero().getX();
					double yMagnetPosition = sprite.getPosition().getY() + draggedMagnet.getRelativePositionToSpriteZero().getY();
					
					boolean hasMagnetConnection = false;
					
					//Vegig az osszes permanens Sprite-on
					for( Sprite possibleToConnectSprite: spriteList){
						
						SizeValue boundBox = possibleToConnectSprite.getBoundBoxAbsolute();
							
						//Ha Van a vonzaskorzetben egyaltalan Sprite
						if( 
								( 
								(xMagnetPosition - magnetXRange >= boundBox.getXMin() &&
								xMagnetPosition - magnetXRange <= boundBox.getXMax() ) ||
								( xMagnetPosition + magnetXRange >= boundBox.getXMin() &&
								xMagnetPosition + magnetXRange <= boundBox.getXMax() ) ||
								( boundBox.getXMin()  >= xMagnetPosition - magnetXRange &&
								boundBox.getXMin() <= xMagnetPosition + magnetXRange &&
								boundBox.getXMax() >= xMagnetPosition - magnetXRange &&
								boundBox.getXMax() <= xMagnetPosition + magnetXRange)
								
								)&&
										
								( 
								(yMagnetPosition - magnetYRange >= boundBox.getYMin() &&
								yMagnetPosition - magnetYRange <= boundBox.getYMax() ) ||
								( yMagnetPosition + magnetYRange >= boundBox.getYMin() &&
								yMagnetPosition + magnetYRange <= boundBox.getYMax() ) ||
								( boundBox.getYMin()  >= yMagnetPosition - magnetYRange &&
								boundBox.getYMin() <= yMagnetPosition + magnetYRange &&
								boundBox.getYMax() >= yMagnetPosition - magnetYRange &&
								boundBox.getYMax() <= yMagnetPosition + magnetYRange)
								)
						){
							
							//Akkor vegig megyek a megtalalt Sprite Magnet-jeint
							for( Magnet possibleToConnectMagnet : possibleToConnectSprite.getMagnetList() ){
								
								MagnetType possibleToConnectType = possibleToConnectMagnet.getType();
								double possibleToConnectXPosition = possibleToConnectSprite.getPosition().getX() + possibleToConnectMagnet.getRelativePositionToSpriteZero().getX();
								double possibleToConnectYPosition = possibleToConnectSprite.getPosition().getY() + possibleToConnectMagnet.getRelativePositionToSpriteZero().getY();
								
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
									
									//Ha mar talaltam part a Sprite-nak, vagyis nem ez az elso 
									if( hasBeenFoundPairForTheSprite ){
										
										//Akkor meg kell nezni, hogy mozgatas nelkul osszekapcsolhato-e a ketto
										PositionValue possibleNewPosition = draggedMagnet.getPossibleSpritePosition(possibleToConnectMagnet);
										
										//Ha nem kapcsolhato ossze, akkor zarja a ciklust
										if( !possibleNewPosition.equals(sprite.getPosition())){

											//zarja a ciklust
											continue;										
										}

									}
									
									//A mozgatott Sprite vizsgalt magneset osszekoti az osszekapcsolhato magnessel
									draggedMagnet.setConnectedTo( possibleToConnectMagnet );
								
									hasBeenFoundPairForTheSprite = true;
									needToRepaintPermanent = true;
									hasMagnetConnection = true;
									break;
									
								}								
							}
						}
						
						if( hasMagnetConnection ){
							break;
						}
					}
					
					//if( hasConnection){
					//	break;
					//}					
									
				}
						
				//A mozgatando Sprite uj pozicioval elhelyezese az atmeneti taroloban
				addTemporarySprite(sprite);
				
				//A Permanens es atmeneti taroloban levo Sprite-ok ujrarajzolasa szukseges
				if( needToRepaintPermanent ){
					revalidateAndRepaintCoreCanvas();
				}else{
					//Csak az atmeneti tarolo ujrarajzolasa az uj pozicioban levo Sprite miatt
					repaintCoreCanvas();
				}
			}
			
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
		}
		
	}
	
	int getNumbersOfEnableToPlaceWithoutConnection( Magnet magnet, int number, HashSet<Sprite> spriteList ){
		
		Magnet pair = magnet.getConnectedTo();
		
		//Nincs a magnet-nek parja
		if( null == pair ){
			return number;
		}
		
		//A magnes masik oldalan levo Sprite
		Sprite pairParent = pair.getParent();
		
		//Ha mar vizsgaltam ezt a Sprite-to
		if( spriteList.contains(pairParent)){
			return number;
		}
		
		spriteList.add( pairParent);
		
		//Ha lehelyezheto onalloan, akkor noveli a szmalalot
		if( pairParent.isEnableToPlaceWithoutConnection() ){
			number++;
		}
		
		//vegig az o magnesein
		for( Magnet m: pairParent.getMagnetList()){
			
			//Megszamolja, hogy arrafele hany onalloan lehelyezheto Sprite van 
			number = getNumbersOfEnableToPlaceWithoutConnection(m, number, spriteList);
			
		}
		
		return number;
		
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
				
					SizeValue boundBox = sprite.getBoundBoxAbsolute();						
			
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
		public void mouseDragged(MouseEvent e) {}		
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
