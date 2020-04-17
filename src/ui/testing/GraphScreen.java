package ui.testing;

import ui.components.UiButton;
import ui.components.UiGraph2D;
import ui.components.UiPanel;
import ui.constraints.CenterConstraint;
import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;
import ui.graphics.UiColours;
import ui.layouts.LinearLayout;
import ui.layouts.RelativeLayout;
import ui.math.DataSet;
import ui.math.Graph;
import ui.math.PolynomialFunction;
import ui.nav.Direction;
import ui.rendering.Screen;
import ui.transitions.ExpandTransition;
import ui.transitions.UiTransition;

public class GraphScreen extends Screen {
	
	private UiGraph2D graph;
	private UiButton exitButton;

	public GraphScreen() {
		super();
		
		Window window = UiTester.window;
		
		UiConstraint constraints = new UiConstraint();
		constraints.setX(new CenterConstraint(window));
		constraints.setY(new CenterConstraint(window));
		constraints.setWidth(new RelativeConstraint(window, 1f));
		constraints.setHeight(new RelativeConstraint(window, 0.9f));
				
		UiPanel mainPanel = new UiPanel(constraints);
//		mainPanel.setBackgroundTransparency(true);
		mainPanel.setBaseColour(UiColours.CYAN);
		mainPanel.addComponentBevel(30);

		mainPanel.setLayout(new RelativeLayout(Direction.HORIZONTAL, 0.0f, 0.025f));
		
		constraints = constraints.clone();
		constraints.setWidth(new RelativeConstraint(mainPanel, 0.2f));
		UiPanel controlPanel = new UiPanel(constraints);
		mainPanel.addUiComponent(controlPanel);
		
		controlPanel.setLayout(new LinearLayout(Direction.VERTICAL, 0.05f, 0.05f));
		controlPanel.addUiComponent(exitButton = new UiButton(null, "Exit"));
		
		constraints = new UiConstraint();
		constraints.setY(new CenterConstraint(window));
		constraints.setWidth(new RelativeConstraint(window, 0.75f));
		
		UiGraph2D graph = new UiGraph2D(constraints);
		mainPanel.addUiComponent(graph);
		
		Graph graphic = new Graph();
		DataSet d2 = new DataSet(new PolynomialFunction(1, 0, 0), -100, 100, 0.1);
		d2.setRenderColour(UiColours.MAGENTA);
		graphic.addDataSet(d2);
		
		DataSet d3 = new DataSet(new PolynomialFunction(1, 0), -100, 100, 0.1);
		d3.setRenderColour(UiColours.GREEN);
		graphic.addDataSet(d3);
		graph.setGraph(graphic);
		
//		scrollPanel.addUiComponent(exitButton = new UiButton(gpConstraints.clone(), "Main"));
//		scrollPanel.addUiComponent(but2 = new UiButton(bc, "X"));but2.suppress();
//		scrollPanel.addUiComponent(new UiButton(gpConstraints.clone()));
//		scrollPanel.addUiComponent(new UiButton(gpConstraints.clone()));
//		scrollPanel.addUiComponent(new UiPanel(gpConstraints.clone()));
//		scrollPanel.addUiComponent(new UiButton(bc.clone()));
//		scrollPanel.addUiComponent(new UiCheckbox(gpConstraints.clone(), "Check"));
//				
////		scrollPanel.addUiComponent(but1 = new UiButton(null, "Main"));
////		scrollPanel.addUiComponent(but2 = new UiButton(bc, "X"));but2.suppress();
////		scrollPanel.addUiComponent(new UiButton(null));
////		scrollPanel.addUiComponent(new UiButton(bc.clone()));
////		scrollPanel.addUiComponent(new UiButton(bc.clone()));
////		scrollPanel.addUiComponent(new UiCheckbox(null, "Check"));
//
//		scrollPanel.addComponentTransitions(transitions);

		
		addUiPanel(mainPanel);
				
	}
	
	@Override
	public void update() {
		super.update();
		
		if(exitButton.isChecked()) {
			nextScreen = UiTester.mainScreen;
			exitButton.uncheck();
		}

	}

}
