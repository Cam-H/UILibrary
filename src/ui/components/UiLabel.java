package ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ui.constraints.UiConstraint;
import ui.graphics.UiColours;
import ui.text.Alignment;

public class UiLabel extends UiComponent {
	
	protected String title;
	
	protected Font font;
	protected Color textColour;
	
	protected boolean renderDropShadow;
	protected int xDrop;
	protected int yDrop;
	protected Color shadowColour;
	
	protected Alignment textAlignment;
	
	public UiLabel(UiConstraint constraints, String title) {
		super(constraints);
		
		this.title = title;
		
		font = new Font("TimesRoman", Font.PLAIN, 32);

		renderDropShadow = false;
		
		textColour = UiColours.BLACK;
		textAlignment = Alignment.CENTER;
	}
	
	public void setTextColour(Color textColour) {
		this.textColour = textColour;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void setTextSize(int textSize) {
		font = new Font(font.getFontName(), font.getStyle(), textSize);
	}
	
	public void setTextAlignment(Alignment textAlignment) {
		this.textAlignment = textAlignment;
	}
	
	public void setText(String title) {
		this.title = title;
	}
	
	public void addDropShadow() {
		addDropShadow(2, 2, UiColours.BLACK);
	}
	
	public void addDropShadow(int xDrop, int yDrop) {
		addDropShadow(xDrop, yDrop, UiColours.BLACK);
	}
	
	public void addDropShadow(Color shadowColour) {
		addDropShadow(2, 2, shadowColour);
	}
	
	public void addDropShadow(int xDrop, int yDrop, Color shadowColour) {
		this.xDrop = xDrop;
		this.yDrop = yDrop;
		
		this.shadowColour = shadowColour;
		
		renderDropShadow = true;
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
		
//		List<Str>
		
		BufferedImage buffer = createBuffer(metrics, title);
		Graphics2D bufferGraphics = prepareBufferGraphics(buffer);
		
		drawTextOnBuffer(buffer, bufferGraphics, title, null);
		
		renderBuffer(buffer, container, metrics, title, g);
	}
	
	protected BufferedImage createBuffer(FontMetrics metrics, String txt) {
		return new BufferedImage(metrics.stringWidth(txt + "  "), metrics.getHeight() / 2 + metrics.getAscent(), BufferedImage.TYPE_INT_ARGB);
	}
	
	protected Graphics2D prepareBufferGraphics(BufferedImage buffer) {
		Graphics2D bufferGraphics = (Graphics2D)buffer.getGraphics();
		
		bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		bufferGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		bufferGraphics.setFont(font);
		bufferGraphics.setColor(textColour);
		
		return bufferGraphics;
	}
	
	protected void drawTextOnBuffer(BufferedImage buffer, Graphics2D bufferGraphics, String txt, int[] selectionRange) {
		FontMetrics metrics = bufferGraphics.getFontMetrics();
		
		List<String> segments = getSegments(txt);
		int stringWidth = getStringWidth(bufferGraphics, segments);
		
		int x = buffer.getWidth() / 2 - stringWidth / 2;
		int height = metrics.getAscent();
		
		if(selectionRange != null) {
			if(selectionRange.length == 2) {
//				System.out.println(selectionRange[0] + " " + selectionRange[1]);
//				System.out.println("x: " + (x + metrics.stringWidth(txt.substring(0, selectionRange[0]))) + " " + (metrics.stringWidth(txt.substring(selectionRange[1])) - metrics.stringWidth(txt.substring(0, selectionRange[0]))));
				bufferGraphics.setColor(UiColours.BLUE);
				bufferGraphics.fillRect(x + metrics.stringWidth(txt.substring(0, selectionRange[0])), buffer.getHeight() / 2 - height / 2, buffer.getWidth(), height);
			}else {
				int barWidth = 3;
				
				bufferGraphics.setColor(UiColours.BLACK);
				bufferGraphics.fillRect(x + metrics.stringWidth(txt.substring(0, selectionRange[0])) - barWidth, buffer.getHeight() / 2 - height / 2, barWidth + (selectionRange[0] == 0 ? barWidth : 0), height);
			}
			
			bufferGraphics.setColor(UiColours.BLACK);
		}
		
		int y = metrics.getHeight() / 2 + height / 2;
		
		if(renderDropShadow) {
			FontRenderContext frc = bufferGraphics.getFontRenderContext();
			TextLayout textLayout = new TextLayout(title, font, frc);
			
			AffineTransform at = AffineTransform.getTranslateInstance(x, y);
			Shape outline = textLayout.getOutline(at);

	        at.setToTranslation(x + xDrop, y + yDrop);
	        outline = textLayout.getOutline(at);
	        bufferGraphics.setPaint(shadowColour);
	        bufferGraphics.fill(outline);
	        bufferGraphics.setPaint(textColour);
	        textLayout.draw(bufferGraphics, x, y);
	        
		}else {
			Font font = this.font;
			int xOffset = 0;
			
			for(String segment : segments) {
				bufferGraphics.setFont(new Font(font.getName(), getFontStyle(segment.charAt(0)), font.getSize()));
				
				bufferGraphics.drawString(segment.substring(1), x + xOffset, metrics.getHeight() / 2 + height / 2);
				xOffset += bufferGraphics.getFontMetrics().stringWidth(segment.substring(1));
			}
			
		}

		bufferGraphics.dispose();
	}
	
	protected int getStringWidth(Graphics2D bufferGraphics, List<String> segments) {
		int length = 0;
		
		Font font = this.font;
		
		for(String segment : segments) {
			int fontStyle = getFontStyle(segment.charAt(0));

			bufferGraphics.setFont(new Font(font.getName(), fontStyle, font.getSize()));
			
			FontMetrics metrics = bufferGraphics.getFontMetrics();
			
			length += metrics.stringWidth(segment.substring(1));
		}
		
		return length;
	}
	
	protected int getFontStyle(char ID) {
		switch(ID) {
		case '0':
			return Font.PLAIN;
		case '1':
			return Font.BOLD;
		case '2':
			return Font.ITALIC;
		}
		
		return Font.PLAIN;
	}
	
	protected List<String> getSegments(String txt) {
		List<String> segments = new ArrayList<String>();
		
		addSegments(segments, "<b>", txt);
		addSegments(segments, "<i>", txt);
		
		int index = 0;

		while(txt.length() > 0) {
			int i = txt.indexOf("<");			
			
			if(i == -1) {
				segments.add(0 + txt);
				return segments;
			}else {
				String tag = txt.substring(i, txt.indexOf(">") + 1);
				String endTag = tag.substring(0, 1) + "/" + tag.substring(1);
				
				String segment = txt.substring(0, i);
				segments.add(index + "-0" + segment);
				txt = txt.substring(i);
				
				index += segment.length();
				
				i = txt.indexOf(endTag);
				
				if(i == -1) {
					txt = "";
				}else {
					txt = txt.substring(i + endTag.length());
					index += i + endTag.length();
				}
				
			}
		}
		
		String[] arr = new String[segments.size()];
		for(int i = 0; i < arr.length; i++) {
			arr[i] = segments.get(i);
		}
		
		mergesort(arr, 0, arr.length - 1);
		
		//Remove the numbering that was used to sort the array
		for(int i = 0; i < segments.size(); i++) {
			segments.set(i, arr[i].substring(arr[i].indexOf("-") + 1));
		}
		
		return segments;
	}
	
	protected void mergesort(String[] arr, int l, int r) {
		if(l < r) {
			int m = l + (r - l) / 2;

			mergesort(arr, l, m);
			mergesort(arr, m + 1, r);

			merge(arr, l, m, r);
		}
	}
	
	protected void merge(String[] arr, int l, int m, int r) {
		
		int i, j, k;
		int n1 = m - l + 1;
		int n2 = r - m;
		
		/* create temp arrays */
		String[] L = new String[n1];
		String[] R = new String[n2];
		
		/* Copy data to temp arrays L[] and R[] */
		for(i = 0; i < n1; i++)
			L[i] = arr[l + i];
		for(j = 0; j < n2; j++)
			R[j] = arr[m + 1 + j];
		
		i = j = 0; k = l;
		while(i < n1 && j < n2) {
			if(Integer.parseInt(L[i].substring(0, L[i].indexOf("-"))) < Integer.parseInt(R[j].substring(0, R[j].indexOf("-")))) {
				arr[k] = L[i];
				i++;
			}else {
				arr[k] = R[j];
				j++;
			}
			
			k++;
		}
		
		while(i < n1) {
			arr[k] = L[i];
			i++;
			k++;
		}
		
		while(j < n2) {
			arr[k] = R[j];
			j++;
			k++;
		}
	}
	
	protected String addSegments(List<String> segments, String tag, String txt) {
		while(txt.contains(tag)) {
			int i = txt.indexOf(tag);
			int e = txt.indexOf(tag.substring(0, 1) + "/" + tag.substring(1));
			
			segments.add(i + "-" + getIDByTag(tag) + txt.substring(i + tag.length(), e));
			txt = txt.substring(0, i) + txt.substring(e + tag.length() + 1);
		}
		
		return txt;
	}
	
	protected int getIDByTag(String tag) {
		switch(tag) {
		case "<b>":
			return 1;
		case "<i>":
			return 2;
		default:
			return 0;
		}
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
