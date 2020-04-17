package ui.math;

import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ui.graphics.UiColours;

public class Graph {
	
	private List<DataSet> dataSets;
	
	private double xScale;
	private double yScale;
	
	private boolean showSecondaryAxis;
	private boolean showTertiaryAxis;
	
	private int secondAxisOffset = 100;
	
	public Graph() {
		this(new DataSet(new PolynomialFunction(new ArrayList<>(Arrays.asList(1d, 2d, 0d, 0d, 0d))), -100, 100, 0.1));
		
		init();
		
		xScale = 10;
		yScale = 10;
	}
	
	public Graph(DataSet dataSet) {
		this(new ArrayList<DataSet>(Arrays.asList(dataSet)));
	}
	
	public Graph(List<DataSet> dataSets) {
		this.dataSets = dataSets;
		
		init();
	}
	
	private void init() {
		xScale = yScale = 1;
		
		showSecondaryAxis = showTertiaryAxis = true;
	}
	
	public void addDataSet(DataSet dataSet) {
		dataSets.add(dataSet);
	}
	
	public void setScale(double xScale, double yScale) {
		this.xScale = xScale;
		this.yScale = yScale;
	}
	
	public double getXScale() {
		return xScale;
	}
	
	public double getYScale() {
		return yScale;
	}
	
	public void render(BufferedImage buffer, int xOffset, int yOffset) {

		Graphics2D bufferGraphics = (Graphics2D)buffer.getGraphics();
		bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
		int width = buffer.getWidth();
		int height = buffer.getHeight();
		
		int cx = width / 2;
		int cy = height / 2;
				
		bufferGraphics.translate(xOffset, yOffset);
		
		//Draw secondary axis
		if(showSecondaryAxis) {
			bufferGraphics.setColor(UiColours.GRAY);
			
			FontMetrics fm = bufferGraphics.getFontMetrics();
			String msg = "";
			
			double secondAxisOffset = this.secondAxisOffset * Math.pow(2, -(int)(Math.log(xScale) / Math.log(2)));
			
			for(int i = 1; i < (width / 2 + xOffset) / (secondAxisOffset * xScale) + 1; i++) {
				bufferGraphics.drawLine(cx - (int)(i * secondAxisOffset * xScale), -yOffset, cx - (int)(i * secondAxisOffset * xScale), height - yOffset);
				msg = "-" + String.valueOf(i * secondAxisOffset);
				
				if(yOffset > height / 2 - fm.getAscent()) {
					bufferGraphics.drawString(msg, cx - (int)(i * secondAxisOffset * xScale) - fm.stringWidth(msg) / 2, -yOffset + height - 1);
				}else if(yOffset < -height / 2) {
					bufferGraphics.drawString(msg, cx - (int)(i * secondAxisOffset * xScale) - fm.stringWidth(msg) / 2, -yOffset + fm.getAscent());
				}else {
					bufferGraphics.drawString(msg, cx - (int)(i * secondAxisOffset * xScale) - fm.stringWidth(msg) / 2, cy + (fm.getHeight() - fm.getDescent()));
				}
			}
			
			for(int i = 1; i < (width / 2 - xOffset) / (secondAxisOffset * xScale) + 1; i++) {
				bufferGraphics.drawLine(cx + (int)(i * secondAxisOffset * xScale), -yOffset, cx + (int)(i * secondAxisOffset * xScale), height - yOffset);
				msg = String.valueOf(i * secondAxisOffset);
				
				if(yOffset > height / 2 - fm.getAscent()) {
					bufferGraphics.drawString(msg, cx + (int)(i * secondAxisOffset * xScale) - fm.stringWidth(msg) / 2, -yOffset + height - 1);
				}else if(yOffset < -height / 2) {
					bufferGraphics.drawString(msg, cx + (int)(i * secondAxisOffset * xScale) - fm.stringWidth(msg) / 2, -yOffset + fm.getAscent());
				}else {
					bufferGraphics.drawString(msg, cx + (int)(i * secondAxisOffset * xScale) - fm.stringWidth(msg) / 2, cy + (fm.getHeight() - fm.getDescent()));
				}
				
			}
			
			secondAxisOffset = this.secondAxisOffset * Math.pow(2, -(int)(Math.log(yScale) / Math.log(2)));
			
			for(int i = 1; i < (height / 2 + yOffset) / (secondAxisOffset * yScale) + 1; i++) {
				bufferGraphics.drawLine(-xOffset, cy - (int)(i * secondAxisOffset * yScale), width - xOffset, cy - (int)(i * secondAxisOffset * yScale));
				msg = String.valueOf(i * secondAxisOffset);
				
				if(xOffset > width / 2) {
					bufferGraphics.drawString(msg, -xOffset + width - fm.stringWidth(msg + " "), cy - (int)(i * secondAxisOffset * yScale) - 1);
				}else if(xOffset < -width / 2 + fm.stringWidth(msg + " ")) {
					bufferGraphics.drawString(msg, -xOffset + fm.stringWidth(" "), cy - (int)(i * secondAxisOffset * yScale) - 1);
				}else {
					bufferGraphics.drawString(msg, cx - fm.stringWidth(msg + " "), cy - (int)(i * secondAxisOffset * yScale) - 1);
				}
			}
			
			for(int i = 1; i < (height / 2 - yOffset) / (secondAxisOffset * yScale) + 1; i++) {
				bufferGraphics.drawLine(-xOffset, cy + (int)(i * secondAxisOffset * yScale), width - xOffset, cy + (int)(i * secondAxisOffset * yScale));
				msg = "-" + String.valueOf(i * secondAxisOffset);
				
				if(xOffset > width / 2) {
					bufferGraphics.drawString(msg, -xOffset + width - fm.stringWidth(msg + " "), cy + (int)(i * secondAxisOffset * yScale) - 1);
				}else if(xOffset < -width / 2 + fm.stringWidth(msg + " ")) {
					bufferGraphics.drawString(msg, -xOffset + fm.stringWidth(" "), cy + (int)(i * secondAxisOffset * yScale) - 1);
				}else {
					bufferGraphics.drawString(msg, cx - fm.stringWidth(msg + " "), cy + (int)(i * secondAxisOffset * yScale) - 1);
				}
			}
		}
		
		bufferGraphics.setColor(UiColours.BLACK);
		bufferGraphics.setStroke(new BasicStroke(2));
		
		//Draw primary axis
		bufferGraphics.drawLine(cx, cy, width - xOffset, cy);
		bufferGraphics.drawLine(cx, cy, -xOffset, cy);
		bufferGraphics.drawLine(cx, cy, cx, height - yOffset);//Bottom
		bufferGraphics.drawLine(cx, cy, cx, -yOffset);		

		bufferGraphics.setStroke(new BasicStroke(3));


		for(DataSet dataSet : dataSets) {
			List<Point> points = dataSet.getData();

			bufferGraphics.setColor(dataSet.getRenderColour());
			
			if(dataSet.isContinuous()) {
				for(int i = 0; i < points.size() - 1; i++) {
					bufferGraphics.drawLine((int)(points.get(i).getX() * xScale) + cx, -(int)(points.get(i).getY() * yScale) + cy, (int)(points.get(i + 1).getX() * xScale) + cx, -(int)(points.get(i + 1).getY() * yScale) + cy);
				}
			}
		}

				
		bufferGraphics.dispose();
	}

}
