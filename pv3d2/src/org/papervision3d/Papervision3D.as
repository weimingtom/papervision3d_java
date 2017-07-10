package org.papervision3d
{
	public class Papervision3D
	{
		//旋转单位是角度数还是弧度数
		static public var useDEGREES:Boolean = true;
		//缩放因子是百分数还是小数
		static public var usePERCENT:Boolean = false;
		//版本信息，用于场景初始化时的日志
		static public var NAME:String = 'Papervision3D';
		static public var VERSION:String = 'Beta RC1';
		static public var DATE:String = '01.02.07';
		//版权信息
		static public var AUTHOR:String = '(c) 2006-2007 Copyright by Carlos Ulloa | papervision3d.org | C4RL054321@gmail.com';
		//是否显示日志
		static public var VERBOSE:Boolean = false;
		
		/**
		 * 日志报告
		 * @param	message
		 */
		static public function log(message:String):void
		{
			if (Papervision3D.VERBOSE)
			{
				trace(message);
			}
		}
	}
}