package hu.akoel.mgu.jcanvas;

import hu.akoel.mgu.jcanvas.own.JGraphics;

import java.awt.Graphics2D;

import javax.swing.JPanel;

interface PainterListener{
	public void paint(JPanel canvas, JGraphics g2 );
}