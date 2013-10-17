package hu.akoel.mgu.jcanvas.own;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.math.BigDecimal;

public class JGraphics {

	private JCanvas canvas;
	private Graphics2D g2;
		
	public JGraphics(JCanvas canvas, Graphics2D g2){
		this.canvas = canvas;
		this.g2 = g2;
	}
	
	public void drawLine( double x1, double y1, double x2, double y2){
//		BigDecimal pp = canvas.getUnitToPixelPortion();
//		g2.drawLine(pp.multiply(x1).intValue(), pp.multiply(y1).intValue(), pp.multiply(x2).intValue(), pp.multiply(y2).intValue());
//		g2.drawLine(canvas.getPixelXPositionByWorld(x1), canvas.getPixelYPositionByWorld(y1), canvas.getPixelXPositionByWorld(x2), canvas.getPixelYPositionByWorld(y2) );
		g2.drawLine(canvas.getPixelXPositionByWorld(x1), canvas.getPixelYPositionByWorldBeforeTranslate(y1), canvas.getPixelXPositionByWorld(x2), canvas.getPixelYPositionByWorldBeforeTranslate(y2) );
	}
	
	public void fillOval( double x, double y, double width, double height){
		//BigDecimal pp = canvas.getUnitToPixelPortion();
		//g2.fillOval(pp.multiply(x).intValue(), pp.multiply(y).intValue(), pp.multiply(width).intValue(), pp.multiply(height).intValue());
		g2.fillOval(canvas.getPixelXPositionByWorld(x), canvas.getPixelYPositionByWorldBeforeTranslate(y), canvas.getPixelXLengthByWorld(width)-1, canvas.getPixelYLengthByWorld(height)-1);
	}

	public void drawOval( double x, double y, double width, double height){
		//BigDecimal pp = canvas.getUnitToPixelPortion();
		//g2.fillOval(pp.multiply(x).intValue(), pp.multiply(y).intValue(), pp.multiply(width).intValue(), pp.multiply(height).intValue());
//		g2.drawOval(canvas.getPixelXPositionByWorld(x), canvas.getPixelYPositionByWorld(y), canvas.getPixelLengthByWorld(width)-1, canvas.getPixelLengthByWorld(height)-1);

//System.err.println(canvas.getPixelPerUnit() + " - " + canvas.getPreferredSize().width + " - " + canvas.getViewerWidth()  + " - " + canvas.getWorldSize().xMin + " - " + canvas.getWorldSize().xMax + " - " + canvas.getPixelXPositionByWorld(x) + " - " + (canvas.getPixelLengthByWorld(width)-1) );
//System.out.println(canvas.getWorldXByPixel(227));


		g2.drawOval(canvas.getPixelXPositionByWorld(x), canvas.getPixelYPositionByWorldBeforeTranslate(y), canvas.getPixelXLengthByWorld(width)-1, canvas.getPixelYLengthByWorld(height)-1);
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
}
