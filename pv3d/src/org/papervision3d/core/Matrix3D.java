package org.papervision3d.core;

import java.util.List;

/**
 * 三维转换矩阵
 */
public class Matrix3D {
	static private double toDEGREES = 180 / Math.PI;
	static private double toRADIANS = Math.PI / 180;
	
	public double n11;		
	public double n12;		
	public double n13;		
	public double n14;
	public double n21;		
	public double n22;		
	public double n23;		
	public double n24;
	public double n31;		
	public double n32;		
	public double n33;		
	public double n34;
	
	/**
	 * 构造函数
	 * @param	args
	 */
	public Matrix3D(double[] args) {
		if (args.length >= 12) {
			n11 = args[0]; 
			n12 = args[1];  
			n13 = args[2];  
			n14 = args[3];
			n21 = args[4];  
			n22 = args[5];  
			n23 = args[6];  
			n24 = args[7];
			n31 = args[8];  
			n32 = args[9];  
			n33 = args[10]; 
			n34 = args[11];
		} else {
			n11 = n22 = n33 = 1;
			n12 = n13 = n14 = n21 = n23 = n24 = n31 = n32 = n34 = 0;
		}
	}
	public Matrix3D(List<Double> args2) {
		double[] args = new double[args2.size()];
		int i = 0;
		for (Double d : args2) {
			args[i++] = d;
		}
		if (args.length >= 12) {
			n11 = args[0]; 
			n12 = args[1];  
			n13 = args[2];  
			n14 = args[3];
			n21 = args[4];  
			n22 = args[5];  
			n23 = args[6];  
			n24 = args[7];
			n31 = args[8];  
			n32 = args[9];  
			n33 = args[10]; 
			n34 = args[11];
		} else {
			n11 = n22 = n33 = 1;
			n12 = n13 = n14 = n21 = n23 = n24 = n31 = n32 = n34 = 0;
		}
	}
	
	/**
	 * 单元
	 */
	public static Matrix3D getIDENTITY() {
		return new Matrix3D (
			new double[] {
				1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0
			}
		);
	}
	
	/**
	 * 字符串形式
	 * @return
	 */
	public String toString() {
		String s = "";
		s += (int)(n11*1000)/1000 + "\t\t" + (int)(n12*1000)/1000 + "\t\t" + (int)(n13*1000)/1000 + "\t\t" + (int)(n14*1000)/1000 +"\n";
		s += (int)(n21*1000)/1000 + "\t\t" + (int)(n22*1000)/1000 + "\t\t" + (int)(n23*1000)/1000 + "\t\t" + (int)(n24*1000)/1000 + "\n";
		s += (int)(n31*1000)/1000 + "\t\t" + (int)(n32*1000)/1000 + "\t\t" + (int)(n33*1000)/1000 + "\t\t" + (int)(n34*1000)/1000 + "\n";
		return s;
	}
	
	/**
	 * 乘
	 * @param	m1
	 * @param	m2
	 * @return
	 */
	public static Matrix3D multiply3x3(Matrix3D m1, Matrix3D m2) {
		Matrix3D dest = getIDENTITY();
		double m111 = m1.n11; 
		double m211 = m2.n11;
		double m121 = m1.n21; 
		double m221 = m2.n21;
		double m131 = m1.n31; 
		double m231 = m2.n31;
		double m112 = m1.n12; 
		double m212 = m2.n12;
		double m122 = m1.n22; 
		double m222 = m2.n22;
		double m132 = m1.n32; 
		double m232 = m2.n32;
		double m113 = m1.n13; 
		double m213 = m2.n13;
		double m123 = m1.n23; 
		double m223 = m2.n23;
		double m133 = m1.n33; 
		double m233 = m2.n33;
		dest.n11 = m111 * m211 + m112 * m221 + m113 * m231;
		dest.n12 = m111 * m212 + m112 * m222 + m113 * m232;
		dest.n13 = m111 * m213 + m112 * m223 + m113 * m233;
		dest.n21 = m121 * m211 + m122 * m221 + m123 * m231;
		dest.n22 = m121 * m212 + m122 * m222 + m123 * m232;
		dest.n23 = m121 * m213 + m122 * m223 + m123 * m233;
		dest.n31 = m131 * m211 + m132 * m221 + m133 * m231;
		dest.n32 = m131 * m212 + m132 * m222 + m133 * m232;
		dest.n33 = m131 * m213 + m132 * m223 + m133 * m233;
		dest.n14 = m1.n14;
		dest.n24 = m1.n24;
		dest.n34 = m1.n34;
		return dest;
	}
	
	/**
	 * 旋转
	 * @param	m
	 * @param	v
	 */
	public static void rotateAxis(Matrix3D m, Number3D v) {
		double vx, vy, vz;
		v.x = (vx = v.x) * m.n11 + (vy = v.y) * m.n12 + (vz = v.z) * m.n13;
		v.y = vx * m.n21 + vy * m.n22 + vz * m.n23;
		v.z = vx * m.n31 + vy * m.n32 + vz * m.n33;
		v.normalize();
	}
	
	/**
	 * 乘
	 * @param	m1
	 * @param	m2
	 * @return
	 */
	public static Matrix3D multiply(Matrix3D m1, Matrix3D m2) {
		Matrix3D dest = getIDENTITY();
		double m111, m211, m121, m221, m131, m231;
		double m112, m212, m122, m222, m132, m232;
		double m113, m213, m123, m223, m133, m233;
		double m114, m214, m124, m224, m134, m234;
		dest.n11 = (m111=m1.n11) * (m211=m2.n11) + (m112=m1.n12) * (m221=m2.n21) + (m113=m1.n13) * (m231=m2.n31);
		dest.n12 = m111 * (m212=m2.n12) + m112 * (m222=m2.n22) + m113 * (m232=m2.n32);
		dest.n13 = m111 * (m213=m2.n13) + m112 * (m223=m2.n23) + m113 * (m233=m2.n33);
		dest.n14 = m111 * (m214=m2.n14) + m112 * (m224=m2.n24) + m113 * (m234=m2.n34) + (m114=m1.n14);
		dest.n21 = (m121=m1.n21) * m211 + (m122=m1.n22) * m221 + (m123=m1.n23) * m231;
		dest.n22 = m121 * m212 + m122 * m222 + m123 * m232;
		dest.n23 = m121 * m213 + m122 * m223 + m123 * m233;
		dest.n24 = m121 * m214 + m122 * m224 + m123 * m234 + (m124=m1.n24);
		dest.n31 = (m131=m1.n31) * m211 + (m132=m1.n32) * m221 + (m133=m1.n33) * m231;
		dest.n32 = m131 * m212 + m132 * m222 + m133 * m232;
		dest.n33 = m131 * m213 + m132 * m223 + m133 * m233;
		dest.n34 = m131 * m214 + m132 * m224 + m133 * m234 + (m134=m1.n34);
		return dest;
	}
	
	/**
	 * 加
	 * @param	m1
	 * @param	m2
	 * @return
	 */
	public static Matrix3D add(Matrix3D m1, Matrix3D m2) {
		Matrix3D dest = getIDENTITY();
		dest.n11 = m1.n11 + m2.n11; 	
		dest.n12 = m1.n12 + m2.n12;
		dest.n13 = m1.n13 + m2.n13;	
		dest.n14 = m1.n14 + m2.n14;
		dest.n21 = m1.n21 + m2.n21;	
		dest.n22 = m1.n22 + m2.n22;
		dest.n23 = m1.n23 + m2.n23;	
		dest.n24 = m1.n24 + m2.n24;
		dest.n31 = m1.n31 + m2.n31;	
		dest.n32 = m1.n32 + m2.n32;
		dest.n33 = m1.n33 + m2.n33;	
		dest.n34 = m1.n34 + m2.n34;
		return dest;
	}
	
	/**
	 * 复制
	 * @param	m
	 * @return
	 */
	public Matrix3D copy(Matrix3D m) {
		this.n11 = m.n11;	
		this.n12 = m.n12;
		this.n13 = m.n13;	
		this.n14 = m.n14;
		this.n21 = m.n21;	
		this.n22 = m.n22;
		this.n23 = m.n23;	
		this.n24 = m.n24;
		this.n31 = m.n31;	
		this.n32 = m.n32;
		this.n33 = m.n33;	
		this.n34 = m.n34;
		return this;
	}
	
	/**
	 * 3x3复制
	 * @param	m
	 * @return
	 */
	public Matrix3D copy3x3(Matrix3D m) {
		this.n11 = m.n11;
		this.n12 = m.n12;
		this.n13 = m.n13;
		this.n21 = m.n21;  
		this.n22 = m.n22;   
		this.n23 = m.n23;
		this.n31 = m.n31;   
		this.n32 = m.n32;   
		this.n33 = m.n33;
		return this;
	}

	/**
	 * 复制
	 * @param	m
	 * @return
	 */
	public static Matrix3D clone(Matrix3D m) {
		return new Matrix3D (
			new double[] {
				m.n11, m.n12, m.n13, m.n14,
				m.n21, m.n22, m.n23, m.n24,
				m.n31, m.n32, m.n33, m.n34
			}
		);
	}

	/**
	 * 乘
	 * @param	m
	 * @param	v
	 */
	public static void multiplyVector(Matrix3D m, Number3D v) {
		double vx, vy, vz;
		v.x = (vx = v.x) * m.n11 + (vy = v.y) * m.n12 + (vz = v.z) * m.n13 + m.n14;
		v.y = vx * m.n21 + vy * m.n22 + vz * m.n23 + m.n24;
		v.z = vx * m.n31 + vy * m.n32 + vz * m.n33 + m.n34;
	}
	
	/**
	 * 3x3乘
	 * @param	m
	 * @param	v
	 */
	public static void multiplyVector3x3(Matrix3D m, Number3D v) {
		double vx, vy, vz;
		v.x = (vx = v.x) * m.n11 + (vy = v.y) * m.n12 + (vz = v.z) * m.n13;
		v.y = vx * m.n21 + vy * m.n22 + vz * m.n23;
		v.z = vx * m.n31 + vy * m.n32 + vz * m.n33;
	}
	
	/**
	 * 矩阵转为欧拉数？
	 * @param	mat
	 * @return
	 */
	public static Number3D matrix2euler(Matrix3D mat) {
		Number3D angle = new Number3D();
		double d = -Math.asin(Math.max(-1, Math.min(1, mat.n13)));
		double c = Math.cos(d);
		angle.y = d * toDEGREES;
		double trX, trY;
		if (Math.abs(c) > 0.005) {
			trX = mat.n33 / c;
			trY = -mat.n23 / c;
			angle.x = Math.atan2( trY, trX ) * toDEGREES;
			trX = mat.n11 / c;
			trY = -mat.n12 / c;
			angle.z = Math.atan2( trY, trX ) * toDEGREES;
		} else {
			angle.x = 0;
			trX = mat.n22;
			trY = mat.n21;
			angle.z = Math.atan2( trY, trX ) * toDEGREES;
		}
		return angle;
	}
	
	/**
	 * 欧拉数转矩阵
	 * @param	angle
	 * @return
	 */
	public static Matrix3D euler2matrix(Number3D angle) {
		Matrix3D m = getIDENTITY();
		double ax = angle.x * toRADIANS;
		double ay = angle.y * toRADIANS;
		double az = angle.z * toRADIANS;
		double a = Math.cos(ax);
		double b = Math.sin(ax);
		double c = Math.cos(ay);
		double d = Math.sin(ay);
		double e = Math.cos(az);
		double f = Math.sin(az);
		double ad = a * d;
		double bd = b * d;
		m.n11 = c * e;
		m.n12 = -c * f;
		m.n13 = d;
		m.n21 = bd * e + a * f;
		m.n22 = -bd * f + a * e;
		m.n23 = -b * c;
		m.n31 = -ad * e + b * f;
		m.n32 = ad * f + b * e;
		m.n33 = a * c;
		return m;
	}
	
	/**
	 * 绕X轴旋转
	 * @param	angleRad
	 * @return
	 */
	public static Matrix3D rotationX(double angleRad) {
		Matrix3D m = getIDENTITY();
		double c = Math.cos(angleRad);
		double s = Math.sin(angleRad);
		m.n22 = c;
		m.n23 = -s;
		m.n32 = s;
		m.n33 = c;
		return m;
	}
	
	/**
	 * 绕Y轴旋转
	 * @param	angleRad
	 * @return
	 */
	public static Matrix3D rotationY(double angleRad) {
		Matrix3D m = getIDENTITY();
		double c = Math.cos(angleRad);
		double s = Math.sin(angleRad);
		m.n11 = c;
		m.n13 = -s;
		m.n31 = s;
		m.n33 = c;
		return m;
	}
	
	/**
	 * 绕Z轴旋转
	 * @param	angleRad
	 * @return
	 */
	public static Matrix3D rotationZ(double angleRad) {
		Matrix3D m = getIDENTITY();
		double c = Math.cos(angleRad);
		double s = Math.sin(angleRad);
		m.n11 = c;
		m.n12 = -s;
		m.n21 = s;
		m.n22 = c;
		return m;
	}
	
	/**
	 * 绕矩阵旋转
	 * @param	u
	 * @param	v
	 * @param	w
	 * @param	angle
	 * @return
	 */
	public static Matrix3D rotationMatrix(double u, double v, double w, double angle) {
		Matrix3D m = getIDENTITY();
		double nCos	= Math.cos(angle);
		double nSin	= Math.sin(angle);
		double scos	= 1 - nCos;
		double suv = u * v * scos;
		double svw = v * w * scos;
		double suw = u * w * scos;
		double sw = nSin * w;
		double sv = nSin * v;
		double su = nSin * u;
		m.n11 = nCos + u * u * scos;
		m.n12 = -sw + suv;
		m.n13 = sv + suw;
		m.n21 = sw + suv;
		m.n22 = nCos + v * v * scos;
		m.n23 = -su + svw;
		m.n31 = -sv + suw;
		m.n32 = su + svw;
		m.n33 = nCos + w * w * scos;
		return m;
	}
	
	/**
	 * 平移
	 * @param	u
	 * @param	v
	 * @param	w
	 * @return
	 */
	public static Matrix3D translationMatrix(double u, double v, double w) {
		Matrix3D m = getIDENTITY();
		m.n14 = u;
		m.n24 = v;
		m.n34 = w;
		return m;
	}
	
	/**
	 * 缩放
	 * @param	u
	 * @param	v
	 * @param	w
	 * @return
	 */
	public static Matrix3D scaleMatrix(double u, double v, double w) {
		Matrix3D m = getIDENTITY();
		m.n11 = u;
		m.n22 = v;
		m.n33 = w;
		return m;
	}
	
	/**
	 * 矩阵秩
	 */
	public double getDet() {
		return (this.n11 * this.n22 - this.n21 * this.n12) * this.n33 - (this.n11 * this.n32 - this.n31 * this.n12) * this.n23 +
			(this.n21 * this.n32 - this.n31 * this.n22) * this.n13;
	}
	
	/**
	 * ?
	 * @param	m
	 * @return
	 */
	public static double getTrace(Matrix3D m) {
		return m.n11 + m.n22 + m.n33 + 1;
	}
	
	/**
	 * 逆矩阵
	 * @param	m
	 * @return
	 */
	public static Matrix3D inverse(Matrix3D m) {
		double d = m.getDet();
		if (Math.abs(d) < 0.001) {
			return null;
		}
		d = 1/d;
		double m11 = m.n11; double m21 = m.n21; double m31 = m.n31;
		double m12 = m.n12; double m22 = m.n22; double m32 = m.n32;
		double m13 = m.n13; double m23 = m.n23; double m33 = m.n33;
		double m14 = m.n14; double m24 = m.n24; double m34 = m.n34;
		return new Matrix3D(
			new double[] {
				d * ( m22 * m33 - m32 * m23 ),
				-d* ( m12 * m33 - m32 * m13 ),
				d * ( m12 * m23 - m22 * m13 ),
				-d* ( m12 * (m23*m34 - m33*m24) - m22 * (m13*m34 - m33*m14) + m32 * (m13*m24 - m23*m14) ),
				-d* ( m21 * m33 - m31 * m23 ),
				d * ( m11 * m33 - m31 * m13 ),
				-d* ( m11 * m23 - m21 * m13 ),
				d * ( m11 * (m23*m34 - m33*m24) - m21 * (m13*m34 - m33*m14) + m31 * (m13*m24 - m23*m14) ),
				d * ( m21 * m32 - m31 * m22 ),
				-d* ( m11 * m32 - m31 * m12 ),
				d * ( m11 * m22 - m21 * m12 ),
				-d* ( m11 * (m22*m34 - m32*m24) - m21 * (m12*m34 - m32*m14) + m31 * (m12*m24 - m22*m14) )
			}
		);
	}
	
	/**
	 * 轴旋转
	 * @param	axis
	 * @param	ref
	 * @param	pAngle
	 * @return
	 */
	public static Matrix3D axisRotationWithReference(Number3D axis, Number3D ref, double pAngle) {
		double angle = (pAngle + 360) % 360;
		Matrix3D m = Matrix3D.translationMatrix(ref.x, -ref.y, ref.z);
		m = Matrix3D.multiply(m, Matrix3D.rotationMatrix(axis.x, axis.y, axis.z, angle));
		m = Matrix3D.multiply(m, Matrix3D.translationMatrix(-ref.x, ref.y, -ref.z));
		return m;
	}
	
	/**
	 * 四元数转矩阵
	 * @param	x
	 * @param	y
	 * @param	z
	 * @param	w
	 * @return
	 */
	public static Matrix3D quaternion2matrix(double x, double y, double z, double w) {
		double xx = x * x;
		double xy = x * y;
		double xz = x * z;
		double xw = x * w;
		double yy = y * y;
		double yz = y * z;
		double yw = y * w;
		double zz = z * z;
		double zw = z * w;
		Matrix3D m = getIDENTITY();
		m.n11 = 1 - 2 * (yy + zz);
		m.n12 = 2 * (xy - zw);
		m.n13 = 2 * (xz + yw);
		m.n21 = 2 * (xy + zw);
		m.n22 = 1 - 2 * (xx + zz);
		m.n23 = 2 * (yz - xw);
		m.n31 = 2 * (xz - yw);
		m.n32 = 2 * (yz + xw);
		m.n33 = 1 - 2 * (xx + yy);
		return m;
	}
	
	/**
	 * 欧拉数转四元数
	 * @param	ax
	 * @param	ay
	 * @param	az
	 * @return
	 */
	public static Quaternion euler2quaternion(double ax, double ay, double az) {
		double fSinPitch = Math.sin(ax * 0.5);
		double fCosPitch = Math.cos(ax * 0.5);
		double fSinYaw = Math.sin(ay * 0.5);
		double fCosYaw = Math.cos(ay * 0.5);
		double fSinRoll = Math.sin(az * 0.5);
		double fCosRoll = Math.cos(az * 0.5);
		double fCosPitchCosYaw = fCosPitch * fCosYaw;
		double fSinPitchSinYaw = fSinPitch * fSinYaw;
		Quaternion q = new Quaternion();
		q.x = fSinRoll * fCosPitchCosYaw - fCosRoll * fSinPitchSinYaw;
		q.y = fCosRoll * fSinPitch * fCosYaw + fSinRoll * fCosPitch * fSinYaw;
		q.z = fCosRoll * fCosPitch * fSinYaw - fSinRoll * fSinPitch * fCosYaw;
		q.w = fCosRoll * fCosPitchCosYaw + fSinRoll * fSinPitchSinYaw;
		return q;
	}
	
	/**
	 * 乘四元数
	 * FIXME:qb
	 * @param	qa
	 * @param	qb
	 * @return
	 */
	public static Quaternion multiplyQuaternion(Quaternion qa, Quaternion qb) {
		double w1 = qa.w;
		double x1 = qa.x;  
		double y1 = qa.y;  
		double z1 = qa.z;
		double w2 = qa.w;  
		double x2 = qa.x;  
		double y2 = qa.y;  
		double z2 = qa.z;
		Quaternion q = new Quaternion();
		q.w = w1*w2 - x1*x2 - y1*y2 - z1*z2;
		q.x = w1*x2 + x1*w2 + y1*z2 - z1*y2;
		q.y = w1*y2 + y1*w2 + z1*x2 - x1*z2;
		q.z = w1*z2 + z1*w2 + x1*y2 - y1*x2;
		return q;
	}
	
	/**
	 * 轴转四元数
	 * @param	x
	 * @param	y
	 * @param	z
	 * @param	angle
	 * @return
	 */
	public static Quaternion axis2quaternion(double x, double y, double z, double angle) {
		double sin_a = Math.sin(angle / 2);
		double cos_a = Math.cos(angle / 2);
		Quaternion q = new Quaternion();
		q.x = x * sin_a;
		q.y = y * sin_a;
		q.z = z * sin_a;
		q.w = cos_a;
		return normalizeQuaternion(q);
	}
	
	/**
	 * 四元数大小
	 * @param	q
	 * @return
	 */
	public static double magnitudeQuaternion(Quaternion q) {
		return Math.sqrt(q.w * q.w + q.x * q.x + q.y * q.y + q.z * q.z);
	}
	
	/**
	 * 单位化四元数
	 * @param	q
	 * @return
	 */
	public static Quaternion normalizeQuaternion(Quaternion q) {
		double mag = magnitudeQuaternion(q);
		q.x /= mag;
		q.y /= mag;
		q.z /= mag;
		q.w /= mag;
		return q;
	}
}
