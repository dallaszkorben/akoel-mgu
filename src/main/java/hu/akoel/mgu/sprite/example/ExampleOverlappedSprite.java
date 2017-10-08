package hu.akoel.mgu.sprite.example;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import hu.akoel.mgu.CursorPositionChangeListener;
import hu.akoel.mgu.MControlPanel;
import hu.akoel.mgu.PossiblePixelPerUnits;
import hu.akoel.mgu.axis.Axis;
import hu.akoel.mgu.crossline.CrossLine;
import hu.akoel.mgu.drawnblock.DrawnBlockStatusPanel;
import hu.akoel.mgu.grid.Grid;
import hu.akoel.mgu.scale.Scale;
import hu.akoel.mgu.scale.ScaleChangeListener;
import hu.akoel.mgu.sprite.Appearance;
import hu.akoel.mgu.sprite.Magnet;
import hu.akoel.mgu.sprite.MagnetType;
import hu.akoel.mgu.sprite.Sprite;
import hu.akoel.mgu.sprite.SpriteCanvas;
import hu.akoel.mgu.sprite.elements.FillOvalElement;
import hu.akoel.mgu.sprite.elements.FillRectangleElement;
import hu.akoel.mgu.sprite.elements.OvalElement;
import hu.akoel.mgu.sprite.elements.RectangleElement;
import hu.akoel.mgu.values.DeltaValue;
import hu.akoel.mgu.values.LengthValue;
import hu.akoel.mgu.values.PixelPerUnitValue;
import hu.akoel.mgu.values.PositionValue;
import hu.akoel.mgu.values.RangeValueInPixel;
import hu.akoel.mgu.values.SizeValue;
import hu.akoel.mgu.values.TranslateValue;
import hu.akoel.mgu.values.Value;

public class ExampleOverlappedSprite extends JFrame {

	private static final long serialVersionUID = 5810956401235486862L;

	private SpriteCanvas myCanvas;	
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
	
	private Sprite baseSprite;
	private Sprite pipeDirectHorizontalSprite;
	private Sprite pipeHorizontalSprite;
	private Sprite pipeVerticalSprite;
	private Sprite pipeDirectVerticalSprite;
	
	//Magnet tipus
	private static final MagnetType layerToNeuronMagnetType = new MagnetType("LayerToNeuronMagnetType");
	private static final MagnetType neuronToLayerMagnetType = new MagnetType("NeuronToLayerMagnetType");
	
	private static final double SPACE_FOR_ONE_NEURON = 1.0;
	
	HashSet<Sprite> sprites = new HashSet<Sprite>(); 
	
	
	public static void main(String[] args) {

		new ExampleOverlappedSprite();
	}

	public ExampleOverlappedSprite() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Example :: Overlapped Sprite");
		this.setUndecorated(false);
		this.setSize(700, 700);
		this.createBufferStrategy(1);

		myCanvas = new SpriteCanvas(BorderFactory.createLoweredBevelBorder(), background, possiblePixelPerUnits, positionToMiddle);
		
		myGrid = new Grid( myCanvas, gridType, gridColor, gridWidth, gridPosition, gridDelta );		
		
		myCrossLine = new CrossLine( myCanvas, crossLinePosition, crossLineColor, crossLineWidthInPixel, crossLineLength, crossLinePainterPosition);
	
		myAxis = new Axis(myCanvas, axisPosition, axisColor, axisWidthInPixel, painterPosition);
			
		myScale = new Scale(myCanvas, pixelPerCm, unit, startScale, rate, minScale, maxScale);
		
		//-----------------
		//
		// K-i oldali elem
		//
		// Vezerlopanel
		//
		//-----------------
		MControlPanel controlPanel = new MControlPanel();
		controlPanel.addElement( myGrid.getControl( myScale ) );
		controlPanel.addElement( myCrossLine.getControl( myScale ) );
		controlPanel.addElement( myAxis.getControl( ) );
		
		//------------------------------
		//
		// Parancsgomb panel a gombokkal
		//
		//------------------------------

		//Elhelyezi az eloterbe a Sprite-okat
		JButton addLayerButton = new JButton("add Layer");
		addLayerButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				int numberOfNeurons = 5;
				
				double layerHeight = numberOfNeurons * SPACE_FOR_ONE_NEURON;
				double layerWidth = SPACE_FOR_ONE_NEURON;
				double layerMinY = -( layerHeight / 2 );
				double layerMinX = -( layerWidth / 2 );
				
				//
				//Define Layer
				//
				Sprite layerSprite = new Sprite( 0, new SizeValue( layerMinX, layerMinY, layerMinX + layerWidth, layerMinY + layerHeight ) );
sprites.add(layerSprite);
				
				
				FillRectangleElement layerBaseElement = new FillRectangleElement(	layerMinX, layerMinY, layerWidth, layerHeight, new Appearance( new Color( 40, 140, 180), new BasicStroke(1f) ) );
				layerBaseElement.setSelectedAppearance( new Appearance( new Color( 0, 0, 255 ), new BasicStroke(1f) ));
				RectangleElement layerBorderElement = new RectangleElement(	layerMinX, layerMinY, layerWidth, layerHeight, new Appearance( new Color( 0, 30, 180), new BasicStroke(1f) ) );
				layerBorderElement.setFocusAppearance( new Appearance( Color.blue, new BasicStroke(3f) ) );
				
				layerSprite.addElement( layerBaseElement );
				layerSprite.addElement( layerBorderElement );	
				
				double toNeuronMagnetX = 0;
				double baseToNeuronMagnetY = layerMinY + SPACE_FOR_ONE_NEURON / 2;
				for( int i = 0; i < numberOfNeurons; i++ ){
					double toNeuronMagnetY = baseToNeuronMagnetY + i * SPACE_FOR_ONE_NEURON;
					FillOvalElement layerToNeuronMagnetElement = new FillOvalElement( 0.0, 0.0, 0.04, new Appearance( Color.red, new BasicStroke(1) ) );
					
					Magnet toNeuronEastMagnet = new Magnet(layerSprite, layerToNeuronMagnetType, 90.0, new RangeValueInPixel( 20, 20 ), new PositionValue( toNeuronMagnetX, toNeuronMagnetY ) );
					toNeuronEastMagnet.addPossibleMagnetTypeToConnect( neuronToLayerMagnetType  );
					toNeuronEastMagnet.addElement( layerToNeuronMagnetElement );

					layerSprite.addMagnet( toNeuronEastMagnet );
				}
				
				layerSprite.setPosition( 0, 0 );
				myCanvas.addSprite(layerSprite);

				//
				//Define Neurons
				//
				double toLayerMagnetX = 0;
				double toLayerMagnetY = 0;
				for( int i = 0; i < numberOfNeurons; i++ ){
					double neuronDiameter = SPACE_FOR_ONE_NEURON - SPACE_FOR_ONE_NEURON * 0.2;
					double neuronRadius = neuronDiameter / 2;
					double neuronMinY = -( neuronRadius );
					double neuronMinX = -( neuronRadius );
					
					Sprite neuronSprite = new Sprite( 1, new SizeValue( neuronMinX, neuronMinY, neuronMinX + neuronDiameter, neuronMinY + neuronDiameter ), false );
sprites.add(neuronSprite);					
					
					FillOvalElement neuronBaseElement = new FillOvalElement( toLayerMagnetX, toLayerMagnetY, neuronRadius, new Appearance( Color.yellow, new BasicStroke(1f) ) );
					neuronBaseElement.setSelectedAppearance( new Appearance( Color.green, new BasicStroke(5) ) );
					OvalElement neuronBorderElement = new OvalElement( toLayerMagnetX, toLayerMagnetY, neuronRadius, new Appearance( Color.yellow, new BasicStroke(1f) ) );
					neuronBorderElement.setFocusAppearance( new Appearance( Color.red, new BasicStroke(3) ) );

					double neuronPositionY = baseToNeuronMagnetY + i * SPACE_FOR_ONE_NEURON;
					neuronSprite.addElement( neuronBaseElement );		
					neuronSprite.addElement( neuronBorderElement );
					
					//Define Neurons Magnet
					FillOvalElement neuronToLayerMagnetElement = new FillOvalElement( 0.0, 0.0, 0.04, new Appearance( Color.yellow, new BasicStroke(1) ) );
					
					Magnet toLayerWestMagnet = new Magnet( neuronSprite, neuronToLayerMagnetType, 270.0, new RangeValueInPixel( 20, 20 ), new PositionValue( toLayerMagnetX, toLayerMagnetY ) );
					toLayerWestMagnet.addPossibleMagnetTypeToConnect( layerToNeuronMagnetType  );
					toLayerWestMagnet.addElement( neuronToLayerMagnetElement );
					neuronSprite.addMagnet( toLayerWestMagnet );
					
					
					
					
					
					
					
					
					neuronSprite.setPosition( toLayerMagnetX, neuronPositionY );			
					myCanvas.addSprite(neuronSprite);
				}
				
				//It makes the real connection by magnet - not only by position
				myCanvas.doArangeSpritePositionByMagnet( layerSprite );
				
				//A Canvas ujrarajzolasa, az uj Sprite megjelenites miatt
				myCanvas.revalidateAndRepaintCoreCanvas();
				
			}
		});
		
		JButton removeLayerButton = new JButton("Remove Layer");
		removeLayerButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				myCanvas.removeSprites(sprites);
				myCanvas.revalidateAndRepaintCoreCanvas();				
			}
		});
		

		//Parancsgomb panel
		JPanel commandButtonPanel = new JPanel();
		commandButtonPanel.setLayout( new FlowLayout(FlowLayout.LEFT));
		commandButtonPanel.add(addLayerButton );
		commandButtonPanel.add(removeLayerButton );
		
		//-------------
		//
		// Statuszpanel
		//
		//-------------
		final DrawnBlockStatusPanel statusPanel = new DrawnBlockStatusPanel();

		// Kezdo ertekek kiirasa
		DecimalFormat df = new DecimalFormat("#.00");
		if( myScale.getScale().getX() < 1.0 ){
			statusPanel.setScale( "M=" + df.format(1/myScale.getScale().getX() ) + ":1" );
		}else{
			statusPanel.setScale( "M=1:" + df.format(myScale.getScale().getX() ) );
		}
		
		// Kurzor pozicio figyelo
		myCanvas.addCursorPositionChangeListener( new CursorPositionChangeListener() {

			public void getWorldPosition(double xPosition, double yPosition) {
				DecimalFormat df = new DecimalFormat("#.0000");				
				statusPanel.setXPosition( "x: " + df.format(xPosition));
				statusPanel.setYPosition( "y: " + df.format(yPosition));				
			}
		});
		
		// Meretarany figyelo
		myScale.addScaleChangeListener(new ScaleChangeListener() {		

			public void getScale(Value scale) {
				DecimalFormat df = new DecimalFormat("#.00");
				if( myScale.getScale().getX() < 1.0 ){
					statusPanel.setScale( "xM=" + df.format(1/myScale.getScale().getX() ) + ":1" );
				}else{
					statusPanel.setScale( "xM=1:" + df.format(myScale.getScale().getX() ) );
				}
			}
		});
		
		//--------------------------------------
		//
		// D-i oldali elemek
		//
		// Tartalmazzak a parancsgombi panelt es
		// a status panel-t
		//
		//--------------------------------------
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS	));
		
		southPanel.add( commandButtonPanel );
		southPanel.add( statusPanel );
		
		//--------------------------------
		//
		// Panelek elhelyezese az ablakban
		//
		//--------------------------------
		this.getContentPane().setLayout(new BorderLayout(10,10));
		this.getContentPane().add(myCanvas, BorderLayout.CENTER);
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		this.getContentPane().add(controlPanel, BorderLayout.EAST);		
				
		this.setVisible(true);		

	}
}
