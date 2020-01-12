package org.papervision3d.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.papervision3d.Papervision3D;
import org.papervision3d.core.Matrix3D;
import org.papervision3d.core.NumberUV;
import org.papervision3d.core.geom.Face3D;
import org.papervision3d.core.geom.Mesh3D;
import org.papervision3d.core.geom.Vertex3D;
import org.papervision3d.core.proto.DisplayObjectContainer3D;
import org.papervision3d.core.proto.GeometryObject3D;
import org.papervision3d.core.proto.MaterialObject3D;
import org.papervision3d.events.FileLoadEvent;
import org.papervision3d.materials.ColorMaterial;
import org.papervision3d.materials.MaterialsList;
import org.papervision3d.materials.WireframeMaterial;
import org.papervision3d.objects.DisplayObject3D;

import flash.XML;
import flash.XMLList;
import flash.events.Event;
import flash.events.EventDispatcher;
import flash.net.URLLoader;
import flash.net.URLRequest;

/**
 * DAE文件读取器，把某个文件转为三维对象，放入container中
 */
public class Collada extends EventDispatcher {
	public static double DEFAULT_SCALING = 1;
	private static double INTERNAL_SCALING = 100;
	private static double toDEGREES = 180 / Math.PI;
	private static double toRADIANS = Math.PI / 180;
	
	private static final String COLLADASECTION = "COLLADA";
	private static final String LIBRARYSECTION = "library";
	private static final String ASSETSECTION = "asset";
	private static final String SCENESECTION = "scene";
	private static final String LIGHTPREFAB = "light";
	private static final String CAMERAPREFAB = "camera";
	private static final String MATERIALSECTION = "material";
	private static final String GEOMETRYSECTION = "geometry";
	private static final String MESHSECTION = "mesh";
	private static final String SOURCESECTION = "source";
	private static final String ARRAYSECTION = "array";
	private static final String ACCESSORSECTION = "accessor";
	private static final String VERTICESSECTION = "vertices";
	private static final String INPUTTAG = "input";
	private static final String POLYGONSSECTION = "polygons";
	private static final String POLYGON = "p";
	private static final String NODESECTION = "node";
	private static final String LOOKATNODE = "lookat";
	private static final String MATRIXNODE = "matrix";
	private static final String PERSPECTIVENODE = "perspective";
	private static final String ROTATENODE = "rotate";
	private static final String SCALENODE = "scale";
	private static final String TRANSLATENODE = "translate";
	private static final String SKEWNODE = "skew";
	private static final String INSTANCENODE = "instance";
	private static final String INSTACESCENE = "instance_visual_scene";
	private static final String PARAMTAG = "param";
	private static final String POSITIONINPUT = "POSITION";
	private static final String VERTEXINPUT = "VERTEX";
	private static final String NORMALINPUT = "NORMAL";
	private static final String TEXCOORDINPUT = "TEXCOORD";
	private static final String UVINPUT = "UV";
	private static final String TANGENTINPUT = "TANGENT";
	
	//最原始加载的XML数据
	private XML COLLADA;
	//输出的三维对象容器
	private DisplayObjectContainer3D _container;
	//几何数据，未使用
	private Object _geometries;
	//加载器
	private URLLoader _loader;
	//URL，加载和发送消息用
	private String _filename;
	//材质列表
	private MaterialsList _materials;
	//缩放因子
	private double _scaling;
	//回调，未使用
	private Runnable _callback;
	//DAE文件指定的模型方向
	private boolean _yUp;	
	
	/**
	 * 构造函数
	 * @param	container 呈现的三维容器
	 * @param	filename URL
	 * @param	materials 材质列表
	 * @param	scale 缩放
	 * @param	callback 回调
	 * @param	initObject 附加数据
	 */	
	public Collada(DisplayObjectContainer3D container, 
			String filename, MaterialsList materials, 
			double scale, Runnable callback, Object initObject) {
		this._container = container;
		this._filename = filename;
		this._materials = materials;
		this._scaling = scale != 0 ? scale : DEFAULT_SCALING;
		this._scaling *= INTERNAL_SCALING;
		this._callback = callback;
		this._geometries = new Object();
		loadCollada();		
	}
	public Collada(DisplayObjectContainer3D container, 
			String filename, MaterialsList materials, 
			double scale) {
		this(container, filename, materials, scale, null, null);
	}
	
	/**
	 * 开始加载
	 */
	private void loadCollada() {
		this._loader = new URLLoader();
		this._loader.addEventListener(Event.COMPLETE, "onComplete");
		this._loader.load(new URLRequest(this._filename));
	}
	
	/**
	 * 完成事件
	 * @param	evt
	 */
	private void onComplete(Event evt) {
		COLLADA = new XML((String)this._loader.data);
		buildCollada();
	}
	
	/**
	 * 构造DAE对象
	 */
	private void buildCollada() {
		//default xml namespace = COLLADA.namespace();
		this._yUp = (COLLADA.findXML("asset").findString("up_axis").equals("Y_UP"));
		String sceneId = getId(COLLADA.findXML("scene").findXML("instance_visual_scene").findString("@url"));
		XML scene = COLLADA.findXML("library_visual_scenes").findXML("visual_scene").findXML("@id", sceneId).get(0);
		//解析
		parseScene(scene);
		FileLoadEvent fileEvent = new FileLoadEvent(FileLoadEvent.LOAD_COMPLETE, _filename);
		this.dispatchEvent(fileEvent);
	}
	
	//开始解析每个XML节点
	private void parseScene(XML scene) {
		for (XML node : scene.getNode()) {
			parseNode(node, this._container);
		}
	}
	
	/**
	 * 解析单个节点
	 * @param	node 单个节点
	 * @param	parent 三维容器
	 */
	private void parseNode(XML node, DisplayObjectContainer3D parent) {
		Matrix3D matrix = Matrix3D.getIDENTITY();
		DisplayObject3D newNode;
		if (((String)node.findString("instance_geometry")).equals("")) {
			newNode = new DisplayObject3D(node.findString("@name"));
		} else {
			newNode = new Mesh3D(null, null, null, node.findString("@name"));
		}
		DisplayObject3D instance = parent.addChild(newNode, node.findString("@name"));
		XMLList children = node.children();
		int totalChildren = children.length();
		for (int i = 0; i < totalChildren; i++) {
			XML child = children.get(i);
			String localName = child.name().get("localName");
			if ("translate".equals(localName)) {
				matrix = Matrix3D.multiply(matrix, translateMatrix(getArray(child.ToString())));
			} else if ("rotate".equals(localName)) {
				matrix = Matrix3D.multiply(matrix, rotateMatrix(getArray(child.ToString())));
			} else if ("scale".equals(localName)) {
				matrix = Matrix3D.multiply(matrix, scaleMatrix(getArray(child.ToString())));
			} else if ("matrix".equals(localName)) {
				matrix = Matrix3D.multiply(matrix, new Matrix3D(getArray(child.ToString())));
			} else if ("node".equals(localName)) {
				parseNode(child, instance);
			} else if ("instance_geometry".equals(localName)) {
				for (XML geometry : child.getNode()) { //FIXME:??? no getNode()???
					String geoId = getId(geometry.findString("@url"));
					XML geo = COLLADA.findXML("library_geometries").findXML("geometry").findXML("@id", geoId).get(0);
					parseGeometry(geo, instance, Matrix3D.clone(matrix));
				}
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
	private void parseGeometry(XML geometry, DisplayObject3D instance, Matrix3D matrix2) {
		Matrix3D matrix_ = Matrix3D.clone(matrix2);
		Matrix3D matrix = matrix_ != null ? matrix_ : Matrix3D.getIDENTITY();
		Map<String, Object> semantics = new HashMap<String, Object>();
		semantics.put("name", geometry.findString("@id"));
		List<Object> faces = new ArrayList<Object>(); semantics.put("triangles", faces);
		boolean multiMaterial = (geometry.findXML("mesh").findXMLs("triangles").size() > 1);
		for (XML triangles : geometry.findXML("mesh").findXMLs("triangles")) {
			List<String> field = new ArrayList<String>();
			for (XML input : triangles.findXMLs("input")) {
				semantics.put(input.findString("@semantic"), deserialize(input, geometry));
				field.add(input.findString("@semantic"));
			}
			Queue<String> data = new LinkedList<String>(Arrays.asList(triangles.findString("p").split(" ")));
			int len = triangles.findInt("@count");
			String material = triangles.findString("@material");
			addMaterial(instance, material);
			for (int j = 0; j < len; j++) {
				Map<String, Object> t = new HashMap<String, Object>();
				for (int v = 0; v < 3; v++) {
					String fld;
					for (int k = 0; (fld = field.get(k)) != null; k++) {
						if (null == t.get(fld)) {
							t.put(fld, new ArrayList<Double>());
						}
						((List<Double>)t.get(fld)).add(Double.parseDouble(data.poll()));
					}
					t.put("material", material);
				}
				faces.add( t );
			}
		}
		buildObject(semantics, instance, matrix);
	}
	private void parseGeometry(XML geometry, DisplayObject3D instance) {
		this.parseGeometry(geometry, instance, null);
	}
	
	/**
	 * 构建对象
	 * @param	semantics
	 * @param	instance
	 * @param	matrix
	 */
	private void buildObject(Map<String, Object> semantics, DisplayObject3D instance, Matrix3D matrix) {
		matrix = matrix != null ? matrix : Matrix3D.getIDENTITY();
		instance.addGeometry(new GeometryObject3D());
		List<Vertex3D> vertices = instance.geometry.vertices = new ArrayList<Vertex3D>();
		double scaling = this._scaling;
		int accVerts = vertices.size();
		ArrayList<Object> semVertices = (ArrayList<Object>)semantics.get("VERTEX");
		int len = semVertices.size();
		int i;
		for(i = 0; i < len; i++) {
			Map<String, String> vert = (Map<String, String>)semVertices.get(i);
			double x = Double.parseDouble(vert.get("X")) * scaling;
			double y = Double.parseDouble(vert.get("Y")) * scaling;
			double z = Double.parseDouble(vert.get("Z")) * scaling;
			if (this._yUp) {
				vertices.add(new Vertex3D(-x, y, z));
			} else {
				vertices.add(new Vertex3D(x, z, y));
			}
		}
		List<Face3D> faces = instance.geometry.faces = new ArrayList<Face3D>();
		List<Object> semFaces = (List<Object>)semantics.get("triangles");
		len = semFaces.size();
		for (i = 0; i < len; i++) {
			List<Integer> tri = ((Map<String, List<Integer>>)semFaces.get(i)).get("VERTEX");
			Vertex3D a = vertices.get(accVerts + tri.get(0));
			Vertex3D b = vertices.get(accVerts + tri.get(1));
			Vertex3D c = vertices.get(accVerts + tri.get(2));
			List<Vertex3D> faceList = new ArrayList<Vertex3D>(Arrays.asList(a, b, c));
			List<Map<String, Double>> tex = (List<Map<String, Double>>)semantics.get("TEXCOORD");
			List<Integer> uv = ((Map<String, List<Integer>>)semFaces.get(i)).get("TEXCOORD");
			List<NumberUV> uvList; NumberUV uvA; NumberUV uvB; NumberUV uvC;
			if (uv != null && tex != null) {
				uvA = new NumberUV(tex.get(uv.get(0)).get("S"), tex.get(uv.get(0)).get("T"));
				uvB = new NumberUV(tex.get(uv.get(1)).get("S"), tex.get(uv.get(1)).get("T"));
				uvC = new NumberUV(tex.get(uv.get(2)).get("S"), tex.get(uv.get(2)).get("T"));
				uvList = new ArrayList<NumberUV>(Arrays.asList(uvA, uvB, uvC));
			} else {
				uvList = null;
			}
			String materialName = ((Map<String, String>)semFaces.get(i)).get("material"); if (materialName == null) materialName = null;
			Face3D face = new Face3D(faceList, materialName, uvList);
			faces.add(face);
		}
		instance.geometry.ready = true;
		matrix.n14 *= scaling;
		matrix.n24 *= scaling;
		matrix.n34 *= scaling;
		instance.material = new ColorMaterial(0xFF0000, 0.25 );
		instance.visible  = true;
	}
	private void buildObject(Map<String, Object> semantics, DisplayObject3D instance) {
		this.buildObject(semantics, instance, null);
	}
	
	/**
	 * 把字符串转为数字数组
	 * @param	spaced
	 * @return
	 */
	private List<Double> getArray(String spaced) {
		String[] strings = spaced.split(" ");
		List<Double> numbers = new ArrayList<Double>();
		int totalStrings = strings.length;
		for (int i =0; i < totalStrings; i++) {
			numbers.add(Double.parseDouble(strings[i]));
		}
		return numbers;
	}
	
	/**
	 * 添加材质
	 * @param	instance 三维显示对象
	 * @param	name 名称
	 */
	private void addMaterial(DisplayObject3D instance, String name) {
		MaterialObject3D material = this._materials.getMaterialByName(name);
		if (material == null) {
			material = new WireframeMaterial((int)(Math.random() * 0xFFFFFF));
			material.name = name;
			material.fillAlpha = 1;
			material.fillColor = 0;
			material.oneSide = true;
			Papervision3D.log("Collada material " + name + " not found." );
		}
		if (instance.materials == null) {
			instance.materials = new MaterialsList();
		}
		instance.materials.addMaterial(material, name);
	}
	
	/**
	 * 旋转矩阵变换
	 * @param	vector
	 * @return
	 */
	private Matrix3D rotateMatrix(List<Double> vector) {
		if (this._yUp) {
			return Matrix3D.rotationMatrix(vector.get(0), vector.get(1), vector.get(2), -vector.get(3) * toRADIANS);
		} else {
			return Matrix3D.rotationMatrix(vector.get(0), vector.get(2), vector.get(1), -vector.get(3) * toRADIANS);
		}
	}
	
	/**
	 * 平移矩阵变换
	 * @param	vector
	 * @return
	 */
	private Matrix3D translateMatrix(List<Double> vector) {
		if (this._yUp) {
			return Matrix3D.translationMatrix( -vector.get(0) * this._scaling, vector.get(1) * this._scaling, vector.get(2) * this._scaling );
		} else {
			return Matrix3D.translationMatrix(  vector.get(0) * this._scaling, vector.get(2) * this._scaling, vector.get(1) * this._scaling );
		}
	}

	/**
	 * 缩放矩阵变换
	 * @param	vector 缩放向量[x, y, z]
	 * @return
	 */
	private Matrix3D scaleMatrix(List<Double> vector) {
		if (this._yUp) {
			return Matrix3D.scaleMatrix(vector.get(0), vector.get(1), vector.get(2));
		} else {
			return Matrix3D.scaleMatrix(vector.get(0), vector.get(2), vector.get(1));
		}
	}
	
	/**
	 * 反序列化
	 * @param	input
	 * @param	geo
	 * @return
	 */
	private List<Object> deserialize(XML input, XML geo) {
		List<Object> output = new ArrayList<Object>();
		String id = input.findString("@source").split("#")[1];
		XMLList acc = geo.findXMLChild("source").findXML("@id", id).findXML("technique_common").findXMLList("accessor");
		if (acc.length() != 0) {
			String floId = acc.findString("@source").split("#")[1];
			XMLList floXML = COLLADA.findXMLChild("float_array").findXMLList("@id", floId);
			String floStr = floXML.toString();
			Queue<String> floats = new LinkedList<String>(Arrays.asList(floStr.split(" ")));
			List<String> params = new ArrayList<String>();
			for (XML par : acc.findXML("param")) {
				params.add(par.findString("@name"));
			}
			int count = acc.findInt("@count");
			int stride = acc.findInt("@stride");
			for (int i = 0; i < count; i++) {
				Map<String, String> element = new HashMap<String, String>();
				for (int j = 0; j < stride; j++) {
					element.put(params.get(j), floats.poll());
				}
				output.add(element);
			}
		} else {
			XMLList recursive = geo.findXMLChild("vertices").findXML("@id", id).findXMLList("INPUTTAG");
			output = deserialize(recursive.get(0), geo);
		}
		return output;
	}
	
	/**
	 * 根据字符串获取#的ID
	 * @param	url
	 * @return
	 */
	private String getId(String url) {
		return url.split("#")[1];
	}
	
	public void _onEvent(String listener, Event event) {
		if (listener != null) {
			if ("onComplete".equals(listener)) {
				onComplete(event);
			}
		}
	}	
}
