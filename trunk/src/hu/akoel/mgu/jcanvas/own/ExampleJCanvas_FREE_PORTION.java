package hu.akoel.mgu.jcanvas.own;


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

public class ExampleJCanvas_FREE_PORTION extends JFrame {

	private static final long serialVersionUID = 5810956401235486862L;

	
	private Color background = Color.GREEN;
	private TranslateValue positionToMiddle = new TranslateValue( 0, 0 );
	private PossiblePixelPerUnits possiblePixelPerUnits = new PossiblePixelPerUnits(new PixelPerUnitValue(1,1), new RateValue( 1.2, 1.2));

	
	public static void main(String[] args) {
		
		new ExampleJCanvas_FREE_PORTION();
	}

	public ExampleJCanvas_FREE_PORTION() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Proba");
		this.setUndecorated(false);
		this.setSize(500, 300);
		this.createBufferStrategy(1);

		final JCanvas myCanvas = new JCanvas(BorderFactory.createLineBorder(Color.red, 1), background, possiblePixelPerUnits, positionToMiddle );

		//Eloszorre kirajzolja az origot
		myCanvas.addPainterListenerToDeepest(new PainterListener(){
			
			@Override
			public void paintByWorldPosition(JCanvas canvas, JGraphics g2) {}

			@Override
			public void paintByViewer(JCanvas canvas, Graphics2D g2) {	
				int x0 = 0; //myCanvas.getPixelXPositionByWorld(0);
				int y0 = 0; //myCanvas.getPixelYPositionByWorld(0);
				g2.setColor(Color.yellow);
				g2.setStroke(new BasicStroke(3));
				g2.drawLine(x0-5, y0, x0+5, y0);
				g2.drawLine(x0, y0-5, x0, y0+5);
			}	
			
		}, JCanvas.Level.UNDER);		
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
		// Kirajzol eloterbe egy egyenes vonalat es egy kitoltott kort
		// valamint a bal also els jobb felso sarokba egy-egy 
		// celkereszt negyedet
		//
		JButton drawLineButton = new JButton("draw Line");
		drawLineButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				myCanvas.removePainterListenersFromTemporary();
				myCanvas.removePainterListenersFromHighest();
				myCanvas.addPainterListenerToHighest(new PainterListener(){
					
					@Override
					public void paintByWorldPosition(JCanvas canvas, JGraphics g2) {
						
						g2.setColor(new Color(200, 100, 100));
//						if( null == worldSize ){
							g2.drawLine((0.0), (0.0), 80, 80 );
//						}else{
//							g2.drawLine(worldSize.getXMin(), worldSize.getYMin(), worldSize.getXMax(), worldSize.getYMax() );
//						}

						//
						//celkereszt negyed
						//
						g2.setColor(Color.red);
						g2.setStroke(new BasicStroke(1));

					
						g2.drawLine(canvas.getWorldXByPixel(0), canvas.getWorldYByPixel(canvas.getViewableSize().height-1 ), canvas.getWorldXByPixel(0) + 5, canvas.getWorldYByPixel(canvas.getViewableSize().height-1 ) );
						g2.drawLine(canvas.getWorldXByPixel(0), canvas.getWorldYByPixel(canvas.getViewableSize().height-1 ), canvas.getWorldXByPixel(0), canvas.getWorldYByPixel(canvas.getViewableSize().height-1 ) + 5);
						
						g2.drawLine(canvas.getWorldXByPixel(canvas.getViewableSize().width) - 5, canvas.getWorldYByPixel(0), canvas.getViewableSize().width, canvas.getWorldYByPixel(0) );
						g2.drawLine(canvas.getWorldXByPixel(canvas.getViewableSize().width - 1), canvas.getWorldYByPixel(0), canvas.getWorldXByPixel(canvas.getViewableSize().width - 1), canvas.getWorldYByPixel(0) - 5);

						
						//
						// Kor
						//
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
						
						PositionValue previous = null;
						double increment = myCanvas.getWorldXLengthByPixel(2);
						double start = canvas.getWorldXByPixel(0);
						double stop = canvas.getWorldXByPixel(canvas.getWidth()	);
						for( double x=start; x<=stop; x+=increment ){
							double y = 0.3*x * x;
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
					public void paintByViewer(JCanvas canvas, Graphics2D g2) {}	
					
				}, JCanvas.Level.UNDER);		
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
