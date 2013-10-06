package hu.akoel.mgu.jcanvas;

import hu.akoel.mgu.jcanvas.JCanvas.CoreCanvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.Border;

class JCoordCanvas extends JCanvas {

	private static final long serialVersionUID = -2394129144233207776L;


	
	/**
	 * 
	 * @param borderType A Canvas kore rajzoladno keret tipuse. null eseten nincs keret
	 * @param background A Canvas hatterszine. null eseten az eredeti szurke
	 * @param size A Canvas maximalis merete. null eseten barmekkorara bovitheto
	 */
	public JCoordCanvas(Border borderType, Color background, Dimension size, SIDES_PORTION sideType) {
		super(borderType, background, size, sideType);

	}

	CoreCanvas getCoreCanvas(JCanvas canvas, Color background ){
		if( null == coreCanvas ){
			coreCanvas = new CoordCoreCanvas( this, background );
		}
		return coreCanvas;
	}
	
	/**
	 * 
	 * @author akoel
	 *
	 */
	class CoordCoreCanvas extends CoreCanvas {

		private static final long serialVersionUID = 5336269565310911828L;

		public CoordCoreCanvas(JCoordCanvas parent, Color background) {
			super(parent, background);			
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
