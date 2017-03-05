package org.papervision3d.core.geom
{
	import flash.geom.Matrix;
	import flash.display.Graphics;
	import flash.display.BitmapData;
	import flash.display.Sprite;
	import flash.utils.Dictionary;
	
	import org.papervision3d.Papervision3D;
	import org.papervision3d.objects.DisplayObject3D;
	import org.papervision3d.core.proto.MaterialObject3D;
	
	/**
	 * 面3D
	 */
	public class Face3D
	{
		//全局，总面数
		private static var _totalFaces:Number = 0;
		//全局，位图矩阵，用于beginBitmapFill
		private static var _bitmapMatrix:Matrix;
		
		//取出顶点数组, Vertex3D[3]
		public var vertices:Array;
		//材质名称, String
		public var materialName:String;
		//取出UV数组，NumberUV[3], (u,v)...
		public var uv:Array;
		
		//未使用，应该是用来排序的
		public var screenZ:Number;
		//未使用，可见性，
		public var visible:Boolean;
		
		//面的全局序号（用于调试）
		public var id:Number;
		
		/**
		 * 一个面的数据
		 * @param	vertices 顶点数组, Vertex3D[3]
		 * @param	materialName 材质名称, String
		 * @param	uv UV数组, NumberUV[3]
		 */
		public function Face3D(vertices:Array, materialName:String = null, uv:Array = null)
		{
			this.vertices = vertices;
			this.materialName = materialName;
			this.uv = uv;
			this.id = _totalFaces++;
			if (!_bitmapMatrix) 
			{
				_bitmapMatrix = new Matrix();
			}
		}
		
		/**
		 * 转换（贴图用?）
		 * @param	instance 贴图用的三维对象
		 * @return 转换矩阵？
		 */
		public function transformUV(instance:DisplayObject3D = null):Object
		{
			var material:MaterialObject3D = 
				(this.materialName && instance.materials) ? 
				instance.materials.materialsByName[this.materialName] : 
				instance.material;
			if (!this.uv)
			{
				Papervision3D.log("Face3D: transformUV() uv not found!");
			}
			else if (material && material.bitmap)
			{
				var uv:Array = this.uv;
				var w:Number = material.bitmap.width;
				var h:Number = material.bitmap.height;
				var u0:Number = w * uv[0].u;
				var v0:Number = h * (1 - uv[0].v);
				var u1:Number = w * uv[1].u;
				var v1:Number = h * (1 - uv[1].v);
				var u2:Number = w * uv[2].u;
				var v2:Number = h * (1 - uv[2].v);
				if ((u0 == u1 && v0 == v1) || (u0 == u2 && v0 == v2))
				{
					u0 -= (u0 > 0.05) ? 0.05 : -0.05;
					v0 -= (v0 > 0.07) ? 0.07 : -0.07;
				}
				if (u2 == u1 && v2 == v1)
				{
					u2 -= (u2 > 0.05) ? 0.04 : -0.04;
					v2 -= (v2 > 0.06) ? 0.06 : -0.06;
				}
				var at:Number = (u1 - u0);
				var bt:Number = (v1 - v0);
				var ct:Number = (u2 - u0);
				var dt:Number = (v2 - v0);
				var m:Matrix = new Matrix(at, bt, ct, dt, u0, v0);
				m.invert();
				var mapping:Object = instance.projected[this] || (instance.projected[this] = new Object());
				mapping._a = m.a;
				mapping._b = m.b;
				mapping._c = m.c;
				mapping._d = m.d;
				mapping._tx = m.tx;
				mapping._ty = m.ty;
			}
			else 
			{
				Papervision3D.log("Face3D: transformUV() material.bitmap not found!");
			}
			return mapping;
		}
		
		/**
		 * ！！！关键代码！！！
		 * 
		 * 渲染，根据面和显示对象的信息，把面画在容器上
		 * @param	instance 显示对象实体
		 * @param	container 显示容器
		 * @return 是否成功
		 */
		public function render(instance:DisplayObject3D, container:Sprite):Number
		{
			var vertices:Array = this.vertices;
			var projected:Dictionary = instance.projected;
			var s0:Vertex2D = projected[vertices[0]];
			var s1:Vertex2D = projected[vertices[1]];
			var s2:Vertex2D = projected[vertices[2]];
			var x0:Number = s0.x;
			var y0:Number = s0.y;
			var x1:Number = s1.x;
			var y1:Number = s1.y;
			var x2:Number = s2.x;
			var y2:Number = s2.y;
			var material:MaterialObject3D = (this.materialName && instance.materials) ? instance.materials.materialsByName[this.materialName] : instance.material;
			if (material.invisible)
			{
				return 0;
			}
			if (material.oneSide)
			{
				if (material.opposite)
				{
					if ((x2 - x0) * (y1 - y0) - (y2 - y0) * (x1 - x0) > 0)
					{
						return 0;
					}
				}
				else
				{
					if ((x2 - x0) * (y1 - y0) - (y2 - y0) * (x1 - x0) < 0)
					{
						return 0;
					}
				}
			}
			var texture:BitmapData = material.bitmap;
			var fillAlpha:Number = material.fillAlpha;
			var lineAlpha:Number = material.lineAlpha;
			var graphics:Graphics = container.graphics;
			if (texture)
			{
				var map:Object = instance.projected[this] || transformUV(instance);
				var a1:Number = map._a;
				var b1:Number = map._b;
				var c1:Number = map._c;
				var d1:Number = map._d;
				var tx1:Number = map._tx;
				var ty1:Number = map._ty;
				var a2:Number = x1 - x0;
				var b2:Number = y1 - y0;
				var c2:Number = x2 - x0;
				var d2:Number = y2 - y0;
				var matrix:Matrix = _bitmapMatrix;
				matrix.a = a1 * a2 + b1 * c2;
				matrix.b = a1 * b2 + b1 * d2;
				matrix.c = c1 * a2 + d1 * c2;
				matrix.d = c1 * b2 + d1 * d2;
				matrix.tx = tx1 * a2 + ty1 * c2 + x0;
				matrix.ty = tx1 * b2 + ty1 * d2 + y0;
				graphics.beginBitmapFill(texture, matrix, false, material.smooth);
			}
			else if (fillAlpha)
			{
				graphics.beginFill(material.fillColor, fillAlpha);
			}
			if (lineAlpha)
			{
				graphics.lineStyle(0, material.lineColor, lineAlpha);
			}
			else
			{
				graphics.lineStyle();
			}
			graphics.moveTo(x0, y0);
			graphics.lineTo(x1, y1);
			graphics.lineTo(x2, y2);
			Papervision3D.log("Face3D:" +
				"(" + x0 + "," + y0 + ")" + 
				"(" + x1 + "," + y1 + ")" + 
				"(" + x2 + "," + y2 + ")");
			if (lineAlpha)
			{
				graphics.lineTo(x0, y0);
			}
			if (texture || fillAlpha)
			{
				graphics.endFill();
			}
			return 1;
		}
	}
}
