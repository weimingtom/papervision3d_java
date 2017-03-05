package ugame.pv3dmod 
{
	/**
	 * ...
	 * @author 
	 */
	public class Face3D
	{
		public var vertices:Array; //Vertex3D[3]
		public var uv:Array; //NumberUV[3]
		public var materialName:String;
		
		public function Face3D(vertices:Array, materialName:String, uv:Array) 
		{
			this.vertices = vertices;
			this.materialName = materialName;
			this.uv = uv;
		}
	}
}
