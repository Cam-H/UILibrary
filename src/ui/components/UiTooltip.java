package ui.components;

import java.awt.Graphics2D;

import ui.constraints.CenterConstraint;
import ui.constraints.Constraint;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;
import ui.control.UiThread;
import ui.graphics.UiColours;
import ui.layouts.RelativeLayout;
import ui.nav.Direction;
import ui.text.Alignment;

public class UiTooltip extends UiPanel {

	public static final int HOVER_TRIGGER = 0;
	public static final int SELECT_TRIGGER = 1;
	
	private int triggerType;
	
	private boolean blockHoverSuppression;
	private boolean initialized;
	
	private UiComponent relative;
	private boolean positionOnMouse;
	
	private int mx;
	private int my;
	
	private UiPanel titlePanel;
	private UiLabel titleLabel;
	private UiButton xTitleButton;
	
	private UiPanel contentsPanel;
	
	public UiTooltip(UiConstraint constraints) {
		this(constraints, "");
	}
	
	public UiTooltip(UiConstraint constraints, String title) {
		super(constraints, "");
		
		if(constraints == null) {
			System.err.println("Error: Tooltips must be defined with constraints!");
		}
		
		initialized = false;
		
		visibility = Visibility.GONE;
		triggerType = HOVER_TRIGGER;
		
		blockHoverSuppression = false;
		
		relative = null;
		positionOnMouse = false;
		
		setAlignment(UiComponent.ALIGNMENT_RIGHT, UiComponent.ALIGNMENT_CENTER);

		mx = my = -1;
		
		titlePanel = new UiPanel(null);
		titlePanel.setBackgroundTransparency(true);
		addUiComponent(titlePanel);
		
		titlePanel.addUiComponent(titleLabel = new UiLabel(null, title));
		titleLabel.setTextColour(UiColours.WHITE);
		
		titlePanel.addUiComponent(xTitleButton = new UiButton(null, "X"));
		xTitleButton.setBaseColour(UiColours.TRANSPARENT);
		xTitleButton.setHighlightColour(UiColours.TRANSPARENT);
		xTitleButton.setSelectColour(UiColours.TRANSPARENT);
		
		xTitleButton.setTextColour(UiColours.RED);
		xTitleButton.setTextHighlightColour(UiColours.RED);
		xTitleButton.setTextSelectColour(UiColours.MAROON);
		
		contentsPanel = new UiPanel(null);
		addUiComponent(contentsPanel);
		
		contentsPanel.setLayout(new RelativeLayout(Direction.VERTICAL, 0.1f, 0f));
		contentsPanel.setAlignment(UiComponent.ALIGNMENT_CENTER, UiComponent.ALIGNMENT_BOTTOM);
		contentsPanel.setBaseColour(UiColours.GREEN);
		
		initialized = true;
	}
	
	public void setTrigger(int triggerType) {
		this.triggerType = triggerType;
	}
	
	public void setRelative(UiComponent relative) {
		this.relative = relative;
	}
	
	public void positionOnMouse() {
		positionOnMouse = true;
	}
	
	public void positionRelative() {
		positionOnMouse = false;
	}
	
	@Override
	public void addUiComponent(UiComponent component) {
		if(initialized) {
			System.err.println("You can not add UI Components directly to a tooltip, use addTip()");
		}
		
		super.addUiComponent(component);
	}
	
	public void addTip(UiComponent tip) {
		contentsPanel.addUiComponent(tip);
	}
	
	@Override
	public void prepare() {
		super.prepare();

		float titleHeight = 0.125f;
		
		UiConstraint panelConstraints = new UiConstraint();
		panelConstraints.setX(new CenterConstraint(this));
		panelConstraints.setY(new RelativeConstraint(null, this, titleHeight / 2 - 0.5f + 0.05f * getWidth() / getHeight()));
		panelConstraints.setWidth(new RelativeConstraint(this, null, 0.9f));
		panelConstraints.setHeight(new RelativeConstraint(this, null, titleHeight));
		
		titlePanel.setConstraints(panelConstraints);
		
		float xWidth = (float)titlePanel.getHeight() / titlePanel.getWidth();
		float titleLabelWidth = 1 - xWidth;

		UiConstraint titleConstraints = new UiConstraint();
		titleConstraints.setX(new RelativeConstraint(null, titlePanel, (titleLabelWidth - 1) / 2));
		titleConstraints.setY(new CenterConstraint(titlePanel));
		titleConstraints.setWidth(new RelativeConstraint(titlePanel, null, titleLabelWidth));
		titleConstraints.setHeight(new RelativeConstraint(titlePanel, null, 1f));
		titleLabel.setConstraints(titleConstraints);
		
		titleConstraints = titleConstraints.clone();
		titleConstraints.setWidth(new RelativeConstraint(titlePanel, null, xWidth));
		titleConstraints.setX(new RelativeConstraint(null, titlePanel, (1 - xWidth) / 2));
		xTitleButton.setConstraints(titleConstraints);
		
		UiConstraint contentConstraints = new UiConstraint();
		contentConstraints.setX(new CenterConstraint(this));
		contentConstraints.setY(new RelativeConstraint(null, titlePanel, 0.55f));
		contentConstraints.setWidth(new RelativeConstraint(this, null, 0.9f));
		contentConstraints.setHeight(new RelativeConstraint(this, null, 1 - titleHeight - 0.05f));
		
		contentsPanel.setConstraints(contentConstraints);
	}
	
	@Override
	public void update() {
		super.update();
		
		if(dragging) {
			blockHoverSuppression = true;
		}
		
		if(xTitleButton.isChecked()) {
			suppress();
			
			blockHoverSuppression = false;
			
			xTitleButton.uncheck();
		}
	}
	
	@Override
	public void hover(int px, int py) {
		super.hover(px, py);
		
		mx = px;
		my = py;
	}
	
	@Override
	public void show() {
		super.show();

		if(relative == null || positionOnMouse) {//Aligning on the mouse cursor
			Constraint xc = constraints.getXConstraint();

			if(xc instanceof PixelConstraint) {
				constraints.setX(new PixelConstraint(mx));
			}else if(xc instanceof RelativeConstraint) {			
				if(relative != null) {
					constraints.setX(new RelativeConstraint(null, relative, 0.5f + (relative.getWidth() / UiThread.getFrame().getWidth())));
				}
			}
			
			Constraint yc = constraints.getYConstraint();
			
			if(yc instanceof PixelConstraint) {
				constraints.setY(new PixelConstraint(my));
			}else if(yc instanceof RelativeConstraint) {
				if(relative != null) {
					constraints.setY(new RelativeConstraint(null, relative, 0.5f + (relative.getHeight() / UiThread.getFrame().getHeight())));
				}
			}
		}
		
		prepare();
	}
	
	public void trigger(boolean hovered, boolean selected) {
		if(visibility != Visibility.GONE) {
			
			switch(triggerType) {
			case HOVER_TRIGGER:
				if(!hovered && !this.hovered && !dragging && !blockHoverSuppression) {
					suppress();
				}
				break;
			}
			
			return;
		}

		switch(triggerType) {
		case HOVER_TRIGGER:
			if(hovered) {
				show();
			}
			break;
		case SELECT_TRIGGER:
			if(selected) {
				show();
			}
			break;
		}
	}
}