package ui.rendering;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;


public class UiRenderer {
	
	private List<Screen> screens;
	
	public UiRenderer() {
		screens = new ArrayList<Screen>();
	}
	
	public void addScreen(Screen screen) {
		if(screens.size() == 0) {
			screen.setVisible(true);
		}
		
		screens.add(screen);
	}
	
	public void render(Graphics2D g) {
		
		for(Screen screen : screens) {
			screen.render(g);
		}
		
	}

}
