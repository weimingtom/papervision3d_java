package org.papervision3d.core.geom
{
	/**
	 * 三维顶点
	 */
	public class Vertex3D
	{
		/**
		 * 三维坐标
		 */
		public var x:Number;
		public var y:Number;
		public var z:Number;
		
		//未使用，附加数据
		public var extra:Object;
		
		public function Vertex3D(x:Number = 0, y:Number = 0, z:Number = 0)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
}