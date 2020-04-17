package ui.math;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import ui.graphics.UiColours;

public class DataSet {
	
	protected List<Point> points;
	
	//Data rendering settings
	protected boolean continuous;
	protected Color renderColour;
	
	public DataSet(Function function) {
		this(function, -10, 10, 0.1);
	}
	
	public DataSet(Function function, double st, double et, double step) {
		points = new ArrayList<Point>();
		
		for(double t = st; t <= et; t += step) {
			points.add(function.get(t));
		}
		
		continuous = true;
		renderColour = UiColours.BLACK;
	}
	
	public DataSet(List<Point> points) {
		this.points = points;
		
		continuous = false;
		renderColour = UiColours.BLACK;
	}
	
	public void setRenderColour(Color renderColour) {
		this.renderColour = renderColour;
	}
	
	public List<Point> getData(){
		return points;
	}
	
	public boolean isContinuous() {
		return continuous;
	}
	
	public Color getRenderColour() {
		return renderColour;
	}

}
