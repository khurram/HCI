import java.io.Serializable;



/**
 * simple class for handling points
 * @author Michal
 *
 */
public class Point implements Serializable {

	private static final long serialVersionUID = 1L;
	private int x = 0;
	private int y = 0;
	public int radius = 0;
	private boolean primary = false;
	
	public Point() {

	}
	
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Point(double x, double y, int radius) {
		this.x = (int)x;
		this.y = (int)y;
		this.radius = radius;
	}
	
	public Point(int x, int y, int radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public Point(int x, int y, boolean primary) {
		this.x = x;
		this.y = y;
		this.primary = primary;
	}
	
	public Point(int x, int y, int radius, boolean primary) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.primary = primary;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public boolean isPrimary() {
		return primary;
	}
	
	public boolean near(Point pt) {
		if((Math.abs(this.getX() - pt.getX()) <= radius) && (Math.abs(this.getY() - pt.getY()) <= radius)) {
			
			//System.out.println("Success....X:"+Math.abs(this.getX() - pt.getX())+" Y:"+Math.abs(this.getY() - pt.getY()));
			return true;
		} else {
			//System.out.println("NOT CLOSE ENOUGH.... X:"+Math.abs(this.getX() - pt.getX())+" Y:"+Math.abs(this.getY() - pt.getY()));
			return false;
		}
	}
}
