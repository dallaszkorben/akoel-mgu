package hu.akoel.mgu.jcanvas;

/**
 * <p>Title: pipeline</p>
 * <p>Description: Pipeline network designer</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: akoel@soft</p>
 * @author akoel
 * @version 1.0
 */

public class Size implements Cloneable{
  private Side xDirection, yDirection;

  public Size(){
    xDirection = new Side(-100, 100);
    yDirection = new Side(-100, 100);
  }

  public Size(double startX, double startY, double stopX, double stopY ) {
    xDirection = new Side(startX, stopX);
    yDirection = new Side(startY, stopY);
  }


  public double getStartX(){
    return xDirection.getStart();
  }
  public double getStartY(){
    return yDirection.getStart();
  }
  public double getStopX(){
    return xDirection.getStop();
  }
  public double getStopY(){
    return yDirection.getStop();
  }
  public double getWidth(){
    return xDirection.getSize();
  }
  public double getHeight(){
    return yDirection.getSize();
  }
  public Side getXDirection(){
    return xDirection;
  }
  public Side getYDirection(){
    return yDirection;
  }


  public void setStartX(double startX){
    xDirection.setStart(startX);
  }
  public void setStartY(double startY){
    yDirection.setStart(startY);
  }
  public void setStopX(double stopX){
    xDirection.setStop(stopX);
  }
  public void setStopY(double stopY){
    yDirection.setStop(stopY);
  }

  public void setSize(double startX, double startY, double stopX, double stopY){
    xDirection.set(startX, stopX);
    yDirection.set(startY, stopY);
  }

  public void moveRight(double step, Side limit){
//System.err.println(this + " --- " + limit);
    double maxStep = limit.getStop() - this.getStopX();

    //Nem lephetek akkorat amekkorat szeretnek
    if(maxStep < step){
      this.setStartX(this.getStartX() + maxStep);
      this.setStopX(this.getStopX() + maxStep);
    }else {
      this.setStartX(this.getStartX() + step);
      this.setStopX(this.getStopX() + step);
    }
  }

  public void moveLeft(double step, Side limit){

    double maxStep = this.getStartX() - limit.getStart();

    //Nem lephetek akkorat amekkorat szeretnek
    if(maxStep < step){
      this.setStartX(this.getStartX() - maxStep);
      this.setStopX(this.getStopX() - maxStep);
    }else {
      this.setStartX(this.getStartX() - step);
      this.setStopX(this.getStopX() - step);
    }
  }

  public void moveDown(double step, Side limit){
    double maxStep = limit.getStop() - this.getStopY();

    //Nem lephetek akkorat amekkorat szeretnek
    if(maxStep < step){
      this.setStartY(this.getStartY() + maxStep);
      this.setStopY(this.getStopY() + maxStep);
    }else {
      this.setStartY(this.getStartY() + step);
      this.setStopY(this.getStopY() + step);
    }
  }

  public void moveUp(double step, Side limit){
    double maxStep = this.getStartY() - limit.getStart();

    //Nem lephetek akkorat amekkorat szeretnek
    if(maxStep < step){
      this.setStartY(this.getStartY() - maxStep);
      this.setStopY(this.getStopY() - maxStep);
    }else {
      this.setStartY(this.getStartY() - step);
      this.setStopY(this.getStopY() - step);
    }
  }

  public String toString(){
    return String.valueOf(getStartX()) + " - " + String.valueOf(getStartY()) + " / " + String.valueOf(getStopX()) + " - " + String.valueOf(getStopY());
  }

  public Object clone() {
    try{

      Size cs = (Size)super.clone();
      cs.xDirection = (Side)xDirection.clone();
      cs.yDirection = (Side)yDirection.clone();

      return cs;
    }catch (CloneNotSupportedException e){return null;}
  }

}