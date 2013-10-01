package hu.akoel.mgu.jcanvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ExampleJCanvas extends JFrame {

	private static final long serialVersionUID = 5810956401235486862L;

	public static void main(String[] args) {
		new ExampleJCanvas();
	}

	public ExampleJCanvas() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Proba");
		this.setUndecorated(false);
		this.setSize(300, 300);
		this.createBufferStrategy(1);

		final JCanvas myCanvas = new JCanvas(BorderFactory.createLoweredBevelBorder(), Color.red, new Dimension(400,400));
		
		
		
		myCanvas.addPainterListenerToPrimary(new PainterListener(){
			@Override
			public void paint(JPanel canvas, Graphics2D g2) {
				for( int j=1; j<200; j++){
					
					g2.setColor(new Color(j, 70, 150));
					int x = (int)(Math.random()*canvas.getWidth());
					int y = (int)(Math.random()*canvas.getHeight());
					int width = (int)(Math.random()*50);
					int height = (int)(Math.random()*50);
//					g2.fillOval(x, y, width, height);
				}
			}			
		}, JCanvas.POSITION.DEEPEST);
	
		myCanvas.addPainterListenerToSecondary(new PainterListener(){
			@Override
			public void paint(JPanel canvas, Graphics2D g2) {
				for( int j=1; j<200; j++){
					
					g2.setColor(new Color(j, j+20, 50));
					int x = (int)(Math.random()*canvas.getWidth());
					int y = (int)(Math.random()*canvas.getHeight());
					int width = (int)(Math.random()*50);
					int height = (int)(Math.random()*50);
//					g2.fillOval(x, y, width, height);
				}
			}			
		}, JCanvas.POSITION.DEEPEST);
		
		myCanvas.addPainterListenerToSecondary(new PainterListener(){
			@Override
			public void paint(JPanel canvas, Graphics2D g2) {
				g2.setColor(Color.black);
//				g2.drawLine(0, 0, canvas.getWidth() - 1, canvas.getHeight() - 1);
//				g2.drawLine(canvas.getWidth() - 1, 0, 0, canvas.getHeight() - 1);
				g2.drawLine(canvas.getWidth()-1, canvas.getHeight() - 20, 395, canvas.getHeight());
			}			
		}, JCanvas.POSITION.HIGHEST);

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
		//Bazis kirajzolasa
		//
		JButton drawBaseButton = new JButton("draw base");
		drawBaseButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				myCanvas.removePainterListenersFromPrimary();
				myCanvas.addPainterListenerToPrimary(new PainterListener(){
					@Override
					public void paint(JPanel canvas, Graphics2D g2) {
						
						for (int i = 2; i < 200; i++) {
							g2.setColor(new Color(i, 100, 100));
							g2.drawLine(0, 0, canvas.getWidth() - i, canvas.getHeight() - i);
							g2.drawLine(canvas.getWidth() - i, 0, 0, canvas.getHeight() - i);
						}				
					}			
				});				
				
				myCanvas.repaint();				
			}			
		});
		
		JButton drawTemporaryButton = new JButton("draw temporary");

		
		
		drawTemporaryButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				BufferStrategy bf = this.getBufferStrategy();
				Graphics g = null;
			 
				try {
					g = bf.getDrawGraphics();
			 
					// It is assumed that mySprite is created somewhere else.
					// This is just an example for passing off the Graphics object.
					mySprite.draw(g);
			 
				} finally {
					// It is best to dispose() a Graphics object when done with it.
					g.dispose();
				}
			 
				// Shows the contents of the backbuffer on the screen.
				bf.show();
			 
				//Tell the System to do the Drawing now, otherwise it can take a few extra ms until 
				//Drawing is done which looks very jerky
				Toolkit.getDefaultToolkit().sync();				
			}			
		});
		
		
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 2, 2));
		
		buttonPanel.add(reprintButton);
		buttonPanel.add(drawBaseButton);
		
		this.getContentPane().setLayout(new BorderLayout(10,10));
		this.getContentPane().add(myCanvas, BorderLayout.CENTER);
		this.getContentPane().add(new JLabel(), BorderLayout.NORTH);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		this.getContentPane().add(new JLabel(), BorderLayout.EAST);
		this.getContentPane().add(new JLabel(), BorderLayout.WEST);

		this.setSize(300, 300);
		this.setVisible(true);

	}
}

