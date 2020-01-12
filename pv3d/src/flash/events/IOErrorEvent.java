package flash.events;

public class IOErrorEvent extends Event {
	public static final String IO_ERROR = "ioError";
	
	public IOErrorEvent(String type, boolean bubbles, boolean cancelable) {
		super(type, bubbles, cancelable);
	}
}
