package org.papervision3d.materials
{
	import org.papervision3d.core.proto.MaterialObject3D;
	
	/**
	 * 颜色材质，只有填充颜色和透明度
	 */
	public class ColorMaterial extends MaterialObject3D
	{
		public function ColorMaterial(color:Number = 0xFF00FF, alpha:Number = 100, initObject:Object = null)
		{
			super(initObject);
			this.fillColor = color;
			this.fillAlpha = alpha;
		}
		
		public override function toString(): String
		{
			return 'ColorMaterial - color:' + this.fillColor + ' alpha:' + this.fillAlpha;
		}
	}
}
