package hu.akoel.mgu.jcanvas.own;

import hu.akoel.mgu.jcanvas.own.JCanvas.Level;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;


public class JGrid {
	
	public static enum Type{
		SOLID,
		CROSS,
		DOT,
	}
	
	public static enum PainterPosition{
		DEEPEST,
		MIDDLE,
		HIGHEST
	}
	
	private Level deepness;
	private JCanvas canvas;
//	private Color color;
	private PainterPosition position;
	Position deltaGrid;
	int crossLengthInPixel = 3;
//	Type type;

	public JGrid( JCanvas canvas, final Type type, final Color color, final int widthInPixel, PainterPosition position, final Position deltaGrid ){
		this.canvas = canvas;
//		this.color = color;
		this.position = position;
//		this.type = type;
		
		PainterListener painterListener = new PainterListener( ) {
			
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
				
				if(type.equals( Type.SOLID ) ){
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
						crossXLength = crossLengthInPixel / canvas.getPixelPerUnitX();
						crossYLength = crossLengthInPixel / canvas.getPixelPerUnitX();
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
