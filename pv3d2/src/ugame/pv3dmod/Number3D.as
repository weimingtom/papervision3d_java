package ugame.pv3dmod 
{
	/**
	 * ...
	 * @author 
	 */
	public class Number3D
	{
		public var x:Number;
		public var y:Number;
		public var z:Number;
		
		public function Number3D(x:Number = 0, y:Number = 0, z:Number = 0) 
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public function getModulo():Number
		{
			return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
		}
		
		public function normalize():void
		{
			var mod:Number = this.getModulo();
			if (mod != 0 && mod != 1)
			{
				this.x /= mod;
				this.y /= mod;
				this.z /= mod;
			}
		}
		
		public static function add(v:Number3D, w:Number3D):Number3D
		{
			return new Number3D(v.x + w.x, v.y + w.y, v.z + w.z);
		}
		
		public static function sub(v:Number3D, w:Number3D):Number3D
		{
			return new Number3D(v.x - w.x, v.y - w.y, v.z - w.z);
		}
		
		public static function dot(v:Number3D, w:Number3D):Number
		{
			return (v.x * w.x + v.y * w.y + w.z * v.z);
		}
		
		public static function cross(v:Number3D, w:Number3D):Number3D
		{
			return new Number3D((w.y * v.z) - (w.z * v.y), (w.z * v.x) - (w.x * v.z), (w.x * v.y) - (w.y * v.x));
		}
	}
}
