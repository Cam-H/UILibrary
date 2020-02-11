package ui.layouts;

import java.util.List;

import ui.components.UiComponent;
import ui.components.UiContainer;
import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;
import ui.nav.Direction;

public class LinearLayout extends RelativeLayout{

	private float range;
	
	public LinearLayout(Direction alignment) {
		this(alignment, 0, 0);
	}
	
	public LinearLayout(Direction alignment, float margins, float spacing) {
		super(alignment, margins, spacing);
		
		range = 1f;
	}
	
	public void setRange(float range) {
		this.range = range;
	}

	@Override
	public void positionComponents(UiContainer container, List<UiComponent> components) {
		positionComponents(container, components, 0, 0);
	}
	
	@Override
	public void positionComponents(UiContainer container, List<UiComponent> components, float xOffset, float yOffset) {
		
		int elements = components.size();

		float x = 0;
		float y = 0;
		
		float width = getComponentWidth();
		float height = getComponentHeight();
		
		switch(alignment) {
		case VERTICAL:
			height = (range - margins * 2 - spacing * (elements - 1)) / elements;
			
			y = -0.5f + height / 2 + margins + yOffset;
			
			yOffset = height + spacing;
					
			break;
		case HORIZONTAL:		
			width = (range - margins * 2 - spacing * (elements - 1)) / elements;
			
			x = -0.5f + width / 2 + margins + xOffset;
			
			xOffset = width + spacing;
			
			break;
		}
				
		for(UiComponent component : components) {
			
			UiConstraint constraints = new UiConstraint();
			
			constraints.setX(new RelativeConstraint(null, container, x));
			constraints.setY(new RelativeConstraint(null, container, y));
			
			constraints.setWidth(new RelativeConstraint(container, width));
			constraints.setHeight(new RelativeConstraint(container, height));
			
			component.setConstraints(constraints);

			x += xOffset;
			y += yOffset;

		}
		
	}
}
