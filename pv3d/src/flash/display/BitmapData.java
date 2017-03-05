package flash.display;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class BitmapData {
	public BufferedImage _image;
	
	public BitmapData(int width, int height) {
		this(width, height, true, 0xffffffff);
	}
	public BitmapData(int width, int height, 
			boolean transparent) {
		this(width, height, transparent, 0xffffffff);
	}
	public BitmapData(int width, int height, 
		boolean transparent, int fillColor) {
		this._image = new BufferedImage(
			width, height, 
			BufferedImage.TYPE_INT_ARGB);
	}
	
	public void draw(IBitmapDrawable source) {
        Graphics2D g2d = this._image.createGraphics();
        g2d.drawImage(source._getBitmap(), 0, 0, null); //FIXME:
        g2d.dispose();        
	}
	
	public void lock() {
		
	}
	
	public void unlock() {
		
	}
	
	public void dispose() {
		
	}
	
	public int getWidth() {
		return 0;
	}
	
	public int getHeight() {
		return 0;
	}
}
