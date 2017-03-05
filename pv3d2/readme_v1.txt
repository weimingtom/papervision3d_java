一、分类概览
1、与贴图无关的（线框）几何运算
(1) org.papervision3d.core.geom.Vertex2D 二维顶点
(2) org.papervision3d.core.Number3D 三维向量
(3) org.papervision3d.core.geom.Vertex3D 三维顶点
(4) org.papervision3d.core.proto.GeometryObject3D 三维几何模型
(5) org.papervision3d.core.Matrix3D 4x3矩阵、欧拉角（欧拉公式）、四元数

2. 贴图几何运算与材质
(1) org.papervision3d.core.NumberUV 贴图向量
(2) org.papervision3d.core.geom.Face3D 三维平面
(3) org.papervision3d.core.proto.MaterialObject3D 三维材质对象
(4) org.papervision3d.materials.MaterialsList 材质列表
(5) org.papervision3d.materials.ColorMaterial 纯颜色材质
(6) org.papervision3d.materials.WireframeMaterial 线框材质
(7) org.papervision3d.materials.BitmapMaterial 位图材质
(8) org.papervision3d.materials.BitmapAssetMaterial 位图资源材质

3. 显示对象
(1) org.papervision3d.objects.DisplayObject3D 三维显示对象
(2) org.papervision3d.core.geom.Vertices3D 通过顶点数组构造的三维显示对象
(3) org.papervision3d.core.geom.Mesh3D 三维网格
(4) org.papervision3d.objects.Plane 三维平面
(5) org.papervision3d.core.proto.DisplayObjectContainer3D 三维显示对象列表

4. 模型文件加载器
(1) org.papervision3d.objects.Ase 加载ASE格式（文本格式）三维模型
(2) org.papervision3d.core.utils.Collada 加载DAE格式（XML格式）三维模型
(3) org.papervision3d.events.FileLoadEvent 加载成功或失败的消息

5. 场景与照相机（三维对象交互）
(1) org.papervision3d.core.proto.CameraObject3D 三维照相机对象
(2) org.papervision3d.cameras.Camera3D 三维交互式照相机对象。
(3) org.papervision3d.core.proto.SceneObject3D 三维场景对象
(4) org.papervision3d.scenes.Scene3D 三维场景渲染器

6. 配置
(1) org.papervision3d.Papervision3D 配置与调试日志

7. 官方demo中未使用或者可以去除的类
(1) com.blitzagency.xray.* 用于日志输出
(2) org.papervision3d.cameras.FreeCamera3D 总是刷新变换的照相机
(3) org.papervision3d.materials.MovieMaterial 影片剪辑材质
(4) org.papervision3d.materials.MovieAssetMaterial 影片剪辑资源的材质
(5) org.papervision3d.objects.PaperPlane 纸飞机网格（用于调试）
(6) org.papervision3d.objects.Stars 星星顶点数组
(7) org.papervision3d.scenes.MovieScene3D 考虑z排序的三维场景渲染

二、各个类的角色和作用
1. 几何变换
* Vertex2D表示二维顶点（虽然它也持有z分量）的值对象，用于投影（构成DisplayObject3D对象的projected数组）
* Number3D和Vertex3D都表示三维顶点，不同的是Number3D支持多种几何运算，而Vertex3D只是值对象类。Vertex3D构成GeometryObject3D的vertices数组，表示三维对象的顶点数组。
* GeometryObject3D描述一个三维对象的所有静态模型数据的集合。包括Face3D数组faces和Vertex3D数组vertices。另外还可以计算包装盒，用于碰撞测试。
* Matrix3D表示一个4x3矩阵，用于一些基本的三维运算。大多数运算以static方法存在。还支持矩阵与欧拉角、四元数的转换。

2. 贴图变换
* NumberUV表示贴图的UV值（贴图上与三维坐标对应的二维坐标）
* Face3D表示贴图平面，保存顶点数组和UV数组和材质名。然后根据某个显示对象的投影信息把贴图平面画在Sprite对象上（即渲染）。
* MaterialObject3D表示用于贴图的材质，支持纯色填充、纯色线框、位图和动画。
* MaterialsList表示材质名与MaterialObject3D的映射表，支持材质名和材质的双向查询
* ColorMaterial, WireframeMaterial, BitmapMaterial, BitmapAssetMaterial都是MaterialObject3D的子类，用于简化MaterialObject3D的使用（因为MaterialObject3D同时支持颜色、线框、位图材质）。另外，BitmapAssetMaterial是一个较复杂的实现，它使用了反射和对象池。

3. 

