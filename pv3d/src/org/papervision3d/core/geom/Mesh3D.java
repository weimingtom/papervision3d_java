package org.papervision3d.core.geom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.papervision3d.core.Matrix3D;
import org.papervision3d.core.NumberUV;
import org.papervision3d.core.proto.CameraObject3D;
import org.papervision3d.core.proto.MaterialObject3D;
import org.papervision3d.objects.DisplayObject3D;

/**
 * 网格
 */
public class Mesh3D extends Vertices3D {
	/**
	 * 
	 * @param	material 材质
	 * @param	vertices 顶点数组
	 * @param	faces 面数组
	 * @param	name 名称
	 * @param	initObject 附加数据
	 */
	public Mesh3D(MaterialObject3D material, List<Vertex3D> vertices, 
		List<Face3D> faces, String name, 
		Map<String, Object> initObject) {
		super(vertices, name, initObject);
		this.geometry.faces = (faces != null ? faces : new ArrayList<Face3D>());
		this.material = material != null ? material : MaterialObject3D.getDEFAULT();
	}
	public Mesh3D(MaterialObject3D material, List<Vertex3D> vertices, 
			List<Face3D> faces, String name) {
		this(material, vertices, faces, name, null);
	}
	
	/**
	 * 投影
	 * @param	parent
	 * @param	camera
	 * @param	sorted
	 * @return
	 */
	public double project(DisplayObject3D parent, CameraObject3D camera, List<Face3D> sorted) {
		super.project(parent, camera, sorted);
		if (sorted == null) {
			sorted = this._sorted;
		}
		Map<Vertex3D, Vertex2D> projected = this.projected;
		Matrix3D view = this.view;
		List<Face3D> faces = this.geometry.faces;
		List<Face3D> iFaces = this.faces;
		double screenZs = 0;
		double visibleFaces = 0;
		Vertex2D vertex0;
		Vertex2D vertex1;
		Vertex2D vertex2;
		int visibles;
		Face3D face;
		Face3D iFace;
		for (int i = 0; i < faces.size(); i++) {
			face = faces.get(i);
			if (i >= 0 && i < this.faces.size() && 
				this.faces.get(i) != null) {
				iFace = this.faces.get(i);
			} else {
				//FIXME:
				if (i >= 0 && i >= iFaces.size()) {
					for (int kkk = iFaces.size(); kkk < i + 1; kkk++) {
						iFaces.add(null);
					}
				}
				iFaces.set(i, new Face3D());
				iFace = iFaces.get(i);
			}
			iFace.face = face;
			iFace.instance = this;
			vertex0 = projected.get(face.vertices.get(0));
			vertex1 = projected.get(face.vertices.get(1));
			vertex2 = projected.get(face.vertices.get(2));
			visibles = (vertex0.visible ? 1 : 0) + 
				(vertex1.visible ? 1 : 0) + 
				(vertex2.visible ? 1 : 0);
			iFace.visible = (visibles == 3);
			if (iFace.visible) {
				screenZs += iFace.screenZ = (vertex0.z + vertex1.z + vertex2.z ) / 3;
				visibleFaces++;
				if (sorted != null) {
					sorted.add(iFace);
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
	public void projectTexture(String u, String v) {
		List<Face3D> faces = this.geometry.faces;
		BBox bBox = this.boundingBox();
		double minX = bBox.min.get(u);
		double sizeX = bBox.size.get(u);
		double minY = bBox.min.get(v);
		double sizeY = bBox.size.get(v);
		MaterialObject3D objectMaterial = this.material;
		for (int i = 0; i < faces.size(); i++) {
			Face3D myFace = faces.get(i);
			List<Vertex3D> myVertices = myFace.vertices;
			Vertex3D a = myVertices.get(0);
			Vertex3D b = myVertices.get(1);
			Vertex3D c = myVertices.get(2);
			NumberUV uvA = new NumberUV((a.get(u) - minX) / sizeX, (a.get(v) - minY) / sizeY);
			NumberUV uvB = new NumberUV((b.get(u) - minX) / sizeX, (b.get(v) - minY) / sizeY);
			NumberUV uvC = new NumberUV((c.get(u) - minX) / sizeX, (c.get(v) - minY) / sizeY);
			myFace.uv = new ArrayList<NumberUV>();
			myFace.uv.add(uvA);
			myFace.uv.add(uvB);
			myFace.uv.add(uvC);
		}
	}
}
