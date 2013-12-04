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
		
		int x1p = canvas.getPixelXPositionByWorldBeforeTranslate(x1);
		int y1p = canvas.getPixelYPositionByWorldBeforeTranslate(y1);
		int x2p = canvas.getPixelXPositionByWorldBeforeTranslate(x2);
		int y2p = canvas.getPixelYPositionByWorldBeforeTranslate(y2);
		
		g2.drawLine( x1p, y1p, x2p, y2p );
					
	}
	
	public void fillOval( double x, double y, double r ){
		
		int rp = canvas.getPixelXLengthByWorld( r );
		if( rp == 0 ){
			r = 0;
		}
		
		int xp = canvas.getPixelXPositionByWorldBeforeTranslate(x - r);
		int yp = canvas.getPixelYPositionByWorldBeforeTranslate(y - r);
				
		if( rp == 0 ){
			g2.drawLine( xp, yp, xp, yp );
		}else{
			g2.fillOval( xp, yp, 2*rp, 2*rp );
		}
	}

	public void drawOval( double x, double y, double r ){
		
		int rp = canvas.getPixelXLengthByWorld( r );
		if( rp == 0 ){
			r = 0;
		}
		
		int xp = canvas.getPixelXPositionByWorldBeforeTranslate(x - r);
		int yp = canvas.getPixelYPositionByWorldBeforeTranslate(y - r);
				
		if( rp == 0 ){
			g2.drawLine( xp, yp, xp, yp );
		}else{
			g2.drawOval( xp, yp, 2*rp, 2*rp );
		}
	}

	public void drawRectangle( double x1, double y1, double x2, double y2 ){
		
		int x = canvas.getPixelXPositionByWorldBeforeTranslate(x1);
		int y = canvas.getPixelYPositionByWorldBeforeTranslate(y1);
		
		int xEnd = canvas.getPixelXPositionByWorldBeforeTranslate(x2);
		int yEnd = canvas.getPixelYPositionByWorldBeforeTranslate(y2);
		
		int width = xEnd - x;
		int height = yEnd - y;
		
		if( width < 0 ){
			x = xEnd;
			width *= -1;
		}
		
		if( height < 0 ){
			y = yEnd;
			height *= -1;
		}
		
		g2.drawRect( x, y, width, height ); 
	}

	public void fillRectangle( double x1, double y1, double x2, double y2 ){
		
		int x = canvas.getPixelXPositionByWorldBeforeTranslate(x1);
		int y = canvas.getPixelYPositionByWorldBeforeTranslate(y1);
		
		int xEnd = canvas.getPixelXPositionByWorldBeforeTranslate(x2);
		int yEnd = canvas.getPixelYPositionByWorldBeforeTranslate(y2);
		
		int width = xEnd - x;
		int height = yEnd - y;
		
		if( width < 0 ){
			x = xEnd;
			width *= -1;
		}
		
		if( height < 0 ){
			y = yEnd;
			height *= -1;
		}
		
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
