package org.papervision3d.cameras
{
	import org.papervision3d.core.Number3D;
	import org.papervision3d.core.Matrix3D;
	import org.papervision3d.core.proto.CameraObject3D;
	import org.papervision3d.objects.DisplayObject3D;
	
	/**
	 * 由于摄像机的特殊性，所以它没有添加到场景中。
	 * 
	 * 通过x,y调整镜头位置
	 * 另见SceneObject3D.renderCamera
	 */
	public class Camera3D extends CameraObject3D
	{
		/**
		 * 面向的对象
		 */
		public var target:DisplayObject3D;
		/**
		 * 位置的3D坐标,用于hover，可选
		 */
		public var goto:Number3D;
		
		public function Camera3D(target:DisplayObject3D = null, zoom:Number = 2, focus:Number = 100, initObject:Object = null)
		{
			super(zoom, focus, initObject);
			this.target = target || DisplayObject3D.ZERO;
			this.goto = new Number3D(this.x, this.y, this.z);
		}
		
		/**
		 * 用于SceneObject3D.renderCamera
		 * @param	transform
		 */
		public override function transformView(transform:Matrix3D = null):void
		{
			this.lookAt(this.target);
			super.transformView();
		}
		
		//TODO: 这个方法可有可无
		public function hover(type:Number, mouseX:Number, mouseY:Number):void
		{
			var target:DisplayObject3D = this.target;
			var goto:Number3D = this.goto;
			var camSpeed:Number = 8;
			switch(type)
			{
				case 0:
					var dX:Number = goto.x - target.x;
					var dZ:Number = goto.z - target.z;
					var ang:Number = Math.atan2(dZ, dX);
					var dist:Number = Math.sqrt(dX * dX + dZ * dZ);
					var xMouse:Number = 0.5 * mouseX;
					var camX :Number = dist * Math.cos(ang - xMouse);
					var camZ :Number = dist * Math.sin(ang - xMouse);
					var camY :Number = goto.y - 300 * mouseY;
					this.x -= (this.x - camX) / camSpeed;
					this.y -= (this.y - camY) / camSpeed;
					this.z -= (this.z - camZ) / camSpeed;
					break;
				
				case 1:
					this.x -= (this.x - 1000 * mouseX) / camSpeed;
					this.y -= (this.y - 1000 * mouseY) / camSpeed;
					break;
			}
		}
	}
}