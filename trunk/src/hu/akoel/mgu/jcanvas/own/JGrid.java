package hu.akoel.mgu.jcanvas.own;

import hu.akoel.mgu.jcanvas.own.JCanvas.Level;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;


public class JGrid {
	
	public static enum Type{
		SOLID,
		DASHED,
		CROSS,
		DOT,
	}
	
	public static enum PainterPosition{
		DEEPEST,
		MIDDLE,
		HIGHEST
	}
	
	private JCanvas canvas;
	private Type type;
	private Color color;
	private int widthInPixel;
	private PainterPosition painterPosition;
	private Position deltaGrid;
	int crossLengthInPixel = 3;
	PainterListener painterListener;

	public JGrid( JCanvas canvas, Type type, Color color, int widthInPixel, PainterPosition painterPosition, Position deltaGrid ){
		this.canvas = canvas;
		this.type = type;
		this.color = color;
		this.widthInPixel = widthInPixel;
		this.painterPosition = painterPosition;
		this.deltaGrid = deltaGrid;
		
		painterListener = new GridPainterListener( );
		
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
	
	public void setCrossLengthInPixel( int crossLengthInPixel ){
		this.crossLengthInPixel = crossLengthInPixel;
	}
	
/*	public void setDeltaGrid( Position deltaGrid ){
		this.deltaGrid = deltaGrid;
	}
*/
	
	public double getDeltaGridX(){
		return this.deltaGrid.getX();
	}

	public double getDeltaGridY(){
		return this.deltaGrid.getY();
	}

	public void setDeltaGridX( double deltaGridX ){
		this.deltaGrid.setX( deltaGridX );
	}
	
	public void setDeltaGridY( double deltaGridY ){
		this.deltaGrid.setY( deltaGridY );
	}
	
	public void setWidthInPixel( int widthInPixel ){
		this.widthInPixel = widthInPixel;
	}
	
	public void setColor( Color color ){
		this.color = color;
	}
	
	public void setType( Type type ){
		this.type = type;
	}
	
	class GridPainterListener implements PainterListener{

		@Override
		public void paintByWorldPosition(JCanvas canvas, JGraphics g2) {
			int n, m;
			double xStart, yStart;
			double yPosition, xPosition;
			double crossXLength, crossYLength;
			
			Size size = canvas.getWorldSize();
			
			xStart = (int)(size.xMin / deltaGrid.getX()) * deltaGrid.getX();
			yStart = (int)(size.yMin / deltaGrid.getY()) * deltaGrid.getY();				
			
			g2.setColor( color );
			g2.setStroke(new BasicStroke(widthInPixel));
			
			if(type.equals( Type.SOLID ) || type.equals( Type.DASHED ) ){
				
				if( type.equals( Type.DASHED )){
					g2.setStroke( new BasicStroke( widthInPixel, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{1,4}, 0) );
				}
				
				m = 1;
				xPosition = xStart;
				while ( xPosition <= size.xMax ){
					g2.drawLine(xPosition, size.yMin, xPosition, size.yMax);
					xPosition = xStart + m * deltaGrid.getX(); //!!!!!!!!
		           m++;
		         }

		         n = 1;
		         yPosition = yStart;
		         while ( yPosition <= size.yMax ){
		           g2.drawLine( size.xMin, yPosition, size.xMax, yPosition );
		           yPosition = yStart + n * deltaGrid.getY(); //!!!!!!!!
		           n++;
		         }
		         
			}else{
				if(type.equals( Type.CROSS ) ){
					crossXLength = crossLengthInPixel / canvas.getPixelPerUnit().getX();
					crossYLength = crossLengthInPixel / canvas.getPixelPerUnit().getY();
				}else{
					crossXLength = 0;
					crossYLength = 0;
				}
		        
		    	 n = 1;
		    	 xPosition = xStart;
		    	 while (xPosition <= size.xMax ){
		    		 m = 1;
		    		 yPosition = yStart;
		    		 while (yPosition <= size.yMax ){
		    			 g2.drawLine( xPosition - crossXLength, yPosition, xPosition + crossXLength, yPosition );
		    			 g2.drawLine( xPosition, yPosition - crossYLength, xPosition, yPosition + crossYLength );

		    			 yPosition = yStart + m * deltaGrid.getY() ; //!!!!!!!!
		    			 m++;
		    		 }//while
		    		 
		    		 xPosition = xStart + n * deltaGrid.getX(); //!!!!!!!!
		    		 n++;
		    	 }//while
			}
			
		}

		@Override
		public void paintByViewer(JCanvas canvas, Graphics2D g2) {	}
		
	}
	
}
