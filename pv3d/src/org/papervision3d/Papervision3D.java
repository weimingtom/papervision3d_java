package org.papervision3d;

public class Papervision3D {
	//旋转单位是角度数还是弧度数
	static public boolean useDEGREES = true;
	//缩放因子是百分数还是小数
	static public boolean usePERCENT = false;
	//版本信息，用于场景初始化时的日志
	static public String NAME = "Papervision3D";
	static public String VERSION = "Beta RC1";
	static public String DATE = "01.02.07";
	//版权信息
	static public String AUTHOR = "(c) 2006-2007 Copyright by Carlos Ulloa | papervision3d.org | C4RL054321@gmail.com";
	//是否显示日志
	static public boolean VERBOSE = false;
	
	/**
	 * 日志报告
	 * @param	message
	 */
	static public void log(String message) {
		if (Papervision3D.VERBOSE) {
			System.out.println(message);
		}
	}
}
