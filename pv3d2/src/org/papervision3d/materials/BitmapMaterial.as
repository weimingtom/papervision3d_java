package org.papervision3d.materials
{
	import flash.display.BitmapData;
	
	import org.papervision3d.core.proto.MaterialObject3D;
	
	/**
	 * 位图材质，只有位图数据
	 * 直接传入位图数据
	 */
	public class BitmapMaterial extends MaterialObject3D
	{
		/**
		 * 位图数据（BitmapData或其子类对象）
		 * 也可能是类名字符串（见BitmapAssetMaterial）
		 */
		protected var _texture:*;
		
		/**
		 * 获取位图数据
		 */
		public function get texture():*
		{
			return this._texture;
		}
		
		/**
		 * 设置位图数据
		 */
		public function set texture(asset:*):void
		{
			this.bitmap = createBitmap(asset);
			this._texture = asset;
		}
		
		/**
		 * 
		 * @param	asset 位图数据（BitmapData的子类对象）
		 * @param	initObject 附加数据
		 */
		public function BitmapMaterial(asset:*, initObject:Object = null)
		{
			super(initObject);
			texture = asset;
		}
		
		/**
		 * 私有，创建位图数据（实际是进行类型转换）
		 * @param	asset
		 * @return
		 */
		protected function createBitmap(asset:*):BitmapData
		{
			return asset;
		}
		
		/**
		 * 字符串形式，用于调试
		 * @return
		 */
		public override function toString():String
		{
			return 'Texture:' + this.texture + ' lineColor:' + this.lineColor + ' lineAlpha:' + this.lineAlpha;
		}
	}
}
