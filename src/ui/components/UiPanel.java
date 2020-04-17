package ui.components;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import ui.constraints.Constraint;
import ui.constraints.PixelConstraint;
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
	
	protected Layout layout;
	
	private List<UiComponent> components;
	
	protected UiLabel titleLabel;
	protected static final float labelWidth = 1f;
	
	private UiTransition componentTransitions;
	private int componentBevel;
	
	private boolean hideBackground;
	
	private boolean resizable;
	private boolean resizing;
	private int px, py;
	private boolean onRight;
	private boolean onBottom;
	private int cursorType;
	
	private float resizeLowerX;
	private float resizeUpperX;
	
	private float resizeLowerY;
	private float resizeUpperY;
	
	
	protected boolean draggable;
	protected boolean dragging;
	
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
		
		onRight = onBottom = false;
		
		resizeLowerX = resizeUpperX = resizeLowerY = resizeUpperY = -1;
		
		draggable = dragging = false;
		
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
	
	/**
	 * Sets bounds on the amount of resizing possible for the panel. Calling this method automatically makes the panel resizable
	 * <p>The amount of resizing is relative to the initial size of the panel
	 * 
	 * @param lowerXMultiple
	 * @param upperXMultiple
	 * @param lowerYMultiple
	 * @param upperYMultiple
	 */
	public void setResizeBounds(float lowerXMultiple, float upperXMultiple, float lowerYMultiple, float upperYMultiple) {
		setResizable(true);
		
		this.resizeLowerX = lowerXMultiple * (constraints.getWidthConstraint() instanceof RelativeConstraint ? ((RelativeConstraint)constraints.getWidthConstraint()).getRatio() : 1);
		this.resizeUpperX = upperXMultiple * (constraints.getWidthConstraint() instanceof RelativeConstraint ? ((RelativeConstraint)constraints.getWidthConstraint()).getRatio() : 1);
		
		this.resizeLowerY = lowerYMultiple * (constraints.getHeightConstraint() instanceof RelativeConstraint ? ((RelativeConstraint)constraints.getHeightConstraint()).getRatio() : 1);
		this.resizeUpperY = upperYMultiple * (constraints.getHeightConstraint() instanceof RelativeConstraint ? ((RelativeConstraint)constraints.getHeightConstraint()).getRatio() : 1);
	}
	
	/**
	 * Makes the panel draggable when pressed by the user and no components / edges are hovered.
	 * Layouts take priority in positioning components, thus only panels inside float layouts may be dragged.
	 * Only panel positions defined by relative or pixel constraints may be dragged.
	 * 
	 * @param draggable
	 */
	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
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
	
	public List<UiComponent> getUiComponents(){
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
		super.update();
		
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
		
		if(!resizing && !dragging) {
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
		}else if(resizing){//Resizing
			if(this.px != -1) {//Resizing width
				int dx = px - this.px;
				
				Constraint wc = constraints.getWidthConstraint();
				
				if(wc instanceof RelativeConstraint) {
					RelativeConstraint widthConstraint = (RelativeConstraint)wc;
					
					float deltaWidthRatio = (float)(onRight ? dx : -dx) / widthConstraint.getContainer().getWidth();
					
					if(deltaWidthRatio != 0) {
						int initialWidth = widthConstraint.getConstraint();
						
						Constraint xc = constraints.getXConstraint();
						
						float ratio = widthConstraint.getRatio() + deltaWidthRatio * (xc instanceof RelativeConstraint ? 1 : 2);
						
						if(resizeUpperX != -1) {
							ratio = Math.min(resizeUpperX, ratio);
						}
						
						if(resizeLowerX != -1) {
							ratio = Math.max(resizeLowerX, ratio);
						}						
						widthConstraint.setRatio(ratio);
												
						if(xc instanceof RelativeConstraint) {
							RelativeConstraint xConstraint = (RelativeConstraint)xc;
							
							float offset = (widthConstraint.getConstraint() - initialWidth) / 2 * (px < getX() ? -1 : 1);
							
							if(xConstraint.getContainer() != null) {
								xConstraint.setRatio(xConstraint.getRatio() + (offset / xConstraint.getContainer().getWidth()));
							}
							
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
					
					float deltaHeightRatio = (float)(onBottom ? dy : -dy) / heightConstraint.getContainer().getHeight();
					
					if(deltaHeightRatio != 0) {
						int initialHeight = heightConstraint.getConstraint();
						
						Constraint yc = constraints.getYConstraint();
						
						float ratio = heightConstraint.getRatio() + deltaHeightRatio * (yc instanceof RelativeConstraint ? 1 : 2);
						
						if(resizeUpperY != -1) {
							ratio = Math.min(resizeUpperY, ratio);
						}
						
						if(resizeLowerY != -1) {
							ratio = Math.max(resizeLowerY, ratio);
						}

						heightConstraint.setRatio(ratio);
						
						
						if(yc instanceof RelativeConstraint) {
							RelativeConstraint yConstraint = (RelativeConstraint)yc;
							
							float offset = (heightConstraint.getConstraint() - initialHeight) / 2 * (py < getY() ? -1 : 1);
							
							if(yConstraint.getContainer() != null) {
								yConstraint.setRatio(yConstraint.getRatio() + (offset / yConstraint.getContainer().getHeight()));
							}
							
						}

					}
					
				}				
								
				this.py += dy;
			}
			
			UiThread.setCursor(new Cursor(cursorType));
		}
		
		if(dragging) {
			if(this.px != -1 && this.py != -1) {
				int dx = px - this.px;
				int dy = py - this.py;
				
				Constraint xConstraint = constraints.getXConstraint();
				
				if(xConstraint instanceof RelativeConstraint) {
					RelativeConstraint rxc = (RelativeConstraint)xConstraint;

					if(rxc.getContainer() != null) {
						rxc.setRatio(rxc.getRatio() + (float)dx / rxc.getContainer().getWidth());
					}else if(rxc.getRelative() != null) {
						rxc.setRatio(rxc.getRatio() + (float)dx / rxc.getRelative().getWidth());
					}
					
				}else if(xConstraint instanceof PixelConstraint) {
					constraints.setX(new PixelConstraint(xConstraint.getConstraint() + dx));
				}
				
				Constraint yConstraint = constraints.getYConstraint();
				
				if(yConstraint instanceof RelativeConstraint) {
					RelativeConstraint ryc = (RelativeConstraint)yConstraint;

					if(ryc.getContainer() != null) {
						ryc.setRatio(ryc.getRatio() + (float)dy / ryc.getContainer().getHeight());
					}else if(ryc.getRelative() != null) {
						ryc.setRatio(ryc.getRatio() + (float)dy / ryc.getRelative().getHeight());
					}

				}else if(yConstraint instanceof PixelConstraint) {
					constraints.setY(new PixelConstraint(yConstraint.getConstraint() + dy));
				}
				
				this.px += dx;
				this.py += dy;
			}else {
				this.px = px;
				this.py = py;
			}
						
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
			}else if(component instanceof UiGraph2D) {
				((UiGraph2D)component).scroll(wheelRotation);
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
			
			onRight = px > getX();
			onBottom = py > getY();

			resizing = true;
		}else if(draggable && hovered) {
			boolean componentHovered = false;
			
			for(UiComponent component : components) {
				if(component.isHovered()) {
					componentHovered = true;
					
					break;
				}
			}
			
			if(!componentHovered) {
				dragging = true;
			}
		}
		
		for(UiComponent component : components) {
			component.select();
		}
	}
	
	@Override
	public void deselect() {
		px = py = -1;
		resizing = false;
		
		dragging = false;
		
		for(UiComponent component : components) {
			component.deselect();
		}
	}
	
	@Override
	public void doubleClick() {
		if(visibility != Visibility.VISIBLE) {
			return;
		}
		
		for(UiComponent component : components) {
			component.doubleClick();
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
	
	public boolean isActive() {
		return getActiveComponent() != null;
	}
	
	public UiComponent getActiveComponent() {
		if(!hovered) {
			return null;
		}
		
		for(UiComponent component : components) {
			if(component.isSelected()) {
				return component;
			}
		}
		
		return null;
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
		if(visibility != Visibility.VISIBLE) {
			return;
		}
		
		if(container != null) {
			super.render(g, container);
			
			UiContainer mergedBounds = getBounds(container);
			
			for(UiComponent component : components) {
				component.render(g, mergedBounds);
			}
			
			titleLabel.render(g);
		}else {
			render(g);
		}
		
//		System.out.println(container.getX() + " " + container.getHeight() + " " + (container == this));
		
//		render(g);
	}
	
	private UiContainer getBounds(UiContainer container) {		
		UiConstraint bounds = new UiConstraint();
		
		int width = container.getWidth();
		int height = container.getHeight();
		
		int minX = container.getX() - width / 2;
		int maxX = minX + width;
		
		int minY = container.getY() - height / 2;
		int maxY = minY + height;
		
		UiContainer panelBounds = layout.getBounds(this);
		
		int x = panelBounds.getX();
		int y = panelBounds.getY();
		
		width = panelBounds.getWidth();
		height = panelBounds.getHeight();
		
		if(x - width / 2 > minX) {
			minX = x - width / 2;
		}
		
		if(x + width / 2 < maxX) {
			maxX = x + width / 2;
		}
		
		if(y - height / 2 > minY) {
			minY = y - height / 2;
		}
		
		if(y + height / 2 < maxY) {
			maxY = y + height / 2;
		}
		
		width = maxX - minX;
		height = maxY - minY;
		
		bounds.setX(new PixelConstraint(minX + width / 2));
		bounds.setY(new PixelConstraint(minY + height / 2));
		bounds.setWidth(new PixelConstraint(width));
		bounds.setHeight(new PixelConstraint(height));

		return new UiComponent(bounds);
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
