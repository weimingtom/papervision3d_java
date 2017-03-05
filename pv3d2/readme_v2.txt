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
(1) org.papervision3d.core.proto.DisplayObjectContainer3D 三维显示对象列表
(2) org.papervision3d.objects.DisplayObject3D 三维显示对象
(3) org.papervision3d.core.geom.Vertices3D 通过顶点数组构造的三维显示对象
(4) org.papervision3d.core.geom.Mesh3D 三维网格
(5) org.papervision3d.objects.Plane 三维平面

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

3. 显示列表和显示对象 
* DisplayObjectContainer3D是管理多个平行地位的DisplayObject3D的数据结构，相当于AS3中的显示容器基类DisplayObjectContainer。内部维护着一个显示对象名称与DisplayObject3D的双向映射表。它作为一种重要的数据结构，是SceneObject3D和DisplayObject3D的基类（pv3d中还有MaterialsList具有相似的机制）。
* DisplayObject3D表示一个三维对象（默认最开始是一个空白的三维空间）。很多新建属性是对外的，用于场景动态渲染，而自身静态的三维属性由geometry域的GeometryObject3D对象保存。它支持矩阵转换（位移或旋转）、碰撞检测、材质管理、投影渲染等基本操作。
* Vertices3D，Mesh3D和Plane是DisplayObject3D的子类，简化DisplayObject3D的构造。Vertices3D从顶点数组中构造三维对象；Mesh3D作为Vertices3D的子类，从材质对象、顶点数组和面数组中构造三维对象；Plane作为Mesh3D的子类，构造一个有界平面的三维对象。

4. 模型加载器
* Ase和Collada都可以用来把外部的模型文件（.ase和.dae格式）数据加载到一个指定的三维对象。不同的是，Ase是Mesh3D的子类，所以它直接把数据加载在自己身上（单个三维对象），而Collada把加载的三维对象（可以有多个）放入三维对象容器中。
* FileLoadEvent是Ase和Collada派发的事件，用于处理异步加载问题。

5. 场景渲染和镜头
* CameraObject3D和Camera3D都表示渲染场景时使用的投影镜头的信息，CameraObject3D是pv3d的内部实现，用于投影运算，而Camera3D则是CameraObject3D的子类，作为交互接口暴露出去（传给Scene3D对象的renderCamera方法）
* SceneObject3D和Scene3D都表示附在Sprite对象上的主渲染三维容器。由于AS3中Sprite拥有渲染能力，所以PV3D需要以Scene3D作为桥梁把三维投影图像画到Sprite对象上以呈现三维影像。SceneObject3D是pv3d的内部实现，一般是最顶层的根显示容器。而Scene3D是SceneObject3D的子类，对外暴露，用于添加三维对象和执行渲染（渲染方式在这个类中定义，默认不考虑三维对象的z排序）。

6. 全局设置
* Papervision3D用于版本声明和调试输出工具

三、算法与关键代码
1. 数据结构
* 双向查询和集合管理，如DisplayObjectContainer3D和MaterialsList。
* 对象池，如BitmapAssetMaterial
* 三维静态数据大量使用数组

2. 数学运算
* 
