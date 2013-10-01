package hu.akoel.mgu.jcanvas;

import java.awt.BorderLayout;
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
	private ArrayList<PainterListener> secondaryList = new ArrayList<PainterListener>();
	private ArrayList<PainterListener> primaryList = new ArrayList<PainterListener>();

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

		coreCanvas = new CoreCanvas(this, background);

		this.add(coreCanvas);

	}
	
	public void addPainterListenerToPrimary(PainterListener painter, POSITION position) {
		if (position.equals(POSITION.HIGHEST)) {
			primaryList.add(painter);
		} else {
			primaryList.add(0, painter);
		}
	}

	public void addPainterListenerToPrimary(PainterListener painter) {
		addPainterListenerToPrimary(painter, POSITION.HIGHEST);
	}
	
	public void removePainterListenersFromPrimary(){
		primaryList.clear();
	}

	public void addPainterListenerToSecondary(PainterListener painter, POSITION position) {
		if (position.equals(POSITION.HIGHEST)) {
			this.secondaryList.add(painter);
		} else {
			this.secondaryList.add(0, painter);
		}
	}

	public void addPainterListenerToSecondary(PainterListener painter) {
		this.addPainterListenerToSecondary(painter, POSITION.HIGHEST);
	}
	
	public void removePainterListenersFromSecondary(){
		secondaryList.clear();
	}
	
	public int getWidth() {
		int possibleWidth = super.getWidth();
		if (null == size || possibleWidth <= size.width) {
			return possibleWidth;
		} else {
			return size.width;
		}
	}

	public int getHeight() {
		int possibleHeight = super.getHeight();
		if (null == size || possibleHeight <= size.height) {
			return possibleHeight;
		} else {
			return size.height;
		}
	}

	class CoreCanvas extends JPanel {

		private static final long serialVersionUID = 5336269435310911828L;

		private JCanvas parent;
		private BufferedImage image;

		public CoreCanvas(JCanvas parent, Color background) {
			super();
			this.parent = parent;
			this.setBackground(background);
		}

		public void paintComponent(Graphics g) {
			//super.paintComponent(g);
System.err.println("width: " + this.getWidth() + " height: " + this.getHeight());
			Graphics2D g2 = (Graphics2D) g;

			if (null != primaryList) {
				for (PainterListener painter : primaryList) {
					painter.paint(this, g2);
				}
			}

			if (null != secondaryList) {
				for (PainterListener painter : secondaryList) {
					painter.paint(this, g2);
				}
			}
		}

		public Dimension getPreferredSize() {
			double mH = 0, wH;
			int pixelWidth, pixelHeight;
			int worldWidth, worldHeight;

			// Felveszi a szulo ablak meretet csokkentve a keret meretevel
			pixelWidth = parent.getWidth() - parent.getInsets().right - parent.getInsets().left;
			pixelHeight = parent.getHeight() - parent.getInsets().top - parent.getInsets().bottom;

			/**
			 * Csak akkor szamol tovabb, ha a szulo-ablak mar megjelent
			 */
			if (pixelWidth != 0 && pixelHeight != 0) {

			}
			return new Dimension(pixelWidth, pixelHeight);

		}

	}
}
