package hu.akoel.mgu.jcanvas;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Side implements Cloneable{
  private double start, stop;

  public Side(double start, double stop) {
    this.start = start;
    this.stop = stop;
  }

  public double getStart(){
    return start;
  }

  public double getStop(){
    return stop;
  }

  public void setStart(double s){
    start = s;
  }

  public void setStop(double s){
    stop = s;
  }

  public double getSize(){
    return (stop - start);
  }

  public void set(double start, double stop){
    this.start = start;
    this.stop = stop;
  }

  public String toString(){
    return String.valueOf(getStart()) + " - " + String.valueOf(getStop());
  }


  public Object clone() {
    try{
      return super.clone();
    }catch (CloneNotSupportedException e){return null;}
  }

}