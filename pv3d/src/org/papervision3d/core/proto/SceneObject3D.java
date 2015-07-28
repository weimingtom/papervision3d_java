package org.papervision3d.core.proto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import flash.display.Sprite;

import org.papervision3d.Papervision3D;
import org.papervision3d.objects.DisplayObject3D;
import org.papervision3d.materials.MaterialsList;

/**
 * Scene3D的底层实现
 * 实现材质动画化和镜头变换
 */
public class SceneObject3D extends DisplayObjectContainer3D {
	/**
	 * 渲染的目标
	 */
	public Sprite container;
	//未使用
//	private var geometries:Dictionary;
	/**
	 * 统计数据
	 * performance:时间
	 */
	public Stats stats;
	/**
	 * 渲染用（利用这个数组进行排序）
	 */
	public List<DisplayObject3D> objects;
	/**
	 * 材质列表，用于使动画材质能及时更新内容
	 */
	public MaterialsList materials;
	
	/**
	 * 构造函数
	 * @param	container
	 */
	public SceneObject3D(Sprite container) {
		if (container != null) {
			this.container = container;
		} else {
			Papervision3D.log("Scene3D: container argument required.");
		}
		this.objects = new ArrayList<DisplayObject3D>();
		this.materials = new MaterialsList();
		Papervision3D.log(Papervision3D.NAME + " " + Papervision3D.VERSION + " (" + Papervision3D.DATE + ")\n");
		this.stats = new Stats();
		this.stats.points = 0;
		this.stats.polys = 0;
		this.stats.triangles = 0;
		this.stats.performance = 0;
		this.stats.rendered = 0;
	}
	
	public DisplayObject3D addChild(DisplayObject3D child) {
		return this.addChild(child, null);
	}
	
	/**
	 * 添加儿子
	 * @param	child
	 * @param	name
	 * @return
	 */
	public DisplayObject3D addChild(DisplayObject3D child, String name) {
		DisplayObject3D newChild = super.addChild(child, name);
		this.objects.add(newChild);
		return newChild;
	}
	
	/**
	 * 删除儿子
	 * @param	child
	 * @return
	 */
	public DisplayObject3D removeChild(DisplayObject3D child) {
		super.removeChild(child);
		for (int i = 0; i < this.objects.size(); i++){
			if (this.objects.get(i) == child) {
				this.objects.remove(i); //FIXME:
				return child;
			}
		}
		return child;
	}
	
	/**
	 * 根据镜头进行渲染
	 * @param	camera
	 */
	public void renderCamera(CameraObject3D camera) {
		Stats stats = this.stats;
		stats.performance = System.currentTimeMillis();
		for (MaterialObject3D m : this.materials.materialsByName.values()) {
			System.out.println("SceneObject3D:materials " + m);
			if (m.animated) {
				m.updateBitmap();
			}
		}
		if (camera != null) {
			camera.transformView(null);
			List<DisplayObject3D> objects = this.objects;
			DisplayObject3D p;
			int i = objects.size();
			while (i > 0 && i <= objects.size() && 
				(p = objects.get(--i)) != null) {
				if (p.visible) {
					p.project(camera, camera);
				}
			}
		}
		if (camera.sort) {
			Collections.sort(this.objects, new Comparator<DisplayObject3D>() {
				@Override
				public int compare(DisplayObject3D arg0, DisplayObject3D arg1) {
					if (arg0.screenZ == arg1.screenZ) {
						return 0;
					} else if (arg0.screenZ < arg1.screenZ) {
						return -1;
					} else {
						return 1;
					}
				}
			});
		}
		stats.rendered = 0;
		renderObjects(camera.sort);
	}
	
	/**
	 * 渲染回调
	 * @param	sort
	 */
	protected void renderObjects(boolean sort) {
		
	}
}