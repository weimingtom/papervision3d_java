package org.papervision3d.objects
{
	import org.papervision3d.core.NumberUV;
	import org.papervision3d.core.geom.Face3D;
	import org.papervision3d.core.geom.Vertex3D;
	import org.papervision3d.core.geom.Mesh3D;
	import org.papervision3d.core.proto.MaterialObject3D;
	
	/**
	 * 3D平面
	 */
	public class Plane extends Mesh3D
	{
		static public var DEFAULT_SIZE:Number = 500;
		static public var DEFAULT_SCALE:Number = 1;
		static public var DEFAULT_SEGMENTS:Number = 1;
		
		//横向和纵向的格数
		public var segmentsW:Number;
		public var segmentsH:Number;
		
		/**
		 * 创建新的平面
		 * @param	material 材质
		 * @param	width 宽
		 * @param	height 高，可以为0，用于匹配材质
		 * @param	segmentsW 横向线段数
		 * @param	segmentsH 纵向线段数
		 * @param	initObject 附加数据
		 */
		public function Plane(material:MaterialObject3D = null, width:Number = 0, height:Number = 0, segmentsW:Number = 0, segmentsH:Number = 0, initObject:Object = null)
		{
			super(material, new Array(), new Array(), null, initObject);
			this.segmentsW = segmentsW || DEFAULT_SEGMENTS;
			this.segmentsH = segmentsH || this.segmentsW;
			var scale:Number = DEFAULT_SCALE;
			if (!height)
			{
				if (width)
				{
					scale = width;
				}
				if (material && material.bitmap)
				{
					width = material.bitmap.width * scale;
					height = material.bitmap.height * scale;
				}
				else
				{
					width = DEFAULT_SIZE * scale;
					height = DEFAULT_SIZE * scale;
				}
			}
			buildPlane(width, height);
		}
		
		/**
		 * 手工写数据
		 * @param	width
		 * @param	height
		 */
		private function buildPlane(width:Number, height:Number):void
		{
			//线段数
			var gridX:Number = this.segmentsW;
			var gridY:Number = this.segmentsH;
			//点数
			var gridX1:Number = gridX + 1;
			var gridY1:Number = gridY + 1;
			//顶点和面数组
			var vertices:Array = this.geometry.vertices;
			var faces:Array = this.geometry.faces;
			//纹理
			var textureX:Number = width / 2;
			var textureY:Number = height / 2;
			//每个线段的长度
			var iW:Number = width / gridX;
			var iH:Number = height / gridY;
			//遍历每个坐标点编号(ix, iy)，计算对应的3D坐标
			for (var ix:int = 0; ix < gridX + 1; ix++)
			{
				for (var iy:int = 0; iy < gridY1; iy++)
				{
					var x:Number = ix * iW - textureX;
					var y:Number = iy * iH - textureY;
					vertices.push(new Vertex3D(x, y, 0));
				}
			}
			var uvA:NumberUV;
			var uvC:NumberUV;
			var uvB:NumberUV;
			//遍历每个坐标点编号(ix, iy)，计算对应的UV值
			for (ix = 0; ix < gridX; ix++)
			{
				for (iy = 0; iy < gridY; iy++)
				{
					//注意由于vertices是一维的，需要转换
					var a:Vertex3D = vertices[ix * gridY1 + iy];
					var c:Vertex3D = vertices[ix * gridY1 + (iy + 1)];
					var b:Vertex3D = vertices[(ix + 1) * gridY1 + iy];
					uvA = new NumberUV(ix / gridX, iy / gridY );
					uvC = new NumberUV(ix / gridX, (iy+1) / gridY);
					uvB = new NumberUV((ix + 1) / gridX, iy / gridY );
					//每个面包含顶点数组，材质名称，UV数组
					faces.push(new Face3D([a, b, c], null, [uvA, uvB, uvC]));
					a = vertices[(ix + 1) * gridY1 + (iy + 1)];
					c = vertices[(ix + 1) * gridY1 + iy];
					b = vertices[ix * gridY1 + (iy + 1)];
					//另一个半格
					uvA = new NumberUV((ix + 1) / gridX, (iy + 1) / gridY );
					uvC = new NumberUV((ix + 1) / gridX, iy / gridY);
					uvB = new NumberUV(ix / gridX, (iy + 1) / gridY);
					faces.push(new Face3D([a, b, c], null, [uvA, uvB, uvC]));
				}
			}
			this.geometry.ready = true;
		}
	}
}

