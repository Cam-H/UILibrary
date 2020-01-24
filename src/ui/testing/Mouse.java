package ui.testing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener{

	private int mx;
	private int my;
	
	private boolean isPressed;
	
	public Mouse() {
		mx = my = 0;
		
		isPressed = false;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		isPressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		isPressed = false;

	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mx = e.getX();
		my = e.getY();

	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
	}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public int mx() {
		return mx;
	}
	
	public int my() {
		return my;
	}
	
	public boolean isPressed() {
		return isPressed;
	}

}