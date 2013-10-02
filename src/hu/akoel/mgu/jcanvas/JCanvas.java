package hu.akoel.mgu.jcanvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.Border;


class JCanvas extends JPanel {

	public enum POSITION {
		DEEPEST, HIGHEST
	}

	private static final long serialVersionUID = 44576557802932936L;

	private Dimension size;
	
	//PERMANENT listak
	ArrayList<PainterListener> aboveList = new ArrayList<PainterListener>();
	ArrayList<PainterListener> middleList = new ArrayList<PainterListener>();
	ArrayList<PainterListener> underList = new ArrayList<PainterListener>();
	
	//TEMPORARY lista
	ArrayList<PainterListener> temporaryList = new ArrayList<PainterListener>();

	CoreCanvas coreCanvas;

	/**
	 * 
	 * @param borderType A Canvas kore rajzoladno keret tipuse. null eseten nincs keret
	 * @param background A Canvas hatterszine. null eseten az eredeti szurke
	 * @param size A Canvas maximalis merete. null eseten barmekkorara bovitheto
	 */
	public JCanvas(Border borderType, Color background, Dimension size) {
		super();

		this.setBorder(borderType);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.size = size;

//		coreCanvas = new CoreCanvas(this, background);
//		this.add(coreCanvas);

		this.add( getCoreCanvas( this, background ) );
	}
	
	CoreCanvas getCoreCanvas(JCanvas canvas, Color background ){
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
	
	public int getWidth() {
		int possibleWidth = super.getWidth();
		int maxWidth = size.width + getInsets().right + getInsets().left;
		if (null == size || possibleWidth <= maxWidth) {
			return possibleWidth;
		} else {
			return maxWidth;
		}
	}

	public int getHeight() {
		int possibleHeight = super.getHeight();
		int maxHeight = size.height + getInsets().top + getInsets().bottom;
		if (null == size || possibleHeight <= maxHeight) {
			return possibleHeight;
		} else {
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

		JCanvas parent;
		private BufferedImage offImage;

		public CoreCanvas(JCanvas parent, Color background) {
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
    
				if (null != underList) {
					for (PainterListener painter : underList) {
						painter.paint(this, offg2);
					}
				}

				if (null != middleList) {
					for (PainterListener painter : middleList) {
						painter.paint(this, offg2);
					}
				}

				if (null != aboveList) {
					for (PainterListener painter : aboveList) {
						painter.paint(this, offg2);
					}
				}
			}
			
			if (offImage != null) {

				//Kirajzolja  a bufferelt kepet
				g2.drawImage(offImage, 0, 0, this);
				
				if (null != temporaryList) {
					for (PainterListener painter : temporaryList) {
						painter.paint(this, g2);
					}
					temporaryList.clear();
				}
			}
		}

		//TODO ez meg hatra van. Meg kell oldani az aranytartast
		public Dimension getPreferredSize() {
			double mH = 0, wH;
			int pixelWidth, pixelHeight;
			int worldWidth, worldHeight;

			// Felveszi a szulo ablak meretet csokkentve a keret meretevel
			pixelWidth = parent.getWidth() - ( parent.getInsets().right + parent.getInsets().left );
			pixelHeight = parent.getHeight() - ( parent.getInsets().top + parent.getInsets().bottom );

			/**
			 * Csak akkor szamol tovabb, ha a szulo-ablak mar megjelent
			 */
			if (pixelWidth != 0 && pixelHeight != 0) {

			}
			return new Dimension(pixelWidth, pixelHeight);

		}

	}
}
