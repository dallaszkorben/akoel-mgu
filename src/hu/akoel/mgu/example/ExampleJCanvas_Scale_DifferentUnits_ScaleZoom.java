package hu.akoel.mgu.example;


import hu.akoel.mgu.CanvasControl;
import hu.akoel.mgu.MGraphics;
import hu.akoel.mgu.MCanvas;
import hu.akoel.mgu.PainterListener;
import hu.akoel.mgu.PositionChangeListener;
import hu.akoel.mgu.PossiblePixelPerUnits;
import hu.akoel.mgu.axis.Axis;
import hu.akoel.mgu.crossline.CrossLine;
import hu.akoel.mgu.grid.Grid;
import hu.akoel.mgu.scale.Scale;
import hu.akoel.mgu.scale.ScaleChangeListener;
import hu.akoel.mgu.scale.values.PixelPerCmValue;
import hu.akoel.mgu.scale.values.ScaleValue;
import hu.akoel.mgu.scale.values.UnitValue;
import hu.akoel.mgu.values.DeltaValue;
import hu.akoel.mgu.values.LengthValue;
import hu.akoel.mgu.values.PixelPerUnitValue;
import hu.akoel.mgu.values.PositionValue;
import hu.akoel.mgu.values.RateValue;
import hu.akoel.mgu.values.SizeValue;
import hu.akoel.mgu.values.TranslateValue;
import hu.akoel.mgu.values.Value2D;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ExampleJCanvas_Scale_DifferentUnits_ScaleZoom extends JFrame {

	private static final long serialVersionUID = 5810956401235486862L;

	private MCanvas myCanvas;
	private SizeValue worldSize = new SizeValue(-10.0, -10.0, 10.0, 30);	
	private SizeValue boundSize = new SizeValue(0.0, 0.0, 40.0, 40);	
	private Color background = Color.black;
	private TranslateValue positionToMiddle = null;//new TranslateValue( 10, 10);
	private PossiblePixelPerUnits possiblePixelPerUnits = new PossiblePixelPerUnits(new PixelPerUnitValue(1,1));
	//private PossiblePixelPerUnits possiblePixelPerUnits = new PossiblePixelPerUnits(new Position(1,1), new Position(1.2, 1.2), new Position(1,1), new Position(15,15));
	//private Position pixelPerUnit = new Position(1,1);

	private Grid myGrid;	
	private Color gridColor = Color.green;
	private int gridWidth = 1;
	private DeltaValue gridDelta = new DeltaValue(1.0, 1.0);
	private Grid.PainterPosition gridPosition = Grid.PainterPosition.DEEPEST; 
	private Grid.Type gridType = Grid.Type.DOT;
	
	private CrossLine myCrossLine;
	private PositionValue crossLinePosition = new PositionValue( 5, 5 );
	private Color crossLineColor = Color.red;
	private int crossLineWidthInPixel = 5;
	private LengthValue crossLineLength = new LengthValue( 1, 1 );
	private CrossLine.PainterPosition crossLinePainterPosition = CrossLine.PainterPosition.DEEPEST;
	
	private Axis myAxis;
	private Color axisColor = Color.yellow;
	private int axisWidthInPixel = 1;
	private Axis.AxisPosition axisPosition = Axis.AxisPosition.AT_LEFT_BOTTOM;
	private Axis.PainterPosition painterPosition = Axis.PainterPosition.HIGHEST;
		
	private Scale myScale;
	private PixelPerCmValue pixelPerCm = new PixelPerCmValue(42.1, 42.1);
	private UnitValue unit = new UnitValue(Scale.UNIT.km, Scale.UNIT.m ); 
	private ScaleValue startScale = new ScaleValue( 100000, 100 );
	private RateValue rate = new RateValue(1.2, 1.2);
	private ScaleValue minScale = new ScaleValue( 2000, 2);
	private ScaleValue maxScale = new ScaleValue( 600000, 600);
	
	private CanvasControl canvasControl;
	
	public static void main(String[] args) {		
		new ExampleJCanvas_Scale_DifferentUnits_ScaleZoom();
	}

	public ExampleJCanvas_Scale_DifferentUnits_ScaleZoom() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Example :: JCanvas with JGrid, JCrossLine, JAxis, Scale :: Different units");
		this.setUndecorated(false);
		this.setSize(700, 700);
		this.createBufferStrategy(1);

//		myCanvas = new JCanvas(BorderFactory.createLoweredBevelBorder(), background, worldSize );
//		myCanvas = new JCanvas(BorderFactory.createLoweredBevelBorder(), background, possiblePixelPerUnits, positionToMiddle, boundSize);
		myCanvas = new MCanvas(BorderFactory.createLoweredBevelBorder(), background, possiblePixelPerUnits, positionToMiddle);
		myCanvas.addPositionChangeListener(new PositionChangeListener() {
			
			@Override
			public void getWorldPosition(double xPosition, double yPosition) {
				DecimalFormat df = new DecimalFormat("#.0000");				
				canvasControl.setStatusPanelXPosition( "x: " + df.format(xPosition));
				canvasControl.setStatusPanelYPosition( "y: " + df.format(yPosition));				
			}
		});
		
		myGrid = new Grid( myCanvas, gridType, gridColor, gridWidth, gridPosition, gridDelta );		
		
		myCrossLine = new CrossLine( myCanvas, crossLinePosition, crossLineColor, crossLineWidthInPixel, crossLineLength, crossLinePainterPosition);
	
		myAxis = new Axis(myCanvas, axisPosition, axisColor, axisWidthInPixel, painterPosition);
		
		myScale = new Scale(myCanvas, pixelPerCm, unit, startScale, rate, minScale, maxScale);
		myScale.addScaleChangeListener(new ScaleChangeListener() {
			
			@Override
			public void getScale(Value2D scale) {
				DecimalFormat df = new DecimalFormat("#.00");
				
				if( scale.getX() < 1.0 ){
					canvasControl.setStatusPanelXScale( "xM=" + df.format(1/scale.getX() ) + ":1" );
				}else{
					canvasControl.setStatusPanelXScale( "xM=1:" + df.format(scale.getX() ) );
				}

				if( scale.getY() < 1.0 ){
					canvasControl.setStatusPanelYScale( "yM=" + df.format(1/scale.getY() ) + ":1" );
				}else{				
					canvasControl.setStatusPanelYScale( "yM=1:" + df.format(scale.getY() ) );
				}
			}
		});
		
		canvasControl = new CanvasControl( myCanvas, myCrossLine, myGrid, myAxis, myScale );
		
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
					public void paintByWorldPosition(MCanvas canvas, MGraphics g2) {					
						g2.setColor(new Color(250, 200, 0));
						g2.setStroke(new BasicStroke(3));
						
						PositionValue previous = null;
						double increment = canvas.getWorldXLengthByPixel(2);
						double start = canvas.getWorldXByPixel(0);
						double stop = canvas.getWorldXByPixel(canvas.getViewableSize().width );
						for( double x=start; x<=stop; x+=increment ){
							double y = 0.01*(x*x*x) - 0.07*(x*x) + 0.1*(x) - 0;
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
		
		//Parancsgomb panel
		JPanel commandButtonPanel = new JPanel();
		commandButtonPanel.setLayout( new FlowLayout(FlowLayout.LEFT));
		commandButtonPanel.add(commandButtonDrawFunction);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS	));
		
		southPanel.add(commandButtonPanel);
		southPanel.add( canvasControl.getStatusPanel() );
	
		this.getContentPane().setLayout(new BorderLayout(10,10));
		this.getContentPane().add(myCanvas, BorderLayout.CENTER);
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		this.getContentPane().add(canvasControl.getControlPanel(), BorderLayout.EAST);

		//Kezdo ertekek kiirasa
		DecimalFormat df = new DecimalFormat("#.00");
		if( myScale.getScale().getX() < 1.0 ){
			canvasControl.setStatusPanelXScale( "xM=" + df.format(1/myScale.getScale().getX() ) + ":1" );
		}else{
			canvasControl.setStatusPanelXScale( "xM=1:" + df.format(myScale.getScale().getX() ) );
		}

		if( myScale.getScale().getY() < 1.0 ){
			canvasControl.setStatusPanelYScale( "yM=" + df.format(1/myScale.getScale().getY() ) + ":1" );
		}else{				
			canvasControl.setStatusPanelYScale( "yM=1:" + df.format(myScale.getScale().getY() ) );
		}
		
		this.setVisible(true);
		
	}
}


