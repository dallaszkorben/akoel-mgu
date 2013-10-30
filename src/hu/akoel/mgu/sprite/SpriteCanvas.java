package hu.akoel.mgu.sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.border.Border;

import hu.akoel.mgu.MCanvas;
import hu.akoel.mgu.MGraphics;
import hu.akoel.mgu.PainterListener;
import hu.akoel.mgu.PossiblePixelPerUnits;
import hu.akoel.mgu.values.SizeValue;
import hu.akoel.mgu.values.TranslateValue;

public class SpriteCanvas extends MCanvas{

	private static final long serialVersionUID = -4187978793520279190L;
	
	private ArrayList<Sprite> spriteList = new ArrayList<Sprite>();

	public SpriteCanvas(Border borderType, Color background, PossiblePixelPerUnits possiblePixelPerUnits, TranslateValue positionToMiddle ) {
		super(borderType, background, possiblePixelPerUnits, positionToMiddle );		
		commonConstructor();
	}
	
	public SpriteCanvas(Border borderType, Color background, PossiblePixelPerUnits possiblePixelPerUnits, TranslateValue positionToMiddle, SizeValue boundSize ) {
		super(borderType, background, possiblePixelPerUnits, positionToMiddle, boundSize );
		commonConstructor();
	}
	

	public SpriteCanvas(Border borderType, Color background, SizeValue worldSize) {
		super(borderType, background, worldSize);
		commonConstructor();
	}

	private void commonConstructor(){
		
		
		this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				
				for( Sprite sprite: spriteList){
					SizeValue boundBox = sprite.getBoundBox();
					double xValue = getWorldXByPixel(e.getX() );
					double yValue = getWorldYByPixel(e.getY());
					if( 
							xValue >= boundBox.getXMin() &&
							xValue <= boundBox.getXMax() &&
							yValue >= boundBox.getYMin() &&
							yValue <= boundBox.getYMax()
							){
					
						//Elhelyezi az atmeneti taroloba a focus kulalakjat a sprite-nak
					}
					
					
				}	
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//A kozepso reteget hasznaljuk a sprite-ok megjelenitesere
		addPainterListenerToMiddle(new SpritePainterListener(), Level.ABOVE );
				
	}
	
	public void addSprite( Sprite sprite ){
		if( !spriteList.contains(sprite)){
			spriteList.add(sprite);
		}		
	}
	
	class SpritePainterListener implements PainterListener{

		@Override
		public void paintByWorldPosition(MCanvas canvas, MGraphics g2) {
			for( Sprite sprite: spriteList){
				sprite.draw(g2);
			}			
		}

		@Override
		public void paintByViewer(MCanvas canvas, Graphics2D g2) {}
		
	}
}
