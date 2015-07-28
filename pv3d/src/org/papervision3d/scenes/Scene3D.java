package org.papervision3d.scenes;

import java.util.List;

import flash.display.Sprite;

import org.papervision3d.objects.DisplayObject3D;
import org.papervision3d.core.proto.SceneObject3D;
import org.papervision3d.core.proto.Stats;

/**
 * 场景，保证所有objects在渲染句柄中渲染
 * 更新performance
 */
public class Scene3D extends SceneObject3D {
	public Scene3D(Sprite container) {
		super(container);
	}
	
	/**
	 * 基于objects的渲染，不考虑排序
	 * @param	sort
	 */
	protected void renderObjects(boolean sort) {
		this.container.getGraphics().clear();
		DisplayObject3D p;
		List<DisplayObject3D> objects = this.objects;
		int i = objects.size();
		while (i > 0 && i <= objects.size() && 
			(p = objects.get(--i)) != null) {
			if (p.visible) {
				p.render(this);
			}
		}
		Stats stats = this.stats;
		stats.performance = System.currentTimeMillis() - stats.performance;
	}
}
