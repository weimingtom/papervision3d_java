package flash.display;

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
}
