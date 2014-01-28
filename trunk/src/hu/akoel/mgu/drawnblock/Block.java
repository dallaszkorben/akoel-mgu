package hu.akoel.mgu.drawnblock;

public class Block {
		
	private double x1;
	private double y1;
	private double x2;
	private double y2;
	
	
	public Block( double x1, double y1, double x2, double y2 ){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public double getX1() {
		return x1;
	}

	public void setX1(double x1) {
		this.x1 = x1;
	}

	public double getY1() {
		return y1;
	}

	public void setY1(double y1) {
		this.y1 = y1;
	}

	public double getX2() {
		return x2;
	}

	public void setX2(double x2) {
		this.x2 = x2;
	}

	public double getY2() {
		return y2;
	}

	public void setY2(double y2) {
		this.y2 = y2;
	}
	
	public boolean intersectsOrContains( Block block2 ){
		double tx1 = getX1();
		double ty1 = getY1();
		double tx2 = getX2();
		double ty2 = getY2();
		
		double rx1 = block2.getX1();
		double ry1 = block2.getY1();
		double rx2 = block2.getX2();
		double ry2 = block2.getY2();
		
		if (tx1 < rx1) tx1 = rx1;
		if (ty1 < ry1) ty1 = ry1;
		if (tx2 > rx2) tx2 = rx2;
		if (ty2 > ry2) ty2 = ry2;
		tx2 -= tx1;
		ty2 -= ty1;

		if( ty2 >= 0 && tx2 >= 0 ){
			return true;
		}else{
			return false;
		}
		
	}
	
	public boolean intersects( Block block2 ) {

		double tx1 = getX1();
		double ty1 = getY1();
		double tx2 = getX2();
		double ty2 = getY2();
		
		double rx1 = block2.getX1();
		double ry1 = block2.getY1();
		double rx2 = block2.getX2();
		double ry2 = block2.getY2();
		
		if (tx1 < rx1) tx1 = rx1;
		if (ty1 < ry1) ty1 = ry1;
		if (tx2 > rx2) tx2 = rx2;
		if (ty2 > ry2) ty2 = ry2;
		tx2 -= tx1;
		ty2 -= ty1;

		//Biztos, hogy keresztezi vagy tartalmazza az egyik a masikat
		if( ty2 >= 0 && tx2 >= 0 ){
			tx2 += tx1;
			ty2 += ty1;

			//Valamelyik tartalmazza a masikat
			if( 
					( ( ( tx2 - tx1 ) == ( getX2() - getX1() ) ) || ( ( tx2 - tx1 ) == ( block2.getX2() - block2.getX1() ) ) ) &&
					( ( ( ty2 - ty1 ) == ( getY2() - getY1() ) ) || ( ( ty2 - ty1 ) == ( block2.getY2() - block2.getY1() ) ) ) 

			){
				return false;

			//Csak keresztezesrol van szo
			}else{
				return true;
			}
			
		}else{
			
			return false;
		}

	}

		
}
