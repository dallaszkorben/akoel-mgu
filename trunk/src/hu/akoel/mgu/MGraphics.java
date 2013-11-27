package hu.akoel.mgu;



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

public class MGraphics {

	private MCanvas canvas;
	private Graphics2D g2;
		
	public MGraphics(MCanvas canvas, Graphics2D g2){
		this.canvas = canvas;
		this.g2 = g2;
	}
	
	public void drawLine( double x1, double y1, double x2, double y2){	
		g2.drawLine(
				Math.round((float)canvas.getPixelXPositionByWorldBeforeTranslate(x1)), 
				Math.round((float)canvas.getPixelYPositionByWorldBeforeTranslate(y1)), 
				Math.round((float)canvas.getPixelXPositionByWorldBeforeTranslate(x2)), 
				Math.round((float)canvas.getPixelYPositionByWorldBeforeTranslate(y2)) );	
	}
	
	public void fillOval( double x, double y, double width, double height){
		g2.fillOval(
				Math.round((float)canvas.getPixelXPositionByWorldBeforeTranslate(x)), 
				Math.round((float)canvas.getPixelYPositionByWorldBeforeTranslate(y)), 
				Math.round((float)canvas.getPixelXLengthByWorld(width)-1), 
				Math.round((float)canvas.getPixelYLengthByWorld(height)-1));
	}

	public void drawOval( double x, double y, double width, double height){
		g2.drawOval(
				Math.round((float)canvas.getPixelXPositionByWorldBeforeTranslate(x)), 
				Math.round((float)canvas.getPixelYPositionByWorldBeforeTranslate(y)), 
				Math.round((float)canvas.getPixelXLengthByWorld(width)-1), 
				Math.round((float)canvas.getPixelYLengthByWorld(height)-1));
	}

	public void drawRectangle( double x1, double y1, double x2, double y2 ){
		
		int x = canvas.getPixelXPositionByWorldBeforeTranslate(x1);
		int y = canvas.getPixelYPositionByWorldBeforeTranslate(y1);
		
		int xEnd = canvas.getPixelXPositionByWorldBeforeTranslate(x2);
		int yEnd = canvas.getPixelYPositionByWorldBeforeTranslate(y2);
		
		int width = xEnd - x;
		int height = yEnd - y;
		
		g2.drawRect( x, y, width, height ); 
	}

	public void fillRectangle( double x1, double y1, double x2, double y2 ){
		
		int x = canvas.getPixelXPositionByWorldBeforeTranslate(x1);
		int y = canvas.getPixelYPositionByWorldBeforeTranslate(y1);
		
		int xEnd = canvas.getPixelXPositionByWorldBeforeTranslate(x2);
		int yEnd = canvas.getPixelYPositionByWorldBeforeTranslate(y2);
		
		int width = xEnd - x;
		int height = yEnd - y;
		
		g2.fillRect( x, y, width, height ); 
	}

	public void setColor( Color color ){
		g2.setColor( color );
	}
	
	public void setBackground( Color color ){
		g2.setBackground(color);
	}
	
	public void setStroke( Stroke stroke ){
		g2.setStroke(stroke);
	}
	
	public FontRenderContext getFontRenderContext(){
		return g2.getFontRenderContext();
	}
	
	public void drawFont( TextLayout textLayout, double x, double y ){
		g2.scale(1,-1);
		textLayout.draw( g2, Math.round((float)canvas.getPixelXPositionByWorldBeforeTranslate(x)), Math.round((float)canvas.getPixelYPositionByWorldBeforeTranslate(-y)) );
		g2.scale(1,-1);
	}
}
