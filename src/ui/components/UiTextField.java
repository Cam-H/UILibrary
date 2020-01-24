package ui.components;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ui.constraints.UiConstraint;
import ui.graphics.UiColours;
import ui.io.UiKeys;
import ui.lang.English;
import ui.lang.Language;
import ui.text.Alignment;
import ui.transitions.UiTransition;

public class UiTextField extends UiLabel {
	
	private Language lang;
	
	private int maxCharCount;
	
	private String placeholder;
	
	private boolean utf8;
	
	private boolean locked;
	
	public UiTextField(UiConstraint constraints) {
		this(constraints, "");
	}
	
	public UiTextField(UiConstraint constraints, String title) {
		super(constraints, title);
		
		lang = new English();
		
		textAlignment = Alignment.LEFT;
		maxCharCount = 15;
		
		placeholder = "";
		
		locked = false;
	}

	public void enableUTF8() {
		utf8 = true;
	}
	
	public void disableUTF8() {
		utf8 = false;
	}
	
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
	
	public void lock() {
		locked = true;
	}
	
	public void unlock() {
		locked = false;
	}
	
	public void setLanguage(Language lang) {
		this.lang = lang;
	}
	
	@Override
	public void update() {
		transitions = new UiTransition();
		
		if(locked) {
			return;
		}
		
		int[] keyPresses = UiKeys.getNextCharacters();
		
		boolean shftOn = false;
		int index = -1;
		
		if(keyPresses.length != 0) {
			switch(keyPresses[0]) {
			case UiKeys.BCK:
				if(title.length() > 0) {
					title = title.substring(0, title.length() - 1);
				}
				break;
			case UiKeys.DEL:
				
				break;
			case UiKeys.SHFT:
				shftOn = true;
				
				if(keyPresses.length > 1) {
					index = 1;
				}
				
				break;
			default:
				index = 0;
				break;
			}
			
			if(index != -1) {
				if(title.length() < maxCharCount) {
					int key = keyPresses[index];
					
					key += (65 <= key && key <= 97) ? (UiKeys.capsIsOn() ^ shftOn ? 0 : 32) : 0;//Handles letters
//					key += (49 <= key && key <= 57) ? (shftOn ? -16 : 0) : 0;
					
					title = title + String.valueOf((char)key);
				}
			}
		}
		
		title = lang.convert(title);
		
//		title = title + String.valueOf((char)code);
//				
//		if(UiKeys.keyIsPressed(8)) {
//			if(title.length() > 0) {
//				title = title.substring(0, title.length() - 2);
//			}
//		}
	}
	
	@Override
	public void render(Graphics2D g, UiContainer container) {
		
		super.render(g, container);
		
		String title = this.title;
		
		if(title.isEmpty()) {
			title = placeholder;
			
			if(placeholder.length() == 0) {
				return;
			}
		}
		
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics();

		BufferedImage buffer = createBuffer(metrics, title);
		Graphics2D bufferGraphics = prepareBufferGraphics(buffer);
		
		if(title.equalsIgnoreCase(placeholder)) {
			bufferGraphics.setColor(new Color(0x999999));
		}
		
		drawTextOnBuffer(buffer, bufferGraphics, title);
		
		renderBuffer(buffer, container, metrics, title, g);
	}
	
	@Override
	public void assignBaseColour(Graphics2D g) {
		g.setColor(UiColours.WHITE);		
	}
	
}
