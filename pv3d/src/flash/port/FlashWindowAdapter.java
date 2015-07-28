package flash.port;

public class FlashWindowAdapter {
	private FlashWindow mWindow;
	
	public void setWindow(FlashWindow window) {
		this.mWindow = window;
	}

	public FlashWindow getWindow() {
		return this.mWindow;
	}
	
	protected void onLButtonUp(FlashPoint point) {
		
	}
	
	protected void onLButtonDown(FlashPoint point) {
		
	}

	protected void onRButtonUp(FlashPoint point) {
		
	}

	protected void onMouseMove(FlashPoint point) {
		
	}

	protected boolean onIdle(int count) {
		return false;
	}
	
	protected boolean onCreate() {
		return true;
	}
	
	/**
	 * FIXME:unnecessary
	 */
	protected void onPaint() {
		
	}
	
	protected void onDestroy() {
		
	}
	
	protected void onKeyDown(int key) {
		
	}
}
