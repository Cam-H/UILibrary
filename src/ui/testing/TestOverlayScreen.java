package ui.testing;

import ui.components.UiButton;
import ui.components.UiCheckbox;
import ui.components.UiPanel;
import ui.constraints.CenterConstraint;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;
import ui.rendering.Screen;

public class TestOverlayScreen extends Screen {
	
	private UiButton but1;
	
	public TestOverlayScreen() {
		super();
		
		Window window = UiTester.window;
		
		UiConstraint constraints = new UiConstraint();
		constraints.setX(new CenterConstraint(window));
		constraints.setY(new CenterConstraint(window));
		constraints.setWidth(new RelativeConstraint(window, 1f));
		constraints.setHeight(new RelativeConstraint(window, 1f));
				
		UiPanel mainPanel = new UiPanel(constraints);
		mainPanel.addBevel(20);
		mainPanel.addComponentBevel(20);
		mainPanel.setBackgroundTransparency(true);
		
		UiConstraint c2 = new UiConstraint();
		c2.setX(new PixelConstraint(200));
		c2.setY(new PixelConstraint(150));
		c2.setWidth(new RelativeConstraint(mainPanel, 0.15f));
		c2.setHeight(new RelativeConstraint(mainPanel, 0.1f));

		mainPanel.addUiComponent(but1 = new UiButton(c2, "Hello!"));

		addUiPanel(mainPanel);
				
	}
	
	@Override
	public void update() {
		super.update();
		
		if(but1.isChecked()) {
			but1.uncheck();
		}
		
	}

}
