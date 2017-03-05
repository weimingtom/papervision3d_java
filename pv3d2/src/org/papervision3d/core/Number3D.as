package org.papervision3d.core
{
	/**
	 * 3D坐标（三维向量）
	 */
	public class Number3D
	{
		//X
		public var x:Number;
		//Y
		public var y:Number;
		//Z
		public var z:Number;
		
		/**
		 * 构造函数
		 * @param	x
		 * @param	y
		 * @param	z
		 */
		public function Number3D(x:Number = 0, y:Number = 0, z:Number = 0)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		/**
		 * 复制
		 * @return
		 */
		public function clone():Number3D
		{
			return new Number3D(this.x, this.y, this.z);
		}
		
		/**
		 * 距离（模）
		 */
		public function get modulo():Number
		{
			return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
		}
		
		/**
		 * 加
		 * @param	v
		 * @param	w
		 * @return
		 */
		public static function add(v:Number3D, w:Number3D):Number3D
		{
			return new Number3D (
				v.x + w.x,
				v.y + w.y,
				v.z + w.z
			);
		}
		
		/**
		 * 减
		 * @param	v
		 * @param	w
		 * @return
		 */
		public static function sub(v:Number3D, w:Number3D):Number3D
		{
			return new Number3D(v.x - w.x, v.y - w.y, v.z - w.z);
		}
		
		/**
		 * 点乘
		 * @param	v
		 * @param	w
		 * @return
		 */
		public static function dot(v:Number3D, w:Number3D):Number
		{
			return (v.x * w.x + v.y * w.y + w.z * v.z);
		}
		
		/**
		 * 叉乘
		 * @param	v
		 * @param	w
		 * @return
		 */
		public static function cross(v:Number3D, w:Number3D):Number3D
		{
			return new Number3D((w.y * v.z) - (w.z * v.y), (w.z * v.x) - (w.x * v.z), (w.x * v.y) - (w.y * v.x));
		}
		
		/**
		 * 归一化（单位化）
		 */
		public function normalize():void
		{
			var mod:Number = this.modulo;
			if (mod != 0 && mod != 1)
			{
				this.x /= mod;
				this.y /= mod;
				this.z /= mod;
			}
		}
		
		/**
		 * (0, 0, 0)
		 */
		static public function get ZERO():Number3D
		{
			return new Number3D(0, 0, 0);
		}
		
		/**
		 * 字符串形式，调试用
		 * @return
		 */
		public function toString(): String
		{
			return 'x:' + x + ' y:' + y + ' z:' + z;
		}
	}
}
