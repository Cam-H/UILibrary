package ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import ui.components.UiComponent.Visibility;
import ui.constraints.UiConstraint;
import ui.graphics.UiColours;
import ui.text.Alignment;

public class UiLabel extends UiComponent {

	protected String title;
	
	protected Font font;
	protected Color textColour;
	
	protected Alignment textAlignment;
	
	public UiLabel(UiConstraint constraints, String title) {
		super(constraints);
		
		this.title = title;
		
		font = new Font("TimesRoman", Font.PLAIN, 32);

		textColour = UiColours.BLACK;
		textAlignment = Alignment.CENTER;
	}
	
	public void setTextColour(Color textColour) {
		this.textColour = textColour;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void setTextAlignment(Alignment textAlignment) {
		this.textAlignment = textAlignment;
	}
	
	public void setText(String title) {
		this.title = title;
	}
	
	public String getText() {
		return title;
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
		
		g.setFont(font);
		g.setColor(textColour);
				
		FontMetrics metrics = g.getFontMetrics();
	
		int xOffset = constraints.getX();
		int yOffset = constraints.getY() - metrics.getHeight() / 2 + metrics.getAscent();
		
		switch(textAlignment) {
		case LEFT:
			xOffset -= constraints.getWidth() / 2f;
			break;
		case CENTER:
			xOffset -= metrics.stringWidth(title) / 2;
			break;
		case RIGHT:
			xOffset += constraints.getWidth() / 2f - metrics.stringWidth(title);
			break;
		}
		
		g.drawString(title, xOffset, yOffset);
	}
	
	@Override
	public void render(Graphics2D g, UiContainer container) {
		if(visibility != Visibility.VISIBLE) {
			return;
		}
		
		super.render(g);
		
		if(title.isEmpty()) {
			return;
		}
		
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics();
		
		BufferedImage buffer = createBuffer(metrics, title);
		Graphics2D bufferGraphics = prepareBufferGraphics(buffer);
		
		drawTextOnBuffer(buffer, bufferGraphics, title);
		
		renderBuffer(buffer, container, metrics, title, g);
	}
	
	protected BufferedImage createBuffer(FontMetrics metrics, String txt) {
		return new BufferedImage(metrics.stringWidth(txt), metrics.getHeight() / 2 + metrics.getAscent(), BufferedImage.TYPE_INT_ARGB);
	}
	
	protected Graphics2D prepareBufferGraphics(BufferedImage buffer) {
		Graphics2D bufferGraphics = (Graphics2D)buffer.getGraphics();
		bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		bufferGraphics.setFont(font);
		bufferGraphics.setColor(textColour);
		
		return bufferGraphics;
	}
	
	protected void drawTextOnBuffer(BufferedImage buffer, Graphics2D bufferGraphics, String txt) {
		FontMetrics metrics = bufferGraphics.getFontMetrics();
		
		bufferGraphics.drawString(txt, buffer.getWidth() / 2 - metrics.stringWidth(txt) / 2, metrics.getHeight() / 2 + metrics.getAscent() / 2);
		
		bufferGraphics.dispose();
	}
	
	protected void renderBuffer(BufferedImage buffer, UiContainer container, FontMetrics metrics, String txt, Graphics2D g) {
		
		int x = getX();
		int y = getY();
		
		int width = buffer.getWidth();
		int height = buffer.getHeight();
		
		int cx = container.getX();
		int cy = container.getY();
		
		int cWidth = container.getWidth();
		int cHeight = container.getHeight();
		
		int tx = Math.max(x - width / 2, cx - cWidth / 2);
		int ty = Math.max(y - height / 2, cy - cHeight / 2);
		
		int bx = Math.min(x + width / 2, cx + cWidth / 2);
		int by = Math.min(y + height / 2, cy + cHeight / 2);
		
		if(by < ty || bx < tx) {
			return;
		}
		
		int xOffset = 0;
		
		switch(textAlignment) {
		case LEFT:
			xOffset -= constraints.getWidth() / 2.025f - metrics.stringWidth(txt) / 2 - getBevel(getWidth(), getHeight());
			break;
		case RIGHT:
			xOffset += constraints.getWidth() / 2.025f - metrics.stringWidth(txt) / 2 - getBevel(getWidth(), getHeight());
			break;
		default:
			break;
		}
		
		
		
		g.drawImage(buffer, tx + xOffset, ty, bx + xOffset, by,
				tx - x + width / 2, ty - y + height / 2, bx - x - width / 2 + buffer.getWidth(), by - y - height / 2 + buffer.getHeight(), null);
		
	}
	
	@Override
	public void assignBaseColour(Graphics2D g) {
		super.assignBaseColour(g);
		
		if(g.getColor() == UiColours.GRAY) {
			g.setColor(UiColours.TRANSPARENT);
		}
	}

}
