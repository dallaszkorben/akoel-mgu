package hu.akoel.mgu.sprite;

import hu.akoel.mgu.MGraphics;

import java.awt.Color;
import java.awt.Stroke;


public class RectangleElement extends SpriteElement{
	
	private double x;
	double y;
	double width;
	double height;
	private Color color;
	private Stroke stroke;
	
	public RectangleElement( double x, double y, double width, double height, Color color, Stroke stroke ){
		this.color = color;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.stroke = stroke;
	}

	@Override
	public void draw(MGraphics g2) {
		g2.setColor(color);
		g2.setStroke(stroke);
		g2.drawRectangle(x + getTranslateX(), y + getTranslateY(), width, height);		
	}
	

	
}
