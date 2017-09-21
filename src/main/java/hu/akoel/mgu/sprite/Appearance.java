package hu.akoel.mgu.sprite;

import java.awt.Color;
import java.awt.Stroke;

public class Appearance {
	private Color color;
	private Stroke stroke;
	
	public Appearance( Color color, Stroke stroke ){
		this.color = color;
		this.stroke = stroke;
	}
	
	public Color getColor(){
		return color;
	}
	
	public Stroke getStroke(){
		return stroke;
	}
}
