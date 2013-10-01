package hu.akoel.mgu.jcanvas;

/**
 * <p>Title: pipeline</p>
 * <p>Description: Pipeline network designer</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: akoel@soft</p>
 * @author akoel
 * @version 1.0
 */

public class Measurement {
  private final int BASE = 1;            //kepernyon egy egyseg (1m)
  private final double PIXEL = 3680;     //ennyi pixel jelent a kepernyoegyseg
  private final double SCALE = 500;     //a kepernyoegyseg ennyi a terepen kezdetben
  private final double IN = 1.2;         //meretarany valtozasa automatikus zoomIn eseten
  private final double OUT = 1.2;        //meretarany valtozasa automatikus zoomOut eseten

  private double pixel;
  private double scale, originalScale;
  private double in;
  private double out;

  private boolean zoomEnabled;

  private ScaleChangeListener scaleChangeListener;

  public Measurement() {
    constructor(SCALE, PIXEL, IN, OUT);
  }
  public Measurement(double g) {
    constructor(g, PIXEL, IN, OUT);
  }
  public Measurement(double g, double p) {
    constructor(g, p, IN, OUT);
  }
  public Measurement(double g, double p, double i, double o) {
    constructor(g, p, i, o);
  }
  private void constructor(double g, double p, double i, double o){
    scale = g;
    originalScale = g;
    pixel = p;
    in = i;
    out = o;
    zoomEnabled = true;
  }

  public void setZoomEnabled(boolean e){
    zoomEnabled = e;
  }

  public double getInRate(){
    return this.in;
  }

  public double getOutRate(){
    return this.out;
  }

  public void setInRate(double in){
    this.in = in;
  }

  public void setOutRate(double out){
    this.out = out;
  }


  /**
   * Megadhato, hogy a kepernyon 1 BASE egyseg (1m) az hany pixelt jelent
   * Alapesetben 368 pixelt szamolva 10 cm-t merhetunk
   */
  public void setPixel(double p){
    pixel = p;
  }

  public double getPixel(){
    return pixel;
  }

  /**
   * Az aktualis es eredeti meretaranyt allitja be
   */
  public void setScale(double g){
    this.scale = g;
    if(scaleChangeListener != null)
      scaleChangeListener.changed(g);
  }

  public void setScaleIn(double p){
    if(zoomEnabled)
      setScale(getScale()/p);
  }
  public void setScaleOut(double p){
    if(zoomEnabled)
      setScale(getScale()*p);
  }


  /**
   * A meretaranyt adja vissza
   */
  public double getScale(){
    return scale;
  }

  public double getMultiplierToDistance(){
      return getScale() / getPixel();
  }

  public double getMultiplierToPixel(){
    return getPixel() / getScale();
  }

  double getDistanceToPixel(double distance){
    return distance*getMultiplierToPixel();
  }

  double getPixelToDistance(double pixel){
    return pixel*getMultiplierToDistance();
  }


  /**
   * A meretarany megvaltozasat figyelo osztaly hozzaadasa
   */
  public void addScaleChangeListener(ScaleChangeListener sl){
    scaleChangeListener = sl;
    scaleChangeListener.changed(getScale());
  }
}