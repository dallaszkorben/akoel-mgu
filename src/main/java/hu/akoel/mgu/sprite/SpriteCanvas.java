package hu.akoel.mgu.sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;

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

/**
 * -The original problem is that Sprites could be ON-EACH other
 * -In that case we should make different between LEVELS of Sprites
 * -The Idea is that the Sprites are in a SpriteListOnLevel list/set. 
 *  We suppose that the Sprites on this list do not overlap each other
 * -Finally the SpriteListOnLevel list are in the SpriteSet.
 * -When we draw the Sprites from the SpriteSet then we start to draw with the lowest level SpriteListOnLevel list
 * 
 * Focus: Check if a Sprite is under  the cursor position
 *        Start the check on the highest SpriteListOnLevel list.
 *        When you found the first one, then set it's isInFocus variable to true
 *        
 * Selection: Check if a Sprite is under  the CLICKED cursor position
 *        Start the check on the highest SpriteListOnLevel list.
 *        When you found the first one, then set it's isSelected variable to true
 *        
 *        Needed a new Appearance for Sprite "Focus+Selected" for that case
 *        Needed a new variable for Sprite "isMultipleSelectionEnabled"
 *        
 * @author akoel
 *
 */
public class SpriteCanvas extends MCanvas{    
	    
	private static final long serialVersionUID = -4187978793520279190L;
	
	private SpriteContainer spriteSet = new SpriteContainer();
	private SpriteContainer temporarySpriteSet = new SpriteContainer();
	
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
		addPainterListenerToMiddle(new PermanentSpritePainterListener(), Level.ABOVE );
		
		//Egy SPrite mozgatasat figyeli
		this.setMouseInputListener( spriteDragListener );
		
	}
		
	public void setNeedFocus( boolean needFocus){
		this.needFocus = needFocus;
	}
	
	public boolean needFocus(){
		return needFocus;
	}
		
	public void addSprite( Sprite sprite ){
	    spriteSet.addSprite(sprite);
	}
	
	public void addSprites( HashSet<Sprite> spriteList ){
		spriteSet.addSprites(spriteList);
	}	

	public void removeSprites( HashSet<Sprite> spriteList ){
		spriteSet.removeSprites(spriteList);
	}
	
	public void removeSprite( Sprite sprite ){
		spriteSet.removeSprite(sprite);
	}		

	public void addTemporarySprite( Sprite sprite ){
		
		//Temporary reteget hasznaljuk a fokus megjelenitesre
		//Minden megjelenites utan torlodik a listener, ezert kell mindig hozzaadni
		addPainterListenerToTemporary(new TemporarySpritePainterListener(), Level.UNDER);	
		
		//To reset the Temporary Position
//		sprite.copyPermanentPositionToTemporary();
		
		temporarySpriteSet.addSprite(sprite);
	}
	
	public void addTemporarySprites( HashSet<Sprite> spriteList ){
		
		//Temporary reteget hasznaljuk a fokus megjelenitesre
		//Minden megjelenites utan torlodik a listener, ezert kell mindig hozzaadni
		addPainterListenerToTemporary(new TemporarySpritePainterListener(), Level.UNDER);	

		for( Sprite sprite: spriteList ){	
			
			//To reset the Temporary Position
//			sprite.copyPermanentPositionToTemporary();
			
			temporarySpriteSet.addSprite( sprite );
		}
	}
	
/*	public void copyTemporaryPositionsToPermanent( HashSet<Sprite> spriteList ){
		
		for( Sprite sprite: spriteList ){	
			
			//To reset the Temporary Position
			sprite.copyTemporaryPositionToPermanent();
			
		}
	}
*/	
	private void setShadow( HashSet<Sprite> spriteList, boolean value ){
		for( Sprite sprite: spriteList ){		
			sprite.setIsShadow( value );
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
	 * 
	 * Sprite mozgatasaert felelos osztaly
	 * 
	 * @author akoel
	 *
	 */
	class SpriteDragListener implements MouseInputListener{
		private PositionValue originalPosition;
		private DeltaValue initialDelta;

		private boolean dragOneSpriteStarted = false;
		private boolean dragAllSpriteStarted = false;
		private Sprite sprite;
		
		//SpriteContainer moveableSpriteSet;
		//private Map<Integer, HashSet<Sprite>> moveableSpriteSet;
		private HashSet<Sprite> moveableSpriteSet;
		
		public void loadSpriteToTemporary(){
			if( dragOneSpriteStarted ){
				addTemporarySprite(sprite);	
			}
		}
		

		public void mousePressed(MouseEvent e) {
			
			//Mar egy elindult Drag-Replace folyamatot nem indithatok ujra el
			if( dragOneSpriteStarted || dragAllSpriteStarted ){
				return;
			}
			
			//
			//Ha a bal-eger gombot nyomtam le a Shift-tel - A teljes kapcsolati rendszert akarom athelyezni
			//
			if( e.getButton() == MouseEvent.BUTTON1 && e.isShiftDown() ){
				
				double xValue = getWorldXByPixel(e.getX() );
				double yValue = getWorldYByPixel(e.getY());
				
				//Check if there is Sprite under the mouse cursor
				Sprite sprite = spriteSet.getHighestSpriteInPosition( xValue, yValue );
				
				//There was a Sprite under the mouse cursor
				if( null != sprite ){
									
					//Rogton letiltom a fokusz mukodeset TODO erre azt hiszem nincs szukseg
					setNeedFocus(false);						
						
					//Elteszem a mozgatando sprite-ot, hogy a drag muvelet tudja mivel dolgozzon
					this.sprite = sprite;
												
					//El kell menteni az eredeti poziciojat
					originalPosition = sprite.getPosition();
					
					//Megallapitani a kulonbseget a Sprite pozicioja es a kurzor kozott
					//initialDelta = new DeltaValue( xValue - originalPosition.getX(), yValue - originalPosition.getY() );
					initialDelta = new DeltaValue( xValue - sprite.getPosition().getX(), yValue - sprite.getPosition().getY() );
						
					moveableSpriteSet = new HashSet<Sprite>();
					moveableSpriteSet = getConnectedSprites( sprite, moveableSpriteSet );					
					dragAllSpriteStarted = true;
					
					//Set Sprite into Shadow state - instead of removing
					setShadow( moveableSpriteSet, true );
						
					//El kell helyezni az atmeneti taroloba az uj pozicioval						
					//sprite.setPosition(new PositionValue(xValue-initialDelta.getX(), yValue-initialDelta.getY()));
					sprite.setPosition( new PositionValue(originalPosition.getX(), originalPosition.getY()) );
//sprite.setTemporaryPosition( new PositionValue( originalPosition.getX(), originalPosition.getY()) );	
					
					addTemporarySprites( moveableSpriteSet );	
						
					revalidateAndRepaintCoreCanvas();						
				}	
			//
			//Ha a bal-eger gombot nyomtam le - At akarom helyezni a Sprite-ot
			//
			}else if( e.getButton() == MouseEvent.BUTTON1 ){
				
				double xValue = getWorldXByPixel(e.getX() );
				double yValue = getWorldYByPixel(e.getY());
				
				//Akkor meg kell nezni, hogy van-e alatta sprite
				Sprite sprite = spriteSet.getHighestSpriteInPosition( xValue, yValue );
				
				//There was a Sprite under the mouse cursor
				if( null != sprite ){
				
					//Rogton letiltom a fokusz mukodeset TODO erre azt hiszem nincs szukseg
					setNeedFocus(false);						
						
					//Elteszem a mozgatando sprite-ot, hogy a drag muvelet tudja mivel dolgozzon
					this.sprite = sprite;
												
					//El kell menteni az eredeti poziciojat
					originalPosition = sprite.getPosition();
					
					//Megallapitani a kulonbseget a Sprite pozicioja es a kurzor kozott
					initialDelta = new DeltaValue( xValue - originalPosition.getX(), yValue - originalPosition.getY() );
					
					//Set the Sprite into Shadow State
					sprite.setIsShadow(true);						
						
					//El kell helyezni az atmeneti taroloba az uj pozicioval						
					//sprite.setPosition(new PositionValue(xValue-initialDelta.getX(), yValue-initialDelta.getY()));
					sprite.setPosition( new PositionValue(originalPosition.getX(), originalPosition.getY()) );						
					//sprite.setTemporaryPosition( new PositionValue( originalPosition.getX(), originalPosition.getY()) );
					addTemporarySprite(sprite);	
						
					revalidateAndRepaintCoreCanvas();
						
					dragOneSpriteStarted = true;										
				}				
			}
			
		}
		
		public void mouseDragged(MouseEvent e) {
			
			//
			//Sprite csoport mozgatasa
			//
			if( dragAllSpriteStarted ){

				//Uj pozicioi kiszamitasa
				double xCursorPosition = getWorldXByPixel(e.getX() );
				double yCursorPosition = getWorldYByPixel(e.getY());

				double xNewSpritePosition = xCursorPosition - initialDelta.getX();
				double yNewSpritePosition = yCursorPosition - initialDelta.getY();
				
				double xDelta = xNewSpritePosition - sprite.getPosition().getX();
				double yDelta = yNewSpritePosition - sprite.getPosition().getY();

				//Az osszes Sprite-ot a mozgatando listabol a megfelelo helyre pozicionalok
				for( Sprite sprite: moveableSpriteSet ){
				
					//A mozgatando Sprite Elozetes uj pozicioba helyezese
					sprite.setPosition(new PositionValue(sprite.getPosition().getX() + xDelta,sprite.getPosition().getY() + yDelta));
/*					sprite.setTemporaryPosition(new PositionValue(
							sprite.getPermanentPosition().getX() + xDelta, 
							sprite.getPermanentPosition().getY() + yDelta
							)
					);*/
				}
			
				//Torlom a Sprite csoport minden kapcsolatat ami nem a csoporton belul kottetett
				//Tulajdonkeppen a lehetseges kapcsolatokrol van szo
				for( Sprite sprite: moveableSpriteSet ){
					for( Magnet magnet: sprite.getMagnetList() ){
						Magnet pairMagnet = magnet.getConnectedTo();
						
						//Kivulre mutato kapcsolat
						if( null != pairMagnet && !moveableSpriteSet.contains( pairMagnet.getParent() ) ){
							
							//Torolni kell a kapcsolatot
							magnet.setConnectedTo(null);
						}
					}
				}
				
				//Poziciok szukseg szerinti korrigalasa a magnes alapjan
				doArangeBlockPositionByMagnet( moveableSpriteSet );
				
				//A mozgatando Sprite uj pozicioval elhelyezese az atmeneti taroloban
				addTemporarySprites( moveableSpriteSet );
				
				//A Permanens es atmeneti taroloban levo Sprite-ok ujrarajzolasa szukseges
				revalidateAndRepaintCoreCanvas();
				
			/**
			 *  Egy Sprite mozgatas 
			 * 
			 *  Azert teszunk kulonbseget az egy illetve csoportos Sprite mozgatasa kozott, mert az egy Sprite mozgatasakor meg kell vizsgalni
			 *  hogy lehetseges-e az illeto Sprite kapcsolatainak megszuntetese anelkul, hogy serulne-e az onalloan lehelyezheto/nem lehelyezheto Sprite szabaly
			 *  Vagyis, ha pld a kapcsolati rendszerben letezik egy db onalloan lehelyezheto Sprite es a tobbi hozzakapcsolodo Sprite az csak kapcsolatban
			 *  helyezheto le, akkor ha egy kozbenso Sprite-ot kivanunk elmozgatni akkor a ket reszre szakitott rendszer egyik fele ugy letezne tovabb, hogy \
			 *  nincs onalloan lehelyezheto eleme, vagyis ellentmondas alakulna ki 
			 */
			}else if( dragOneSpriteStarted ){
				
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
//sprite.setTemporaryPosition( new PositionValue( xCursorPosition-initialDelta.getX(), yCursorPosition-initialDelta.getY() ) );
				
				boolean needToRepaintPermanent = false;				

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
							
				//Pozicio szukseg szerinti korrigalasa a magnes alapjan
				needToRepaintPermanent = doArangeSpritePositionByMagnet( sprite ) || needToRepaintPermanent;
	
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

		public void mouseReleased(MouseEvent e) {	

			if( dragAllSpriteStarted ){
				
				//visszahelyezni a Sprite-okat a vegleges taroloba
				//addSprites( moveableSpriteList );
				
				//The Sprite gets the TEMPORARY position as its new PERMANENT position
				//copyTemporaryPositionsToPermanent( moveableSpriteSet );
				
				//Remove Sprites from Shadow state
				setShadow( moveableSpriteSet, false );
				
				//Engedelyezem a fokusz mukodeset
				setNeedFocus(true);
				
				//Es ujra kell rajzoltatni a Permanens lista elemeit
				revalidateAndRepaintCoreCanvas();
			
				//Es mivel fokuszbanlehet meg tovabbra is, ezert egy eger-mozgast is szimulalni kell
				fireMouseMoved();
				
				//Es jelzem, hogy vege a dragg-nek. MINDENKEPPEN
				dragAllSpriteStarted = false;
			
			// Only 1 Sprite was Dragged
			}else if( dragOneSpriteStarted ){
						
				//Ha nem csatlakozik mas elemhez de nincs engedelyezve kapcsolat nelkuli lehelyezesre
				if( !sprite.isConnected() && !sprite.isEnableToPlaceWithoutConnection() ){

					//Akkor vissza kell helyezni az elozo pozicioba
					//Visszairom az eredeti poziciot
//					//sprite.setPosition( originalPosition );
					
					//General egy Drag muveletet vissza az eredeti pozicioba
					MouseEvent me = new MouseEvent(coreCanvas, 11, 0, 0, Math.round((float)getMouseXPositionByWorld(originalPosition.getX() + initialDelta.getX())), Math.round((float)getMouseYPositionByWorld(originalPosition.getY() + initialDelta.getY())), 1, false);									
					spriteDragListener.mouseDragged(me);					
									
				}
					
				//El kell helyezni a vegleges taroloba az uj pozicioval			
//				addSprite(sprite);
				
				//The Sprite gets the TEMPORARY position as its new PERMANENT position
//				sprite.copyTemporaryPositionToPermanent();

				//remove Sprite from Shadow State
				sprite.setIsShadow(false);						
				
				//Engedelyezem a fokusz mukodeset
				setNeedFocus(true);
				
				//Es ujra kell rajzoltatni a Permanens lista elemeit
				revalidateAndRepaintCoreCanvas();
			
				//Es mivel fokuszbanlehet meg tovabbra is, ezert egy eger-mozgast is szimulalni kell
				fireMouseMoved();
				
				//Es jelzem, hogy vege a dragg-nek. MINDENKEPPEN
				dragOneSpriteStarted = false;
			}			
		}
		
		public void mouseExited(MouseEvent e) {	
						
			//Ha elindult mar a drag es igy hagyom el a Canvas-t
			if( dragOneSpriteStarted || dragAllSpriteStarted ){
				
				//mouseReleased( e );
				
			//Csak siman kisetalt a kurzor a kepernyorol, akkor minden Sprite-rol toroljuk a fokuszt
			}else{
				
				//Unfocus all Sprites
				boolean needToReprint = spriteSet.unFocusAll();
				
				if( needToReprint ){			
				
					//Azert kell, hogy az esetlegesen fokuszban levo Sprite-rol eltunjon a fokusz
					repaintCoreCanvas();
				}			
			}						
		}
		
		
		
		public void mouseMoved(MouseEvent e) {}
		
		public void mouseEntered(MouseEvent e) {}
		
		public void mouseClicked(MouseEvent e) {
			double xValue = getWorldXByPixel(e.getX() );			
			double yValue = getWorldYByPixel(e.getY());
			boolean needToPrint = false;

			Sprite sprite = spriteSet.getHighestSpriteInPosition( xValue, yValue );
			if( null != sprite ){

				//addTemporarySprite(sprite);						
				needToPrint = true;
				if( sprite.isSelected() ){
					sprite.setIsSelected(false);
				}else{
					sprite.setIsSelected(true);
				}									
			}
			if( needToPrint ){
				repaintCoreCanvas();
			}				
		}		
	}
	
	/**
	 * Mozgatott Sprite-csoport Poziciojanak korrigalasa a Magnesek alapjan
	 * 
	 * @param sprite
	 * @return
	 */
	public boolean doArangeBlockPositionByMagnet( HashSet<Sprite> moveableSpriteList ){
		
		boolean needToRepaintPermanent = false;
		boolean hasBeenFoundPairForTheBlock = false;
		
		//Vegig megyek az osszes mozgatott Sprite-on
		for( Sprite sprite: moveableSpriteList ){		
		
			//Vegig megyek a Sprite minden magnesen
			for( Magnet draggedMagnet: sprite.getMagnetList() 	){
			
				//Ha a Sprite Magnet-je foglalt, akkor azt mar nem vizsgalom tovabb
				if( null != draggedMagnet.getConnectedTo() ){
					continue;
				}
				
				//Az aktualis magnes pozicioja
				double magnetXRange = getWorldXLengthByPixel( draggedMagnet.getRangeInPixel().getX() );
				double magnetYRange = getWorldYLengthByPixel( draggedMagnet.getRangeInPixel().getY() );
			
				//Az aktualis Magnet hatotavolsaga
				double xMagnetPosition = sprite.getPosition().getX() + draggedMagnet.getRelativePositionToSpriteZeroPoint().getX();
				double yMagnetPosition = sprite.getPosition().getY() + draggedMagnet.getRelativePositionToSpriteZeroPoint().getY();
			
				//boolean hasMagnetConnection = false;
			
				//Vegig az osszes permanens Sprite-on
				for( Sprite possibleToConnectSprite: spriteSet.getMixedSetOfSprites() ){
				
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
							double possibleToConnectXPosition = possibleToConnectSprite.getPosition().getX() + possibleToConnectMagnet.getRelativePositionToSpriteZeroPoint().getX();
							double possibleToConnectYPosition = possibleToConnectSprite.getPosition().getY() + possibleToConnectMagnet.getRelativePositionToSpriteZeroPoint().getY();
						
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
							
								//Ha mar talaltam part a Blokk-nak, vagyis nem ez az elso 
								if( hasBeenFoundPairForTheBlock ){
								
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
								
								//Ehhez az atmozgatott Sprite-hoz igazitja az osszes tobbi kapcsolt Sprite-ot
								draggedMagnet.rRepositionBlockByConnectedMagnet( draggedMagnet, moveableSpriteList, new HashSet<Sprite>() );							
								
								hasBeenFoundPairForTheBlock = true;
								needToRepaintPermanent = true;
//								hasMagnetConnection = true;
								break;
							
							}								
						}
					}
				
					//if( hasMagnetConnection ){
					//	break;
					//}
				}
			}
			
			//if( hasConnection){
			//	break;
			//}					
							
		}
		
		return needToRepaintPermanent;
	}
	
	
	/**
	 * Mozgatott Sprite Poziciojanak korrigalasa a Magnes alapjan
	 * 
	 * @param sprite
	 * @return
	 */
	public boolean doArangeSpritePositionByMagnet( Sprite sprite ){
		
		boolean needToRepaintPermanent = false;
		boolean hasBeenFoundPairForTheSprite = false;
		
		//Vegig megyek a mozgatott Sprite magnesein
		for( Magnet draggedMagnet: sprite.getMagnetList() 	){
			
			//Az aktualis magnes tulajdonsagai
			double magnetXRange = getWorldXLengthByPixel( draggedMagnet.getRangeInPixel().getX() );
			double magnetYRange = getWorldYLengthByPixel( draggedMagnet.getRangeInPixel().getY() );
			
			//Megnezem, hogy az aktualis magnes hatotavolsagaban, van-e egyaltalan masik sprite
			double xMagnetPosition = sprite.getPosition().getX() + draggedMagnet.getRelativePositionToSpriteZeroPoint().getX();
			double yMagnetPosition = sprite.getPosition().getY() + draggedMagnet.getRelativePositionToSpriteZeroPoint().getY();
			
			boolean hasMagnetConnection = false;
			
			//Vegig az osszes permanens Sprite-on
			for( Sprite possibleToConnectSprite: spriteSet.getMixedSetOfSprites() ){
				
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
						double possibleToConnectXPosition = possibleToConnectSprite.getPosition().getX() + possibleToConnectMagnet.getRelativePositionToSpriteZeroPoint().getX();
						double possibleToConnectYPosition = possibleToConnectSprite.getPosition().getY() + possibleToConnectMagnet.getRelativePositionToSpriteZeroPoint().getY();
						
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
		
		return needToRepaintPermanent;
	}
	

	
	
	
	
	/**
	 * A prameterkent megadott Sprite-hoz kapcsolt Sprite-ok listajat adja vissza
	 * 
	 * @param spriteList
	 * @return
	 */
	private HashSet<Sprite> getConnectedSprites( Sprite sprite, HashSet<Sprite> spriteList ){
		
		//Ha a Sprite mar volt a listaban, akkor visszater
		if( spriteList.contains( sprite ) ){
			return spriteList;
		}
		
		//Eloszor is az adott Sprite-ot behelyezi a listaba
		spriteList.add( sprite );
		
		//Vegig a Sprite magnesein 
		for( Magnet m: sprite.getMagnetList()){
			
			Magnet pairMagnet = m.getConnectedTo();
			
			//Ha kapcsolodik a magnese egy masik Sprite-hoz
			if( null != pairMagnet ){
				
				//Akkor megnezi, hogy arrafele mennyi Sprite van
				spriteList = getConnectedSprites( pairMagnet.getParent(), spriteList );
				
			}					
		}
		
		return spriteList;
		
	}
	
	/**
	 * A parameterkent megadott Magnet fele megszamolja a az onalloan lehelyezheto Sprite-ok szamat rekurziv modon
	 * 
	 * @param magnet
	 * @param number
	 * @param spriteList
	 * @return
	 */
	private int getNumbersOfEnableToPlaceWithoutConnection( Magnet magnet, int number, HashSet<Sprite> spriteList ){
		
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
		
		private HashSet<Sprite> spriteSetWithFocus;
		
		public void mouseMoved(MouseEvent e) {
	
			SizeValue biggestBoundBox = new SizeValue( 0, 0, 0, 0 );
			
			if( needFocus() ){
			
				double xValue = getWorldXByPixel(e.getX() );			
				double yValue = getWorldYByPixel(e.getY());
				boolean needToPrint = false;

				//Get the Sprite which is in Focus
				Sprite spriteInFocus = spriteSet.getHighestSpriteInPosition( xValue, yValue );
		
				//There is a Sprite under the Cursor
				if( null != spriteInFocus ){

					biggestBoundBox = spriteInFocus.getBoundBoxAbsolute();
					
					spriteSetWithFocus = new HashSet<Sprite>();
					spriteSetWithFocus.add( spriteInFocus );
				}
				
				//Go through all Sprites - Regardles if there was any Sprite under the Cursor
				for( Sprite sprite:  spriteSet.getMixedSetOfSprites() ){
					
					if( sprite == spriteInFocus ){
						sprite.setInFocus( true );
					}else{
						sprite.setInFocus( false );
					
						if( null != spriteInFocus ){
					
							SizeValue boundBox = sprite.getBoundBoxAbsolute();
												
							//The Sprite is Interesting if it is higher level than the Sprite in focus
							if( sprite.getLevel() > spriteInFocus.getLevel() &&
						
									//Check if the actual Sprite's bound crosses the biggest bound
									!(
									( boundBox.getXMin() > biggestBoundBox.getXMax() ) ||
									( boundBox.getXMax() < biggestBoundBox.getXMin() ) ||
									( boundBox.getYMax() < biggestBoundBox.getYMin() ) ||
									( boundBox.getYMin() > biggestBoundBox.getYMax() )
							) ){											
								biggestBoundBox.setXMin( Math.min( biggestBoundBox.getXMin(), boundBox.getXMin() ) );
								biggestBoundBox.setYMin( Math.min( biggestBoundBox.getYMin(), boundBox.getYMin() ) );
								biggestBoundBox.setXMax( Math.max( biggestBoundBox.getXMax(), boundBox.getXMax() ) );
								biggestBoundBox.setYMax( Math.max( biggestBoundBox.getYMax(), boundBox.getYMax() ) );						
						
								spriteSetWithFocus.add( sprite );
							}
						}
					}					
				}
				if( null != spriteSetWithFocus ){
					addTemporarySprites(spriteSetWithFocus);
					repaintCoreCanvas();
				}
			}
							
		}

		public void mouseDragged(MouseEvent e) {}					
	}
	
	/**
	 * Sprite-ok kirajzolasaert felelos osztaly
	 * 			
	 * @author akoel
	 *
	 */
	class PermanentSpritePainterListener implements PainterListener{

		public void paintByWorldPosition(MCanvas canvas, MGraphics g2) {
			//spriteSet.drawPermanent( g2 );
			spriteSet.draw( g2 );
		}

		public void paintByCanvasAfterTransfer(MCanvas canvas, Graphics2D g2) {}
		
	}
	
	/**
	 * Az atmeneti retegben elhelyezett sprite-ok kirajzolasaert felelos
	 * 
	 * @author akoel
	 *
	 */
	class TemporarySpritePainterListener implements PainterListener{

		public void paintByWorldPosition(MCanvas canvas, MGraphics g2) {
			//temporarySpriteSet.drawTemporary( g2 );
			temporarySpriteSet.drawTemporary( g2 );
			temporarySpriteSet.clear();
		}
		
		public void paintByCanvasAfterTransfer(MCanvas canvas, Graphics2D g2) {}
		
	}
	
	
	
	
	/**
	 * 
	 * @author akoel
	 *
	 */
	class SpriteContainer extends TreeMap<Integer, HashSet<Sprite>> {

		private static final long serialVersionUID = 8046574446846857868L;

		public boolean isIn( Sprite sprite ){
			HashSet<Sprite> spriteListOnLevel = this.get( sprite.getLevel() );
		    if( null != spriteListOnLevel ){
		    	return spriteListOnLevel.contains( sprite );
		    }
		    return false;
		}
		
		public void addSprite( Sprite sprite ){
			
			//Get the actual spriteListOnLevel - if it does not exist then create it
		    HashSet<Sprite> spriteListOnLevel = this.get( sprite.getLevel() );
		    if( null == spriteListOnLevel ){
		    	spriteListOnLevel = new HashSet<Sprite>();
		    	this.put( sprite.getLevel(), spriteListOnLevel );
		    }
		    
		    //Add the Sprite to the specific level
		    spriteListOnLevel.add( sprite );
		}
		
		public void addSprites( HashSet<Sprite> spriteList ){
			
			Iterator<Sprite> it = spriteList.iterator();
			while( it.hasNext() ){
				Sprite sprite = it.next();
				this.addSprite( sprite );
			}		
		}	
		
		public void removeSprite( Sprite sprite ){
			HashSet<Sprite> spriteListOnLevel = this.get( sprite.getLevel() );
		    if( null != spriteListOnLevel ){
		    	spriteListOnLevel.remove( sprite );
		    }
		}
		
		public void removeSprites( HashSet<Sprite> spriteList ){
			Iterator<Sprite> it = spriteList.iterator();
			while( it.hasNext() ){
				Sprite sprite = it.next();
				this.removeSprite( sprite );
			}		
		}
		
		/*public void setShadowStateForAll( boolean value ){
			Set<Map.Entry<Integer,HashSet<Sprite>>> set = this.entrySet();

			//Go through the all SpriteListOnLevel
			Iterator<Map.Entry<Integer,HashSet<Sprite>>> keyIterator = set.iterator();
		    while ( keyIterator.hasNext() ) {
		       Map.Entry<Integer,HashSet<Sprite>> entry = (Map.Entry<Integer,HashSet<Sprite>>) keyIterator.next();
		       //Integer key = entry.getKey();
		       HashSet<Sprite> spriteListOnLevel = entry.getValue();
		       
		       Iterator<Sprite> spriteIterator = spriteListOnLevel.iterator();
		       while( spriteIterator.hasNext() ){
		    	   Sprite sprite = spriteIterator.next();
		    	   sprite.setIsShadow( value );
		       }
		    }
		}
		*/
		
		public boolean unFocusAll(){
			boolean wasInFocus = false;
			Set<Map.Entry<Integer,HashSet<Sprite>>> set = this.entrySet();

			//Go through the all SpriteListOnLevel
			Iterator<Map.Entry<Integer,HashSet<Sprite>>> keyIterator = set.iterator();
		    while ( keyIterator.hasNext() ) {
		       Map.Entry<Integer,HashSet<Sprite>> entry = (Map.Entry<Integer,HashSet<Sprite>>) keyIterator.next();
		       //Integer key = entry.getKey();
		       HashSet<Sprite> spriteListOnLevel = entry.getValue();
		       
		       Iterator<Sprite> spriteIterator = spriteListOnLevel.iterator();
		       while( spriteIterator.hasNext() ){
		    	   Sprite sprite = spriteIterator.next();
		    	   if( sprite.isInFocus() ){
		    		   sprite.setInFocus( false );
		    		   wasInFocus = true;
		    	   }
		       }
		    }
		    return wasInFocus;
			
		}
		
		
		public Sprite getHighestSpriteInPosition( double mouseX, double mouseY){
			
			//Get the levels in opposite direction - from TOP
			NavigableSet<Integer> set = this.descendingKeySet();

		    //Go through the all SpriteListOnLevel
			Iterator<Integer> keyIterator = set.iterator();
		    while ( keyIterator.hasNext() ) {
		       Integer key = keyIterator.next();
		       HashSet<Sprite> spriteListOnLevel = this.get( key );
		       
		       Iterator<Sprite> spriteIterator = spriteListOnLevel.iterator();
		       while( spriteIterator.hasNext() ){
		    	   Sprite sprite = spriteIterator.next();
		    	   
		    	   SizeValue boundBox = sprite.getBoundBoxAbsolute();
					
		    	   //If there is a Sprite under the mouse position
		    	   if( 
		    			   mouseX >= boundBox.getXMin() &&
		    			   mouseX <= boundBox.getXMax() &&
		    			   mouseY >= boundBox.getYMin() &&
		    			   mouseY <= boundBox.getYMax()
		    		){
		    		   return sprite;
		    	   }		    	   
		       }
		    }
		    return null;
		}
		
		public HashSet<Sprite> getMixedSetOfSprites(){
			HashSet<Sprite> mixedSet = new HashSet<Sprite>();
			
			Set<Map.Entry<Integer,HashSet<Sprite>>> set = this.entrySet();

			//Go through the all SpriteListOnLevel
			Iterator<Map.Entry<Integer,HashSet<Sprite>>> keyIterator = set.iterator();
		    while ( keyIterator.hasNext() ) {
		       Map.Entry<Integer,HashSet<Sprite>> entry = (Map.Entry<Integer,HashSet<Sprite>>) keyIterator.next();
		       HashSet<Sprite> spriteListOnLevel = entry.getValue();
		       mixedSet.addAll( spriteListOnLevel );
		    }
		    
		    return mixedSet;
		}
		
		public void draw( MGraphics g2 ){
			Set<Map.Entry<Integer,HashSet<Sprite>>> set = this.entrySet();

			//Go through the all SpriteListOnLevel
			Iterator<Map.Entry<Integer,HashSet<Sprite>>> keyIterator = set.iterator();
		    while ( keyIterator.hasNext() ) {
		       Map.Entry<Integer,HashSet<Sprite>> entry = (Map.Entry<Integer,HashSet<Sprite>>) keyIterator.next();
		       //Integer key = entry.getKey();
		       HashSet<Sprite> spriteListOnLevel = entry.getValue();
		       
		       Iterator<Sprite> spriteIterator = spriteListOnLevel.iterator();
		       while( spriteIterator.hasNext() ){
		    	   Sprite sprite = spriteIterator.next();
		    	   sprite.draw(g2);
		       }
		    }
		}
		
		public void drawTemporary( MGraphics g2 ){
			Set<Map.Entry<Integer,HashSet<Sprite>>> set = this.entrySet();

			//Go through the all SpriteListOnLevel
			Iterator<Map.Entry<Integer,HashSet<Sprite>>> keyIterator = set.iterator();
		    while ( keyIterator.hasNext() ) {
		       Map.Entry<Integer,HashSet<Sprite>> entry = (Map.Entry<Integer,HashSet<Sprite>>) keyIterator.next();
		       //Integer key = entry.getKey();
		       HashSet<Sprite> spriteListOnLevel = entry.getValue();
		       
		       Iterator<Sprite> spriteIterator = spriteListOnLevel.iterator();
		       while( spriteIterator.hasNext() ){
		    	   Sprite sprite = spriteIterator.next();
		    	   sprite.drawTemporary(g2);
		       }
		    }
		}
	}
}
