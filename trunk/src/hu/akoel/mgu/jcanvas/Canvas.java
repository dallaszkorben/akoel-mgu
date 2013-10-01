package hu.akoel.mgu.jcanvas;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;

/**
 * <p>Title: pipeline</p>
 * <p>Description: Pipeline network designer</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: akoel@soft</p>
 * @author akoel
 * @version 1.0
 */


class Canvas extends JPanel{
  private Size worldTotal, worldViewable, worldIntend;
  private Origo origo;
  private Grid grid;
  private Measurement measurement;
  private InsideAxisPart horizontalInsideAxis;
  private InsideAxisPart verticalInsideAxis;

  private BufferedImage offImage;

  // Az ablakban latszik a teljes definialt vilag. Ez egyben azt jelenti, hogy
  // a meretarany az ablak meretenek valtoztatasaval valtozik, es az ablak
  // oldalainak aranya nem kotott
  public static final int TYPE_SHOW_WHOLE_WORLD = 0;
  // Az ablak merete szabadon valtozhat, a meretarany nem valtozik
  public static final int TYPE_FREE_WINDOW_PORTION = 1;
  // Az ablak oldalainak aranya a definialt vilag oldalainak aranyaval egyezik meg
  public static final int TYPE_SETTLED_WINDOW_PORTION = 2;
  // A rajzolo metodusok (koordinata rendszer alatt - folott) osztalyai
  private DrawInterface drawUnderInterface, drawAboveInterface, drawTemporaryInterface;


  private Container parent;
  private int canvasType = Canvas.TYPE_FREE_WINDOW_PORTION;
  private Color background;

  private WindowSizeChangeListener windowSizeChangeListener;


  public Canvas(Container parent, int type, Size total, Color b) {
    this.parent = parent;
    canvasType = type;
    background = b;

    //Alaptulajdonsagok letrehozasa
    worldTotal = total;
    worldViewable = (Size)total.clone();
    worldIntend = (Size)total.clone();

    measurement = new Measurement();
    origo = new Origo(measurement);
    grid = new Grid(measurement, worldViewable);
    horizontalInsideAxis = new InsideAxisPart(measurement, worldViewable, InsideAxisPart.NONE);
    verticalInsideAxis = new InsideAxisPart(measurement, worldViewable, InsideAxisPart.NONE);


    // Eger kormanyaval letrehozott zoom figyelese
    addMouseWheelListener(new WheelZoomListener());

    // Az ablak meretvaltozasanak figyelese
    addComponentListener(new WindowResizeListener());
  }


  public void setZoomEnabled(boolean e){
    measurement.setZoomEnabled(e);
  }

  /**
   * belso koordinatarendszer tulajdonsagai
   */
  public void setHorizontalInsideAxisTitleText(String t){
    horizontalInsideAxis.setTitleText(t);
  }
  public void setHorizontalInsideAxisTitleColor(Color c){
    horizontalInsideAxis.setTitleColor(c);
  }
  public void setHorizontalInsideAxisTitleFont(Font f){
    horizontalInsideAxis.setTitleFont(f);
  }
  public void setHorizontalInsideAxisTitlePosition(int p){
    horizontalInsideAxis.setTitlePosition(p);
  }
  public void setHorizontalInsideAxisEnabled(boolean e){
    horizontalInsideAxis.setOrientation(e ? InsideAxisPart.HORIZONTAL : InsideAxisPart.NONE);
  }
  public void setHorizontalInsideAxisLineColor(Color c){
    horizontalInsideAxis.setLineColor(c);
  }
  public void setHorizontalInsideAxisNumberColor(Color c){
    horizontalInsideAxis.setNumberColor(c);
  }
  public void setHorizontalInsideAxisNumberFont(Font f){
    horizontalInsideAxis.setNumberFont(f);
  }
  public void setHorizontalInsideAxisNumberPosition(int p){
    horizontalInsideAxis.setNumberPosition(p);
  }
  public void setHorizontalInsideAxisMainStickColor(Color c){
    horizontalInsideAxis.setMainStickColor(c);
  }
  public void setHorizontalInsideAxisMainStickSize(int s){
    horizontalInsideAxis.setMainStickSize(s);
  }
  public void setHorizontalInsideAxisSecondaryStickSize(int s){
    horizontalInsideAxis.setSecondaryStickSize(s);
  }
  public void setHorizontalInsideAxisSecondaryStickNumber(int n){
    horizontalInsideAxis.setSecondaryStickNumber(n);
  }
  public void setHorizontalInsideAxisWidth(int w){
    horizontalInsideAxis.setWidth(w);
  }


  public void setVerticalInsideAxisTitleText(String t){
    verticalInsideAxis.setTitleText(t);
  }
  public void setVerticalInsideAxisTitleColor(Color c){
    verticalInsideAxis.setTitleColor(c);
  }
  public void setVerticalInsideAxisTitleFont(Font f){
    verticalInsideAxis.setTitleFont(f);
  }
  public void setVerticalInsideAxisTitlePosition(int p){
    verticalInsideAxis.setTitlePosition(p);
  }

  public void setVerticalInsideAxisEnabled(boolean e){
    verticalInsideAxis.setOrientation(e ? InsideAxisPart.VERTICAL : InsideAxisPart.NONE);
  }
  public void setVerticalInsideAxisLineColor(Color c){
    verticalInsideAxis.setLineColor(c);
  }
  public void setVerticalInsideAxisNumberColor(Color c){
    verticalInsideAxis.setNumberColor(c);
  }
  public void setVerticalInsideAxisNumberFont(Font f){
    verticalInsideAxis.setNumberFont(f);
  }
  public void setVerticalInsideAxisNumberPosition(int p){
    verticalInsideAxis.setNumberPosition(p);
  }
  public void setVerticalInsideAxisMainStickColor(Color c){
    verticalInsideAxis.setMainStickColor(c);
  }
  public void setVerticalInsideAxisMainStickSize(int s){
    verticalInsideAxis.setMainStickSize(s);
  }
  public void setVerticalInsideAxisSecondaryStickSize(int s){
    verticalInsideAxis.setSecondaryStickSize(s);
  }
  public void setVerticalInsideAxisSecondaryStickNumber(int n){
    verticalInsideAxis.setSecondaryStickNumber(n);
  }
  public void setVerticalInsideAxisWidth(int w){
    verticalInsideAxis.setWidth(w);
  }


  /**
   * Grid tulajdonsagaira vonatkozo tulajdonsagok
   */
  public void setGridType(int t){
    grid.setType(t);
  }
  public void setGridColor(Color c){
    grid.setColor(c);
  }
  public void setGridSize(double xDelta, double yDelta){
    grid.setDelta(xDelta, yDelta);
  }
  public void setGridBrokenLength(double l){
    grid.setBrokenLength(l);
  }
  public void setGridCrossLength(double l){
    grid.setCrossLength(l);
  }

  /**
   * worldTotal-ra vonatkozo tulajdonsagok
   */
  public void setWorldTotalSize(double startX, double startY, double stopX, double stopY){
    worldTotal.setSize(startX, startY, stopX, stopY);
  }

  /**
   * worldViewable-re vonatkozo tulajdonsagok
   */
  public void setWorldViewableSize(double startX, double startY, double stopX, double stopY){
    worldViewable.setSize(startX, startY, stopX, stopY);
  }

  /**
   * measurement-re vonatkozo tulajdonsagok
   */
  public void setMeasurementPixel(double pixel){
    measurement.setPixel(pixel);
  }
  public void setMeasurementScale(double scale){
    measurement.setScale(scale);
  }
  public void setMeasurementInRate(double in){
    measurement.setInRate(in);
  }
  public void setMeasurementOutRate(double out){
    measurement.setOutRate(out);
  }
  public void setMeasurementScaleIn(double in){
    measurement.setScaleIn(in);
  }
  public void setMeasurementScaleOut(double out){
    measurement.setScaleOut(out);
  }
  public void setMeasurementZoomEnabled(boolean e){
    measurement.setZoomEnabled(e);
  }

  /**
   * Origo-ra vonatkozo tulajdonsagok
   */
  public void setOrigoColor(Color c){
    origo.setColor(c);
  }
  public void setOrigoEnabled(boolean e){
    origo.setEnabled(e);
  }
  public void setOrigoSize(double s){
    origo.setSize(s);
  }



  /**
   * A segedelemek rajzolasa elott a Canvasra kerulendo elemek elhelyezese
   */
  public void addDrawUnderInterface(DrawInterface di){
    drawUnderInterface = di;
  }

  /**
   * A segedelemek rajzolasa utan a Canvasra kerulendo elemek elhelyezese
   */
  public void addDrawAboveInterface(DrawInterface di){
    drawAboveInterface = di;
  }

  /**
   * Atmenetileg a Canvasra kerulendo elemek elhelyezese
   */
  public void addDrawTemporaryInterface(DrawInterface di){
    drawTemporaryInterface = di;
  }


  /**
   * A meretarany megvaltozasat figyelo osztaly hozzaadasa
   */
  public void addScaleChangeListener(ScaleChangeListener sl){
    measurement.addScaleChangeListener(sl);
  }

  public void addWindowSizeChangeListener(WindowSizeChangeListener cl){
    windowSizeChangeListener = cl;
  }

  Size getWorldTotalSize(){
    return worldTotal;
  }

  Size getWorldVieableSize(){
    return worldViewable;
  }


  /**
   * Ide akkor kerul a vezerles, ha megvaltozott az ablak merete.
   * Ekkor, ha definialtam egy figyelo osztalyt akkor atadodik oda a vezerles
   */
  public void setSize(Dimension size){
    super.setSize(size);
    if(windowSizeChangeListener != null)
      windowSizeChangeListener.changed(size);
  }







  /**
   * Ha kerdezi az elrendezeskezelo, kiszamolja szamara az ablak meretet.
   * Tulajdonkeppen, azt a maximalis meretet adja vissza ami eppen belefer
   * a szulo ablakba, de ha az ablak tipusa olyan, akkor valamelyik oldalt
   * megroviditi, hogy a vilag oldal-aranyainak feleljen meg.
   */
  public Dimension getPreferredSize() {
    double mH = 0, wH;
    double pixelWidth, pixelHeight;
    double worldWidth, worldHeight;

    // Felveszi a szulo ablak meretet
    pixelWidth = (double)parent.getWidth();
    pixelHeight = (double)parent.getHeight();

    /**
     * Csak akkor szamol tovabb, ha a szulo-ablak mar megjelent
     */
    if(pixelWidth != 0  && pixelHeight != 0){

      /**
       * Ha a lathato teruelt oldalainak aranyanak meg kell egyeznie az eredeti
       * terulet oldalainak aranyaval
       * Beallitja a rajzolhato feluletet az oldalaranyoknak megfeleloen
       * a szulo-ablak aktualis merete alapjan.
       */
      if(canvasType != Canvas.TYPE_FREE_WINDOW_PORTION){

        /**
         * A szulo ablak oldalainak aranya(model meret)
         */
        mH = pixelWidth/pixelHeight;

        /**
         * Az abrazolando vilag oldalainak aranya (world meret)
         */
        wH = worldTotal.getWidth()/worldTotal.getHeight();

        /**
         * Ha a vilag tomzsibb mint a modell
         */
        if(wH > mH){

          /**
           * Akkor a model magassagat kell csokkenteni
           */
          pixelHeight = pixelWidth / wH;

          /**
           * Ha a vilag elnyujtottabb (magassagban) mint a modell
           */
        }else{

          /**
           * Akkor a modell szelesseget kell csokkenteni
           */
          pixelWidth = wH * pixelHeight;
        }
      }
      /**
       * Ekkor a pixelWidth-pixelHeight egy olyan ablakmeretet hataroz meg, mely
       * belefer a szulo ablakba es az aranyai megegyeznek az abrazolando vilag
       * aranyaival
      */
    }
    return new Dimension((int)Math.rint(pixelWidth), (int)Math.rint(pixelHeight));
  }


  /**
   * Amikor meghivodik a repaint()-update()-paint() harmas akkor, hogy
   * ne torlodjon a canvas, at kell irni az update metodust
   */
  public void update(Graphics g) {
    paint(g);
  }

  /**
   * Az canvas ervenytelenitesekor torolni kell a hatter kepet is
   */
  public void invalidate() {
    super.invalidate();
    offImage = null;
  }

  /**
   * Kep frissites
   */
  public void paint(Graphics g) {
    int width = this.getWidth();
    int height = this.getHeight();
    Graphics2D offg2;
    Graphics2D g2 = (Graphics2D) g;

    //Ha meg nem is jelent meg a canvas a kepernyon
    if (width <= 0 || height <= 0)
      return;

    //Ha nem volt meg ervenyes kep, vagy megvaltozott a canvas merete, akkor
    //-uj canvas-t keszit es ujra rajzolja a kepet
    if (offImage == null || offImage.getWidth() != width || offImage.getHeight() != height) {

      //Uj Canvas keszitese
      offImage = (BufferedImage) createImage(width, height);

      //Az uj canvas grafikai objektumanak elkerese
      offg2 = (Graphics2D) offImage.getGraphics();

      //Beszinezi a Canvas-t
      offg2.setColor(background);
      offg2.fillRect(0, 0, width, height);

      //Origo beallitasa
      offg2.translate(Math.rint(measurement.getDistanceToPixel(-worldViewable.getStartX())), Math.rint(measurement.getDistanceToPixel(-worldViewable.getStartY())));

      //Segedelemek elotti rajzolas (interface)
      if (drawUnderInterface != null)
        drawUnderInterface.draw (offg2, measurement, worldTotal, worldViewable);

      //Racsozat rajzolas
      grid.paint(offg2);

      //Koordinatatengely rajzolas
      horizontalInsideAxis.paint(offg2);
      verticalInsideAxis.paint(offg2);

      //Origo rajzolas
      origo.paint(offg2);//, measurement.getMultiplierToPixel());

      //Segedelemek utani rajzolas (interface)
      if (drawAboveInterface != null)
        drawAboveInterface.draw(offg2, measurement, worldTotal, worldViewable);

    }

    if (offImage != null) {

      //Kirajzolja  a bufferelt kepet
      g2.drawImage(offImage, 0, 0, this);

      //kirajzol egy atmeneti kepet
      g2.translate(Math.rint(measurement.getDistanceToPixel(-worldViewable.getStartX())), Math.rint(measurement.getDistanceToPixel(-worldViewable.getStartY())));

      //Meghivja azt a metodust ahol atmeneti elemeket rajzolun ki
      if(drawTemporaryInterface != null)
        drawTemporaryInterface.draw(g2, measurement, worldTotal, worldViewable);
    }
  }//paint()






  /**
   * A lathato vilag jobbra mozgatasa
   */
  public void moveRight(double step){
    worldViewable.moveRight(step, worldTotal.getXDirection());
    worldIntend.moveRight(step, worldTotal.getXDirection());
    this.invalidate();
    this.repaint();
  }


  /**
   * A lathato vilag balra mozgatasa
   */
  public void moveLeft(double step){
    worldViewable.moveLeft(step, worldTotal.getXDirection());
    worldIntend.moveLeft(step, worldTotal.getXDirection());
    this.invalidate();
    this.repaint();
  }

  /**
   * A lathato vilag felfele mozgatasa
   */
  public void moveUp(double step){
    worldViewable.moveUp(step, worldTotal.getYDirection());
    worldIntend.moveUp(step, worldTotal.getYDirection());
    this.invalidate();
    this.repaint();
  }

  /**
   * A lathato vilag lefele mozgatasa
   */
  public void moveDown(double step){
    worldViewable.moveDown(step, worldTotal.getYDirection());
    worldIntend.moveDown(step, worldTotal.getYDirection());
    this.invalidate();
    this.repaint();
  }





  public void zoomIn(double xCenter, double yCenter, int xPoint, int yPoint, double rate){
    measurement.setScaleIn(rate);
    zoomArrangement(xCenter, yCenter, xPoint, yPoint);
  }

  public void zoomOut(double xCenter, double yCenter, int xPoint, int yPoint, double rate){
      measurement.setScaleOut(rate);
      zoomArrangement(xCenter, yCenter, xPoint, yPoint);
  }

  public void zoom(double xCenter, double yCenter, int xDelta, int yDelta, double value){
      measurement.setScale(value);
      zoomArrangement(xCenter, yCenter, xDelta, yDelta);
  }

  private void zoomArrangement(double xCenter, double yCenter, int xDelta, int yDelta){

    //Ablak meretenek beallitasa es igazitasa (csak a meretaranytol fugg)
    setSize(getCorrectionedSize(getPreferredSize()));

    // A lathato vilag meretenek kiszamolasa
    setViewableWorld(xCenter, yCenter, xDelta, yDelta);

    //A megmutando vilag meretenek kiszamolasa
    setIntendWorld(xCenter, yCenter, xDelta, yDelta);

    // Mivel az ablak merete nem valtozott, ezert ervenytelenne kell tenni
    // a rajzolo feluletet, hogy a paint() tenyleg ujra letrehozza
    invalidate();

    //A lathato vilag ujra rajzolasa
    repaint();
  }




  /**
   * Az eger kormanya altal letrehozott zoom figyelo osztaly
   */
  class WheelZoomListener implements MouseWheelListener{
    public void mouseWheelMoved(MouseWheelEvent e){
      double xCenter, yCenter;
      xCenter = (worldViewable.getStartX() + measurement.getPixelToDistance(e.getX()));
      yCenter = (worldViewable.getStartY() + measurement.getPixelToDistance(e.getY()));

      //Felfele nyomas -> ZoomIn
      if (e.getWheelRotation() < 0)
        zoomIn(xCenter, yCenter, e.getX(), e.getY(), measurement.getInRate());
        //Lefele nyomas -> ZoomOut
      else
        zoomOut(xCenter, yCenter, e.getX(), e.getY(), measurement.getInRate());

    }//mouseWheelMoved(MouseWheelEvent e)

  }//class WheelZoomListener








  /**
   * Az ablak atmeretezeset figyelo osztaly
   * Amikor ide kerul a vezerles, a getPreferredSize() mar meghivodott, igy
   * a getWidth() es a getHeight() mar a fizikailag es meretaranyilag rendelkezesre
   * allo meretet szolgaltatja vissza
   */
  class WindowResizeListener extends ComponentAdapter{
    public void componentResized(ComponentEvent e){
      double centerX, centerY, widthScale, heightScale;

      // Csak akkor veszek tudomast az ablakmeret valtozasrol, ha mar egyszer
      // kirajzolta
      if(getWidth() != 0 && getHeight() != 0){

        // A megmutando vilag bal felso sarka lesz a nagyitas kozpontja
        centerX = worldIntend.getStartX();
        centerY = worldIntend.getStartY();

        // Ha mutassa az egesz vilagot tipus eseten ablakmeret valtozasakor
        // a meretarany valtozik
        if(canvasType == Canvas.TYPE_SHOW_WHOLE_WORLD){

          //A megmutatni kivant vilag merete es az ablak merete alapjan
          //a lehetseges meretaranyok megallapitasa
          //Nem lesz egyforma a szelessegben es magassagban!!!
          widthScale = measurement.getPixel() * worldIntend.getWidth() / getWidth();
          heightScale = measurement.getPixel() * worldIntend.getHeight() / getHeight();


          //A kisebb meretarany (nagyobb meret) lesz a meghatarozo
          if (widthScale < heightScale){
            measurement.setScale(widthScale);
          }else {
            measurement.setScale(heightScale);
          }

        }//if(canvasType == CoordCanvas.TYPE_SHOW_WHOLE_WORLD)


        // Ablak meretenek beallitasa es igazitasa
        // Igazabol itt csak az igazitasra volna szukseg, hiszen az ablak
        // atmeretezesekor kerulunk ide, akkor pedig automatikusan meghivodik
        // a getPreferredSize() metodus
        setSize(getCorrectionedSize(new Dimension(getWidth(), getHeight())));

        // A lathato vilag meretenek kiszamolasa a meretarany es az
        // ablakmeret alapjan
        setViewableWorld(centerX, centerY, 0, 0);

        /**
         * A lathato vilag ujra rajzolasa
         */
//        repaint();



      }//if(getWidth() != 0 && getHeight() != 0)

    }//componentResized(ComponentEvent e)

  }//class WindowResizeListener








  /**
   * Beallitja a vilag lathato mereteit az ablakmeret, es a meretarany alapjan
   * Ide akkor jut, ha mar be van allitva es korrigalva van az ablakmeret
   */
  void setViewableWorld(double centerX, double centerY, double deltaX, double deltaY){
    double startX, startY, stopX, stopY, width, height;
    double pixelWidth = (double)getWidth();
    double pixelHeight = (double)getHeight();

    // A vilag szelessegenek es magassaganak megallapitasa a meretarany es
    // az ablakmeret alapjan
    width = measurement.getPixelToDistance(pixelWidth);
    height = measurement.getPixelToDistance(pixelHeight);

    // A vilag kezdo es vegzo pozicioinak megallapitasa a megadott fixpont alapjan
    startX = centerX - (deltaX * width / pixelWidth);
    stopX = startX + width;
    startY = centerY - (deltaY * height / pixelHeight);
    stopY = startY + height;

    // A kezdo es vegpontok korrekcioja, ha esetleg kilognanak az eredetileg
    // megadott keretbol
    if(startX < worldTotal.getStartX()){
      startX = worldTotal.getStartX();
      stopX = startX + width;
    }else if(stopX > worldTotal.getStopX()){
      stopX = worldTotal.getStopX();
      startX = stopX - width;
    }
    if(startY < worldTotal.getStartY()){
      startY = worldTotal.getStartY();
      stopY = startY + height;
    }else if(stopY > worldTotal.getStopY()){
      stopY = worldTotal.getStopY();
      startY = stopY - height;
    }



    // A vilag lathato mereteinek rogzitese
    worldViewable.setSize(startX, startY, stopX, stopY);

  }


  void setIntendWorld(double centerX, double centerY, double deltaX, double deltaY){
    double startX, startY, stopX, stopY, width, height;
    double pixelWidth = (double)getWidth();
    double pixelHeight = (double)getHeight();

    //A vilag szelessegenek es magassaganak megallapitasa a meretarany es
    // az ablakmeret alapjan
    width = measurement.getPixelToDistance(pixelWidth);
    height = measurement.getPixelToDistance(pixelHeight);

    //A vilag kezdo es vegzo pozicioinak megallapitasa a megadott fixpont alapjan
    startX = centerX - (deltaX * width / pixelWidth);
    stopX = startX + width;
    startY = centerY - (deltaY * height / pixelHeight);
    stopY = startY + height;

    // A kezdo es vegpontok korrekcioja, ha esetleg kilognanak az eredetileg
    // megadott keretbol
    if(stopX > worldTotal.getStopX()){
      stopX = worldTotal.getStopX();
      startX = stopX - width;
    }
    if(stopY > worldTotal.getStopY()){
      stopY = worldTotal.getStopY();
      startY = stopY - height;
    }

    if(startX < worldTotal.getStartX()){
      startX = worldTotal.getStartX();
      stopX = startX + width;
    }
    if(startY < worldTotal.getStartY()){
      startY = worldTotal.getStartY();
      stopY = startY + height;
    }

    //A vilag megmutando mereteinek rogzitese
    worldIntend.setSize(startX, startY, stopX, stopY);

  }





  /**
   * Visszaadja az aktualis ablakmeretnek egy korrekciojat, mely csokkenti
   * a mereteket, ha az aktualis meretarany eseten az adott ablakmeretben
   * a vilag merete nagyobb lenne mint az eredetileg maximalt ertek
   * Kulon kellett valasztani a getPreferredSize() metodustol, mert szamol az
   * aktualis meretarannyal, a getPreferredSize() eseten pedig az meg nem
   * ismert.
   */
  Dimension getCorrectionedSize(Dimension d){
    double worldWidth, worldHeight;
    double pixelWidth = d.getWidth();
    double pixelHeight = d.getHeight();

    /**
     * Az aktualis ablakba belefero vilag meretei
     */
    worldWidth = measurement.getPixelToDistance(pixelWidth);
    worldHeight = measurement.getPixelToDistance(pixelHeight);

    /**
     * Ha a lathato vilag meretei nagyobbak lennenek az eredetileg meghatarozott
     * vilag mereteinel
     */
    if(worldWidth > worldTotal.getWidth()){

      // Akkor csokkenteni kell modell (ablak) meretet
      worldWidth = worldTotal.getWidth();
      pixelWidth = worldWidth * measurement.getPixel() / measurement.getScale();

    }

    if(worldHeight > worldTotal.getHeight()){
      worldHeight = worldTotal.getHeight();
      pixelHeight = worldHeight * measurement.getPixel() / measurement.getScale();

    }

    return new Dimension((int)Math.floor(pixelWidth), (int)Math.floor(pixelHeight));
  }





}//class Canvas



interface WindowSizeChangeListener{
  public void changed(Dimension size);
}











