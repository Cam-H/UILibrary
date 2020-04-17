package ui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ui.constraints.UiConstraint;
import ui.control.UiThread;
import ui.graphics.UiColours;
import ui.io.UiKeys;
import ui.lang.English;
import ui.lang.Language;
import ui.text.Alignment;
import ui.transitions.UiTransition;

public class UiTextField extends UiLabel {
	
	public static final int ALPHABETIC = 0;
	public static final int NUMERIC = 1;
	public static final int ALPHANUMERIC = 2;
	
	private int textType;
	private Language lang;
	
	private boolean stopTypingOutsideField;
	private int maxCharCount;
	
	private String placeholder;
	
	private BufferedImage textRender;
	
//	private boolean utf8;
	
	private boolean locked;
	
	private int[] keyPresses;
	private int[] selectionRange;
	private int px;
	
	private boolean doubleClicked;
	
	public UiTextField(UiConstraint constraints) {
		this(constraints, "");
	}
	
	public UiTextField(UiConstraint constraints, String title) {
		super(constraints, title);
		
		textType = ALPHANUMERIC;
		lang = new English();
		
		textAlignment = Alignment.LEFT;
		stopTypingOutsideField = true;
		
		placeholder = "";
		
		locked = false;
		
		selectionRange = null;
		
		textRender = null;
		
		keyPresses = new int[] {};
		px = -1;
		
		doubleClicked = false;
		
		maxCharCount = -1;
		
	}

//	public void enableUTF8() {
//		utf8 = true;
//	}
//	
//	public void disableUTF8() {
//		utf8 = false;
//	}
	
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
	public void hover(int px, int py, UiContainer container) {
		super.hover(px, py, container);
		
		if(hovered) {
			UiThread.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		}
		
		this.px = px;
	}
	
	@Override
	public void deselect() {
		if(doubleClicked) {
			doubleClicked = false;
			return;
		}
		
		if(selected && !locked) {
			if(hovered) {
				if(selectionRange != null) {
					if(title.length() == 0 || textRender == null) {
						selectionRange = new int[] {title.length()};
					}else {
						Graphics g = textRender.getGraphics();
						g.setFont(font);
						
						FontMetrics fm = g.getFontMetrics();
						
						int width = getWidth();

						int bevel = getBevel(width, getHeight());
						
						int rx = px - getX() + width / 2 - bevel;
						
						int i = 0;
						int stringWidth = fm.stringWidth(title.substring(0, 1));
						
						if(rx > stringWidth / 2) {
							for(i = 1; i < title.length(); i++) {
								int charWidth = fm.stringWidth(String.valueOf(title.charAt(i)));

								if(rx < stringWidth + charWidth / 2) {
									break;
								}
								
								stringWidth += charWidth;
							}
						}
						
						
						selectionRange = new int[] {i};						
					}
				}else {
					selectionRange = new int[] {0, title.length()};
				}
			}
		}
		
		super.deselect();
	}
	
	@Override
	public void doubleClick() {
		if(selectionRange != null) {
			selectionRange = new int[] {0, title.length()};
			
			doubleClicked = true;
		}
	}
	
	public void setLanguage(Language lang) {
		this.lang = lang;
	}
	
	public void enableTypingBeyondField() {
		stopTypingOutsideField = false;
	}
	
	public void disableTypingBeyondField() {
		stopTypingOutsideField = true;
	}
	
	public void setMaxCharCount(int maxCharCount) {
		this.maxCharCount = maxCharCount;
	}
	
	public boolean isSelected() {
		return selectionRange != null;
	}
	
	@Override
	public void update() {
		super.update();
		
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
				if(title.length() > 0) {
					if(selectionRange.length == 2) {
						title = title.substring(0, selectionRange[0]) + title.substring(selectionRange[1]);
						selectionRange = new int[] {0};
					}else if(selectionRange[0] < title.length()){
						title = title.substring(0, selectionRange[0]) + title.substring(selectionRange[0] + 1);
					}
				}
				break;
			case UiKeys.LEFT:
				if(title.length() > 0) {
					if(selectionRange.length == 2) {
						selectionRange = new int[] {selectionRange[0]};
					}else if(selectionRange[0] - 1 >= 0) {
						selectionRange[0] = selectionRange[0] - 1;
					}
				}
				break;
			case UiKeys.RIGHT:
				if(title.length() > 0) {
					if(selectionRange.length == 2) {
						selectionRange = new int[] {selectionRange[1]};
					}else if(selectionRange[0] < title.length()) {
						selectionRange[0] = selectionRange[0] + 1;
					}
				}
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
			
			if(title.length() == maxCharCount && maxCharCount != -1) {
				return;
			}
			
			if(index != -1) {
				int key = getUniqueKey(keyPresses, this.keyPresses);

				if(key == -1) {
					key = keyPresses[index];
				}
				
				if(48 <= key && key <= 57) {
					if(textType == ALPHABETIC) {
						return;
					}
				}else { 
					if(textType == NUMERIC) {
						return;
					}
					
					if(textType == ALPHABETIC) {
						if(key < 65 || key > 90) {
							return;
						}
					}
				}
				
				key += (65 <= key && key <= 97) ? (UiKeys.capsIsOn() ^ shftOn ? 0 : 32) : 0;//Handles letters
				
				//Handle special characters
				if(shftOn) {
					key = attemptSpecialCharacterConversion(key);
				}
				
				String temp = title;
				
				title = title.substring(0, selectionRange[0])
						+ String.valueOf((char)key)
						+ title.substring((selectionRange.length == 2 ? selectionRange[1] : selectionRange[0]));

				//Make sure that the text does not overflow out of the textfield
				if(stopTypingOutsideField && temp.length() < title.length() && textRender != null) {
					Graphics g = textRender.getGraphics();
					g.setFont(font);
					
					FontMetrics fm = g.getFontMetrics();
					
					int width = getWidth();

					int bevel = getBevel(width, getHeight());
										
					if(width - bevel * 2 < fm.stringWidth(title)) {
						title = temp;
						
						selectionRange = new int[] {selectionRange[0] - 1};
					}
				}
				
				selectionRange = new int[] {1 + selectionRange[0]};
			}
		}
		
		this.keyPresses = keyPresses;
		
		int preConversionLength = title.length();
		title = lang.convert(title);

		if(preConversionLength != title.length() && title.length() != 0) {
			selectionRange = new int[] {selectionRange[0] + title.length() - preConversionLength};
		}

	}
	
	private int getUniqueKey(int[] arr1, int[] arr2) {
		int key = -1;
		
		for(int i = 0; i < arr1.length; i++) {
			boolean sharedKey = false;
			
			if(arr1[i] == UiKeys.SHFT) {
				continue;
			}
			
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
	
	private int attemptSpecialCharacterConversion(int key) {
		switch(key) {
		case 48://0
			return 41;
		case 49://1
			return 33;
		case 50://2
			return 64;
		case 51://3
			return 35;
		case 52://4
			return 36;
		case 53://5
			return 37;
		case 54://6
			return 94;
		case 55://7
			return 38;
		case 56://8
			return 42;
		case 57://9
			return 40;	
		}
		
		return key;
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
		
		if(this.title.isEmpty()) {
			bufferGraphics.setColor(new Color(0x999999));
		}
		
		drawTextOnBuffer(buffer, bufferGraphics, title, (this.title.isEmpty() ? null : selectionRange));
		
		renderBuffer(buffer, container, metrics, title, g);
		
		this.textRender = buffer;
	}
	
	@Override
	public void assignBaseColour(Graphics2D g) {
		g.setColor(UiColours.WHITE);		
	}
	
}
