package hu.akoel.mgu.jcanvas;
import java.awt.*;
import java.awt.geom.*;
import java.math.*;
import java.awt.font.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class InsideAxisPart extends AxisPartAdapter{

  public InsideAxisPart(Measurement measurement, Size viewable, int orientation){
    super(measurement, viewable, orientation);
  }

  public void paint(Graphics2D g2){
    BigDecimal steps = getSteps(), mainStick;
    TextLayout textLayout;
    FontRenderContext frc;
//    Font portionNumberFont, portionTitleFont;
    float xT, yT;
    double lineStart, lineStop, position, secondaryDistance, secondaryPosition;

    secondaryDistance = measurement.getDistanceToPixel(steps.doubleValue() / secondaryStickNumber);

    if (orientation == HORIZONTAL){
      lineStart = measurement.getDistanceToPixel(viewable.getStartX());
      lineStop = measurement.getDistanceToPixel(viewable.getStopX());

      //Aranyos betumagassag valasztasa
//      portionNumberFont = new Font(numberFont.getName(), numberFont.getStyle(), Math.min(numberFont.getSize(), (int)((lineStop - lineStart)*numberFontPortion)));
//      portionTitleFont = new Font(titleFont.getName(), titleFont.getStyle(), Math.min(titleFont.getSize(), (int)((lineStop - lineStart)*titleFontPortion)));

      //vonal huzasa
      g2.setColor(lineColor);
      g2.drawLine( (int)Math.rint(lineStart), 0, (int)Math.rint(lineStop), 0);

      mainStick = ((new BigDecimal(viewable.getStartX())).divide(steps, 0, BigDecimal.ROUND_CEILING)).multiply(steps);
      mainStick = mainStick.subtract(steps);

      while(mainStick.doubleValue() < viewable.getStopX()){
        position = measurement.getDistanceToPixel(mainStick.doubleValue());

        //Fobeosztasok bejelolese
        g2.setColor(mainStickColor);
        g2.drawLine((int)Math.rint(position),     -(int)Math.rint(mainStickSize/2), (int)Math.rint(position),     (int)Math.rint(mainStickSize/2));
        g2.drawLine((int)Math.rint(position) + 1, -(int)Math.rint(mainStickSize/2), (int)Math.rint(position) + 1, (int)Math.rint(mainStickSize/2));
        g2.drawLine((int)Math.rint(position) - 1, -(int)Math.rint(mainStickSize/2), (int)Math.rint(position) - 1, (int)Math.rint(mainStickSize/2));

        //szamok kiirasa
        g2.setColor(numberColor);
        frc = g2.getFontRenderContext();
        textLayout = new TextLayout(String.valueOf(mainStick), numberFont, frc);
        if(numberPosition == NUMBER_UP)
          yT = (float)(-mainStickSize/2 - 1);
        else
          yT = (float)(mainStickSize + textLayout.getAscent());
        xT = (float)(position - textLayout.getAdvance()/2);
        textLayout.draw(g2, xT, yT);

        //Mellekbeosztasok bejelolese
        g2.setColor(secondaryStickColor);
        for(int i = 1; i <= secondaryStickNumber; i++){
          position+=secondaryDistance;
          g2.drawLine((int)Math.rint(position), -(int)Math.rint(secondaryStickSize/2), (int)Math.rint(position), (int)Math.rint(secondaryStickSize/2));
        }

        mainStick = mainStick.add(steps);
      }

      //Title kiirasa
      g2.setColor(titleColor);
      frc = g2.getFontRenderContext();
      textLayout = new TextLayout(titleText, titleFont, frc);
      if(titlePosition == TITLE_DOWN_RIGHT){
        xT =  (float)(lineStop - textLayout.getAdvance());
        yT = (float)(mainStickSize + textLayout.getAscent());
      }else if(titlePosition == TITLE_DOWN_LEFT){
        xT =  (float)(lineStart);
        yT = (float)(mainStickSize + textLayout.getAscent());
      }else if(titlePosition == TITLE_UP_RIGHT){
        xT =  (float)(lineStop - textLayout.getAdvance());
        yT = (float)(-mainStickSize + 1);
      }else{
        xT =  (float)(lineStart);
        yT = (float)(-mainStickSize + 1);
      }
      textLayout.draw(g2, xT, yT);

    }else if (orientation == VERTICAL){
      lineStart = measurement.getDistanceToPixel(viewable.getStartY());
      lineStop = measurement.getDistanceToPixel(viewable.getStopY());

//      portionNumberFont = new Font(numberFont.getName(), numberFont.getStyle(), Math.min(numberFont.getSize(), (int)((lineStop - lineStart)*numberFontPortion)));
//      portionTitleFont = new Font(titleFont.getName(), titleFont.getStyle(), Math.min(titleFont.getSize(), (int)((lineStop - lineStart)*titleFontPortion)));

      g2.setColor(lineColor);
      g2.drawLine( 0, (int)Math.rint(lineStart), 0, (int)Math.rint(lineStop));

      mainStick = ((new BigDecimal(viewable.getStartY())).divide(steps, 0, BigDecimal.ROUND_CEILING)).multiply(steps);
      mainStick = mainStick.subtract(steps);

      while(mainStick.doubleValue() < viewable.getStopY()){
        position = measurement.getDistanceToPixel(mainStick.doubleValue());

        g2.setColor(mainStickColor);
        g2.drawLine(-(int)Math.rint(mainStickSize / 2), (int)Math.rint(position),     (int)Math.rint(mainStickSize / 2), (int)Math.rint(position));
        g2.drawLine(-(int)Math.rint(mainStickSize / 2), (int)Math.rint(position) + 1, (int)Math.rint(mainStickSize / 2), (int)Math.rint(position) + 1);
        g2.drawLine(-(int)Math.rint(mainStickSize / 2), (int)Math.rint(position ) -1, (int)Math.rint(mainStickSize / 2), (int)Math.rint(position) - 1);

        //szamok kiirasa
        g2.setColor(numberColor);
        frc = g2.getFontRenderContext();
        textLayout = new TextLayout(String.valueOf(mainStick), numberFont, frc);
        if(numberPosition == NUMBER_UP)
          xT = (float)(-mainStickSize / 2 - 1 - textLayout.getAdvance());
        else
          xT = (float)(mainStickSize / 2 + 1);
        yT = (int)Math.rint(position + textLayout.getAscent()/2 + 1);
        textLayout.draw(g2, xT, yT);

        //Mellek beosztasok kirakasa
        g2.setColor(secondaryStickColor);
        for(int i = 1; i < secondaryStickNumber; i++){
          position+=secondaryDistance;
          g2.drawLine(-(int)Math.rint(secondaryStickSize/2), (int)Math.rint(position), (int)Math.rint(secondaryStickSize/2), (int)Math.rint(position));
        }

        mainStick = mainStick.add(steps);
      }//while

//      FontRenderContext frc = g2.getFontRenderContext();
//      String szoveg = "A ello h g hgfhd h dg";
//      TextLayout megjelenito = new TextLayout(szoveg, font, frc);
//      AffineTransform at = AffineTransform.getRotateInstance(-Math.PI/2, 0, 0);
//      at.concatenate(AffineTransform.getTranslateInstance((float)-lineStop, 0));
//      Shape korvonal = megjelenito.getOutline(at);
//      g2.setColor(Color.red);
//      g2.draw(korvonal);

      //title kiirasa
      //szoveg kiirasa
      g2.setColor(titleColor);
      frc = g2.getFontRenderContext();
      textLayout = new TextLayout(titleText, titleFont, frc);
      if(titlePosition == TITLE_DOWN_RIGHT){
        xT = (float)(mainStickSize / 2 + 1);
        yT =  (float)(lineStop - 3);
      }else if(titlePosition == TITLE_DOWN_LEFT){
        xT = (float)(-mainStickSize / 2 - textLayout.getAdvance());
        yT =  (float)(lineStop - 3);
      }else if(titlePosition == TITLE_UP_RIGHT){
        xT = (float)(mainStickSize / 2 + 1);
        yT =  (float)(lineStart + textLayout.getAscent());
      }else{
        xT = (float)(-mainStickSize / 2 - 1 - textLayout.getAdvance());
        yT =  (float)(lineStart + textLayout.getAscent());
      }
      textLayout.draw(g2, xT, yT);

    }
  }
}