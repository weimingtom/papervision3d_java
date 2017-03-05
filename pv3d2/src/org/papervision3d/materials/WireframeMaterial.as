package org.papervision3d.materials
{
	import org.papervision3d.core.proto.MaterialObject3D;
	
	/**
	 * 线框材质（只有线颜色，先透明度），
	 * 肯定是双面的
	 */
	public class WireframeMaterial extends MaterialObject3D
	{
		/**
		 * 构造函数
		 * @param	color 线颜色
		 * @param	alpha 线透明度
		 * @param	initObject 附加信息
		 */
		public function WireframeMaterial(color:Number = 0xFF00FF, alpha:Number = 100, initObject:Object = null)
		{
			super(initObject);
			this.lineColor = color;
			this.lineAlpha = alpha;
			this.doubleSided = true;
		}
		
		/**
		 * 字符串形式，用于调试
		 * @return
		 */
		public override function toString(): String
		{
			return 'WireframeMaterial - color:' + this.lineColor + ' alpha:' + this.lineAlpha;
		}
	}
}