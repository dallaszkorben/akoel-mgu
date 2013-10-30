package hu.akoel.mgu.sprite;

import hu.akoel.mgu.MGraphics;

import java.awt.Color;
import java.awt.Stroke;


public class RectangleElement extends SpriteElement{
	
	private double x;
	double y;
	double width;
	double height;
	private Color normalColor;
	private Stroke normalStroke;
	private Color focusColor;
	private Stroke focusStroke;
	private Color ghostColor;
	private Stroke ghostStroke;
	
	public RectangleElement( double x, double y, double width, double height, Color color, Stroke stroke ){

		commonConstructor(x, y, width, height, color, stroke);

		this.focusColor = color;
		this.focusStroke = stroke;	
		
		this.ghostColor = color;
		this.ghostStroke = stroke;	
	}

	public RectangleElement( double x, double y, double width, double height, Color normalColor, Stroke normalStroke, Color focusColor, Stroke focusStroke ){

		commonConstructor(x, y, width, height, normalColor, normalStroke);

		this.focusColor = focusColor;
		this.focusStroke = focusStroke;		
		
		this.ghostColor = normalColor;
		this.ghostStroke = normalStroke;	
	}

	public RectangleElement( double x, double y, double width, double height, Color normalColor, Stroke normalStroke, Color focusColor, Stroke focusStroke, Color ghostColor, Stroke ghostStroke ){

		commonConstructor(x, y, width, height, normalColor, normalStroke);

		this.focusColor = focusColor;
		this.focusStroke = focusStroke;	
		
		this.ghostColor = ghostColor;
		this.ghostStroke = ghostStroke;	
	}
	
	private void commonConstructor(double x, double y, double width, double height, Color color, Stroke stroke){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.normalColor = color;
		this.normalStroke = stroke;	
	}
	
	@Override
	public void draw(MGraphics g2) {
		g2.setColor(normalColor);
		g2.setStroke(normalStroke);
		g2.drawRectangle(x + getPositionX(), y + getPositionY(), width, height);		
	}

	@Override
	public void drawFocus(MGraphics g2) {
		g2.setColor(focusColor);
		g2.setStroke(focusStroke);
		g2.drawRectangle(x + getPositionX(), y + getPositionY(), width, height);		
	}

	@Override
	public void drawGhost(MGraphics g2) {
		g2.setColor(ghostColor);
		g2.setStroke(ghostStroke);
		g2.drawRectangle(x + getPositionX(), y + getPositionY(), width, height);				
	}
	

	
}
