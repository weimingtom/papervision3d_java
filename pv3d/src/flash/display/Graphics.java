package flash.display;

import java.awt.Color;

import flash.geom.Matrix;
import flash.port.FlashWindow;

public class Graphics {
	public FlashWindow _window;
	public java.awt.Graphics _graph;
	private double _currentX;
	private double _currentY;
	private double _lastX;
	private double _lastY;
	
	public double _offsetX;
	public double _offsetY;
	
	public void clear() {
		if (_graph != null) {
			_graph.clearRect(0, 0, 800, 600);
		}
	}
	
	public void beginBitmapFill (BitmapData bitmap, 
		Matrix matrix, boolean repeat, 
		boolean smooth) {
		
	}
	public void beginFill(int color, double alpha) {
		
	}
	
	
	public void lineStyle() {
		
	}
	public void lineStyle(double thickness, 
		int color, double alpha) {
	}
	public void lineStyle(double thickness, 
		int color, double alpha, boolean pixelHinting, 
		String scaleMode, String caps, 
		String joints, double miterLimit) {
		
	}
	
	public void endFill() {
		if (_graph != null) {
			_graph.setColor(Color.BLACK);
			_graph.drawLine(
				(int)_offsetX + (int)_currentX, 
				(int)_offsetY + (int)_currentY, 
				(int)_offsetX + (int)_lastX, 
				(int)_offsetY + (int)_lastY);
		}
	}
	
	public void lineTo(double x, double y) {
		if (_graph != null) {
			_graph.setColor(Color.BLACK);
			_graph.drawLine(
				(int)_offsetX + (int)x, 
				(int)_offsetY + (int)y, 
				(int)_offsetX + (int)_lastX, 
				(int)_offsetY + (int)_lastY);
		}
		_lastX = x;
		_lastY = y;
	}

	public void moveTo(double x, double y) {
		_currentX = x;
		_currentY = y;
		_lastX = x;
		_lastY = y;
	}
}
