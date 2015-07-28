package org.papervision3d.core.proto;

import java.util.Map;

import flash.display.BitmapData;

/**
 * 所有材质的基类
 */
public class MaterialObject3D {
	//全局，默认材质的颜色
	public static int DEFAULT_COLOR = 0xFF00FF;
	//全局，材质总数，用于计算ID
	private static int _totalMaterialObjects = 0;
	
	/**
	 * 位图，用于位图材质
	 */
	public BitmapData bitmap;
	
	//是否动画，用于SceneObject3D.renderCamera时是否执行updateBitmap
	public boolean animated;
	//是否平滑，用于Face3D的beginBitmapFill（贴图用）
	public boolean smooth;
	
	/**
	 * 线颜色和透明度，用于线框材质
	 */
	public int lineColor;
	public int lineAlpha;
	/**
	 * 填充颜色和透明度，用于颜色材质
	 */
	public int fillColor;
	public double fillAlpha;
	
	/**
	 * 是否单面，用于Face3D的渲染
	 */
	public boolean oneSide;
	/**
	 * 是否跳过Face3D的渲染
	 */
	public boolean invisible;
	/**
	 * 是否对面（当oneSide为true）
	 */
	public boolean opposite;
	//内部使用（调试用）
	public SceneObject3D scene;
	//材质名称，用于添加材质列表时用（非必须）
	public String name;
	//全局材质序号(调试用)
	public int id;
	
	/**
	 * 是否双面
	 */
	public boolean getDoubleSided() {
		return !this.oneSide;
	}
	
	/**
	 * 设置双面
	 */
	public void setDoubleSided(boolean doubleSided) {
		this.oneSide = !doubleSided;
	}
	
	public MaterialObject3D() {
		this(null);
	}
	
	/**
	 * 构造函数
	 * @param	initObject 可用于指定bitmap
	 */
	public MaterialObject3D(Map<String, Object> initObject) {
		if (initObject != null && 
			initObject.get("bitmap") != null) {
			this.bitmap = (BitmapData)initObject.get("bitmap");
		}
		if (initObject != null && 
			initObject.get("lineColor") != null) {
			this.lineColor = (Integer)initObject.get("lineColor");
		} else {
			this.lineColor = DEFAULT_COLOR;
		}
		if (initObject != null && 
			initObject.get("lineAlpha") != null) {
			this.lineAlpha = (Integer)initObject.get("lineAlpha");
		} else {
			this.lineAlpha = 0;
		}
		if (initObject != null && 
			initObject.get("fillColor") != null) {
			this.fillColor = (Integer)initObject.get("fillColor");
		} else {
			this.fillColor = DEFAULT_COLOR;
		}
		if (initObject != null && 
			initObject.get("fillAlpha") != null) {
			this.fillAlpha = (Integer)initObject.get("fillAlpha");
		} else {
			this.fillAlpha = 0;
		}
		if (initObject != null && 
			initObject.get("animated") != null) {
			this.animated = (Boolean)initObject.get("animated");
		} else {
			this.animated = false;
		}
		if (initObject != null && 
			initObject.get("invisible") != null) {
			this.invisible = (Boolean)initObject.get("invisible");
		} else {
			this.invisible = false;
		}		
		if (initObject != null && 
			initObject.get("smooth") != null) {
			this.smooth = (Boolean)initObject.get("smooth");
		} else {
			this.smooth = false;
		}
		if (initObject != null && 
			initObject.get("doubleSided") != null) {
			this.setDoubleSided((Boolean)initObject.get("doubleSided"));
		} else {
			this.setDoubleSided(false);
		}		
		if (initObject != null && 
			initObject.get("opposite") != null) {
			this.opposite = (Boolean)initObject.get("opposite");
		} else {
			this.opposite = false;
		}
		this.id = _totalMaterialObjects++;
	}
	
	/**
	 * 默认材质，用于网格
	 * 线框加纯色材质
	 */
	static public MaterialObject3D getDEFAULT() {
		MaterialObject3D defMaterial = new MaterialObject3D();
		defMaterial.lineColor = DEFAULT_COLOR;
		defMaterial.lineAlpha = 100;
		defMaterial.fillColor = DEFAULT_COLOR;
		defMaterial.fillAlpha = 10;
		defMaterial.setDoubleSided(true);
		return defMaterial;
	}
	
	/**
	 * 如果开启animated，则在场景执行renderCamera时执行此方法
	 */
	public void updateBitmap() {
		
	}
	
	/**
	 * 复制
	 * @param	material
	 */
	public void copy(MaterialObject3D material) {
		this.bitmap = material.bitmap;
		this.animated = material.animated;
		this.smooth = material.smooth;
		this.lineColor = material.lineColor;
		this.lineAlpha = material.lineAlpha;
		this.fillColor = material.fillColor;
		this.fillAlpha = material.fillAlpha;
		this.oneSide = material.oneSide;
		this.opposite = material.opposite;
		this.invisible = material.invisible;
		this.scene = material.scene;
		this.name = material.name;
	}
	
	/**
	 * 复制
	 * @return
	 */
	public MaterialObject3D clone() {
		MaterialObject3D cloned = new MaterialObject3D();
		cloned.bitmap = this.bitmap;
		cloned.animated = this.animated;
		cloned.smooth = this.smooth;
		cloned.lineColor = this.lineColor;
		cloned.lineAlpha = this.lineAlpha;
		cloned.fillColor = this.fillColor;
		cloned.fillAlpha = this.fillAlpha;
		cloned.oneSide = this.oneSide;
		cloned.opposite = this.opposite;
		cloned.invisible = this.invisible;
		cloned.scene = this.scene;
		cloned.name = this.name;
		return cloned;
	}
	
	/**
	 * 字符串形式
	 * @return
	 */
	public String toString() {
		return "[MaterialObject3D] bitmap:" + this.bitmap + " lineColor:" + this.lineColor + " fillColor:" + fillColor;
	}
}
