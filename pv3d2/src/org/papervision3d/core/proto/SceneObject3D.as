package org.papervision3d.core.proto
{
	import flash.display.Sprite;
	import flash.utils.getTimer;
	import flash.utils.Dictionary;
	
	import org.papervision3d.Papervision3D;
	import org.papervision3d.objects.DisplayObject3D;
	import org.papervision3d.materials.MaterialsList;
	
	/**
	 * Scene3D的底层实现
	 * 实现材质动画化和镜头变换
	 */
	public class SceneObject3D extends DisplayObjectContainer3D
	{
		/**
		 * 渲染的目标
		 */
		public var container:Sprite;
		//未使用
		private var geometries:Dictionary;
		/**
		 * 统计数据
		 * performance:时间
		 */
		public var stats:Object;
		/**
		 * 渲染用（利用这个数组进行排序）
		 */
		public var objects:Array;
		/**
		 * 材质列表，用于使动画材质能及时更新内容
		 */
		public var materials:MaterialsList;
		
		/**
		 * 构造函数
		 * @param	container
		 */
		public function SceneObject3D(container:Sprite)
		{
			if (container)
			{
				this.container = container;
			}
			else
			{
				Papervision3D.log("Scene3D: container argument required.");
			}
			this.objects = new Array();
			this.materials = new MaterialsList();
			Papervision3D.log(Papervision3D.NAME + " " + Papervision3D.VERSION + " (" + Papervision3D.DATE + ")\n");
			this.stats = new Object();
			this.stats.points = 0;
			this.stats.polys = 0;
			this.stats.triangles = 0;
			this.stats.performance = 0;
			this.stats.rendered = 0;
		}
		
		/**
		 * 添加儿子
		 * @param	child
		 * @param	name
		 * @return
		 */
		public override function addChild(child:DisplayObject3D, name:String = null):DisplayObject3D
		{
			var newChild:DisplayObject3D = super.addChild(child, name);
			this.objects.push(newChild);
			return newChild;
		}
		
		/**
		 * 删除儿子
		 * @param	child
		 * @return
		 */
		public override function removeChild(child:DisplayObject3D):DisplayObject3D
		{
			super.removeChild(child);
			for (var i:int = 0; i < this.objects.length; i++ )
			{
				if (this.objects[i] === child )
				{
					this.objects.splice(i, 1);
					return child;
				}
			}
			return child;
		}
		
		/**
		 * 根据镜头进行渲染
		 * @param	camera
		 */
		public function renderCamera(camera:CameraObject3D):void
		{
			var stats:Object = this.stats;
			stats.performance = getTimer();
			for each (var m:MaterialObject3D in this.materials)
			{
				trace("SceneObject3D:materials " + m);
				if (m.animated)
				{
					m.updateBitmap();
				}
			}
			if (camera)
			{
				camera.transformView();
				var objects:Array = this.objects;
				var p:DisplayObject3D;
				var i:Number = objects.length;
				while (p = objects[--i])
				{
					if (p.visible)
					{
						p.project(camera, camera);
					}
				}
			}
			if (camera.sort)
			{
				this.objects.sortOn('screenZ', Array.NUMERIC);
			}
			stats.rendered = 0;
			renderObjects(camera.sort);
		}
		
		/**
		 * 渲染回调
		 * @param	sort
		 */
		protected function renderObjects(sort:Boolean):void 
		{
			
		}
	}
}
