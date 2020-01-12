package org.papervision3d.example;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.papervision3d.cameras.Camera3D;
import org.papervision3d.materials.BitmapAssetMaterial;
import org.papervision3d.objects.Ase;
import org.papervision3d.objects.DisplayObject3D;
import org.papervision3d.objects.Plane;
import org.papervision3d.scenes.Scene3D;

import flash.display.IBitmapDrawable;
import flash.display.Sprite;
import flash.events.Event;
import flash.port.FlashSpriteWindow;
import flash.port.FlashWindow;

public class Helloworld extends Sprite {
	private Sprite container;
	private Scene3D scene;
	private Camera3D camera;
	private Ase sphere;

	private static class Earth implements IBitmapDrawable {
		@Override
		public BufferedImage _getBitmap() {
            BufferedImage img = null;
			try {
                img = ImageIO.read(new File("Earth.jpg"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
			return img;
		}
	}

	private static class Space implements IBitmapDrawable {
		@Override
		public BufferedImage _getBitmap() {
            BufferedImage img = null;
			try {
                img = ImageIO.read(new File("Space.jpg"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
			return img;
		}
	}
	static {
		BitmapAssetMaterial._setDefinitionByName("Earth", new Earth());
		BitmapAssetMaterial._setDefinitionByName("Space", new Space());
	}
	
	public Helloworld() {
		super();
		
		init3D();
		this.addEventListener(Event.ENTER_FRAME, "loop3D");
	}

	private void init3D() {
		this.container = new Sprite();
		this.addChild(container);
		container.setX(320);
		container.setY(240);
		scene = new Scene3D(container);
		camera = new Camera3D();
		if (false) {
			addEarth();
		}
		addSpace();
	}
	
	private void addEarth() {
		BitmapAssetMaterial materialEarth = new BitmapAssetMaterial("Earth");
		sphere = new Ase(materialEarth, "world.ase", 0.5);
		sphere.setRotationX(45);
		sphere.yaw(-30);
		scene.addChild(sphere);
	}
	
	private void addSpace() {
		BitmapAssetMaterial materialSpace = new BitmapAssetMaterial("Space");
		if (false) {
			DisplayObject3D plane = new Plane(materialSpace, 6400, 4800, 8, 8);
			plane.setZ(500);
			scene.addChild(plane);
		} else {
			DisplayObject3D plane = new Plane(materialSpace, 640 * 4, 480 * 4, 1, 1);
			scene.addChild(plane);			
		}
	}	
	
	private void loop3D(Event event) {
		if (false) {
			camera.setX(-container.getMouseX() / 4);
			camera.setY(container.getMouseY() / 3);
			sphere.yaw( /*0.2*/ 20 );
		} else {
			camera.setX(-container.getMouseX() / 4);
			camera.setY(container.getMouseY() / 3);
		}
		scene.renderCamera(camera);
	}

	public void _onEvent(String listener, Event event) {
		if (listener != null) {
			if ("loop3D".equals(listener)) {
				loop3D(event);
			}
		}
	}
	
	public static void main(String[] args) {
		FlashWindow win = new FlashWindow();
		win.setAdapter(new FlashSpriteWindow(new Helloworld()));
		win.start();
	}
}
