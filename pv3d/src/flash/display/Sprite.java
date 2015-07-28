package flash.display;


public class Sprite extends DisplayObjectContainer {
	public Sprite() {
		this._graph = new Graphics();
	}
	
	public Graphics getGraphics() {
		return _graph;
	}
}
