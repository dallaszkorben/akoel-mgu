package hu.akoel.mgu.jcanvas;

import hu.akoel.mgu.jcanvas.own.JCanvas;
import hu.akoel.mgu.jcanvas.own.JGraphics;
import hu.akoel.mgu.jcanvas.own.Offset;
import hu.akoel.mgu.jcanvas.own.PainterListener;
import hu.akoel.mgu.jcanvas.own.Size;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ExampleJCanvasWithTranslate extends JFrame {

	private static final long serialVersionUID = 5810956401235486862L;

	Size worldSize;
	
	public static void main(String[] args) {
		
		new ExampleJCanvasWithTranslate();
	}

	public ExampleJCanvasWithTranslate() {
		worldSize = new Size(10.0, 30.0);
		//worldSize = null;
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Proba");
		this.setUndecorated(false);
		this.setSize(500, 300);
		this.createBufferStrategy(1);

		final JCanvas myCanvas = new JCanvas(BorderFactory.createLoweredBevelBorder(), Color.CYAN, worldSize, BigDecimal.valueOf(20), JCanvas.SIDES_PORTION.FIX_PORTION	);

		//
		//Ujra rajzol minden statikus rajzi elemet
		//
		JButton reprintButton = new JButton("reprint");
		reprintButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				myCanvas.repaint();				
			}			
		});	
		
		//
		//Kirajzol eloterbe egy egyenes vonalat
		//
		JButton drawLineButton = new JButton("draw Line");
		drawLineButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				myCanvas.removePainterListenersFromTemporary();
				myCanvas.removePainterListenersFromAbove();
				myCanvas.addPainterListenerToAbove(new PainterListener(){
					
					@Override
					public void paint(JPanel canvas, JGraphics g2) {
						
						g2.setColor(new Color(200, 100, 100));
						g2.drawLine(BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0), worldSize.getWidth(), worldSize.getHeight() );
//						g2.fillOval(worldSize.getWidth().subtract(BigDecimal.valueOf(8)), worldSize.getHeight().subtract(BigDecimal.valueOf(8)), BigDecimal.valueOf(17), BigDecimal.valueOf(17));
//						g2.fillOval(-8, -8, 17, 17);

						g2.setColor(Color.red);
						g2.setStroke(new BasicStroke(1));
						g2.drawLine(0, 0, 10, 0 );
						g2.drawLine(0, 0, 0, 10 );
						g2.drawLine(worldSize.getWidth().doubleValue()-10, worldSize.getHeight().doubleValue(), worldSize.getWidth().doubleValue(), worldSize.getHeight().doubleValue());
						g2.drawLine(worldSize.getWidth().doubleValue(), worldSize.getHeight().doubleValue()-10, worldSize.getWidth().doubleValue(), worldSize.getHeight().doubleValue());
//System.err.println(myCanvas.getPixelYPositionByWorld(BigDecimal.valueOf(0)));						
//System.err.println(myCanvas.getWorldLengthByPixel(1) + " - " + myCanvas.getWorldLengthByPixel(1).multiply(worldSize.getWidth()));
//System.err.println(myCanvas.getWorldTranslate().getY() + " - " + myCanvas.getPixelLengthByWorld(myCanvas.getWorldTranslate().getY()));						
						
						
					}			 
				});	
				myCanvas.repaint();
			}			
		});
		

		
		//
		//Kirajzol atmeneti jelleggel egy x^2 fuggvenyt
		//
		JButton drawTempButton = new JButton("draw Temp");
		drawTempButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				myCanvas.removePainterListenersFromTemporary();
				myCanvas.addPainterListenerToTemporary(new PainterListener(){
					
					@Override
					public void paint(JPanel canvas, JGraphics g2) {					
						g2.setColor(new Color(250, 200, 0));
						g2.setStroke(new BasicStroke(1));
						
						Offset previous = null;
						double increment = myCanvas.getWorldLengthByPixel(1).doubleValue();
						for( double x=0; x<=worldSize.getWidth().doubleValue(); x+=increment ){
							double y = 0.3*x * x;
							if( null == previous ){
								previous = new Offset(x, y);
							}
							g2.drawLine(previous.getXDouble(), previous.getYDouble(), x, y);
							previous = new Offset(x, y);
						}
						
						g2.setColor(Color.blue);
						g2.drawLine(0, 0, 0, 0);

					}			
				}, JCanvas.POSITION.DEEPEST);		
				myCanvas.repaint();
			}
			
		});

		JButton upButton = new JButton("up");
		upButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0 ){			
				myCanvas.moveUp(1);
			}
		});
		
		JButton downButton = new JButton("down");
		downButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0 ){
				myCanvas.moveDown(1);
			}
		});
		
		JButton rightButton = new JButton("right");
		rightButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0 ){
				myCanvas.moveRight(1);
			}
		});
		
		JButton leftButton = new JButton("left");
		leftButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0 ){
				myCanvas.moveLeft(1);
			}
		});
		
		JPanel translationPanel = new JPanel();	
		translationPanel.setLayout(new GridLayout(3,3));
		translationPanel.add(new JLabel());
		translationPanel.add(upButton);
		translationPanel.add(new JLabel());
		translationPanel.add(leftButton);
		translationPanel.add(new JLabel());
		translationPanel.add(rightButton);
		translationPanel.add(new JLabel());
		translationPanel.add(downButton);
		translationPanel.add(new JLabel());
		
		JPanel drawPanel = new JPanel();
		drawPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 2, 2));
		
		drawPanel.add(reprintButton);
		drawPanel.add(drawLineButton);
		drawPanel.add(drawTempButton);
		
		this.getContentPane().setLayout(new BorderLayout(10,10));
		this.getContentPane().add(myCanvas, BorderLayout.CENTER);
		this.getContentPane().add(new JLabel(), BorderLayout.NORTH);
		this.getContentPane().add(drawPanel, BorderLayout.SOUTH);
		this.getContentPane().add(translationPanel, BorderLayout.EAST);
		this.getContentPane().add(new JLabel(), BorderLayout.WEST);

		this.setVisible(true);

	}
}

