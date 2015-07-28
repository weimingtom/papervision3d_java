package org.papervision3d.materials;

import java.util.Map;

import org.papervision3d.core.proto.MaterialObject3D;

import flash.display.BitmapData;

/**
 * 位图材质，只有位图数据
 * 直接传入位图数据
 */
public class BitmapMaterial extends MaterialObject3D {
	/**
	 * 位图数据（BitmapData或其子类对象）
	 * 也可能是类名字符串（见BitmapAssetMaterial）
	 */
	protected Object _texture;
	
	/**
	 * 获取位图数据
	 */
	public Object getTexture() {
		return this._texture;
	}
	
	/**
	 * 设置位图数据
	 */
	public void setTexture(Object asset) {
		this.bitmap = createBitmap(asset);
		this._texture = asset;
	}
	
	/**
	 * 
	 * @param	asset 位图数据（BitmapData的子类对象）
	 * @param	initObject 附加数据
	 */
	public BitmapMaterial(Object asset, Map<String, Object>initObject) {
		super(initObject);
		setTexture(asset);
	}
	
	/**
	 * 私有，创建位图数据（实际是进行类型转换）
	 * @param	asset
	 * @return
	 */
	protected BitmapData createBitmap(Object asset) {
		return (BitmapData)asset;
	}
	
	/**
	 * 字符串形式，用于调试
	 * @return
	 */
	public String toString() {
		return "Texture:" + 
			this.getTexture() + 
			" lineColor:" + 
			this.lineColor + 
			" lineAlpha:" + 
			this.lineAlpha;
	}
}
