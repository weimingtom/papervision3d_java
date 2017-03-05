package org.papervision3d.core.proto
{
	import flash.events.EventDispatcher;
	
	import org.papervision3d.core.Matrix3D;
	import org.papervision3d.core.geom.Vertex3D;
	
	/**
	 * 几何数据（面、顶点数组）
	 */
	public class GeometryObject3D extends EventDispatcher
	{
		//面数组, Face3D[], ((x,y,z), materialName, (u,v))...
		public var faces:Array;
		//顶点数组, Vertex3D[], (x, y, z),...
		public var vertices:Array;
		//表示数据是否加载或解析完成
		public var ready:Boolean = false;
		
		//未使用
		protected var _material:MaterialObject3D;
		
		/**
		 * 保存全部顶点离原点距离平方最大的值
		 * 用于碰撞测试
		 */
		protected var _boundingSphere2:Number;
		//记录_boundingSphere2是否已经被计算
		protected var _boundingSphereDirty:Boolean = true;
		
		/**
		 * 计算全部顶点离原点距离平方最大的值
		 * @return
		 */
		public function getBoundingSphere2():Number
		{
			var max:Number = 0;
			var d:Number;
			for each (var v:Vertex3D in this.vertices)
			{
				d = v.x * v.x + v.y * v.y + v.z * v.z;
				max = (d > max) ? d : max;
			}
			this._boundingSphereDirty = false;
			return _boundingSphere2 = max;
		}
		
		/**
		 * 用于显示对象的碰撞测试
		 * 返回最大范围的平方
		 */
		public function get boundingSphere2():Number
		{
			if (_boundingSphereDirty)
			{
				return getBoundingSphere2();
			}
			else
			{
				return _boundingSphere2;
			}
		}
		
		/**
		 * 私有，没有使用
		 * @param	transformation
		 */
		public function transformVertices(transformation:Matrix3D):void 
		{
			
		}
		
		/**
		 * 私有，没有使用
		 * @param	material
		 */
		public function transformUV(material:MaterialObject3D):void
		{
			if (material.bitmap)
			{
				for (var i:String in this.faces)
				{
					faces[i].transformUV(material);
				}
			}
		}
		
		/**
		 * 构造函数
		 * @param	initObject
		 */
		public function GeometryObject3D(initObject:Object = null):void
		{
			
		}
	}
}
