package org.papervision3d.materials;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.papervision3d.core.proto.MaterialObject3D;

/**
 * 材质列表，用于名称和材质对象的双向查询
 */
public class MaterialsList {
	/**
	 * 材质列表（强键，因为materialsByName才是主要的）
	 * 用于反向查询名称
	 */
	protected Map<MaterialObject3D, String> _materials;
	/**
	 * 材质列表个数
	 */
	private int _materialsTotal;
	/**
	 * 材质哈希表（弱键）
	 */
	public Map<String, MaterialObject3D> materialsByName;
	
	/**
	 * 材质个数
	 */
	public int getNumMaterials() {
		return this._materialsTotal;
	}
	
	public MaterialsList() {
		this(null);
	}
	
	/**
	 * 把材质数组或哈希表转换为一个材质列表
	 * @param	materials
	 */
	public MaterialsList(Object materials) {
		this.materialsByName = new HashMap<String, MaterialObject3D>(); //FIXME:
		this._materials = new HashMap<MaterialObject3D, String>(); //FIXME: 
		this._materialsTotal = 0;
		if (materials != null) {
			if (materials instanceof List) {
				for (MaterialObject3D i : (List<MaterialObject3D>)materials) {
					this.addMaterial(i);
				}
			} else if (materials instanceof Map) {
				for (String name : ((Map<String, MaterialObject3D>)materials).keySet()) {
					this.addMaterial(((Map<String, MaterialObject3D>)materials).get(name), name);
				}
			}
		}
	}
	
	public MaterialObject3D addMaterial(MaterialObject3D material) {
		return addMaterial(material, null);
	}
	
	/**
	 * 添加材质
	 * @param	material 材质
	 * @param	name 如果是数组则缺省，如果是哈希表则为键
	 * @return 参数的材质
	 */
	public MaterialObject3D addMaterial(MaterialObject3D material, String name) {
		if (name != null) {
			//do nothing
		} else if (material.name != null) {
			name = material.name;
		} else {
			name = Integer.toString(material.id);
		}
		this._materials.put(material, name);
		this.materialsByName.put(name, material);
		this._materialsTotal++;
		return material;
	}
	
	/**
	 * 根据材质删除
	 * 由于使用字典，必须使用delete进行删除
	 * @param	material
	 * @return
	 */
	public MaterialObject3D removeMaterial(MaterialObject3D material) {
		this.materialsByName.remove(this._materials.get(material));
		this._materials.remove(material);
		return material;
	}
	
	/**
	 * 根据名称查询
	 * @param	name
	 * @return
	 */
	public MaterialObject3D getMaterialByName(String name) {
		return this.materialsByName.get(name);
	}
	
	/**
	 * 根据名称删除
	 * @param	name
	 * @return
	 */
	public MaterialObject3D removeMaterialByName(String name) {
		return removeMaterial(getMaterialByName(name));
	}
	
	/**
	 * 复制
	 * @return
	 */
	public MaterialsList clone() {
		MaterialsList cloned = new MaterialsList();
		for (MaterialObject3D m : this.materialsByName.values()) {
			cloned.addMaterial(m.clone(), this._materials.get(m));
		}
		return cloned;
	}
	
	/**
	 * 字符串形式，用于调试
	 * @return
	 */
	public String toString() {
		String list = "";
		for (MaterialObject3D m : this.materialsByName.values()) {
			list += this._materials.get(m) + "\n";
		}
		return list;
	}
}
