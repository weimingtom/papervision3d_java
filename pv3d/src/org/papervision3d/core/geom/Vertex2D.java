package org.papervision3d.core.geom;

/**
 * 二维顶点，用于投影
 */
public class Vertex2D {
	//三维坐标
	public double x;
	public double y;
	public double z;
	
	//可见性（应该是计算出来的，用于消隐）
	public boolean visible;
	
	//未使用
	public Object extra;
	
	public Vertex2D() {
		this(0, 0, 0);
	}
	
	public Vertex2D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.visible = false;
	}
}
