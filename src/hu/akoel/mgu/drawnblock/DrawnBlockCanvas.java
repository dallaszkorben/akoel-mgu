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
	
	private DrawnBlockDrawListener drawnBlockDrawListener = new DrawnBlockDrawListener();
	private DrawnBlockPainterListener drawnBlockPainterListener = new DrawnBlockPainterListener();
	
	private SecondaryCursor secondaryCursor = new SecondaryCursor();
	
	private boolean needFocus = true;
	

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
		this.addPainterListenerToMiddle( drawnBlockPainterListener, Level.ABOVE );
		
		//A kurzor mozgasat vizsgalolo listener
		this.addMouseInputListener( drawnBlockDrawListener );
		
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
	
	public void addTemporarySecondaryCursor( SecondaryCursor secondaryCursor ){
		
		//Minden megjelenites utan torlodik a listener, ezert kell mindig hozzaadni
		this.addPainterListenerToTemporary( new TemporarySecondaryCursorPainterListener(), Level.ABOVE );
		
		//Itt nincs szukseg a lista megadasara, mert csak egy elem szerepel
	}

	public void zoomIn(double xCenter, double yCenter, int xPoint, int yPoint){
		super.zoomIn(xCenter, yCenter, xPoint, yPoint);

		//Azert kell, hogy a zoom utan kovetkezo ujrarajzolas miatt eltuno fokuszban levo DrawnBlock ujra bekeruljon a temporary listaba
		//Mozgast szimulal, mintha megmozdult volna a kurzor, ami azt eredmenyezi, hogy kirajzolodik a kurzor
		//fireMouseMoved();

		drawnBlockDrawListener.repaintSecondaryCursorAndDrawnBlockToDraw();
	}
	
	public void zoomOut(double xCenter, double yCenter, int xPoint, int yPoint){
		super.zoomOut(xCenter, yCenter, xPoint, yPoint);
		
		//Azert kell, hogy a zoom utan kovetkezo ujrarajzolas miatt eltuno fokuszban levo DrawnBlock ujra bekeruljon a temporary listaba

//		fireMouseMoved();
		
		drawnBlockDrawListener.repaintSecondaryCursorAndDrawnBlockToDraw();

	}
	

	
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
		
		private PositionValue secondaryStartCursorPosition = new PositionValue(0, 0);
		
		private boolean drawnStarted = false;
		private DrawnBlock drawnBlockToDraw = null;


		@Override
		public void mouseClicked(MouseEvent e) {
			
//System.err.println("clicked");			
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
			//Ha a baloldali egergombot nyomtam es meg nem kezdtem el rajzolni
			if( e.getButton() == MouseEvent.BUTTON1 && !drawnStarted ){

				//A kurzor pozicioja
				secondaryStartCursorPosition.setX( secondaryCursor.getX() );
				secondaryStartCursorPosition.setY( secondaryCursor.getY() );

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

				//Azert kell, hogy az elengedes pillanataban ne tunjon el a masodlagos kurzor
				addTemporarySecondaryCursor(secondaryCursor);
				
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
			addTemporarySecondaryCursor( secondaryCursor );
			
//repaintCoreCanvas();
			revalidateAndRepaintCoreCanvas();
		}

		@Override
		public void mouseExited(MouseEvent e) {
//System.err.println("exited");			
			revalidateAndRepaintCoreCanvas();
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {

//System.err.println("dragged");			
			
			//Meghatarozza a masodlagos kurzor aktualis erteket
			findOutCursorPosition( e );
			
			//Ha mar elkezdtem rajzolni
			if( drawnStarted ){
				
				if( secondaryCursor.getX() < secondaryStartCursorPosition.getX() ){
					drawnBlockToDraw.setX2( secondaryStartCursorPosition.getX());					
					drawnBlockToDraw.setX1( secondaryCursor.getX());
				}else{
					drawnBlockToDraw.setX2( secondaryCursor.getX());	
				}
				
				if( secondaryCursor.getY() < secondaryStartCursorPosition.getY() ){
					drawnBlockToDraw.setY2( secondaryStartCursorPosition.getY());					
					drawnBlockToDraw.setY1( secondaryCursor.getY());
				}else{
					drawnBlockToDraw.setY2( secondaryCursor.getY());
				}
				
				//Elhelyezni a temporary listaban a most szerkesztendo DrawnBlock-ot
				addTemporaryDrawnBlock( drawnBlockToDraw );
			}

			//Elhelyezi a temporary listaban a masodlagos kurzort
			addTemporarySecondaryCursor( secondaryCursor );
			
			//Kirajzolja az elhelyezett szerkesztedno DrawnBlock-ot es a masodlagos kurzort
//repaintCoreCanvas();
			revalidateAndRepaintCoreCanvas();
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
//System.err.println("moved");
			
			//Meghatarozza a masodlagos kurzor aktualis erteket
			findOutCursorPosition( e );
			
			//Kirajzolja a masodlagos kurzort
			addTemporarySecondaryCursor( secondaryCursor );
			
			//Kirajzolja a masodlagos kurzort
//repaintCoreCanvas();
			revalidateAndRepaintCoreCanvas();
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
					}
					
				}
				
			}
			
			//A Masodlagos kurzor poziciojanak beallitasa
			secondaryCursor.setPosition( x, y );
		}
		
		public void repaintSecondaryCursorAndDrawnBlockToDraw(){
			
			if( null != drawnBlockToDraw ){
				addTemporaryDrawnBlock( drawnBlockToDraw );
			}
			
			if( null != secondaryCursor ){
				addTemporarySecondaryCursor( secondaryCursor );
			}
			
			repaintCoreCanvas();
			
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
	 * Az atmeneti retegben elhelyezett DrawnBlock-ok kirajzolasaert felelos osztaly
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
	
	class TemporarySecondaryCursorPainterListener implements PainterListener{

		@Override
		public void paintByWorldPosition(MCanvas canvas, MGraphics g2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void paintByCanvasAfterTransfer( MCanvas canvas, Graphics2D g2 ) {
		
			secondaryCursor.draw(g2);
			
		}
		
	}
	
	class SecondaryCursor{
		PositionValue cursorPosition = null;
		
		public SecondaryCursor(){
			this.cursorPosition = new PositionValue(0, 0);
		}
		
		public SecondaryCursor( PositionValue cursorPosition ){
			this.cursorPosition = new PositionValue( cursorPosition.getX(), cursorPosition.getY() );
		}
		
		public void setPosition( double x, double y ){
			this.cursorPosition.setX( x );
			this.cursorPosition.setY( y );
		}
		
		public void setPosition( PositionValue cursorPosition ){
			this.cursorPosition.setX( cursorPosition.getX() );
			this.cursorPosition.setY( cursorPosition.getY() );
		}
		
		public double getX(){
			return cursorPosition.getX();
		}
		
		public double getY(){
			return cursorPosition.getY();
		}
		
		public void draw( Graphics2D g2 ){
			int x, y;
			
			if( null != cursorPosition ){

				x = getPixelXPositionByWorldBeforeTranslate( cursorPosition.getX() );
				y = getPixelYPositionByWorldBeforeTranslate( cursorPosition.getY() );
			
				g2.setColor( Color.white );
				g2.setStroke( basicStroke );
				g2.drawLine( x, y - 8, x, y + 8 );
				g2.drawLine( x - 8, y, x + 8, y );
			}
			
		}
	}
}
