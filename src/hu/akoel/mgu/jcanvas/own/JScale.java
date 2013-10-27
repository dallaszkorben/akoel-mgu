package hu.akoel.mgu.jcanvas.own;

import java.util.ArrayList;

public class JScale {

	public static enum UNIT{
		mm(0.001, "mm"),
		cm(0.01, "cm"),
		m(1, "m"),
		km(1000, "km");
		
		private double exchange;
		private String sign;
		
		private UNIT( double exchange, String sign ){
			this.exchange = exchange;
			this.sign = sign;
		}
		
		public double getExchange(){
			return exchange;
		}
		
		public String getSign(){
			return sign;
		}
	}
	
	private JCanvas canvas;
	private double pixelPerCmX;
	private UNIT unitX;
	private double startScaleX;
	
	private double pixelPerCmY;
	private UNIT unitY;
	private  double startScaleY;
	
	private Position rate;
	private Position minScale;
	private Position maxScale;
	
	private ArrayList<ScaleChangeListener> scaleChangeListenerList = new ArrayList<ScaleChangeListener>();
	
	public JScale( JCanvas canvas, double pixelPerCm, UNIT unit, double startScale){
		commonConstructorForFreeScale(canvas, pixelPerCm, unit, startScale, pixelPerCm, unit, startScale, null, null, null );
	}
	
	public JScale( JCanvas canvas, double pixelPerCm, UNIT unit, double startScale, Position rate ){
		commonConstructorForFreeScale(canvas, pixelPerCm, unit, startScale, pixelPerCm, unit, startScale, rate, null, null );		
	}
	
	public JScale( JCanvas canvas, double pixelPerCm, UNIT unit, double startScale, Position rate, Position minScale, Position maxScale ){
		commonConstructorForFreeScale(canvas, pixelPerCm, unit, startScale, pixelPerCm, unit, startScale, rate, minScale, maxScale );		
	}

	public JScale( JCanvas canvas, double pixelPerCmX, UNIT unitX, double startScaleX, double pixelPerCmY, UNIT unitY, double startScaleY ){
		commonConstructorForFreeScale(canvas, pixelPerCmX, unitX, startScaleX, pixelPerCmY, unitY, startScaleY, null, null, null);
	}
	
	public JScale( JCanvas canvas, double pixelPerCmX, UNIT unitX, double startScaleX, double pixelPerCmY, UNIT unitY, double startScaleY, Position rate){
		commonConstructorForFreeScale(canvas, pixelPerCmX, unitX, startScaleX, pixelPerCmY, unitY, startScaleY, rate, null, null);
	}
	
	public JScale( JCanvas canvas, double pixelPerCmX, UNIT unitX, double startScaleX, double pixelPerCmY, UNIT unitY, double startScaleY, Position rate, Position minScale, Position maxScale){
		commonConstructorForFreeScale(canvas, pixelPerCmX, unitX, startScaleX, pixelPerCmY, unitY, startScaleY, rate, minScale, maxScale);
	}
	
	private void commonConstructorForFreeScale( JCanvas canvas, double pixelPerCmX, UNIT unitX, double startScaleX, double pixelPerCmY, UNIT unitY, double startScaleY, Position rate, Position minScale, Position maxScale ){
		this.canvas = canvas;
		this.pixelPerCmX = pixelPerCmX;
		this.unitX = unitX;
		this.startScaleX = startScaleX;
		
		this.pixelPerCmY = pixelPerCmY;
		this.unitY = unitY;
		this.startScaleY = startScaleY;
		
		this.rate = rate;
		this.minScale = minScale;
		this.maxScale = maxScale;
		
		double startPPUX = getPixelPerUnitByScale(pixelPerCmX, unitX, startScaleX);
		double startPPUY = getPixelPerUnitByScale(pixelPerCmY, unitY, startScaleY);

		//Ha adtam meg nagyitasi hatarokat
		if( null != minScale && null != maxScale ){
		
			double minPPUX = getPixelPerUnitByScale(pixelPerCmX, unitX, maxScale.getX());
			double minPPUY = getPixelPerUnitByScale(pixelPerCmY, unitY, maxScale.getY());
			
			double maxPPUX = getPixelPerUnitByScale(pixelPerCmX, unitX, minScale.getX());
			double maxPPUY = getPixelPerUnitByScale(pixelPerCmY, unitY, minScale.getY());

			canvas.setPossiblePixelPerUnits( new PossiblePixelPerUnits( 
				new Position(startPPUX, startPPUY), 
				rate, 
				new Position(minPPUX, minPPUY), 
				new Position(maxPPUX, maxPPUY)
			));
		
		//Ha nem voltak nagyitasi hatarok
		}else{
			canvas.setPossiblePixelPerUnits( new PossiblePixelPerUnits( 
					new Position(startPPUX, startPPUY), 
					rate					
				));
		}

		canvas.addPixelPerUnitChangeListener( new ScalePixelPerUnitChangeListener( pixelPerCmX, unitX, pixelPerCmY, unitY ));

	}
	
	public JScale( JCanvas canvas, double pixelPerCm, UNIT unit, ArrayList<Position> possibleScaleList, int pointerForPossibleScaleList){
		commonConstructorForDiscrateScale(canvas, pixelPerCm, unit, pixelPerCm, unit, possibleScaleList, pointerForPossibleScaleList);
	}
	
	public JScale( JCanvas canvas, double pixelPerCmX, UNIT unitX, double pixelPerCmY, UNIT unitY, ArrayList<Position> possibleScaleList, int pointerForPossibleScaleList){
		commonConstructorForDiscrateScale(canvas, pixelPerCmX, unitX, pixelPerCmY, unitY, possibleScaleList, pointerForPossibleScaleList);
	}
	
	private void commonConstructorForDiscrateScale( JCanvas canvas, double pixelPerCmX, UNIT unitX, double pixelPerCmY, UNIT unitY, ArrayList<Position> possibleScaleList, int pointerForPossibleScaleList ){
		this.canvas = canvas;
		this.pixelPerCmX = pixelPerCmX;
		this.unitX = unitX;
		this.pixelPerCmY = pixelPerCmY;
		this.unitY = unitY;
		
		ArrayList<Position> possiblePPUList = new ArrayList<Position>();
		
		for( Position scale: possibleScaleList ){
			possiblePPUList.add(
					new Position(getPixelPerUnitByScale(pixelPerCmX, unitX, scale.getX()), getPixelPerUnitByScale(pixelPerCmY, unitY, scale.getY()))				
			);
		}
		
		canvas.setPossiblePixelPerUnits( new PossiblePixelPerUnits(possiblePPUList, pointerForPossibleScaleList));
	
		canvas.addPixelPerUnitChangeListener( new ScalePixelPerUnitChangeListener( pixelPerCmX, unitX, pixelPerCmY, unitY ));

	}
	
	public void addScaleChangeListener( ScaleChangeListener scaleChangeListener ){
		scaleChangeListenerList.add(scaleChangeListener);
	}	
	
	public double getPixelPerUnitByScale(double pixelPerCm, UNIT unit, double scale){
		return pixelPerCm * unit.getExchange() / UNIT.cm.getExchange() / scale;
	}
	
	public double getScaleByPixelPerUnit( double pixelPerCm, UNIT unit, double ppu){
		return (pixelPerCm * unit.getExchange() / UNIT.cm.getExchange() / ppu	);
	}
		
	public UNIT getUnitX(){
		return unitX;
	}
	
	public UNIT getUnitY(){
		return unitY;
	}
	
	class ScalePixelPerUnitChangeListener implements PixelPerUnitChangeListener{
		public double pixelPerCmX;
		public UNIT unitX;
		
		public double pixelPerCmY;
		public UNIT unitY;
		
		public ScalePixelPerUnitChangeListener( double pixelPerCmX, UNIT unitX, double pixelPerCmY, UNIT unitY){
			this.pixelPerCmX = pixelPerCmX;
			this.unitX = unitX;
			
			this.pixelPerCmY = pixelPerCmY;
			this.unitY = unitY;
		}
		
		@Override
		public void getPixelPerUnit( Position pixelPerUnit ) {
			
			double scaleX = getScaleByPixelPerUnit(pixelPerCmX, unitX, pixelPerUnit.getX());
			double scaleY = getScaleByPixelPerUnit(pixelPerCmY, unitY, pixelPerUnit.getY());
			
			for( ScaleChangeListener listener : scaleChangeListenerList){
				listener.getScale( new Position( scaleX, scaleY ) );
			}
			
		}
	}
	
	public Position getScale(){
		double scaleX = getScaleByPixelPerUnit(pixelPerCmX, unitX, canvas.getPixelPerUnit().getX());
		double scaleY = getScaleByPixelPerUnit(pixelPerCmY, unitY, canvas.getPixelPerUnit().getY());
		return new Position( scaleX, scaleY );
	}
}

	
	
