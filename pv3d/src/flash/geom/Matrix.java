package flash.geom;

/**
 * @see https://github.com/joa/apparat/blob/master/apparat-playerglobal/src/main/java/flash/geom/Matrix.java
 * @see http://blog.csdn.net/android_technology/article/details/52374596
 */
public class Matrix {
	public double a;
	public double b;
	public double c;
	public double d;
	public double tx;
	public double ty;

	/**
	 * a c tx
	 * b d ty
	 * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/geom/Matrix.html
	 */
	public Matrix() {
		this(1, 0, 0, 1, 0, 0);
	}
	
	/**
	 * a c tx
	 * b d ty
	 * @see http://help.adobe.com/en_US/FlashPlatform/reference/actionscript/3/flash/geom/Matrix.html
	 */	
	public Matrix(double a, double b, double c, double d, 
		double tx, double ty) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.tx = tx;
		this.ty = ty;
	}
	
	public void invert () {
		final double det = determinant();
		double dd = (0.0 == det) ? 1.0 : det;

		final double ta = a;
		final double tb = b;
		final double tc = c;
		final double td = d;
		final double ttx = tx;
		final double tty = ty;

		if (false) {
			//see https://github.com/joa/apparat/blob/master/apparat-playerglobal/src/main/java/flash/geom/Matrix.java
			//this is wrong
			a = td / dd;
			b = -tb / dd;
			c = -(tc - ttx) / dd;
			d = ta / dd;
			tx = -(tc - ttx * td) / dd;
			ty = -(ta * tty - ttx * tb) / dd;
		} else {
			//see https://github.com/soywiz/java-flash/blob/master/src/com/soywiz/flash/util/Matrix.java
			if (0.0 == det) {
		        a = 0;
		        b = 0;
		        c = 0;
		        d = 0;
		        tx = -tx;
		        ty = -ty;
		    } else {
		        dd = 1.0 / dd;
		        double a1 = d * dd;
		        d = a * dd;
		        a = a1;
		        b *= -dd;
		        c *= -dd;
		
		        double tx1 = -a * tx - c * ty;
		        ty = -b * tx - d * ty;
		        tx = tx1;
		    }
		}
	}
	
	//https://en.wikipedia.org/wiki/Determinant
	//http://blog.csdn.net/longhuihu/article/details/9746413
	private double determinant() {
		return a * d - b * c;
	}
	
	@Override
	public String toString() {
		return "(a="+a+", b="+b+", c="+c+", d="+d+", tx="+tx+", ty="+ty+")";
	}
}
