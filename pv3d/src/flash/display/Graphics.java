package flash.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.papervision3d.core.NumberUV;
import org.papervision3d.core.geom.Face3D;

import com.bric.geom.TransformUtils;

import flash.geom.Matrix;
import flash.port.FlashWindow;

public class Graphics {
	public FlashWindow _window;
	public java.awt.Graphics _graph;
	private double _currentX;
	private double _currentY;
	private double _lastX;
	private double _lastY;
	private BufferedImage _affineImage;
	
	public double _offsetX;
	public double _offsetY;
	
	private boolean _isFillColor = false;
	private int _fillColor = 0;
	private double _fillAlpha = 1.0;
	private List<Double> _fillX = new ArrayList<Double>();
	private List<Double> _fillY = new ArrayList<Double>();
	private boolean _isLineColor = false;
	private int _lineColor = 0;
	private double _lineAlpha = 1.0;
	private double _lineThickness = 1.0;
	private boolean _isFillBitmap = false;
	private BitmapData _fillBitmapData = null;
	private Matrix _fillMatrix = null;
	
	public Matrix _getAffineTransform(Face3D face, BitmapData texture,
			double x0, double y0, double x1, double y1, double x2, double y2) {
		List<NumberUV> uv = face.uv;
		double w = texture.getWidth();
		double h = texture.getHeight();
		double u0 = uv.get(0).u * w;
		double v0 = uv.get(0).v * h;
		double u1 = uv.get(1).u * w;
		double v1 = uv.get(1).v * h;
		double u2 = uv.get(2).u * w;
		double v2 = uv.get(2).v * h;
		
		//FIXME: don't be calculated here, be calculated in endFill()
//		x0 += _offsetX;
//		y0 += _offsetY;
//		x1 += _offsetX;
//		y1 += _offsetY;
//		x2 += _offsetX;
//		y2 += _offsetY;
		
		AffineTransform transform = TransformUtils.createAffineTransform(u0, v0, u1, v1, u2, v2, 
				x0, y0, x1, y1, x2, y2);
		double[] flatmatrix = new double[6];
		transform.getMatrix(flatmatrix);
		return new Matrix(flatmatrix[0],flatmatrix[1],flatmatrix[2],flatmatrix[3],flatmatrix[4],flatmatrix[5]);
	}
	
	public void clear() {
		if (_graph != null) {
			//_graph.clearRect(0, 0, 800, 600); //FIXME:
			_graph.clearRect(0, 0, this._window.getWidth(), this._window.getHeight());
		}
	}
	
	public void beginBitmapFill(BitmapData bitmap, 
		Matrix matrix, boolean repeat, 
		boolean smooth) {
		this._isFillBitmap = true;
		this._fillBitmapData = bitmap;
		this._fillMatrix = matrix;
	}
	
	public void beginFill(int color, double alpha) {
		this._isFillColor = true;
		this._fillColor = color;
		this._fillAlpha = alpha;
	}
	
	public void lineStyle() {
		lineStyle(0.0, 0, 1.0);
	}
	public void lineStyle(double thickness, 
		int color, double alpha) {
		lineStyle(thickness, color, alpha, false, "normal", null, null, 3);
	}
	public void lineStyle(double thickness, 
		int color, double alpha, boolean pixelHinting, 
		String scaleMode, String caps, 
		String joints, double miterLimit) {
		if (true) {
			if (this._lineThickness > 0) {
				this._isLineColor = true;
				this._lineThickness = thickness;
				this._lineColor = color;
			}
		} else {
			this._isLineColor = true;
			this._lineThickness = 1.0;
			this._lineColor = 0x000000;		
		}
	}
	
	public void endFill() {
		if (_graph != null) {
			if (_graph instanceof Graphics2D) {
				((Graphics2D)_graph).setRenderingHint(
					RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
			}
			if (true && this._isLineColor && _graph != null) {
				int r = (this._lineColor >> 24) & 0xff;
				int g = (this._lineColor >> 18) & 0xff;
				int b = (this._lineColor >>  0) & 0xff;
				int a = (int)(this._lineAlpha * 0xff) & 0xff;
				_graph.setColor(new Color(r, g, b, a));
				if (_graph instanceof Graphics2D) {
					((Graphics2D)_graph).setStroke(new BasicStroke((float)this._lineThickness));
				}
				_graph.drawLine(
					(int)_offsetX + (int)_currentX, 
					(int)_offsetY + (int)_currentY, 
					(int)_offsetX + (int)_lastX, 
					(int)_offsetY + (int)_lastY);
			}
			
			if (false && this._isLineColor) {
				int r = (this._lineColor >> 24) & 0xff;
				int g = (this._lineColor >> 18) & 0xff;
				int b = (this._lineColor >>  0) & 0xff;
				int a = (int)(this._lineAlpha * 0xff) & 0xff;
				_graph.setColor(new Color(r, g, b, a));
				int[] arrFillX = new int[_fillX.size()];
				int[] arrFillY = new int[_fillY.size()];
				for (int i = 0; i < arrFillX.length; ++i) {
					arrFillX[i] = _fillX.get(i).intValue();
				}
				for (int i = 0; i < arrFillY.length; ++i) {
					arrFillY[i] = _fillY.get(i).intValue();
				}
				_graph.drawPolygon(arrFillX, arrFillY, arrFillX.length);
			}
			if (this._isFillColor) {
				int r = (this._fillColor >> 24) & 0xff;
				int g = (this._fillColor >> 18) & 0xff;
				int b = (this._fillColor >>  0) & 0xff;
				int a = (int)(this._fillAlpha * 0xff) & 0xff;
				_graph.setColor(new Color(r, g, b, a));
				int[] arrFillX = new int[_fillX.size()];
				int[] arrFillY = new int[_fillY.size()];
				for (int i = 0; i < arrFillX.length; ++i) {
					arrFillX[i] = _fillX.get(i).intValue();
				}
				for (int i = 0; i < arrFillY.length; ++i) {
					arrFillY[i] = _fillY.get(i).intValue();
				}
				_graph.fillPolygon(arrFillX, arrFillY, arrFillX.length);
			}
			if (_isFillBitmap) {
				GeneralPath path = new GeneralPath();
				path.moveTo(_fillX.get(0), _fillY.get(0));
				path.lineTo(_fillX.get(1), _fillY.get(1));
				path.lineTo(_fillX.get(2), _fillY.get(2));
				_graph.setClip(path);
				if (_affineImage == null) {
					_affineImage = new BufferedImage(this._window.getWidth(), this._window.getHeight(), BufferedImage.TYPE_INT_ARGB);
				}
				//try {
					AffineTransform transform = new AffineTransform(
							this._fillMatrix.a,
							this._fillMatrix.b,
							this._fillMatrix.c,
							this._fillMatrix.d,
							this._fillMatrix.tx + _offsetX, //FIXME:offset is not calculated before, so put it here
							this._fillMatrix.ty + _offsetY);
					AffineTransformOp op = new AffineTransformOp(transform,  
			                AffineTransformOp.TYPE_BILINEAR);  
			        op.filter(this._fillBitmapData._image, _affineImage);  				
					_graph.drawImage(_affineImage, 0, 0, null);
					_graph.setClip(null);
				//} catch (Exception e) { e.printStackTrace();}
				/*
				AffineTransform translationTransform = //new AffineTransform();
						new AffineTransform(
								this._fillMatrix.a,
								this._fillMatrix.b,
								this._fillMatrix.c,
								this._fillMatrix.d,
								this._fillMatrix.tx,
								this._fillMatrix.ty);
				translationTransform.translate(_offsetX, _offsetY);
				if (_graph instanceof Graphics2D) {
					((Graphics2D)_graph).drawRenderedImage(
						this._fillBitmapData._image, 
						translationTransform);
				}
				*/
			}
			_isLineColor = false;
			_isFillColor = false;
			_fillX.clear();
			_fillY.clear();
			if (_graph instanceof Graphics2D) {
				((Graphics2D)_graph).setRenderingHint(
					RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_OFF);
			}
		}
	}
	
	public void lineTo(double x, double y) {		
		if (true && this._isLineColor && _graph != null) {
			int r = (this._lineColor >> 24) & 0xff;
			int g = (this._lineColor >> 18) & 0xff;
			int b = (this._lineColor >>  0) & 0xff;
			int a = (int)(this._lineAlpha * 0xff) & 0xff;
			_graph.setColor(new Color(r, g, b, a));
			_graph.drawLine(
				(int)_offsetX + (int)x, 
				(int)_offsetY + (int)y, 
				(int)_offsetX + (int)_lastX, 
				(int)_offsetY + (int)_lastY);
		}
		_lastX = x;
		_lastY = y;
		_fillX.add(_offsetX + x);
		_fillY.add(_offsetY + y);
	}

	public void moveTo(double x, double y) {
		_currentX = x;
		_currentY = y;
		_lastX = x;
		_lastY = y;
		_fillX.clear();
		_fillY.clear();
		_fillX.add(_offsetX + x);
		_fillY.add(_offsetY + y);
	}
}
