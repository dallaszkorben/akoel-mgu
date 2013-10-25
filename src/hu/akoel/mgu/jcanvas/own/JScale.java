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
	
	private JCanvas canvas;
	public double pixelPerCm;
	public UNIT unit;
	private double startScale;
	
	public JScale( JCanvas canvas, double pixelPerCm, UNIT unit, double startScale ){
		this.canvas = canvas;
		this.pixelPerCm = pixelPerCm;
		this.unit = unit;
		
		double ppu = pixelPerCm * unit.getExchange() / UNIT.cm.getExchange() / startScale;
		
		canvas.setPixelPerUnitX( ppu );
		canvas.setPixelPerUnitY( ppu );
		
		canvas.addPixelPerUnitChangeListener( new PixelPerUnitChangeListener() {
			
			@Override
			public void getPixelPerUnit(double x, double y) {
				
				System.err.println( "M= " + (JScale.this.pixelPerCm * JScale.this.unit.getExchange() / UNIT.cm.getExchange() / x));
				
			}
		});
		
		System.out.println( "ppu: " + ppu );
		
	}
}
