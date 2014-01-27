package hu.akoel.mgu.drawnblock;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
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
import hu.akoel.mgu.drawnblock.DrawnBlock.Status;
import hu.akoel.mgu.values.DeltaValue;
import hu.akoel.mgu.values.PositionValue;
import hu.akoel.mgu.values.SizeValue;
import hu.akoel.mgu.values.TranslateValue;

public class DrawnBlockCanvas extends MCanvas{

	private static final long serialVersionUID = -8308688255617119442L;
	
	private Stroke basicStroke = new BasicStroke();
	
	private ArrayList<DrawnBlock> drawnBlockList = new ArrayList<DrawnBlock>();
	private ArrayList<DrawnBlock> temporaryDrawnBlockList = new ArrayList<DrawnBlock>();
	
	private boolean needFocus = true;
	
	private PositionValue secondaryStartCursorPosition = new PositionValue(0, 0);
	private PositionValue secondaryActualCursorPosition = new PositionValue(0, 0);
	private boolean drawnStarted = false;
	private DrawnBlock drawnBlockToDraw = null;

	public DrawnBlockCanvas(Border borderType, Color background, PossiblePixelPerUnits possiblePixelPerUnits, TranslateValue positionToMiddle ) {
		super(borderType, background, possiblePixelPerUnits, positionToMiddle );		
		commonConstructor();
	}
	
	public DrawnBlockCanvas(Border borderType, Color background, PossiblePixelPerUnits possiblePixelPerUnits, TranslateValue positionToMiddle, SizeValue boundSize ) {
		super(borderType, background, possiblePixelPerUnits, positionToMiddle, boundSize );
		commonConstructor();
	}

	private void commonConstructor(){
		
		//Azt figyeli, hogy egy DrawnBlock fokuszba kerult-e
//		this.addMouseMotionListener( drawnBlockInFocusListener );
		
		//A kozepso reteget hasznaljuk a DrawnBlock-ok megjelenitesere
		this.addPainterListenerToMiddle( new DrawnBlockPainterListener(), Level.ABOVE );
		
		//A kurzor mozgasat vizsgalolo listener
		this.addMouseInputListener( new DrawnBlockDrawListener() );
		
//		//Egy DrawnBlock mozgatasat figyeli
//		this.addMouseInputListener( DragListener );
		
	}

	public void setNeedFocus( boolean needFocus){
		this.needFocus = needFocus;
	}
	
	public boolean needFocus(){
		return needFocus;
	}
	
	/**
	 * Hozzaad a megjelenitendo listahoz egy DrawnBlock-ot
	 * 
	 * @param drawnBlock
	 */
	public void addDrawnBlock( DrawnBlock drawnBlock ){

		if( !this.drawnBlockList.contains( drawnBlock ) ){
			this.drawnBlockList.add( drawnBlock );
		}		
	}
	
	/**
	 * Eltavolit egy DrawnBlock elemet a megjelenitendo DrawnBlock listabol
	 * 
	 * @param drawnBlock
	 */
	public void removeDrawnBlock( DrawnBlock drawnBlock ){
		this.drawnBlockList.remove( drawnBlock );
	}
		
	/**
	 * Hozzaad egy DrawnBlock elemet a Temporary listahoz atmeneti megjelenitesre
	 * 
	 * @param drawnBlock
	 */
	public void addTemporaryDrawnBlock( DrawnBlock drawnBlock ){
		
		//Temporary reteget hasznaljuk a fokus megjelenitesre
		//Minden megjelenites utan torlodik a listener, ezert kell mindig hozzaadni
		this.addPainterListenerToTemporary( new TemporaryDrawnBlockPainterListener(), Level.UNDER );	
		
		if( !temporaryDrawnBlockList.contains(drawnBlock)){
			temporaryDrawnBlockList.add(drawnBlock);
		}	
	}
	
	
	//A mozgatando Sprite uj pozicioval elhelyezese az atmeneti taroloban
//	addTemporarySprites(moveableSpriteList);
	
	//A Permanens es atmeneti taroloban levo Sprite-ok ujrarajzolasa szukseges
//	revalidateAndRepaintCoreCanvas();
	
/*	
	public void zoomIn(double xCenter, double yCenter, int xPoint, int yPoint){
		super.zoomIn(xCenter, yCenter, xPoint, yPoint);
		
		//Azert kell, hogy a zoom utan kovetkezo ujrarajzolas miatt eltuno fokuszban levo Sprite ujra bekeruljon a temporary listaba

		fireMouseMoved();
		DragListener.loadSpriteToTemporary();
		repaintCoreCanvas();
	}
	
	public void zoomOut(double xCenter, double yCenter, int xPoint, int yPoint){
		super.zoomOut(xCenter, yCenter, xPoint, yPoint);
		
		//Azert kell, hogy a zoom utan kovetkezo ujrarajzolas miatt eltuno fokuszban levo Sprite ujra bekeruljon a temporary listaba

		fireMouseMoved();
		DragListener.loadSpriteToTemporary();
		repaintCoreCanvas();
	}
*/	
	
	/**
	 * 
	 * DrawnBlock rajzolasaert felelos osztaly
	 * 
	 * move, exited
	 * exited, dragged
	 * pressed, released clicked
	 * 
	 * @author akoel
	 *
	 */
	class DrawnBlockDrawListener implements MouseInputListener{
		
		int cursorX, cursorY;

		@Override
		public void mouseClicked(MouseEvent e) {
			
//System.err.println("clicked");			
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
			//Ha meg nem kezdtem el rajzolni
			if( !drawnStarted ){
				
				drawnStarted = true;
				
				secondaryStartCursorPosition.setX( getWorldXByPixel( e.getX() ) );
				secondaryStartCursorPosition.setY( getWorldYByPixel( e.getY() ) );
				
				drawnBlockToDraw = new DrawnBlock(Status.INPROCESS, secondaryStartCursorPosition.getX(), secondaryStartCursorPosition.getY(), secondaryStartCursorPosition.getX(), secondaryStartCursorPosition.getY());
				
				//Atmeneti listaba helyezi a most rajzolas alatt levo DrawnBlock-ot
				addTemporaryDrawnBlock( drawnBlockToDraw );

			}

//System.err.println("pressed");			
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
			//Ha elindult mar egy rajzolasi folyamat
			if( drawnStarted ){
				
				//A lehelyezendo DrawnBlokk statusza NORMAL lesz
				drawnBlockToDraw.setStatus( Status.NORMAL );
				
				//Hozzaadom a statikusan kirajzolando DrawnBlock-ok listajahoz
				addDrawnBlock( drawnBlockToDraw );
				
				//Ujrarajzoltatom a Canvas-t az uj statikus DrawnBlock-kal egyutt
				revalidateAndRepaintCoreCanvas();
				
				//Jelzi, hogy meg nem indult el a kovetkezo DrawnBlock rajzolasa
				drawnStarted = false;
				
				//Az ujabb DrawnBlock meg nem letezik
				drawnBlockToDraw = null;
			}
			
//System.err.println("release");
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
//System.err.println("entered");			
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
System.err.println("exited");			
			// TODO Auto-generated method stub
			//drawCursor(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
System.err.println("dragged");			
			//Ha mar elkezdtem rajzolni
			if( drawnStarted ){
				
				//Modositani kell a poziciot
				secondaryActualCursorPosition.setX( getWorldXByPixel( e.getX() ) );
				secondaryActualCursorPosition.setY( getWorldYByPixel( e.getY() ) );
				
				drawnBlockToDraw.setX2( secondaryActualCursorPosition.getX());
				drawnBlockToDraw.setY2( secondaryActualCursorPosition.getY());
				
				//Elhelyezni a temporary listaban
				addTemporaryDrawnBlock( drawnBlockToDraw );
			}

			
			drawCursor( e );
		}

		@Override
		public void mouseMoved(MouseEvent e) {
System.err.println("moved");
			
			drawCursor( e );
		}
		
		/**
		 * Az kurzor poziciojanak valtoztatasa es a
		 * Temporary lista ujra rajzolasa
		 * 
		 * @param e
		 */
		private void drawCursor( MouseEvent e ){
			int y = e.getY();
			int x = e.getX();

			secondaryStartCursorPosition.setX( getWorldXByPixel( x ) );
			secondaryStartCursorPosition.setY( getWorldYByPixel( y ) );

			addPainterListenerToTemporary( new TemporaryDrawnBlockPainterListener(){
			
				@Override
				public void paintByCanvasAfterTransfer(MCanvas canvas, Graphics2D g2) {
					
					int x = getPixelXPositionByWorldBeforeTranslate( secondaryStartCursorPosition.getX() );
					int y = getPixelYPositionByWorldBeforeTranslate( secondaryStartCursorPosition.getY() );
						
					g2.setColor( Color.white );
					g2.setStroke( basicStroke );
					g2.drawLine( x, y - 7, x, y + 7 );
					g2.drawLine( x - 7, y, x + 7, y );				
				}
								
			}, Level.ABOVE );	
			
			revalidateAndRepaintCoreCanvas();
		}
		
	}
	
	
/*	class SpriteDragListener implements MouseInputListener{
		private PositionValue originalPosition;
		private DeltaValue initialDelta;

		private boolean dragOneSpriteStarted = false;
		private boolean dragAllSpriteStarted = false;
		private DrawnBlock sprite;
		private HashSet<DrawnBlock> moveableSpriteList;
		
		public void loadSpriteToTemporary(){
			if( dragOneSpriteStarted ){
				addTemporarySprite(sprite);	
			}
		}
		

		
		@Override
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
				
				//Akkor meg kell nezni, hogy van-e alatta sprite
				for( DrawnBlock sprite: drawnBlockList){
					
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
						
						moveableSpriteList = new HashSet<DrawnBlock>();
						moveableSpriteList = getConnectedSprites(sprite, moveableSpriteList);					
						dragAllSpriteStarted = true;
					
						//Eltavolitom a permanens listabol
						removeSprites(moveableSpriteList);
						
						//El kell helyezni az atmeneti taroloba az uj pozicioval						
						//sprite.setPosition(new PositionValue(xValue-initialDelta.getX(), yValue-initialDelta.getY()));
//						sprite.setPosition( new PositionValue(originalPosition.getX(), originalPosition.getY()) );
						
						addTemporarySprites(moveableSpriteList);	
						
						revalidateAndRepaintCoreCanvas();
						
						break;

					}					
				}	
			//
			//Ha a bal-eger gombot nyomtam le - At akarom helyezni a Sprite-ot
			//
			}else if( e.getButton() == MouseEvent.BUTTON1 ){
				
				double xValue = getWorldXByPixel(e.getX() );
				double yValue = getWorldYByPixel(e.getY());
				
				//Akkor meg kell nezni, hogy van-e alatta sprite
				for( DrawnBlock sprite: drawnBlockList){
					
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
						
						dragOneSpriteStarted = true;
						
						break;

					}					
				}				
			}
			
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {	

			if( dragAllSpriteStarted ){
				
				//visszahelyezni a Sprite-okat a vegleges taroloba
				addDrawnBlocks( moveableSpriteList );
				
				//Engedelyezem a fokusz mukodeset
				setNeedFocus(true);
				
				//Es ujra kell rajzoltatni a Permanens lista elemeit
				revalidateAndRepaintCoreCanvas();
			
				//Es mivel fokuszbanlehet meg tovabbra is, ezert egy eger-mozgast is szimulalni kell
				fireMouseMoved();
				
				//Es jelzem, hogy vege a dragg-nek. MINDENKEPPEN
				dragAllSpriteStarted = false;
				
			}else if( dragOneSpriteStarted ){
						
				//Ha nem csatlakozik mas elemhez de nincs engedelyezve kapcsolat nelkuli lehelyezesre
				if( !sprite.isConnected() && !sprite.isEnableToPlaceWithoutConnection() ){

					//Akkor vissza kell helyezni az elozo pozicioba
					//Visszairom az eredeti poziciot
//					//sprite.setPosition( originalPosition );
					
					//General egy Drag muveletet vissza az eredeti pozicioba
					MouseEvent me = new MouseEvent(coreCanvas, 11, 0, 0, Math.round((float)getMouseXPositionByWorld(originalPosition.getX() + initialDelta.getX())), Math.round((float)getMouseYPositionByWorld(originalPosition.getY() + initialDelta.getY())), 1, false);									
					DragListener.mouseDragged(me);					
									
				}
					
				//El kell helyezni a vegleges taroloba az uj pozicioval			
				addDrawnBlock(sprite);
			
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
		
		@Override
		public void mouseExited(MouseEvent e) {	
						
			//Ha elindult mar a drag es igy hagyom el a Canvas-t
			if( dragOneSpriteStarted || dragAllSpriteStarted ){
				
				//mouseReleased( e );
				
			//Csak siman kisetalt a kurzor a kepernyorol, akkor minden Sprite-rol toroljuk a fokuszt
			}else{
				
				boolean needToReprint = false;
				
				//Vegig az osszes Sprite-on
				for( DrawnBlock sprite: drawnBlockList){
					
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

				//A mozgatando Sprite Elozetes uj pozicioba helyezese
//				sprite.setPosition(new PositionValue(xCursorPosition-initialDelta.getX(), yCursorPosition-initialDelta.getY()));
							
//				draggedMagnet.blabla( draggedMagnet, new HashSet<Sprite>(), moveableSpriteList );				
	
				
				//Az osszes Sprite-ot a mozgatando listabol a megfelelo helyre pozicionalok
				for( DrawnBlock sprite: moveableSpriteList ){
				
					//A mozgatando Sprite Elozetes uj pozicioba helyezese
					sprite.setPosition(new PositionValue(
							sprite.getPosition().getX() + xDelta, 
							sprite.getPosition().getY() + yDelta
							)
					);
				}
			
				//Torlom a Sprite csoport minden kapcsolatat ami nem a csoporton belul kottetett
				//Tulajdonkeppen a lehetseges kapcsolatokrol van szo
				for( DrawnBlock sprite: moveableSpriteList ){
					for( Magnet magnet: sprite.getMagnetList() ){
						Magnet pairMagnet = magnet.getConnectedTo();
						
						//Kivulre mutato kapcsolat
						if( null != pairMagnet && !moveableSpriteList.contains( pairMagnet.getParent() ) ){
							
							//Torolni kell a kapcsolatot
							magnet.setConnectedTo(null);
						}
					}
				}
				
				//Poziciok szukseg szerinti korrigalasa a magnes alapjan
				doArangeBlockPositionByMagnet(moveableSpriteList);
				
				//A mozgatando Sprite uj pozicioval elhelyezese az atmeneti taroloban
				addTemporarySprites(moveableSpriteList);
				
				//A Permanens es atmeneti taroloban levo Sprite-ok ujrarajzolasa szukseges
				revalidateAndRepaintCoreCanvas();
				
					
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {}
		
		@Override
		public void mouseClicked(MouseEvent e) {}
		
	}
*/	

	
	


	
	
	/**
	 * Azt figyeli, hogy egy DrawnBlock fokuszba kerult-e
	 * 
	 * @author akoel
	 *
	 */
/*	class DrawnBlockInFocusListener implements MouseMotionListener{
		
		@Override
		public void mouseMoved(MouseEvent e) {
	
			if( needFocus() ){
			
				double xValue = getWorldXByPixel(e.getX() );			
				double yValue = getWorldYByPixel(e.getY());
				boolean needToPrint = false;

				for( DrawnBlock sprite: drawnBlockList){
				
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
*/	

	/**
	 * DrawnBlock-ok kirajzolasaert felelos osztaly
	 * 			
	 * @author akoel
	 *
	 */
	class DrawnBlockPainterListener implements PainterListener{

		@Override
		public void paintByWorldPosition(MCanvas canvas, MGraphics g2) {

			for( DrawnBlock sprite: drawnBlockList){
				sprite.draw(g2);
			}
			
		}
		
		@Override
		public void paintByCanvasAfterTransfer(MCanvas canvas, Graphics2D g2) {}
		
	}
	
	/**
	 * Az atmeneti retegben elhelyezett drawnBlock-ok kirajzolasaert felelos
	 * 
	 * @author akoel
	 *
	 */
	class TemporaryDrawnBlockPainterListener implements PainterListener{

		@Override
		public void paintByWorldPosition(MCanvas canvas, MGraphics g2) {
			
			// Kirajzolja a Temporary listaban levo elemeket
			for( DrawnBlock drawnBlock: temporaryDrawnBlockList){
				drawnBlock.draw(g2);
			}
			
			//Majd a vegen torli a listat
			temporaryDrawnBlockList.clear();
		}		

		@Override
		public void paintByCanvasAfterTransfer(MCanvas canvas, Graphics2D g2) {}
		
	}
}
