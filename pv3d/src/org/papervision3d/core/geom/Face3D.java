package org.papervision3d.core.geom;

import java.util.List;
import java.util.Map;

import org.papervision3d.Papervision3D;
import org.papervision3d.core.NumberUV;
import org.papervision3d.core.proto.MaterialObject3D;
import org.papervision3d.objects.DisplayObject3D;

import flash.display.BitmapData;
import flash.display.Graphics;
import flash.display.Sprite;
import flash.geom.Matrix;

public class Face3D {
	//全局，总面数
	private static double _totalFaces = 0;
	//全局，位图矩阵，用于beginBitmapFill
	private static Matrix _bitmapMatrix;
	
	//取出顶点数组, Vertex3D[3]
	public List<Vertex3D> vertices;
	//材质名称, String
	public String materialName;
	//取出UV数组，NumberUV[3], (u,v)...
	public List<NumberUV> uv;
	
	//未使用，应该是用来排序的
	public double screenZ;
	//未使用，可见性，
	public boolean visible;
	
	//面的全局序号（用于调试）
	public double id;
	
	public Face3D face; //实际指向自己
	public Mesh3D instance; //
	
	public Face3D() {
		this(null, null, null);
	}
	
	/**
	 * 一个面的数据
	 * @param	vertices 顶点数组, Vertex3D[3]
	 * @param	materialName 材质名称, String
	 * @param	uv UV数组, NumberUV[3]
	 */
	public Face3D(List<Vertex3D> vertices, String materialName, List<NumberUV> uv) {
		this.vertices = vertices;
		this.materialName = materialName;
		this.uv = uv;
		this.id = _totalFaces++;
		if (_bitmapMatrix == null) {
			_bitmapMatrix = new Matrix();
		}
	}
	
	/**
	 * 转换（贴图用?）
	 * @param	instance 贴图用的三维对象
	 * @return 转换矩阵？
	 */
	public MatrixMap transformUV(DisplayObject3D instance) {
		MatrixMap mapping = null;
		MaterialObject3D material = 
			(this.materialName != null && instance.materials != null) ? 
			instance.materials.materialsByName.get(this.materialName) : 
			instance.material;
		if (this.uv == null) {
			Papervision3D.log("Face3D: transformUV() uv not found!");
		} else if (material != null && material.bitmap != null) {
			List<NumberUV> uv = this.uv;
			double w = material.bitmap.getWidth();
			double h = material.bitmap.getHeight();
			double u0 = w * uv.get(0).u;
			double v0 = h * (1 - uv.get(0).v);
			double u1 = w * uv.get(1).u;
			double v1 = h * (1 - uv.get(1).v);
			double u2 = w * uv.get(2).u;
			double v2 = h * (1 - uv.get(2).v);
			if ((u0 == u1 && v0 == v1) || (u0 == u2 && v0 == v2))
			{
				u0 -= (u0 > 0.05) ? 0.05 : -0.05;
				v0 -= (v0 > 0.07) ? 0.07 : -0.07;
			}
			if (u2 == u1 && v2 == v1)
			{
				u2 -= (u2 > 0.05) ? 0.04 : -0.04;
				v2 -= (v2 > 0.06) ? 0.06 : -0.06;
			}
			double at = (u1 - u0);
			double bt = (v1 - v0);
			double ct = (u2 - u0);
			double dt = (v2 - v0);
			Matrix m = new Matrix(at, bt, ct, dt, u0, v0);
			m.invert();
			if (instance.projected_.get(this) != null) {
				mapping = instance.projected_.get(this); 
			} else {
				instance.projected_.put(this, new MatrixMap());
				mapping = instance.projected_.get(this);
			}
			mapping._a = m.a;
			mapping._b = m.b;
			mapping._c = m.c;
			mapping._d = m.d;
			mapping._tx = m.tx;
			mapping._ty = m.ty;
		} else {
			Papervision3D.log("Face3D: transformUV() material.bitmap not found!");
		}
		return mapping;
	}
	
	/**
	 * ！！！关键代码！！！
	 * 
	 * 渲染，根据面和显示对象的信息，把面画在容器上
	 * @param	instance 显示对象实体
	 * @param	container 显示容器
	 * @return 是否成功
	 */
	public double render(DisplayObject3D instance, Sprite container) {
		List<Vertex3D> vertices = this.vertices;
		Map<Vertex3D, Vertex2D> projected = instance.projected;
		Vertex2D s0 = projected.get(vertices.get(0));
		Vertex2D s1 = projected.get(vertices.get(1));
		Vertex2D s2 = projected.get(vertices.get(2));
		double x0 = s0.x;
		double y0 = s0.y;
		double x1 = s1.x;
		double y1 = s1.y;
		double x2 = s2.x;
		double y2 = s2.y;
		MaterialObject3D material = null;
		if (this.materialName != null && instance.materials != null) {
			material = instance.materials.materialsByName.get(this.materialName);
		} else {
			material = instance.material;
		}
		if (material.invisible) {
			return 0;
		}
		if (material.oneSide) {
			if (material.opposite) {
				if ((x2 - x0) * (y1 - y0) - (y2 - y0) * (x1 - x0) > 0) {
					return 0;
				}
			} else {
				if ((x2 - x0) * (y1 - y0) - (y2 - y0) * (x1 - x0) < 0) {
					return 0;
				}
			}
		}
		BitmapData texture = material.bitmap;
		double fillAlpha = material.fillAlpha;
		double lineAlpha = material.lineAlpha;
		Graphics graphics = container.getGraphics();
		if (texture != null) {
			MatrixMap map = null;
			if (instance.projected_.get(this) != null) { 
				map = instance.projected_.get(this);
			} else {
				map = transformUV(instance);
			}
			double a1 = map._a;
			double b1 = map._b;
			double c1 = map._c;
			double d1 = map._d;
			double tx1 = map._tx;
			double ty1 = map._ty;
			double a2 = x1 - x0;
			double b2 = y1 - y0;
			double c2 = x2 - x0;
			double d2 = y2 - y0;
			Matrix matrix = _bitmapMatrix;
			matrix.a = a1 * a2 + b1 * c2;
			matrix.b = a1 * b2 + b1 * d2;
			matrix.c = c1 * a2 + d1 * c2;
			matrix.d = c1 * b2 + d1 * d2;
			matrix.tx = tx1 * a2 + ty1 * c2 + x0;
			matrix.ty = tx1 * b2 + ty1 * d2 + y0;
			graphics.beginBitmapFill(texture, matrix, false, material.smooth);
		} else if (fillAlpha != 0) {
			graphics.beginFill(material.fillColor, fillAlpha);
		}
		if (lineAlpha != 0) {
			graphics.lineStyle(0, material.lineColor, lineAlpha);
		} else {
			graphics.lineStyle();
		}
		graphics.moveTo(x0, y0);
		graphics.lineTo(x1, y1);
		graphics.lineTo(x2, y2);
		Papervision3D.log("Face3D:" +
			"(" + x0 + "," + y0 + ")" + 
			"(" + x1 + "," + y1 + ")" + 
			"(" + x2 + "," + y2 + ")");
		if (lineAlpha != 0) {
			graphics.lineTo(x0, y0);
		}
		if (texture != null || fillAlpha != 0) {
			graphics.endFill();
		}
		return 1;
	}
}
