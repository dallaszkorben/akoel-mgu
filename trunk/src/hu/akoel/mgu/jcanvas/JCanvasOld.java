package hu.akoel.mgu.jcanvas;

import hu.akoel.mgu.jcanvas.own.JGraphics;
import hu.akoel.mgu.jcanvas.own.Offset;
import hu.akoel.mgu.jcanvas.own.Size;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.Border;


public class JCanvasOld extends JPanel {

	public enum POSITION {
		DEEPEST, HIGHEST
	}

	//Az oldalak aranyara vonatkozo szabaly a befoglalo kontener meretvaltozasanak fuggvenyeben
	public enum SIDES_TYPE{
		
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
	private BigDecimal unitToPixelPortion;
//	private Point worldPixelTranslate = new Point(0,0); //pixelben adott eltolas
//	private Offset worldTranslate = null; //new Offset( 0.0, 0.0 );
	private Point pixelTranslate = new Point(0, 0);
	
	//PERMANENT listak
	ArrayList<PainterListener> aboveList = new ArrayList<PainterListener>();
	ArrayList<PainterListener> middleList = new ArrayList<PainterListener>();
	ArrayList<PainterListener> underList = new ArrayList<PainterListener>();
	
	//TEMPORARY lista
	ArrayList<PainterListener> temporaryList = new ArrayList<PainterListener>();

	CoreCanvas coreCanvas;
	SIDES_TYPE sideType;

	/**
	 * 
	 * @param borderType A Canvas kore rajzoladno keret tipuse. null eseten nincs keret
	 * @param background A Canvas hatterszine. null eseten az eredeti szurke
	 * @param worldSize A Canvas maximalis merete. null eseten barmekkorara bovitheto
	 * @param unitToPixelPortion Megadja, hogy a valo vilag 1 egysege hany kepernyo pixelnek felel meg. Alapertelmezetten 1:1
	 * @param sideType
	 */
	public JCanvasOld(Border borderType, Color background, Size worldSize, BigDecimal unitToPixelPortion, SIDES_TYPE sideType ) {
		super();

		this.setBorder(borderType);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.worldSize = worldSize;
		this.unitToPixelPortion = unitToPixelPortion;
		this.sideType = sideType;

//		coreCanvas = new CoreCanvas(this, background);
//		this.add(coreCanvas);

		this.add( getCoreCanvas( this, background ) );
	}
	
	public BigDecimal getUnitToPixelPortion(){
		return unitToPixelPortion;
	}
	
	public CoreCanvas getCoreCanvas(JCanvasOld canvas, Color background ){
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
	
	public void moveUp(int pixel){		
		pixelTranslate.setLocation( pixelTranslate.x, Math.min(0, pixelTranslate.y + pixel ) );
		worldTranslate = null;
/*		BigDecimal lengthByPixel = getWorldLengthByPixel(pixel);
		worldTranslate.setY(worldTranslate.getY().add(lengthByPixel).min(BigDecimal.valueOf(0)));
*/		
		coreCanvas.invalidate();
		coreCanvas.repaint();		 
	}

	public void moveDown(int pixel){
//TODO		
//		if( coreCanvas.getHeight() - worldTranslate.y + pixel <= worldSize.height ){ 
		pixelTranslate.setLocation( pixelTranslate.x, pixelTranslate.y - pixel);
		worldTranslate = null;
/*		BigDecimal lengthByPixel = getWorldLengthByPixel(pixel);
		worldTranslate.setY(worldTranslate.getY().subtract(lengthByPixel));
*/		
		coreCanvas.invalidate();
		coreCanvas.repaint();
//		}
	}

	public void moveRight(int pixel){
//		if( coreCanvas.getWidth() - worldPixelTranslate.x + pixel <= worldSize.width ){
			pixelTranslate.setLocation( Math.min(0, pixelTranslate.x - pixel), pixelTranslate.y );
			worldTranslate = null;;
/*			BigDecimal lengthByPixel = getWorldLengthByPixel(pixel);
			worldTranslate.setX(worldTranslate.getX().subtract(lengthByPixel));
*/
			coreCanvas.invalidate();
			coreCanvas.repaint();
//		}
	}
	
	public void moveLeft(int pixel){
		pixelTranslate.setLocation( Math.min(0, pixelTranslate.x + pixel), pixelTranslate.y );
/*		BigDecimal lengthByPixel = getWorldLengthByPixel(pixel);
		worldTranslate.setX(worldTranslate.getX().add(lengthByPixel).min(BigDecimal.valueOf(0)));
*/		
		worldTranslate = null;
		coreCanvas.invalidate();
		coreCanvas.repaint();
	}

	public BigDecimal getWorldLengthByPixel( int pixel ){
		return BigDecimal.valueOf(1).divide(getUnitToPixelPortion(), MathContext.DECIMAL128).multiply(BigDecimal.valueOf(pixel));
	}
	
	public int getPixelLengthByWorld( BigDecimal length ){
		return (getUnitToPixelPortion().multiply(length)).intValue();
	}
	
	public int getPixelXPositionByWorld( BigDecimal xPosition ){
		return (getUnitToPixelPortion().multiply(xPosition).add(worldTranslate.getX())).intValue();
	}
	
	public int getPixelYPositionByWorld( BigDecimal yPosition ){
		return (getUnitToPixelPortion().multiply(yPosition).add(worldTranslate.getY())).intValue();
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
		return getPreferredSize();
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
	BigDecimal getViewerWidthWithBorder() {
		
		//Ez a szelesseg lehetne a befoglalo panel szelessege (ha nem korlatoznank)
		BigDecimal possibleWidth = BigDecimal.valueOf( super.getWidth() );
			
		//Ha nincs megadva a vilag merete
		if (null == worldSize ){

			//Akkor a befoglalo panel szelessege a mervado
			return possibleWidth;

		}
		
		//Ez a szelesseg a teljes vilag szelessege plusz a keret
		BigDecimal maxWidth = (worldSize.getWidth().multiply( unitToPixelPortion ).add( BigDecimal.valueOf(getInsets().right + getInsets().left) ));
		
		//Ha a befoglalo panel szelessege kisebb mint a vilag szelessege
		//Vagyis a befoglalo panel teljes szelessegeben elnyulik a valo vilag		
		if (possibleWidth.compareTo(maxWidth) == -1) {
			
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
	BigDecimal getViewerHeightWithBorder() {
		
		//Ez a magassag lehetne a befoglalo panel magassaga (ha nem korlatoznank)
		BigDecimal possibleHeight = BigDecimal.valueOf( super.getHeight() );
	
		//Ha nincs megadva a vilag merete
		if (null == worldSize ){

			//Akkor a befoglalo panel magassaga a mervado
			return possibleHeight;

		}
		
		//Ez a magassag a teljes vilag magassag plusz a keret
		BigDecimal maxHeight = (worldSize.getHeight().multiply( unitToPixelPortion ).add( BigDecimal.valueOf(getInsets().top + getInsets().bottom)));				
		
		//Ha a befoglalo panel magassaga kisebb mint a vilag magassaga
		//Vagyis a befoglalo panel teljes magassagaban elnyulik a valo vilag
		if ( possibleHeight.compareTo( maxHeight) == -1 ) {
			
			//Akkor a befoglalo panel magassaga a mervado
			return possibleHeight;
			
		//Ha a vilag magassaga kisebb mint a befoglalo panele
		} else {
			
			//Akkor a vilag magassaga lesz a mervado
			return maxHeight;
		}
	}
	
	/**
	 * 
	 * @author akoel
	 *
	 */
	class CoreCanvas extends JPanel {

		private static final long serialVersionUID = 5336269435310911828L;

		JCanvasOld parent;
		private BufferedImage offImage;

		public CoreCanvas(JCanvasOld parent, Color background) {
			super();
			this.parent = parent;
			this.setBackground(background);
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
			
//System.err.println("width: " + this.getWidth() + " height: " + this.getHeight());

			if (offImage == null) {

				//Uj Canvas keszitese
				offImage = (BufferedImage) createImage(width, height);

				//Az uj canvas grafikai objektumanak elkerese
				offg2 = (Graphics2D) offImage.getGraphics();
//TODO    
//				offg2.translate(worldPixelTranslate.getXDouble(), worldPixelTranslate.getYDouble());
		
				if (null != underList) {
					for (PainterListener painter : underList) {
						painter.paint(this, new JGraphics(parent, offg2));
					}
				}

				if (null != middleList) {
					for (PainterListener painter : middleList) {
						painter.paint(this, new JGraphics(parent, offg2));
					}
				}

				if (null != aboveList) {
					for (PainterListener painter : aboveList) {
						painter.paint(this, new JGraphics(parent, offg2));
					}
				}
				
			}
			if (offImage != null) {
				
				//Kirajzolja  a bufferelt kepet
				g2.drawImage(offImage, 0, 0, this);
				
				if (null != temporaryList) {
					for (PainterListener painter : temporaryList) {
						painter.paint(this, new JGraphics(parent, g2));
					}
					temporaryList.clear();
				}
				
			}
		
		}

		/**
		 * Az igazi lathato vilag merete
		 */
		public Dimension getPreferredSize() {
			BigDecimal mH, wH;
			BigDecimal pixelWidth, pixelHeight;

			// Felveszi a szulo ablak meretet csokkentve a keret meretevel
			pixelWidth = parent.getViewerWidthWithBorder().subtract( BigDecimal.valueOf( parent.getInsets().right + parent.getInsets().left ) );
			pixelHeight = parent.getViewerHeightWithBorder().subtract( BigDecimal.valueOf( parent.getInsets().top + parent.getInsets().bottom ) );
			
			/**
			 * Ha a lathato teruelt oldalainak aranyanak meg kell egyeznie az eredeti
			 * terulet oldalainak aranyaval.
			 * A teljes vilag latszik a rajzolofeluleten
			 * Beallitja a rajzolhato feluletet az oldalaranyoknak megfeleloen
			 * a szulo-ablak aktualis merete alapjan.
			 */
			if( sideType == SIDES_TYPE.FIX_PORTION ){
			
				 /**
		         * A szulo ablak oldalainak aranya(model meret)
		         */
		        mH = pixelWidth.divide( pixelHeight );

		        /**
		         * Az abrazolando vilag oldalainak aranya (world meret)
		         */
		        wH = worldSize.getWidth().divide(worldSize.getHeight());

		        /**
		         * Ha a vilag tomzsibb mint a modell
		         */
		        if(wH.compareTo( mH ) == 1 ){

		          /**
		           * Akkor a model magassagat kell csokkenteni
		           */
		          pixelHeight = pixelWidth.divide( wH );

		          /**
		           * Ha a vilag elnyujtottabb (magassagban) mint a modell
		           */
		        }else{

		          /**
		           * Akkor a modell szelesseget kell csokkenteni
		           */
		          pixelWidth = wH.multiply( pixelHeight );
		        }
				
/*			}else{
				pixelWidth.add( worldTranslate.getX() );
				pixelHeight.add( worldTranslate.getY() );
*/				
			}
				return new Dimension(pixelWidth.intValue(), pixelHeight.intValue());

		}

	}
}
