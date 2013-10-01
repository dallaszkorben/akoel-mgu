package hu.akoel.mgu.jcanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.math.*;

/**
 * <p>Title: pipeline</p>
 * <p>Description: Pipeline network designer</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: akoel@soft</p>
 * @author akoel
 * @version 1.0
 */

public class ExampleCanvas extends JFrame{
  JTextField display;
  Canvas c;

  public ExampleCanvas() {
    super();

    this.getContentPane().setLayout(new BorderLayout(5, 5));

    //Canvas Segedpanel letrehozasa
    JPanel canvasTmp = new JPanel();
    canvasTmp.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

    //Billentyuzet segedpanel
    JPanel buttonTmp = new JPanel();
    buttonTmp.setLayout(new GridBagLayout());
    GridBagConstraints co = new GridBagConstraints();
    co.fill = GridBagConstraints.BOTH;

    co.gridx = 1;
    co.gridy = 0;
    JButton up = new JButton("Up");
    buttonTmp.add(up, co);

    co.gridx = 0;
    co.gridy = 1;
    JButton left = new JButton("Left");
    buttonTmp.add(left, co);

    co.gridx = 2;
    co.gridy = 1;
    JButton right = new JButton("Right");
    buttonTmp.add(right, co);

    co.gridx = 1;
    co.gridy = 2;
    JButton down = new JButton("Down");
    buttonTmp.add(down, co);


    //CoordCanvas letrehozasa
    c = new Canvas(canvasTmp, Canvas.TYPE_FREE_WINDOW_PORTION, new Size(-5, -1, 5, 10), Color.gray);

    c.setMeasurementScale(100);

    c.setGridType(Grid.TYPE_SOLID);
    c.setGridSize(1, 1);
    c.setOrigoEnabled(true);
    c.setOrigoSize(2.5);

    c.setHorizontalInsideAxisEnabled(true);
    c.setHorizontalInsideAxisTitleText("távolság [m]");
    c.setHorizontalInsideAxisTitlePosition(InsideAxisPart.TITLE_UP_RIGHT);
    c.setHorizontalInsideAxisNumberPosition(InsideAxisPart.NUMBER_DOWN);
    c.setVerticalInsideAxisEnabled(true);
    c.setVerticalInsideAxisTitleText("távolság [m]");
    c.setVerticalInsideAxisTitlePosition(InsideAxisPart.TITLE_DOWN_LEFT);
    c.setVerticalInsideAxisNumberPosition(InsideAxisPart.NUMBER_DOWN);

    //Segedelemek folott kirajzolando kep
    c.addDrawAboveInterface(new DrawInterface(){
      public void draw(Graphics2D offg2, Measurement measurement, Size totalSize, Size viewableSize){
        double startX = viewableSize.getStartX();
        double stopX = viewableSize.getStopX();
        double delta = (stopX - startX) / 100;
        double prevX = Double.NaN, prevY = Double.NaN;

        offg2.setColor(Color.red);
        prevX = (int)(measurement.getDistanceToPixel(startX));
        prevY = (int)(measurement.getDistanceToPixel(startX*startX));

        for(double i = startX; i<stopX; i+=delta){
          offg2.drawLine((int)prevX, (int)prevY, (int)(measurement.getDistanceToPixel(i)), (int)(measurement.getDistanceToPixel(i*i)));
          prevX = (int)(measurement.getDistanceToPixel(i));
          prevY = (int)(measurement.getDistanceToPixel(i*i));
        }
      }
    });



    /**
     * Jobbra mozgast figyelo osztaly
     */
    right.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        c.moveRight(0.1);
      }
    });

    /**
     * Balra mozgast figyelo osztaly
     */
    left.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        c.moveLeft(0.1);
      }
    });

    /**
     * Felfele mozgast figyelo osztaly
     */
    up.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        c.moveUp(0.1);
      }
    });

    /**
     * Balra mozgast figyelo osztaly
     */
    down.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        c.moveDown(0.1);
      }
    });

    //Segedelemek a szukseges gap-ek elhelyezese erdekeben bal oldalt es fent
    this.getContentPane().add(new JLabel(), BorderLayout.NORTH);
    this.getContentPane().add(new JLabel(), BorderLayout.WEST);

    //Gombcsoport befogadasat vegzo panel
    JPanel buttonAranger = new JPanel();
    buttonAranger.setLayout(new FlowLayout());
    buttonAranger.add(buttonTmp);
    this.getContentPane().add(buttonAranger, BorderLayout.EAST);

    //Canvas elhelyezese
    canvasTmp.add(c);
    this.getContentPane().add(canvasTmp, BorderLayout.CENTER);

    //A meretarany kijelzesenek elhelyezese
    display = new JTextField();
    display.setEditable(false);
    getContentPane().add(display, BorderLayout.SOUTH);


    //Meretarany valtozasat figyelo
    c.addScaleChangeListener(new ScaleChangeListener(){
      public void changed(double e){
        display.setText("M=1:" + e);
      }
    });


    /**
     * Ablak bezarodasat figyelo osztaly
     */
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });


  }

  public static void main(String[] args) {
    ExampleCanvas example = new ExampleCanvas();
    example.pack();
    example.setSize(470, 350);
    example.show();
  }

}