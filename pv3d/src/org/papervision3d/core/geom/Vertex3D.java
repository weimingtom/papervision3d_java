package org.papervision3d.core.geom;

/**
 * 三维顶点
 */
public class Vertex3D {
	/**
	 * 三维坐标
	 */
	public double x;
	public double y;
	public double z;
	
	//未使用，附加数据
	public Object extra;
	
	public Vertex3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double get(String str) {
		if (str != null && str.equals("x")) {
			return x;
		} else if (str != null && str.equals("y")) {
			return y;
		} else if (str != null && str.equals("z")) {
			return z;
		} else {
			throw new RuntimeException("error Vertex3D field");
		}
	}
}
