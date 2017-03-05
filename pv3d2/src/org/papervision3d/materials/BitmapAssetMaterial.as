package org.papervision3d.materials
{
	import flash.display.BitmapData;
	import flash.utils.getDefinitionByName;
	import flash.utils.describeType;
	import flash.utils.Dictionary;
	
	/**
	 * 位图资源材质，用反射获取BitmapData
	 * 参数为字符串
	 */
	public class BitmapAssetMaterial extends BitmapMaterial
	{
		//全局的BitmapData缓存,类名->BitmapData
		static private var _library:Dictionary = new Dictionary();
		//应用计数，类名->BitmapData
		static private var _count:Dictionary = new Dictionary();
		
		public function BitmapAssetMaterial(id:String, initObject:Object = null)
		{
			super(id, initObject);
		}
		
		/**
		 * 通过反射获取资源中的位图数据
		 * @param	asset 字符串，如果为空则释放位图
		 * @return
		 */
		protected override function createBitmap(asset:*):BitmapData
		{
			//资源名称改变（把原有位图的引用计数减一）
			if (this._texture != asset)
			{
				//删除原有引用
				_count[this._texture]--;
				var prevBitmap:BitmapData = _library[this._texture];
				if (prevBitmap && _count[this._texture] == 0)
				{
					//应用计数减到0时析构
					prevBitmap.dispose();
				}
			}
			var bitmap:BitmapData = _library[asset];
			if (!bitmap) //没有缓存的话
			{
				//通过反射取出
				var BitmapAsset:Class = getDefinitionByName(asset) as Class;
				var description:XML = describeType(BitmapAsset);
				if (description..constructor.length() == 0)
				{
					bitmap = new BitmapAsset();
				}
				else
				{
					bitmap = new BitmapAsset(0, 0);
				}
				_library[asset] = bitmap;
				_count[asset] = 0;
			}
			else //有缓存的话
			{
				//增加引用计数
				_count[asset]++;
			}
			return bitmap;
		}
	}
}