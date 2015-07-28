package flash.display;

public class DisplayObjectContainer extends InteractiveObject {
	public DisplayObject addChild(DisplayObject child) {
		child._graph = this._graph;
		return null;
	}
}
