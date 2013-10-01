package hu.akoel.mgu.jcanvas;

import java.awt.*;
import java.math.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
//h.translate(Math.rint(measurement.getDistanceToPixel(-worldViewable.getStartX())), Math.rint(measurement.getDistanceToPixel(-worldViewable.getStartY())));
public class OutsideAxisPart extends AxisPartAdapter{
  private int frameWide;
  private Color background;

  public OutsideAxisPart(Measurement measurement, Size viewable, int orientation, int frameWide){
    super(measurement, viewable, orientation);
    this.frameWide = frameWide;
    background = Color.lightGray;
  }

  public void paint(Graphics2D g2){
    BigDecimal steps = getSteps(), disMainStick;
    double lineStart, lineStop, pixPosition, pixSecondaryDistance = 0;
    Rectangle rect = g2.getClip().getBounds();
    int x = rect.x, y = rect.y, width = rect.width - 1, height = rect.height - 1;

    if (orientation != NONE){

      //Hatter beszinezese
      g2.setColor(this.background);
      g2.fillRect(x, y, width, height);

      //A segedvonasok tavolsaga pixelben
      pixSecondaryDistance = measurement.getDistanceToPixel(steps.doubleValue() / secondaryStickNumber);
    }

    if (orientation == HORIZONTAL){

      //Vonal huzas
      g2.setColor(lineColor);
      g2.drawLine(x, y + frameWide, x + width - 1, y + frameWide);

      //A fovonas kezdeti helyzetenek kiszamolasa
      disMainStick = ((new BigDecimal(viewable.getStartX())).divide(steps, 0, BigDecimal.ROUND_CEILING)).multiply(steps);
      disMainStick = disMainStick.subtract(steps);

      //Amig a fovonas ki nem fut
      while(disMainStick.doubleValue() < viewable.getStopX()){

        //A fovonas helyzete pixelben
        pixPosition = measurement.getDistanceToPixel(disMainStick.doubleValue());

        //Fobeosztasok kijelolese
        g2.setColor(mainStickColor);
        g2.drawLine((int)pixPosition + frameWide,     y + frameWide, (int)pixPosition + frameWide,     mainStickSize + y + frameWide);
        g2.drawLine((int)pixPosition + frameWide + 1, y + frameWide, (int)pixPosition + frameWide + 1, mainStickSize + y + frameWide);
        g2.drawLine((int)pixPosition + frameWide - 1, y + frameWide, (int)pixPosition + frameWide - 1, mainStickSize + y + frameWide);

        //Ertek kiiras
        g2.setColor(numberColor);
        g2.drawString(String.valueOf(disMainStick), (int)(pixPosition - (String.valueOf(disMainStick).length() * numberFont.getSize())/2), (int)(2*mainStickSize + numberFont.getSize()) + y);

        //Mellek beosztasok kijelolese
        g2.setColor(secondaryStickColor);
        for(int i = 1; i <= secondaryStickNumber; i++){
          pixPosition+=pixSecondaryDistance;
          g2.drawLine((int)pixPosition + frameWide, y + frameWide, (int)pixPosition + frameWide, secondaryStickSize + y + frameWide);
        }

        disMainStick = disMainStick.add(steps);
      }

      //Title kiiras
      g2.setColor(titleColor);
      g2.drawString(titleText, x + frameWide, (int)(2*mainStickSize + 2*numberFont.getSize()) + y);

    }else if (orientation == VERTICAL){

      //Vonal huzas
      g2.setColor(lineColor);
      g2.drawLine( x + frameWide, y, x + frameWide, y + height - 1);

      disMainStick = ((new BigDecimal(viewable.getStartY())).divide(steps, 0, BigDecimal.ROUND_CEILING)).multiply(steps);
      disMainStick = disMainStick.subtract(steps);

      while(disMainStick.doubleValue() < viewable.getStopY()){
        pixPosition = measurement.getDistanceToPixel(disMainStick.doubleValue());

        //Fobeosztas
        g2.setColor(mainStickColor);
        g2.drawLine(x + frameWide, (int)pixPosition, x + frameWide + mainStickSize, (int)pixPosition );
        g2.drawLine(x + frameWide, (int)pixPosition + 1, x + frameWide + mainStickSize, (int)pixPosition + 1);
        g2.drawLine(x + frameWide, (int)pixPosition - 1, x + frameWide + mainStickSize, (int)pixPosition - 1);

        //Ertek
        g2.setColor(numberColor);
        g2.drawString(String.valueOf(disMainStick), x + frameWide + secondaryStickSize + numberFont.getSize(), (int)(pixPosition + numberFont.getSize() / 2));

        //Mellekbeosztas
        g2.setColor(secondaryStickColor);
        for(int i = 1; i < secondaryStickNumber; i++){
          pixPosition+=pixSecondaryDistance;
          g2.drawLine(x + frameWide, (int)pixPosition, x + frameWide + secondaryStickSize, (int)pixPosition);
        }

        disMainStick = disMainStick.add(steps);
      }
      //Title kiiras
      g2.setColor(titleColor);
      g2.drawString(titleText, x + frameWide + secondaryStickSize + numberFont.getSize(), y + numberFont.getSize() + frameWide);

    }

    if (orientation != NONE){
      //Keret kirajzolasa
      g2.setColor(this.background);
      g2.draw3DRect(x, y, width, height, true);
    }

  }//paint

}//class OutsideAxisPart