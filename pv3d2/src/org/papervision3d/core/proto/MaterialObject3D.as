package org.papervision3d.core.proto
{
	import flash.display.BitmapData;
	
	/**
	 * 所有材质的基类
	 */
	public class MaterialObject3D
	{
		//全局，默认材质的颜色
		static public var DEFAULT_COLOR:Number = 0xFF00FF;
		//全局，材质总数，用于计算ID
		static private var _totalMaterialObjects:Number = 0;
		
		/**
		 * 位图，用于位图材质
		 */
		public var bitmap:BitmapData;
		
		//是否动画，用于SceneObject3D.renderCamera时是否执行updateBitmap
		public var animated:Boolean;
		//是否平滑，用于Face3D的beginBitmapFill（贴图用）
		public var smooth:Boolean;
		
		/**
		 * 线颜色和透明度，用于线框材质
		 */
		public var lineColor:Number;
		public var lineAlpha:Number;
		/**
		 * 填充颜色和透明度，用于颜色材质
		 */
		public var fillColor:Number;
		public var fillAlpha:Number;
		
		/**
		 * 是否单面，用于Face3D的渲染
		 */
		public var oneSide:Boolean;
		/**
		 * 是否跳过Face3D的渲染
		 */
		public var invisible:Boolean;
		/**
		 * 是否对面（当oneSide为true）
		 */
		public var opposite:Boolean;
		//内部使用（调试用）
		public var scene:SceneObject3D;
		//材质名称，用于添加材质列表时用（非必须）
		public var name:String;
		//全局材质序号(调试用)
		public var id:Number;
		
		/**
		 * 是否双面
		 */
		public function get doubleSided():Boolean
		{
			return !this.oneSide;
		}
		
		/**
		 * 设置双面
		 */
		public function set doubleSided( double:Boolean ):void
		{
			this.oneSide = !double;
		}
		
		/**
		 * 构造函数
		 * @param	initObject 可用于指定bitmap
		 */
		public function MaterialObject3D(initObject:Object = null)
		{
			if (initObject && initObject.bitmap) 
			{
				this.bitmap = initObject.bitmap;
			}
			this.lineColor = initObject ? initObject.lineColor || DEFAULT_COLOR : DEFAULT_COLOR;
			this.lineAlpha = initObject ? initObject.lineAlpha || 0 : 0;
			this.fillColor = initObject ? initObject.fillColor || DEFAULT_COLOR : DEFAULT_COLOR;
			this.fillAlpha = initObject ? initObject.fillAlpha || 0 : 0;
			this.animated = initObject ? initObject.animated || false : false;
			this.invisible = initObject ? initObject.invisible || false : false;
			this.smooth = initObject ? initObject.smooth || false : false;
			this.doubleSided = initObject ? initObject.doubleSided || false : false;
			this.opposite = initObject ? initObject.opposite || false : false;
			this.id = _totalMaterialObjects++;
		}
		
		/**
		 * 默认材质，用于网格
		 * 线框加纯色材质
		 */
		static public function get DEFAULT():MaterialObject3D
		{
			var defMaterial:MaterialObject3D = new MaterialObject3D();
			defMaterial.lineColor = DEFAULT_COLOR;
			defMaterial.lineAlpha = 100;
			defMaterial.fillColor = DEFAULT_COLOR;
			defMaterial.fillAlpha = 10;
			defMaterial.doubleSided = true;
			return defMaterial;
		}
		
		/**
		 * 如果开启animated，则在场景执行renderCamera时执行此方法
		 */
		public function updateBitmap():void 
		{
			
		}
		
		/**
		 * 复制
		 * @param	material
		 */
		public function copy(material:MaterialObject3D):void
		{
			this.bitmap = material.bitmap;
			this.animated = material.animated;
			this.smooth = material.smooth;
			this.lineColor = material.lineColor;
			this.lineAlpha = material.lineAlpha;
			this.fillColor = material.fillColor;
			this.fillAlpha = material.fillAlpha;
			this.oneSide = material.oneSide;
			this.opposite = material.opposite;
			this.invisible = material.invisible;
			this.scene = material.scene;
			this.name = material.name;
		}
		
		/**
		 * 复制
		 * @return
		 */
		public function clone():MaterialObject3D
		{
			var cloned:MaterialObject3D = new MaterialObject3D();
			cloned.bitmap = this.bitmap;
			cloned.animated = this.animated;
			cloned.smooth = this.smooth;
			cloned.lineColor = this.lineColor;
			cloned.lineAlpha = this.lineAlpha;
			cloned.fillColor = this.fillColor;
			cloned.fillAlpha = this.fillAlpha;
			cloned.oneSide = this.oneSide;
			cloned.opposite = this.opposite;
			cloned.invisible = this.invisible;
			cloned.scene = this.scene;
			cloned.name = this.name;
			return cloned;
		}
		
		/**
		 * 字符串形式
		 * @return
		 */
		public function toString():String
		{
			return '[MaterialObject3D] bitmap:' + this.bitmap + ' lineColor:' + this.lineColor + ' fillColor:' + fillColor;
		}
	}
}
