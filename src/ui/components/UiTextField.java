package ui.components;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ui.components.UiComponent.Visibility;
import ui.constraints.UiConstraint;
import ui.graphics.UiColours;
import ui.io.UiKeys;
import ui.lang.English;
import ui.lang.Language;
import ui.math.UiMath;
import ui.text.Alignment;
import ui.transitions.UiTransition;

public class UiTextField extends UiLabel {
	
	public static final int ALPHABETIC = 0;
	public static final int NUMERIC = 1;
	public static final int ALPHANUMERIC = 2;
	
	private int textType;
	private Language lang;
	
	private int maxCharCount;
	
	private String placeholder;
	
	private boolean utf8;
	
	private boolean locked;
	
	private int[] selectionRange;
	
	public UiTextField(UiConstraint constraints) {
		this(constraints, "");
	}
	
	public UiTextField(UiConstraint constraints, String title) {
		super(constraints, title);
		
		textType = ALPHANUMERIC;
		lang = new English();
		
		textAlignment = Alignment.LEFT;
		maxCharCount = 15;
		
		placeholder = "";
		
		locked = false;
		
		selectionRange = null;
		
	}

	public void enableUTF8() {
		utf8 = true;
	}
	
	public void disableUTF8() {
		utf8 = false;
	}
	
	public void setTextType(int textType) {
		this.textType = textType;
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
	
	public void select() {
		if(!hovered) {
			selectionRange = null;
		}
		
		super.select();
	}
	
	@Override
	public void deselect() {
		if(selected && !locked) {
			if(hovered) {
				if(selectionRange != null) {
					selectionRange = new int[] {title.length()};
				}else {
					selectionRange = new int[] {0, title.length()};
				}
			}
		}
		
		super.deselect();
	}
	
	public void setLanguage(Language lang) {
		this.lang = lang;
	}
	
	public void setMaxCharCount(int maxCharCount) {
		this.maxCharCount = maxCharCount;
	}
	
	public boolean isSelected() {
		return selectionRange != null;
	}
	
	@Override
	public void update() {
		transitions = new UiTransition();

		if(locked || selectionRange == null) {
			return;
		}
		
		int[] keyPresses = UiKeys.getNextCharacters();
		
		boolean shftOn = false;
		int index = -1;
		
		if(keyPresses.length != 0) {
			switch(keyPresses[0]) {
			case UiKeys.BCK:
				if(title.length() > 0) {
					if(selectionRange.length == 2) {//Delete just the selected characters
						title = title.substring(0, selectionRange[0]) + title.substring(selectionRange[1]);
						selectionRange = new int[] {0};
					}else {
						title = title.substring(0, selectionRange[0] - 1) + title.substring(selectionRange[0]);
						selectionRange[0] = selectionRange[0] - 1;
					}
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
					
					if(48 <= key && key <= 57) {
						if(textType == ALPHABETIC) {
							return;
						}
					}else if(textType == NUMERIC) {
						return;
					}
					
					key += (65 <= key && key <= 97) ? (UiKeys.capsIsOn() ^ shftOn ? 0 : 32) : 0;//Handles letters
//					key += (49 <= key && key <= 57) ? (shftOn ? -16 : 0) : 0;
					
//					System.out.println(selectionRange[0] + " " + selectionRange[1] + " " + title.length());
					title = title.substring(0, selectionRange[0])
							+ String.valueOf((char)key)
							+ title.substring((selectionRange.length == 2 ? selectionRange[1] : (selectionRange[0] < title.length() - 1 ? selectionRange[0] + 1 : selectionRange[0])));
					selectionRange = new int[] {1 + selectionRange[0]};
					
				}
			}
		}
		
		int preConversionLength = title.length();
//		title = lang.convert(title);

//		if(preConversionLength != title.length() && title.length() != 0) {
//			textPointer = preConversionLength - title.length();
//		}
		
//		title = title + String.valueOf((char)code);
//				
//		if(UiKeys.keyIsPressed(8)) {
//			if(title.length() > 0) {
//				title = title.substring(0, title.length() - 2);
//			}
//		}
	}
	
//	public static void main(String[] args) {
//		String test = "abcdefghij";
//		
//		System.out.println(test.substring(3));
//	}
	
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
		
		if(selectionRange != null) {//Text field is active
			g.setColor(UiColours.BLACK);
			
			int x = getX();
			int y = getY();
			
			int width = getWidth();
			int height = getHeight();

			int bevel = getBevel(width, height);
			width -= bevel; height -= bevel;

			g.drawRect(x - width / 2, y - height / 2, width, height);
		}
		
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics();

		BufferedImage buffer = createBuffer(metrics, title);
		Graphics2D bufferGraphics = prepareBufferGraphics(buffer);
		
		if(title.equalsIgnoreCase(placeholder)) {
			bufferGraphics.setColor(new Color(0x999999));
		}
		
		drawTextOnBuffer(buffer, bufferGraphics, title, (this.title.isEmpty() ? null : selectionRange));
		
		renderBuffer(buffer, container, metrics, title, g);
	}
	
	@Override
	public void assignBaseColour(Graphics2D g) {
		g.setColor(UiColours.WHITE);		
	}
	
}
