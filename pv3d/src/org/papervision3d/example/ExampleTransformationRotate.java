package org.papervision3d.example;

import org.papervision3d.cameras.Camera3D;
import org.papervision3d.materials.ColorMaterial;
import org.papervision3d.objects.Plane;
import org.papervision3d.scenes.Scene3D;

import flash.display.Sprite;
import flash.events.Event;
import flash.port.FlashSpriteWindow;
import flash.port.FlashWindow;

public class ExampleTransformationRotate extends Sprite {
	private Plane plane;

	private Sprite container;
	private Scene3D scene;
	private Camera3D camera;
	
	public ExampleTransformationRotate() {
		super();
		
		init3D();
		this.addEventListener(Event.ENTER_FRAME, "render");
	}

	private void init3D() {
		this.container = new Sprite();
		this.addChild(container);
		container.setX(320);
		container.setY(240);
		scene = new Scene3D(container);
		camera = new Camera3D();

		ColorMaterial material = new ColorMaterial();
		material.setDoubleSided(true);
		material.fillColor = 0xFF0000;
		material.fillAlpha = 1.0;

		plane = new Plane(material, 300, 300, 1, 1);

		scene.addChild(plane);
	}
	
	private void render(Event event) {
		plane.setRotationX(plane.getRotationX() + 4.35);
		plane.setRotationY(plane.getRotationY() + 6.55);
		plane.setRotationZ(plane.getRotationZ() + 0.55);

		scene.renderCamera(camera);
	}

	public void _onEvent(String listener, Event event) {
		if (listener != null) {
			if ("render".equals(listener)) {
				render(event);
			}
		}
	}
	
	public static void main(String[] args) {
		FlashWindow win = new FlashWindow();
		win.setAdapter(new FlashSpriteWindow(new ExampleTransformationRotate()));
		win.start();
	}
}
