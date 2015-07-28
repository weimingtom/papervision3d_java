package org.papervision3d.core.utils;

import org.papervision3d.core.proto.DisplayObjectContainer3D;
import org.papervision3d.materials.MaterialsList;

public class Collada {
	public Collada(DisplayObjectContainer3D container, 
			String filename, MaterialsList materials, 
			double scale) {
		this(container, filename, materials, scale, null, null);
	}
	
	public Collada(DisplayObjectContainer3D container, 
			String filename, MaterialsList materials, 
			double scale, Runnable callback, Object initObject) {
		
	}
}
