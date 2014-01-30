package hu.akoel.mgu.drawnblock;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import hu.akoel.mgu.MGraphics;


public abstract class DrawnBlock extends java.awt.geom.Rectangle2D.Double{
	
	private static final long serialVersionUID = -3676065835228116831L;

	public static enum Status{
		NORMAL,
		SELECTED,
		INFOCUS,
		INPROCESS
	};
	
	private static final Color NORMAL_COLOR = Color.white;
	private static final Stroke NORMAL_STROKE = new BasicStroke(1);
	private static final Color NORMAL_BACKGROUND = Color.black;
	
	private static final Color SELECTED_COLOR = Color.red;
	private static final Stroke SELECTED_STROKE = new BasicStroke(3);
	private static final Color SELECTED_BACKGROUND = Color.black;
	
	private static final Color INFOCUS_COLOR = Color.yellow;
	private static final Stroke INFOCUS_STROKE = new BasicStroke(1);
	private static final Color INFOCUS_BACKGROUND = Color.black;
	
	private static final Color INPROCESS_COLOR = Color.red;
	private static final Stroke INPROCESS_STROKE = new BasicStroke(3);
	private static final Color INPROCESS_BACKGROUND = Color.black;
	
	java.lang.Double minLength = null;
	java.lang.Double minWidth = null;
	java.lang.Double maxLength = null;
	java.lang.Double maxWidth = null;
	
	private Status status;
	
	private Color color;
	private Color backgroundColor;
	private Stroke stroke;
		
	private Color normalColor;
	private Stroke normalStroke; 
	private Color normalBackgroundColor;
	
	private Color selectedColor;
	private Stroke selectedStroke;
	private Color selectedBackgroundColor;
	
	private Color infocusColor;
	private Stroke infocusStroke;
	private Color infocusBackgroundColor;
	
	private Color inprocessColor;
	private Stroke inprocessStroke;
	private Color inprocessBackgroundColor;
	
	private double startX, startY;
	
	public DrawnBlock( Status status, double x1, double y1 ){
		super( x1, y1, 0, 0 );
		
		this.startX = x1;
		this.startY = y1;
		
		this.status = status;
		
		setNormal( NORMAL_COLOR, NORMAL_STROKE, NORMAL_BACKGROUND );
		setSelected( SELECTED_COLOR, SELECTED_STROKE, SELECTED_BACKGROUND );
		setInfocus(INFOCUS_COLOR, INFOCUS_STROKE, INFOCUS_BACKGROUND );
		setInprocess( INPROCESS_COLOR, INPROCESS_STROKE, INPROCESS_BACKGROUND );
				
		refreshStatus();
	}
	
	public DrawnBlock( Status status, double x1, double y1, java.lang.Double minLength, java.lang.Double maxLength, java.lang.Double minWidth, java.lang.Double maxWidth ){
		super( x1, y1, 0, 0 );
		
		this.startX = x1;
		this.startY = y1;
		
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.minWidth = minWidth;
		this.maxWidth = maxWidth;
		
/*		if( null != minLength ){
			setX2( x1 + minLength );
		}
		
		if( null != minWidth ){
			setY2( y1 + minWidth );
		}
*/		
		this.status = status;
		
		setNormal( NORMAL_COLOR, NORMAL_STROKE, NORMAL_BACKGROUND );
		setSelected( SELECTED_COLOR, SELECTED_STROKE, SELECTED_BACKGROUND );
		setInfocus(INFOCUS_COLOR, INFOCUS_STROKE, INFOCUS_BACKGROUND );
		setInprocess( INPROCESS_COLOR, INPROCESS_STROKE, INPROCESS_BACKGROUND );
				
		refreshStatus();
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
		
		if( status.equals( Status.NORMAL ) ){
			color = getNormalColor();
			backgroundColor = getNormalBackgroundColor();
			stroke = getNormalStroke();
		}else if( status.equals( Status.SELECTED ) ){
			color = getSelectedColor();
			backgroundColor = getSelectedBackgroundColor();
			stroke = getSelectedStroke();
		}else if( status.equals( Status.INFOCUS ) ){
			color = getInfocusColor();
			backgroundColor = getInfocusBackgroundColor();
			stroke = getInfocusStroke();
		}else if( status.equals( Status.INPROCESS ) ){
			color = getInprocessColor();
			backgroundColor = getInprocessBackgroundColor();
			stroke = getInprocessStroke();
		}
	}
	
	public void refreshStatus(){
		setStatus( status );
	}	
	
	public boolean enabledToChange( double x, double y ){
		double w, h;
		
		if( x < this.startX ){
			w =  this.startX - x;
		}else{
			w = x - this.startX;
		}
		
		if( y < this.startY ){
			h = this.startY - y;
		}else{
			h = y - this.startY;
		}
		
		// Ketiranyu korlatozas van megadva
		if( null != maxWidth && null != maxLength ){

			if( !( ( h <= maxWidth && w <= maxLength ) || (  w <= maxWidth && h <= maxLength ) ) ){
			
			//if( ( h <= maxWidth && w > maxLength ) || ( w <= maxWidth && h > maxLength ) || ( h <= maxLength && w > maxWidth ) || ( w <= maxLength && h > maxWidth ) || ( w > maxLength && h > maxWidth ) || ( h > maxLength && w > maxWidth ) ){
				return false;
			}
		
		// Csak az egyik oldalnak van megadva korlatozas
		} else if( null != maxWidth ){
			
			if( h > maxWidth && w > maxWidth ){
				return false;			
			}
			
		// Csak az egyik oldalnak van megadva korlatozas
		} else if( null != maxLength ){
						
			if( h > maxLength && w > maxLength ){
				return false;
			}
		}
		return true;
		
	}
	
	
	public void changeSize( double x, double y ){
		
		if( x <= this.startX ){
			this.width =  this.startX - x;
			this.x = x;
		}else{
			this.width = x - this.startX;
			this.x = startX;
		}
		
		if( y <= this.startY ){
			this.height = this.startY - y;
			this.y = y;
		}else{
			this.height = y - this.startY;
			this.y = startY;
		}
		
	}
	public double getX1() {
		return getX();
	}
	
	public double getY1() {
		return getY();
	}
	
	public double getX2() {
		return getX() + getWidth();
	}

	public double getY2() {
		return getY() + getHeight();
	}

	public java.lang.Double getMinLength() {
		return minLength;
	}

	public java.lang.Double getMinWidth() {
		return minWidth;
	}

	public java.lang.Double getMaxLength() {
		return maxLength;
	}

	public java.lang.Double getMaxWidth() {
		return maxWidth;
	}

	public Color getNormalColor() {
		return normalColor;
	}

	public Stroke getNormalStroke() {
		return normalStroke;
	}

	public Color getNormalBackgroundColor() {
		return normalBackgroundColor;
	}

	public Color getSelectedColor() {
		return selectedColor;
	}

	public Stroke getSelectedStroke() {
		return selectedStroke;
	}

	public Color getSelectedBackgroundColor() {
		return selectedBackgroundColor;
	}

	public Color getInfocusColor() {
		return infocusColor;
	}

	public Stroke getInfocusStroke() {
		return infocusStroke;
	}

	public Color getInfocusBackgroundColor() {
		return infocusBackgroundColor;
	}

	public Color getInprocessColor() {
		return inprocessColor;
	}

	public Stroke getInprocessStroke() {
		return inprocessStroke;
	}

	public Color getInprocessBackgroundColor() {
		return inprocessBackgroundColor;
	}

	public void setNormal( Color color, Stroke stroke, Color backgroundColor ){
		this.normalColor = color;
		this.normalStroke = stroke;
		this.normalBackgroundColor = backgroundColor;
		
		refreshStatus();
	}

	public void setSelected( Color color, Stroke stroke, Color backgroundColor ){
		this.selectedColor = color;
		this.selectedStroke = stroke;
		this.selectedBackgroundColor = backgroundColor;
		
		refreshStatus();
	}
	
	public void setInfocus( Color color, Stroke stroke, Color backgroundColor ){
		this.infocusColor = color;
		this.infocusStroke = stroke;
		this.infocusBackgroundColor = backgroundColor;
		
		refreshStatus();
	}

	public void setInprocess( Color color, Stroke stroke, Color backgroundColor ){
		this.inprocessColor = color;
		this.inprocessStroke = stroke;
		this.inprocessBackgroundColor = backgroundColor;
		
		refreshStatus();
	}

	public void draw( MGraphics g2 ){
		
		g2.setColor( backgroundColor );		
		g2.fillRectangle( getX1(), getY1(), getX2(), getY2());
		
		g2.setColor( color );
		g2.setStroke( stroke );
		g2.drawRectangle(getX1(), getY1(), getX2(), getY2());
	}
	
	public boolean intersectsOrContains( java.awt.geom.Rectangle2D.Double block2 ){
		double tx1 = getX1();
		double ty1 = getY1();
		double tx2 = getX2();
		double ty2 = getY2();
		
		double rx1 = block2.getX();
		double ry1 = block2.getY();
		double rx2 = block2.getX() + block2.getWidth();
		double ry2 = block2.getY() + block2.getHeight();
		
		if (tx1 < rx1) tx1 = rx1;
		if (ty1 < ry1) ty1 = ry1;
		if (tx2 > rx2) tx2 = rx2;
		if (ty2 > ry2) ty2 = ry2;
		tx2 -= tx1;
		ty2 -= ty1;
		
		//Jelzi hogy van atlapolas
		if( ( ty2 > 0 && tx2 > 0 ) ){
			return true;
		
		//Nincs atlapolas
		}else{
			return false;
		}
		
	}
	
	public boolean intersects( java.awt.geom.Rectangle2D.Double block2 ) {

		double tx1 = getX1();
		double ty1 = getY1();
		double tx2 = getX2();
		double ty2 = getY2();
		
		double rx1 = block2.getX();
		double ry1 = block2.getY();
		double rx2 = block2.getX() + block2.getWidth();
		double ry2 = block2.getY() + block2.getHeight();
		
		if (tx1 < rx1) tx1 = rx1;
		if (ty1 < ry1) ty1 = ry1;
		if (tx2 > rx2) tx2 = rx2;
		if (ty2 > ry2) ty2 = ry2;
		tx2 -= tx1;
		ty2 -= ty1;

		//Biztos, hogy keresztezi vagy tartalmazza az egyik a masikat
		if( ty2 >= 0 && tx2 >= 0 ){
			tx2 += tx1;
			ty2 += ty1;

			//Valamelyik tartalmazza a masikat
			if( 
					( ( ( tx2 - tx1 ) == ( getX2() - getX1() ) ) || ( ( tx2 - tx1 ) == ( block2.getWidth() ) ) ) &&
					( ( ( ty2 - ty1 ) == ( getY2() - getY1() ) ) || ( ( ty2 - ty1 ) == ( block2.getHeight() ) ) ) 

			){
				return false;

			//Csak keresztezesrol van szo
			}else{
				return true;
			}
			
		}else{
			
			return false;
		}

	}
	
}
