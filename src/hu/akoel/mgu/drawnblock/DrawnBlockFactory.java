package hu.akoel.mgu.drawnblock;

import hu.akoel.mgu.drawnblock.DrawnBlock.Status;

public interface DrawnBlockFactory {

	public DrawnBlock getNewDrawnBlock( Status status, double x1, double y1 );
	
}
