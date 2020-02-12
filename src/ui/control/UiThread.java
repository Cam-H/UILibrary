package ui.control;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import ui.io.Pointer;
import ui.rendering.Screen;

public class UiThread extends Thread {
	
	private static JFrame frame;
	private static Cursor cursor;
	
	private Pointer pointer;
	private boolean wasSelecting;
	
	
	private List<Screen> activeScreens;
	
	public UiThread() {
		this(new ArrayList<Screen>());
	}
	
	public UiThread(Screen activeScreen) {
		this(new ArrayList<>(Arrays.asList(activeScreen)));
	}
	
	public UiThread(List<Screen> activeScreens) {		
		this.activeScreens = activeScreens;
		
		pointer = new Pointer();
		wasSelecting = false;
		
		start();
	}
	
	public void run() {
		
		while(true) {
			
			cursor = new Cursor(Cursor.DEFAULT_CURSOR);
			
			for(int i = 0; i < activeScreens.size(); i++) {
				Screen temp = activeScreens.get(i).getNextScreen();
				
				if(temp != null) {
					setActiveScreen(temp);
					
					break;
				}
				
				temp = activeScreens.get(i).getNextOverlay();
				
				if(temp != null) {
					if(temp == activeScreens.get(i)) {//Remove overlays
						setActiveScreen(temp);
						break;
					}
					
					overlayScreen(temp);
					
					break;
				}
				
			}
			
			for(Screen screen : activeScreens) {
								
				screen.update();
				
				screen.hover(pointer.getX(), pointer.getY());
				
				if(pointer.getWheelRotation() != 0) {
					screen.scroll(pointer.getWheelRotation());
				}
				
				if(wasSelecting != pointer.isSelecting()) {
					if(wasSelecting) {
						screen.deselect();
					}else {
						screen.select();
					}
				}
				
				screen.runTransitions();
			}
			
			if(wasSelecting != pointer.isSelecting()) {
				wasSelecting = !wasSelecting;
			}
			
			frame.setCursor(cursor);
			
			try {
				Thread.sleep(1000 / 60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void updatePointer(Pointer pointer) {
		this.pointer = pointer;
	}
	
	public void setActiveScreen(Screen screen) {
		for(Screen toReset : activeScreens) {
			toReset.reset();
		}
		
		activeScreens.clear();
		
		activeScreens.add(screen);
		screen.flagToPrepare();
	}
	
	public void overlayScreen(Screen overlay) {
		activeScreens.add(overlay);
		overlay.flagToPrepare();
	}
	
	public static void setFrame(JFrame frame) {
		UiThread.frame = frame;		
	}
	
	public static void setCursor(Cursor cursor) {
		UiThread.cursor = cursor;
	}
	
	public static Cursor getCursor() {
		return cursor;
	}

}
