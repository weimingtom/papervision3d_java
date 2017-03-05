package org.papervision3d.core.geom
{
	import flash.utils.Dictionary;
	
	import org.papervision3d.objects.DisplayObject3D;
	import org.papervision3d.core.Matrix3D;
	import org.papervision3d.core.NumberUV;
	import org.papervision3d.core.proto.MaterialObject3D;
	import org.papervision3d.core.proto.CameraObject3D;
	
	/**
	 * 网格
	 */
	public class Mesh3D extends Vertices3D
	{
		/**
		 * 
		 * @param	material 材质
		 * @param	vertices 顶点数组
		 * @param	faces 面数组
		 * @param	name 名称
		 * @param	initObject 附加数据
		 */
		public function Mesh3D(material:MaterialObject3D, vertices:Array, faces:Array, name:String = null, initObject:Object = null)
		{
			super(vertices, name, initObject);
			this.geometry.faces = faces || new Array();
			this.material = material || MaterialObject3D.DEFAULT;
		}
		
		/**
		 * 投影
		 * @param	parent
		 * @param	camera
		 * @param	sorted
		 * @return
		 */
		public override function project(parent:DisplayObject3D, camera:CameraObject3D, sorted:Array = null):Number
		{
			super.project(parent, camera, sorted);
			if (!sorted)
			{
				sorted = this._sorted;
			}
			var projected:Dictionary = this.projected;
			var view:Matrix3D = this.view;
			var faces:Array = this.geometry.faces;
			var iFaces:Array = this.faces;
			var screenZs:Number = 0;
			var visibleFaces :Number = 0;
			var vertex0:Vertex2D, vertex1:Vertex2D, vertex2:Vertex2D, visibles:Number, iFace:Object, face:Face3D;
			for (var i:int = 0; face = faces[i]; i++)
			{
				iFace = iFaces[i] || (iFaces[i] = {});
				iFace.face = face;
				iFace.instance = this;
				vertex0 = projected[face.vertices[0]];
				vertex1 = projected[face.vertices[1]];
				vertex2 = projected[face.vertices[2]];
				visibles = Number(vertex0.visible) + Number(vertex1.visible) + Number(vertex2.visible);
				iFace.visible = ( visibles == 3 );
				if(iFace.visible)
				{
					screenZs += iFace.screenZ = ( vertex0.z + vertex1.z + vertex2.z ) /3;
					visibleFaces++;
					if (sorted) 
					{
						sorted.push( iFace );
					}
				}
			}
			return this.screenZ = screenZs / visibleFaces;
		}
		
		/**
		 * 利用反射机制进行映射
		 * @param	u
		 * @param	v
		 */
		public function projectTexture(u:String = "x", v:String = "y"):void
		{
			var faces:Array = this.geometry.faces;
			var bBox:Object = this.boundingBox();
			var minX:Number = bBox.min[u];
			var sizeX:Number = bBox.size[u];
			var minY:Number = bBox.min[v];
			var sizeY:Number = bBox.size[v];
			var objectMaterial:MaterialObject3D = this.material;
			for (var i:String in faces)
			{
				var myFace:Face3D = faces[Number(i)];
				var myVertices:Array = myFace.vertices;
				var a:Vertex3D = myVertices[0];
				var b:Vertex3D = myVertices[1];
				var c:Vertex3D = myVertices[2];
				var uvA:NumberUV = new NumberUV((a[u] - minX) / sizeX, (a[v] - minY) / sizeY);
				var uvB:NumberUV = new NumberUV((b[u] - minX) / sizeX, (b[v] - minY) / sizeY);
				var uvC:NumberUV = new NumberUV((c[u] - minX) / sizeX, (c[v] - minY) / sizeY);
				myFace.uv = [uvA, uvB, uvC];
			}
		}
	}
}
