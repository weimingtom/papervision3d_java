package org.papervision3d.core.proto
{
	import flash.events.EventDispatcher;
	import flash.utils.Dictionary;
	
	import org.papervision3d.objects.DisplayObject3D;
	import org.papervision3d.materials.MaterialsList;
	import org.papervision3d.core.utils.Collada;
	
	/**
	 * 三维显示对象容器
	 * 维护一个显示列表
	 */
	public class DisplayObjectContainer3D extends EventDispatcher
	{
		//儿子列表（反向查询），强键
		protected var _children:Dictionary;
		//儿子哈希表（名称映射到DisplayObject3D），弱键
		//（有助于回收DisplayObject3D）
		protected var _childrenByName:Dictionary;
		//儿子总数
		private var _childrenTotal:int;
		
		/**
		 * 儿子总数
		 */
		public function get numChildren():int
		{
			return this._childrenTotal;
		}
		
		/**
		 * 构造函数
		 */
		public function DisplayObjectContainer3D():void
		{
			this._children = new Dictionary(false);
			this._childrenByName = new Dictionary(true);
			this._childrenTotal = 0;
		}
		
		/**
		 * 添加儿子
		 * @param	child
		 * @param	name
		 * @return
		 */
		public function addChild(child:DisplayObject3D, name:String = null):DisplayObject3D
		{
			name = name || child.name || String(child.id);
			this._children[child] = name;
			this._childrenByName[name] = child;
			this._childrenTotal++;
			return child;
		}
		
		/**
		 * 移除对象
		 * @param	child
		 * @return
		 */
		public function removeChild(child:DisplayObject3D):DisplayObject3D
		{
			delete this._childrenByName[this._children[child]];
			delete this._children[child];
			return child;
		}
		
		/**
		 * 根据名称查找儿子
		 * @param	name 名称
		 * @return 三维显示对象
		 */
		public function getChildByName(name:String):DisplayObject3D
		{
			return this._childrenByName[name];
		}
		
		/**
		 * 根据名称删除
		 * @param	name
		 * @return
		 */
		public function removeChildByName(name:String):DisplayObject3D
		{
			return removeChild(getChildByName(name));
		}
		
		/**
		 * 加载dae文件
		 * @param	filename URL
		 * @param	materials 材质列表
		 * @param	scale 缩放
		 */
		public function addCollada(filename:String, materials:MaterialsList = null, scale:Number = 1):void
		{
			var collada:Collada = new Collada(this, filename, materials, scale);
		}
		
		/**
		 * 字符串形式，调试用
		 * @return
		 */
		public override function toString():String
		{
			return childrenList();
		}
		
		/**
		 * 字符串形式，调试用
		 * @return
		 */
		public function childrenList():String
		{
			var list:String = "";
			for (var name:String in this._children)
			{
				list += name + "\n";
			}
			return list;
		}
	}
}