package hu.akoel.mgu.jcanvas;

import java.awt.Graphics2D;
import javax.swing.JPanel;

interface PainterListener{
	public void paint(JPanel canvas, Graphics2D g2 );
}