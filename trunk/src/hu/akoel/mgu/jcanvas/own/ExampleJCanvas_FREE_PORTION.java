package hu.akoel.mgu.jcanvas.own;


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

public class ExampleJCanvas_FREE_PORTION extends JFrame {

	private static final long serialVersionUID = 5810956401235486862L;

	Size worldSize;
	Position positionToMiddle = new Position( 0, 0);
	
	public static void main(String[] args) {
		
		new ExampleJCanvas_FREE_PORTION();
	}

	public ExampleJCanvas_FREE_PORTION() {
		worldSize = null;
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Proba");
		this.setUndecorated(false);
		this.setSize(500, 300);
		this.createBufferStrategy(1);

		final JCanvas myCanvas = new JCanvas(BorderFactory.createLineBorder(Color.red, 1), Color.GREEN, 10, positionToMiddle );

			//Eloszorre kirajzolja az origot
			myCanvas.addPainterListenerToUnder(new PainterListener(){
			
			@Override
			public void paintByWorldPosition(JCanvas canvas, JGraphics g2) {}

			@Override
			public void paintByViewer(JCanvas canvas, Graphics2D g2) {	
				int x0 = myCanvas.getPixelXPositionByWorld(0);
				int y0 = myCanvas.getPixelYPositionByWorld(0);
				g2.setColor(Color.yellow);
				g2.setStroke(new BasicStroke(3));
				g2.drawLine(x0-5, y0, x0+5, y0);
				g2.drawLine(x0, y0-5, x0, y0+5);
			}	
			
		}, JCanvas.POSITION.DEEPEST);		
		myCanvas.repaint();
		
		
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
					public void paintByWorldPosition(JCanvas canvas, JGraphics g2) {
						
						g2.setColor(new Color(200, 100, 100));
						if( null == worldSize ){
							g2.drawLine((0.0), (0.0), 80, 80 );
						}else{
							g2.drawLine(worldSize.getXMin(), worldSize.getYMin(), worldSize.getXMax(), worldSize.getYMax() );
						}

						g2.setColor(Color.red);
						g2.setStroke(new BasicStroke(1));

						if( null != worldSize ){
							g2.drawLine(worldSize.getXMin(), worldSize.getYMin(), worldSize.getXMin() + 5, worldSize.getYMin());
							g2.drawLine(worldSize.getXMin(), worldSize.getYMin(), worldSize.getXMin(), worldSize.getYMin() + 5);
							
							g2.drawLine(worldSize.getXMax() - 5, worldSize.getYMax(), worldSize.getXMax(), worldSize.getYMax());
							g2.drawLine(worldSize.getXMax(), worldSize.getYMax() - 5, worldSize.getXMax(), worldSize.getYMax());
						}else{
//System.out.println(canvas.getViewableSize().height + " - " + canvas.getPixelPerUnit() + " - " + canvas.getWorldYByPixel(0) );							
						
							g2.drawLine(canvas.getWorldXByPixel(0), canvas.getWorldYByPixel(canvas.getViewableSize().height ), canvas.getWorldXByPixel(0) + 5, canvas.getWorldYByPixel(canvas.getViewableSize().height ) );
							g2.drawLine(canvas.getWorldXByPixel(0), canvas.getWorldYByPixel(canvas.getViewableSize().height ), canvas.getWorldXByPixel(0), canvas.getWorldYByPixel(canvas.getViewableSize().height ) + 5);
							
							g2.drawLine(canvas.getWorldXByPixel(canvas.getViewableSize().width) - 5, canvas.getWorldYByPixel(0), canvas.getViewableSize().width, canvas.getWorldYByPixel(0) );
							g2.drawLine(canvas.getWorldXByPixel(canvas.getViewableSize().width - 0), canvas.getWorldYByPixel(0), canvas.getWorldXByPixel(canvas.getViewableSize().width - 0), canvas.getWorldYByPixel(0) - 5);
							
						}
						
						g2.setColor(Color.black);
						g2.drawOval(-10, -10, 20, 20);
						g2.setColor(Color.CYAN);
						
						g2.fillOval(-4, -1, 2, 2);
										
					}

					@Override
					public void paintByViewer(JCanvas canvas, Graphics2D g2) {

					}			 
				});	
				myCanvas.repaint();
			}			
		});
		

		
		//
		//Kirajzol atmeneti jelleggel egy x^2 fuggvenyt
		//
		JButton drawTempButton = new JButton("draw x^2");
		drawTempButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				myCanvas.removePainterListenersFromTemporary();
				myCanvas.addPainterListenerToTemporary(new PainterListener(){
					
					@Override
					public void paintByWorldPosition(JCanvas canvas, JGraphics g2) {					
						g2.setColor(new Color(250, 200, 0));
						g2.setStroke(new BasicStroke(3));
						
						Position previous = null;
						double increment = myCanvas.getWorldLengthByPixel(2);
						double start = canvas.getWorldXByPixel(0);
						double stop = canvas.getWorldXByPixel(canvas.getWidth()	);
						for( double x=start; x<=stop; x+=increment ){
							double y = 0.3*x * x;
							if( null == previous ){
								previous = new Position(x, y);
							}
							g2.drawLine(previous.getX(), previous.getY(), x, y);
							previous = new Position(x, y);
						}
						
						g2.setColor(Color.blue);
						g2.drawLine(0, 0, 0, 0);

					}

					@Override
					public void paintByViewer(JCanvas canvas, Graphics2D g2) {}	
					
				}, JCanvas.POSITION.DEEPEST);		
				myCanvas.repaint();
			}
			
		});

		JButton upButton = new JButton("up");
		upButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0 ){			
				myCanvas.moveY(-1);
			}
		});
		
		JButton downButton = new JButton("down");
		downButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0 ){
				myCanvas.moveY(1);
			}
		});
		
		JButton rightButton = new JButton("right");
		rightButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0 ){
				myCanvas.moveX(1);
			}
		});
		
		JButton leftButton = new JButton("left");
		leftButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0 ){
				myCanvas.moveX(-1);
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
