package org.papervision3d.materials;

import java.util.HashMap;
import java.util.Map;

import flash.display.BitmapData;

/**
 * 位图资源材质，用反射获取BitmapData
 * 参数为字符串
 */
public class BitmapAssetMaterial extends BitmapMaterial {

	//全局的BitmapData缓存,类名->BitmapData
	static private Map<String, BitmapData> _library = new HashMap<String, BitmapData>();
	//应用计数，类名->BitmapData
	static private Map<String, Integer> _count = new HashMap<String, Integer>();
	
	public BitmapAssetMaterial(Object asset, Map<String, Object>initObject) {
		super(asset, initObject);
	}

	/**
	 * 通过反射获取资源中的位图数据
	 * @param	asset 字符串，如果为空则释放位图
	 * @return
	 */
	protected BitmapData createBitmap(Object asset) {
		//资源名称改变（把原有位图的引用计数减一）
		if (this._texture != asset) {
			//删除原有引用
			_count.put((String)this._texture, _count.get((String)this._texture) - 1);
			BitmapData prevBitmap = _library.get((String)this._texture);
			if (prevBitmap != null && _count.get((String)this._texture) == 0) {
				//应用计数减到0时析构
				prevBitmap.dispose();
			}
		}
		BitmapData bitmap = _library.get((String)asset);
		if (bitmap == null)  {
			//没有缓存的话
			//通过反射取出
			bitmap = getDefinitionByName((String)asset);
			_library.put((String)asset, bitmap);
			_count.put((String)asset, 0);
		} else {
			//有缓存的话
			//增加引用计数
			_count.put((String)asset, _count.get((String)asset) + 1);
		}
		return bitmap;
	}
	
	private static BitmapData getDefinitionByName(String asset) {
		return null;
	}
}
