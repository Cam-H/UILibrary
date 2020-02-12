package ui.io;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class UiKeys {
	
	public static final int UP = 38;
	public static final int DOWN = 40;

	public static final int LEFT = 37;
	public static final int RIGHT = 39;

	
	public static final int ENTER = 10;

	public static final int SPACE = 32;

	public static final int SHFT = 16;
	public static final int CTRL = 17;
	public static final int ALT = 18;
	
	public static final int CAPS = 20;
	
	public static final int BCK = 8;
	public static final int DEL = 127;

	public static final int ESC = 27;
	
	public static int[] keyPresses = new int[0];
	
	private static long ticksPerCharacter = 25;
	private static long speedTicksPerCharacter = 1;
	private static long remainingTicks = 0;
	
	private static boolean keyUpdate = false;
	private static boolean keyHeld = false;
		
	public static void setKeysPressed(int[] keyPresses) {
		if(getUniqueKey(keyPresses, UiKeys.keyPresses) != -1) {
			keyUpdate = true;
		}
		
		UiKeys.keyPresses = keyPresses;
	}
	
	public static boolean keyIsPressed(int keyCode) {
		
		for(Integer key : keyPresses) {
			if(key == keyCode) {
				return true;
			}
		}
		
		return false;
		
	}
	
	public static int getUniqueKey(int[] arr1, int[] arr2) {
		int key = -1;
		
		for(int i = 0; i < arr1.length; i++) {
			boolean sharedKey = false;
			
			for(int j = 0; j < arr2.length; j++) {
				if(arr1[i] == arr2[j]) {
					sharedKey = true;
					break;
				}
			}
			
			if(!sharedKey) {
				key = arr1[i];
				break;
			}
		}
		
		return key;
	}
	
	public static int[] getNextCharacters() {
//		int[] typingKeys = getTypingKeys();
		
		if(keyPresses.length == 0 || keyUpdate) {
			remainingTicks = 0;
			keyHeld = false;
			keyUpdate = false;
		}else {
			
			if(remainingTicks <= 0) {
				remainingTicks = keyHeld ? speedTicksPerCharacter : ticksPerCharacter;
				
				keyHeld = true;
				
				return getTypingKeys();
			}
			
			remainingTicks--;
			
		}
		
		return new int[0];
	}
	
	private static int[] getTypingKeys() {
//		System.out.print("-->" );
//		for(Integer i : keyPresses) {
//			System.out.print(" " + (char)i.intValue());
//		}
//		System.out.println();
		List<Integer> typingKeys = new ArrayList<Integer>();
		
		for(int keyCode : keyPresses) {
			int index = -1;
			
			switch(keyCode) {
			case BCK:
				index = 0;
				break;
			case DEL:
				index = 0;
				
				if(typingKeys.size() > 0) {
					if(typingKeys.get(0) == BCK) {
						index++;
					}
				}
				break;
			case SHFT:
				index = 0;
				
				if(typingKeys.size() > 0) {
					if(typingKeys.get(0) == BCK || typingKeys.get(0) == DEL) {
						index++;
						
						if(typingKeys.size() > 1) {
							if(typingKeys.get(1) == DEL) {
								index++;
							}
						}
					}
				}
				break;
			case LEFT:
				index = 0;
				break;
			case RIGHT:
				index = 0;
				break;
			default:
				
				if((65 <= keyCode && keyCode <= 97) || (45 <= keyCode && keyCode <= 57) || (keyCode == SPACE)) {
					index = typingKeys.size();
				}
				
				break;
			}
			
			if(index != -1) {
				typingKeys.add(index, keyCode);
			}
		}
		
		int[] keys = new int[typingKeys.size()];
		
		for(int i = 0; i < typingKeys.size(); i++) {
			keys[i] = typingKeys.get(i);
		}
		
		return keys;
	}
	
	public static boolean capsIsOn() {
		return Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
	}
	
}
