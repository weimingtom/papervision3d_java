package org.papervision3d.materials;

import java.util.Map;

import org.papervision3d.core.proto.MaterialObject3D;

/**
 * 线框材质（只有线颜色，先透明度），
 * 肯定是双面的
 */
public class WireframeMaterial extends MaterialObject3D {
	public WireframeMaterial() {
		this(0xFF00FF);
	}
	
	public WireframeMaterial(int color) {
		this(color, 100);
	}
	
	public WireframeMaterial(int color, int alpha) {
		this(color, alpha, null);
	}
	
	/**
	 * 构造函数
	 * @param	color 线颜色
	 * @param	alpha 线透明度
	 * @param	initObject 附加信息
	 */
	public WireframeMaterial(int color, int alpha, Map<String, Object> initObject) {
		super(initObject);
		this.lineColor = color;
		this.lineAlpha = alpha;
		this.setDoubleSided(true);
	}
	
	/**
	 * 字符串形式，用于调试
	 * @return
	 */
	public String toString() {
		return "WireframeMaterial - color:" + 
			this.lineColor + 
			" alpha:" + 
			this.lineAlpha;
	}
}
