package flash.events;

public class ProgressEvent extends Event {
	public static final String PROGRESS = "progress";
	
	public ProgressEvent(String type, boolean bubbles, boolean cancelable) {
		super(type, bubbles, cancelable);
	}

	public double getBytesLoaded() {
		return 0;
	}
	
	public void setBytesLoaded(int value) {
		
	}
	
	public double getBytesTotal() {
		return 0;
	}
	
	public void setBytesTotal(int value) {
		
	}
}
