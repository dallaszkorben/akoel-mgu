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

	Dimension worldSize;
	
	public static void main(String[] args) {
		new ExampleJCanvas();
	}

	public ExampleJCanvas() {
		worldSize = new Dimension(900, 400);
		//worldSize = null;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Proba");
		this.setUndecorated(false);
		this.setSize(500, 300);
		this.createBufferStrategy(1);

		final JCanvas myCanvas = new JCanvas(BorderFactory.createLoweredBevelBorder(), Color.CYAN, worldSize, JCanvas.SIDES_TYPE.FIX_PORTION	);

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
		JButton drawWallButton = new JButton("draw Wall");
		drawWallButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				myCanvas.removePainterListenersFromTemporary();
				myCanvas.removePainterListenersFromAbove();
				myCanvas.addPainterListenerToAbove(new PainterListener(){
					
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
		
		//
		//Hatterbe kirajzol 200 db ovalist
		//
		JButton drawOvalsButton = new JButton("draw Ovals");
		drawOvalsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				myCanvas.removePainterListenersFromTemporary();
				myCanvas.removePainterListenersFromUnder();
				myCanvas.addPainterListenerToUnder(new PainterListener(){
					@Override
					public void paint(JPanel canvas, Graphics2D g2) {
						for( int j=1; j<2000; j++){
							
							g2.setColor(new Color(33, 70, 150));
							int x = (int)(Math.random()*canvas.getWidth());
							int y = (int)(Math.random()*canvas.getHeight());
							int width = (int)(Math.random()*50);
							int height = (int)(Math.random()*50);
							g2.fillOval(x, y, width, height);
						}					
					}			
				}, JCanvas.POSITION.DEEPEST);
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

		
		
	
/*		
		myCanvas.addPainterListenerToAbove(new PainterListener(){
			@Override
			public void paint(JPanel canvas, Graphics2D g2) {
				g2.setColor(Color.black);
//				g2.drawLine(0, 0, canvas.getWidth() - 1, canvas.getHeight() - 1);
//				g2.drawLine(canvas.getWidth() - 1, 0, 0, canvas.getHeight() - 1);
				g2.drawLine(canvas.getWidth()-1, canvas.getHeight() - 20, 395, canvas.getHeight());
			}			
		}, JCanvas.POSITION.HIGHEST);
*/
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 2, 2));
		
		buttonPanel.add(reprintButton);
		buttonPanel.add(drawWallButton);
		buttonPanel.add(drawOvalsButton);
		buttonPanel.add(drawTempButton);
		
		this.getContentPane().setLayout(new BorderLayout(10,10));
		this.getContentPane().add(myCanvas, BorderLayout.CENTER);
		this.getContentPane().add(new JLabel(), BorderLayout.NORTH);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		this.getContentPane().add(new JLabel(), BorderLayout.EAST);
		this.getContentPane().add(new JLabel(), BorderLayout.WEST);

		this.setVisible(true);

	}
}

