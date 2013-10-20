package hu.akoel.mgu.jcanvas.own;

import hu.akoel.mgu.jcanvas.own.JCanvas.Level;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.math.BigDecimal;


public class JAxis {
	
	public static enum AxisPosition{
		AT_ZERO_ZERO,
		AT_LEFT_BOTTOM,
		AT_LEFT_TOP,
		AT_RIGHT_BOTTOM,
		AT_RIGHT_TOP,		
	}
	
	public static enum PainterPosition{
		DEEPEST,
		MIDDLE,
		HIGHEST
	}
	
	int mainStickSizeInPixel = 8;
	int secondaryStickSizeInPixel = 6;
	int secondaryStickNumberInPixel = 8;
    double[] units = new double[]{0.25, 0.5, 1};
    int minimalDistanceInPixel = 40;
	    
	public JAxis( final JCanvas canvas, final AxisPosition axisPosition, final Color axisColor, final int axisWidthInPixel, PainterPosition painterPosition ){
		
		PainterListener painterListener = new PainterListener( ) {
			
			@Override
			public void paintByWorldPosition(JCanvas canvas, JGraphics g2) {
				double positionXVerticalAxis = 0;
				double positionYHorizontalAxis = 0;
				
				double positionYNumber = 0;
				double positionXNumber = 0;
				
				BigDecimal xSteps = getXSteps(), mainXStick;
				
				FontRenderContext frc;
				TextLayout textLayout;
				Font fontNumber = new Font("Default", Font.PLAIN, 14);
				
				if( axisPosition.equals( AxisPosition.AT_ZERO_ZERO) ){
					positionXVerticalAxis = 0;
					positionYHorizontalAxis = 0;
					
					positionYNumber = canvas.getPixelYPositionByWorldBeforeTranslate(positionYHorizontalAxis) - mainStickSizeInPixel/2;					
					positionXNumber = canvas.getPixelXPositionByWorld( positionXVerticalAxis );
					
				}else if( axisPosition.equals( AxisPosition.AT_LEFT_BOTTOM )){
					positionXVerticalAxis = canvas.getWorldXByPixel( 0 );
					positionYHorizontalAxis = canvas.getWorldYByPixel( canvas.getViewableSize().height - 1 );
					
					
					positionYNumber = canvas.getPixelYPositionByWorldBeforeTranslate(canvas.getWorldSize().yMax) - mainStickSizeInPixel;
					positionYNumber = 0;
					positionXNumber = canvas.getPixelXPositionByWorld( positionXVerticalAxis );
					
					
					
					
				}else if( axisPosition.equals( AxisPosition.AT_LEFT_TOP)){
					positionXVerticalAxis = canvas.getWorldXByPixel( 0 );
					positionYHorizontalAxis = canvas.getWorldSize().yMax;	
					
					positionYNumber =  mainStickSizeInPixel;					
					positionXNumber = canvas.getPixelXPositionByWorld( positionXVerticalAxis );

				}else if( axisPosition.equals( AxisPosition.AT_RIGHT_BOTTOM ) ){
					positionXVerticalAxis = canvas.getWorldSize().xMax;
					positionYHorizontalAxis = canvas.getWorldYByPixel( 0 );
					
					positionYNumber = canvas.getPixelYPositionByWorldBeforeTranslate(positionYHorizontalAxis) - mainStickSizeInPixel/2;					
					positionXNumber = canvas.getPixelXPositionByWorld( positionXVerticalAxis );

				}else if( axisPosition.equals( AxisPosition.AT_RIGHT_TOP ) ){
					positionXVerticalAxis = canvas.getWorldSize().xMax;
					positionYHorizontalAxis = canvas.getWorldSize().yMin;
				}
				
				g2.setColor( axisColor );
				g2.setStroke(new BasicStroke(axisWidthInPixel));

				//Vizszintes axis rajzolasa
				g2.drawLine(canvas.getWorldSize().xMin, positionYHorizontalAxis, canvas.getWorldSize().xMax, positionYHorizontalAxis);
				
				//Fuggoleges axis rajzolasa
				g2.drawLine(positionXVerticalAxis, canvas.getWorldSize().yMin, positionXVerticalAxis, canvas.getWorldSize().yMax);
				
				//Vizszintes fobeosztasok es feliratok
				mainXStick = ((new BigDecimal(canvas.getWorldSize().xMin)).divide(xSteps, 0, BigDecimal.ROUND_CEILING)).multiply(xSteps);
				mainXStick = mainXStick.subtract(xSteps);

				g2.setStroke(new BasicStroke(1));
				while(mainXStick.doubleValue() < canvas.getWorldSize().xMax){
				
					//Vizszintes fobeosztas
					g2.drawLine(
							mainXStick.doubleValue(), 
							positionYHorizontalAxis - canvas.getWorldYLengthByPixel(mainStickSizeInPixel)/2, 
							mainXStick.doubleValue(), 
							positionYHorizontalAxis + canvas.getWorldYLengthByPixel(mainStickSizeInPixel)/2 
					);
					
				
					frc = g2.getFontRenderContext();
					textLayout = new TextLayout(String.valueOf(mainXStick), fontNumber, frc);
					g2.drawFont(textLayout, canvas.getPixelXPositionByWorld(mainXStick.doubleValue()), canvas.getPixelYPositionByWorldBeforeTranslate(canvas.getWorldSize().yMin) + fontNumber.getSize() + mainStickSizeInPixel);
			        
				        
					
					//Kovetkezo fobeosztas
					mainXStick = mainXStick.add(xSteps);
				}
			}
			
			@Override
			public void paintByViewer(JCanvas canvas, Graphics2D g2) {							
			}
			
			  
			
			/**
			 * Visszaadja a parameterkent megadott szamhoz legkozelebbi jobb oldali
			 * 10 valamelyik hatvanyat
			 */
			private BigDecimal getNextOrder(BigDecimal base){
				int counter;
				  
			    //base > 0
			    if(base.compareTo(BigDecimal.valueOf(0)) > 0){
			      counter = 0;

			      //base >= 1
			      if (base.compareTo(BigDecimal.valueOf(1)) >= 0) {
			        while (base.compareTo(BigDecimal.valueOf(1)) > 0) {
			          base = base.movePointLeft(1);
			          counter ++;
			        }
			      }
			      //base < 1
			      else {
			        counter = 1;
			        while (base.compareTo(BigDecimal.valueOf(1)) < 0) {
			          base = base.movePointRight(1);
			          counter--;
			        }
			      }
			      BigDecimal ten = new BigDecimal(1);
			      ten = ten.movePointRight(counter);

			      return ten;
			    }else
			    	return base;
			}
			
			protected BigDecimal getXSteps(){

			    //A vilagban minimum ekkora lepeseket kell tennem, hogy a minimalis tavolsagot
			    //tudjam tartani a kepernyon
			    BigDecimal minMarkerDistance = BigDecimal.valueOf( canvas.getWorldXLengthByPixel(minimalDistanceInPixel) );
			    BigDecimal actualMarkerDistance = BigDecimal.valueOf( canvas.getWorldXLengthByPixel(minimalDistanceInPixel) );

			    int i = 0;
			    while(units.length > i){
			      actualMarkerDistance = getNextOrder(minMarkerDistance).multiply(new BigDecimal(units[i]));
			      if(actualMarkerDistance.doubleValue() >= minMarkerDistance.doubleValue()){
			        break;
			      }
			      i++;
			    }//while


			    return actualMarkerDistance;
			}// getSteps()			
			  
			  
		};
		
		if( painterPosition.equals(PainterPosition.DEEPEST ) ){
			canvas.addPainterListenerToDeepest(painterListener, Level.ABOVE);
		}else if( painterPosition.equals( PainterPosition.MIDDLE)){
			canvas.addPainterListenerToMiddle(painterListener, Level.ABOVE);
		}else if( painterPosition.equals(PainterPosition.HIGHEST)){
			canvas.addPainterListenerToHighest(painterListener, Level.ABOVE);
		}
		
	}
	
}
