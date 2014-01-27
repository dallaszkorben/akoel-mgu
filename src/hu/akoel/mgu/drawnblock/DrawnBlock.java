package hu.akoel.mgu.drawnblock;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import hu.akoel.mgu.MGraphics;


public  class DrawnBlock {
	
	public static enum Status{
		NORMAL,
		SELECTED,
		INFOCUS,
		INPROCESS
	};
	
	private double x1;
	private double y1;
	private double x2;
	private double y2;
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
		setStatus(status);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
		
		if( status.equals( Status.NORMAL ) ){
			color = normalColor;
			backgroundColor = normalBackgroundColor;
			stroke = normalStroke;
		}else if( status.equals( Status.SELECTED ) ){
			color = selectedColor;
			backgroundColor = selectedBackgroundColor;
			stroke = selectedStroke;
		}else if( status.equals( Status.INFOCUS ) ){
			color = infocusColor;
			backgroundColor = infocusBackgroundColor;
			stroke = infocusStroke;
		}else if( status.equals( Status.INPROCESS ) ){
			color = inprocessColor;
			backgroundColor = inprocessBackgroundColor;
			stroke = inprocessStroke;
		}

	}

	public void setNormal( Color color, Stroke stroke, Color backgroundColor ){
		this.normalColor = color;
		this.normalStroke = stroke;
		this.normalBackgroundColor = backgroundColor;
		
		setStatus( this.status );
	}

	public void setSelected( Color color, Stroke stroke, Color backgroundColor ){
		this.selectedColor = color;
		this.selectedStroke = stroke;
		this.selectedBackgroundColor = backgroundColor;
		
		setStatus( this.status );
	}
	
	public void setInfocus( Color color, Stroke stroke, Color backgroundColor ){
		this.infocusColor = color;
		this.infocusStroke = stroke;
		this.infocusBackgroundColor = backgroundColor;
		
		setStatus( this.status );
	}

	public void setInprocess( Color color, Stroke stroke, Color backgroundColor ){
		this.inprocessColor = color;
		this.inprocessStroke = stroke;
		this.inprocessBackgroundColor = backgroundColor;
		
		setStatus( this.status );
	}

	public double getX1() {
		return x1;
	}

	public void setX1(double x1) {
		this.x1 = x1;
	}

	public double getY1() {
		return y1;
	}

	public void setY1(double y1) {
		this.y1 = y1;
	}

	public double getX2() {
		return x2;
	}

	public void setX2(double x2) {
		this.x2 = x2;
	}

	public double getY2() {
		return y2;
	}

	public void setY2(double y2) {
		this.y2 = y2;
	}

	public void draw( MGraphics g2 ){
		
		g2.setColor( backgroundColor );		
		g2.fillRectangle(x1, y1, x2, y2);
		
		g2.setColor( color );
		g2.setStroke( stroke );
		g2.drawRectangle(x1, y1, x2, y2);
	}
	
}
