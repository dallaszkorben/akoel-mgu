package hu.akoel.mgu.jcanvas.own;

import java.text.DecimalFormat;

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
	public double pixelPerCmX;
	public UNIT unitX;
	private double startScaleX;
	
	public double pixelPerCmY;
	public UNIT unitY;
	private double startScaleY;
	
	public JScale( JCanvas canvas, double pixelPerCm, UNIT unit, double startScale ){
		this.canvas = canvas;
		this.pixelPerCmX = pixelPerCm;
		this.unitX = unit;
		this.startScaleX = startScale;
		
		this.pixelPerCmY = pixelPerCm;
		this.unitY = unit;
		this.startScaleY = startScale;
		
		double ppuX = getPixelPerUnitByScale(pixelPerCmX, unitX, startScaleX);
		double ppuY = getPixelPerUnitByScale(pixelPerCmY, unitY, startScaleY);
		
		canvas.setPixelPerUnit( ppuX, ppuY );
		
		canvas.addPixelPerUnitChangeListener( new ScalePixelPerUnitChangeListener( pixelPerCmX, unitX, pixelPerCmY, unitY ));
	}

	public JScale( JCanvas canvas, double pixelPerCmX, UNIT unitX, double startScaleX, double pixelPerCmY, UNIT unitY, double startScaleY ){
		this.canvas = canvas;
		this.pixelPerCmX = pixelPerCmX;
		this.unitX = unitX;
		this.startScaleX = startScaleX;
		
		this.pixelPerCmY = pixelPerCmY;
		this.unitY = unitY;
		this.startScaleY = startScaleY;
		
		double ppuX = getPixelPerUnitByScale(pixelPerCmX, unitX, startScaleX);
		double ppuY = getPixelPerUnitByScale(pixelPerCmY, unitY, startScaleY);
		
		canvas.setPixelPerUnit( ppuX, ppuY );
		
		canvas.addPixelPerUnitChangeListener( new ScalePixelPerUnitChangeListener( pixelPerCmX, unitX, pixelPerCmY, unitY ));
		
		canvas.addPositionListener( new ScalePositionListener() );
	}

	
	public double getPixelPerUnitByScale(double pixelPerCm, UNIT unit, double scale){
		return pixelPerCm * unit.getExchange() / UNIT.cm.getExchange() / scale;
	}
	
	public double getScaleByPixelPerUnit( double pixelPerCm, UNIT unit, double ppu){
		return (pixelPerCm * unit.getExchange() / UNIT.cm.getExchange() / ppu	);
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
		public void getPixelPerUnit(double x, double y) {
			DecimalFormat df = new DecimalFormat("#.00");
			
			double scaleX = getScaleByPixelPerUnit(pixelPerCmX, unitX, x);
			if( scaleX >= 1)
				System.err.println( "X M= 1:" + df.format(scaleX));
			else					
				System.err.println( "X M= " + df.format(1/scaleX) + ":1" );
			
			double scaleY = getScaleByPixelPerUnit(pixelPerCmY, unitY, y);			
			if( scaleY >= 1)
				System.err.println( "Y M= 1:" + df.format(scaleY));
			else					
				System.err.println( "Y M= " + df.format(1/scaleY) + ":1" );
			
			System.out.println();
		}
	}
	
	class ScalePositionListener implements PositionListener{
		@Override
		public void getWorldPosition(double xPosition, double yPosition) {
			System.err.println( "X:" + xPosition + " Y:" + yPosition);			
		}		
	}
	
	
}
