package hu.akoel.mgu.jcanvas.own;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;


public class JCanvas extends JPanel {

	public enum POSITION {
		DEEPEST, HIGHEST
	}

	//Az oldalak aranyara vonatkozo szabaly a befoglalo kontener meretvaltozasanak fuggvenyeben
	public enum SIDES_PORTION{
		
		//Az ablakban latszik a teljes definialt vilag. Ez egyben azt jelenti, hogy
		//a meretarany az ablak meretenek valtoztatasaval valtozik, es az ablak
		//oldalainak aranya nem kotott
		//SHOW_WHOLE_WORLD,
		FIX_PORTION,
		  
		//Az ablak merete szabadon valtozhat
		//a meretarany nem valtozik
		//Nem feltetlenullatom a teljes meretet
		FREE_PORTION,		  
		
	}
	
	private static final long serialVersionUID = 44576557802932936L;

	private Size worldSize;
	private double pixelPerUnit;
//	private Point worldPixelTranslate = new Point(0,0); //pixelben adott eltolas
	private Position worldTranslate = new Position( 0.0, 0.0 );
	private Position positionToMiddle = null;
	private boolean wasTransferedToMiddle = false;
	
	//PERMANENT listak
	ArrayList<PainterListener> aboveList = new ArrayList<PainterListener>();
	ArrayList<PainterListener> middleList = new ArrayList<PainterListener>();
	ArrayList<PainterListener> underList = new ArrayList<PainterListener>();
	
	//TEMPORARY lista
	ArrayList<PainterListener> temporaryList = new ArrayList<PainterListener>();

	CoreCanvas coreCanvas;
	SIDES_PORTION sidePortion;
	
	/**
	 * If the unitToPixelPortion is set then the sidePortion is FREE_PORTION
	 * 
	 * @param borderType a Canvas keretenek tipusa
	 * @param background a Canvas hatterszine
	 * @param pixelPerUnit a felbontas merteke [pixel/unit]
	 * @param positionToMiddle Az itt megadott ertek legyen a canvas kozepen az indulaskor.
	 * null eseten a 0,0 pozicio lesz a bal also sarokban
	 */
	public JCanvas(Border borderType, Color background, double pixelPerUnit, Position positionToMiddle ) {
		
		this.commonConstructor(borderType, background, null);		
		this.pixelPerUnit = pixelPerUnit;
		this.sidePortion = SIDES_PORTION.FREE_PORTION;
		this.positionToMiddle = positionToMiddle;
		
		//Ha nem adtam meg eltolast a koordinatarendszernek
		if(null == positionToMiddle ){
			
			//Akkor azt jelzem, hogy megtorent az eltolas. 
			wasTransferedToMiddle = true;
		}
	}

	
	/**
	 * If the unitToPixelPortion is missing then the sidePortion is FIX_PORTION
	 * 
	 * @param borderType
	 * @param background
	 * @param worldSize
	 */
	public JCanvas(Border borderType, Color background, Size worldSize) {
		
		if( null == worldSize ){
			throw new Error("In case of FIX_PORTION it is required to set the worldSize. Now it is null.");
		}

		this.commonConstructor(borderType, background, worldSize);
		this.sidePortion = SIDES_PORTION.FIX_PORTION;
	}
	
	private void commonConstructor(Border borderType, Color background, Size worldSize ){
		this.setBorder(borderType);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.worldSize = worldSize;
		this.add( getCoreCanvas( this, background ) );
	}
	
	public Position getWorldTranslate(){
		return worldTranslate;
	}
	
	public void setWorldTranslate(Position worldTranslate){
		this.worldTranslate = worldTranslate;
	}
	public double getPixelPerUnit(){
		return pixelPerUnit;
	}
	
	public void setPixelPerUnit( double unitToPixelPortion ){
		this.pixelPerUnit = unitToPixelPortion;
	}
	
	public CoreCanvas getCoreCanvas(JCanvas canvas, Color background ){
		if( null == coreCanvas ){
			coreCanvas = new CoreCanvas( this, background );
		}
		return coreCanvas;
	}
	
	//
	//PERMANENT - Under list
	//
	public void addPainterListenerToUnder(PainterListener painter, POSITION position) {
		if (position.equals(POSITION.HIGHEST)) {
			underList.add(painter);
		} else {
			underList.add(0, painter);
		}
		coreCanvas.invalidate();
	}

	public void addPainterListenerToUnder(PainterListener painter) {
		addPainterListenerToUnder(painter, POSITION.HIGHEST);
	}
	
	public void removePainterListenersFromUnder(){
		underList.clear();
		coreCanvas.invalidate();
	}

	//
	//PERMANENT - Middle list
	//
	public void addPainterListenerToMiddle(PainterListener painter, POSITION position) {
		if (position.equals(POSITION.HIGHEST)) {
			this.middleList.add(painter);
		} else {
			this.middleList.add(0, painter);
		}
		coreCanvas.invalidate();
	}

	public void addPainterListenerToMiddle(PainterListener painter) {
		this.addPainterListenerToMiddle(painter, POSITION.HIGHEST);
	}
	
	public void removePainterListenersFromMiddle(){
		middleList.clear();
		coreCanvas.invalidate();
	}
	
	//
	//PERMANENT - Above list
	//
	public void addPainterListenerToAbove(PainterListener painter, POSITION position) {
		if (position.equals(POSITION.HIGHEST)) {
			this.aboveList.add(painter);
		} else {
			this.aboveList.add(0, painter);
		}
		coreCanvas.invalidate();
	}

	public void addPainterListenerToAbove(PainterListener painter) {
		this.addPainterListenerToAbove(painter, POSITION.HIGHEST);
	}
	
	public void removePainterListenersFromAbove(){
		aboveList.clear();
		coreCanvas.invalidate();
	}
	
	//
	//TEMPORARY list
	//
	public void addPainterListenerToTemporary(PainterListener painter, POSITION position) {
		if (position.equals(POSITION.HIGHEST)) {
			this.temporaryList.add(painter);
		} else {
			this.temporaryList.add(0, painter);
		}
	}

	public void addPainterListenerToTemporary(PainterListener painter) {
		this.addPainterListenerToTemporary(painter, POSITION.HIGHEST);
	}
	
	public void removePainterListenersFromTemporary(){
		temporaryList.clear();
	}
	
	//---------
	// Mozgatas
	//---------
	public void moveX( int pixel ){
		double possibleXTranslate = worldTranslate.getX();
		
		if( pixel > 0){
			possibleXTranslate -= getWorldLengthByPixel(pixel + 1);
		}else{
			possibleXTranslate += getWorldLengthByPixel(-pixel + 1);
		}

		worldTranslate.setX( possibleXTranslate );
		
		coreCanvas.invalidate();
		coreCanvas.repaint();
	}
	
	public void moveY( int pixel ){

		double possibleYTranslate = worldTranslate.getY();
		
		if( pixel > 0){
			possibleYTranslate += getWorldLengthByPixel(pixel + 1);
		}else{
			possibleYTranslate -= getWorldLengthByPixel(-pixel + 1);
		}
		
		worldTranslate.setY( possibleYTranslate );

		coreCanvas.invalidate();
		coreCanvas.repaint();
		
	}
	
	public void moveXY(int pixelX, int pixelY){
		double possibleXTranslate = worldTranslate.getX();
		
		if( pixelX > 0){
			possibleXTranslate -= getWorldLengthByPixel(pixelX + 1);
		}else{
			possibleXTranslate += getWorldLengthByPixel(-pixelX + 1);
		}

		worldTranslate.setX( possibleXTranslate );
		
		double possibleYTranslate = worldTranslate.getY();
		
		if( pixelY > 0){
			possibleYTranslate += getWorldLengthByPixel(pixelY + 1);
		}else{
			possibleYTranslate -= getWorldLengthByPixel(-pixelY + 1);
		}
		
		worldTranslate.setY( possibleYTranslate );

		coreCanvas.invalidate();
		coreCanvas.repaint();
	}
	
	
	public void moveDown(int pixel){		
		
		moveY(pixel);		
		
	}

	public void moveUp(int pixel){
		moveY(-pixel);
		
	}

	public void moveRight(int pixel){	
		moveX(pixel);
	}
	
	public void moveLeft(int pixel){
		moveX(-pixel);
	}

	//-------------------
	// Koordinata kezeles
	//-------------------
	public double getWorldXByPixel( int pixel ){
		if( null == worldSize ){

			return getWorldLengthByPixel( pixel + 1 ) - worldTranslate.getX();
			
		}else{
		
			return getWorldLengthByPixel( pixel + 1 ) + worldSize.getXMin() - worldTranslate.getX();
			
		}
		
	}
	
	public double getWorldYByPixel( int pixel ){
		
		if( null == worldSize ){
			return (getViewableSize().height - pixel) / getPixelPerUnit() - worldTranslate.getY();
		}else{			
			return (getViewableSize().height - pixel) / getPixelPerUnit() + worldSize.getYMin() - worldTranslate.getY();
		}
	}

	
	
	public double getWorldLengthByPixel( int pixel ){
		return (pixel-1) / getPixelPerUnit();
	}
	
	public int getPixelLengthByWorld( double length ){
		return (int)( getPixelPerUnit() * length + 1 );
	}
	
	
	
	public int getPixelXPositionByWorld( double x ){

		if( null == worldSize ){
			return getPixelLengthByWorld( x ) - 1;
		}else{
			return getPixelLengthByWorld( x - worldSize.getXMin() ) - 1;
		}
	}
	
	public int getPixelYPositionByWorld( double y ){
		if( null == worldSize ){
			return getPixelLengthByWorld( y ) - 1;
		}else{
			return getPixelLengthByWorld( y - worldSize.getYMin() ) - 1;
		}
	}
	
	/**
	 * Visszaadja teljes vilag meretet
	 * @return
	 */
	public Size getWorldSize(){
		return worldSize;
	}

	/**
	 * Visszaadja a teljes vilagbol lathato resz meretet
	 * @return
	 */
	public Dimension getViewableSize(){
		return coreCanvas.getPreferredSize();
	}
	
	public int getWidth() {
		return getPreferredSize().width;		
	}

	public int getHeight() {
		return getPreferredSize().height;
	}


	/**
	 * Visszaadja a lathato vilag border nelkuli szelesseget
	 * Ha a vilag kilog a befoglalo panel szelessegebol, akkor a befoglalo panel
	 * szelessege a meghatarozo
	 * Ha a vilag lotyogne a befoglalo panel szelessegeben, akkor a vilag
	 * szelessege a meghatarozo
	 * @return
	 */
	public double getViewerWidth() {
		
		//Ez a szelesseg lehetne a befoglalo panel szelessege (ha nem korlatoznank)
		double possibleWidth = super.getWidth();
			
		//Ha nincs megadva a vilag merete vagy adott oldalaranyt kell tartani
		if (null == worldSize || sidePortion.equals(SIDES_PORTION.FIX_PORTION ) ){

			//Akkor a befoglalo panel szelessege a mervado
			return possibleWidth - ( getInsets().right + getInsets().left);

		}
		
		//Ez a szelesseg a teljes vilag szelessege
//		double maxWidth = (worldSize.getWidth() + worldTranslate.getX()) * ( getPixelPerUnit() );// + ( getInsets().right + getInsets().left);
		double maxWidth = getPixelLengthByWorld(worldSize.getWidth() + worldTranslate.getX());
		
		//Ha a befoglalo panel szelessege kisebb mint a vilag szelessege
		//Vagyis a befoglalo panel teljes szelessegeben elnyulik a valo vilag		
		if (possibleWidth < maxWidth) {
			
			//Akkor a befoglalo panel szelessege a mervado
			return possibleWidth;
		
		//Ha a vilag szelessege kisebb mint a befoglalo panele
		} else {
			
			//Akkor a vilag szelessege lesz a mervado
			return maxWidth;
		}
	}
	
	/**
	 * Visszaadja a lathato vilag border nelkuli magassagat
	 * Ha a vilag kilog a befoglalo panel magassagabol, akkor a befoglalo panel
	 * magassaga a meghatarozo
	 * Ha a vilag lotyogne a befoglalo panel magassagaban, akkor a vilag
	 * magassaga a meghatarozo
	 * @return
	 */
	public double getViewerHeight() {
		
		//Ez a magassag lehetne a befoglalo panel magassaga (ha nem korlatoznank)
		double possibleHeight = super.getHeight();
	
		//Ha nincs megadva a vilag merete
		if (null == worldSize || sidePortion.equals(SIDES_PORTION.FIX_PORTION ) ){

			//Akkor a befoglalo panel magassaga a mervado
			return possibleHeight - ( getInsets().top + getInsets().bottom );

		}
	
		//Ez a magassag a teljes vilag magassag
		//double maxHeight = (worldSize.getHeight() + worldTranslate.getY()) * getPixelPerUnit();// + getInsets().top + getInsets().bottom;				
		double maxHeight = getPixelLengthByWorld(worldSize.getHeight() + worldTranslate.getY());
			
		//Ha a befoglalo panel magassaga kisebb mint a vilag magassaga
		//Vagyis a befoglalo panel teljes magassagaban elnyulik a valo vilag
		if ( possibleHeight < maxHeight ) {
			
			//Akkor a befoglalo panel magassaga a mervado
			return possibleHeight;
			
		//Ha a vilag magassaga kisebb mint a befoglalo panele
		} else {
			
			//Akkor a vilag magassaga lesz a mervado
			return maxHeight;
		}
	}
	
	//---------------------------
	//Koordinatarendszer mozgatas
	//---------------------------
	class WheelMoveListener implements MouseInputListener{
		private int startX;
		private int startY;
		private boolean canBeDragged = false;
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
		}
		@Override
		public void mousePressed(MouseEvent e) {
			if( e.getButton() == MouseEvent.BUTTON2){
				canBeDragged = true;
				startX = e.getX();
				startY = e.getY();
			}else{
				canBeDragged = false;
			}
			
		}
		@Override
		public void mouseReleased(MouseEvent e) {			
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
				int dX = startX - e.getX();
				int dY = startY - e.getY();	
				moveXY(dX, dY);
				startX = e.getX();
				startY = e.getY();
			}
			
		}
		@Override
		public void mouseMoved(MouseEvent e) {
		}
		
	}
	
	/**
	 * Az eger kormanya altal letrehozott zoom figyelo osztaly
	 */
	class WheelZoomListener implements MouseWheelListener{
		public void mouseWheelMoved(MouseWheelEvent e){
			double xCenter, yCenter;
			xCenter = getWorldXByPixel(e.getX());
			yCenter = getWorldYByPixel(e.getY());

//System.err.println(xCenter + ", " + yCenter);
//System.err.println(getPixelYPositionByWorld(31.47) + " - " + getPixelYPositionByWorld(-3) + " --- " + getViewableSize().height);

	      //Felfele tekeres -> ZoomIn
	      if (e.getWheelRotation() < 0)
	    	  zoomIn(xCenter, yCenter, e.getX(), e.getY(), 1.2 );
	      //Lefele tekeres -> ZoomOut
	      else
	    	  zoomOut(xCenter, yCenter, e.getX(), e.getY(), 1.2 );

		}//mouseWheelMoved(MouseWheelEvent e)

	}//class WheelZoomListener

	 public void zoomIn(double xCenter, double yCenter, int xPoint, int yPoint, double rate){
		 setPixelPerUnit(getPixelPerUnit() * rate );
		 
		 double possibleXTranslate = worldTranslate.getX() - getWorldLengthByPixel(xPoint) * (rate-1);
		 double possibleYTranslate = worldTranslate.getY() - getWorldLengthByPixel(getViewableSize().height-yPoint) * (rate-1);

/*		 int availableHeight = (int)super.getHeight();
		 int possibleHeight = (int)getViewerHeight();
		 double freeY = getWorldLengthByPixel(availableHeight - possibleHeight);
	
		 int availableWidth = (int)super.getWidth();
		 int possibleWidth = (int)getViewerWidth();
		 double freeX = getWorldLengthByPixel(availableWidth - possibleWidth);
		 
		 if( freeY > 0){	
			 possibleYTranslate = worldTranslate.getY() - Math.max(possibleYTranslate, -freeY);				
		 }
		 if( freeX > 0){
			 possibleXTranslate = worldTranslate.getX() - Math.max(possibleXTranslate, -freeX);
		 }
*/		 
		 worldTranslate.setX( possibleXTranslate );
		 worldTranslate.setY( possibleYTranslate );
		 
		 this.getParent().repaint();
		 coreCanvas.invalidate();
		 coreCanvas.repaint();
		 revalidate();
			  
	 }	
	 
	 public void zoomOut(double xCenter, double yCenter, int xPoint, int yPoint, double rate){
		 setPixelPerUnit(getPixelPerUnit() / rate );
		 
		 double possibleXTranslate = worldTranslate.getX() + getWorldLengthByPixel(xPoint) * (rate-1)/rate;
		 double possibleYTranslate = worldTranslate.getY() + getWorldLengthByPixel(getViewableSize().height-yPoint) * (rate-1)/rate;
/*
		 int availableHeight = (int)super.getHeight();
		 int possibleHeight = (int)getViewerHeight();
		 double freeY = getWorldLengthByPixel(availableHeight - possibleHeight);
	
		 int availableWidth = (int)super.getWidth();
		 int possibleWidth = (int)getViewerWidth();
		 double freeX = getWorldLengthByPixel(availableWidth - possibleWidth);

if( freeY > 0){	
	possibleYTranslate = worldTranslate.getY() - Math.max(possibleYTranslate, -freeY);
}
if( freeX > 0){
	possibleXTranslate = worldTranslate.getX() - Math.max(possibleXTranslate, -freeX);
}
*/
		 worldTranslate.setX( possibleXTranslate );
		 worldTranslate.setY( possibleYTranslate );
		 
		 
		 this.getParent().repaint();
		 coreCanvas.invalidate();
		 coreCanvas.repaint();
		 revalidate();
		  
	 }
	
	
	/**
	 * 
	 * @author akoel
	 *
	 */
	class CoreCanvas extends JPanel {

		private static final long serialVersionUID = 5336269435310911828L;

		JCanvas parent;
		private BufferedImage offImage;

		public CoreCanvas(JCanvas parent, Color background) {
			super();
			this.parent = parent;
			this.setBackground(background);
			
			// Eger kormanyaval letrehozott zoom figyelese
		    addMouseWheelListener(new WheelZoomListener());
		    
		    //Eger kormanyaval letrehozott move figyeles
		    WheelMoveListener myWheelMoveListener = new WheelMoveListener();
		    addMouseListener(myWheelMoveListener);
		    addMouseMotionListener(myWheelMoveListener);
		}

		  /**
		   * Amikor meghivodik a repaint()-update()-paint() harmas akkor, hogy
		   * ne torlodjon a canvas, at kell irni az update metodust
		   */
//		  public void update(Graphics g) {
//		    paint(g);
//		  }

		  /**
		   * Az canvas ervenytelenitesekor torolni kell a hatter kepet is
		   */
		  public void invalidate() {		  
		    super.invalidate();
		    offImage = null;	    
		  }
		  
		public void paintComponent(Graphics g) {

			if( !wasTransferedToMiddle ){
				wasTransferedToMiddle = true;
				setWorldTranslate( new Position( 
						( getWorldLengthByPixel( (int)(getViewerWidth()/2) ) - positionToMiddle.getX() ), 
						( getWorldLengthByPixel( (int)(getViewerHeight()/2) ) - positionToMiddle.getY() )  
						) );
			}
			
			Graphics2D offg2;
		    Graphics2D g2 = (Graphics2D) g;
		    int width = this.getWidth();
		    int height = this.getHeight();

		    super.paintComponent(g);

			if (offImage == null) {

				//Uj Canvas keszitese
				offImage = (BufferedImage) createImage(width, height);

				//Az uj canvas grafikai objektumanak elkerese
				offg2 = (Graphics2D) offImage.getGraphics();
				
				offg2.setColor(Color.GREEN);
				offg2.fillRect(0, 0, width, height);
				
				offg2.scale(1,-1);

				//Most tolom el a koordinatarendszert
				//offg2.translate((int)(getPixelPerUnit()*(worldTranslate.getX())), (int)(getPixelPerUnit()*(worldTranslate.getY()))-getHeight() + 0);
				offg2.translate( getPixelLengthByWorld(worldTranslate.getX()) - 1, getPixelLengthByWorld( worldTranslate.getY()) - getHeight() );
				
				if (null != underList) {
					for (PainterListener painter : underList) {
						painter.paintByWorldPosition(parent, new JGraphics(parent, offg2));
						painter.paintByViewer(parent, offg2);
					}
				}

				if (null != middleList) {
					for (PainterListener painter : middleList) {
						painter.paintByWorldPosition(parent, new JGraphics(parent, offg2));
						painter.paintByViewer(parent, offg2);
					}
				}

				if (null != aboveList) {
					for (PainterListener painter : aboveList) {
						painter.paintByWorldPosition(parent, new JGraphics(parent, offg2));
						painter.paintByViewer(parent, offg2);
					}
				}
				
			}
			if (offImage != null) {
				
				//Kirajzolja  a bufferelt kepet
				g2.drawImage(offImage, 0, 0, this);
				
				g2.scale(1,-1);
//				g2.translate(0,getHeight());
//				g2.translate((int)(getPixelPerUnit()*(worldTranslate.getX())), (int)(getPixelPerUnit()*(worldTranslate.getY()))-getHeight() + 0);			
				
				g2.translate( getPixelLengthByWorld(worldTranslate.getX()) -1, getPixelLengthByWorld( worldTranslate.getY()) - getHeight() );
				
				if (null != temporaryList) {
					for (PainterListener painter : temporaryList) {
						painter.paintByWorldPosition(parent, new JGraphics(parent, g2));
					}
					temporaryList.clear();
				}				
				
			}
		
		}

		/**
		 * A lathato vilag merete
		 */
		public Dimension getPreferredSize() {
			double mH, wH;
			double pixelWidth, pixelHeight;

			// Felveszi a szulo ablak meretet csokkentve a keret meretevel
			pixelWidth = parent.getViewerWidth(); // - ( parent.getInsets().right + parent.getInsets().left );
			pixelHeight = parent.getViewerHeight(); // - ( parent.getInsets().top + parent.getInsets().bottom );
	
			/**
			 * Ha a lathato teruelt oldalainak aranyanak meg kell egyeznie az eredeti
			 * terulet oldalainak aranyaval.
			 * A teljes vilag latszik a rajzolofeluleten
			 * Beallitja a rajzolhato feluletet az oldalaranyoknak megfeleloen
			 * a szulo-ablak aktualis merete alapjan.
			 */
			if( sidePortion == SIDES_PORTION.FIX_PORTION ){
			
				 /**
		         * A szulo ablak oldalainak aranya(model meret)
		         */
		        mH = pixelWidth / pixelHeight;

		        /**
		         * Az abrazolando vilag oldalainak aranya (world meret)
		         */
		        wH = worldSize.getWidth() / worldSize.getHeight();

		        /**
		         * Ha a vilag tomzsibb mint a modell
		         */
		        if(wH > mH ){

		          /**
		           * Akkor a model magassagat kell csokkenteni
		           */
		          pixelHeight = pixelWidth / wH;
		          
		          /**
		           * Ha a vilag elnyujtottabb (magassagban) mint a modell
		           */
		        }else{

		          /**
		           * Akkor a modell szelesseget kell csokkenteni
		           */
		          pixelWidth = wH * pixelHeight;
		        }
				
		        pixelPerUnit = pixelWidth / worldSize.getWidth();				
			}
			
//System.err.println( ((int)Math.round(pixelHeight) + 1) + " - " + getPixelYPositionByWorld(-3) + ", " + getPixelYPositionByWorld(31.47) + " --- " + getWorldXPositionByPixel(getPixelYPositionByWorld(31.47)));
			
			//Felfele kerekitek, de ezzel az utolso pixel lekerdezese, rossz eredmenyt adhat. 
			//Ezt a getWorld... fuggvenyeknel figyelembe kell venni			
				return new Dimension( (int)(pixelWidth), (int)(pixelHeight));

		}
		
	

	}
}
