package org.papervision3d.objects
{
	import flash.net.URLRequest;
	import flash.net.URLLoader;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.ProgressEvent;
	
	import org.papervision3d.Papervision3D;
	import org.papervision3d.core.NumberUV;
	import org.papervision3d.core.geom.Mesh3D;
	import org.papervision3d.core.geom.Vertex3D;
	import org.papervision3d.core.geom.Face3D;
	import org.papervision3d.core.proto.MaterialObject3D;
	import org.papervision3d.events.FileLoadEvent;
	
	/**
	 * ASE格式文件加载器，转为网格
	 */
	public class Ase extends Mesh3D
	{
		//默认缩放（没有使用？）
		static public var DEFAULT_SCALING:Number = 1;
		//内部缩放（用于加载）
		static public var INTERNAL_SCALING:Number = 50;
		
		//缩放因子
		private var _scaleAse:Number;
		//加载器
		private var _loaderAse:URLLoader;
		//URL
		private var _filename:String
		
		/**
		 * ASE格式文件加载器
		 * @param	material 材质
		 * @param	filename URL
		 * @param	scale 缩放因子
		 * @param	initObject 附加对象
		 */
		public function Ase(material:MaterialObject3D, filename:String, scale:Number = 1, initObject:Object = null)
		{
			super(material, new Array(), new Array(), null, initObject);
			this._scaleAse = scale;
			this._filename = filename;
			loadAse(filename);
		}
		
		/**
		 * 开始加载文件
		 * @param	filename ASE文件名
		 */
		private function loadAse(filename:String):void
		{
			_loaderAse = new URLLoader();
			_loaderAse.addEventListener(Event.COMPLETE, parseAse);
			_loaderAse.addEventListener(ProgressEvent.PROGRESS, progressHandler);
			_loaderAse.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
			var request:URLRequest = new URLRequest(filename);
			try
			{
				_loaderAse.load(request);
			}
			catch(e:Error)
			{
				Papervision3D.log("error in loading ase file");
			}
		}
		
		/**
		 * 出错，发送FileLoadEvent事件，携带相关文件名
		 * @param	event
		 */
		private function ioErrorHandler(event:IOErrorEvent):void
		{
			var fileEvent:FileLoadEvent = new FileLoadEvent(FileLoadEvent.LOAD_ERROR, _filename);
			dispatchEvent(fileEvent);
			throw new Error("Ase: ioErrorHandler Error.");
		}
		
		/**
		 * 加载进度
		 * @param	event
		 */
		private function progressHandler(event:ProgressEvent):void
		{
			Papervision3D.log("progressHandler loaded:" + event.bytesLoaded + " total: " + event.bytesTotal);
		}
		
		/**
		 * 解析器（立即建立对象引用关系）
		 * MESH_VERTEX_LIST:顶点列表(x,y,z)
		 * MESH_FACE_LIST:面-顶点映射表face=> ((x1,y1,z1), (x2, y2, z2), (x3, y3, z3))
		 * MESH_TVERTLIST:贴图UV列表(u, v)
		 * MESH_TFACELIST:面-贴图UV映射表face => ((u1, v1), (u2, v2), (u3, v3))
		 * UV需要在MESH_TFACELIST时才真正保存到geometry中（立刻把编号转成UV值）
		 * @param	e
		 */
		private function parseAse(e:Event):void
		{
			var scale:Number = this._scaleAse;
			scale *= INTERNAL_SCALING;
			var vertices:Array = this.geometry.vertices = new Array();
			var faces:Array = this.geometry.faces = new Array();
			var loader:URLLoader = URLLoader(e.target);
			var lines:Array = unescape(loader.data).split('\r\n');
			var line:String;
			var chunk:String;
			var content:String;
			var uvs:Array = new Array();
			var material:MaterialObject3D = this.material;
			while(lines.length)
			{
				line = String(lines.shift());
				line = line.substr(line.indexOf('*') + 1);
				if (line.indexOf('}') >= 0)
				{
					line = '';
				}
				chunk = line.substr(0, line.indexOf(' '));
				switch (chunk)
				{
					case 'MESH_VERTEX_LIST':
						try
						{
							while((content = String(lines.shift())).indexOf('}') < 0)
							{
								content = content.split("*")[1];
								var mvl:Array = content.split('\t');
								var x:Number = Number(mvl[1]) * scale;
								var y:Number = Number(mvl[3]) * scale;
								var z:Number = Number(mvl[2]) * scale;
								vertices.push(new Vertex3D(x, y, z));
							}
						}
						catch(e:Error)
						{
							Papervision3D.log("MESH_VERTEX_LIST error");
						}
						break;
					
					case 'MESH_FACE_LIST':
						try
						{
							while((content = String(lines.shift())).indexOf('}') < 0)
							{
								content = content.split("*")[1];
								var mfl:String = content.split('\t')[0];
								var drc:Array = mfl.split(':');
								var con:String;
								con = drc[2]
								var a:Vertex3D = vertices[int(con.substr(0, con.lastIndexOf(' ')))];
								con = drc[3];
								var b:Vertex3D = vertices[int(con.substr(0, con.lastIndexOf(' ')))];
								con = drc[4];
								var c:Vertex3D = vertices[int(con.substr(0, con.lastIndexOf(' ')))];
								faces.push(new Face3D([a, b, c], null, [new NumberUV(), new NumberUV(), new NumberUV()]));
							}
						}
						catch(e:Error)
						{
							Papervision3D.log("MESH_FACE_LIST : ");
						}
						break;

					case 'MESH_TVERTLIST':
						try
						{
							while((content = String(lines.shift())).indexOf('}') < 0)
							{
								content = content.split("*")[1];
								var mtvl: Array = content.split('\t');
								uvs.push(new NumberUV(parseFloat(mtvl[1]), parseFloat(mtvl[2])));
							}
						}
						catch(e:Error)
						{
							Papervision3D.log("MESH_TVERTLIST error" + e.message);
						}
						break;
						
					case 'MESH_TFACELIST':
						try
						{
							var num:int = 0;
							while((content = String(lines.shift())).indexOf( '}') < 0)
							{
								content = content.substr(content.indexOf( '*' ) + 1);
								var mtfl:Array = content.split('\t');
								var faceUV:Array = faces[num].uv;
								faceUV[0] = uvs[parseInt(mtfl[1])];
								faceUV[1] = uvs[parseInt(mtfl[2])];
								faceUV[2] = uvs[parseInt(mtfl[3])];
								num++;
							}
						}
						catch(e:Error)
						{
							Papervision3D.log("MESH_TFACELIST ERROR" + e.message);
						}
						break;
				}
			}
			this.geometry.ready = true;
			var fileEvent:FileLoadEvent = new FileLoadEvent(FileLoadEvent.LOAD_COMPLETE, _filename);
			dispatchEvent(fileEvent);
			Papervision3D.log("Parsed ASE: " + this._filename + " [vertices:" + vertices.length + " faces:" + faces.length + "]");
		}
	}
}

