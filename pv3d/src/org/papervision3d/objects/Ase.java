package org.papervision3d.objects;

import java.util.List;
import java.util.Map;

import org.papervision3d.core.geom.Face3D;
import org.papervision3d.core.geom.Mesh3D;
import org.papervision3d.core.geom.Vertex3D;
import org.papervision3d.core.proto.MaterialObject3D;

public class Ase extends Mesh3D{
	public Ase(MaterialObject3D material, List<Vertex3D> vertices,
			List<Face3D> faces, String name, Map<String, Object> initObject) {
		super(material, vertices, faces, name, initObject);
	}
}
