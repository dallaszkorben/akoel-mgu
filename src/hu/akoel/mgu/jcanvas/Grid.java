package hu.akoel.mgu.jcanvas;

import java.awt.*;

/**
 * <p>Title: pipeline</p>
 * <p>Description: Pipeline network designer</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: akoel@soft</p>
 * @author akoel
 * @version 1.0
 */

public class Grid{

  public static final int TYPE_NONE = 0;
  public static final int TYPE_SOLID = 1;
  public static final int TYPE_CROSS = 2;
  public static final int TYPE_DOT = 3;
  public static final int TYPE_BROKEN = 4;

  private int type;
  private Color color;
  private double deltaX, deltaY;
  private double crossLength;
  private double brokenLength;

  private Measurement measurement;
  private Size size;

  public Grid(Measurement measurement, Size size) {
    constructor(measurement, size, TYPE_NONE, new Color(154,180,180), 2.0, 2.0);
  }
  public Grid(Measurement measurement, Size size, int type) {
    constructor(measurement, size, type, new Color(154,180,180), 2.0, 2.0);
  }
  public Grid(Measurement measurement, Size size, int type, Color color) {
    constructor(measurement, size, type, color, 2.0, 2.0);
  }
  public Grid(Measurement measurement, Size size, int type, Color color, double deltaX, double deltaY) {
    constructor(measurement, size, type, color, deltaX, deltaY);
  }
  public Grid(Measurement measurement, Size size, int type, double deltaX, double deltaY) {
    constructor(measurement, size, type, new Color(154,180,180), deltaX, deltaY);
  }

  public void constructor(Measurement m, Size s, int t, Color c, double dX, double dY){
    this.measurement = m;
    this.size = s;
    this.type = t;
    this.color = c;
    this.deltaX = dX;
    this.deltaY = dY;

    double crossLength = 0.1;
    double brokenLength = 0.01;

  }

  public void setType(int t){
    type = t;
  }
  public void setColor(Color c){
    color = c;
  }
  public void setDelta(double dX, double dY) {
    deltaX = dX;
    deltaY = dY;
  }
  public void setBrokenLength(double length){
    brokenLength = length;
  }
  public void setCrossLength(double length){
    crossLength = length;
  }
  public double getDeltaX(){
    return deltaX;
  }
  public double getDeltaY(){
    return deltaY;
  }


  public void paint(Graphics2D g2){
    int n = 1, m = 1;
    double xStart, yStart;
    double yPosition, xPosition;
    double crossXLength, crossYLength;

    double startX = measurement.getDistanceToPixel(size.getStartX());
    double stopX = measurement.getDistanceToPixel(size.getStopX());
    double deltaY = measurement.getDistanceToPixel(this.getDeltaY());
    double startY = measurement.getDistanceToPixel(size.getStartY());
    double stopY = measurement.getDistanceToPixel(size.getStopY());
    double deltaX = measurement.getDistanceToPixel(this.getDeltaX());

    if(type != TYPE_NONE){
      g2.setColor(color);

      xStart = deltaX * Math.rint(startX / deltaX);
      yStart = deltaY * Math.rint(startY / deltaY);

      //Ha folyamatos vonal
      if(type == TYPE_SOLID){
        n = 1;
        xPosition = xStart;
        while (xPosition <= stopX){
          g2.drawLine((int)Math.rint(xPosition), (int)Math.rint(startY), (int)Math.rint(xPosition), (int)Math.rint(stopY));
//          xPosition+=deltaX;
//          xPosition = (double)Math.round(xStart + n*deltaX); //!!!!!!!!
          xPosition = xStart + n*deltaX; //!!!!!!!!
          n++;
        }

        m = 1;
        yPosition = yStart;
        while (yPosition <= stopY){
          g2.drawLine((int)Math.rint(startX), (int)Math.rint(yPosition), (int)Math.rint(stopX), (int)Math.rint(yPosition));
//          yPosition+=deltaY;
//          yPosition = (double)Math.round(yStart + m*deltaY); //!!!!!!!!
          yPosition = yStart + m*deltaY; //!!!!!!!!
          m++;
        }

      //Ha szaggatott vagy pont
      }else {
        if(type == TYPE_CROSS){
          crossXLength = crossLength * deltaX;
          crossYLength = crossLength * deltaY;
        }else{
          crossXLength = 0;
          crossYLength = 0;
        }
        n = 1;
        xPosition = xStart;
        while (xPosition <= stopX ){
          m = 1;
          yPosition = yStart;
          while (yPosition <= stopY ){
            g2.drawLine((int)Math.rint(xPosition - crossXLength), (int)Math.rint(yPosition), (int)Math.rint((xPosition + crossXLength)), (int)Math.rint(yPosition));
            g2.drawLine((int)Math.rint(xPosition), (int)Math.rint(yPosition - crossYLength), (int)Math.rint(xPosition), (int)Math.rint(yPosition + crossYLength));
//            yPosition+=deltaY;
            yPosition = (double)Math.rint(yStart + m*deltaY); //!!!!!!!!
            m++;
          }//while
//          xPosition += deltaX;
          xPosition = (double)Math.rint(xStart + n*deltaX); //!!!!!!!!
          n++;
        }//while
      }//if
    }//if
  }
}