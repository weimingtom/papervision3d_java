package org.papervision3d.scenes
{
	import flash.utils.getTimer;
	import flash.display.Sprite;
	
	import org.papervision3d.objects.DisplayObject3D;
	import org.papervision3d.core.proto.SceneObject3D;
	
	/**
	 * 场景，保证所有objects在渲染句柄中渲染
	 * 更新performance
	 */
	public class Scene3D extends SceneObject3D
	{
		public function Scene3D(container:Sprite)
		{
			super(container);
		}
		
		/**
		 * 基于objects的渲染，不考虑排序
		 * @param	sort
		 */
		protected override function renderObjects(sort:Boolean):void
		{
			this.container.graphics.clear();
			var p:DisplayObject3D;
			var objects:Array = this.objects;
			var i:Number = objects.length;
			while (p = objects[--i])
			{
				if (p.visible)
				{
					p.render(this);
				}
			}
			var stats:Object = this.stats;
			stats.performance = getTimer() - stats.performance;
		}
	}
}
