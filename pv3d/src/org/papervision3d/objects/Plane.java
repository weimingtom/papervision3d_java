package org.papervision3d.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.papervision3d.core.NumberUV;
import org.papervision3d.core.geom.Face3D;
import org.papervision3d.core.geom.Mesh3D;
import org.papervision3d.core.geom.Vertex3D;
import org.papervision3d.core.proto.MaterialObject3D;

/**
 * 3D平面
 */
public class Plane extends Mesh3D {
	static public int DEFAULT_SIZE = 500;
	static public double DEFAULT_SCALE = 1;
	static public int DEFAULT_SEGMENTS = 1;
	
	//横向和纵向的格数
	public int segmentsW;
	public int segmentsH;
	
	public Plane(MaterialObject3D material, double width, double height, 
			int segmentsW, int segmentsH) {
		this(material, width, height, segmentsW, segmentsH, null);
	}
	
	/**
	 * 创建新的平面
	 * @param	material 材质
	 * @param	width 宽
	 * @param	height 高，可以为0，用于匹配材质
	 * @param	segmentsW 横向线段数
	 * @param	segmentsH 纵向线段数
	 * @param	initObject 附加数据
	 */
	public Plane(MaterialObject3D material, double width, double height, 
			int segmentsW, int segmentsH, 
			Map<String, Object> initObject) {
		super(material, new ArrayList<Vertex3D>(), new ArrayList<Face3D>(), 
			null, initObject);
		this.segmentsW = segmentsW != 0 ? segmentsW : DEFAULT_SEGMENTS;
		this.segmentsH = segmentsH != 0 ? segmentsH : this.segmentsW;
		double scale = DEFAULT_SCALE;
		if (height == 0) {
			if (width != 0) {
				scale = width;
			}
			if (material != null && material.bitmap != null) {
				width = material.bitmap.getWidth() * scale;
				height = material.bitmap.getHeight() * scale;
			} else {
				width = DEFAULT_SIZE * scale;
				height = DEFAULT_SIZE * scale;
			}
		}
		buildPlane(width, height);
	}
	
	/**
	 * 手工写数据
	 * @param	width
	 * @param	height
	 */
	private void buildPlane(double width, double height) {
		//线段数
		int gridX = this.segmentsW;
		int gridY = this.segmentsH;
		//点数
		int gridX1 = gridX + 1;
		int gridY1 = gridY + 1;
		//顶点和面数组
		List<Vertex3D> vertices = this.geometry.vertices;
		List<Face3D> faces = this.geometry.faces;
		//纹理
		double textureX = width / 2;
		double textureY = height / 2;
		//每个线段的长度
		double iW = width / gridX;
		double iH = height / gridY;
		//遍历每个坐标点编号(ix, iy)，计算对应的3D坐标
		for (int ix = 0; ix < gridX + 1; ix++) {
			for (int iy = 0; iy < gridY1; iy++) {
				double x = ix * iW - textureX;
				double y = iy * iH - textureY;
				vertices.add(new Vertex3D(x, y, 0));
			}
		}
		NumberUV uvA;
		NumberUV uvC;
		NumberUV uvB;
		//遍历每个坐标点编号(ix, iy)，计算对应的UV值
		for (int ix = 0; ix < gridX; ix++) {
			for (int iy = 0; iy < gridY; iy++) {
				//注意由于vertices是一维的，需要转换
				Vertex3D a = vertices.get(ix * gridY1 + iy);
				Vertex3D c = vertices.get(ix * gridY1 + (iy + 1));
				Vertex3D b = vertices.get((ix + 1) * gridY1 + iy);
				uvA = new NumberUV(ix / gridX, iy / gridY );
				uvC = new NumberUV(ix / gridX, (iy+1) / gridY);
				uvB = new NumberUV((ix + 1) / gridX, iy / gridY );
				//每个面包含顶点数组，材质名称，UV数组
				List<Vertex3D> argVertices = new ArrayList<Vertex3D>();
				List<NumberUV> argUV = new ArrayList<NumberUV>();
				argVertices.add(a);
				argVertices.add(b);
				argVertices.add(c);
				argUV.add(uvA);
				argUV.add(uvB);
				argUV.add(uvC);
				faces.add(new Face3D(argVertices, null, argUV));
				a = vertices.get((ix + 1) * gridY1 + (iy + 1));
				c = vertices.get((ix + 1) * gridY1 + iy);
				b = vertices.get(ix * gridY1 + (iy + 1));
				//另一个半格
				uvA = new NumberUV((ix + 1) / gridX, (iy + 1) / gridY );
				uvC = new NumberUV((ix + 1) / gridX, iy / gridY);
				uvB = new NumberUV(ix / gridX, (iy + 1) / gridY);
				argVertices = new ArrayList<Vertex3D>();
				argUV = new ArrayList<NumberUV>();
				argVertices.add(a);
				argVertices.add(b);
				argVertices.add(c);
				argUV.add(uvA);
				argUV.add(uvB);
				argUV.add(uvC);
				faces.add(new Face3D(argVertices, null, argUV));
			}
		}
		this.geometry.ready = true;
	}
}
