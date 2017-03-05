package org.papervision3d.core.geom
{
	/**
	 * 二维顶点，用于投影
	 */
	public class Vertex2D
	{
		//三维坐标
		public var x:Number;
		public var y:Number;
		public var z:Number;
		
		//可见性（应该是计算出来的，用于消隐
		public var visible:Boolean;
		
		//未使用
		public var extra:Object;
		
		public function Vertex2D(x:Number = 0, y:Number = 0, z:Number = 0)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.visible = false;
		}
	}
}
