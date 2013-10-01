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

public class Origo {
  private boolean enabled;
  private Color color;
  private double size;   //m-ben
  private Measurement measurement;

  public Origo(Measurement measurement){
    constructor(measurement, false, new Color(118, 255, 255), 10);
  }
  public Origo(Measurement measurement, boolean enabled){
    constructor(measurement, enabled, new Color(118, 255, 255), 10);
  }
  public Origo(Measurement measurement, boolean enabled, double size){
    constructor(measurement, enabled, new Color(118, 255, 255), size);
  }
  public Origo(Measurement measurement, boolean enabled, Color color){
    constructor(measurement, enabled, color, 10);
  }
  public Origo(Measurement measurement, boolean enabled, Color color, double size){
    constructor(measurement, enabled, color, size);
  }
  public void constructor(Measurement m, boolean e, Color c, double s){
    enabled = e;
    color = c;
    size = s;
    measurement =  m;
  }

  public void setColor(Color c){
    color = c;
  }
  public void setEnabled(boolean e){
    enabled = e;
  }
  public void setSize(double s){
    size = s;
  }
  public void paint(Graphics2D g2){
    if(enabled){
      int dx = (int)Math.rint((measurement.getDistanceToPixel(size)) / 2);
      int dy = (int)Math.rint((measurement.getDistanceToPixel(size)) / 2);
      g2.setColor(color);
      g2.drawLine( -dx, 0, dx, 0);
      g2.drawLine( -dx, -1, dx, -1);
      g2.drawLine( -dx, 1, dx, 1);

      g2.drawLine(0, -dy, 0, dy);
      g2.drawLine(-1, -dy, -1, dy);
      g2.drawLine(1, -dy, 1, dy);
    }
  }
}