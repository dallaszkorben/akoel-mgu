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
		g2.drawLine(canvas.getPixelXPositionByWorld(x1), canvas.getPixelYPositionByWorldBeforeTranslate(y1), canvas.getPixelXPositionByWorld(x2), canvas.getPixelYPositionByWorldBeforeTranslate(y2) );	
	}
	
	public void fillOval( double x, double y, double width, double height){
		g2.fillOval(canvas.getPixelXPositionByWorld(x), canvas.getPixelYPositionByWorldBeforeTranslate(y), canvas.getPixelXLengthByWorld(width)-1, canvas.getPixelYLengthByWorld(height)-1);
	}

	public void drawOval( double x, double y, double width, double height){
		g2.drawOval(canvas.getPixelXPositionByWorld(x), canvas.getPixelYPositionByWorldBeforeTranslate(y), canvas.getPixelXLengthByWorld(width)-1, canvas.getPixelYLengthByWorld(height)-1);
	}

	public void drawRectangle( double x, double y, double width, double height ){
		g2.drawRect( canvas.getPixelXPositionByWorld(x), canvas.getPixelYPositionByWorldBeforeTranslate(y), canvas.getPixelXLengthByWorld(width), canvas.getPixelYLengthByWorld(height));
	}

	public void fillRectangle( double x, double y, double width, double height ){
		g2.fillRect( canvas.getPixelXPositionByWorld(x), canvas.getPixelYPositionByWorldBeforeTranslate(y), canvas.getPixelXLengthByWorld(width), canvas.getPixelYLengthByWorld(height));
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
		textLayout.draw( g2, canvas.getPixelXPositionByWorld(x), canvas.getPixelYPositionByWorldBeforeTranslate(-y) );
		g2.scale(1,-1);
	}
}
