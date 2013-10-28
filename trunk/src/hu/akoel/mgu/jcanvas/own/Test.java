package hu.akoel.mgu.jcanvas.own;

import java.awt.Dimension;

public class Test {
	
	private Size worldSize = null;
	private double pixelPerUnit;
	private Value2D worldTranslate = new Value2D( 0.0, 0.0 );
	private Dimension viewableSize;
	private int viewableWidth;
	
	public static void main(String[] args){
		
		Test test = new Test();
		
	}
	
	public Test(){
		setPixelPerUnit(17.84);
		worldSize = new Size(0,0, 20,20);
		
		//szamitjuk hozza a fizikai meretet
		viewableWidth = getPixelLengthByWorld(worldSize.getWidth());
		
		System.err.println("world width: " + worldSize.getWidth() + " canvas size: " + viewableWidth + " value of the last pixel: " + (getWorldXByPixel(viewableWidth-1)) + " Pixel order of the border: " + getPixelXPositionByWorld(worldSize.getXMax()) + " pixel of the bigest counted valu: " + getPixelXPositionByWorld(19.955156950672645));
		
	}
	
	
	public double getWorldXByPixel( int pixel ){
		if( null == worldSize ){

			return getWorldLengthByPixel( pixel + 1 ) - worldTranslate.getX();
			
		}else{
		
			return getWorldLengthByPixel( pixel + 1 ) + worldSize.getXMin() - worldTranslate.getX();
			
		}
		
	}
	
	/**
	 * Visszaadja a kepernyo-koordinatarendszerben (origo a bal felso sarok) talalhato
	 * pixel y poziciojanak valos Y koordinatajat
	 * @param pixel kepernyo-koordinatarendszerben levo pixel y koordinataja
	 * @return valos Y koordinata
	 */
	public double getWorldYByPixel( int pixel ){
		
		if( null == worldSize ){
			return (getViewableSize().height - pixel) / getPixelPerUnit() - worldTranslate.getY();
		}else{			
			return (getViewableSize().height - pixel) / getPixelPerUnit() + worldSize.getYMin() - worldTranslate.getY();
		}
	}

	
	
	public double getWorldLengthByPixel( int pixel ){
		return (pixel-1) / getPixelPerUnit();
	}
	
	public int getPixelLengthByWorld( double length ){
		return (int)( getPixelPerUnit() * length + 1 );
	}
	
	
	
	public int getPixelXPositionByWorld( double x ){

		if( null == worldSize ){
			return getPixelLengthByWorld( x ) - 1;
		}else{
			return getPixelLengthByWorld( x - worldSize.getXMin() ) - 1;
		}
	}
	
	public int getPixelYPositionByWorld( double y ){
		if( null == worldSize ){
			return getPixelLengthByWorld( y ) - 1;
		}else{
			return getPixelLengthByWorld( y - worldSize.getYMin() ) - 1;
		}
	}
	
	public double getPixelPerUnit(){
		return pixelPerUnit;
	}
	
	public void setPixelPerUnit( double unitToPixelPortion ){
		this.pixelPerUnit = unitToPixelPortion;
	}
	
	public Dimension getViewableSize(){
		return viewableSize;
	}
	
	public void setViewableWidth(int pixel){
		viewableSize.width = pixel;
	}
}
