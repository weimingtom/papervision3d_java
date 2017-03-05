package org.papervision3d.core.utils
{
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	
	import org.papervision3d.Papervision3D;
	import org.papervision3d.events.FileLoadEvent;
	import org.papervision3d.objects.DisplayObject3D;
	import org.papervision3d.materials.MaterialsList;
	import org.papervision3d.materials.ColorMaterial;
	import org.papervision3d.materials.WireframeMaterial;
	import org.papervision3d.core.Matrix3D;
	import org.papervision3d.core.NumberUV;
	import org.papervision3d.core.geom.Face3D;
	import org.papervision3d.core.geom.Vertex3D;
	import org.papervision3d.core.geom.Mesh3D;
	import org.papervision3d.core.proto.GeometryObject3D;
	import org.papervision3d.core.proto.DisplayObjectContainer3D;
	import org.papervision3d.core.proto.MaterialObject3D;
	
	/**
	 * DAE文件读取器，把某个文件转为三维对象，放入container中
	 */
	public class Collada extends EventDispatcher
	{
		public static var DEFAULT_SCALING:Number = 1;
		private static var INTERNAL_SCALING:Number = 100;
		private static var toDEGREES :Number = 180 / Math.PI;
		private static var toRADIANS :Number = Math.PI / 180;
		
		private static var COLLADASECTION:String = "COLLADA";
		private static var LIBRARYSECTION:String = "library";
		private static var ASSETSECTION:String = "asset";
		private static var SCENESECTION:String = "scene";
		private static var LIGHTPREFAB:String = "light";
		private static var CAMERAPREFAB:String = "camera";
		private static var MATERIALSECTION:String = "material";
		private static var GEOMETRYSECTION:String = "geometry";
		private static var MESHSECTION:String = "mesh";
		private static var SOURCESECTION:String = "source";
		private static var ARRAYSECTION:String = "array";
		private static var ACCESSORSECTION:String = "accessor";
		private static var VERTICESSECTION:String = "vertices";
		private static var INPUTTAG:String = "input";
		private static var POLYGONSSECTION:String = "polygons";
		private static var POLYGON:String = "p";
		private static var NODESECTION:String = "node";
		private static var LOOKATNODE:String = "lookat";
		private static var MATRIXNODE:String = "matrix";
		private static var PERSPECTIVENODE:String = "perspective";
		private static var ROTATENODE:String = "rotate";
		private static var SCALENODE:String = "scale";
		private static var TRANSLATENODE:String = "translate";
		private static var SKEWNODE:String = "skew";
		private static var INSTANCENODE:String = "instance";
		private static var INSTACESCENE:String = "instance_visual_scene";
		private static var PARAMTAG:String = "param";
		private static var POSITIONINPUT:String = "POSITION";
		private static var VERTEXINPUT:String = "VERTEX";
		private static var NORMALINPUT:String = "NORMAL";
		private static var TEXCOORDINPUT:String = "TEXCOORD";
		private static var UVINPUT:String = "UV";
		private static var TANGENTINPUT:String = "TANGENT";
		
		//最原始加载的XML数据
		private var COLLADA:XML;
		//输出的三维对象容器
		private var _container:DisplayObjectContainer3D;
		//几何数据，未使用
		private var _geometries:Object;
		//加载器
		private var _loader:URLLoader;
		//URL，加载和发送消息用
		private var _filename:String;
		//材质列表
		private var _materials:MaterialsList;
		//缩放因子
		private var _scaling:Number;
		//回调，未使用
		private var _callback:Function;
		//DAE文件指定的模型方向
		private var _yUp:Boolean;
		
		/**
		 * 构造函数
		 * @param	container 呈现的三维容器
		 * @param	filename URL
		 * @param	materials 材质列表
		 * @param	scale 缩放
		 * @param	callback 回调
		 * @param	initObject 附加数据
		 */
		public function Collada(container:DisplayObjectContainer3D, filename:String, materials:MaterialsList = null, scale:Number = 1, callback:Function = null, initObject:Object = null)
		{
			this._container = container;
			this._filename = filename;
			this._materials = materials;
			this._scaling = scale || DEFAULT_SCALING;
			this._scaling *= INTERNAL_SCALING;
			this._callback = callback;
			this._geometries = new Object();
			loadCollada();
		}
		
		/**
		 * 开始加载
		 */
		private function loadCollada():void
		{
			this._loader = new URLLoader();
			this._loader.addEventListener(Event.COMPLETE, onComplete);
			this._loader.load(new URLRequest(this._filename));
		}
		
		/**
		 * 完成事件
		 * @param	evt
		 */
		private function onComplete(evt:Event):void
		{
			COLLADA = new XML(this._loader.data);
			buildCollada();
		}
		
		/**
		 * 构造DAE对象
		 */
		private function buildCollada():void
		{
			default xml namespace = COLLADA.namespace();
			this._yUp = (COLLADA.asset.up_axis == "Y_UP");
			var sceneId:String = getId(COLLADA.scene.instance_visual_scene.@url);
			var scene:XML = COLLADA.library_visual_scenes.visual_scene.(@id == sceneId)[0];
			//解析
			parseScene(scene);
			var fileEvent:FileLoadEvent = new FileLoadEvent(FileLoadEvent.LOAD_COMPLETE, _filename);
			this.dispatchEvent(fileEvent);
		}
		
		//开始解析每个XML节点
		private function parseScene(scene:XML):void
		{
			for each (var node:XML in scene.node)
			{
				parseNode(node, this._container);
			}
		}
		
		/**
		 * 解析单个节点
		 * @param	node 单个节点
		 * @param	parent 三维容器
		 */
		private function parseNode(node:XML, parent:DisplayObjectContainer3D):void
		{
			var matrix:Matrix3D = Matrix3D.IDENTITY;
			var newNode:DisplayObject3D;
			if (String(node.instance_geometry) == "")
			{
				newNode = new DisplayObject3D(node.@name);
			}
			else
			{
				newNode = new Mesh3D(null, null, null, node.@name);
			}
			var instance:DisplayObject3D = parent.addChild(newNode, node.@name);
			var children:XMLList = node.children();
			var totalChildren:int = children.length();
			for (var i:int = 0; i < totalChildren; i++)
			{
				var child:XML = children[i];
				switch (child.name().localName)
				{
					case "translate":
						matrix = Matrix3D.multiply(matrix, translateMatrix(getArray(child)));
						break;

					case "rotate":
						matrix = Matrix3D.multiply(matrix, rotateMatrix(getArray(child)));
						break;

					case "scale":
						matrix = Matrix3D.multiply(matrix, scaleMatrix(getArray(child)));
						break;
					
					case "matrix":
						matrix = Matrix3D.multiply(matrix, new Matrix3D(getArray(child)));
						break;
						
					case "node":
						parseNode(child, instance);
						break;
						
					case "instance_geometry":
						for each (var geometry:XML in child)
						{
							var geoId:String = getId(geometry.@url);
							var geo:XML = COLLADA.library_geometries.geometry.(@id == geoId)[0];
							parseGeometry(geo, instance, Matrix3D.clone(matrix));
						}
						break;
				}
			}
			instance.copyTransform( matrix );
		}
		
		/**
		 * 解析几何数据
		 * @param	geometry 几何节点
		 * @param	instance 三维显示对象
		 * @param	matrix2 变换矩阵
		 */
		private function parseGeometry(geometry:XML, instance:DisplayObject3D, matrix2:Matrix3D = null):void
		{
			var matrix:Matrix3D = Matrix3D.clone(matrix2) || Matrix3D.IDENTITY;
			var semantics:Object = new Object();
			semantics.name = geometry.@id;
			var faces:Array = semantics.triangles = new Array();
			var multiMaterial:Boolean = (geometry.mesh.triangles.length() > 1);
			for each (var triangles:XML in geometry.mesh.triangles)
			{
				var field:Array = new Array();
				for each (var input:XML in triangles.input)
				{
					semantics[input.@semantic] = deserialize(input, geometry);
					field.push(input.@semantic);
				}
				var data:Array = triangles.p.split(' ');
				var len:Number = triangles.@count;
				var material:String = triangles.@material;
				addMaterial(instance, material);
				for (var j:Number = 0; j < len; j++)
				{
					var t:Object = new Object();
					for (var v:Number = 0; v < 3; v++)
					{
						var fld:String;
						for (var k:Number = 0; fld = field[k]; k++)
						{
							if (!t[fld]) 
							{
								t[fld] = new Array();
							}
							t[fld].push(Number(data.shift()));
						}
						t["material"] = material;
					}
					faces.push( t );
				}
			}
			buildObject(semantics, instance, matrix);
		}
		
		/**
		 * 构建对象
		 * @param	semantics
		 * @param	instance
		 * @param	matrix
		 */
		private function buildObject(semantics:Object, instance:DisplayObject3D, matrix:Matrix3D = null):void
		{
			matrix = matrix || Matrix3D.IDENTITY;
			instance.addGeometry(new GeometryObject3D());
			var vertices:Array = instance.geometry.vertices = new Array();
			var scaling:Number = this._scaling;
			var accVerts:Number = vertices.length;
			var semVertices:Array = semantics.VERTEX;
			var len:Number = semVertices.length;
			var i:int;
			for( i=0; i < len; i++ )
			{
				var vert:Object = semVertices[i];
				var x:Number = Number(vert.X) * scaling;
				var y:Number = Number(vert.Y) * scaling;
				var z:Number = Number(vert.Z) * scaling;
				if (this._yUp)
				{
					vertices.push(new Vertex3D(-x, y, z));
				}
				else
				{
					vertices.push(new Vertex3D(x, z, y));
				}
			}
			var faces:Array = instance.geometry.faces = new Array();
			var semFaces:Array = semantics.triangles;
			len = semFaces.length;
			for (i = 0; i < len; i++)
			{
				var tri:Array = semFaces[i].VERTEX;
				var a:Vertex3D = vertices[accVerts + tri[0]];
				var b:Vertex3D = vertices[accVerts + tri[1]];
				var c:Vertex3D = vertices[accVerts + tri[2]];
				var faceList:Array = [a, b, c];
				var tex:Array = semantics.TEXCOORD;
				var uv:Array = semFaces[i].TEXCOORD;
				var uvList :Array, uvA :NumberUV, uvB :NumberUV, uvC :NumberUV;
				if (uv && tex)
				{
					uvA = new NumberUV(tex[uv[0]].S, tex[uv[0]].T);
					uvB = new NumberUV(tex[uv[1]].S, tex[uv[1]].T);
					uvC = new NumberUV(tex[uv[2]].S, tex[uv[2]].T);
					uvList = [uvA, uvB, uvC];
				}
				else 
				{
					uvList = null;
				}
				var materialName:String = semFaces[i].material || null;
				var face:Face3D = new Face3D( faceList, materialName, uvList );
				faces.push( face );
			}
			instance.geometry.ready = true;
			matrix.n14 *= scaling;
			matrix.n24 *= scaling;
			matrix.n34 *= scaling;
			instance.material = new ColorMaterial( 0xFF0000, 0.25 );
			instance.visible  = true;
		}
		
		/**
		 * 把字符串转为数字数组
		 * @param	spaced
		 * @return
		 */
		private function getArray(spaced:String):Array
		{
			var strings:Array = spaced.split(" ");
			var numbers:Array = new Array();
			var totalStrings:Number = strings.length;
			for (var i:Number=0; i < totalStrings; i++)
			{
				numbers[i] = Number(strings[i]);
			}
			return numbers;
		}
		
		/**
		 * 添加材质
		 * @param	instance 三维显示对象
		 * @param	name 名称
		 */
		private function addMaterial(instance:DisplayObject3D, name:String):void
		{
			var material:MaterialObject3D = this._materials.getMaterialByName( name );
			if (!material)
			{
				material = new WireframeMaterial( Math.random() * 0xFFFFFF );
				material.name = name;
				material.fillAlpha = 1;
				material.fillColor = 0;
				material.oneSide = true;
				Papervision3D.log("Collada material " + name + " not found." );
			}
			if (!instance.materials) 
			{
				instance.materials = new MaterialsList();
			}
			instance.materials.addMaterial(material, name);
		}
		
		/**
		 * 旋转矩阵变换
		 * @param	vector
		 * @return
		 */
		private function rotateMatrix(vector:Array):Matrix3D
		{
			if (this._yUp)
			{
				return Matrix3D.rotationMatrix(vector[0], vector[1], vector[2], -vector[3] *toRADIANS);
			}
			else
			{
				return Matrix3D.rotationMatrix(vector[0], vector[2], vector[1], -vector[3] *toRADIANS);
			}
		}
		
		/**
		 * 平移矩阵变换
		 * @param	vector
		 * @return
		 */
		private function translateMatrix( vector:Array ):Matrix3D
		{
			if (this._yUp)
			{
				return Matrix3D.translationMatrix( -vector[0] *this._scaling, vector[1] *this._scaling, vector[2] *this._scaling );
			}
			else
			{
				return Matrix3D.translationMatrix(  vector[0] *this._scaling, vector[2] *this._scaling, vector[1] *this._scaling );
			}
		}

		/**
		 * 缩放矩阵变换
		 * @param	vector 缩放向量[x, y, z]
		 * @return
		 */
		private function scaleMatrix(vector:Array):Matrix3D
		{
			if (this._yUp)
			{
				return Matrix3D.scaleMatrix(vector[0], vector[1], vector[2]);
			}
			else
			{
				return Matrix3D.scaleMatrix(vector[0], vector[2], vector[1]);
			}
		}
		
		/**
		 * 反序列化
		 * @param	input
		 * @param	geo
		 * @return
		 */
		private function deserialize(input:XML, geo:XML):Array
		{
			var output:Array = new Array();
			var id:String = input.@source.split("#")[1];
			var acc:XMLList = geo..source.(@id == id).technique_common.accessor;
			if (acc != new XMLList())
			{
				var floId:String = acc.@source.split("#")[1];
				var floXML:XMLList = COLLADA..float_array.(@id == floId);
				var floStr:String = floXML.toString();
				var floats:Array = floStr.split(" ");
				var params:Array = new Array();
				for each (var par:XML in acc.param)
				{
					params.push(par.@name);
				}
				var count:int = acc.@count;
				var stride:int = acc.@stride;
				for (var i:int = 0; i < count; i++)
				{
					var element :Object = new Object();
					for (var j:int = 0; j < stride; j++)
					{
						element[params[j]] = floats.shift();
					}
					output.push(element);
				}
			}
			else
			{
				var recursive:XMLList = geo..vertices.(@id == id)[INPUTTAG];
				output = deserialize(recursive[0], geo);
			}
			return output;
		}
		
		/**
		 * 根据字符串获取#的ID
		 * @param	url
		 * @return
		 */
		private function getId(url:String):String
		{
			return url.split("#")[1];
		}
	}
}