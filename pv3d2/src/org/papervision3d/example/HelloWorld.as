package org.papervision3d.example 
{
	import flash.display.*;
	import flash.events.*;
	import org.papervision3d.scenes.*;
	import org.papervision3d.cameras.*;
	import org.papervision3d.objects.*;
	import org.papervision3d.materials.*;

	[SWF(width='640', height='480', frameRate='30')]
	public class HelloWorld extends Sprite
	{
		import Earth; Earth;
		import Space; Space;
		
		private var container:Sprite;
		private var scene:Scene3D;
		private var camera:Camera3D;
		private var sphere:Ase;
		
		public function HelloWorld()
		{
			init3D();
			this.addEventListener(Event.ENTER_FRAME, loop3D);
		}
		
		private function init3D():void
		{
			container = new Sprite();
			addChild(container);
			container.x = 320;
			container.y = 240;
			scene = new Scene3D(container);
			camera = new Camera3D();
			addEarth();
			addSpace();
		}
		
		private function addEarth():void
		{
			var materialEarth:BitmapAssetMaterial = new BitmapAssetMaterial("Earth");
			sphere = new Ase(materialEarth, "world.ase", 0.5);
			sphere.rotationX = 45;
			sphere.yaw(-30);
			scene.addChild(sphere);
		}
		
		private function addSpace():void
		{
			var materialSpace:BitmapAssetMaterial = new BitmapAssetMaterial("Space");
			var plane:DisplayObject3D = new Plane(materialSpace, 6400, 4800, 8, 8);
			plane.z = 500;
			scene.addChild(plane);
		}
		
		private function loop3D(event:Event):void
		{
			camera.x = -container.mouseX / 4;
			camera.y = container.mouseY / 3;
			sphere.yaw( /*0.2*/ 20 );
			scene.renderCamera( camera );
		}
	}
}
