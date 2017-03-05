package org.papervision3d.materials
{
	import flash.utils.Dictionary;
	
	import org.papervision3d.core.proto.MaterialObject3D;
	
	/**
	 * 材质列表，用于名称和材质对象的双向查询
	 */
	public class MaterialsList
	{
		/**
		 * 材质列表（强键，因为materialsByName才是主要的）
		 * 用于反向查询名称
		 */
		protected var _materials:Dictionary;
		/**
		 * 材质列表个数
		 */
		private var _materialsTotal:int;
		/**
		 * 材质哈希表（弱键）
		 */
		public var materialsByName:Dictionary;
		
		/**
		 * 材质个数
		 */
		public function get numMaterials():int
		{
			return this._materialsTotal;
		}
		
		/**
		 * 把材质数组或哈希表转换为一个材质列表
		 * @param	materials
		 */
		public function MaterialsList(materials:* = null):void
		{
			this.materialsByName = new Dictionary(true);
			this._materials = new Dictionary(false); 
			this._materialsTotal = 0;
			if (materials)
			{
				if (materials is Array)
				{
					for (var i:String in materials)
					{
						this.addMaterial(materials[i]);
					}
				}
				else if (materials is Object)
				{
					for (var name:String in materials)
					{
						this.addMaterial(materials[name], name);
					}
				}
			}
		}
		
		/**
		 * 添加材质
		 * @param	material 材质
		 * @param	name 如果是数组则缺省，如果是哈希表则为键
		 * @return 参数的材质
		 */
		public function addMaterial(material:MaterialObject3D, name:String = null):MaterialObject3D
		{
			name = name || material.name || String(material.id);
			this._materials[material] = name;
			this.materialsByName[name] = material;
			this._materialsTotal++;
			return material;
		}
		
		/**
		 * 根据材质删除
		 * 由于使用字典，必须使用delete进行删除
		 * @param	material
		 * @return
		 */
		public function removeMaterial(material:MaterialObject3D):MaterialObject3D
		{
			delete this.materialsByName[this._materials[material]];
			delete this._materials[material];
			return material;
		}
		
		/**
		 * 根据名称查询
		 * @param	name
		 * @return
		 */
		public function getMaterialByName(name:String):MaterialObject3D
		{
			return this.materialsByName[name];
		}
		
		/**
		 * 根据名称删除
		 * @param	name
		 * @return
		 */
		public function removeMaterialByName(name:String):MaterialObject3D
		{
			return removeMaterial(getMaterialByName(name));
		}
		
		/**
		 * 复制
		 * @return
		 */
		public function clone():MaterialsList
		{
			var cloned:MaterialsList = new MaterialsList();
			for each (var m:MaterialObject3D in this.materialsByName)
			{
				cloned.addMaterial(m.clone(), this._materials[m]);
			}
			return cloned;
		}
		
		/**
		 * 字符串形式，用于调试
		 * @return
		 */
		public function toString():String
		{
			var list:String = "";
			for each (var m:MaterialObject3D in this.materialsByName)
			{
				list += this._materials[m] + "\n";
			}
			return list;
		}
	}
}
