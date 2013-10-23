package hu.akoel.mgu.jcanvas.own;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ExampleJCanvas_Grid extends JFrame {

	private static final long serialVersionUID = 5810956401235486862L;

	JCanvas myCanvas;
	private Size worldSize = new Size(-10.0, -10.0, 10.0, 30);	
	private Color background = Color.black;
	private Position positionToMiddle = new Position( 0, 0);
	private double pixelPerUnit = 10;

	JGrid myGrid;	
	private Color gridColor = Color.green;
	private int gridWidth = 1;
	private Position gridDelta = new Position(1.0, 1.0);
	private JGrid.PainterPosition gridPosition = JGrid.PainterPosition.DEEPEST; 
	private JGrid.Type gridType = JGrid.Type.DOT;
	
	JOrigo myOrigo;
	private Color origoColor = Color.red;
	private int origoWidthInPixel = 5;
	private double origoLength = 1;
	private JOrigo.PainterPosition origoPosition = JOrigo.PainterPosition.DEEPEST;
	
	JAxis myAxis;
	private Color axisColor = Color.yellow;
	private int axisWidthInPixel = 3;
	private JAxis.AxisPosition axisPosition = JAxis.AxisPosition.AT_LEFT_BOTTOM;
	private JAxis.PainterPosition painterPosition = JAxis.PainterPosition.HIGHEST;
		
	
	public static void main(String[] args) {		
		new ExampleJCanvas_Grid();
	}

	public ExampleJCanvas_Grid() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Proba");
		this.setUndecorated(false);
		this.setSize(500, 300);
		this.createBufferStrategy(1);

		myCanvas = new JCanvas(BorderFactory.createLoweredBevelBorder(), background, worldSize );
//		myCanvas = new JCanvas(BorderFactory.createLoweredBevelBorder(), background, pixelPerUnit, positionToMiddle);

		myGrid = new JGrid( myCanvas, gridType, gridColor, gridWidth, gridPosition, gridDelta );		
		
		myOrigo = new JOrigo( myCanvas, origoColor, origoWidthInPixel, origoLength, origoPosition);
	
		myAxis = new JAxis(myCanvas, axisPosition, axisColor, axisWidthInPixel, painterPosition);
		
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
		//Kirajzol eloterbe egy fuggvenyt
		//
		JButton drawFunctionButton = new JButton("draw Function");
		drawFunctionButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				//myCanvas.removePainterListenersFromHighest();
				myCanvas.addPainterListenerToHighest(new PainterListener(){
					
					@Override
					public void paintByWorldPosition(JCanvas canvas, JGraphics g2) {					
						g2.setColor(new Color(250, 200, 0));
						g2.setStroke(new BasicStroke(3));
						
						Position previous = null;
						double increment = canvas.getWorldXLengthByPixel(2);
						double start = canvas.getWorldXByPixel(0);
						double stop = canvas.getWorldXByPixel(canvas.getViewableSize().width );
						for( double x=start; x<=stop; x+=increment ){
							double y = 0.01*(x*x*x) - 0.07*(x*x) + 0.1*(x) - 0;
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
					
				}, JCanvas.Level.UNDER);		
				myCanvas.repaint();
			}
			
		});

		//
		//Grid ki/be kapcsolo
		//
		JCheckBox turnOnGrid = new JCheckBox("Turn On Grid");
		turnOnGrid.setSelected(true);
		turnOnGrid.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if( e.getStateChange() == ItemEvent.DESELECTED){
					myGrid.turnOff();
				}else{
					myGrid.turnOn();
				}
				myCanvas.repaint();
			}
		});
		
		//
		//Origo ki/be kapcsolo
		//
		JCheckBox turnOnOrigo = new JCheckBox("Turn On Origo");
		turnOnOrigo.setSelected(true);
		turnOnOrigo.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if( e.getStateChange() == ItemEvent.DESELECTED){
					myOrigo.turnOff();
				}else{
					myOrigo.turnOn();
				}
				myCanvas.repaint();
			}
		});
		
		//
		//Axis ki/be kapcsolo
		//
		JCheckBox turnOnAxis = new JCheckBox("Turn On Axis");
		turnOnAxis.setSelected(true);
		turnOnAxis.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if( e.getStateChange() == ItemEvent.DESELECTED){
					myAxis.turnOff();
				}else{
					myAxis.turnOn();
				}
				myCanvas.repaint();
			}
		});
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.add(turnOnAxis);
		controlPanel.add(turnOnOrigo);
		controlPanel.add(turnOnGrid);
		
		//
		// Iranyito gombok
		//
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
		
		JPanel mainControlPanel = new JPanel();
		mainControlPanel.setLayout(new BoxLayout(mainControlPanel, BoxLayout.Y_AXIS	));
		mainControlPanel.add(controlPanel);
		mainControlPanel.add(translationPanel);
		
		JPanel drawPanel = new JPanel();
		drawPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 2, 2));
		
		drawPanel.add(reprintButton);
		drawPanel.add(drawFunctionButton);
		
		this.getContentPane().setLayout(new BorderLayout(10,10));
		this.getContentPane().add(myCanvas, BorderLayout.CENTER);
		this.getContentPane().add(new JLabel(), BorderLayout.NORTH);
		this.getContentPane().add(drawPanel, BorderLayout.SOUTH);
		this.getContentPane().add(mainControlPanel, BorderLayout.EAST);
		this.getContentPane().add(new JLabel(), BorderLayout.WEST);

		this.setVisible(true);

	}
}

