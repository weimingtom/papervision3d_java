package org.papervision3d.core.proto;

import java.util.Map;

import org.papervision3d.core.Matrix3D;
import org.papervision3d.core.Number3D;
import org.papervision3d.objects.DisplayObject3D;

public class CameraObject3D extends DisplayObject3D {
	private static Matrix3D _flipY = Matrix3D.scaleMatrix(1, -1, 1);
	public static Number3D DEFAULT_POS = new Number3D(0, 0, -1000);
	
	public double zoom;
	public double focus;
	public boolean sort;
	
	public CameraObject3D(double zoom, double focus, Map<String, Object> initObject) {
		super();
		this.setX((initObject != null && initObject.get("x") != null) ? (Double)initObject.get("x") : DEFAULT_POS.x);
		this.setY((initObject != null && initObject.get("y") != null) ? (Double)initObject.get("y") : DEFAULT_POS.y);
		this.setZ((initObject != null && initObject.get("z") != null) ? (Double)initObject.get("z") : DEFAULT_POS.z);
		this.zoom = zoom;
		this.focus = focus;
		this.sort = (initObject != null && initObject.get("sorted") != null) ? (Boolean)initObject.get("sorted") : true;
	}
	
//	public void transformView() {
//		this.transformView(null);
//	}
	
	public void transformView(Matrix3D transform) {
		this.view = Matrix3D.inverse(Matrix3D.multiply(transform != null ? transform : this.transform, _flipY));
	}

	public void tilt(double angle) {
		super.pitch(angle);
	}

	public void pan(double angle) {
		super.yaw(angle);
	}
}
