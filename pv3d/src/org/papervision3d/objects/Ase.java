package org.papervision3d.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.papervision3d.Papervision3D;
import org.papervision3d.core.NumberUV;
import org.papervision3d.core.geom.Face3D;
import org.papervision3d.core.geom.Mesh3D;
import org.papervision3d.core.geom.Vertex3D;
import org.papervision3d.core.proto.MaterialObject3D;
import org.papervision3d.events.FileLoadEvent;

import flash.TopLevel;
import flash.events.Event;
import flash.events.IOErrorEvent;
import flash.events.ProgressEvent;
import flash.net.URLLoader;
import flash.net.URLRequest;

/**
 * ASE格式文件加载器，转为网格
 */
public class Ase extends Mesh3D {
	//默认缩放（没有使用？）
	static public double DEFAULT_SCALING = 1;
	//内部缩放（用于加载）
	static public double INTERNAL_SCALING = 50;
	
	//缩放因子
	private double _scaleAse;
	//加载器
	private URLLoader _loaderAse;
	//URL
	private String _filename;
	
	/**
	 * ASE格式文件加载器
	 * @param	material 材质
	 * @param	filename URL
	 * @param	scale 缩放因子
	 * @param	initObject 附加对象
	 */
	public Ase(MaterialObject3D material, String filename, double scale, Map<String, Object> initObject) {
		super(material, new ArrayList<Vertex3D>(), new ArrayList<Face3D>(), null, initObject);
		this._scaleAse = scale;
		this._filename = filename;
		loadAse(filename);
	}
	public Ase(MaterialObject3D material, String filename, double scale) {
		this(material , filename, scale, null);
	}
	
	/**
	 * 开始加载文件
	 * @param	filename ASE文件名
	 */
	private void loadAse(String filename) {
		_loaderAse = new URLLoader() {
			public void _onEvent(String listener, Event event) {
				if (listener != null) {
					if ("parseAse".equals(listener)) {
						parseAse(event);
					} else if ("progressHandler".equals(listener)) {
						progressHandler((ProgressEvent)event);
					} else if ("ioErrorHandler".equals(listener)) {
						ioErrorHandler((IOErrorEvent)event);
					}
				}
			}
		};
		_loaderAse.addEventListener(Event.COMPLETE, "parseAse");
		_loaderAse.addEventListener(ProgressEvent.PROGRESS, "progressHandler");
		_loaderAse.addEventListener(IOErrorEvent.IO_ERROR, "ioErrorHandler");
		URLRequest request = new URLRequest(filename);
		try {
			_loaderAse.load(request);
		} catch (Throwable e) {
			Papervision3D.log("error in loading ase file");
		}
	}
	
	/**
	 * 出错，发送FileLoadEvent事件，携带相关文件名
	 * @param	event
	 * @throws Exception 
	 */
	private void ioErrorHandler(IOErrorEvent event) {
		FileLoadEvent fileEvent = new FileLoadEvent(FileLoadEvent.LOAD_ERROR, _filename);
		dispatchEvent(fileEvent);
		throw new RuntimeException("Ase: ioErrorHandler Error.");
	}
	
	/**
	 * 加载进度
	 * @param	event
	 */
	private void progressHandler(ProgressEvent event) {
		Papervision3D.log("progressHandler loaded:" + event.getBytesLoaded() + " total: " + event.getBytesTotal());
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
	private void parseAse(Event e) {
		double scale = this._scaleAse;
		scale *= INTERNAL_SCALING;
		List<Vertex3D> vertices = this.geometry.vertices = new ArrayList<Vertex3D>();
		List<Face3D> faces = this.geometry.faces = new ArrayList<Face3D>();
		URLLoader loader = (URLLoader)e.getTarget();
		Queue<String> lines = new LinkedList<String>(Arrays.asList(TopLevel.unescape((String)loader.data).split("\r\n"))); 
		String line;
		String chunk;
		String content;
		List<NumberUV> uvs = new ArrayList<NumberUV>();
//		MaterialObject3D material = this.material;
		while (lines.size() > 0) {
			line = (String)lines.poll();
			line = line.substring(line.indexOf('*') + 1);
			if (line.indexOf('}') >= 0) {
				line = "";
			}
			chunk = line.substring(0, line.indexOf(' '));
			if ("MESH_VERTEX_LIST".equals(chunk)) {
				try {
					while((content = (String)lines.poll()).indexOf('}') < 0) {
						content = content.split("*")[1];
						String[] mvl = content.split("\t");
						double x = Double.parseDouble(mvl[1]) * scale;
						double y = Double.parseDouble(mvl[3]) * scale;
						double z = Double.parseDouble(mvl[2]) * scale;
						vertices.add(new Vertex3D(x, y, z));
					}
				} catch (Throwable e2) {
					Papervision3D.log("MESH_VERTEX_LIST error");
				}
			} else if("MESH_FACE_LIST".equals(chunk)) {
				try {
					while((content = (String)lines.poll()).indexOf('}') < 0) {
						content = content.split("*")[1];
						String mfl = content.split("\\t")[0];
						String drc[] = mfl.split(":");
						String con;
						con = drc[2];
						Vertex3D a = vertices.get(Integer.parseInt(con.substring(0, con.lastIndexOf(' '))));
						con = drc[3];
						Vertex3D b = vertices.get(Integer.parseInt(con.substring(0, con.lastIndexOf(' '))));
						con = drc[4];
						Vertex3D c = vertices.get(Integer.parseInt(con.substring(0, con.lastIndexOf(' '))));
						faces.add(new Face3D(new ArrayList<Vertex3D>(Arrays.asList(a, b, c)), null, 
								new ArrayList<NumberUV>(Arrays.asList(new NumberUV(), new NumberUV(), new NumberUV()))));
					}
				} catch (Throwable e2) {
					Papervision3D.log("MESH_FACE_LIST : ");
				}
			} else if ("MESH_TVERTLIST".equals(chunk)) {
				try {
					while ((content = (String)lines.poll()).indexOf('}') < 0) {
						content = content.split("*")[1];
						String[] mtvl = content.split("\\t");
						uvs.add(new NumberUV(Double.parseDouble(mtvl[1]), Double.parseDouble(mtvl[2])));
					}
				} catch (Throwable e2) {
					Papervision3D.log("MESH_TVERTLIST error" + e2.getMessage());
				}
			} else if ("MESH_TFACELIST".equals(chunk)) {
				try {
					int num = 0;
					while ((content = (String)lines.poll()).indexOf( '}') < 0) {
						content = content.substring(content.indexOf( '*' ) + 1);
						String[] mtfl = content.split("\\t");
						List<NumberUV> faceUV = faces.get(num).uv;
						faceUV.set(0, uvs.get(Integer.parseInt(mtfl[1])));
						faceUV.set(1, uvs.get(Integer.parseInt(mtfl[2])));
						faceUV.set(2, uvs.get(Integer.parseInt(mtfl[3])));
						num++;
					}
				} catch (Throwable e2) {
					Papervision3D.log("MESH_TFACELIST ERROR" + e2.getMessage());
				}
			}
		}
		this.geometry.ready = true;
		FileLoadEvent fileEvent = new FileLoadEvent(FileLoadEvent.LOAD_COMPLETE, _filename);
		dispatchEvent(fileEvent);
		Papervision3D.log("Parsed ASE: " + this._filename + " [vertices:" + vertices.size() + " faces:" + faces.size() + "]");
	}	
}
