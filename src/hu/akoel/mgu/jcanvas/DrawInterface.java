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

public interface DrawInterface {
  public void draw(Graphics2D g2, Measurement measurement, Size totalSize, Size viewableSize);
}