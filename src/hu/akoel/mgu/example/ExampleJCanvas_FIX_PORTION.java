package hu.akoel.mgu.example;



import hu.akoel.mgu.MGraphics;
import hu.akoel.mgu.MCanvas;
import hu.akoel.mgu.PainterListener;
import hu.akoel.mgu.values.PositionValue;
import hu.akoel.mgu.values.SizeValue;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ExampleJCanvas_FIX_PORTION extends JFrame {

	private static final long serialVersionUID = 5810956401235486862L;

	SizeValue worldSize;
	
	public static void main(String[] args) {	
		
		new ExampleJCanvas_FIX_PORTION();
	}

	public ExampleJCanvas_FIX_PORTION() {
		worldSize = new SizeValue(-10.0, -3.0, 10.0, 25);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Proba");
		this.setUndecorated(false);
		this.setSize(500, 300);
		this.createBufferStrategy(1);

		final MCanvas myCanvas = new MCanvas(BorderFactory.createLoweredBevelBorder(), Color.CYAN, worldSize );

			//Eloszorre kirajzolja az origot
			myCanvas.addPainterListenerToDeepest(new PainterListener(){
			
			@Override
			public void paintByWorldPosition(MCanvas canvas, MGraphics g2) {}

			@Override
			public void paintByViewer(MCanvas canvas, Graphics2D g2) {	
				int x0 = myCanvas.getPixelXPositionByWorld(0);
				int y0 = myCanvas.getPixelYPositionByWorldBeforeTranslate(0);
				g2.setColor(Color.yellow);
				g2.setStroke(new BasicStroke(3));
				g2.drawLine(x0-5, y0, x0+5, y0);
				g2.drawLine(x0, y0-5, x0, y0+5);
			}	
			
		}, MCanvas.Level.UNDER);		
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
				myCanvas.removePainterListenersFromHighest();
				myCanvas.addPainterListenerToHighest(new PainterListener(){
					
					@Override
					public void paintByWorldPosition(MCanvas canvas, MGraphics g2) {
						
						g2.setColor(new Color(200, 100, 100));
						g2.drawLine(worldSize.getXMin(), worldSize.getYMin(), worldSize.getXMax(), worldSize.getYMax() );
						
						g2.setColor(Color.red);
						g2.setStroke(new BasicStroke(1));

						if( null != worldSize ){
							
							g2.drawLine(worldSize.getXMin(), worldSize.getYMin(), worldSize.getXMin() + 5, worldSize.getYMin());
							g2.drawLine(worldSize.getXMin(), worldSize.getYMin(), worldSize.getXMin(), worldSize.getYMin() + 5);
							
							g2.drawLine(worldSize.getXMax() - 5, worldSize.getYMax(), worldSize.getXMax(), worldSize.getYMax());
							g2.drawLine(worldSize.getXMax(), worldSize.getYMax() - 5, worldSize.getXMax(), worldSize.getYMax());
							
						}
						
					}

					@Override
					public void paintByViewer(MCanvas canvas, Graphics2D g2) {}			 
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
					public void paintByWorldPosition(MCanvas canvas, MGraphics g2) {					
						g2.setColor(new Color(250, 200, 0));
						g2.setStroke(new BasicStroke(3));
						
						PositionValue previous = null;
						double increment = canvas.getWorldXLengthByPixel(2);
						double start = canvas.getWorldXByPixel(0);
						double stop = canvas.getWorldXByPixel(canvas.getViewableSize().width );

						for( double x=start; x<=stop; x+=increment ){
							double y = (0.5*x)*(0.5*x);
							if( null == previous ){
								previous = new PositionValue(x, y);
							}
							g2.drawLine(previous.getX(), previous.getY(), x, y);
							previous = new PositionValue(x, y);						
						}
						
						g2.setColor(Color.blue);
						g2.drawLine(0, 0, 0, 0);

					}

					@Override
					public void paintByViewer(MCanvas canvas, Graphics2D g2) {}	
					
				}, MCanvas.Level.UNDER);		
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

