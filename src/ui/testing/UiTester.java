package ui.testing;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ui.components.UiButton;
import ui.components.UiCheckbox;
import ui.components.UiLabel;
import ui.components.UiPanel;
import ui.components.UiTextArea;
import ui.components.UiTextField;
import ui.constraints.CenterConstraint;
import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;
import ui.control.UiThread;
import ui.graphics.UiColours;
import ui.io.Pointer;
import ui.io.UiKeys;
import ui.lang.Japanese;
import ui.layouts.LinearLayout;
import ui.nav.Direction;
import ui.rendering.Screen;
import ui.rendering.UiRenderer;
import ui.testing.Window.WindowMode;
import ui.text.Alignment;
import ui.transitions.ExpandTransition;
import ui.transitions.SlideTransition;
import ui.transitions.UiTransition;

public class UiTester extends Thread {
	
	public static Window window;
	private MasterRenderer mr;
	
	public static Screen mainScreen;
	public static  Screen testScreen;
	public static  Screen overlay;
	public static  Screen layoutTestScreen;

	private UiThread uit;
	private UiButton b1;
	private UiButton b3;
	private UiButton b4;

	public UiTester() {
		window = new Window(WindowMode.RESIZABLE);
		
		UiConstraint constraints = new UiConstraint();
		constraints.setX(new CenterConstraint(window));
		constraints.setY(new CenterConstraint(window));
		constraints.setWidth(new RelativeConstraint(window, 0.9f));
		constraints.setHeight(new RelativeConstraint(window, 0.8f));
		
		
		
		mr = window.getMasterRenderer();
		
		UiRenderer uir = new UiRenderer();
		mainScreen = new Screen();
		
		UiPanel ui1 = new UiPanel(constraints);
		ui1.setLayout(new LinearLayout(Direction.HORIZONTAL, 0.05f, 0.025f));

		ui1.addComponentBevel(10);
				
		UiPanel ui2 = new UiPanel(null);		
		UiPanel ui3 = new UiPanel(null, "Sample text area:");
		
		ui1.addUiComponent(ui2);
		ui1.addUiComponent(ui3);
		
		
		ui2.setLayout(new LinearLayout(Direction.VERTICAL, 0.05f, 0.025f));
		ui2.addComponentBevel(10);

		UiTransition transitions = new UiTransition();
		transitions.setHoverExpansion(new ExpandTransition(0, 0, 0.1f));
//		transitions.setHoverSlide(new SlideTransition(0, 0, 0, 0.025f), new SlideTransition(0, 0, 0, 0));
		
		ui2.addComponentTransitions(transitions);
		
		
		b1 = new UiButton(null, "EXIT");
		
		ui2.addUiComponent(b1);
		ui2.addUiComponent(new UiCheckbox(null, "Check"));
		ui2.addUiComponent(b3 = new UiButton(null, "Test Screen"));
		ui2.addUiComponent(b4 = new UiButton(null, "Layout Test Screen"));
		ui2.addUiComponent(new UiButton(null));
		
		UiTextField t1 = new UiTextField(null, "");
		t1.setPlaceholder("textfield");
		t1.setLanguage(new Japanese());
		ui2.addUiComponent(t1);

		ui3.setLayout(new LinearLayout(Direction.VERTICAL, 0.05f, 0.025f));
		ui3.addComponentBevel(5);
		ui3.addUiComponent(new UiTextArea(null, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged."));
		
		mainScreen.addUiPanel(ui1);
		uir.addScreen(mainScreen);
		uir.addScreen(testScreen = new TestScreen());
		uir.addScreen(overlay = new TestOverlayScreen());
		uir.addScreen(layoutTestScreen = new LayoutTestScreen());
		
		mr.setUiRenderer(uir);
		
		List<Screen> screens = new ArrayList<Screen>();
		screens.add(mainScreen);
		
		uit = new UiThread(screens);
		
		
		
		start();
		
	}
	
	public void run() {
		while(true) {

			try {
				mr.render();
			}catch(Exception e) {e.printStackTrace();}

			uit.updatePointer(new Pointer(window.getMouse().mx(), window.getMouse().my(), window.getMouse().wheelRotation(), window.getMouse().mousePresses()));
			Object[] set = window.getKeyboard().getKeyPresses().toArray();
			
			int[] keyPresses = new int[set.length];
			
			for(int i = 0; i < keyPresses.length; i++) {
				keyPresses[i] = (Integer)set[i];
			}
			
			UiKeys.setKeysPressed(keyPresses);
			
			if(b1.isChecked()) {
				System.exit(0);
			}
			
			if(b3.isChecked()) {
				mainScreen.swapScreen(testScreen);
				b3.uncheck();
			}
			
			if(b4.isChecked()) {
				mainScreen.swapScreen(layoutTestScreen);
				b4.uncheck();
			}
			
			try {
				Thread.sleep(1000 / 60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) {
		new UiTester();
	}

}
