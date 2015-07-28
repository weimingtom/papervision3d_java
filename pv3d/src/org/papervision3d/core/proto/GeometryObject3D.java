package org.papervision3d.core.proto;

import java.util.List;

import org.papervision3d.core.Matrix3D;
import org.papervision3d.core.geom.Face3D;
import org.papervision3d.core.geom.Vertex3D;

import flash.events.EventDispatcher;

public class GeometryObject3D extends EventDispatcher {
	//面数组, Face3D[], ((x,y,z), materialName, (u,v))...
	public List<Face3D> faces;
	//顶点数组, Vertex3D[], (x, y, z),...
	public List<Vertex3D> vertices;
	//表示数据是否加载或解析完成
	public boolean ready = false;
	
	//未使用
	protected MaterialObject3D _material;
	
	/**
	 * 保存全部顶点离原点距离平方最大的值
	 * 用于碰撞测试
	 */
	protected double _boundingSphere2;
	//记录_boundingSphere2是否已经被计算
	protected boolean _boundingSphereDirty = true;
	
	/**
	 * 计算全部顶点离原点距离平方最大的值
	 * @return
	 */
	public double getBoundingSphere2_() {
		double max = 0;
		double d;
		for (Vertex3D v : this.vertices) {
			d = v.x * v.x + v.y * v.y + v.z * v.z;
			max = (d > max) ? d : max;
		}
		this._boundingSphereDirty = false;
		return _boundingSphere2 = max;
	}
	
	/**
	 * 用于显示对象的碰撞测试
	 * 返回最大范围的平方
	 */
	public double getBoundingSphere2() {
		if (_boundingSphereDirty) {
			return getBoundingSphere2_();
		} else {
			return _boundingSphere2;
		}
	}
	
	/**
	 * 私有，没有使用
	 * @param	transformation
	 */
	public void transformVertices(Matrix3D transformation) {
		
	}
	
	/**
	 * 私有，没有使用
	 * @param	material
	 */
	public void transformUV(MaterialObject3D material) {
		//FIXME:
//		if (material.bitmap != null) {
//			for (Face3D f : this.faces) {
//				f.transformUV(material);
//			}
//		}
	}
	
	public GeometryObject3D() {
		this(null);
	}	
	
	/**
	 * 构造函数
	 * @param	initObject
	 */
	public GeometryObject3D(Object initObject) {
		
	}
}
