package hu.akoel.mgu.jcanvas.own;


import hu.akoel.mgu.jcanvas.own.JAxis.AxisPosition;

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
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ExampleJCanvas_Scale_SameUnits_DiscretZoom extends JFrame {

	private static final long serialVersionUID = 5810956401235486862L;

	private JCanvas myCanvas;
	private Size worldSize = new Size(-10.0, -10.0, 10.0, 30);	
	private Size boundSize = new Size(0.0, 0.0, 400.0, 400);	
	private Color background = Color.black;
	private Position positionToMiddle = null;//new Position( 10, 10);
	private PossiblePixelPerUnits possiblePixelPerUnits = new PossiblePixelPerUnits(new Position(1,1));
	//private PossiblePixelPerUnits possiblePixelPerUnits = new PossiblePixelPerUnits(new Position(1,1), new Position(1.2, 1.2), new Position(1,1), new Position(15,15));

	private JGrid myGrid;	
	private Color gridColor = Color.green;
	private int gridWidth = 1;
	private Position gridDelta = new Position(1.0, 1.0);
	private JGrid.PainterPosition gridPosition = JGrid.PainterPosition.DEEPEST; 
	private JGrid.Type gridType = JGrid.Type.DOT;
	
	private JOrigo myOrigo;
	private Position origoPosition = new Position( 5, 5 );
	private Color origoColor = Color.red;
	private int origoWidthInPixel = 5;
	private double origoLength = 1;
	private JOrigo.PainterPosition origoPainterPosition = JOrigo.PainterPosition.DEEPEST;
	
	private JAxis myAxis;
	private Color axisColor = Color.yellow;
	private int axisWidthInPixel = 1;
	private JAxis.AxisPosition axisPosition = JAxis.AxisPosition.AT_LEFT_BOTTOM;
	private JAxis.PainterPosition painterPosition = JAxis.PainterPosition.HIGHEST;
		
	private JScale myScale;
	private double pixelPerCm = 42.1;
	private JScale.UNIT unit = JScale.UNIT.m;
//	private double startScale = 100;
//	private Position rate = new Position(1.2, 1.2);
	private ArrayList<Position> possibleScaleList = new ArrayList<Position>();
	
	private JRadioButton lbAxisSelector;
	private JRadioButton rbAxisSelector;
	private JRadioButton ltAxisSelector;
	private JRadioButton rtAxisSelector;
	private JRadioButton zzAxisSelector;
	
	public static void main(String[] args) {		
		new ExampleJCanvas_Scale_SameUnits_DiscretZoom();
	}

	public ExampleJCanvas_Scale_SameUnits_DiscretZoom() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Proba");
		this.setUndecorated(false);
		this.setSize(500, 300);
		this.createBufferStrategy(1);

//		myCanvas = new JCanvas(BorderFactory.createLoweredBevelBorder(), background, worldSize );
		myCanvas = new JCanvas(BorderFactory.createLoweredBevelBorder(), background, possiblePixelPerUnits, positionToMiddle, boundSize);

		myGrid = new JGrid( myCanvas, gridType, gridColor, gridWidth, gridPosition, gridDelta );		
		
		myOrigo = new JOrigo( myCanvas, origoPosition, origoColor, origoWidthInPixel, origoLength, origoPainterPosition);
	
		myAxis = new JAxis(myCanvas, axisPosition, axisColor, axisWidthInPixel, painterPosition);
		
		possibleScaleList.add( new Position( 2, 2 ));
		possibleScaleList.add( new Position( 5, 5 ));
		possibleScaleList.add( new Position( 8, 8 ));
		possibleScaleList.add( new Position( 10, 10 ));
		possibleScaleList.add( new Position( 20, 20 ));
		possibleScaleList.add( new Position( 40, 40 ));
		possibleScaleList.add( new Position( 50, 50 ));
		possibleScaleList.add( new Position( 100, 100 ));
		possibleScaleList.add( new Position( 200, 200 ));
		possibleScaleList.add( new Position( 500, 500 ));
		possibleScaleList.add( new Position( 1000, 1000 ));
			
		myScale = new JScale(myCanvas, pixelPerCm, unit, possibleScaleList, 7);
		
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
		//Axis
		//
		ActionListener axisSelectorActionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if( e.getSource() == lbAxisSelector){
					myAxis.setAxisPosition( AxisPosition.AT_LEFT_BOTTOM );				
				}else if( e.getSource() == rbAxisSelector){
					myAxis.setAxisPosition( AxisPosition.AT_RIGHT_BOTTOM );
				}else if( e.getSource() == ltAxisSelector){
					myAxis.setAxisPosition( AxisPosition.AT_LEFT_TOP );
				}else if( e.getSource() == rtAxisSelector){
					myAxis.setAxisPosition( AxisPosition.AT_RIGHT_TOP );
				}else if( e.getSource() == zzAxisSelector){
					myAxis.setAxisPosition( AxisPosition.AT_ZERO_ZERO );
				}
				myAxis.refresh();
			}
		};
		
		ButtonGroup bg = new ButtonGroup();
		lbAxisSelector = new JRadioButton( "LEFT BOTTOM", true);
		bg.add(lbAxisSelector);
		lbAxisSelector.addActionListener(axisSelectorActionListener);
		rbAxisSelector = new JRadioButton( "RIGHT BOTTOM");
		bg.add(rbAxisSelector);
		rbAxisSelector.addActionListener(axisSelectorActionListener);
		ltAxisSelector = new JRadioButton( "LEFT TOP");
		bg.add(ltAxisSelector);
		ltAxisSelector.addActionListener(axisSelectorActionListener);
		rtAxisSelector = new JRadioButton( "RIGHT TOP");
		bg.add(rtAxisSelector);
		rtAxisSelector.addActionListener(axisSelectorActionListener);
		zzAxisSelector = new JRadioButton( "ZERO ZERO");
		bg.add(zzAxisSelector);
		zzAxisSelector.addActionListener(axisSelectorActionListener);
		
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
					lbAxisSelector.setEnabled(false);
					rbAxisSelector.setEnabled(false);
					ltAxisSelector.setEnabled(false);
					rtAxisSelector.setEnabled(false);
					zzAxisSelector.setEnabled(false);					
				}else{
					myAxis.turnOn();
					lbAxisSelector.setEnabled(true);
					rbAxisSelector.setEnabled(true);
					ltAxisSelector.setEnabled(true);
					rtAxisSelector.setEnabled(true);
					zzAxisSelector.setEnabled(true);
				}
				myCanvas.repaint();
			}
		});
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		
		//Axis elemei
		controlPanel.add(turnOnAxis);
		controlPanel.add(lbAxisSelector);
		controlPanel.add(rbAxisSelector);
		controlPanel.add(ltAxisSelector);
		controlPanel.add(rtAxisSelector);
		controlPanel.add(zzAxisSelector);
		
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

