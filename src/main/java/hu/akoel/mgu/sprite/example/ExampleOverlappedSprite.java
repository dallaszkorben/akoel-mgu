package hu.akoel.mgu.sprite.example;

import javax.swing.JFrame;

public class ExampleOverlappedSprite extends JFrame {

	private static final long serialVersionUID = 5810956401235486862L;
	
	public ExampleOverlappedSprite() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Example :: Overlapped Sprite");
		this.setUndecorated(false);
		this.setSize(700, 700);
		this.createBufferStrategy(1);
	}
}
