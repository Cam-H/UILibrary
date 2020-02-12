package ui.components;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import ui.constraints.Constraint;
import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;
import ui.control.UiThread;
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
	
	private boolean resizable;
	private boolean resizing;
	private int px, py;
	private int cursorType;
	
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
		
		hideBackground = resizable = resizing = false;
		px = py = -1;
		
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
	
	public void setResizable(boolean resizable) {
		this.resizable = resizable;
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
		if(layout instanceof RelativeConstraint) {
			UiConstraint constraints = component.getConstraints();
			
			if(constraints == null) {
				constraints = new UiConstraint();
				
				constraints.setWidth(new RelativeConstraint(this, 0.1f));
				constraints.setHeight(new RelativeConstraint(this, 0.1f));
				
				component.setConstraints(constraints);
			}
		}
		
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
		
		if(!resizing) {
			this.px = this.py = -1;
			
			if(resizable) {
				int x = getX();
				int y = getY();
				
				int width = getWidth() / 2;
				int height = getHeight() / 2;
				
				int offset = 10;
				
				Cursor cursor = null;
				
				if(Math.abs(x - width - px) < offset) {
					
					if(Math.abs(y - height - py) < offset) {
						cursor = new Cursor(Cursor.NW_RESIZE_CURSOR);
					}else if(Math.abs(y + height - py) < offset) {
						cursor = new Cursor(Cursor.SW_RESIZE_CURSOR);
					}else if(Math.abs(y - py) < height) {
						cursor = new Cursor(Cursor.W_RESIZE_CURSOR);
					}
				}else if(Math.abs(x + width - px) < offset) {
					if(Math.abs(y - height - py) < offset) {
						cursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
					}else if(Math.abs(y + height - py) < offset) {
						cursor = new Cursor(Cursor.SE_RESIZE_CURSOR);
					}else if(Math.abs(y - py) < height) {
						cursor = new Cursor(Cursor.E_RESIZE_CURSOR);
					}
				}else if(Math.abs(y - height - py) < offset) {
					if(Math.abs(x - px) < width) {
						cursor = new Cursor(Cursor.N_RESIZE_CURSOR);
					}
				}else if(Math.abs(y + height - py) < offset) {
					if(Math.abs(x - px) < width) {
						cursor = new Cursor(Cursor.S_RESIZE_CURSOR);
					}
				}
				
				if(cursor != null) {
					this.px = px;
					this.py = py;
					
					UiThread.setCursor(cursor);
				}
				
			}
		}else {//Resizing
			if(this.px != -1) {//Resizing width
				int dx = px - this.px;
				
				Constraint wc = constraints.getWidthConstraint();
				
				if(wc instanceof RelativeConstraint) {
					RelativeConstraint widthConstraint = (RelativeConstraint)wc;
					
					float deltaWidthRatio = (float)(px < getX() ? -dx : dx) / widthConstraint.getContainer().getWidth();
					
					if(deltaWidthRatio != 0) {
						int initialWidth = widthConstraint.getConstraint();
						
						widthConstraint.setRatio(widthConstraint.getRatio() + deltaWidthRatio);
						
						Constraint xc = constraints.getXConstraint();
						
						if(xc instanceof RelativeConstraint) {
							RelativeConstraint xConstraint = (RelativeConstraint)xc;
							
							float offset = (widthConstraint.getConstraint() - initialWidth) / 2 * (px < getX() ? -1 : 1);
							
							if(xConstraint.getContainer() != null) {
								xConstraint.setRatio(xConstraint.getRatio() + (offset / xConstraint.getContainer().getWidth()));
							}
							
						}else {//X constraint is a center constraint
							widthConstraint.setRatio(widthConstraint.getRatio() + deltaWidthRatio);
						}

					}
					
				}
							
				this.px += dx;
			}
			
			if(this.py != -1) {//Resizing height
				int dy = py - this.py;
				
				Constraint hc = constraints.getHeightConstraint();
				
				if(hc instanceof RelativeConstraint) {
					RelativeConstraint heightConstraint = (RelativeConstraint)hc;
					
					float deltaHeightRatio = (float)(py < getY() ? -dy : dy) / heightConstraint.getContainer().getHeight();
					
					if(deltaHeightRatio != 0) {
						int initialHeight = heightConstraint.getConstraint();
						
						heightConstraint.setRatio(heightConstraint.getRatio() + deltaHeightRatio);
						
						Constraint yc = constraints.getYConstraint();
						
						if(yc instanceof RelativeConstraint) {
							RelativeConstraint yConstraint = (RelativeConstraint)yc;
							
							float offset = (heightConstraint.getConstraint() - initialHeight) / 2 * (py < getY() ? -1 : 1);
							
							if(yConstraint.getContainer() != null) {
								yConstraint.setRatio(yConstraint.getRatio() + (offset / yConstraint.getContainer().getHeight()));
							}
							
						}else {//X constraint is a center constraint
							heightConstraint.setRatio(heightConstraint.getRatio() + deltaHeightRatio);
						}

					}
					
				}				
								
				this.py += dy;
			}
			
			UiThread.setCursor(new Cursor(cursorType));
		}
		
		
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
		
		if(px != -1 && py != -1) {
			cursorType = UiThread.getCursor().getType();
			
			if(cursorType == Cursor.E_RESIZE_CURSOR || cursorType == Cursor.W_RESIZE_CURSOR) {
				py = -1;
			}else if(cursorType == Cursor.N_RESIZE_CURSOR || cursorType == Cursor.S_RESIZE_CURSOR) {
				px = -1;
			}
			
			resizing = true;
		}
		
		for(UiComponent component : components) {
			component.select();
		}
	}
	
	@Override
	public void deselect() {
		px = py = -1;
		resizing = false;
		
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
		
		if(baseColour != null) {
			g.setColor(baseColour);
		}
		
		if(hideBackground) {
			g.setColor(UiColours.TRANSPARENT);
		}
	}
	

}
