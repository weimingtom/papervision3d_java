package org.papervision3d.core.geom
{
	import flash.utils.Dictionary;
	
	import org.papervision3d.objects.DisplayObject3D;
	import org.papervision3d.core.Matrix3D;
	import org.papervision3d.core.Number3D;
	import org.papervision3d.core.proto.CameraObject3D;
	import org.papervision3d.core.proto.GeometryObject3D;
	
	/**
	 * 顶点集合
	 */
	public class Vertices3D extends DisplayObject3D
	{
		/**
		 * 构造函数
		 * @param	vertices 顶点数组
		 * @param	name 名称
		 * @param	initObject 附加数据
		 */
		public function Vertices3D(vertices:Array, name:String = null, initObject:Object = null)
		{
			super(name, new GeometryObject3D(), initObject);
			this.geometry.vertices = vertices || new Array();
		}
		
		/**
		 * ！！！关键代码！！！
		 * 
		 * 投影
		 * @param	parent
		 * @param	camera
		 * @param	sorted
		 * @return
		 */
		public override function project(parent:DisplayObject3D, camera:CameraObject3D, sorted:Array = null):Number
		{
			super.project(parent, camera, sorted);
			var projected:Dictionary = this.projected;
			var view:Matrix3D = this.view;
			var m11:Number = view.n11;
			var m12:Number = view.n12;
			var m13:Number = view.n13;
			var m21:Number = view.n21;
			var m22:Number = view.n22;
			var m23:Number = view.n23;
			var m31:Number = view.n31;
			var m32:Number = view.n32;
			var m33:Number = view.n33;
			var vertices:Array = this.geometry.vertices;
			var i:int = vertices.length;
			var focus:Number = camera.focus;
			var zoom:Number = camera.zoom;
			var vertex:Vertex3D, screen:Vertex2D, persp:Number;
			while (vertex = vertices[--i])
			{
				var vx:Number = vertex.x;
				var vy:Number = vertex.y;
				var vz:Number = vertex.z;
				var s_x:Number = vx * m11 + vy * m12 + vz * m13 + view.n14;
				var s_y:Number = vx * m21 + vy * m22 + vz * m23 + view.n24;
				var s_z:Number = vx * m31 + vy * m32 + vz * m33 + view.n34;
				screen = projected[vertex] || (projected[vertex] = new Vertex2D());
				if (screen.visible = (s_z > 0))
				{
					persp = focus / (focus + s_z) * zoom;
					screen.x = s_x * persp;
					screen.y = s_y * persp;
					screen.z = s_z;
				}
			}
			return 0;
		}
		
		/**
		 * 包装盒，用于纹理映射
		 * 返回最大，最小和间距
		 * min:Number3D
		 * max:Number3D
		 * size:Number3D
		 * @return
		 */
		public function boundingBox():Object
		{
			var vertices:Object = this.geometry.vertices;
			var bBox:Object = new Object();
			bBox.min = new Number3D();
			bBox.max = new Number3D();
			bBox.size = new Number3D();
			for (var i:String in vertices)
			{
				var v:Vertex3D = vertices[Number(i)];
				bBox.min.x = (bBox.min.x == undefined)? v.x : Math.min(v.x, bBox.min.x);
				bBox.max.x = (bBox.max.x == undefined)? v.x : Math.max(v.x, bBox.max.x);
				bBox.min.y = (bBox.min.y == undefined)? v.y : Math.min(v.y, bBox.min.y);
				bBox.max.y = (bBox.max.y == undefined)? v.y : Math.max(v.y, bBox.max.y);
				bBox.min.z = (bBox.min.z == undefined)? v.z : Math.min(v.z, bBox.min.z);
				bBox.max.z = (bBox.max.z == undefined)? v.z : Math.max(v.z, bBox.max.z);
			}
			bBox.size.x = bBox.max.x - bBox.min.x;
			bBox.size.y = bBox.max.y - bBox.min.y;
			bBox.size.z = bBox.max.z - bBox.min.z;
			return bBox;
		}
		
		/**
		 * 遍历所有顶点进行线性变换
		 * @param	transformation 变换矩阵
		 */
		public function transformVertices(transformation:Matrix3D):void
		{
			var m11:Number = transformation.n11;
			var m12:Number = transformation.n12;
			var m13:Number = transformation.n13;
			var m21:Number = transformation.n21;
			var m22:Number = transformation.n22;
			var m23:Number = transformation.n23;
			var m31:Number = transformation.n31;
			var m32:Number = transformation.n32;
			var m33:Number = transformation.n33;
			var m14:Number = transformation.n14;
			var m24:Number = transformation.n24;
			var m34:Number = transformation.n34;
			var vertices:Array = this.geometry.vertices;
			var i:int = vertices.length;
			var vertex:Vertex3D;
			while (vertex = vertices[--i])
			{
				var vx:Number = vertex.x;
				var vy:Number = vertex.y;
				var vz:Number = vertex.z;
				var tx:Number = vx * m11 + vy * m12 + vz * m13 + m14;
				var ty:Number = vx * m21 + vy * m22 + vz * m23 + m24;
				var tz:Number = vx * m31 + vy * m32 + vz * m33 + m34;
				vertex.x = tx;
				vertex.y = ty;
				vertex.z = tz;
			}
		}
	}
}
