package org.papervision3d.core
{
	/**
	 * UV数
	 */
	public class NumberUV
	{
		//U值
		public var u:Number;
		//V值
		public var v:Number;
		
		/**
		 * UV值，默认为(0, 0)
		 * @param	u
		 * @param	v
		 */
		public function NumberUV(u:Number = 0, v:Number = 0)
		{
			this.u = u;
			this.v = v;
		}
		
		/**
		 * 复制
		 * @return
		 */
		public function clone():NumberUV
		{
			return new NumberUV(this.u, this.v);
		}
		
		/**
		 * (0,0)
		 */
		static public function get ZERO():NumberUV
		{
			return new NumberUV(0, 0);
		}
		
		/**
		 * 字符串形式，调试用
		 * @return
		 */
		public function toString():String
		{
			return 'u:' + u + ' v:' + v;
		}
	}
}

