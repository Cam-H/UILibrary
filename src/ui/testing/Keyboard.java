package ui.testing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Keyboard implements KeyListener {

	private Set<Integer> keyPresses;
	
	public Keyboard() {
		keyPresses = new HashSet<Integer>();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
//		System.out.println(e.getKeyCode());
		keyPresses.add(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyPresses.remove(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	public Set<Integer> getKeyPresses(){
		return keyPresses;
	}
	
}
