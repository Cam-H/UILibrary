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
	
	public static String superscript(String str) {
	    str = str.replaceAll("0", "⁰");
	    str = str.replaceAll("1", "¹");
	    str = str.replaceAll("2", "²");
	    str = str.replaceAll("3", "³");
	    str = str.replaceAll("4", "⁴");
	    str = str.replaceAll("5", "⁵");
	    str = str.replaceAll("6", "⁶");
	    str = str.replaceAll("7", "⁷");
	    str = str.replaceAll("8", "⁸");
	    str = str.replaceAll("9", "⁹");      
	    
	    return str;
	}

}
