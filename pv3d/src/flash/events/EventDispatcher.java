package flash.events;

import java.util.HashMap;
import java.util.Map;

public class EventDispatcher  {
	private Map<String, String> _eventMap = new HashMap<String, String>();
	
	public void addEventListener(String type, String listener) {
		this.addEventListener(type, listener, false, 0, false);
	}
	
	public void addEventListener(String type, String listener, boolean useCapture, int priority, boolean useWeakReference) {
		_eventMap.put(type, listener);
	}
	
	public void _onEvent(String listener, Event event) {
		
	}
	
	public void _displatchEvent(String type) {
		String listener = _eventMap.get(type);
		if (listener != null) {
			//FIXME:
			_onEvent(listener, new Event(type, false, false));
		}
	}
	
	public boolean dispatchEvent(Event event) {
		String listener = _eventMap.get(event._type);
		if (listener != null) {
			//FIXME:
			_onEvent(listener, event);
		}
		return true;
	}
}
