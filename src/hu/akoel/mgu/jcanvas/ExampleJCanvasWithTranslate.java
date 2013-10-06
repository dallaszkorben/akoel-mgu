package hu.akoel.mgu.jcanvas;

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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ExampleJCanvasWithTranslate extends JFrame {

	private static final long serialVersionUID = 5810956401235486862L;

	Dimension worldSize;
	
	public static void main(String[] args) {
		new ExampleJCanvasWithTranslate();
	}

	public ExampleJCanvasWithTranslate() {
		worldSize = new Dimension(300, 300);
		//worldSize = null;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Proba");
		this.setUndecorated(false);
		this.setSize(500, 300);
		this.createBufferStrategy(1);

		final JCanvas myCanvas = new JCanvas(BorderFactory.createLoweredBevelBorder(), Color.CYAN, worldSize, 1.0, JCanvas.SIDES_TYPE.FREE_PORTION	);

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
		//Kirajzolja eloterbe a falat
		//
		JButton drawLineButton = new JButton("draw Line");
		drawLineButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				myCanvas.removePainterListenersFromTemporary();
				myCanvas.removePainterListenersFromAbove();
				myCanvas.addPainterListenerToAbove(new PainterListener(){
					
					@Override
					public void paint(JPanel canvas, Graphics2D g2) {
						
						g2.setColor(new Color(200, 100, 100));
						g2.drawLine(0, 0, worldSize.width, worldSize.height );
						g2.fillOval(worldSize.width-8, worldSize.height-8, 17, 17);
						g2.fillOval(-8, -8, 17, 17);
						
						g2.setColor(Color.black);
						g2.setStroke(new BasicStroke(3));
						g2.drawLine(0, 0, 10, 0 );
						g2.drawLine(0, 0, 0, 10 );
						g2.drawLine(worldSize.width-10, worldSize.height-1, worldSize.width, worldSize.height-1);
						g2.drawLine(worldSize.width-1, worldSize.height-10, worldSize.width-1, worldSize.height);
						
						//g2.fillOval(290, 290, 17, 17);
//						g2.drawLine(canvas.getWidth() - i, 0, 0, canvas.getHeight() - i);
						
					}			
				});	
				myCanvas.repaint();
			}			
		});
		

		
		//
		//Kirajzol egy piros kort veletlen pozicioba atmeneti jelleggel
		//
		JButton drawTempButton = new JButton("draw Temp");
		drawTempButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				myCanvas.removePainterListenersFromTemporary();
				myCanvas.addPainterListenerToTemporary(new PainterListener(){
					@Override
					public void paint(JPanel canvas, Graphics2D g2) {					
						g2.setColor(new Color(250, 0, 0));
						int x = (int)(Math.random()*canvas.getWidth());
						int y = (int)(Math.random()*canvas.getHeight());
						int width = 50;
						int height = 50;
						g2.fillOval(x, y, width, height);						
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

