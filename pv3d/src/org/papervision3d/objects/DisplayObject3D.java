package org.papervision3d.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.papervision3d.Papervision3D;
import org.papervision3d.core.Matrix3D;
import org.papervision3d.core.Number3D;
import org.papervision3d.core.Quaternion;
import org.papervision3d.core.geom.Face3D;
import org.papervision3d.core.geom.MatrixMap;
import org.papervision3d.core.geom.Vertex2D;
import org.papervision3d.core.geom.Vertex3D;
import org.papervision3d.core.proto.CameraObject3D;
import org.papervision3d.core.proto.DisplayObjectContainer3D;
import org.papervision3d.core.proto.GeometryObject3D;
import org.papervision3d.core.proto.MaterialObject3D;
import org.papervision3d.core.proto.SceneObject3D;
import org.papervision3d.materials.MaterialsList;

import flash.display.Sprite;

/**
 * 3D显示对象
 */
public class DisplayObject3D extends DisplayObjectContainer3D {
	private static int _totalDisplayObjects = 0;
	private static double toDEGREES = 180 / Math.PI;
	private static double toRADIANS = Math.PI / 180;
	private static Number3D FORWARD = new Number3D(0, 0, 1);
	private static Number3D BACKWARD = new Number3D(0, 0, -1);
	private static Number3D LEFT = new Number3D(-1, 0, 0);
	private static Number3D RIGHT = new Number3D(1, 0, 0);
	private static Number3D UP = new Number3D(0, 1, 0);
	private static Number3D DOWN = new Number3D(0, -1, 0);
	
	/**
	 * 平移矩阵是否需要重新计算
	 */
	protected boolean _transformDirty = false;
	//缓存的旋转角度
	private double _rotationX;
	private double _rotationY;
	private double _rotationZ;
	/**
	 * 旋转矩阵是否需要重新计算
	 */
	private boolean _rotationDirty = false;
	private double _scaleX;
	private double _scaleY;
	private double _scaleZ;
	private boolean _scaleDirty = false;
	protected List<Face3D> _sorted;
	public boolean visible;
	public String name;
	public int id;
	public Object extra;
	public Sprite container;
	public MaterialObject3D material;
	public MaterialsList materials;
	public SceneObject3D scene;
	public DisplayObjectContainer3D parent;
	//自身的转换矩阵（包含其位置信息）
	public Matrix3D transform;
	public Matrix3D view;
	//投影? Vertex2D[]
	public Map<Vertex3D, Vertex2D> projected;
	public Map<Face3D, MatrixMap> projected_;
	public List<Face3D> faces = new ArrayList<Face3D>();
	/**
	 * 几何数据（面点，UV数据）
	 */
	public GeometryObject3D geometry;
	//排序用，Z排序
	public double screenZ;
	
	/**
	 * 横
	 */
	public double getX() {
		return this.transform.n14;
	}
	
	/**
	 * 横
	 */
	public void setX(double value) {
		this.transform.n14 = value;
	}
	
	/**
	 * 纵
	 */
	public double getY() {
		return this.transform.n24;
	}
	
	/**
	 * 纵
	 */
	public void setY(double value) {
		this.transform.n24 = value;
	}
	
	/**
	 * 深
	 */
	public double getZ() {
		return this.transform.n34;
	}
	
	/**
	 * 深
	 */
	public void setZ(double value) {
		this.transform.n34 = value;
	}
	
	/**
	 * 绕X轴旋转度数
	 */
	public double getRotationX() {
		if (this._rotationDirty) {
			updateRotation();
		}
		return Papervision3D.useDEGREES ? -this._rotationX * toDEGREES : -this._rotationX;
	}
	
	/**
	 * 设置绕X轴旋转度数
	 */
	public void setRotationX(double rot) {
		this._rotationX = Papervision3D.useDEGREES ? -rot * toRADIANS : -rot;
		this._transformDirty = true;
	}
	
	/**
	 * 绕Y轴旋转度数
	 */
	public double getRotationY() {
		if (this._rotationDirty) {
			updateRotation();
		}
		return Papervision3D.useDEGREES ? -this._rotationY * toDEGREES : -this._rotationY;
	}
	
	/**
	 * 设置绕Y轴旋转度数
	 */
	public void setRotationY(double rot) {
		this._rotationY = Papervision3D.useDEGREES ? -rot * toRADIANS : -rot;
		this._transformDirty = true;
	}
	
	/**
	 * 设置绕Z轴旋转度数
	 */
	public double getRotationZ() {
		if (this._rotationDirty) {
			updateRotation();
		}
		return Papervision3D.useDEGREES ? -this._rotationZ * toDEGREES : -this._rotationZ;
	}

	/**
	 * 绕Z轴旋转度数
	 */
	public void setRotationZ(double rot) {
		this._rotationZ = Papervision3D.useDEGREES? -rot * toRADIANS : -rot;
		this._transformDirty = true;
	}
	
	/**
	 * 重新计算旋转矩阵
	 */
	private void updateRotation() {
		Number3D rot = Matrix3D.matrix2euler(this.transform);
		this._rotationX = rot.x * toRADIANS;
		this._rotationY = rot.y * toRADIANS;
		this._rotationZ = rot.z * toRADIANS;
		this._rotationDirty = false;
	}
	
	/**
	 * 整体缩放因子
	 */
	public void setScale(double scale) {
		if (Papervision3D.usePERCENT) {
			scale /= 100;
		}
		this._scaleX = this._scaleY = this._scaleZ = scale;
		this._transformDirty = true;
	}
	
	/**
	 * 整体缩放
	 */
	public void setScaleX(double scale) {
		if (Papervision3D.usePERCENT) {
			this._scaleX = scale / 100;
		} else {
			this._scaleX = scale;
		}
		this._transformDirty = true;
	}
	
	/**
	 * Y轴缩放
	 */
	public void setScaleY(double scale) {
		if (Papervision3D.usePERCENT) {
			this._scaleY = scale / 100;
		} else {
			this._scaleY = scale;
		}
		this._transformDirty = true;
	}
	
	/**
	 * Z轴缩放
	 */
	public void setScaleZ(double scale) {
		if (Papervision3D.usePERCENT) {
			this._scaleZ = scale / 100;
		} else {
			this._scaleZ = scale;
		}
		this._transformDirty = true;
	}
	
	/**
	 * 初始显示对象，用于初始化镜头
	 */
	static public DisplayObject3D getZERO() {
		return new DisplayObject3D();
	}
	
	public DisplayObject3D() {
		this(null, null, null);
	}
		
	/**
	 * 构造函数
	 * @param	name 名称，用于三维显示容器的列表
	 * @param	geometry 三维几何数据
	 * @param	initObject 附加数据
	 */
	public DisplayObject3D(String name, GeometryObject3D geometry, Map<String, Object> initObject) {
		super();
		Papervision3D.log("DisplayObject3D: " + name);
		this.transform = Matrix3D.getIDENTITY();
		this.view = Matrix3D.getIDENTITY();
		this.setX((initObject != null && initObject.get("x") != null) ? (Double)initObject.get("x") : 0);
		this.setY((initObject != null && initObject.get("y") != null) ? (Double)initObject.get("y") : 0);
		this.setZ((initObject != null && initObject.get("z") != null) ? (Double)initObject.get("z") : 0);
		this.setRotationX((initObject != null && initObject.get("rotationX") != null) ? (Double)initObject.get("rotationX") : 0);
		this.setRotationY((initObject != null && initObject.get("rotationY") != null) ? (Double)initObject.get("rotationY") : 0);
		this.setRotationZ((initObject != null && initObject.get("rotationZ") != null) ? (Double)initObject.get("rotationZ") : 0);
		double scaleDefault = Papervision3D.usePERCENT ? 100 : 1;
		this.setScaleX((initObject != null && initObject.get("scaleX") != null) ? (Double)initObject.get("scaleX") : scaleDefault);
		this.setScaleY((initObject != null && initObject.get("scaleY") != null) ? (Double)initObject.get("scaleY") : scaleDefault);
		this.setScaleZ((initObject != null && initObject.get("scaleZ") != null) ? (Double)initObject.get("scaleZ") : scaleDefault);
		if (initObject != null && initObject.get("extra") != null) {
			this.extra = initObject.get("extra");
		}
		if (initObject != null && initObject.get("container") != null) {
			this.container = (Sprite)initObject.get("container");
		}
		this.visible = true;
		this.id = _totalDisplayObjects++;
		this.name = (name != null ? name : Integer.toString(this.id));
		if (geometry != null) {
			addGeometry(geometry);
		}
	}
	
	public void addGeometry() {
		this.addGeometry(null);
	}
	
	/**
	 * 设置几何数据
	 * @param	geometry
	 */
	public void addGeometry(GeometryObject3D geometry) {
		if (geometry != null) {
			this.geometry = geometry;
		}
		this.projected = new HashMap<Vertex3D, Vertex2D>();
	}
	
	/**
	 * 质心距离
	 * @param	obj
	 * @return
	 */
	public double distanceTo(DisplayObject3D obj) {
		double x = this.getX() - obj.getX();
		double y = this.getY() - obj.getY();
		double z = this.getZ() - obj.getZ();
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	public boolean hitTestPoint(double x, double y, double z) {
		double dx = this.getX() - x;
		double dy = this.getY() - y;
		double dz = this.getZ() - z;
		double d2 = x * x + y * y + z * z;
		double sA = this.geometry != null ? this.geometry.getBoundingSphere2() : 0;
		return sA > d2;
	}

	public boolean hitTestObject(DisplayObject3D obj) {
		double dx = this.getX() - obj.getX();
		double dy = this.getY() - obj.getY();
		double dz = this.getZ() - obj.getZ();
		double d2 = dx * dx + dy * dy + dz * dz;
		double sA = this.geometry != null ? this.geometry.getBoundingSphere2() : 0;
		double sB = obj.geometry != null ? obj.geometry.getBoundingSphere2() : 0;
		return sA + sB > d2;
	}
	
	public MaterialObject3D getMaterialByName(String name) {
		MaterialObject3D material = this.materials.getMaterialByName(name);
		if (material != null) {
			return material;
		} else {
			for (DisplayObject3D child : this._childrenByName.values()) {
				material = child.getMaterialByName(name);
				if (material != null) {
					return material;
				}
			}
		}
		return null;
	}
	
	public String materialsList() {
		String list = "";
		//FIXME:
		for (String name : this.materials.materialsByName.keySet()) {
			list += name + "\n";
		}
		for (DisplayObject3D child : this._childrenByName.values()) {
			for (String name : child.materials.materialsByName.keySet()) {
				list += "+ " + name + "\n";
			}
		}
		return list;
	}
	
	public double project(DisplayObject3D parent, 
			CameraObject3D camera) {
		return this.project(parent, camera, null);
	}
	
	/**
	 * ！！！关键代码！！！
	 * 
	 * 投影
	 * 计算深度
	 * @param	parent
	 * @param	camera
	 * @param	sorted
	 * @return
	 */
	public double project(DisplayObject3D parent, 
		CameraObject3D camera, List<Face3D> sorted) {
		if (sorted == null) {
			this._sorted = sorted = new ArrayList<Face3D>();
		}
		if (this._transformDirty) {
			updateTransform();
		}
		this.view = Matrix3D.multiply(parent.view, this.transform);
		double screenZs = 0;
		double children = 0;
		for (DisplayObject3D child : this._childrenByName.values()) {
			screenZs += child.project(this, camera, sorted);
			children++;
		}
		return this.screenZ = screenZs / children;
	}
	
	/**
	 * 旋转
	 * @param	scene
	 */
	public void render(SceneObject3D scene) {
		List<Face3D> iFaces = this._sorted;
		Collections.sort(iFaces, new Comparator<Face3D>() {
			@Override
			public int compare(Face3D arg0, Face3D arg1) {
				if (arg0.screenZ == arg1.screenZ) {
					return 0;
				} else if (arg0.screenZ > arg1.screenZ) {
					return -1;
				} else {
					return 1;
				}
			}
		});
		Sprite container = (this.container != null ? this.container : scene.container);
		double rendered = 0;
		Face3D iFace = null;
		for (int i = 0; i < iFaces.size(); i++) {
			iFace = iFaces.get(i);
			if (iFace.visible) {
				rendered += iFace.face.render(iFace.instance, container);
			}
		}
		scene.stats.rendered += rendered;
	}
	
	public void moveForward(double distance) { 
		translate(distance, FORWARD); 
	}
	
	public void moveBackward(double distance) { 
		translate(distance, BACKWARD); 
	}
	
	public void moveLeft(double distance) { 
		translate(distance, LEFT); 
	}
	
	public void moveRight(double distance) { 
		translate(distance, RIGHT); 
	}
	
	public void moveUp(double distance) { 
		translate(distance, UP); 
	}
	
	public void moveDown(double distance) { 
		translate(distance, DOWN); 
	}
	
	public void translate(double distance, Number3D axis) {
		Number3D vector = axis.clone();
		if (this._transformDirty) {
			updateTransform();
		}
		Matrix3D.rotateAxis(transform, vector);
		this.setX(this.getX() + distance * vector.x);
		this.setY(this.getY() + distance * vector.y);
		this.setZ(this.getZ() + distance * vector.z);
	}

	public void pitch(double angle) {
		angle = Papervision3D.useDEGREES ? angle * toRADIANS : angle;
		Number3D vector = RIGHT.clone();
		if (this._transformDirty) {
			updateTransform();
		}
		Matrix3D.rotateAxis(transform, vector);
		Matrix3D m = Matrix3D.rotationMatrix(vector.x, vector.y, vector.z, angle);
		this.transform.copy3x3(Matrix3D.multiply3x3(m, transform));
		this._rotationDirty = true;
	}

	public void yaw(double angle) {
		angle = Papervision3D.useDEGREES ? angle * toRADIANS : angle;
		Number3D vector = UP.clone();
		if (this._transformDirty) {
			updateTransform();
		}
		Matrix3D.rotateAxis(transform, vector);
		Matrix3D m = Matrix3D.rotationMatrix(vector.x, vector.y, vector.z, angle);
		this.transform.copy3x3(Matrix3D.multiply3x3(m, transform));
		this._rotationDirty = true;
	}
	
	/**
	 * Z轴旋转度数
	 * @param	angle
	 */
	public void roll(double angle) {
		angle = Papervision3D.useDEGREES ? angle * toRADIANS : angle;
		Number3D vector = FORWARD.clone();
		if (this._transformDirty) {
			updateTransform();
		}
		Matrix3D.rotateAxis(transform, vector);
		Matrix3D m = Matrix3D.rotationMatrix(vector.x, vector.y, vector.z, angle);
		this.transform.copy3x3(Matrix3D.multiply3x3(m, transform));
		this._rotationDirty = true;
	}
	
	public void lookAt(DisplayObject3D targetObject) {
		this.lookAt(targetObject, null);
	}
	
	/**
	 * 用于镜头？
	 * @param	targetObject
	 * @param	upAxis
	 */
	public void lookAt(DisplayObject3D targetObject, Number3D upAxis) {
		Number3D position = new Number3D(this.getX(), this.getY(), this.getZ());
		Number3D target = new Number3D(targetObject.getX(), targetObject.getY(), targetObject.getZ());
		Number3D zAxis = Number3D.sub(target, position);
		zAxis.normalize();
		if (zAxis.getModulo() > 0.1) {
			Number3D xAxis = Number3D.cross(zAxis, upAxis != null ? upAxis : UP);
			xAxis.normalize();
			Number3D yAxis = Number3D.cross(zAxis, xAxis);
			yAxis.normalize();
			Matrix3D look = this.transform;
			look.n11 = xAxis.x;
			look.n21 = xAxis.y;
			look.n31 = xAxis.z;
			look.n12 = -yAxis.x;
			look.n22 = -yAxis.y;
			look.n32 = -yAxis.z;
			look.n13 = zAxis.x;
			look.n23 = zAxis.y;
			look.n33 = zAxis.z;
			this._transformDirty = false;
			this._rotationDirty = true;
		} else {
			System.out.println("lookAt Error");
		}
	}
	
	/**
	 * 复制位置
	 * @param	reference 显示对象或矩阵
	 */
	public void copyPosition(Object reference) {
		Matrix3D trans = this.transform;
		Matrix3D matrix = (reference instanceof DisplayObject3D) ? 
			((DisplayObject3D)reference).transform : 
			(Matrix3D)reference;
		trans.n14 = matrix.n14;
		trans.n24 = matrix.n24;
		trans.n34 = matrix.n34;
	}
	
	/**
	 * 复制转换矩阵
	 * @param	reference 显示对象或矩阵
	 */
	public void copyTransform(Object reference) {
		Matrix3D trans = this.transform;
		Matrix3D matrix = (reference instanceof DisplayObject3D) ? 
					((DisplayObject3D)reference).transform : 
					(Matrix3D)reference;
		trans.n11 = matrix.n11;
		trans.n12 = matrix.n12;
		trans.n13 = matrix.n13;	
		trans.n14 = matrix.n14;
		trans.n21 = matrix.n21;		
		trans.n22 = matrix.n22;
		trans.n23 = matrix.n23;		
		trans.n24 = matrix.n24;
		trans.n31 = matrix.n31;		
		trans.n32 = matrix.n32;
		trans.n33 = matrix.n33;		
		trans.n34 = matrix.n34;
		this._transformDirty = false;
		this._rotationDirty = true;
	}
	
	/**
	 * 更新平移矩阵
	 */
	protected void updateTransform() {
		Quaternion q = Matrix3D.euler2quaternion(-this._rotationY, -this._rotationZ, this._rotationX);
		Matrix3D m = Matrix3D.quaternion2matrix(q.x, q.y, q.z, q.w);
		Matrix3D transform = this.transform;
		m.n14 = transform.n14;
		m.n24 = transform.n24;
		m.n34 = transform.n34;
		transform.copy(m);
		Matrix3D scaleM = Matrix3D.getIDENTITY();
		scaleM.n11 = this._scaleX;
		scaleM.n22 = this._scaleY;
		scaleM.n33 = this._scaleZ;
		this.transform = Matrix3D.multiply(transform, scaleM);
		this._transformDirty = false;
	}
	
	/**
	 * 字符串形式
	 * @return
	 */
	public String toString() {
		return this.name + 
			": x:" + 
			Math.round(this.getX()) + 
			" y:" + 
			Math.round(this.getY()) + 
			" z:" + 
			Math.round(this.getZ());
	}
}
