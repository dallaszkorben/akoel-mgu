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

public abstract class AxisPartAdapter {
  public final static int NONE = 0;
  public final static int HORIZONTAL = 2;
  public final static int VERTICAL = 3;

  public final static int TITLE_UP_RIGHT = 0;
  public final static int TITLE_UP_LEFT = 1;
  public final static int TITLE_DOWN_RIGHT = 2;
  public final static int TITLE_DOWN_LEFT = 3;

  public final static int NUMBER_UP = 0;
  public final static int NUMBER_DOWN = 1;

  private double[] units;            //0 es 1 kozotti lista. Ezek kozulik a 10 valamelyik hatvagyszorosai
                                     //kozul a legkisebbet valasztja
                                     //mely belefer a minimalDistance altal maghatarozott ertekhez.
  private double minimalDistance;    //Megmondom m-ben, hogy a kepernyon minimum mekkora tavolsag kell
                                     //ket beosztas kozott. Altalaban 1 cm


  protected Measurement measurement;
  protected Size viewable;
  protected int orientation;
  protected Color lineColor;
  protected Color numberColor;
  protected Font numberFont;
  protected double numberFontPortion;
  protected int numberPosition;
  protected Color mainStickColor;
  protected int mainStickSize;
  protected Color secondaryStickColor;
  protected int secondaryStickSize;
  protected int secondaryStickNumber;
  protected int width;
  protected Color titleColor;
  protected String titleText;
  protected Font titleFont;
  protected double titleFontPortion;
  protected int titlePosition;


  public AxisPartAdapter(Measurement measurement, Size viewable, int orientation) {
    this.orientation = orientation;
    this.measurement = measurement;
    this.viewable = viewable;

    numberColor = Color.yellow;
    numberFont = new Font("Default", Font.PLAIN, 14);
    numberPosition = NUMBER_UP;
    numberFontPortion = 0.05;

    titleColor = Color.yellow.darker();
    titleText = "";
    titlePosition = TITLE_DOWN_RIGHT;
    titleFont = new Font("Default", Font.BOLD, 16);
    titleFontPortion = 0.08;


    mainStickColor = Color.yellow;
    mainStickSize = 8;

    lineColor = Color.yellow;

    secondaryStickColor = Color.yellow;
    secondaryStickSize = 4;
    secondaryStickNumber = 5;

    units = new double[]{0.25, 0.5, 1};
    minimalDistance = 0.01;

    width = 35;



  }

  public void setTitleText(String t){
    titleText = t;
  }
  public void setTitleColor(Color c){
    titleColor = c;
  }
  public void setTitleFont(Font f){
    titleFont = f;
  }
  public void setTitlePosition(int p){
    titlePosition = p;
  }

  public void setOrientation(int o){
    orientation = o;
  }

  public void setNumberFont(Font f){
    numberFont = f;
  }
  public void setNumberColor(Color c){
    numberColor = c;
  }
  public void setNumberPosition(int p){
    numberPosition = p;
  }

  public void setLineColor(Color c){
    lineColor = c;
  }

  public void setMainStickColor(Color c){
    mainStickColor = c;
  }

  public void setMainStickSize(int s){
    mainStickSize = s;
  }

  public void setSecondaryStickNumber(int n){
    secondaryStickNumber = n;
  }

  public void setSecondaryStickColor(Color c){
    secondaryStickColor = c;
  }

  public void setSecondaryStickSize(int s){
    secondaryStickSize = s;
  }

  public void setWidth(int w){
    width = w;
  }


  public int getOrientation(){
    return orientation;
  }

  public int getWidth(){
    return width;
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


  /**
   * Megmondja, hogy az adott meretaranyban mekkora tavolsagokat kell jelolni
   */
  protected BigDecimal getSteps(){

    //A vilagban minimum ekkora lepeseket kell tennem, hogy a minimalis tavolsagot
    //tudjam tartani a kepernyon
    BigDecimal minMarkerDistance = new BigDecimal (measurement.getScale() * minimalDistance);
    BigDecimal actualMarkerDistance = new BigDecimal (measurement.getScale() * minimalDistance);

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


  public abstract void paint(Graphics2D g2);


}