package ui.math;

public class UiMath {
	
	public static boolean isColliding(double x, double y, double sx, double sy, double sw, double sh) {
		if(Math.abs(sx - x) < sw / 2) {
			if(Math.abs(sy - y) < sh / 2) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isColliding(double x, double y, double cx, double cy, double r) {
		return Math.pow(cx - x, 2) + Math.pow(cy - y, 2) < Math.pow(r, 2);
	}

}
