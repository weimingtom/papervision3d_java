package org.papervision3d.core.proto
{
	import org.papervision3d.core.Matrix3D;
	import org.papervision3d.core.Number3D;
	import org.papervision3d.objects.DisplayObject3D;
	
	public class CameraObject3D extends DisplayObject3D
	{
		private static var _flipY:Matrix3D = Matrix3D.scaleMatrix(1, -1, 1);
		public static var DEFAULT_POS:Number3D = new Number3D(0, 0, -1000);
		
		public var zoom:Number;
		public var focus:Number;
		public var sort:Boolean;
		
		public function CameraObject3D(zoom:Number = 3, focus:Number = 500, initObject:Object = null)
		{
			super();
			this.x = initObject? initObject.x || DEFAULT_POS.x : DEFAULT_POS.x;
			this.y = initObject? initObject.y || DEFAULT_POS.y : DEFAULT_POS.y;
			this.z = initObject? initObject.z || DEFAULT_POS.z : DEFAULT_POS.z;
			this.zoom = zoom;
			this.focus = focus;
			this.sort = initObject? (initObject.sort != false) : true;
		}
		
		public function transformView( transform:Matrix3D=null ):void
		{
			this.view = Matrix3D.inverse(Matrix3D.multiply(transform || this.transform, _flipY));
		}

		public function tilt(angle:Number):void
		{
			super.pitch(angle);
		}

		public function pan(angle:Number):void
		{
			super.yaw(angle);
		}
	}
}
