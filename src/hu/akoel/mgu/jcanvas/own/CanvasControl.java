package hu.akoel.mgu.jcanvas.own;

public class CanvasControl {

	JCanvas myCanvas;
	JCrossLine myCrossLine;
	JGrid myGrid;
	JAxis myAxis;
	
	public CanvasControl( JCanvas myCanvas, JCrossLine myCrossLine, JGrid myGrid, JAxis myAxis ){
		this.myCanvas = myCanvas;
		this.myCrossLine = myCrossLine;
		this.myGrid = myGrid;
		this.myAxis = myAxis;
	}
	
}
