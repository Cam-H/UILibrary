package ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import ui.constraints.UiConstraint;
import ui.graphics.UiColours;

public class UiTextArea extends UiComponent {

	private String title;
	
	private int maxTextSize;
	private float margins;
	
	private Color textColour;
	
	public UiTextArea(UiConstraint constraints, String title) {
		super(constraints);
		
		this.title = title;
		
		maxTextSize = 32;
		margins = 0.1f;
		
		textColour = UiColours.BLACK;
		
	}
	
	public void setTextMargins(float margins) {
		this.margins = margins;
	}
	
	@Override
	public void render(Graphics2D g) {
		
		if(visibility != Visibility.VISIBLE) {
			return;
		}
		
		if(constraints == null) {
			System.err.println("Constraints not set for component!");
			return;
		}
		
		super.render(g);
		
		g.setColor(textColour);
				
		FontMetrics metrics;
		
		int x = constraints.getX();// - metrics.stringWidth(title) / 2;
		int y = constraints.getY();// - metrics.getHeight() / 2 + metrics.getAscent();
		
		int width = (int)(constraints.getWidth() * (1 - margins));
		int height = (int)(constraints.getHeight() * (1 - margins));
		
		int volume = (int)(width * height * 0.8f);//Safety factor
		
		int fontSizeModifier = 0;
		
		int textWidth;
		int textHeight;
		
		do {
			g.setFont(new Font("TimesRoman", Font.PLAIN, maxTextSize - fontSizeModifier));
			metrics = g.getFontMetrics();
			
			textWidth = metrics.stringWidth(title);
			textHeight = (int)((metrics.getHeight() / 2 + metrics.getAscent()) / 1.8f);
			
			fontSizeModifier++;
						
		}while(textWidth * textHeight > volume);
		
		x -= width / 2;
		y -= height / 2 - textHeight;
		
		int xOffset = metrics.stringWidth(" ");
		int yOffset = 0;
				
		int i = 0;
		int nextSpace = 0;
		
		boolean spacesRemain = true;
		
		while(i < title.length() - 1) {
			if(!(xOffset == 0 && title.substring(i, i + 1).equals(" "))) {
				if(spacesRemain) {
					if(title.charAt(i) == ' ') {
						for(int j = i + 1; j < title.length(); j++) {
							if(title.charAt(j) == ' ') {
								nextSpace = j - i;
								break;
							}
						}
						
						if(nextSpace <= 0) {
							spacesRemain = false;

							if(xOffset + metrics.stringWidth(title.substring(i)) > width) {
								xOffset = 0;
								yOffset += textHeight;
							}
						}
					}
				}
				
				
				if(xOffset > width) {
					xOffset = 0;
					yOffset += textHeight;
				}else if(nextSpace > 0 && spacesRemain) {
					if(xOffset + metrics.stringWidth(title.substring(i, i + nextSpace)) > width) {
						xOffset = 0;
						yOffset += textHeight;
					}
				}
				
				g.drawString(title.substring(i, i + 1), x + xOffset, y + yOffset);
				
				xOffset += metrics.stringWidth(title.substring(i, i + 1));
				
			}
			
			nextSpace--;
			i++;
		}
		
	}
	
	@Override
	public void render(Graphics2D g, UiContainer container) {
		render(g);
	}
	
	@Override
	public void assignBaseColour(Graphics2D g) {
		g.setColor(UiColours.WHITE);		
	}

}
