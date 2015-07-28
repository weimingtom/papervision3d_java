package org.papervision3d.cameras;

import java.util.Map;

import org.papervision3d.core.Number3D;
import org.papervision3d.core.Matrix3D;
import org.papervision3d.core.proto.CameraObject3D;
import org.papervision3d.objects.DisplayObject3D;

/**
 * 由于摄像机的特殊性，所以它没有添加到场景中。
 * 
 * 通过x,y调整镜头位置
 * 另见SceneObject3D.renderCamera
 */
public class Camera3D extends CameraObject3D {
	/**
	 * 面向的对象
	 */
	public DisplayObject3D target;
	/**
	 * 位置的3D坐标,用于hover，可选
	 */
	public Number3D _goto;
	
	public Camera3D() {
		this(null, 2, 100, null);
	}
	
	public Camera3D(DisplayObject3D target, double zoom, 
		double focus, Map<String, Object> initObject) {
		super(zoom, focus, initObject);
		this.target = target != null ? target : DisplayObject3D.getZERO();
		this._goto = new Number3D(this.getX(), this.getY(), this.getZ());
	}
	
//	public void transformView() {
//		this.transformView(null);
//	}
	
	/**
	 * 用于SceneObject3D.renderCamera
	 * @param	transform
	 */
	public void transformView(Matrix3D transform) {
		this.lookAt(this.target);
		super.transformView(null);
	}
	
	//TODO: 这个方法可有可无
	public void hover(int type, double mouseX, double mouseY) {
		DisplayObject3D target = this.target;
		Number3D goto_ = this._goto;
		double camSpeed = 8;
		switch(type) {
		case 0:
			double dX = goto_.x - target.getX();
			double dZ = goto_.z - target.getZ();
			double ang = Math.atan2(dZ, dX);
			double dist = Math.sqrt(dX * dX + dZ * dZ);
			double xMouse = 0.5 * mouseX;
			double camX = dist * Math.cos(ang - xMouse);
			double camZ = dist * Math.sin(ang - xMouse);
			double camY = goto_.y - 300 * mouseY;
			this.setX(this.getX() - ((this.getX() - camX) / camSpeed));
			this.setY(this.getY() - ((this.getY() - camY) / camSpeed));
			this.setZ(this.getZ() - ((this.getZ() - camZ) / camSpeed));
			break;
		
		case 1:
			this.setX(this.getX() - ((this.getX() - 1000 * mouseX) / camSpeed));
			this.setY(this.getY() - ((this.getY() - 1000 * mouseY) / camSpeed));
			break;
		}
	}
}
