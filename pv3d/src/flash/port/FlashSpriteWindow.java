package flash.port;

import flash.display.Sprite;
import flash.events.Event;

public class FlashSpriteWindow extends FlashWindowAdapter {
	private Sprite mSprite;
	
	public FlashSpriteWindow(Sprite sprite) {
		this.mSprite = sprite;
	}
	
	@Override
	public boolean onCreate() {
		super.onCreate();
		FlashWindow w = this.getWindow();
		this.mSprite._graph._window = w;
		this.mSprite._graph._graph = w.getBufGraph();
		return false;
	}
	
	@Override
	public boolean onIdle(int count) {
		if (mSprite != null) {
			mSprite._displatchEvent(Event.ENTER_FRAME);
		}
		return super.onIdle(count);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
