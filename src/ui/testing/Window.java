package ui.testing;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import ui.components.UiContainer;

public class Window implements UiContainer{
	
	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	private static final long resetDelay = 0;
	public enum WindowMode{
		FULL_SCREEN, WINDOWED, RESIZABLE;
		
		public static WindowMode getTypeByID(int ID) {
			return values()[ID];
		}
		
		public static int getIDByType(WindowMode type) {
			for(int i = 0; i < values().length; i++) {
				if(values()[i] == type) {
					return i;
				}
			}
			
			return -1;
		}
		
	}
	
	public enum Position{
		TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;
	}
	
	private JFrame frame;
	
	private MasterRenderer mr;
	
	private Mouse mouse;
	private Keyboard keyboard;
	
	
	private boolean resizing;
	private long ticksForReset;
	
	public Window() {
		this(WindowMode.FULL_SCREEN);
	}
	
	public Window(WindowMode windowType) {
		createWindow(windowType);
		
		mr = new MasterRenderer(this);
		
		mouse = new Mouse();
		keyboard = new Keyboard();
		
		resizing = false;
		ticksForReset = 0;
		
		linkListeners();
	}
	
	public void createWindow(WindowMode windowType) {

		if(frame != null) {
			frame.setVisible(false);
			
			frame = null;
		}
		
		frame = new JFrame();
		
		if(mr != null) {
			mr.swapFrame(frame);
			
			linkListeners();
		}
		
		
//		lastWidth = getWidth();
//		lastHeight = getHeight();
		
		frame.addComponentListener(new ComponentAdapter() {
			public void componentMoved(ComponentEvent e) {
				
//				if(locked && location != null) {
//					MyJFrame.this.setLocation(location);
//				}
			}
		});
		
		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				resizing = true;
				ticksForReset = resetDelay;
			}
		});

		setFrameType(windowType);
		
		frame.setVisible(true);
		
//		if(renderer == null) {
//			renderer = new Renderer(frame);
//		}else {
//			renderer.swapFrame(frame);
//			renderer.unfreeze();
//		}
		
	}
	
	private void setFrameType(WindowMode windowType) {

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		switch(windowType) {
		case FULL_SCREEN://Fullscreen
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
			
//			lock();
			break;
		case WINDOWED://Windowed
			frame.setSize(screenSize);
			frame.setLocationRelativeTo(null);
			frame.setUndecorated(false);
			frame.setResizable(false);
			break;
		case RESIZABLE://Resizable
			frame.setSize(screenSize.width / 2, screenSize.height / 2);
			frame.setUndecorated(false);
			frame.setResizable(true);
			break;
		}
	}
	
	private void linkListeners() {
		frame.addMouseListener(mouse);
		frame.addMouseMotionListener(mouse);
		frame.addMouseWheelListener(mouse);
		
		frame.addKeyListener(keyboard);
	}
	
	public void pollReset() {
		if((ticksForReset--) <= 0) {
			resizing = false;
		}
	}
	
	public void center() {
		frame.setLocationRelativeTo(null);
	}
	
//	public void lock() {
//		locked = true;
//		
//		location = super.getLocation();
//		
//	}
//	
//	public void unlock() {
//		locked = false;
//		
//		location = null;
//	}
	
	public int getWidth() {
		return (frame != null) ? frame.getWidth() : (int)screenSize.getWidth();
	}
	
	public int getHeight() {
		return (frame != null) ? frame.getHeight() : (int)screenSize.getHeight();
	}

	public int getX() {
		return 0;
	}

	public int getY() {
		return 0;
	}
	
	public MasterRenderer getMasterRenderer() {
		return mr;
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public boolean isResizing() {
		return resizing;
	}
	
	public Mouse getMouse() {
		return mouse;
	}
	
	public Keyboard getKeyboard() {
		return keyboard;
	}

}
