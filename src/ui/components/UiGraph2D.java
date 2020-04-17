package ui.components;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ui.constraints.UiConstraint;
import ui.control.UiThread;
import ui.graphics.UiColours;
import ui.math.Graph;

public class UiGraph2D extends UiComponent {
	
	private Graph graph;
	
	protected boolean xScrollable;
	protected boolean yScrollable;	
	
	private int px;
	private int py;
	
	protected float xOffset;
	protected float yOffset;
	
	protected boolean xZoomable;
	protected boolean yZoomable;
	
	protected double xZoom;
	protected double yZoom;
	
	public UiGraph2D(UiConstraint constraints) {
		super(constraints);
		
		baseColour = UiColours.WHITE;
		
		graph = new Graph();
		
		xScrollable = yScrollable = true;
		
		xOffset = yOffset = 0;
		
		xZoom = graph.getXScale();
		yZoom = graph.getYScale();
		
		xZoomable = yZoomable = true;
	}
	
	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	
	public void setScrollable(boolean xScrollable, boolean yScrollable) {
		this.xScrollable = xScrollable;
		this.yScrollable = yScrollable;
	}
	
	public void setOffsets(float xOffset, float yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	public void setZoomable(boolean xZoomable, boolean yZoomable) {
		this.xZoomable = xZoomable;
		this.yZoomable = yZoomable;
	}
	
	public void setZoom(double xZoom, double yZoom) {
		this.xZoom = xZoom;
		this.yZoom = yZoom;
		
		graph.setScale(xZoom, yZoom);
	}
	
	@Override
	public void hover(int px, int py, UiContainer container) {
		super.hover(px, py, container);
		
		if(hovered) {
			UiThread.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
		
		if(selected) {
			if(xScrollable) {
				xOffset += (float)(px - this.px) / getWidth();
			}
			
			if(yScrollable) {
				yOffset += (float)(py - this.py) / getHeight();
			}
		}
		
		this.px = px;
		this.py = py;
		
	}
	
	public void scroll(int wheelRotation) {
		if(xZoomable) {
			xZoom -= (wheelRotation / 100f) * xZoom;
			
			if(xZoom < 0) {
				xZoom = 0;
			}
		}
		
		if(yZoomable) {
			yZoom -= (wheelRotation / 100f) * yZoom;
			
			if(yZoom < 0) {
				yZoom = 0;
			}
		}
		
		graph.setScale(xZoom, yZoom);
	}
	
	@Override
	public void render(Graphics2D g) {
		if(visibility != Visibility.VISIBLE) {
			return;
		}

		super.render(g);
	}
	
	@Override
	public void render(Graphics2D g, UiContainer container) {
		if(visibility != Visibility.VISIBLE) {
			return;
		}
		
		super.render(g, container);
		
		int cx = getX();
		int cy = getY();

		int width = getWidth();
		int height = getHeight();
		
		BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		graph.render(buffer, (int)(xOffset * width), (int)(yOffset * height));
		
		g.drawImage(buffer, cx - width / 2, cy - height / 2, null);
		

	}
	

}
