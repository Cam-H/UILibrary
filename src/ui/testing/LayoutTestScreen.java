package ui.testing;

import ui.components.UiButton;
import ui.components.UiCheckbox;
import ui.components.UiPanel;
import ui.constraints.CenterConstraint;
import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;
import ui.graphics.UiColours;
import ui.layouts.GridLayout;
import ui.layouts.LinearLayout;
import ui.layouts.ScrollLayout;
import ui.nav.Direction;
import ui.rendering.Screen;
import ui.transitions.ExpandTransition;
import ui.transitions.UiTransition;

public class LayoutTestScreen extends Screen {

	private UiButton but1;
	private UiButton but2;
	private UiButton but3;
	private UiButton but4;

	public LayoutTestScreen() {
		super();
		
		Window window = UiTester.window;
		
		UiConstraint constraints = new UiConstraint();
		constraints.setX(new CenterConstraint(window));
		constraints.setY(new CenterConstraint(window));
		constraints.setWidth(new RelativeConstraint(window, 1f));
		constraints.setHeight(new RelativeConstraint(window, 0.9f));
				
		UiPanel mainPanel = new UiPanel(constraints);
		mainPanel.setBackgroundTransparency(true);
		mainPanel.addComponentBevel(20);

		mainPanel.setLayout(new LinearLayout(Direction.HORIZONTAL, 0.05f, 0.025f));
		

		UiPanel gridPanel = new UiPanel(null);
		mainPanel.addUiComponent(gridPanel);

		gridPanel.setLayout(new GridLayout(3, 3, 0.05f, 0.025f));
		
		gridPanel.addUiComponent(new UiButton(null, "1"));
		gridPanel.addUiComponent(new UiButton(null, "2"));
		gridPanel.addUiComponent(new UiButton(null, "3"));
		gridPanel.addUiComponent(new UiButton(null, "4"));
		gridPanel.addUiComponent(new UiButton(null, "5"));
		gridPanel.addUiComponent(new UiButton(null, "6"));
		gridPanel.addUiComponent(new UiButton(null, "7"));
		gridPanel.addUiComponent(new UiButton(null, "8"));
		gridPanel.addUiComponent(new UiButton(null, "9"));

		UiTransition transitions = new UiTransition();
		transitions.setHoverExpansion(new ExpandTransition(0, 0, 0.1f));
		
		gridPanel.addComponentTransitions(transitions);
		
		
		UiPanel scrollPanel = new UiPanel(null);
		mainPanel.addUiComponent(scrollPanel);

		scrollPanel.setLayout(new ScrollLayout(Direction.VERTICAL, scrollPanel, 0.1f, 0.025f, 3f));
		scrollPanel.addComponentBevel(10);
		
		UiConstraint bc = new UiConstraint(); bc.setHeight(new RelativeConstraint(scrollPanel, 0.2f));
		
		UiConstraint gpConstraints = new UiConstraint();
		gpConstraints.setHeight(new RelativeConstraint(window, 0.5f));

		scrollPanel.addUiComponent(but1 = new UiButton(gpConstraints.clone(), "Main"));
		scrollPanel.addUiComponent(but2 = new UiButton(bc, "X"));but2.suppress();
		scrollPanel.addUiComponent(new UiButton(gpConstraints.clone()));
		scrollPanel.addUiComponent(new UiButton(gpConstraints.clone()));
		scrollPanel.addUiComponent(new UiPanel(gpConstraints.clone()));
		scrollPanel.addUiComponent(new UiButton(bc.clone()));
		scrollPanel.addUiComponent(new UiCheckbox(gpConstraints.clone(), "Check"));
				
//		scrollPanel.addUiComponent(but1 = new UiButton(null, "Main"));
//		scrollPanel.addUiComponent(but2 = new UiButton(bc, "X"));but2.suppress();
//		scrollPanel.addUiComponent(new UiButton(null));
//		scrollPanel.addUiComponent(new UiButton(bc.clone()));
//		scrollPanel.addUiComponent(new UiButton(bc.clone()));
//		scrollPanel.addUiComponent(new UiCheckbox(null, "Check"));

		scrollPanel.addComponentTransitions(transitions);

		
		addUiPanel(mainPanel);
				
	}
	
	@Override
	public void update() {
		super.update();
		
		if(but1.isChecked()) {
			nextScreen = UiTester.mainScreen;
			but1.uncheck();
		}

	}
	
}
