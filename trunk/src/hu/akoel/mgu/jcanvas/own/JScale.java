package hu.akoel.mgu.jcanvas.own;

public class JScale {

	public static enum UNIT{
		mm(0.001),
		cm(0.01),
		m(1),
		km(1000);
		
		private double exchange;	
		
		private UNIT( double exchange ){
			this.exchange = exchange;
		}
		
		public double getExchange(){
			return exchange;
		}
	}
	
	JCanvas canvas;
	double pixelPerCm;
	UNIT unit;
	double startScale;
	
	public JScale( JCanvas canvas, double pixelPerCm, UNIT unit, double startScale ){
		this.canvas = canvas;
		this.pixelPerCm = pixelPerCm;
		this.unit = unit;
		
		double ppu = pixelPerCm * unit.getExchange() / UNIT.cm.getExchange() / startScale;
		
		canvas.setPixelPerUnitX( ppu );
		canvas.setPixelPerUnitY( ppu );
		
		
		System.out.println( "ppu: " + ppu );
		
	}
}
