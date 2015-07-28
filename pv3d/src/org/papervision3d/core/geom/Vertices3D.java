package org.papervision3d.core.geom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.papervision3d.core.Matrix3D;
import org.papervision3d.core.Number3D;
import org.papervision3d.core.proto.CameraObject3D;
import org.papervision3d.core.proto.GeometryObject3D;
import org.papervision3d.objects.DisplayObject3D;

/**
 * 顶点集合
 */
public class Vertices3D extends DisplayObject3D {
	/**
	 * 构造函数
	 * @param	vertices 顶点数组
	 * @param	name 名称
	 * @param	initObject 附加数据
	 */
	public Vertices3D(List<Vertex3D> vertices, String name, Map<String, Object> initObject) {
		super(name, new GeometryObject3D(), initObject);
		this.geometry.vertices = vertices != null ? vertices : new ArrayList<Vertex3D>();
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
	public double project(DisplayObject3D parent, 
		CameraObject3D camera, List<Face3D> sorted) {
		super.project(parent, camera, sorted);
		Map<Vertex3D, Vertex2D> projected = this.projected;
		Matrix3D view = this.view;
		double m11 = view.n11;
		double m12 = view.n12;
		double m13 = view.n13;
		double m21 = view.n21;
		double m22 = view.n22;
		double m23 = view.n23;
		double m31 = view.n31;
		double m32 = view.n32;
		double m33 = view.n33;
		List<Vertex3D> vertices = this.geometry.vertices;
		int i = vertices.size();
		double focus = camera.focus;
		double zoom = camera.zoom;
		Vertex3D vertex;
		Vertex2D screen;
		double persp;
		while (i > 0 && i <= vertices.size() && 
			(vertex = vertices.get(--i)) != null) {
			double vx = vertex.x;
			double vy = vertex.y;
			double vz = vertex.z;
			double s_x = vx * m11 + vy * m12 + vz * m13 + view.n14;
			double s_y = vx * m21 + vy * m22 + vz * m23 + view.n24;
			double s_z = vx * m31 + vy * m32 + vz * m33 + view.n34;
			if (projected.get(vertex) != null) {
				screen = projected.get(vertex);
			} else {
				screen = new Vertex2D();
				projected.put(vertex, screen);
			}
			if (screen.visible = (s_z > 0)) {
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
	public BBox boundingBox() {
		List<Vertex3D> vertices = this.geometry.vertices;
		BBox bBox = new BBox();
		bBox.min = new Number3D();
		bBox.max = new Number3D();
		bBox.size = new Number3D();
		for (int i = 0; i < vertices.size(); i++) {
			Vertex3D v = vertices.get(i);
			//FIXME: undefined
			bBox.min.x = (bBox.min.x == 0)? v.x : Math.min(v.x, bBox.min.x);
			bBox.max.x = (bBox.max.x == 0)? v.x : Math.max(v.x, bBox.max.x);
			bBox.min.y = (bBox.min.y == 0)? v.y : Math.min(v.y, bBox.min.y);
			bBox.max.y = (bBox.max.y == 0)? v.y : Math.max(v.y, bBox.max.y);
			bBox.min.z = (bBox.min.z == 0)? v.z : Math.min(v.z, bBox.min.z);
			bBox.max.z = (bBox.max.z == 0)? v.z : Math.max(v.z, bBox.max.z);
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
	public void transformVertices(Matrix3D transformation) {
		double m11 = transformation.n11;
		double m12 = transformation.n12;
		double m13 = transformation.n13;
		double m21 = transformation.n21;
		double m22 = transformation.n22;
		double m23 = transformation.n23;
		double m31 = transformation.n31;
		double m32 = transformation.n32;
		double m33 = transformation.n33;
		double m14 = transformation.n14;
		double m24 = transformation.n24;
		double m34 = transformation.n34;
		List<Vertex3D> vertices = this.geometry.vertices;
		int i = vertices.size();
		Vertex3D vertex;
		while (i > 0 && i <= vertices.size() &&
			(vertex = vertices.get(--i)) != null) {
			double vx = vertex.x;
			double vy = vertex.y;
			double vz = vertex.z;
			double tx = vx * m11 + vy * m12 + vz * m13 + m14;
			double ty = vx * m21 + vy * m22 + vz * m23 + m24;
			double tz = vx * m31 + vy * m32 + vz * m33 + m34;
			vertex.x = tx;
			vertex.y = ty;
			vertex.z = tz;
		}
	}
}
