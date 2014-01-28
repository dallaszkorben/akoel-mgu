package hu.akoel.mgu.drawnblock;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;

import hu.akoel.mgu.MCanvas;
import hu.akoel.mgu.MGraphics;
import hu.akoel.mgu.PainterListener;
import hu.akoel.mgu.PossiblePixelPerUnits;
import hu.akoel.mgu.drawnblock.DrawnBlock.Status;
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
			
			//Ha a baloldali egergombot nyomtam es meg nem kezdtem el rajzolni
			if( e.getButton() == MouseEvent.BUTTON1 && !drawnStarted ){

				//A kurzor pozicioja
				secondaryStartCursorPosition.setX( secondaryActualCursorPosition.getX() );
				secondaryStartCursorPosition.setY( secondaryActualCursorPosition.getY() );

				drawnStarted = true;
				
				//A szerkesztendo DrawnBlock legyartasa
				drawnBlockToDraw = new DrawnBlock(Status.INPROCESS, secondaryStartCursorPosition.getX(), secondaryStartCursorPosition.getY(), secondaryStartCursorPosition.getX(), secondaryStartCursorPosition.getY());
				
				//Atmeneti listaba helyezi a most rajzolas alatt levo DrawnBlock-ot
				addTemporaryDrawnBlock( drawnBlockToDraw );

			}

//System.err.println("pressed");			
			
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
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
//System.err.println("entered");	
			
			//Meghatarozza a masodlagos kurzor aktualis erteket
			findOutCursorPosition( e );

			//Kirajzolja a masodlagos kurzort
			drawCursor( e );
		}

		@Override
		public void mouseExited(MouseEvent e) {
//System.err.println("exited");			
			revalidateAndRepaintCoreCanvas();
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			double tmp;
//System.err.println("dragged");			
			
			//Meghatarozza a masodlagos kurzor aktualis erteket
			findOutCursorPosition( e );
			
			//Ha mar elkezdtem rajzolni
			if( drawnStarted ){
				
				//Modositani kell a poziciot
//				secondaryActualCursorPosition.setX( getWorldXByPixel( e.getX() ) );
//				secondaryActualCursorPosition.setY( getWorldYByPixel( e.getY() ) );
				
				if( secondaryActualCursorPosition.getX() < secondaryStartCursorPosition.getX() ){
					drawnBlockToDraw.setX2( secondaryStartCursorPosition.getX());					
					drawnBlockToDraw.setX1( secondaryActualCursorPosition.getX());
				}else{
					drawnBlockToDraw.setX2( secondaryActualCursorPosition.getX());	
				}
				
				if( secondaryActualCursorPosition.getY() < secondaryStartCursorPosition.getY() ){
					drawnBlockToDraw.setY2( secondaryStartCursorPosition.getY());					
					drawnBlockToDraw.setY1( secondaryActualCursorPosition.getY());
				}else{
					drawnBlockToDraw.setY2( secondaryActualCursorPosition.getY());
				}
				
				//Megnezi, hogy fed-e egy mar meglevo DrawnBlock-ot
				for( DrawnBlock db: drawnBlockList ){
					 
				}
				
				//Elhelyezni a temporary listaban
				addTemporaryDrawnBlock( drawnBlockToDraw );
			}

			//Kirajzolja a masodlagos kurzort
			drawCursor( e );
		}

		@Override
		public void mouseMoved(MouseEvent e) {
//System.err.println("moved");
			
			//Meghatarozza a masodlagos kurzor aktualis erteket
			findOutCursorPosition( e );
			
			//Kirajzolja a masodlagos kurzort
			drawCursor( e );
		}
		
		/**
		 * Meghatarozza a masodlagos kurzor aktualis erteket
		 * 
		 * @param e
		 */
		private void findOutCursorPosition( MouseEvent e ){

			double tmpX1, tmpX2, tmpY1, tmpY2;
			
			double x = getWorldXByPixel( e.getX() );
			double y = getWorldYByPixel( e.getY() );
			
			//Ha meg nem kezdodott el a rajzolas
			if( !drawnStarted ){

				//Megnezi, hogy az aktualis kurzor egy lehelyezett DrawnBlock-ra esik-e
				for( DrawnBlock db: drawnBlockList ){
				
					//Beleesik a kurzor egy lehelyezett DrawnBlock-ba
					if( ( x > db.getX1() && x < db.getX2() ) && ( y > db.getY1() && y < db.getY2() ) ){
						
						//Akkor a masodlagos kurzor marad a regi pozicioban
						return;
					}
				}
			
			//Ha mar elkezdte a rajzolast
			}else{
				
				if( x < secondaryStartCursorPosition.getX() ){
					tmpX1 = x;
					tmpX2 = secondaryStartCursorPosition.getX();
				}else{					
					tmpX1 = secondaryStartCursorPosition.getX();
					tmpX2 = x;
				}
				
				if( y < secondaryStartCursorPosition.getY() ){
					tmpY1 = y;
					tmpY2 = secondaryStartCursorPosition.getY();
				}else{					
					tmpY1 = secondaryStartCursorPosition.getY();
					tmpY2 = y;
				}
				
				//Megnezi, hogy a lehelyezendo DrawnBlock fedesbe kerul-e egy mar lehelyezett DrawnBlock-kal
				for( DrawnBlock db: drawnBlockList ){
//this->db
//r->tmp
					
					double tx1 = db.getX1();
					double ty1 = db.getY1();
					double rx1 = tmpX1;
					double ry1 = tmpY1;
					
					double tx2 = db.getX2();
					double ty2 = db.getY2();
					double rx2 = tmpX2;
					double ry2 = tmpY2;
					
					if (tx1 < rx1) tx1 = rx1;
					if (ty1 < ry1) ty1 = ry1;
					if (tx2 > rx2) tx2 = rx2;
					if (ty2 > ry2) ty2 = ry2;
					tx2 -= tx1;
					ty2 -= ty1;
			
					//Van kozos metszete a most rajzolando negyzetnek es a mar lehelyezett negyzetnek
					if( 
							ty2 >= 0 && tx2 >= 0
					){
						
						//Akkor a masodlagos kurzor marad a regi pozicioban
						return;
					}else{
						System.err.println(ty2 + ", " + tx2 + "   -  " + tmpX1 + ", " + tmpX2 );
					}
					
				}
				
			}
			
			secondaryActualCursorPosition.setX( x );
			secondaryActualCursorPosition.setY( y );
			
		}
		
		
		/**
		 * Az kurzor poziciojanak valtoztatasa es a
		 * Temporary lista ujra rajzolasa
		 * 
		 * @param e
		 */
		private void drawCursor( MouseEvent e ){

			addPainterListenerToTemporary( new TemporaryDrawnBlockPainterListener(){
			
				@Override
				public void paintByCanvasAfterTransfer(MCanvas canvas, Graphics2D g2) {
					
					int x = getPixelXPositionByWorldBeforeTranslate( secondaryActualCursorPosition.getX() );
					int y = getPixelYPositionByWorldBeforeTranslate( secondaryActualCursorPosition.getY() );
						
					g2.setColor( Color.white );
					g2.setStroke( basicStroke );
					g2.drawLine( x, y - 7, x, y + 7 );
					g2.drawLine( x - 7, y, x + 7, y );
					
				}
								
			}, Level.ABOVE );	
			
			revalidateAndRepaintCoreCanvas();
		}
		
	}
	
	


	
	
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
