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
	
	private JCanvas canvas;
	Position position;
	private Color color;
	private int widthInPixel;
	private double length;
	private PainterPosition painterPosition;
	private PainterListener painterListener;

	public JOrigo( JCanvas canvas, Position position, Color color, int widthInPixel, double length, PainterPosition painterPosition ){
		this.canvas = canvas;
		this.position = position;
		this.color = color;
		this.length = length;  
		this.widthInPixel = widthInPixel;
		this.painterPosition = painterPosition;
		
		painterListener = new OrigoPainterListener( );
		
		if( painterPosition.equals(PainterPosition.DEEPEST ) ){
			canvas.addPainterListenerToDeepest(painterListener, Level.ABOVE);
		}else if( painterPosition.equals( PainterPosition.MIDDLE)){
			canvas.addPainterListenerToMiddle(painterListener, Level.ABOVE);
		}else if( painterPosition.equals(PainterPosition.HIGHEST)){
			canvas.addPainterListenerToHighest(painterListener, Level.ABOVE);
		}
	}
	
	public void turnOff(){
		if( painterPosition.equals(PainterPosition.DEEPEST ) ){
			canvas.removePainterListenerFromDeepest(painterListener);
		}else if( painterPosition.equals( PainterPosition.MIDDLE)){
			canvas.removePainterListenerFromMiddle(painterListener);
		}else if( painterPosition.equals(PainterPosition.HIGHEST)){
			canvas.removePainterListenerFromHighest(painterListener);
		}
	}
	
	public void turnOn(){
		if( painterPosition.equals(PainterPosition.DEEPEST ) ){
			canvas.addPainterListenerToDeepest(painterListener, Level.ABOVE);
		}else if( painterPosition.equals( PainterPosition.MIDDLE)){
			canvas.addPainterListenerToMiddle(painterListener, Level.ABOVE);
		}else if( painterPosition.equals(PainterPosition.HIGHEST)){
			canvas.addPainterListenerToHighest(painterListener, Level.ABOVE);
		}	
	}
	
	public void setColor( Color color ){
		this.color = color;
	}
	
	public void setLength( double length ){
		this.length = length;
	}
	
	public void setWidthInPixel( int widthInPixel ){
		this.widthInPixel = widthInPixel;
	}
	
	class OrigoPainterListener implements PainterListener{

		@Override
		public void paintByWorldPosition(JCanvas canvas, JGraphics g2) {
			g2.setColor( color );
			g2.setStroke(new BasicStroke(widthInPixel));
			
			g2.drawLine( (position.getX() - length/2.0), position.getY(), (position.getX() + length/2.0), position.getY() );
			g2.drawLine( position.getX(), (position.getY() - length/2.0), position.getX(), (position.getY() + length/2.0) );
		}

		@Override
		public void paintByViewer(JCanvas canvas, Graphics2D g2) {}
	}
	
}
