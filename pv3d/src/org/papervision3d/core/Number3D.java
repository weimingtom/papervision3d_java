package org.papervision3d.core;

/**
 * 3D坐标（三维向量）
 */
public class Number3D {
	//X
	public double x;
	//Y
	public double y;
	//Z
	public double z;
	
	public Number3D() {
		this(0, 0, 0);
	}
	
	/**
	 * 构造函数
	 * @param	x
	 * @param	y
	 * @param	z
	 */
	public Number3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * 复制
	 * @return
	 */
	public Number3D clone(){
		return new Number3D(this.x, this.y, this.z);
	}
	
	/**
	 * 距离（模）
	 */
	public double getModulo() {
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}
	
	/**
	 * 加
	 * @param	v
	 * @param	w
	 * @return
	 */
	public static Number3D add(Number3D v, Number3D w) {
		return new Number3D (
			v.x + w.x,
			v.y + w.y,
			v.z + w.z
		);
	}
	
	/**
	 * 减
	 * @param	v
	 * @param	w
	 * @return
	 */
	public static Number3D sub(Number3D v, Number3D w) {
		return new Number3D(v.x - w.x, v.y - w.y, v.z - w.z);
	}
	
	/**
	 * 点乘
	 * @param	v
	 * @param	w
	 * @return
	 */
	public static Number dot(Number3D v, Number3D w) {
		return (v.x * w.x + v.y * w.y + w.z * v.z);
	}
	
	/**
	 * 叉乘
	 * @param	v
	 * @param	w
	 * @return
	 */
	public static Number3D cross(Number3D v, Number3D w) {
		return new Number3D((w.y * v.z) - (w.z * v.y), (w.z * v.x) - (w.x * v.z), (w.x * v.y) - (w.y * v.x));
	}
	
	/**
	 * 归一化（单位化）
	 */
	public void normalize() {
		double mod = this.getModulo();
		if (mod != 0 && mod != 1) {
			this.x /= mod;
			this.y /= mod;
			this.z /= mod;
		}
	}
	
	/**
	 * (0, 0, 0)
	 */
	static public Number3D getZERO() {
		return new Number3D(0, 0, 0);
	}
	
	/**
	 * 字符串形式，调试用
	 * @return
	 */
	public String toString() {
		return "x:" + x + " y:" + y + " z:" + z;
	}
	
	public double get(String str) {
		if (str != null && str.equals("x")) {
			return x;
		} else if (str != null && str.equals("y")) {
			return y;
		} else if (str != null && str.equals("z")) {
			return z;
		} else {
			throw new RuntimeException("error Number3D field");
		}
	}
}
