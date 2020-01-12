package org.papervision3d.example
{
	import flash.display.*;
	import flash.events.*;
	import flash.ui.Keyboard;

	import org.papervision3d.scenes.*;
	import org.papervision3d.objects.*;
	import org.papervision3d.cameras.*;
	import org.papervision3d.materials.*;
	import org.papervision3d.events.*;
	
	//[SWF(width = '1000', height = '600', backgroundColor = '0xcccccc')]
	[SWF(width='640', height='480', frameRate='30')]
	public class Focus extends Sprite
	{
		private var container:Sprite;
		private var scene:Scene3D;
		private var camera:Camera3D;
		private var rootNode:DisplayObject3D;
		private var focusMaterials:Object = {
			materialBody:new BitmapAssetMaterial("FocusBody.png"),
			materialWheel:new BitmapAssetMaterial("Wheel.png")
		};
		private var topSpeed:Number = 0;
		private var topSteer:Number = 0;
		private var speed:Number = 0;
		private var steer:Number = 0;
		private var keyRight:Boolean = false;
		private var keyLeft:Boolean = false;
		private var keyForward:Boolean = false;
		private var keyReverse:Boolean = false;
		
		public function Focus()
		{
			stage.quality = "MEDIUM";
			stage.scaleMode = "noScale";
			stage.addEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);
			stage.addEventListener(KeyboardEvent.KEY_UP, keyUpHandler);
			this.addEventListener(Event.ENTER_FRAME, loop3D);
			init3D();
		}
		
		private function init3D():void
		{
			this.container = new Sprite();
			addChild(this.container);
			this.container.x = 500;
			this.container.y = 300;
			this.scene = new Scene3D(this.container);
			camera = new Camera3D();
			camera.x = 3000;
			camera.z = -300;
			camera.zoom = 10;
			camera.focus = 100;
			rootNode = scene.addChild(new DisplayObject3D("rootNode"));
			rootNode.addCollada( "Focus.dae", new MaterialsList(focusMaterials ) );
			var plane :DisplayObject3D = rootNode.addChild(new Plane(new ColorMaterial(0x333333), 1000, 1000, 8, 8), "Plane");
			plane.rotationX = -90;
			plane.y = -25;
		}
		
		private function keyDownHandler(event:KeyboardEvent):void
		{
			switch (event.keyCode)
			{
				case "W".charCodeAt():
				case Keyboard.UP:
					keyForward = true;
					keyReverse = false;
					break;

				case "S".charCodeAt():
				case Keyboard.DOWN:
					keyReverse = true;
					keyForward = false;
					break;

				case "A".charCodeAt():
				case Keyboard.LEFT:
					keyLeft = true;
					keyRight = false;
					break;

				case "D".charCodeAt():
				case Keyboard.RIGHT:
					keyRight = true;
					keyLeft = false;
					break;
			}
		}
		
		private function keyUpHandler(event:KeyboardEvent):void
		{
			switch (event.keyCode)
			{
				case "W".charCodeAt():
				case Keyboard.UP:
					keyForward = false;
					break;

				case "S".charCodeAt():
				case Keyboard.DOWN:
					keyReverse = false;
					break;

				case "A".charCodeAt():
				case Keyboard.LEFT:
					keyLeft = false;
					break;

				case "D".charCodeAt():
				case Keyboard.RIGHT:
					keyRight = false;
					break;
			}
		}

		private function driveCar():void
		{
			if (keyForward)
			{
				topSpeed = 50;
			}
			else if (keyReverse)
			{
				topSpeed = -20;
			}
			else
			{
				topSpeed = 0;
			}
			speed -= (speed - topSpeed) / 10;
			if (keyRight)
			{
				if (topSteer < 45)
				{
					topSteer += 5;
				}
			}
			else if (keyLeft)
			{
				if (topSteer > -45)
				{
					topSteer -= 5;
				}
			}
			else
			{
				topSteer -= topSteer / 24;
			}
			steer -= (steer - topSteer) / 2;
		}
		
		private function updateCar(car:DisplayObject3D):void
		{
			var steerFR:DisplayObject3D = car.getChildByName("Steer_FR");
			var steerFL:DisplayObject3D = car.getChildByName("Steer_FL");
			steerFR.rotationY = steer;
			steerFL.rotationY = steer;
			var wheelFR:DisplayObject3D = steerFR.getChildByName("Wheel_FR");
			var wheelFL:DisplayObject3D = steerFL.getChildByName("Wheel_FL");
			var wheelRR:DisplayObject3D = car.getChildByName("Wheel_RR");
			var wheelRL:DisplayObject3D = car.getChildByName("Wheel_RL");
			var roll:Number = speed / 2;
			wheelFR.roll(roll);
			wheelRR.roll(roll);
			wheelFL.roll(-roll);
			wheelRL.roll(-roll);
			car.yaw(speed * steer / 500);
			car.moveForward(speed);
		}
		
		private function loop3D(event:Event):void
		{
			camera.z = -300 + scene.container.mouseX * 5;
			camera.y = Math.max(0, this.mouseY) * 5;
			var car:DisplayObject3D = this.rootNode.getChildByName("Focus");
			if (car)
			{
				var plane:DisplayObject3D = this.rootNode.getChildByName("Plane");
				if (car.hitTestObject(plane))
				{
					plane.material.fillColor = 0xFFFFFF;
				}
				else
				{
					plane.material.fillColor = 0x333333;
				}
				driveCar();
				updateCar( car );
			}
			this.scene.renderCamera( camera );
		}
	}
}

