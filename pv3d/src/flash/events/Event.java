package flash.events;

public class Event {
	public String _type;
	public Object _target;
	
	public static final String ENTER_FRAME = "enterFrame";
	public static final String COMPLETE = "complete";
	
	public Event(String type, boolean bubbles, boolean cancelable) {
		this._type = type;
	}
	
	public Object getTarget() {
		return _target;
	}
}
