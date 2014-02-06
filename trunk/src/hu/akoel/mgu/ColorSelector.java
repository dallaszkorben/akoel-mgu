package hu.akoel.mgu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ColorSelector extends JComboBox<Integer>{

	private static final long serialVersionUID = -5968583501958576573L;

	private static final int PREFERED_WIDTH = 50;
	private static final int PREFERRED_HEIGHT = 12;
	private static final int ICON_WIDTH = 30;
	private static final int ICON_HEIGHT = 10;
	
	BufferedImage colorBufferedImage; 
	Graphics2D g2;
	ImageIcon icon;
	
	ImageIcon[] images;
	private static final Color[] DEFAULT_COLOR_LIST = { 
			Color.black, 
			Color.red, 
			Color.yellow, 
			Color.blue,
			Color.green,
			Color.cyan,
			Color.magenta,
			Color.white
	}; 
	
	private Color[] colorList;
	    
	public ColorSelector(){
		super();
		commonConstructor( DEFAULT_COLOR_LIST );
	}
	
	public ColorSelector( Color[] cl ){
		super();
		
		commonConstructor( cl );
	}
	
	private void commonConstructor( Color[] cl ){
		
		colorList = cl;
		
		//Most gyartja le a szineket
		images = new ImageIcon[ colorList.length ];
		Integer[] intArray = new Integer[ colorList.length ];
        for (int i = 0; i < colorList.length; i++) {
        	intArray[i] = new Integer(i);
        	this.addItem( intArray[i] );
        	
        	colorBufferedImage = new BufferedImage( ICON_WIDTH, ICON_HEIGHT, BufferedImage.TYPE_INT_RGB); 
			g2 = colorBufferedImage.createGraphics();
			g2.setColor( colorList[ i ] );
			g2.fillRect( 0, 0, ICON_WIDTH, ICON_HEIGHT );
			icon = new ImageIcon( colorBufferedImage );
			images[ i ] = icon;
        }
	
        ComboBoxRenderer renderer= new ComboBoxRenderer();
        renderer.setPreferredSize(new Dimension( PREFERED_WIDTH, PREFERRED_HEIGHT));
        this.setRenderer(renderer);
        this.setMaximumRowCount(6);
	}
	

	/**
	 * Beallitja hogy a megadott szin legyen kivalasztva a listabol
	 * Ha nem talalja a szint, akkor marad az utljara kivalasztotton
	 * 
	 * @param color
	 */
	public void setSelectedItem( Color color ){
		for( int i = 0; i < colorList.length; i++ ){
			if( color.equals( colorList[i] ) ){
				this.setSelectedIndex( i );
				break;
			}
		}
	}
	
	public Color getSelectedColor(){
		return colorList [this.getSelectedIndex()];
	}
	
	
	/**
	 * Szineket mint szinek jeleniti meg a ComboBox-ban
	 * 
	 * @author afoldvarszky
	 *
	 */
	class ComboBoxRenderer extends JLabel implements ListCellRenderer {
		
		private static final long serialVersionUID = -1604564045340814374L;
		
//		private Font uhOhFont;
		
		public ComboBoxRenderer() {
			setOpaque(true);
			setHorizontalAlignment(CENTER);
			setVerticalAlignment(CENTER);
		}

		public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

			//Az eppen kivalasztott elem sorszama
			int selectedIndex = ((Integer)value).intValue();

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			
			icon = images[ selectedIndex ];
			setIcon(icon);

			//Letrehozza a szin ikont
//			BufferedImage colorBufferedImage = new BufferedImage( ICON_WIDTH, ICON_HEIGHT, BufferedImage.TYPE_INT_RGB); 
//			Graphics2D g2 = colorBufferedImage.createGraphics();
//			g2.setColor( Color.red );
//			g2.fillRect( 0, 0, ICON_WIDTH, ICON_HEIGHT );
//			ImageIcon icon = new ImageIcon( colorBufferedImage );
			
/*			String pet = petStrings[selectedIndex];

			if (icon != null) {
				setText(pet);
				setFont(list.getFont());
			} else {
				setUhOhText(pet + " (no image available)",	list.getFont());
			}
*/
			return this;
		}
		
/*		//Set the font and text when no image was found.
        protected void setUhOhText(String uhOhText, Font normalFont) {
            if (uhOhFont == null) { //lazily create this font
                uhOhFont = normalFont.deriveFont(Font.ITALIC);
            }
            setFont(uhOhFont);
            setText(uhOhText);
        }
*/
		
	}
	
}
