package org.papervision3d.events
{
	import flash.events.Event;
	
	/**
	 * 用于ASE文件加载时的事件
	 */
	public class FileLoadEvent extends Event
	{
		/**
		 * 加载完成，且数据已经解析完毕
		 */
		public static var LOAD_COMPLETE:String = "loadComplete";
		/**
		 * 加载出错
		 */
		public static var LOAD_ERROR:String = "loadError";
		/**
		 * URL
		 */
		public var file:String;
		
		/**
		 * 构造函数
		 * @param	type 类型
		 * @param	p_file URL
		 * @param	bubbles 是否冒泡
		 * @param	cancelable 是否可取消
		 */
		public function FileLoadEvent(type:String, p_file:String = "", bubbles:Boolean = false, cancelable:Boolean = false)
		{
			super(type, bubbles, cancelable);
			file = p_file;
		}
	}
}
