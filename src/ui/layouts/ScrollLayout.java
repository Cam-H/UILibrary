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

public class ScrollLayout extends RelativeLayout {
	
	private RelativeLayout contentLayout;
	
	private UiSlider slider;
	private float sliderPosition;
	
	private float sliderWidth;
	private float sliderHeight;
	
	private float scrollRange;
	
	public ScrollLayout(Direction alignment, UiContainer container) {
		this(alignment, container, 0, 0);
	}
	
	public ScrollLayout(Direction alignment, UiContainer container, float margins, float spacing) {
		this(alignment, container, margins, spacing, -1);
	}
	
	public ScrollLayout(Direction alignment, UiContainer container, float margins, float spacing, float scrollRange) {
		super(alignment, margins, spacing);
		
		contentLayout = new RelativeLayout(alignment, margins, spacing);
		setScrollRange(scrollRange);
		
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
		
	}
	
	public void setScrollRange(float scrollRange) {
		if(scrollRange > 0) {
			contentLayout = new LinearLayout(alignment, margins, spacing);
			((LinearLayout)contentLayout).setRange(scrollRange);			
		}
		
		this.scrollRange = scrollRange;
	}
	
	@Override
	public void positionComponents(UiContainer container, List<UiComponent> components) {
		components.remove(slider);
		
		float sliderPosition = slider.getSliderPosition() + 0.5f;

		float xOffset = 0;
		float yOffset = 0;
		
		float scrollRange = this.scrollRange;
		if(scrollRange == -1) {//Relative layout
			scrollRange = margins * 2 - margins;
			
			int containerWidth = container.getWidth();
			int containerHeight = container.getHeight();
			
			for(UiComponent component : components) {
				UiConstraint constraints = component.getConstraints();
				
				if(constraints != null) {
					scrollRange += (alignment == Direction.VERTICAL ? (float)constraints.getHeight() / containerHeight : (float)constraints.getWidth() / containerWidth);
				}
			}
			
			if(alignment == Direction.VERTICAL) {
				yOffset += spacing;
			}else {
				xOffset += spacing;
			}
			
			if(components.size() > 0) {
				UiConstraint constraints = components.get(0).getConstraints();
				
				if(constraints != null) {
//					if(alignment == Direction.VERTICAL) {
//						yOffset -= (float)constraints.getHeight() / containerHeight / 2;
//					}else {
//						xOffset -= (float)constraints.getWidth() / containerWidth / 2;
//					}
				}
				
			}
//			this.scrollRange = scrollRange;
		}
		
		xOffset -= (alignment == Direction.HORIZONTAL ? sliderPosition * (scrollRange - 0.99f) : 0);
		yOffset -= (alignment == Direction.VERTICAL ? sliderPosition * (scrollRange - 0.99f) : 0);

		contentLayout.positionComponents(container, components, xOffset, yOffset);

		switch(alignment) {
		case VERTICAL:
			
			for(UiComponent component : components) {
				UiConstraint constraints = component.getConstraints();
				
				constraints.setWidth(new RelativeConstraint(container, 1f - margins * 2 - sliderWidth));
				constraints.setX(new RelativeConstraint(null, container, -sliderWidth / 2));
			}
			
			break;
		case HORIZONTAL:
			
			for(UiComponent component : components) {
				UiConstraint constraints = component.getConstraints();
				
				constraints.setHeight(new RelativeConstraint(container, 1f - margins * 2 - sliderHeight));
				constraints.setY(new RelativeConstraint(null, container, -sliderHeight / 2));
			}
			
			break;
		}
		
		components.add(slider);
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
