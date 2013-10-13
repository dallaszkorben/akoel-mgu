package hu.akoel.mgu.jcanvas.own;


import java.awt.Graphics2D;

import javax.swing.JPanel;

public interface PainterListener{
	public void paintByWorldPosition(JCanvas canvas, JGraphics g2 );
	
	public void paintByViewer( JCanvas canvas, Graphics2D g2 );
}