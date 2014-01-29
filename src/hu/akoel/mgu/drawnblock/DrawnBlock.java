package hu.akoel.mgu.drawnblock;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import hu.akoel.mgu.MGraphics;


public abstract class DrawnBlock extends Block{
	
	public static enum Status{
		NORMAL,
		SELECTED,
		INFOCUS,
		INPROCESS
	};
	
	private Status status;
	
	private Color color;
	private Color backgroundColor;
	private Stroke stroke;
		
	private Color normalColor = Color.white;
	private Stroke normalStroke = new BasicStroke(1);
	private Color normalBackgroundColor = Color.black;;
	
	private Color selectedColor = Color.red;
	private Stroke selectedStroke = new BasicStroke(3);
	private Color selectedBackgroundColor = Color.black;
	
	private Color infocusColor = Color.yellow;
	private Stroke infocusStroke = new BasicStroke(1);
	private Color infocusBackgroundColor = Color.black;
	
	private Color inprocessColor = Color.red;
	private Stroke inprocessStroke = new BasicStroke(3);
	private Color inprocessBackgroundColor = Color.black;
	
	public DrawnBlock( Status status, double x1, double y1, double x2, double y2 ){
		super( x1, y1, x2, y2 );
		setStatus(status);
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
	
}
