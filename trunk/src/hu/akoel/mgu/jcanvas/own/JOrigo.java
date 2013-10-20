package hu.akoel.mgu.jcanvas.own;

import hu.akoel.mgu.jcanvas.own.JCanvas.Level;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;


public class JOrigo {
	
	public static enum PainterPosition{
		DEEPEST,
		MIDDLE,
		HIGHEST
	}
	
	private Level deepness;
	private JCanvas canvas;
//	private Color color;
	private PainterPosition position;
//	Type type;

	public JOrigo( JCanvas canvas, final Color color, final int widthInPixel, final double length, PainterPosition position ){
		this.canvas = canvas;
//		this.color = color;
		this.position = position;
//		this.type = type;
		
		PainterListener painterListener = new PainterListener( ) {
			
			@Override
			public void paintByWorldPosition(JCanvas canvas, JGraphics g2) {
				
				g2.setColor( color );
				g2.setStroke(new BasicStroke(widthInPixel));
				
				g2.drawLine( (0 - length/2.0), 0, (0 + length/2.0), 0 );
				g2.drawLine( 0, (0 - length/2.0), 0, (0 + length/2.0) );
			}
			
			@Override
			public void paintByViewer(JCanvas canvas, Graphics2D g2) {							
			}
		};
		
		if( position.equals(PainterPosition.DEEPEST ) ){
			canvas.addPainterListenerToDeepest(painterListener, Level.ABOVE);
		}else if( position.equals( PainterPosition.MIDDLE)){
			canvas.addPainterListenerToMiddle(painterListener, Level.ABOVE);
		}else if( position.equals(PainterPosition.HIGHEST)){
			canvas.addPainterListenerToHighest(painterListener, Level.ABOVE);
		}
		
	}
	
}
