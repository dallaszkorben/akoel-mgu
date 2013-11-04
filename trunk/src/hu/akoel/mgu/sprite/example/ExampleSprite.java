package hu.akoel.mgu.sprite.example;


import hu.akoel.mgu.PositionChangeListener;
import hu.akoel.mgu.PossiblePixelPerUnits;
import hu.akoel.mgu.axis.Axis;
import hu.akoel.mgu.crossline.CrossLine;
import hu.akoel.mgu.example.CanvasControl;
import hu.akoel.mgu.grid.Grid;
import hu.akoel.mgu.scale.Scale;
import hu.akoel.mgu.scale.ScaleChangeListener;
import hu.akoel.mgu.sprite.Magnet;
import hu.akoel.mgu.sprite.MagnetType;
import hu.akoel.mgu.sprite.RectangleElement;
import hu.akoel.mgu.sprite.Sprite;
import hu.akoel.mgu.sprite.SpriteCanvas;
import hu.akoel.mgu.values.DeltaValue;
import hu.akoel.mgu.values.LengthValue;
import hu.akoel.mgu.values.PixelPerUnitValue;
import hu.akoel.mgu.values.PositionValue;
import hu.akoel.mgu.values.RangeValueInPixel;
import hu.akoel.mgu.values.SizeValue;
import hu.akoel.mgu.values.TranslateValue;
import hu.akoel.mgu.values.Value;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ExampleSprite extends JFrame {

	private static final long serialVersionUID = 5810956401235486862L;

	private SpriteCanvas myCanvas;
	//private SizeValue worldSize = new SizeValue(-10.0, -10.0, 10.0, 30);	
	//private SizeValue boundSize = new SizeValue(0.0, 0.0, 400.0, 400);	
	private Color background = Color.black;
	private TranslateValue positionToMiddle = new TranslateValue( 0, 0);
	private PossiblePixelPerUnits possiblePixelPerUnits = new PossiblePixelPerUnits(new PixelPerUnitValue(10,10));

	private Grid myGrid;	
	private Color gridColor = Color.green;
	private int gridWidth = 1;
	private DeltaValue gridDelta = new DeltaValue(1.0, 1.0);
	private Grid.PainterPosition gridPosition = Grid.PainterPosition.DEEPEST; 
	private Grid.Type gridType = Grid.Type.DOT;

	private CrossLine myCrossLine;
	private PositionValue crossLinePosition = new PositionValue( 0, 0 );
	private Color crossLineColor = Color.red;
	private int crossLineWidthInPixel = 1;
	private Value crossLineLength = new LengthValue( 1, 1 );
	private CrossLine.PainterPosition crossLinePainterPosition = CrossLine.PainterPosition.DEEPEST;
	
	private Axis myAxis;
	private Color axisColor = Color.yellow;
	private int axisWidthInPixel = 1;
	private Axis.AxisPosition axisPosition = Axis.AxisPosition.AT_LEFT_BOTTOM;
	private Axis.PainterPosition painterPosition = Axis.PainterPosition.HIGHEST;
		
	private Scale myScale;
	private double pixelPerCm = 42.1;
	private Scale.UNIT unit = Scale.UNIT.m;
	private double startScale = 100;//38.426;//100;
	private double rate = 1.2;
	private double minScale = 0.5;
	private double maxScale =  600;
	
	private CanvasControl canvasControl;
	
	private Sprite baseSprite;
	private Sprite pipeToBaseSprite;
	private Sprite pipeSprite;
	
	public static void main(String[] args) {		
		new ExampleSprite();
	}

	public ExampleSprite() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Example :: Sprite");
		this.setUndecorated(false);
		this.setSize(700, 700);
		this.createBufferStrategy(1);

		myCanvas = new SpriteCanvas(BorderFactory.createLoweredBevelBorder(), background, possiblePixelPerUnits, positionToMiddle);
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
			public void getScale(Value scale) {
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
			}
		});
		
		canvasControl = new CanvasControl( myCanvas, myCrossLine, myGrid, myAxis, myScale );

		//
		//Elhelyezi az eloterbe a Sprite-okat
		//
		JButton commandButtonAddSprite = new JButton("add new Sprite");
		commandButtonAddSprite.addActionListener(new ActionListener(){

			//Magnet tipus
			MagnetType pipeMagnet = new MagnetType("Pipe");
			MagnetType outMagnet = new MagnetType("Out");
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
	
				baseSprite = new Sprite(new SizeValue(-1, -1.5, 1, 1.5));
				
			
				//A Sprite leirasa
				RectangleElement rectBase11 = new RectangleElement(-1,-1.5,2,3,Color.blue, new BasicStroke(1f), Color.red, new BasicStroke(3), Color.cyan, new BasicStroke(3));
				RectangleElement rectBase12 = new RectangleElement(0.95,-0.05,0.05,0.1,Color.blue, new BasicStroke(1), Color.red, new BasicStroke(3), Color.yellow, new BasicStroke(3));

				Magnet baseSpriteMagnetEast = new Magnet(baseSprite, outMagnet, 90.0, new RangeValueInPixel(20, 10 ), new PositionValue(1, 0) );
				baseSpriteMagnetEast.addPossibleMagnetTypToConnect( pipeMagnet  );
				baseSpriteMagnetEast.addElement( rectBase12 );
				
				
				baseSprite.addElement(rectBase11);
				//baseSprite.addElement(rectBase12);
				baseSprite.setPosition( 0, 0 );
				
				//A Sprite magneseinek hozzarendelese
				baseSprite.addMagnet( baseSpriteMagnetEast );
				
				//A Sprite elhelyezese a Canvas-on
				myCanvas.addSprite(baseSprite);	
				
				
				//Kozvetlen cso
				//Nem helyezheto le onmagaban
				pipeToBaseSprite = new Sprite(new SizeValue(-2, -0.25, 2, 0.25), false);
				
				//A Sprite leirasa
				RectangleElement rectPipeToBase11 = new RectangleElement(-2,-0.125,4,0.25,Color.blue, new BasicStroke(1f), Color.red, new BasicStroke(3), Color.magenta, new BasicStroke(3));
				RectangleElement rectPipeToBase12 = new RectangleElement(-2,-0.05,0.05,0.1,Color.green, new BasicStroke(1f), Color.red, new BasicStroke(3), Color.yellow, new BasicStroke(3));
				RectangleElement rectPipeToBase13 = new RectangleElement(1.95,-0.05,0.05,0.1,Color.green, new BasicStroke(1f), Color.red, new BasicStroke(3), Color.yellow, new BasicStroke(3));

				Magnet pipeToBaseSpriteMagnetEast = new Magnet(pipeToBaseSprite, pipeMagnet, 90.0, new RangeValueInPixel(20, 10 ), new PositionValue(2, 0) );
				pipeToBaseSpriteMagnetEast.addPossibleMagnetTypToConnect( pipeMagnet  );
				pipeToBaseSpriteMagnetEast.addElement( rectPipeToBase13 );
	
				Magnet pipeToBaseSpriteMagnetWest = new Magnet(pipeToBaseSprite, pipeMagnet, 270.0, new RangeValueInPixel(20, 10 ), new PositionValue(-2, 0) );
				pipeToBaseSpriteMagnetWest.addPossibleMagnetTypToConnect( pipeMagnet );
				pipeToBaseSpriteMagnetWest.addPossibleMagnetTypToConnect( outMagnet );
				pipeToBaseSpriteMagnetWest.addElement( rectPipeToBase12 );
				
				//A Sprite magneseinek hozzarendelese
				pipeToBaseSprite.addMagnet( pipeToBaseSpriteMagnetEast );
				pipeToBaseSprite.addMagnet( pipeToBaseSpriteMagnetWest );
				
				pipeToBaseSprite.addElement(rectPipeToBase11);
//				pipeToBaseSprite.addElement(rectPipeToBase12);
//				pipeToBaseSprite.addElement(rectPipeToBase13);
				pipeToBaseSprite.setPosition( 0, 0 );
				
				pipeToBaseSpriteMagnetWest.setConnectedTo(baseSpriteMagnetEast);
				
				//A Sprite elhelyezese a Canvas-on
				myCanvas.addSprite(pipeToBaseSprite);	
				
				
				//Normal cso
				//Nem helyezheto le onmagaban
				pipeSprite = new Sprite(new SizeValue(-2, -0.25, 2, 0.25), false);
				
				//A Sprite leirasa
				RectangleElement rectPipe11 = new RectangleElement(-2,-0.125,4,0.25,Color.green, new BasicStroke(1f), Color.red, new BasicStroke(3), Color.orange, new BasicStroke(3));
				RectangleElement rectPipe12 = new RectangleElement(-2,-0.05,0.05,0.1,Color.green, new BasicStroke(1f), Color.red, new BasicStroke(3), Color.yellow, new BasicStroke(3));
				RectangleElement rectPipe13 = new RectangleElement(1.95,-0.05,0.05,0.1,Color.green, new BasicStroke(1f), Color.red, new BasicStroke(3), Color.yellow, new BasicStroke(3));

				Magnet pipeSpriteMagnetEast = new Magnet(pipeSprite, pipeMagnet, 90.0, new RangeValueInPixel(20, 10 ), new PositionValue(2, 0) );
				pipeSpriteMagnetEast.addPossibleMagnetTypToConnect( pipeMagnet  );
				pipeSpriteMagnetEast.addElement(rectPipe13);
				
				Magnet pipeSpriteMagnetWest = new Magnet(pipeSprite, pipeMagnet, 270.0, new RangeValueInPixel(20, 10 ), new PositionValue(-2, 0) );
				pipeSpriteMagnetWest.addPossibleMagnetTypToConnect( pipeMagnet );
				pipeSpriteMagnetWest.addElement(rectPipe12);

				//A Sprite magneseinek hozzarendelese
				pipeSprite.addMagnet( pipeSpriteMagnetEast );
				pipeSprite.addMagnet( pipeSpriteMagnetWest );
				
				pipeSprite.addElement(rectPipe11);
//				pipeSprite.addElement(rectPipe12);
//				pipeSprite.addElement(rectPipe13);
				pipeSprite.setPosition( 0, 0 );
				
				pipeSpriteMagnetWest.setConnectedTo(pipeToBaseSpriteMagnetEast);
				
				//A Sprite elhelyezese a Canvas-on
				myCanvas.addSprite(pipeSprite);	
				
				
				
				
				
/*				
				
				
				//Nem helyezheto le onmagaban
				sprite2 = new Sprite(new SizeValue(-1, -1, 1, 1), false);
				
				Magnet magnetNorth2 = new Magnet(sprite2, pipeMagnet, 0.0, new RangeValueInPixel(10, 20), new PositionValue(0, 1) );
				magnetNorth2.addPossibleMagnetTypToConnect( pipeMagnet );
				
				Magnet magnetEast2 = new Magnet(sprite2, pipeMagnet, 90.0, new RangeValueInPixel(20, 10 ), new PositionValue(1, 0) );
				magnetEast2.addPossibleMagnetTypToConnect( pipeMagnet  );
				
				Magnet magnetSouth2 = new Magnet(sprite2, pipeMagnet, 180.0, new RangeValueInPixel(10, 20 ), new PositionValue(0, -1) );
				magnetSouth2.addPossibleMagnetTypToConnect( pipeMagnet );
				
				Magnet magnetWest2 = new Magnet(sprite2, pipeMagnet, 270.0, new RangeValueInPixel(20, 10 ), new PositionValue(-1, 0) );
				magnetWest2.addPossibleMagnetTypToConnect( pipeMagnet );
				
				//A Sprite magneseinek hozzarendelese
				sprite2.addMagnet( magnetNorth2 );
				sprite2.addMagnet( magnetEast2 );
				sprite2.addMagnet( magnetSouth2 );
				sprite2.addMagnet( magnetWest2 );
				
				//A Sprite leirasa
				RectangleElement rect211 = new RectangleElement(-1,-1,2,2,Color.green, new BasicStroke(1f), Color.red, new BasicStroke(1));
				RectangleElement rect212 = new RectangleElement(-0.5,-0.5,1,1,Color.green, new BasicStroke(9f), Color.red, new BasicStroke(1));
				sprite2.addElement(rect211);
				sprite2.addElement(rect212);
				sprite2.setPosition( 0, 0 );
				
				magnetWest2.setConnectedTo(baseSpriteMagnetEast);
				
				//A Sprite elhelyezese a Canvas-on
				myCanvas.addSprite(sprite2);	
	*/			
				
				
				
				
				
				
				
				
				//A Canvas ujrarajzolasa, az uj Sprite megjelenites miatt
				myCanvas.revalidateAndRepaintCoreCanvas();
				
			}
			
		});

		//Report button
		JButton commandButtonReport = new JButton("Report");
		commandButtonReport.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
			
				
				for( Magnet magnet: baseSprite.getMagnetList() ){
					System.err.println("Sprite: " + magnet.getDirection() + ", " + magnet.getConnectedTo() );
				}
				
				System.err.println();
				

				for( Magnet magnet: pipeToBaseSprite.getMagnetList() ){
					System.err.println("Pipe to Base sprite: " + magnet.getDirection() + ", " + magnet.getConnectedTo() );
				}
				

				System.err.println();
				

				for( Magnet magnet: pipeSprite.getMagnetList() ){
					System.err.println("Pipe: " + magnet.getDirection() + ", " + magnet.getConnectedTo() );
				}
			}
			
		});

			

		//Parancsgomb panel
		JPanel commandButtonPanel = new JPanel();
		commandButtonPanel.setLayout( new FlowLayout(FlowLayout.LEFT));
		commandButtonPanel.add(commandButtonAddSprite);
		commandButtonPanel.add(commandButtonReport);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS	));
		
		southPanel.add(commandButtonPanel);
//		southPanel.add( canvasControl.getStatusPanel() );
	
		this.getContentPane().setLayout(new BorderLayout(10,10));
		this.getContentPane().add(myCanvas, BorderLayout.CENTER);
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
//		this.getContentPane().add(canvasControl.getControlPanel(), BorderLayout.EAST);
/*
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
*/		
		this.setVisible(true);		

	}
}

