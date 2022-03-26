# BaseMvpANdMvvm
androidx+kotlin+mvp+mvvm+rxjava+retrofit+协程+aRouter+组件化

# 架构说明

`这个项目是mvp和mvvm共存的一个架构`

app_xx模块是app壳包，可以创建不同的app模块、引入不同的biz模块实现按需调试功能

biz_xx模块是业务模块，主要包含各个业务需求的页面以及逻辑

common_base模块是基类模块，主要包含activity、fragment等的基类实现

common_data模块是共用数据模块，主要包含项目用到的共用实体类、aRouter的路由常量、共用常量等

common_resource模块是基于screen_match的dimens资源模块，用来实现屏幕适配

common_sdk模块是工具类模块，主要包含项目中需要用到的工具类，已经对三方包的封装

common_sdk_module模块是三方包的源码引入以及封装

lib_common模块是对于三方包的远程依赖

lib_resources模块是项目共用资源模块

* mvvm还在重构中
