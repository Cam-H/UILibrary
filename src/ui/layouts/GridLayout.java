package ui.layouts;

import java.util.ArrayList;
import java.util.List;

import ui.components.UiComponent;
import ui.components.UiContainer;
import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;

public class GridLayout implements Layout {

	protected int rows;
	protected int cols;
	
	protected float margins;
	protected float spacing;
	
	public GridLayout() {
		this(2, 2);
	}
	
	public GridLayout(int rows, int cols) {
		this(rows, cols, 0, 0);
	}
	
	public GridLayout(int rows, int cols, float margins, float spacing) {		
		this.rows = rows;
		this.cols = cols;
		
		this.margins = margins;
		this.spacing = spacing;
	}
	
	@Override
	public void positionComponents(UiContainer container, List<UiComponent> components) {
		
		float width = (1 - margins * 2 - (cols - 1) * spacing) / cols;
		float height = (1 - margins * 2 - (rows - 1) * spacing) / rows;
		
		float x = -0.5f + width / 2 + margins;
		float y = -0.5f + height / 2 + margins;
		
		float xOffset = width + spacing;
		float yOffset = height + spacing;
		
		int i = 0, j = 0;
		
		for(UiComponent component : components) {
			
			UiConstraint constraints = new UiConstraint();
			
			constraints.setX(new RelativeConstraint(null, container, x));
			constraints.setY(new RelativeConstraint(null, container, y));
			
			constraints.setWidth(new RelativeConstraint(container, width));
			constraints.setHeight(new RelativeConstraint(container, height));
			
			component.setConstraints(constraints);

			x += xOffset;
			i++;
			
			if(i >= cols) {
				x = -0.5f + width / 2 + margins;
				i = 0;
				j++;
				y += yOffset;
				
				if(j >= rows && cols * rows < components.size()) {
					x = y = xOffset = yOffset = width = height = 0;
					System.err.println("There was not enough space in the grid layout to fit all components!");
				}
			}
			
			
		}
		
	}

	@Override
	public List<UiComponent> getGeneratedComponents() {return new ArrayList<UiComponent>();}
	
	@Override
	public boolean repositionIsRequired() {return false;}
	
	@Override
	public UiContainer getBounds(UiContainer container) {		
		UiConstraint bounds = new UiConstraint();
		
		bounds.setX(new RelativeConstraint(null, container, 0));
		bounds.setY(new RelativeConstraint(null, container, 0));
		bounds.setWidth(new RelativeConstraint(container, 1f - margins * 2));
		bounds.setHeight(new RelativeConstraint(container, 1f - margins * 2));

		return new UiComponent(bounds);
		
	}
	
	@Override
	public float getMargins() {return margins;}
	
}
