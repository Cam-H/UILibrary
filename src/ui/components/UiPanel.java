package ui.components;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;
import ui.graphics.UiColours;
import ui.layouts.FloatLayout;
import ui.layouts.Layout;
import ui.layouts.ScrollLayout;
import ui.text.Alignment;
import ui.transitions.UiTransition;

public class UiPanel extends UiComponent {
	
	private Layout layout;
	
	private List<UiComponent> components;
	
	protected UiLabel titleLabel;
	protected static final float labelWidth = 1f;
	
	private UiTransition componentTransitions;
	private int componentBevel;
	
	private boolean hideBackground;
	
	public UiPanel(UiConstraint constraints) {
		this(constraints, new ArrayList<UiComponent>());
	}
	
	public UiPanel(UiConstraint constraints, String title) {
		this(constraints, new ArrayList<UiComponent>(), title);
	}
	
	public UiPanel(UiConstraint constraints, List<UiComponent> components) {
		this(constraints, components, "");
	}
	
	public UiPanel(UiConstraint constraints, List<UiComponent> components, String title) {
		super(constraints);
		
		this.components = components;
		
		UiConstraint titleConstraints = new UiConstraint();
		titleConstraints.setX(new RelativeConstraint(null, this, -0.5f));
		titleConstraints.setY(new RelativeConstraint(null, this, -0.5f));
		titleConstraints.setWidth(new RelativeConstraint(this, labelWidth));
		titleConstraints.setHeight(new RelativeConstraint(this, 0.075f));

		titleLabel = new UiLabel(titleConstraints, title);
		titleLabel.setTextColour(UiColours.WHITE);
		titleLabel.setFont(new Font("TimesRoman", Font.PLAIN, 18));
		titleLabel.setTextAlignment(Alignment.LEFT);
		
		componentTransitions = new UiTransition();
		componentBevel = 0;
		
		hideBackground = false;
		
		layout = new FloatLayout();
	}
	
	public void setLayout(Layout layout) {
		if(layout != null) {
			this.layout = layout;
			
			List<UiComponent> layoutGeneratedComponents = layout.getGeneratedComponents();
			
			for(UiComponent lgc : layoutGeneratedComponents) {
				components.add(lgc);
			}
			
			
			UiConstraint titleConstraints = titleLabel.getConstraints();
			titleConstraints.setX(new RelativeConstraint(null, this, -0.5f + labelWidth / 2));
			titleConstraints.setWidth(new RelativeConstraint(this, labelWidth - layout.getMargins() * 2));

		}
	}
	
	public void prepare() {
		layout.positionComponents(this, getPresentUiComponents());
				
		applyComponentTransitions();
		
		applyComponentBevels();
		
		for(UiComponent component : components) {
			if(component instanceof UiPanel) {
				((UiPanel)component).prepare();
			}
		}
	}
	
	public void addUiComponent(UiComponent component) {
		components.add(component);
	}
	
	private List<UiComponent> getPresentUiComponents(){
		List<UiComponent> components = new ArrayList<UiComponent>();
		
		for(UiComponent component : this.components) {
			if(component.visibility != Visibility.GONE) {
				components.add(component);
			}
		}
		
		return components;
		
	}
	
	public void addComponentTransitions(UiTransition transitions) {
		this.componentTransitions = transitions;
	}
	
	private void applyComponentTransitions() {
		for(UiComponent component : components) {
			component.setTransitions(componentTransitions.clone());
		}
	}
	
	public void addComponentBevel(int bevel) {
		this.componentBevel = bevel;
	}
	
	private void applyComponentBevels() {
		for(UiComponent component : components) {
			component.addBevel(componentBevel);
		}
	}
	
	public void setBackgroundTransparency(boolean isTransparent) {
		hideBackground = isTransparent;
	}
	
	@Override
	public void update() {

		if(layout.repositionIsRequired()) {
			layout.positionComponents(this, getPresentUiComponents());
		}
		
		for(UiComponent component : components) {
			component.update();
		}

	}
	
	@Override
	public void hover(int px, int py) {
		if(visibility != Visibility.VISIBLE) {
			return;
		}
		
		UiContainer bounds = layout.getBounds(this);
		
		for(UiComponent component : components) {
			component.hover(px, py, bounds);
		}
		
		super.hover(px, py);
	}
	
	@Override
	public void hover(int px, int py, UiContainer container) {
		hover(px, py);
	}
	
	public void scroll(int wheelRotation) {
		if(visibility != Visibility.VISIBLE) {
			return;
		}
				
		for(UiComponent component : components) {
			if(component instanceof UiPanel) {
				((UiPanel)component).scroll(wheelRotation);
			}
		}
				
		if(layout instanceof ScrollLayout && hovered) {
			((ScrollLayout)layout).scroll(wheelRotation);
		}
	}
	
	
	@Override
	public void select() {
		if(visibility != Visibility.VISIBLE) {
			return;
		}
		
		for(UiComponent component : components) {
			component.select();
		}
	}
	
	@Override
	public void deselect() {
		for(UiComponent component : components) {
			component.deselect();
		}
	}
	
	@Override
	public void runTransition() {
		if(visibility != Visibility.VISIBLE) {
			return;
		}
		
		for(UiComponent component : components) {
			component.runTransition();
		}
	}
	
	public void render(Graphics2D g) {
		
		if(visibility != Visibility.VISIBLE) {
			return;
		}
		
		super.render(g);

		UiContainer bounds = layout.getBounds(this);

		for(UiComponent component : components) {
			component.render(g, bounds);
		}
		
		titleLabel.render(g);
		
	}
	
	public void render(Graphics2D g, UiContainer container) {
		render(g);
	}
	
	@Override
	protected void assignBaseColour(Graphics2D g) {
		g.setColor(UiColours.DARK_GRAY);
		
		if(hideBackground) {
			g.setColor(UiColours.TRANSPARENT);
		}
	}
	

}
