package ui.testing;

import ui.components.UiButton;
import ui.components.UiCheckbox;
import ui.components.UiComponent;
import ui.components.UiLabel;
import ui.components.UiPanel;
import ui.components.UiTextField;
import ui.constraints.CenterConstraint;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;
import ui.graphics.UiColours;
import ui.layouts.LinearLayout;
import ui.layouts.RelativeLayout;
import ui.nav.Direction;
import ui.rendering.Screen;
import ui.transitions.ExpandTransition;
import ui.transitions.UiTransition;

public class TestScreen extends Screen {
	
	private UiButton but1;
	private UiButton but2;
	private UiButton but3;
	private UiButton but4;
	private UiButton but5;
	
	public TestScreen() {
		super();
		
		Window window = UiTester.window;
		
		UiConstraint constraints = new UiConstraint();
		constraints.setX(new CenterConstraint(window));
		constraints.setY(new CenterConstraint(window));
		constraints.setWidth(new RelativeConstraint(window, 0.25f));
		constraints.setHeight(new RelativeConstraint(window, 0.8f));
				
		UiPanel mainPanel = new UiPanel(constraints);
		mainPanel.setResizable(true);
		mainPanel.addBevel(20);
		mainPanel.addComponentBevel(20);
		
		UiTransition transitions = new UiTransition();
		transitions.setHoverExpansion(new ExpandTransition(0, 0, 0.05f));
//		transitions.setHoverSlide(new SlideTransition(0, 0, 0, 0.025f), new SlideTransition(0, 0, 0, 0));
		
		mainPanel.addComponentTransitions(transitions);
		
		mainPanel.setLayout(new LinearLayout(Direction.VERTICAL, 0.05f, 0.025f));
		
		mainPanel.addUiComponent(but1 = new UiButton(null, "Main Screen"));
		mainPanel.addUiComponent(but2 = new UiButton(null, "Show Overlay"));
		mainPanel.addUiComponent(but3 = new UiButton(null, "Hide Overlay"));
		mainPanel.addUiComponent(but4 = new UiButton(null, "Overlay"));
		mainPanel.addUiComponent(but5 = new UiButton(null, "LOCKED")); but5.lock();
		mainPanel.addUiComponent(new UiButton(null));
		mainPanel.addUiComponent(new UiCheckbox(null));

		addUiPanel(mainPanel);
		
		constraints = new UiConstraint();
		constraints.setX(new RelativeConstraint(window, 0.2f));//new PixelConstraint(250)
		constraints.setY(new RelativeConstraint(window, 0.6f));
		constraints.setWidth(new RelativeConstraint(window, 0.2f));
		constraints.setHeight(new RelativeConstraint(window, 0.75f));
		
		UiPanel toolBarPanel = new UiPanel(constraints);
		toolBarPanel.setResizeBounds(0.5f, 2.0f, 1f, 1.5f);
		toolBarPanel.setDraggable(true);
		toolBarPanel.setLayout(new RelativeLayout(Direction.VERTICAL, 0.05f, 0.05f));
		toolBarPanel.setBaseColour(UiColours.GREEN);
		addUiPanel(toolBarPanel);
		
		constraints = constraints.clone();
		constraints.setHeight(new RelativeConstraint(window, 0.1f));
		
		UiPanel doPanel = new UiPanel(constraints);
		toolBarPanel.addUiComponent(doPanel);
		doPanel.setLayout(new LinearLayout(Direction.HORIZONTAL, 0.1f, 0.05f));
		doPanel.addComponentBevel(10);
		doPanel.setBaseColour(UiColours.MAGENTA);
		
		doPanel.addUiComponent(new UiButton(null));
		doPanel.addUiComponent(new UiButton(null));

		constraints = constraints.clone();

		UiPanel rotationPanel = new UiPanel(constraints.clone());
		rotationPanel.setLayout(new LinearLayout(Direction.HORIZONTAL));
		toolBarPanel.addUiComponent(rotationPanel);
		rotationPanel.setBaseColour(UiColours.CYAN);
		
		constraints = constraints.clone();
		constraints.setHeight(new RelativeConstraint(toolBarPanel, null, 0.3f));
		
		UiPanel bPanel = new UiPanel(constraints);
		bPanel.setBaseColour(UiColours.WHITE);
		toolBarPanel.addUiComponent(bPanel);
	}
	
	@Override
	public void update() {
		super.update();
		
		if(but1.isChecked()) {
			nextScreen = UiTester.mainScreen;
			but1.uncheck();
		}
		
		if(but2.isChecked()) {
			nextOverlay = UiTester.overlay;
			but2.uncheck();
		}
		
		if(but3.isChecked()) {
			nextOverlay = this;
			but3.uncheck();
		}
		
		if(but4.isChecked()) {
			nextScreen = UiTester.overlay;
			but4.uncheck();
		}
		
	}

}
