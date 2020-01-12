package org.papervision3d.materials;

import java.util.Map;

import org.papervision3d.core.proto.MaterialObject3D;

/**
 * 颜色材质，只有填充颜色和透明度
 */
public class ColorMaterial extends MaterialObject3D {
	public ColorMaterial() {
		this(0xFF00FF);
	}
	
	public ColorMaterial(int color) {
		this(color, 100);
	}
	
	public ColorMaterial(int color, double alpha) {
		this(color, alpha, null);
	}
	
	public ColorMaterial(int color, double alpha, Map<String, Object> initObject) {
		super(initObject);
		this.fillColor = color;
		this.fillAlpha = alpha;
	}
	
	public String toString() {
		return "ColorMaterial - color:" + 
			this.fillColor + 
			" alpha:" + 
			this.fillAlpha;
	}
}
