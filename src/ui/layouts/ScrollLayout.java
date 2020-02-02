package ui.layouts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ui.components.UiComponent;
import ui.components.UiContainer;
import ui.components.UiSlider;
import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;
import ui.nav.Direction;

public class ScrollLayout extends LinearLayout {
	
	private UiSlider slider;
	private float sliderPosition;
	
	private float sliderWidth;
	private float sliderHeight;

	private float componentSize;
	
	public ScrollLayout(Direction alignment, UiContainer container) {
		this(alignment, container, 0, 0);
	}
	
	public ScrollLayout(Direction alignment, UiContainer container, float margins, float spacing) {
		super(alignment, margins, spacing);
		
		UiConstraint sliderConstraints = new UiConstraint();
		
		sliderPosition = 0.5f;
		
		slider = new UiSlider(null, alignment);
		
		switch(alignment) {
		case VERTICAL:
			sliderConstraints.setX(new RelativeConstraint(null, container, 0.425f));
			sliderConstraints.setY(new RelativeConstraint(null, container, 0f));
			
			sliderConstraints.setWidth(new RelativeConstraint(container, sliderWidth = 0.15f));
			sliderConstraints.setHeight(new RelativeConstraint(container, sliderHeight = 0.9f));
			break;
		case HORIZONTAL:
			sliderConstraints.setX(new RelativeConstraint(null, container, 0f));
			sliderConstraints.setY(new RelativeConstraint(null, container, 0.425f));
			
			sliderConstraints.setWidth(new RelativeConstraint(container, sliderWidth = 0.9f));
			sliderConstraints.setHeight(new RelativeConstraint(container, sliderHeight = 0.15f));
			break;
		}
		
		slider.setConstraints(sliderConstraints);
		
		componentSize = 0.2f;
		
	}
	
	public void setComponentSize(float relativeSize) {
		this.componentSize = relativeSize;
	}
	
	@Override
	public void positionComponents(UiContainer container, List<UiComponent> components) {

		float sliderPosition = slider.getSliderPosition() + 0.5f;
		
		int elements = components.size();
		
		float x = 0;
		float y = 0;
		
		float xOffset = 0;
		float yOffset = 0;
		
		float width = 0;
		float height = 0;
		
		switch(alignment) {
		case VERTICAL:
			
			width = 1 - margins * 2 - sliderWidth;
			height = componentSize;
			
			yOffset = height + spacing;
			
			y += sliderPosition * Math.min(1 - margins * 2 - yOffset * (elements - 1) + spacing, 0);
			
			break;
		case HORIZONTAL:
						
			width = componentSize;
			height = 1 - margins * 2 - sliderHeight;
			
			xOffset = width + spacing;

			break;
		}
		
		x += -0.5f + width / 2 + margins;
		y += -0.5f + height / 2 + margins;
				
		for(UiComponent component : components) {
			
			if(component != slider) {
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
	
	public void scroll(int wheelRotation) {
		slider.slide(wheelRotation / 10f);
	}
	
	@Override
	public List<UiComponent> getGeneratedComponents() {
		return new ArrayList<>(Arrays.asList(slider));
	}
	
	@Override
	public boolean repositionIsRequired() {
		float sliderPosition = slider.getSliderPosition();

		if(sliderPosition != this.sliderPosition) {
			this.sliderPosition = sliderPosition;
			
			return true;
		}
		
		return false;
	}
	
}
