package org.papervision3d.example;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.papervision3d.cameras.Camera3D;
import org.papervision3d.materials.BitmapAssetMaterial;
import org.papervision3d.materials.ColorMaterial;
import org.papervision3d.materials.MaterialsList;
import org.papervision3d.objects.Ase;
import org.papervision3d.objects.DisplayObject3D;
import org.papervision3d.objects.Plane;
import org.papervision3d.scenes.Scene3D;

import flash.display.IBitmapDrawable;
import flash.display.Sprite;
import flash.events.Event;
import flash.events.KeyboardEvent;
import flash.port.FlashSpriteWindow;
import flash.port.FlashWindow;
import flash.ui.Keyboard;

public class Focus extends Sprite {
	private Sprite container;
	private Scene3D scene;
	private Camera3D camera;
	private DisplayObject3D rootNode;
	private Map<String, BitmapAssetMaterial> focusMaterials = new HashMap<String, BitmapAssetMaterial>() {
		private static final long serialVersionUID = 1L; 
		{
			this.put("materialBody", new BitmapAssetMaterial("FocusBody.png"));
			this.put("materialWheel", new BitmapAssetMaterial("Wheel.png"));
		}
	};
	private double topSpeed = 0;
	private double topSteer = 0;
	private double speed = 0;
	private double steer = 0;
	private boolean keyRight = false;
	private boolean keyLeft = false;
	private boolean keyForward = false;
	private boolean keyReverse = false;

	public Focus() {
		super();
		
		getStage().setQuality("MEDIUM");
		getStage().setScaleMode("noScale");
		getStage().addEventListener(KeyboardEvent.KEY_DOWN, "keyDownHandler");
		getStage().addEventListener(KeyboardEvent.KEY_UP, "keyUpHandler");
		this.addEventListener(Event.ENTER_FRAME, "loop3D");
		init3D();
	}

	private void init3D() {
		this.container = new Sprite();
		addChild(this.container);
		this.container.setX(500);
		this.container.setY(300);
		this.scene = new Scene3D(this.container);
		camera = new Camera3D();
		camera.setX(3000);
		camera.setZ(-300);
		camera.zoom = 10;
		camera.focus = 100;
		rootNode = scene.addChild(new DisplayObject3D("rootNode"));
		rootNode.addCollada("Focus.dae", new MaterialsList(focusMaterials));
		DisplayObject3D plane = rootNode.addChild(new Plane(new ColorMaterial(0x333333), 1000, 1000, 8, 8), "Plane");
		plane.setRotationX(-90);
		plane.setY(-25);
	}
	

	private void keyDownHandler(KeyboardEvent event) {
		switch (event.getKeyCode()) {
			case 'W':
			case Keyboard.UP:
				keyForward = true;
				keyReverse = false;
				break;

			case 'S':
			case Keyboard.DOWN:
				keyReverse = true;
				keyForward = false;
				break;

			case 'A':
			case Keyboard.LEFT:
				keyLeft = true;
				keyRight = false;
				break;

			case 'D':
			case Keyboard.RIGHT:
				keyRight = true;
				keyLeft = false;
				break;
		}
	}
	
	private void keyUpHandler(KeyboardEvent event) {
		switch (event.getKeyCode()) {
			case 'W':
			case Keyboard.UP:
				keyForward = false;
				break;

			case 'S':
			case Keyboard.DOWN:
				keyReverse = false;
				break;

			case 'A':
			case Keyboard.LEFT:
				keyLeft = false;
				break;

			case 'D':
			case Keyboard.RIGHT:
				keyRight = false;
				break;
		}
	}

	private void driveCar() {
		if (keyForward) {
			topSpeed = 50;
		} else if (keyReverse) {
			topSpeed = -20;
		} else {
			topSpeed = 0;
		}
		speed -= (speed - topSpeed) / 10;
		if (keyRight) {
			if (topSteer < 45) {
				topSteer += 5;
			}
		} else if (keyLeft) {
			if (topSteer > -45) {
				topSteer -= 5;
			}
		} else {
			topSteer -= topSteer / 24;
		}
		steer -= (steer - topSteer) / 2;
	}
	
	private void updateCar(DisplayObject3D car) {
		DisplayObject3D steerFR = car.getChildByName("Steer_FR");
		DisplayObject3D steerFL = car.getChildByName("Steer_FL");
		steerFR.setRotationY(steer);
		steerFL.setRotationY(steer);
		DisplayObject3D wheelFR = steerFR.getChildByName("Wheel_FR");
		DisplayObject3D wheelFL = steerFL.getChildByName("Wheel_FL");
		DisplayObject3D wheelRR = car.getChildByName("Wheel_RR");
		DisplayObject3D wheelRL = car.getChildByName("Wheel_RL");
		double roll = speed / 2;
		wheelFR.roll(roll);
		wheelRR.roll(roll);
		wheelFL.roll(-roll);
		wheelRL.roll(-roll);
		car.yaw(speed * steer / 500);
		car.moveForward(speed);
	}
	
	private void loop3D(Event event) {
		camera.setZ(-300 + scene.container.getMouseX() * 5);
		camera.setY(Math.max(0, this.getMouseY()) * 5);
		DisplayObject3D car = this.rootNode.getChildByName("Focus");
		if (car != null) {
			DisplayObject3D plane = this.rootNode.getChildByName("Plane");
			if (car.hitTestObject(plane)) {
				plane.material.fillColor = 0xFFFFFF;
			} else {
				plane.material.fillColor = 0x333333;
			}
			driveCar();
			updateCar( car );
		}
		this.scene.renderCamera( camera );
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
		win.setAdapter(new FlashSpriteWindow(new Focus()));
		win.start();
	}
}
