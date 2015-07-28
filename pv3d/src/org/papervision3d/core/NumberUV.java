package org.papervision3d.core;

/**
 * UV数
 */
public class NumberUV {
	//U值
	public double u;
	//V值
	public double v;
	
	public NumberUV() {
		this(0, 0);
	} 
	
	/**
	 * UV值，默认为(0, 0)
	 * @param	u
	 * @param	v
	 */
	public NumberUV(double u, double v) {
		this.u = u;
		this.v = v;
	}
	
	/**
	 * 复制
	 * @return
	 */
	public NumberUV clone() {
		return new NumberUV(this.u, this.v);
	}
	
	/**
	 * (0,0)
	 */
	static public NumberUV getZERO() {
		return new NumberUV(0, 0);
	}
	
	/**
	 * 字符串形式，调试用
	 * @return
	 */
	public String toString() {
		return "u:" + u + " v:" + v;
	}
}
