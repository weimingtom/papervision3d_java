package flash.events;

public class KeyboardEvent extends Event {
	public static final String KEY_DOWN = "keyDown";
	public static final String KEY_UP = "keyUp";
	
	public KeyboardEvent(String type, boolean bubbles, boolean cancelable) {
		super(type, bubbles, cancelable);
	}

	public int getKeyCode() {
		return 0;
	}
}
