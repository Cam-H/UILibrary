package ui.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import ui.constraints.UiConstraint;
import ui.graphics.UiColours;
import ui.math.UiMath;
import ui.transitions.UiTransition;

public class UiComponent implements UiContainer {
	
	public enum Visibility{
		VISIBLE, HIDDEN, GONE;
	}
	
	public static final float ALIGNMENT_CENTER = 0;
	public static final float ALIGNMENT_TOP = -0.5f;
	public static final float ALIGNMENT_BOTTOM = 0.5f;
	public static final float ALIGNMENT_LEFT = -0.5f;
	public static final float ALIGNMENT_RIGHT = 0.5f;

	protected Visibility visibility;
	
	protected UiConstraint constraints;
	protected UiTransition transitions;
	
	protected float xAlignment;
	protected float yAlignment;

	protected boolean hovered;
	protected boolean selected;
	
	private int bevel;
	
	protected Color baseColour;
	
	protected UiTooltip tooltip;
	
	public UiComponent(UiConstraint constraints) {
		this.constraints = constraints;
		transitions = new UiTransition();
		
		visibility = Visibility.VISIBLE;
		
		xAlignment = yAlignment = ALIGNMENT_CENTER;
		
		hovered = selected = false;
		
		bevel = 0;
		
		baseColour = null;
		
		tooltip = null;
	}
	
	public void setConstraints(UiConstraint constraints) {
		this.constraints = constraints;
	}
	
	public void setTransitions(UiTransition transitions) {
		this.transitions = transitions;
	}
	
	public void setAlignment(float xAlignment, float yAlignment) {
		this.xAlignment = xAlignment;
		this.yAlignment = yAlignment;
	}
	
	public void addTooltip(UiTooltip tooltip) {
		this.tooltip = tooltip;
		
		tooltip.setRelative(this);
	}
	
	public void show() {
		visibility = Visibility.VISIBLE;
	}
	
	public void hide() {
		visibility = Visibility.HIDDEN;
	}
	
	public void suppress() {
		visibility = Visibility.GONE;
	}
	
	public void hover(int px, int py, UiContainer container) {
		if(visibility != Visibility.VISIBLE) {
			return;
		}
		
		int x = constraints.getX();
		int y = constraints.getY();
		
		int width = constraints.getWidth();
		int height = constraints.getHeight();
		
		int cx = container.getX();
		int cy = container.getY();
		
		int cWidth = container.getWidth();
		int cHeight = container.getHeight();
		
		int tx = Math.max(x - width / 2, cx - cWidth / 2);
		int ty = Math.max(y - height / 2, cy - cHeight / 2);
		
		int bx = Math.min(x + width / 2, cx + cWidth / 2);
		int by = Math.min(y + height / 2, cy + cHeight / 2);

		hover(px, py, (tx + bx) / 2 + (int)(constraints.getWidth() * (xAlignment + transitions.getXMultiplier())), (ty + by) / 2 + (int)(constraints.getHeight() * (yAlignment + transitions.getYMultiplier())), (int)(Math.max(0, bx - tx) * (transitions.getWidthMultiplier())), (int)(Math.max(0, by - ty) * (transitions.getHeightMultiplier())));

	}
	
	public void hover(int px, int py) {
		hover(px, py, getX(), getY(), getWidth(), getHeight());
	}
	
	protected void hover(int px, int py, int x, int y, int width, int height) {
		boolean temp = false;
		
		if(visibility == Visibility.VISIBLE) {
			
			if(UiMath.isColliding(px, py, x, y, width, height)) {
				temp = true;

				if(bevel > 0) {				
					
					int bevel = getBevel(width, height);
					
					if(UiMath.isColliding(px, py, x - width / 2, y - height / 2, bevel * 2, bevel * 2)) {
						temp = UiMath.isColliding(px, py, x - width / 2 + bevel, y - height / 2 + bevel, bevel);
					}else if(UiMath.isColliding(px, py, x + width / 2, y - height / 2, bevel * 2, bevel * 2)) {
						temp = UiMath.isColliding(px, py, x + width / 2 - bevel, y - height / 2 + bevel, bevel);
					}else if(UiMath.isColliding(px, py, x + width / 2, y + height / 2, bevel * 2, bevel * 2)) {
						temp = UiMath.isColliding(px, py, x + width / 2 - bevel, y + height / 2 - bevel, bevel);
					}else if(UiMath.isColliding(px, py, x - width / 2, y + height / 2, bevel * 2, bevel * 2)) {
						temp = UiMath.isColliding(px, py, x - width / 2 + bevel, y + height / 2 - bevel, bevel);
					}

				}
			}
			
		}
		
		hovered = temp;
		
	}

	
	public void select() {
		if(hovered) {
			selected = true;
		}
	}
	
	public void deselect() {
		selected = false;
	}
	
	public void doubleClick() {}
	
	public void runTransition() {
		transitions.run(hovered);
	}
	
	public void update() {
		if(tooltip != null) {
			tooltip.trigger(hovered, selected);
		}
	}
	
	public void addBevel(int bevel) {
		this.bevel = bevel;
	}
	
	public int getBevel(int width, int height) {
		if(bevel > 0) {
			
			int maxBevel = Math.min(width, height) / 2;
			return (this.bevel < maxBevel) ? this.bevel : maxBevel;
			
		}
		
		return 0;
	}

	public void setBaseColour(Color colour) {
		this.baseColour = colour;
	}
	
	public boolean isVisible() {
		return visibility == Visibility.VISIBLE;
	}
	
	public boolean isHovered() {
		return hovered;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public int getAlpha() {
		return transitions.getAlpha();
	}
	
	public int getX() {
		return constraints.getX() + (int)(constraints.getWidth() * (xAlignment + transitions.getXMultiplier()));
	}

	public int getY() {
		return constraints.getY() + (int)(constraints.getHeight() * (yAlignment + transitions.getYMultiplier()));
	}

	public int getWidth() {
		return (int)(constraints.getWidth() * transitions.getWidthMultiplier());
	}

	public int getHeight() {
		return (int)(constraints.getHeight() * transitions.getHeightMultiplier());
	}
	
	public UiConstraint getConstraints() {
		return constraints;
	}
	
	public void render(Graphics2D g) {
		
		if(constraints == null) {
			System.err.println("Constraints not set for component!");
			return;
		}
		
		int x = getX();
		int y = getY();
		
		int width = getWidth();
		int height = getHeight();
		
		render(g, x - width / 2, y - height / 2, x + width / 2, y + height / 2);
		
	}
	
	public void render(Graphics2D g, UiContainer container) {
		
		if(constraints == null) {
			System.err.println("Constraints not set for component!");
			return;
		}
		
		int x = getX();
		int y = getY();
		
		int width = getWidth();
		int height = getHeight();
		
		int cx = container.getX();
		int cy = container.getY();
		
		int cWidth = container.getWidth();
		int cHeight = container.getHeight();
		
		int tx = Math.max(x - width / 2, cx - cWidth / 2);
		int ty = Math.max(y - height / 2, cy - cHeight / 2);
		
		int bx = Math.min(x + width / 2, cx + cWidth / 2);
		int by = Math.min(y + height / 2, cy + cHeight / 2);
		
		render(g, tx, ty, bx, by);
		
	}
	
	protected void render(Graphics2D g, int tx, int ty, int bx, int by) {
		
		if(visibility != Visibility.VISIBLE) {
			return;
		}
		
		if(bx - tx <= 0 || by - ty <= 0) {
			return;
		}
		
		BufferedImage back = renderBack(getWidth(), getHeight());
		
		assignBaseColour(g);
		
		//**Important to add transition effects
		int xOff = (int)(constraints.getWidth() * (transitions.getXMultiplier()));
		int yOff = (int)(constraints.getHeight() * (transitions.getYMultiplier()));
		
		int widthOff = -(int)((bx - tx) * (1 - transitions.getWidthMultiplier())) / 2;
		int heightOff = -(int)((by - ty) * (1 - transitions.getHeightMultiplier())) / 2;

		g.drawImage(back, tx - widthOff + xOff, ty - heightOff + yOff, bx + widthOff + xOff, by + heightOff + yOff,
				tx - getX() + back.getWidth() / 2 - widthOff, ty - getY() + back.getHeight() / 2 - heightOff, bx - getX() + back.getWidth() / 2 + widthOff, by - getY() + back.getHeight() / 2 + heightOff, null);

	}
	
	private BufferedImage renderBack(int width, int height) {
		BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D bufferGraphics = (Graphics2D)buffer.getGraphics();
		bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		assignBaseColour(bufferGraphics);
		
		int maxBevel = Math.min(width, height) / 2;
		int bevel = (this.bevel < maxBevel) ? this.bevel : maxBevel;
		
		if(bevel > 0) {

			bufferGraphics.fillOval(0, 0, bevel * 2, bevel * 2);
			bufferGraphics.fillOval(width - bevel * 2, 0, bevel * 2, bevel * 2);
			bufferGraphics.fillOval(width - bevel * 2, height - bevel * 2, bevel * 2, bevel * 2);
			bufferGraphics.fillOval(0, height - bevel * 2, bevel * 2, bevel * 2);

			bufferGraphics.fillRect(0, bevel, bevel, height - bevel * 2);
			bufferGraphics.fillRect(width - bevel, bevel, bevel, height - bevel * 2);
			bufferGraphics.fillRect(bevel, 0, width - bevel * 2, height);

		}else {
			bufferGraphics.fillRect(0, 0, width, height);
		}
		
		bufferGraphics.dispose();
		
		return buffer;
	}
	
	protected void assignBaseColour(Graphics2D g) {
		g.setColor(UiColours.GRAY);
		
		if(baseColour != null) {
			g.setColor(baseColour);
		}
	}

}
