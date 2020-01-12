package flash.display;

import java.awt.Point;

import flash.events.EventDispatcher;

public class DisplayObject extends EventDispatcher {
	public Graphics _graph;
	
	private double x;
	private double y;
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
		_graph._offsetX = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
		_graph._offsetY = y;
	}
	
	private double _lastMouseX = 0;
	public double getMouseX() {
		Point b = this._graph._window.getMousePosition();
		if (b != null) {
			double result = (b != null ? b.getX() : 0) - x;
			System.out.println("result.x == " + result);
			_lastMouseX = result;
			return result;
		} else {
			return _lastMouseX;
		}
		
//		PointerInfo a = MouseInfo.getPointerInfo();
//		Point b = a.getLocation();
//		return b.getX() - x;
	}
	
	private double _lastMouseY = 0;
	public double getMouseY() {
		Point b = this._graph._window.getMousePosition();
		if (b != null) {
			double result = (b != null ? b.getY() : 0) - y;
			System.out.println("result.y == " + result);
			_lastMouseY = result;
			return result;
		} else {
			return _lastMouseY;
		}
		
//		PointerInfo a = MouseInfo.getPointerInfo();
//		Point b = a.getLocation();
//		return b.getY() - y;
	}
	
	public Stage getStage() {
		return null;
	}
}
