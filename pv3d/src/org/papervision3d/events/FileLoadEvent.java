package org.papervision3d.events;

import flash.events.Event;

public class FileLoadEvent extends Event {
	/**
	 * 加载完成，且数据已经解析完毕
	 */
	public static String LOAD_COMPLETE = "loadComplete";
	/**
	 * 加载出错
	 */
	public static String LOAD_ERROR = "loadError";
	/**
	 * URL
	 */
	public String file;
	
	/**
	 * 构造函数
	 * @param	type 类型
	 * @param	p_file URL
	 * @param	bubbles 是否冒泡
	 * @param	cancelable 是否可取消
	 */
	public FileLoadEvent(String type, String p_file, boolean bubbles, boolean cancelable) {
		super(type, bubbles, cancelable);
		file = p_file;
	}
	public FileLoadEvent(String type, String p_file) {
		this(type, p_file, false, false);
	}
}
