package hu.akoel.mgu.jcanvas.own;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;


public class JCanvas extends JPanel {

	public static enum Level {
		UNDER, ABOVE
	}

	//Az oldalak aranyara vonatkozo szabaly a befoglalo kontener meretvaltozasanak fuggvenyeben
	public static enum SIDES_PORTION{
		
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
	private double pixelPerUnitX, pixelPerUnitY;
	private Position worldTranslate = new Position( 0.0, 0.0 );
	private Position positionToMiddle = null;
	private boolean wasTransferedToMiddle = false;
	
	//PERMANENT listak
	ArrayList<PainterListener> highestList = new ArrayList<PainterListener>();
	ArrayList<PainterListener> middleList = new ArrayList<PainterListener>();
	ArrayList<PainterListener> deepestList = new ArrayList<PainterListener>();
	
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
		
		if( pixelPerUnit <= 0 ){
			throw new Error("In case of FREE_PORTION it is required to set the pixelPerUnit to a real number. Now it is: " + pixelPerUnit );
		}
		
		this.setSidePortion( SIDES_PORTION.FREE_PORTION );
		
		this.commonConstructor(borderType, background, null);		
		this.setPixelPerUnitX(pixelPerUnit);
		this.setPixelPerUnitY(pixelPerUnit);
		
		this.positionToMiddle = positionToMiddle;
		
		//Ha nem adtam meg eltolast a koordinatarendszernek
		if(null == positionToMiddle ){
			setWasTransferedToMiddle(true);
/*			setWorldTranslate( new Position( 
				( getWorldXLengthByPixel( (int)(getViewerWidth()/2) ) - getPositionToMiddleX() ), 
				( getWorldYLengthByPixel( (int)(getViewerHeight()/2) ) - getPositionToMiddleY() )  
				)
				 
			);
*/		

		}else{
			setWasTransferedToMiddle(false);
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

		this.setSidePortion( SIDES_PORTION.FIX_PORTION );
		
		this.commonConstructor(borderType, background, worldSize);
						
		//Jelzem, hogy megtorent az eltolas. Persze nem volt, csak nem akarom, hogy megtortenjen
		setWasTransferedToMiddle( true );
		
		setWorldTranslate( new Position( -worldSize.xMin, -worldSize.yMin ));
	}
	
	private void commonConstructor(Border borderType, Color background, Size worldSize ){
		this.setBorder(borderType);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.setWorldSize( worldSize );
		this.add( getCoreCanvas( this, background ) );
	}
	
	public SIDES_PORTION getSidePortion(){
		return sidePortion;
	}
	
	public void setSidePortion( SIDES_PORTION sidePortion ){
		this.sidePortion = sidePortion;
	}
	
	public Position getWorldTranslate(){
		return worldTranslate;
	}
	
	public void setWorldTranslate(Position worldTranslate){
		this.worldTranslate = worldTranslate;
	}
	
	public void setWorldTranslateX( double x ){
		this.worldTranslate.setX(x);
	}
	
	public void setWorldTranslateY( double y){
		this.worldTranslate.setY(y);
	}
	
	public double getWorldTranslateX(){
		return this.worldTranslate.getX();
	}
	
	public double getWorldTranslateY(){
		return this.worldTranslate.getY();
	}
	
	public double getPixelPerUnitX(){
		return pixelPerUnitX;
	}
	
	public void setPixelPerUnitX( double pixelPerUnit ){
		this.pixelPerUnitX = pixelPerUnit;
	}

	public double getPixelPerUnitY(){
		return pixelPerUnitY;
	}
	
	public void setPixelPerUnitY( double pixelPerUnit ){
		this.pixelPerUnitY = pixelPerUnit;
	}

	public double getPositionToMiddleX(){
		return this.positionToMiddle.getX();
	}
	
	public double getPositionToMiddleY(){
		return this.positionToMiddle.getY();
	}
	
	public void setPositionToMiddle( Position positionToMiddle ){
		this.positionToMiddle = positionToMiddle;
	}
	
	public void setWasTransferedToMiddle( boolean wasTransferedToMiddle ){
		this.wasTransferedToMiddle = wasTransferedToMiddle;
	}
	
	public boolean getWasTransferedToMiddle(){
		return this.wasTransferedToMiddle;
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
	public void addPainterListenerToDeepest(PainterListener painterListener, Level position) {
		if(!this.deepestList.contains(painterListener)){
			if (position.equals(Level.ABOVE)) {
				deepestList.add(painterListener);
			} else {
				deepestList.add(0, painterListener);
			}
			coreCanvas.invalidate();
		}
	}

	public void addPainterListenerToDeepest(PainterListener painterListener) {
		if(!this.deepestList.contains(painterListener)){
			addPainterListenerToDeepest(painterListener, Level.ABOVE);
		}
	}
	
	public void removePainterListenerFromDeepest( PainterListener painterListener ){
		deepestList.remove(painterListener);
		coreCanvas.invalidate();
	}
	
	public void removePainterListenersFromDeepest(){
		deepestList.clear();
		coreCanvas.invalidate();
	}

	//
	//PERMANENT - Middle list
	//
	public void addPainterListenerToMiddle(PainterListener painterListener, Level position) {
		if(!this.middleList.contains(painterListener)){
			if (position.equals(Level.ABOVE)) {
				this.middleList.add(painterListener);
			} else {
				this.middleList.add(0, painterListener);
			}
			coreCanvas.invalidate();
		}
	}

	public void addPainterListenerToMiddle(PainterListener painterListener) {
		if(!this.middleList.contains(painterListener)){
			this.addPainterListenerToMiddle(painterListener, Level.ABOVE);
		}
	}
	
	public void removePainterListenerFromMiddle( PainterListener painterListener ){
		middleList.remove(painterListener);
		coreCanvas.invalidate();
	}
	
	public void removePainterListenersFromMiddle(){
		middleList.clear();
		coreCanvas.invalidate();
	}
	
	//
	//PERMANENT - Above list
	//
	public void addPainterListenerToHighest(PainterListener painterListener, Level position) {
		if(!this.highestList.contains(painterListener)){
			if (position.equals(Level.ABOVE)) {
				this.highestList.add(painterListener);
			} else {
				this.highestList.add(0, painterListener);
			}
			coreCanvas.invalidate();
		}
	}

	public void addPainterListenerToHighest(PainterListener painterListener) {
		if(!this.highestList.contains(painterListener)){
			this.addPainterListenerToHighest(painterListener, Level.ABOVE);
		}
	}
	
	public void removePainterListenerFromHighest( PainterListener painterListener ){
		highestList.remove(painterListener);
		coreCanvas.invalidate();
	}
	
	public void removePainterListenersFromHighest(){
		highestList.clear();
		coreCanvas.invalidate();
	}
	
	//
	//TEMPORARY list
	//
	public void addPainterListenerToTemporary(PainterListener painterListener, Level position) {
		
		if(!this.temporaryList.contains(painterListener)){			
			if (position.equals(Level.ABOVE)){
				this.temporaryList.add(painterListener);
			} else {			
				this.temporaryList.add(0, painterListener);
			}
		}
	}

	public void addPainterListenerToTemporary(PainterListener painterListener ) {
		if(!this.temporaryList.contains(painterListener)){
			this.addPainterListenerToTemporary(painterListener, Level.ABOVE);
		}
	}
	
	public void removePainterListenerFromTemporary( PainterListener painterListener ){
		temporaryList.remove(painterListener);
	}
	
	public void removePainterListenersFromTemporary(){
		temporaryList.clear();
	}
	
	//---------
	// Mozgatas
	//---------
	public void moveX( int pixel ){
		double possibleXTranslate = getWorldTranslate().getX();
		
		if( pixel > 0){
			possibleXTranslate -= getWorldXLengthByPixel(pixel + 1);
		}else{
			possibleXTranslate += getWorldXLengthByPixel(-pixel + 1);
		}

		setWorldTranslateX( possibleXTranslate );
		
		coreCanvas.invalidate();
		coreCanvas.repaint();
	}
	
	public void moveY( int pixel ){

		double possibleYTranslate = getWorldTranslateY();
		
		if( pixel > 0){
			possibleYTranslate += getWorldYLengthByPixel(pixel + 1);
		}else{
			possibleYTranslate -= getWorldYLengthByPixel(-pixel + 1);
		}
		
		setWorldTranslateY( possibleYTranslate );

		coreCanvas.invalidate();
		coreCanvas.repaint();
		
	}
	
	public void moveXY(int pixelX, int pixelY){
		double possibleXTranslate = getWorldTranslateX();
		
		if( pixelX > 0){
			possibleXTranslate -= getWorldXLengthByPixel(pixelX + 1);
		}else{
			possibleXTranslate += getWorldXLengthByPixel(-pixelX + 1);
		}

		setWorldTranslateX( possibleXTranslate );
		
		double possibleYTranslate = getWorldTranslateY();
		
		if( pixelY > 0){
			possibleYTranslate += getWorldYLengthByPixel(pixelY + 1);
		}else{
			possibleYTranslate -= getWorldYLengthByPixel(-pixelY + 1);
		}
		
		setWorldTranslateY( possibleYTranslate );

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
	
	/**
	 * Visszaadja a kepernyo-koordinatarendszerben (origo a bal felso sarok) talalhato
	 * pixel x poziciojanak valos X koordinatajat
	 * @param pixel kepernyo-koordinatarendszerben levo pixel x koordinataja
	 * @return valos X koordinata
	 */
	public double getWorldXByPixel( int pixel ){
//		if( getSidePortion().equals(  SIDES_PORTION.FREE_PORTION ) ){
			return getWorldXLengthByPixel( pixel + 1 ) - getWorldTranslateX();
//		}else{
//			return getWorldXLengthByPixel( pixel + 1 ) + getWorldSize().getXMin() - getWorldTranslateX();
//		}
		
	}
	
	/**
	 * Visszaadja a kepernyo-koordinatarendszerben (origo a bal felso sarok) talalhato
	 * pixel y poziciojanak valos Y koordinatajat
	 * @param pixel kepernyo-koordinatarendszerben levo pixel y koordinataja
	 * @return valos Y koordinata
	 */
	public double getWorldYByPixel( int pixel ){
		
//		if( getSidePortion().equals(  SIDES_PORTION.FREE_PORTION ) ){
			return getWorldYLengthByPixel( getViewableSize().height - pixel ) - getWorldTranslateY();
			//return (getViewableSize().height - pixel - 1) / getPixelPerUnit() - worldTranslate.getY();
//		}else{			
//			return getWorldYLengthByPixel( getViewableSize().height - pixel ) + getWorldSize().getYMin() - getWorldTranslateY();
			//return (getViewableSize().height - pixel - 1) / getPixelPerUnit() + worldSize.getYMin() - worldTranslate.getY();			
//		}
	}

	
	
	public double getWorldXLengthByPixel( int pixel ){
		return (pixel-1) / getPixelPerUnitX();
	}
	
	public int getPixelXLengthByWorld( double length ){
		double doubleLength = getPixelPerUnitX() * length;
	
		doubleLength = doubleLength + 1;

		if(doubleLength < 0)
			return -Math.round( -(float)doubleLength );
		else
		return Math.round( (float)doubleLength );
	}
	
	public double getWorldYLengthByPixel( int pixel ){
		return (pixel-1) / getPixelPerUnitY();
	}
	
	public int getPixelYLengthByWorld( double length ){
		double doubleLength = getPixelPerUnitY() * length;
		doubleLength = doubleLength + 1;
		
		if(doubleLength < 0)
			return -Math.round( -(float)doubleLength );
		else
			return Math.round( (float)doubleLength );
	}
	//Mas ne hasznalja
	public int getPixelXPositionByWorld( double x ){

//		if( getSidePortion().equals(  SIDES_PORTION.FREE_PORTION ) ){
			return getPixelXLengthByWorld( x ) - 1;
//		}else{
//			return getPixelXLengthByWorld( x - getWorldSize().getXMin() ) - 1;
//		}
	}
	
	//Mas ne hasznalja
	public int getPixelYPositionByWorldBeforeTranslate( double y ){
//		if( getSidePortion().equals(  SIDES_PORTION.FREE_PORTION ) ){
			return getPixelYLengthByWorld( y ) - 1;
//		}else{
//			return getPixelYLengthByWorld( y - getWorldSize().getYMin() ) - 1;
//		}
	}
	
	/**
	 * Visszaadja teljes vilag meretet
	 * @return
	 */
	public Size getWorldSize(){
		
		if( null == getSidePortion() ){
			return null;
			
		}else if( getSidePortion().equals(SIDES_PORTION.FIX_PORTION)){
			return worldSize;
			
		}else if( getSidePortion().equals(SIDES_PORTION.FREE_PORTION)){
			double width = getViewerWidth();
			double height = getViewerHeight();
			double xMin = getWorldXByPixel(0);
			double xMax = getWorldXByPixel((int)width-1);
			double yMax = getWorldYByPixel(0);
			double yMin = getWorldYByPixel((int)height-1);
			
			Size size = new Size(xMin, yMin, xMax, yMax);
			return size;
		}else{
			return null;
		}
	}
	
	public void setWorldSize( Size worldSize ){
		this.worldSize = worldSize;
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
//		if (null == worldSize || sidePortion.equals(SIDES_PORTION.FIX_PORTION ) ){

			//Akkor a befoglalo panel szelessege a mervado
		return possibleWidth - ( getInsets().right + getInsets().left);

//		}
/*		
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
*/		
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
//		if (null == worldSize || sidePortion.equals(SIDES_PORTION.FIX_PORTION ) ){

			//Akkor a befoglalo panel magassaga a mervado
			return possibleHeight - ( getInsets().top + getInsets().bottom );

//		}
/*	
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
*/		
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
		 setPixelPerUnitX(getPixelPerUnitX() * rate );
		 setPixelPerUnitY(getPixelPerUnitY() * rate );
		 
		 double possibleXTranslate = getWorldTranslateX() - getWorldXLengthByPixel(xPoint) * (rate-1);
		 double possibleYTranslate = getWorldTranslateY() - getWorldYLengthByPixel(getViewableSize().height-yPoint) * (rate-1);

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
		 setWorldTranslateX( possibleXTranslate );
		 setWorldTranslateY( possibleYTranslate );
		 
		 this.getParent().repaint();
		 coreCanvas.invalidate();
		 coreCanvas.repaint();
		 revalidate();
			  
	 }	
	 
	 public void zoomOut(double xCenter, double yCenter, int xPoint, int yPoint, double rate){
		 setPixelPerUnitX(getPixelPerUnitX() / rate );
		 setPixelPerUnitY(getPixelPerUnitY() / rate );
		 
		 double possibleXTranslate = getWorldTranslateX() + getWorldXLengthByPixel(xPoint) * (rate-1)/rate;
		 double possibleYTranslate = getWorldTranslateY() + getWorldYLengthByPixel(getViewableSize().height-yPoint) * (rate-1)/rate;
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
		 setWorldTranslateX( possibleXTranslate );
		 setWorldTranslateY( possibleYTranslate );
		 
		 
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

		private JCanvas parent;
		private BufferedImage offImage;
//		private Size parentWorldSize;

		public CoreCanvas(JCanvas parent, Color background) {
			super();
			this.parent = parent;
//			this.parentWorldSize = parent.getWorldSize();
			
			this.setBackground(background);
			
			//if( ha szabadon valtoztathato az oldalarany ){
			if( parent.getSidePortion().equals( SIDES_PORTION.FREE_PORTION) ){
			
				// Eger kormanyaval letrehozott zoom figyelese
				addMouseWheelListener(new WheelZoomListener());
		    
				//Eger kormanyaval letrehozott move figyeles
				WheelMoveListener myWheelMoveListener = new WheelMoveListener();
				addMouseListener(myWheelMoveListener);
				addMouseMotionListener(myWheelMoveListener);
			}
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

			if( !getWasTransferedToMiddle() ){
				setWasTransferedToMiddle( true );
				setWorldTranslate( new Position( 
						( getWorldXLengthByPixel( (int)(getViewerWidth()/2) ) - getPositionToMiddleX() ), 
						( getWorldYLengthByPixel( (int)(getViewerHeight()/2) ) - getPositionToMiddleY() )  
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
				
				offg2.setColor(getBackground());
				offg2.fillRect(0, 0, width, height);
				
				offg2.scale(1,-1);

				//Most tolom el a koordinatarendszert
				//offg2.translate((int)(getPixelPerUnit()*(worldTranslate.getX())), (int)(getPixelPerUnit()*(worldTranslate.getY()))-getHeight() + 0);
				offg2.translate( getPixelXLengthByWorld(parent.getWorldTranslateX()) - 1, getPixelYLengthByWorld( parent.getWorldTranslateY()) - getHeight() );
				
				if (null != deepestList) {
					for (PainterListener painter : deepestList) {
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

				if (null != highestList) {
					for (PainterListener painter : highestList) {
						painter.paintByWorldPosition(parent, new JGraphics(parent, offg2));
						painter.paintByViewer(parent, offg2);
					}
				}
				
			}
			if (offImage != null) {
				
				//Kirajzolja  a bufferelt kepet
				g2.drawImage(offImage, 0, 0, this);
				
				g2.scale(1,-1);
				
				g2.translate( getPixelXLengthByWorld( parent.getWorldTranslateX()) -1, getPixelYLengthByWorld( parent.getWorldTranslateY()) - getHeight() );
				
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
			if( getSidePortion().equals(  SIDES_PORTION.FIX_PORTION ) ){
			
				 /**
		         * A szulo ablak oldalainak aranya(model meret)
		         */
		        mH = pixelWidth / pixelHeight;

		        /**
		         * Az abrazolando vilag oldalainak aranya (world meret)
		         */
		        //wH = parentWorldSize.getWidth() / parentWorldSize.getHeight();
		        Size parentWorldSize = parent.getWorldSize();
		        wH = parentWorldSize.getWidth() / parentWorldSize.getHeight();
		        

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
		        
		        pixelWidth = Math.round(pixelWidth)-1;
		        pixelHeight = Math.round(pixelHeight)-1;
		       
		        parent.setPixelPerUnitX((pixelWidth) / (parentWorldSize.getWidth()));
		        parent.setPixelPerUnitY((pixelHeight) / (parentWorldSize.getHeight()));
		        
		        pixelWidth++;
		        pixelHeight++;
		        
			}		
				return new Dimension( (int)(pixelWidth), (int)(pixelHeight) );

		}	

	}
}
