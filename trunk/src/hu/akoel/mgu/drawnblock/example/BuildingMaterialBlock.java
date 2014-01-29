package hu.akoel.mgu.drawnblock.example;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import hu.akoel.mgu.drawnblock.DrawnBlock;
import hu.akoel.mgu.drawnblock.DrawnBlock.Status;

public class BuildingMaterialBlock extends DrawnBlock{
		
	private Color normalColor = Color.blue;
	private Stroke normalStroke = new BasicStroke(1);
	private Color normalBackgroundColor = new Color( 0, 0, 100 );
	
	private Color selectedColor = Color.cyan;
	private Stroke selectedStroke = new BasicStroke(3);
	private Color selectedBackgroundColor = Color.black;
	
	private Color infocusColor = Color.magenta;
	private Stroke infocusStroke = new BasicStroke(1);
	private Color infocusBackgroundColor = Color.black;
	
	private Color inprocessColor = Color.green;
	private Stroke inprocessStroke = new BasicStroke(3);
	private Color inprocessBackgroundColor = Color.black;
	
	public BuildingMaterialBlock(Status status, double x1, double y1, double x2, double y2) {
		super(status, x1, y1, x2, y2);
		setStatus(status);
	}

	public Color getNormalColor() {
		return normalColor;
	}

	public Stroke getNormalStroke() {
		return normalStroke;
	}

	public Color getNormalBackgroundColor() {
		return normalBackgroundColor;
	}

	public Color getSelectedColor() {
		return selectedColor;
	}

	public Stroke getSelectedStroke() {
		return selectedStroke;
	}

	public Color getSelectedBackgroundColor() {
		return selectedBackgroundColor;
	}

	public Color getInfocusColor() {
		return infocusColor;
	}

	public Stroke getInfocusStroke() {
		return infocusStroke;
	}

	public Color getInfocusBackgroundColor() {
		return infocusBackgroundColor;
	}

	public Color getInprocessColor() {
		return inprocessColor;
	}

	public Stroke getInprocessStroke() {
		return inprocessStroke;
	}

	public Color getInprocessBackgroundColor() {
		return inprocessBackgroundColor;
	}
	
	public void setNormal( Color color, Stroke stroke, Color backgroundColor ){
		this.normalColor = color;
		this.normalStroke = stroke;
		this.normalBackgroundColor = backgroundColor;
		
		refreshStatus();
	}

	public void setSelected( Color color, Stroke stroke, Color backgroundColor ){
		this.selectedColor = color;
		this.selectedStroke = stroke;
		this.selectedBackgroundColor = backgroundColor;
		
		refreshStatus();
	}
	
	public void setInfocus( Color color, Stroke stroke, Color backgroundColor ){
		this.infocusColor = color;
		this.infocusStroke = stroke;
		this.infocusBackgroundColor = backgroundColor;
		
		refreshStatus();
	}

	public void setInprocess( Color color, Stroke stroke, Color backgroundColor ){
		this.inprocessColor = color;
		this.inprocessStroke = stroke;
		this.inprocessBackgroundColor = backgroundColor;
		
		refreshStatus();
	}
}
