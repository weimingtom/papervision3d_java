# papervision3d_java
Play with papervision3d (pv3d) on java awt. JUST FOR FUN (?)  

## Main Entry and Status  
* org.papervision3d.example.ExampleTransformationRotate  
Status: done    

## Key Code  
* flash.display.Graphics  
public void endFill() {}   
* org.papervision3d.Papervision3D  
static public boolean VERBOSE = false;  

## NOTE: 
This is based on **VERY OLD** version of papervision3d.  

## References / Dependencies:  
* papervision3d  
https://code.google.com/p/papervision3d/  

* timdp/jflow  
https://github.com/timdp/jflow  

* matrix3d/spriteflexjs  
https://github.com/matrix3d/spriteflexjs  

* triangletest  
https://github.com/weimingtom/triangletest  

* joa/apparat  
https://github.com/joa/apparat  
https://github.com/joa/apparat/blob/master/apparat-playerglobal/src/main/java/flash/geom/Matrix.java  

## Screenshot  
* org.papervision3d.example.ExampleTransformationRotate  
![screenshot](/screenshot/screenshot_20170710112344.jpg)    

## AffineTransformOp   
* Mapping triangle to triangle  
triangletest  
https://github.com/weimingtom/triangletest  

* The underlying implementation of AffineTransformOp, see this function in OpenCV:  
cvWarpAffine  
Java version is not implemented, use AffineTransformOp instead.  

* The underlying implementation of Face3D.Render()/Face3D.transformUV(),   
https://github.com/weimingtom/papervision3d_java/blob/master/pv3d/src/org/papervision3d/core/geom/Face3D.java  
see this function in OpenCV:  
cvGetAffineTransform, implemented in papervision3d itself   
((u1,v1),(u2,v2),(u3,v3))->((x1,y1),(x2,y2),(x3,y3))  

Another little different implementation is in Pixelitor, _getAffineTransform, see here:  
https://github.com/weimingtom/papervision3d_java/blob/master/pv3d/src/com/bric/geom/TransformUtils.java  
https://github.com/lbalazscs/Pixelitor/blob/master/src/main/java/com/bric/geom/TransformUtils.java  
https://github.com/pwlam09/FYP/blob/master/src/main/java/com/bric/geom/TransformUtils.java  
https://github.com/weimingtom/triangletest/tree/master/vendor  
