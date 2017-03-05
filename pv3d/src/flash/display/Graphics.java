package flash.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import flash.geom.Matrix;
import flash.port.FlashWindow;

public class Graphics {
	public FlashWindow _window;
	public java.awt.Graphics _graph;
	private double _currentX;
	private double _currentY;
	private double _lastX;
	private double _lastY;
	
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
	
	public void clear() {
		if (_graph != null) {
			_graph.clearRect(0, 0, 800, 600);
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
