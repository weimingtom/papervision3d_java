package org.papervision3d.objects
{
	import flash.display.Sprite;
	import flash.utils.Dictionary;
	
	import org.papervision3d.Papervision3D;
	import org.papervision3d.core.Number3D;
	import org.papervision3d.core.Matrix3D;
	import org.papervision3d.core.proto.CameraObject3D;
	import org.papervision3d.core.proto.DisplayObjectContainer3D;
	import org.papervision3d.core.proto.GeometryObject3D;
	import org.papervision3d.core.proto.MaterialObject3D;
	import org.papervision3d.core.proto.SceneObject3D;
	import org.papervision3d.materials.MaterialsList;
	
	/**
	 * 3D显示对象
	 */
	public class DisplayObject3D extends DisplayObjectContainer3D
	{
		private static var _totalDisplayObjects:int = 0;
		private static var toDEGREES:Number = 180 / Math.PI;
		private static var toRADIANS:Number = Math.PI / 180;
		private static var FORWARD:Number3D = new Number3D(0, 0, 1);
		private static var BACKWARD:Number3D = new Number3D(0, 0, -1);
		private static var LEFT:Number3D = new Number3D(-1, 0, 0);
		private static var RIGHT:Number3D = new Number3D(1, 0, 0);
		private static var UP:Number3D = new Number3D(0, 1, 0);
		private static var DOWN:Number3D = new Number3D(0, -1, 0);
		
		/**
		 * 平移矩阵是否需要重新计算
		 */
		protected var _transformDirty:Boolean = false;
		//缓存的旋转角度
		private var _rotationX:Number;
		private var _rotationY:Number;
		private var _rotationZ:Number;
		/**
		 * 旋转矩阵是否需要重新计算
		 */
		private var _rotationDirty:Boolean = false;
		private var _scaleX:Number;
		private var _scaleY:Number;
		private var _scaleZ:Number;
		private var _scaleDirty:Boolean = false;
		protected var _sorted:Array;
		public var visible:Boolean;
		public var name:String;
		public var id:int;
		public var extra:Object
		public var container:Sprite;
		public var material:MaterialObject3D;
		public var materials:MaterialsList;
		public var scene:SceneObject3D;
		public var parent:DisplayObjectContainer3D;
		//自身的转换矩阵（包含其位置信息）
		public var transform:Matrix3D;
		public var view:Matrix3D;
		//投影? Vertex2D[]
		public var projected:Dictionary;
		public var faces:Array = new Array();
		/**
		 * 几何数据（面点，UV数据）
		 */
		public var geometry:GeometryObject3D;
		//排序用，Z排序
		public var screenZ:Number;
		
		/**
		 * 横
		 */
		public function get x():Number
		{
			return this.transform.n14;
		}
		
		/**
		 * 横
		 */
		public function set x(value:Number):void
		{
			this.transform.n14 = value;
		}
		
		/**
		 * 纵
		 */
		public function get y():Number
		{
			return this.transform.n24;
		}
		
		/**
		 * 纵
		 */
		public function set y(value:Number):void
		{
			this.transform.n24 = value;
		}
		
		/**
		 * 深
		 */
		public function get z():Number
		{
			return this.transform.n34;
		}
		
		/**
		 * 深
		 */
		public function set z(value:Number):void
		{
			this.transform.n34 = value;
		}
		
		/**
		 * 绕X轴旋转度数
		 */
		public function get rotationX():Number
		{
			if (this._rotationDirty) 
			{
				updateRotation();
			}
			return Papervision3D.useDEGREES ? -this._rotationX * toDEGREES : -this._rotationX;
		}
		
		/**
		 * 设置绕X轴旋转度数
		 */
		public function set rotationX(rot:Number):void
		{
			this._rotationX = Papervision3D.useDEGREES ? -rot * toRADIANS : -rot;
			this._transformDirty = true;
		}
		
		/**
		 * 绕Y轴旋转度数
		 */
		public function get rotationY():Number
		{
			if (this._rotationDirty) 
			{
				updateRotation();
			}
			return Papervision3D.useDEGREES ? -this._rotationY * toDEGREES : -this._rotationY;
		}
		
		/**
		 * 设置绕Y轴旋转度数
		 */
		public function set rotationY(rot:Number):void
		{
			this._rotationY = Papervision3D.useDEGREES ? -rot * toRADIANS : -rot;
			this._transformDirty = true;
		}
		
		/**
		 * 设置绕Z轴旋转度数
		 */
		public function get rotationZ():Number
		{
			if (this._rotationDirty) 
			{
				updateRotation();
			}
			return Papervision3D.useDEGREES ? -this._rotationZ * toDEGREES : -this._rotationZ;
		}

		/**
		 * 绕Z轴旋转度数
		 */
		public function set rotationZ(rot:Number):void
		{
			this._rotationZ = Papervision3D.useDEGREES? -rot * toRADIANS : -rot;
			this._transformDirty = true;
		}
		
		/**
		 * 重新计算旋转矩阵
		 */
		private function updateRotation():void
		{
			var rot:Number3D = Matrix3D.matrix2euler(this.transform);
			this._rotationX = rot.x * toRADIANS;
			this._rotationY = rot.y * toRADIANS;
			this._rotationZ = rot.z * toRADIANS;
			this._rotationDirty = false;
		}
		
		/**
		 * 整体缩放因子
		 */
		public function set scale(scale:Number):void
		{
			if (Papervision3D.usePERCENT) 
			{
				scale /= 100;
			}
			this._scaleX = this._scaleY = this._scaleZ = scale;
			this._transformDirty = true;
		}
		
		/**
		 * 整体缩放
		 */
		public function set scaleX(scale:Number):void
		{
			if (Papervision3D.usePERCENT) 
			{
				this._scaleX = scale / 100;
			}
			else 
			{
				this._scaleX = scale;
			}
			this._transformDirty = true;
		}
		
		/**
		 * Y轴缩放
		 */
		public function set scaleY(scale:Number):void
		{
			if (Papervision3D.usePERCENT) 
			{
				this._scaleY = scale / 100;
			}
			else 
			{
				this._scaleY = scale;
			}
			this._transformDirty = true;
		}
		
		/**
		 * Z轴缩放
		 */
		public function set scaleZ(scale:Number):void
		{
			if (Papervision3D.usePERCENT) 
			{
				this._scaleZ = scale / 100;
			}
			else 
			{
				this._scaleZ = scale;
			}
			this._transformDirty = true;
		}
		
		/**
		 * 初始显示对象，用于初始化镜头
		 */
		static public function get ZERO():DisplayObject3D
		{
			return new DisplayObject3D();
		}
		
		/**
		 * 构造函数
		 * @param	name 名称，用于三维显示容器的列表
		 * @param	geometry 三维几何数据
		 * @param	initObject 附加数据
		 */
		public function DisplayObject3D(name:String = null, geometry:GeometryObject3D = null, initObject:Object = null):void
		{
			super();
			Papervision3D.log("DisplayObject3D: " + name);
			this.transform = Matrix3D.IDENTITY;
			this.view = Matrix3D.IDENTITY;
			this.x = initObject? initObject.x || 0 : 0;
			this.y = initObject? initObject.y || 0 : 0;
			this.z = initObject? initObject.z || 0 : 0;
			rotationX = initObject? initObject.rotationX || 0 : 0;
			rotationY = initObject? initObject.rotationY || 0 : 0;
			rotationZ = initObject? initObject.rotationZ || 0 : 0;
			var scaleDefault:Number = Papervision3D.usePERCENT ? 100 : 1;
			scaleX = initObject? initObject.scaleX || scaleDefault : scaleDefault;
			scaleY = initObject? initObject.scaleY || scaleDefault : scaleDefault;
			scaleZ = initObject? initObject.scaleZ || scaleDefault : scaleDefault;
			if (initObject && initObject.extra) 
			{
				this.extra = initObject.extra;
			}
			if (initObject && initObject.container) 
			{
				this.container = initObject.container;
			}
			this.visible = true;
			this.id = _totalDisplayObjects++;
			this.name = name || String( this.id );
			if (geometry) 
			{
				addGeometry(geometry);
			}
		}
		
		/**
		 * 设置几何数据
		 * @param	geometry
		 */
		public function addGeometry(geometry:GeometryObject3D = null):void
		{
			if (geometry)
			{
				this.geometry = geometry;
			}
			this.projected = new Dictionary();
		}
		
		/**
		 * 质心距离
		 * @param	obj
		 * @return
		 */
		public function distanceTo(obj:DisplayObject3D):Number
		{
			var x:Number = this.x - obj.x;
			var y:Number = this.y - obj.y;
			var z:Number = this.z - obj.z;
			return Math.sqrt(x * x + y * y + z * z);
		}
		
		public function hitTestPoint(x:Number, y:Number, z:Number):Boolean
		{
			var dx:Number = this.x - x;
			var dy:Number = this.y - y;
			var dz:Number = this.z - z;
			var d2:Number = x * x + y * y + z * z;
			var sA:Number = this.geometry ? this.geometry.boundingSphere2 : 0;
			return sA > d2;
		}

		public function hitTestObject(obj:DisplayObject3D):Boolean
		{
			var dx:Number = this.x - obj.x;
			var dy:Number = this.y - obj.y;
			var dz:Number = this.z - obj.z;
			var d2:Number = dx * dx + dy * dy + dz * dz;
			var sA:Number = this.geometry? this.geometry.boundingSphere2 : 0;
			var sB:Number = obj.geometry? obj.geometry.boundingSphere2 : 0;
			return sA + sB > d2;
		}
		
		public function getMaterialByName(name:String):MaterialObject3D
		{
			var material:MaterialObject3D = this.materials.getMaterialByName(name);
			if (material)
			{
				return material;
			}
			else
			{	
				for each (var child :DisplayObject3D in this._childrenByName)
				{
					material = child.getMaterialByName(name);
					if (material)
					{
						return material;
					}
				}
			}
			return null;
		}
		
		public function materialsList():String
		{
			var list:String = "";
			for (var name:String in this.materials)
			{
				list += name + "\n";
			}
			for each (var child:DisplayObject3D in this._childrenByName)
			{
				for (name in child.materials.materialsByName)
				{
					list += "+ " + name + "\n";
				}
			}
			return list;
		}
		
		/**
		 * ！！！关键代码！！！
		 * 
		 * 投影
		 * 计算深度
		 * @param	parent
		 * @param	camera
		 * @param	sorted
		 * @return
		 */
		public function project(parent:DisplayObject3D, camera:CameraObject3D, sorted:Array = null):Number
		{
			if (!sorted) 
			{
				this._sorted = sorted = new Array();
			}
			if (this._transformDirty) 
			{
				updateTransform();
			}
			this.view = Matrix3D.multiply(parent.view, this.transform);
			var screenZs:Number = 0;
			var children:Number = 0;
			for each (var child:DisplayObject3D in this._childrenByName)
			{
				screenZs += child.project(this, camera, sorted);
				children++;
			}
			return this.screenZ = screenZs / children;
		}
		
		/**
		 * 旋转
		 * @param	scene
		 */
		public function render(scene:SceneObject3D):void
		{
			var iFaces :Array = this._sorted;
			iFaces.sortOn('screenZ', Array.DESCENDING | Array.NUMERIC);
			var container:Sprite = this.container || scene.container;
			var rendered:Number = 0;
			var iFace:Object;
			for (var i:int = 0; iFace = iFaces[i]; i++)
			{
				if (iFace.visible)
				{
					rendered += iFace.face.render(iFace.instance, container);
				}
			}
			scene.stats.rendered += rendered;
		}
		
		public function moveForward(distance:Number):void 
		{ 
			translate(distance, FORWARD); 
		}
		
		public function moveBackward(distance:Number):void 
		{ 
			translate(distance, BACKWARD); 
		}
		
		public function moveLeft(distance:Number):void 
		{ 
			translate(distance, LEFT); 
		}
		
		public function moveRight(distance:Number):void 
		{ 
			translate(distance, RIGHT); 
		}
		
		public function moveUp(distance:Number):void 
		{ 
			translate(distance, UP); 
		}
		
		public function moveDown(distance:Number):void 
		{ 
			translate(distance, DOWN); 
		}
		
		public function translate(distance:Number, axis:Number3D):void
		{
			var vector:Number3D = axis.clone();
			if (this._transformDirty) 
			{
				updateTransform();
			}
			Matrix3D.rotateAxis(transform, vector)
			this.x += distance * vector.x;
			this.y += distance * vector.y;
			this.z += distance * vector.z;
		}

		public function pitch(angle:Number):void
		{
			angle = Papervision3D.useDEGREES ? angle * toRADIANS : angle;
			var vector:Number3D = RIGHT.clone();
			if (this._transformDirty) 
			{
				updateTransform();
			}
			Matrix3D.rotateAxis(transform, vector);
			var m:Matrix3D = Matrix3D.rotationMatrix(vector.x, vector.y, vector.z, angle);
			this.transform.copy3x3(Matrix3D.multiply3x3(m, transform));
			this._rotationDirty = true;
		}

		public function yaw(angle:Number):void
		{
			angle = Papervision3D.useDEGREES ? angle * toRADIANS : angle;
			var vector:Number3D = UP.clone();
			if (this._transformDirty) 
			{
				updateTransform();
			}
			Matrix3D.rotateAxis(transform, vector);
			var m:Matrix3D = Matrix3D.rotationMatrix(vector.x, vector.y, vector.z, angle);
			this.transform.copy3x3(Matrix3D.multiply3x3(m, transform));
			this._rotationDirty = true;
		}
		
		/**
		 * Z轴旋转度数
		 * @param	angle
		 */
		public function roll(angle:Number):void
		{
			angle = Papervision3D.useDEGREES ? angle * toRADIANS : angle;
			var vector:Number3D = FORWARD.clone();
			if (this._transformDirty)
			{
				updateTransform();
			}
			Matrix3D.rotateAxis(transform, vector);
			var m:Matrix3D = Matrix3D.rotationMatrix(vector.x, vector.y, vector.z, angle);
			this.transform.copy3x3(Matrix3D.multiply3x3(m, transform));
			this._rotationDirty = true;
		}
		
		/**
		 * 用于镜头？
		 * @param	targetObject
		 * @param	upAxis
		 */
		public function lookAt(targetObject:DisplayObject3D, upAxis:Number3D = null):void
		{
			var position:Number3D = new Number3D(this.x, this.y, this.z);
			var target:Number3D = new Number3D(targetObject.x, targetObject.y, targetObject.z);
			var zAxis:Number3D = Number3D.sub(target, position);
			zAxis.normalize();
			if (zAxis.modulo > 0.1)
			{
				var xAxis:Number3D = Number3D.cross(zAxis, upAxis || UP);
				xAxis.normalize();
				var yAxis:Number3D = Number3D.cross(zAxis, xAxis);
				yAxis.normalize();
				var look:Matrix3D = this.transform;
				look.n11 = xAxis.x;
				look.n21 = xAxis.y;
				look.n31 = xAxis.z;
				look.n12 = -yAxis.x;
				look.n22 = -yAxis.y;
				look.n32 = -yAxis.z;
				look.n13 = zAxis.x;
				look.n23 = zAxis.y;
				look.n33 = zAxis.z;
				this._transformDirty = false;
				this._rotationDirty = true;
			}
			else
			{
				trace("lookAt Error");
			}
		}
		
		/**
		 * 复制位置
		 * @param	reference 显示对象或矩阵
		 */
		public function copyPosition(reference:*):void
		{
			var trans:Matrix3D = this.transform;
			var matrix:Matrix3D = (reference is DisplayObject3D) ? reference.transform : reference;
			trans.n14 = matrix.n14;
			trans.n24 = matrix.n24;
			trans.n34 = matrix.n34;
		}
		
		/**
		 * 复制转换矩阵
		 * @param	reference 显示对象或矩阵
		 */
		public function copyTransform(reference:*):void
		{
			var trans:Matrix3D = this.transform;
			var matrix:Matrix3D = (reference is DisplayObject3D) ? reference.transform : reference;
			trans.n11 = matrix.n11;
			trans.n12 = matrix.n12;
			trans.n13 = matrix.n13;	
			trans.n14 = matrix.n14;
			trans.n21 = matrix.n21;		
			trans.n22 = matrix.n22;
			trans.n23 = matrix.n23;		
			trans.n24 = matrix.n24;
			trans.n31 = matrix.n31;		
			trans.n32 = matrix.n32;
			trans.n33 = matrix.n33;		
			trans.n34 = matrix.n34;
			this._transformDirty = false;
			this._rotationDirty = true;
		}
		
		/**
		 * 更新平移矩阵
		 */
		protected function updateTransform():void
		{
			var q:Object = Matrix3D.euler2quaternion(-this._rotationY, -this._rotationZ, this._rotationX);
			var m:Matrix3D = Matrix3D.quaternion2matrix(q.x, q.y, q.z, q.w);
			var transform:Matrix3D = this.transform;
			m.n14 = transform.n14;
			m.n24 = transform.n24;
			m.n34 = transform.n34;
			transform.copy(m);
			var scaleM:Matrix3D = Matrix3D.IDENTITY;
			scaleM.n11 = this._scaleX;
			scaleM.n22 = this._scaleY;
			scaleM.n33 = this._scaleZ;
			this.transform = Matrix3D.multiply(transform, scaleM);
			this._transformDirty = false;
		}
		
		/**
		 * 字符串形式
		 * @return
		 */
		public override function toString():String
		{
			return this.name + ': x:' + Math.round(this.x) + ' y:' + Math.round(this.y) + ' z:' + Math.round(this.z);
		}
	}
}
