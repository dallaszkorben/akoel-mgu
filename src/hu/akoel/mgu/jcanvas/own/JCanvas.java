package hu.akoel.mgu.jcanvas.own;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.Border;


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
	private double unitToPixelPortion;
//	private Point worldPixelTranslate = new Point(0,0); //pixelben adott eltolas
	private Position worldTranslate = new Position( 0.0, 0.0 );
	
	//PERMANENT listak
	ArrayList<PainterListener> aboveList = new ArrayList<PainterListener>();
	ArrayList<PainterListener> middleList = new ArrayList<PainterListener>();
	ArrayList<PainterListener> underList = new ArrayList<PainterListener>();
	
	//TEMPORARY lista
	ArrayList<PainterListener> temporaryList = new ArrayList<PainterListener>();

	CoreCanvas coreCanvas;
	SIDES_PORTION sidePortion;

	
	
	/**
	 * 
	 * @param borderType A Canvas kore rajzoladno keret tipuse. null eseten nincs keret
	 * @param background A Canvas hatterszine. null eseten az eredeti szurke
	 * @param worldSize A Canvas maximalis merete. null eseten barmekkorara bovitheto
	 * @param unitToPixelPortion Megadja, hogy a valo vilag 1 egysege hany kepernyo pixelnek felel meg. Alapertelmezetten 1:1
	 * @param sidePortion Megadja, hogy az oldalak aranya milyen modon valtozhat. FREE_PORTION eseten
	 * az oldalak meretet a befoglalo panel merete hatarozza meg elsosorban.
	 * FIX_PORTION eseten viszont az egesz vilag latszik a vilag oldalaranyainak megfelelo oldalarannyal
	 */
	public JCanvas(Border borderType, Color background, Size worldSize, double unitToPixelPortion, SIDES_PORTION sidePortion ) {
		super();

		if( sidePortion.equals( SIDES_PORTION.FIX_PORTION ) && null == worldSize ){
			throw new Error("In case of FIX_PORTION it is required to set the worldSize. Now it is null.");
		}
		
		this.setBorder(borderType);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.worldSize = worldSize;
		this.unitToPixelPortion = unitToPixelPortion;
		this.sidePortion = sidePortion;

		this.add( getCoreCanvas( this, background ) );
	}
	
	/**
	 * If the unitToPixelPortion is set then the sidePortion is FREE_PORTION
	 * 
	 * @param borderType
	 * @param background
	 * @param worldSize
	 * @param unitToPixelPortion
	 */
	public JCanvas(Border borderType, Color background, Size worldSize, double unitToPixelPortion ) {
		
		this.commonConstructor(borderType, background, worldSize);		
		this.unitToPixelPortion = unitToPixelPortion;
		this.sidePortion = SIDES_PORTION.FREE_PORTION;
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
	public double getUnitToPixelPortion(){
		return unitToPixelPortion;
	}
	
	public void setUnitToPixelPortion( double unitToPixelPortion ){
		this.unitToPixelPortion = unitToPixelPortion;
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
	
	public void moveDown(int pixel){		
		double lengthByPixel = getWorldLengthByPixel(pixel);
		double possibleYTranslate = worldTranslate.getY() + lengthByPixel;
		
		if( possibleYTranslate <= 0 ){
			worldTranslate.setY( possibleYTranslate );
		}else{
			worldTranslate.setY(0);
		}
		coreCanvas.invalidate();
		coreCanvas.repaint();
		
	}

	public void moveUp(int pixel){
		double possibleYTranslate = worldTranslate.getY() - getWorldLengthByPixel(pixel);
		double possibleYMax = worldSize.getYMin() + getWorldLengthByPixel(getViewableSize().height) - possibleYTranslate;

		if( possibleYMax <= worldSize.getYMax() ){		
			worldTranslate.setY( possibleYTranslate );
		}else{
			worldTranslate.setY(-(worldSize.getHeight()-getWorldLengthByPixel(getViewableSize().height-1)));
		}
		coreCanvas.invalidate();
		coreCanvas.repaint();
		
	}

	public void moveRight(int pixel){	
		double possibleXTranslate = worldTranslate.getX() - getWorldLengthByPixel(pixel);
		double possibleXMax = worldSize.getXMin() + getWorldLengthByPixel(getViewableSize().width) - possibleXTranslate;
		
		if( possibleXMax <= worldSize.getXMax() ){
			worldTranslate.setX( possibleXTranslate );
		}else{
			worldTranslate.setX(-(worldSize.getWidth()-getWorldLengthByPixel(getViewableSize().width-1)));
		}
		
		coreCanvas.invalidate();
		coreCanvas.repaint();
	}
	
	public void moveLeft(int pixel){
		double possibleXTranslate = worldTranslate.getX() + getWorldLengthByPixel(pixel);
		
		if( possibleXTranslate <= 0){
			worldTranslate.setX( possibleXTranslate );
		}else{
			worldTranslate.setX( 0 );
		}
		coreCanvas.invalidate();
		coreCanvas.repaint();
	}

	public double getWorldXPositionByPixel( int pixel ){
		return getWorldLengthByPixel( pixel ) + worldSize.getXMin() - worldTranslate.getX();
	}
	
	public double getWorldYPositionByPixel( int pixel ){
		return getWorldLengthByPixel(getViewableSize().height - 1 - pixel) + worldSize.getYMin() - worldTranslate.getY();
	}
	
	public double getWorldLengthByPixel( int pixel ){
		return pixel / getUnitToPixelPortion();
	}
	
	public int getPixelLengthByWorld( double length ){
		return (int)Math.round( getUnitToPixelPortion() * length );
	}
	
	public int getPixelXPositionByWorld( double xPosition ){

		return (int)Math.round( getUnitToPixelPortion() * ( xPosition - worldSize.getXMin() ) );
	}
	
	public int getPixelYPositionByWorld( double yPosition ){

		return (int)Math.round( getUnitToPixelPortion() * ( yPosition - worldSize.getYMin() ) );
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
	 * Visszaadja a lathato vilag border vastagsaggal novelt szelesseget
	 * Ha a vilag kilog a befoglalo panel szelessegebol, akkor a befoglalo panel
	 * szelessege a meghatarozo
	 * Ha a vilag lotyogne a befoglalo panel szelessegeben, akkor a vilag
	 * szelessege a meghatarozo
	 * @return
	 */
	double getViewerWidthWithBorder() {
		
		//Ez a szelesseg lehetne a befoglalo panel szelessege (ha nem korlatoznank)
		double possibleWidth = super.getWidth();
			
		//Ha nincs megadva a vilag merete vagy adott oldalaranyt kell tartani
		if (null == worldSize || sidePortion.equals(SIDES_PORTION.FIX_PORTION ) ){

			//Akkor a befoglalo panel szelessege a mervado
			return possibleWidth;

		}
		
		//Ez a szelesseg a teljes vilag szelessege plusz a keret
		double maxWidth = (worldSize.getWidth() + worldTranslate.getX()) * ( unitToPixelPortion ) + ( getInsets().right + getInsets().left);
		
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
	 * Visszaadja a lathato vilag border vastagsaggal novelt magassagat
	 * Ha a vilag kilog a befoglalo panel magassagabol, akkor a befoglalo panel
	 * magassaga a meghatarozo
	 * Ha a vilag lotyogne a befoglalo panel magassagaban, akkor a vilag
	 * magassaga a meghatarozo
	 * @return
	 */
	double getViewerHeightWithBorder() {
		
		//Ez a magassag lehetne a befoglalo panel magassaga (ha nem korlatoznank)
		double possibleHeight = super.getHeight();
	
		//Ha nincs megadva a vilag merete
		if (null == worldSize || sidePortion.equals(SIDES_PORTION.FIX_PORTION ) ){

			//Akkor a befoglalo panel magassaga a mervado
			return possibleHeight;

		}
	
		//Ez a magassag a teljes vilag magassag plusz a keret
		double maxHeight = (worldSize.getHeight() + worldTranslate.getY()) * unitToPixelPortion + getInsets().top + getInsets().bottom;				
			
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
	
	/**
	 * Az eger kormanya altal letrehozott zoom figyelo osztaly
	 */
	class WheelZoomListener implements MouseWheelListener{
		public void mouseWheelMoved(MouseWheelEvent e){
			double xCenter, yCenter;
			xCenter = getWorldXPositionByPixel(e.getX());
			yCenter = getWorldYPositionByPixel(e.getY());

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
		 setUnitToPixelPortion(getUnitToPixelPortion() * rate );

		 worldTranslate.setX( worldTranslate.getX() - getWorldLengthByPixel(xPoint) * (rate-1) );
		 worldTranslate.setY( worldTranslate.getY() - getWorldLengthByPixel(getViewableSize().height-yPoint) * (rate-1) );
		 
		 this.getParent().repaint();
		 coreCanvas.invalidate();
		 coreCanvas.repaint();
		 revalidate();
			  
	 }	
	 
	 public void zoomOut(double xCenter, double yCenter, int xPoint, int yPoint, double rate){
		 setUnitToPixelPortion(getUnitToPixelPortion() / rate );

		 worldTranslate.setX( worldTranslate.getX() + getWorldLengthByPixel(xPoint) * (rate-1)/rate);
		 worldTranslate.setY( worldTranslate.getY() + getWorldLengthByPixel(getViewableSize().height-yPoint) * (rate-1)/rate );
		 
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
				offg2.translate(getPixelLengthByWorld(worldTranslate.getX()), getPixelLengthByWorld(worldTranslate.getY())-getHeight() + 1);			
				
				if (null != underList) {
					for (PainterListener painter : underList) {
						painter.paintByWorldPosition(this, new JGraphics(parent, offg2));
						painter.paintByViewer(this, offg2);
					}
				}

				if (null != middleList) {
					for (PainterListener painter : middleList) {
						painter.paintByWorldPosition(this, new JGraphics(parent, offg2));
						painter.paintByViewer(this, offg2);
					}
				}

				if (null != aboveList) {
					for (PainterListener painter : aboveList) {
						painter.paintByWorldPosition(this, new JGraphics(parent, offg2));
						painter.paintByViewer(this, offg2);
					}
				}
				
			}
			if (offImage != null) {
				
				//Kirajzolja  a bufferelt kepet
				g2.drawImage(offImage, 0, 0, this);
				
				g2.scale(1,-1);
//				g2.translate(0,getHeight());
				g2.translate(getPixelLengthByWorld(worldTranslate.getX()), getPixelLengthByWorld(worldTranslate.getY())-getHeight() + 1);
				
				if (null != temporaryList) {
					for (PainterListener painter : temporaryList) {
						painter.paintByWorldPosition(this, new JGraphics(parent, g2));
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
			pixelWidth = parent.getViewerWidthWithBorder() - ( parent.getInsets().right + parent.getInsets().left );
			pixelHeight = parent.getViewerHeightWithBorder() - ( parent.getInsets().top + parent.getInsets().bottom );
	
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
				
		        unitToPixelPortion = pixelWidth / worldSize.getWidth();				
			}
			
//System.err.println( ((int)Math.round(pixelHeight) + 1) + " - " + getPixelYPositionByWorld(-3) + ", " + getPixelYPositionByWorld(31.47) + " --- " + getWorldXPositionByPixel(getPixelYPositionByWorld(31.47)));				return new Dimension( (int)Math.round(pixelWidth) + 1, (int)Math.round(pixelHeight) + 1);

		}
		
	

	}
}
