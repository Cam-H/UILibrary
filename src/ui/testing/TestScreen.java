package ui.testing;

import ui.components.UiButton;
import ui.components.UiCheckbox;
import ui.components.UiPanel;
import ui.constraints.CenterConstraint;
import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;
import ui.layouts.LinearLayout;
import ui.nav.Direction;
import ui.rendering.Screen;
import ui.transitions.ExpandTransition;
import ui.transitions.UiTransition;

public class TestScreen extends Screen {
	
	private UiButton but1;
	private UiButton but2;
	private UiButton but3;
	private UiButton but4;

	public TestScreen() {
		super();
		
		Window window = UiTester.window;
		
		UiConstraint constraints = new UiConstraint();
		constraints.setX(new CenterConstraint(window));
		constraints.setY(new CenterConstraint(window));
		constraints.setWidth(new RelativeConstraint(window, 0.25f));
		constraints.setHeight(new RelativeConstraint(window, 0.8f));
				
		UiPanel mainPanel = new UiPanel(constraints);
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
		mainPanel.addUiComponent(new UiButton(null));
		mainPanel.addUiComponent(new UiButton(null));
		mainPanel.addUiComponent(new UiCheckbox(null));

		addUiPanel(mainPanel);
				
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
