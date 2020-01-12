package org.papervision3d.core.proto;

import java.util.HashMap;
import java.util.Map;

import org.papervision3d.core.utils.Collada;
import org.papervision3d.materials.MaterialsList;
import org.papervision3d.objects.DisplayObject3D;

import flash.events.EventDispatcher;

public class DisplayObjectContainer3D extends EventDispatcher {
	//儿子列表（反向查询），强键
	protected Map<DisplayObject3D, String> _children;
	//儿子哈希表（名称映射到DisplayObject3D），弱键
	//（有助于回收DisplayObject3D）
	protected Map<String, DisplayObject3D> _childrenByName;
	//儿子总数
	private int _childrenTotal;
	
	/**
	 * 儿子总数
	 */
	public int getNumChildren() {
		return this._childrenTotal;
	}
	
	/**
	 * 构造函数
	 */
	public DisplayObjectContainer3D() {
		this._children = new HashMap<DisplayObject3D, String>();
		this._childrenByName = new HashMap<String, DisplayObject3D>();
		this._childrenTotal = 0;
	}
	
	public DisplayObject3D addChild(DisplayObject3D child) {
		return this.addChild(child, null);
	}
	
	/**
	 * 添加儿子
	 * @param	child
	 * @param	name
	 * @return
	 */
	public DisplayObject3D addChild(DisplayObject3D child, String name) {
		if (name != null) {
			
		} else if (child.name != null) {
			name = child.name;
		} else {
			name = Integer.toString(child.id);
		}
		this._children.put(child, name);
		this._childrenByName.put(name, child);
		this._childrenTotal++;
		return child;
	}
	
	/**
	 * 移除对象
	 * @param	child
	 * @return
	 */
	public DisplayObject3D removeChild(DisplayObject3D child) {
		this._childrenByName.remove(this._children.get(child));
		this._children.remove(child);
		return child;
	}
	
	/**
	 * 根据名称查找儿子
	 * @param	name 名称
	 * @return 三维显示对象
	 */
	public DisplayObject3D getChildByName(String name) {
		return this._childrenByName.get(name);
	}
	
	/**
	 * 根据名称删除
	 * @param	name
	 * @return
	 */
	public DisplayObject3D removeChildByName(String name) {
		return removeChild(getChildByName(name));
	}
	
	/**
	 * 加载dae文件
	 * @param	filename URL
	 * @param	materials 材质列表
	 * @param	scale 缩放
	 */
	public void addCollada(String filename, MaterialsList materials, double scale) {
		Collada collada = new Collada(this, filename, materials, scale);
	}
	public void addCollada(String filename, MaterialsList materials) {
		this.addCollada(filename, materials, 1);
	}
	
	/**
	 * 字符串形式，调试用
	 * @return
	 */
	public String toString() {
		return childrenList();
	}
	
	/**
	 * 字符串形式，调试用
	 * @return
	 */
	public String childrenList() {
		String list = "";
		for (String name : this._children.values()) {
			list += name + "\n";
		}
		return list;
	}
	
	
}
