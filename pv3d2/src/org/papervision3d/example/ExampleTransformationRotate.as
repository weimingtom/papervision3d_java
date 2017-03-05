package org.papervision3d.example {

	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.Sprite;
	import flash.events.Event;

	import org.papervision3d.materials.ColorMaterial;
	import org.papervision3d.objects.Plane;
	import org.papervision3d.scenes.Scene3D;
	import org.papervision3d.cameras.Camera3D;
	import org.papervision3d.materials.BitmapMaterial;
	
	public class ExampleTransformationRotate extends Sprite {

		private var plane:Plane;

		private var container:Sprite;
		private var scene:Scene3D;
		private var camera:Camera3D;
		
		[Embed(source="../../../../lib/test.jpg")]
		private var TestJPG:Class;
		
		public function ExampleTransformationRotate() {

			super();
			
			init3D();
			this.addEventListener(Event.ENTER_FRAME, render);
		} 

		private function init3D():void
		{
			container = new Sprite();
			addChild(container);
			container.x = 320;
			container.y = 240;
			scene = new Scene3D(container);
			camera = new Camera3D();

			var material:ColorMaterial = new ColorMaterial();
			material.doubleSided = true;
			material.fillColor = 0xFF0000;
			material.fillAlpha = 1.0;

			var backBuffer:BitmapData = new BitmapData(570, 570, false);
			backBuffer.lock();
			backBuffer.draw(new TestJPG());
			backBuffer.unlock();
			var material2:BitmapMaterial = new BitmapMaterial(backBuffer);
			material2.doubleSided = true;
			material.fillColor = 0xFF0000;
			material.fillAlpha = 1.0;
			
			plane = new Plane(material2, 600, 600, 1, 1);

			scene.addChild(plane);
		}
		
		
		private function render(event:Event):void {

			plane.rotationX += 4.35;
			plane.rotationY += 6.55;
			plane.rotationZ += 0.55;

			scene.renderCamera( camera );
		}

	}

}
