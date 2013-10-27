package hu.akoel.mgu.jcanvas.own;


import hu.akoel.mgu.jcanvas.own.JAxis.AxisPosition;
import hu.akoel.mgu.jcanvas.own.JScale.UNIT;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ExampleJCanvas_Scale_DifferentUnits_ScaleZoom extends JFrame {

	private static final long serialVersionUID = 5810956401235486862L;

	private JCanvas myCanvas;
	private Size worldSize = new Size(-10.0, -10.0, 10.0, 30);	
	private Size boundSize = new Size(0.0, 0.0, 40.0, 40);	
	private Color background = Color.black;
	private Position positionToMiddle = null;//new Position( 10, 10);
	private PossiblePixelPerUnits possiblePixelPerUnits = new PossiblePixelPerUnits(new Position(1,1));
	//private PossiblePixelPerUnits possiblePixelPerUnits = new PossiblePixelPerUnits(new Position(1,1), new Position(1.2, 1.2), new Position(1,1), new Position(15,15));
	//private Position pixelPerUnit = new Position(1,1);

	private JGrid myGrid;	
	private Color gridColor = Color.green;
	private int gridWidth = 1;
	private Position gridDelta = new Position(1.0, 1.0);
	private JGrid.PainterPosition gridPosition = JGrid.PainterPosition.DEEPEST; 
	private JGrid.Type gridType = JGrid.Type.DOT;
	
	private JCrossLine myCrossLine;
	private Position crossLinePosition = new Position( 5, 5 );
	private Color crossLineColor = Color.red;
	private int crossLineWidthInPixel = 5;
	private Position crossLineLength = new Position( 1, 1 );
	private JCrossLine.PainterPosition crossLinePainterPosition = JCrossLine.PainterPosition.DEEPEST;
	
	private JAxis myAxis;
	private Color axisColor = Color.yellow;
	private int axisWidthInPixel = 1;
	private JAxis.AxisPosition axisPosition = JAxis.AxisPosition.AT_LEFT_BOTTOM;
	private JAxis.PainterPosition painterPosition = JAxis.PainterPosition.HIGHEST;
		
	private JScale myScale;
	private double pixelPerCmX = 42.1;
	private JScale.UNIT unitX = JScale.UNIT.km;
	private double startScaleX = 100000;
	private double pixelPerCmY = 42.1;
	private JScale.UNIT unitY = JScale.UNIT.m;
	private double startScaleY = 100;
	private Position rate = new Position(1.2, 1.2);
	private Position minScale = new Position( 2000, 2);
	private Position maxScale = new Position( 600000, 600);
	
	private JRadioButton lbAxisSelector;
	private JRadioButton rbAxisSelector;
	private JRadioButton ltAxisSelector;
	private JRadioButton rtAxisSelector;
	private JRadioButton zzAxisSelector;
	
	private JTextField xScaleField;
	private JTextField yScaleField;
	private JTextField xPositionField;
	private JTextField yPositionField;
	
	private JComboBox<String> gridTypeCombo;
	private JComboBox<String> gridWidthCombo;
	private JComboBox<String> crossLineWidthCombo;
	
	private JTextField crossLineXPosField;
	private JTextField crossLineYPosField;
	private JTextField crossLineXLengthField;
	private JTextField crossLineYLengthField;
	
	private JTextField gridXDeltaField;
	private JTextField gridYDeltaField;
	
	public static void main(String[] args) {		
		new ExampleJCanvas_Scale_DifferentUnits_ScaleZoom();
	}

	public ExampleJCanvas_Scale_DifferentUnits_ScaleZoom() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Proba");
		this.setUndecorated(false);
		this.setSize(700, 700);
		this.createBufferStrategy(1);

//		myCanvas = new JCanvas(BorderFactory.createLoweredBevelBorder(), background, worldSize );
//		myCanvas = new JCanvas(BorderFactory.createLoweredBevelBorder(), background, possiblePixelPerUnits, positionToMiddle, boundSize);
		myCanvas = new JCanvas(BorderFactory.createLoweredBevelBorder(), background, possiblePixelPerUnits, positionToMiddle);
		myCanvas.addPositionChangeListener(new PositionChangeListener() {
			
			@Override
			public void getWorldPosition(double xPosition, double yPosition) {
				DecimalFormat df = new DecimalFormat("#.0000");				
				xPositionField.setText("x: " + df.format(xPosition));
				yPositionField.setText("y: " + df.format(yPosition));				
			}
		});
		
		myGrid = new JGrid( myCanvas, gridType, gridColor, gridWidth, gridPosition, gridDelta );		
		
		myCrossLine = new JCrossLine( myCanvas, crossLinePosition, crossLineColor, crossLineWidthInPixel, crossLineLength, crossLinePainterPosition);
	
		myAxis = new JAxis(myCanvas, axisPosition, axisColor, axisWidthInPixel, painterPosition);
		
		myScale = new JScale(myCanvas, pixelPerCmX, unitX, startScaleX, pixelPerCmY, unitY, startScaleY, rate, minScale, maxScale);
		myScale.addScaleChangeListener(new ScaleChangeListener() {
			
			@Override
			public void getScale(Position scale) {
				DecimalFormat df = new DecimalFormat("#.00");
				xScaleField.setText( "xM=" + df.format(scale.getX() ) );
				yScaleField.setText( "yM=" + df.format(scale.getY() ) );
			}
		});
		
		//
		//Kirajzol eloterbe egy fuggvenyt
		//
		JButton commandButtonDrawFunction = new JButton("draw Function");
		commandButtonDrawFunction.addActionListener(new ActionListener(){

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

		//-------------------
		//
		//Grid ki/be kapcsolo
		//
		//-------------------
		gridXDeltaField = new JTextField();
		gridXDeltaField.setColumns( 8 );
		gridXDeltaField.setText( String.valueOf( myGrid.getDeltaGridX() ) );
		gridXDeltaField.setInputVerifier( new InputVerifier() {
			String goodValue =  String.valueOf( myGrid.getDeltaGridX() );
			
			@Override
			public boolean verify(JComponent input) {
				JTextField text = (JTextField)input;
	            String possibleValue = text.getText();
	            try{
	            	Double.valueOf(possibleValue);
	            	goodValue = possibleValue;
	            }catch(NumberFormatException e){
	            	text.setText(goodValue);
	            	return false;
	            }
	            myGrid.setDeltaGridX( Double.valueOf(goodValue));
	            myCanvas.refreshCoreCanvas();
	            return true;
			}
		});
		
		gridYDeltaField = new JTextField();
		gridYDeltaField.setColumns( 8 );
		gridYDeltaField.setText( String.valueOf( myGrid.getDeltaGridY() ) );
		gridYDeltaField.setInputVerifier( new InputVerifier() {
			String goodValue =  String.valueOf( myGrid.getDeltaGridY() );
			
			@Override
			public boolean verify(JComponent input) {
				JTextField text = (JTextField)input;
	            String possibleValue = text.getText();
	            try{
	            	Double.valueOf(possibleValue);
	            	goodValue = possibleValue;
	            }catch(NumberFormatException e){
	            	text.setText(goodValue);
	            	return false;
	            }
	            myGrid.setDeltaGridY( Double.valueOf(goodValue));
	            myCanvas.refreshCoreCanvas();
	            return true;
			}
		});
		
		String[] gridWidthElements = { "1", "3" };
		gridWidthCombo = new JComboBox<String>(gridWidthElements);
		gridWidthCombo.setSelectedIndex(0);
		gridWidthCombo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JComboBox<String> jcmbType = (JComboBox<String>) e.getSource();
				String cmbType = (String) jcmbType.getSelectedItem();
				
				if( cmbType.equals( "1")){					
					myGrid.setWidthInPixel(1);
				}else if( cmbType.equals( "3")){
					myGrid.setWidthInPixel(3);
				}
				myCanvas.refreshCoreCanvas();
			}
		});		
		
		String[] gridTypeElements = { "Solid", "Cross", "Dot" };
		gridTypeCombo = new JComboBox<String>(gridTypeElements);
		gridTypeCombo.setSelectedIndex(2);
		gridTypeCombo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JComboBox<String> jcmbType = (JComboBox<String>) e.getSource();
				String cmbType = (String) jcmbType.getSelectedItem();
				
				if( cmbType.equals( "Solid")){
					myGrid.setType( JGrid.Type.SOLID );
				}else if( cmbType.equals( "Cross")){
					myGrid.setType( JGrid.Type.CROSS );
				}else if( cmbType.equals( "Dot")){
					myGrid.setType( JGrid.Type.DOT );
				}
				myCanvas.refreshCoreCanvas();
			}
		});
		
		JCheckBox turnOnGrid = new JCheckBox("Turn On Grid");
		turnOnGrid.setSelected(true);
		turnOnGrid.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if( e.getStateChange() == ItemEvent.DESELECTED){
					myGrid.turnOff();
					gridTypeCombo.setEnabled(false);
					gridWidthCombo.setEnabled(false);
				}else{
					myGrid.turnOn();
					gridTypeCombo.setEnabled(true);
					gridWidthCombo.setEnabled(true);
				}
				myCanvas.repaint();
			}
		});
		
		
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridBagLayout());
		gridPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Grid", TitledBorder.LEFT, TitledBorder.TOP));
		GridBagConstraints gridPanelConstraints = new GridBagConstraints();
		
		//1. sor - Turn on grid
		gridPanelConstraints.gridx = 0;
		gridPanelConstraints.gridy = 0;
		gridPanelConstraints.gridwidth = 4;
		gridPanelConstraints.anchor = GridBagConstraints.WEST;
		gridPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridPanelConstraints.weightx = 1;
		gridPanel.add(turnOnGrid, gridPanelConstraints);
		
		//2. sor - Type
		gridPanelConstraints.gridx = 0;
		gridPanelConstraints.gridy++;
		gridPanelConstraints.gridwidth = 1;
		gridPanelConstraints.weightx = 0;
		gridPanel.add(new JLabel("     "),gridPanelConstraints );

		gridPanelConstraints.gridx = 1;
		gridPanelConstraints.gridwidth = 1;
		gridPanelConstraints.weightx = 0;
		gridPanel.add( new JLabel("Type: "), gridPanelConstraints);

		gridPanelConstraints.gridx = 2;
		gridPanelConstraints.gridwidth = 1;
		gridPanelConstraints.weightx = 1;
		gridPanel.add(gridTypeCombo, gridPanelConstraints);
		 
		//3. sor - Width
		gridPanelConstraints.gridx = 1;
		gridPanelConstraints.gridy++;
		gridPanelConstraints.gridwidth = 1;
		gridPanelConstraints.weightx = 0;
		gridPanel.add( new JLabel("Width: "), gridPanelConstraints);

		gridPanelConstraints.gridx = 2;
		gridPanelConstraints.gridwidth = 1;
		gridPanelConstraints.weightx = 1;
		gridPanel.add(gridWidthCombo, gridPanelConstraints);
		
		gridPanelConstraints.gridx = 3;
		gridPanelConstraints.gridwidth = 1;
		gridPanelConstraints.weightx = 0;
		gridPanel.add(new JLabel(" px"),gridPanelConstraints );
		
		//4. sor - Grid delta x
		gridPanelConstraints.gridx = 1;
		gridPanelConstraints.gridy++;
		gridPanelConstraints.gridwidth = 1;
		gridPanelConstraints.weightx = 0;
		gridPanel.add( new JLabel("Delta X: "), gridPanelConstraints);

		gridPanelConstraints.gridx = 2;
		gridPanelConstraints.gridwidth = 1;
		gridPanelConstraints.weightx = 1;
		gridPanel.add(gridXDeltaField, gridPanelConstraints);
				
		gridPanelConstraints.gridx = 3;
		gridPanelConstraints.gridwidth = 1;
		gridPanelConstraints.weightx = 0;
		gridPanel.add( new JLabel(" " + myScale.getUnitX().getSign() ), gridPanelConstraints );	
		
		//5. sor - Grid delta y
		gridPanelConstraints.gridx = 1;
		gridPanelConstraints.gridy++;
		gridPanelConstraints.gridwidth = 1;
		gridPanelConstraints.weightx = 0;
		gridPanel.add( new JLabel("Delta Y: "), gridPanelConstraints);

		gridPanelConstraints.gridx = 2;
		gridPanelConstraints.gridwidth = 1;
		gridPanelConstraints.weightx = 1;
		gridPanel.add(gridYDeltaField, gridPanelConstraints);
				
		gridPanelConstraints.gridx = 3;
		gridPanelConstraints.gridwidth = 1;
		gridPanelConstraints.weightx = 0;
		gridPanel.add( new JLabel(" " + myScale.getUnitY().getSign() ), gridPanelConstraints );	
		
		//--------------------
		//
		//Origo ki/be kapcsolo
		//
		//--------------------		
		crossLineXPosField = new JTextField();
		crossLineXPosField.setColumns( 8 );
		crossLineXPosField.setText( String.valueOf(myCrossLine.getPositionX() ) );
		crossLineXPosField.setInputVerifier( new InputVerifier() {
			String goodValue =  String.valueOf( myCrossLine.getPositionX() );
			
			@Override
			public boolean verify(JComponent input) {
				JTextField text = (JTextField)input;
	            String possibleValue = text.getText();
	            try{
	            	Double.valueOf(possibleValue);
	            	goodValue = possibleValue;
	            }catch(NumberFormatException e){
	            	text.setText(goodValue);
	            	return false;
	            }
	            myCrossLine.setPositionX( Double.valueOf(goodValue));
	            myCanvas.refreshCoreCanvas();
	            return true;
			}
		});
	
		crossLineYPosField = new JTextField();
		crossLineYPosField.setColumns( 8 );
		crossLineYPosField.setText( String.valueOf(myCrossLine.getPositionY() ) );
		crossLineYPosField.setInputVerifier( new InputVerifier() {
			String goodValue =  String.valueOf( myCrossLine.getPositionY() );
			
			@Override
			public boolean verify(JComponent input) {
				JTextField text = (JTextField)input;
	            String possibleValue = text.getText();
	            try{
	            	Double.valueOf(possibleValue);
	            	goodValue = possibleValue;
	            }catch(NumberFormatException e){
	            	text.setText(goodValue);
	            	return false;
	            }
	            myCrossLine.setPositionY( Double.valueOf(goodValue));
	            myCanvas.refreshCoreCanvas();
	            return true;
			}
		});
		
		crossLineXLengthField = new JTextField();
		crossLineXLengthField.setColumns( 8 );
		crossLineXLengthField.setText( String.valueOf( myCrossLine.getLengthX() ) );
		crossLineXLengthField.setInputVerifier( new InputVerifier() {
			String goodValue =  String.valueOf( myCrossLine.getLengthX() );
			
			@Override
			public boolean verify(JComponent input) {
				JTextField text = (JTextField)input;
	            String possibleValue = text.getText();
	            try{
	            	Double.valueOf(possibleValue);
	            	goodValue = possibleValue;
	            }catch(NumberFormatException e){
	            	text.setText(goodValue);
	            	return false;
	            }
	            myCrossLine.setLengthX( Double.valueOf( goodValue ) );
	            myCanvas.refreshCoreCanvas();
	            return true;
			}
		});
	
		crossLineYLengthField = new JTextField();
		crossLineYLengthField.setColumns( 8 );
		crossLineYLengthField.setText( String.valueOf(myCrossLine.getLengthY()) );
		crossLineYLengthField.setInputVerifier( new InputVerifier() {
			String goodValue =  String.valueOf( myCrossLine.getLengthY() );
			
			@Override
			public boolean verify(JComponent input) {
				JTextField text = (JTextField)input;
	            String possibleValue = text.getText();
	            try{
	            	Double.valueOf(possibleValue);
	            	goodValue = possibleValue;
	            }catch(NumberFormatException e){
	            	text.setText(goodValue);
	            	return false;
	            }
	            myCrossLine.setLengthY( Double.valueOf( goodValue ) );
	            myCanvas.refreshCoreCanvas();
	            return true;
			}
		});
		

		String[] crossLineWidthElements = { "1", "3", "5" };
		crossLineWidthCombo = new JComboBox<String>(crossLineWidthElements);
		crossLineWidthCombo.setSelectedIndex(2);
		crossLineWidthCombo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JComboBox<String> jcmbType = (JComboBox<String>) e.getSource();
				String cmbType = (String) jcmbType.getSelectedItem();
				
				if( cmbType.equals( "1")){					
					myCrossLine.setWidthInPixel(1);
				}else if( cmbType.equals( "3")){
					myCrossLine.setWidthInPixel(3);
				}else if( cmbType.equals( "5")){
					myCrossLine.setWidthInPixel(5);
				}
				myCanvas.refreshCoreCanvas();
			}
		});		
		
		JCheckBox turnOnCrossLine = new JCheckBox("Turn On Crossline");
		turnOnCrossLine.setSelected(true);
		turnOnCrossLine.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if( e.getStateChange() == ItemEvent.DESELECTED){
					myCrossLine.turnOff();
					crossLineWidthCombo.setEnabled(false);
					crossLineXPosField.setEnabled(false);
					crossLineYPosField.setEnabled(false);
					crossLineXLengthField.setEnabled(false);
					crossLineYLengthField.setEnabled(false);
				}else{
					myCrossLine.turnOn();
					crossLineWidthCombo.setEnabled(true);
					crossLineXPosField.setEnabled(true);
					crossLineYPosField.setEnabled(true);
					crossLineXLengthField.setEnabled(true);
					crossLineYLengthField.setEnabled(true);
				}
				myCanvas.repaint();
			}
		});
		
		JColorChooser crossLineColor = new JColorChooser();
		
		JPanel crossLinePanel = new JPanel();
		crossLinePanel.setLayout(new GridBagLayout());
		crossLinePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Cross line", TitledBorder.LEFT, TitledBorder.TOP));
		GridBagConstraints crossLinePanelConstraints = new GridBagConstraints();
		
		//1. sor - Turn on CrossLine
		crossLinePanelConstraints.gridx = 0;
		crossLinePanelConstraints.gridy = 0;
		crossLinePanelConstraints.gridwidth = 4;
		crossLinePanelConstraints.anchor = GridBagConstraints.WEST;
		crossLinePanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		crossLinePanelConstraints.weightx = 1;
		crossLinePanelConstraints.anchor = GridBagConstraints.WEST;
		crossLinePanel.add(turnOnCrossLine, crossLinePanelConstraints);
		
		//2. sor - Position X
		crossLinePanelConstraints.gridx = 1;
		crossLinePanelConstraints.gridy++;
		crossLinePanelConstraints.gridwidth = 1;
		crossLinePanelConstraints.weightx = 0;
		crossLinePanel.add( new JLabel("Position X: "), crossLinePanelConstraints);

		crossLinePanelConstraints.gridx = 2;
		crossLinePanelConstraints.gridwidth = 1;
		crossLinePanelConstraints.weightx = 1;
		crossLinePanel.add(crossLineXPosField, crossLinePanelConstraints);	
		
		crossLinePanelConstraints.gridx = 3;
		crossLinePanelConstraints.gridwidth = 1;
		crossLinePanelConstraints.weightx = 0;
		crossLinePanel.add( new JLabel(" " + myScale.getUnitX().getSign() ), crossLinePanelConstraints );

		//3. sor - Position Y
		crossLinePanelConstraints.gridx = 1;
		crossLinePanelConstraints.gridy++;
		crossLinePanelConstraints.gridwidth = 1;
		crossLinePanelConstraints.weightx = 0;
		crossLinePanel.add( new JLabel("Position Y: "), crossLinePanelConstraints);

		crossLinePanelConstraints.gridx = 2;
		crossLinePanelConstraints.gridwidth = 1;
		crossLinePanelConstraints.weightx = 1;
		crossLinePanel.add(crossLineYPosField, crossLinePanelConstraints);	
		
		crossLinePanelConstraints.gridx = 3;
		crossLinePanelConstraints.gridwidth = 1;
		crossLinePanelConstraints.weightx = 0;
		crossLinePanel.add( new JLabel(" " + myScale.getUnitY().getSign() ), crossLinePanelConstraints);
		
		//4. sor - Width
		crossLinePanelConstraints.gridx = 0;
		crossLinePanelConstraints.gridy++;
		crossLinePanelConstraints.gridwidth = 1;
		crossLinePanelConstraints.weightx = 0;
		crossLinePanel.add(new JLabel("     "),crossLinePanelConstraints );
		
		crossLinePanelConstraints.gridx = 1;
		crossLinePanelConstraints.gridwidth = 1;
		crossLinePanelConstraints.weightx = 0;
		crossLinePanel.add( new JLabel("Width: "), crossLinePanelConstraints);

		crossLinePanelConstraints.gridx = 2;
		crossLinePanelConstraints.gridwidth = 1;
		crossLinePanelConstraints.weightx = 1;
		crossLinePanel.add(crossLineWidthCombo, crossLinePanelConstraints);
		
		crossLinePanelConstraints.gridx = 3;
		crossLinePanelConstraints.gridwidth = 1;
		crossLinePanelConstraints.weightx = 0;
		crossLinePanel.add( new JLabel(" px"), crossLinePanelConstraints);
		
		//5. sor - Length X
		crossLinePanelConstraints.gridx = 1;
		crossLinePanelConstraints.gridy++;
		crossLinePanelConstraints.gridwidth = 1;
		crossLinePanelConstraints.weightx = 0;
		crossLinePanel.add( new JLabel("Length X: "), crossLinePanelConstraints);

		crossLinePanelConstraints.gridx = 2;
		crossLinePanelConstraints.gridwidth = 1;
		crossLinePanelConstraints.weightx = 1;
		crossLinePanel.add(crossLineXLengthField, crossLinePanelConstraints);
		
		crossLinePanelConstraints.gridx = 3;
		crossLinePanelConstraints.gridwidth = 1;
		crossLinePanelConstraints.weightx = 0;
		crossLinePanel.add( new JLabel(" " + myScale.getUnitX().getSign() ), crossLinePanelConstraints);
		
		//6. sor - Length Y
		crossLinePanelConstraints.gridx = 1;
		crossLinePanelConstraints.gridy++;
		crossLinePanelConstraints.gridwidth = 1;
		crossLinePanelConstraints.weightx = 0;
		crossLinePanel.add( new JLabel("Length Y: "), crossLinePanelConstraints);

		crossLinePanelConstraints.gridx = 2;
		crossLinePanelConstraints.gridwidth = 1;
		crossLinePanelConstraints.weightx = 1;
		crossLinePanel.add(crossLineYLengthField, crossLinePanelConstraints);
		
		crossLinePanelConstraints.gridx = 3;
		crossLinePanelConstraints.gridwidth = 1;
		crossLinePanelConstraints.weightx = 0;
		crossLinePanel.add( new JLabel(" " + myScale.getUnitY().getSign() ), crossLinePanelConstraints);
		
		
		//----
		//
		//Axis
		//
		//----
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
		
		JPanel axisPanel = new JPanel();
		axisPanel.setLayout(new GridBagLayout());
		axisPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Axis", TitledBorder.LEFT, TitledBorder.TOP));
		GridBagConstraints axisPanelConstraints = new GridBagConstraints();
		
		axisPanelConstraints.gridx = 0;
		axisPanelConstraints.gridy = 0;
		axisPanelConstraints.gridwidth = 2;
		axisPanelConstraints.anchor = GridBagConstraints.WEST;
		axisPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		axisPanelConstraints.weightx = 1;
		axisPanelConstraints.anchor = GridBagConstraints.WEST;				
		axisPanel.add(turnOnAxis, axisPanelConstraints);
		
		axisPanelConstraints.gridx = 0;
		axisPanelConstraints.gridy = 1;
		axisPanelConstraints.gridwidth = 1;
		axisPanelConstraints.weightx = 0;
		axisPanel.add(new JLabel("     "), axisPanelConstraints );
		
		axisPanelConstraints.gridx = 1;
		axisPanelConstraints.gridy = 1;
		axisPanelConstraints.gridwidth = 1;
		axisPanel.add(lbAxisSelector, axisPanelConstraints );
		
		axisPanelConstraints.gridx = 1;
		axisPanelConstraints.gridy = 2;
		axisPanelConstraints.gridwidth = 1;
		axisPanel.add(rbAxisSelector, axisPanelConstraints );
		
		axisPanelConstraints.gridx = 1;
		axisPanelConstraints.gridy = 3;
		axisPanelConstraints.gridwidth = 1;
		axisPanel.add(ltAxisSelector, axisPanelConstraints );
		
		axisPanelConstraints.gridx = 1;
		axisPanelConstraints.gridy = 4;
		axisPanelConstraints.gridwidth = 1;
		axisPanel.add(rtAxisSelector, axisPanelConstraints );
		
		axisPanelConstraints.gridx = 1;
		axisPanelConstraints.gridy = 5;
		axisPanelConstraints.gridwidth = 1;
		axisPanel.add(zzAxisSelector, axisPanelConstraints );
		
		//----------------
		//
		// Iranyito gombok
		//
		//----------------
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
		
		JPanel moverPanel = new JPanel();
		moverPanel.setLayout(new GridBagLayout());
		moverPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Move", TitledBorder.LEFT, TitledBorder.TOP));
		GridBagConstraints moverPanelConstraints = new GridBagConstraints();
		
		moverPanelConstraints.gridx = 1;
		moverPanelConstraints.gridy = 0;
		moverPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		moverPanelConstraints.anchor = GridBagConstraints.WEST;				
		moverPanel.add(upButton, moverPanelConstraints);
		
		moverPanelConstraints.gridx = 0;
		moverPanelConstraints.gridy = 1;
		moverPanelConstraints.anchor = GridBagConstraints.WEST;				
		moverPanel.add(leftButton, moverPanelConstraints);
		
		moverPanelConstraints.gridx = 2;
		moverPanelConstraints.gridy = 1;
		moverPanelConstraints.anchor = GridBagConstraints.WEST;				
		moverPanel.add(rightButton, moverPanelConstraints);
		
		moverPanelConstraints.gridx = 1;
		moverPanelConstraints.gridy = 3;
		moverPanelConstraints.anchor = GridBagConstraints.WEST;				
		moverPanel.add(downButton, moverPanelConstraints);
		
	
		
	

		
		
		
		
		
		//-------------------------
		//
		// Jobboldali vezerlo panel
		//
		//-------------------------
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new GridBagLayout());
		GridBagConstraints eastPanelConstraints = new GridBagConstraints();
		
		eastPanelConstraints.gridx = 0;
		eastPanelConstraints.gridy = 0;
		eastPanelConstraints.anchor = GridBagConstraints.NORTH;
		eastPanelConstraints.weighty = 0;
		eastPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		eastPanel.add(gridPanel, eastPanelConstraints);
		
		eastPanelConstraints.weighty = 0;
		eastPanelConstraints.gridy++;
		eastPanel.add(crossLinePanel, eastPanelConstraints);
		
		eastPanelConstraints.weighty = 0;
		eastPanelConstraints.gridy++;
		eastPanel.add(axisPanel, eastPanelConstraints);

		eastPanelConstraints.weighty = 0;
		eastPanelConstraints.gridy++;
		eastPanel.add(moverPanel, eastPanelConstraints);

		
		
		eastPanelConstraints.weighty = 1;
		eastPanelConstraints.gridy++;
		eastPanel.add(new JLabel(), eastPanelConstraints);
		
		

		
		
		//----------------------------------------------------
		//
		// Legalso panel parncsgombokkal es a pozicio jelzovel
		//
		//----------------------------------------------------
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS	));
		
		//Parancsgomb panel
		JPanel commandButtonPanel = new JPanel();
		commandButtonPanel.setLayout( new FlowLayout(FlowLayout.LEFT));
		commandButtonPanel.add(commandButtonDrawFunction);
		
		//Kijelzo panel
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout( new FlowLayout( FlowLayout.LEFT ));
		xScaleField = new JTextField("xM=");
		xScaleField.setColumns(10);
		xScaleField.setBorder(BorderFactory.createLoweredBevelBorder());
		xScaleField.setEditable(false);
		yScaleField = new JTextField("yM=");
		yScaleField.setColumns(10);
		yScaleField.setBorder(BorderFactory.createLoweredBevelBorder());
		yScaleField.setEditable(false);
		xPositionField = new JTextField("x:");
		xPositionField.setColumns(8);
		xPositionField.setBorder(BorderFactory.createLoweredBevelBorder());
		xPositionField.setEditable(false);
		yPositionField = new JTextField("y:");
		yPositionField.setColumns(8);
		yPositionField.setBorder(BorderFactory.createLoweredBevelBorder());
		yPositionField.setEditable(false);
		
		statusPanel.add(xScaleField);
		statusPanel.add(yScaleField);
		statusPanel.add(xPositionField);
		statusPanel.add(yPositionField);
		
		southPanel.add(commandButtonPanel);
		southPanel.add(statusPanel);
	
		this.getContentPane().setLayout(new BorderLayout(10,10));
		this.getContentPane().add(myCanvas, BorderLayout.CENTER);
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		this.getContentPane().add(eastPanel, BorderLayout.EAST);

		//Kezdo ertekek kiirasa
		DecimalFormat df = new DecimalFormat("#.00");
		xScaleField.setText("xM=" + df.format( startScaleX ));
		yScaleField.setText("yM=" + df.format( startScaleY ));
		
		this.setVisible(true);
		
	}
}


