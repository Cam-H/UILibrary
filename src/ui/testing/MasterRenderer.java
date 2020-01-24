package ui.testing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import ui.rendering.UiRenderer;

public class MasterRenderer {
	
	private Window window;
	private JFrame frame;
	
	private UiRenderer uir;
	
	public MasterRenderer(Window window) {
		
		this.window = window;
		
		this.frame = window.getFrame();
				
		BufferStrategy bs = frame.getBufferStrategy();
		
		if(bs == null) {
			frame.createBufferStrategy(3);
		}
		
		uir = null;
		
	}
	
	public void setUiRenderer(UiRenderer uir) {
		this.uir = uir;
	}
	
	public void render() {
		
		if(window.isResizing()) {//Do not render while the user is changing the size of the frame
			window.pollReset();
			return;
		}
		
		BufferStrategy bs = frame.getBufferStrategy();
				
		Graphics2D g = (Graphics2D)bs.getDrawGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		/////////////////////////////////////////////////////
		
		g.setColor(new Color(0xffffff));
		g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
		
		uir.render(g);
		
		/////////////////////////////////////////////////////
		
		g.dispose();
		bs.show();
		
	}
	
	
	public void swapFrame(JFrame newFrame) {
		frame = newFrame;
		
		frame.createBufferStrategy(3);
	}

}
