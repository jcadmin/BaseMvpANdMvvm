-keep class android.databinding.** { *; }
-keep class com.joye.jiang.common.sdk.extension.Rxjava3ExtensionKt {*;}
-keep class com.joye.jiang.common.sdk.extension.RxjavaExtensionKt {*;}
-keep class com.joye.jiang.common.sdk.BitmapExtensionKt{*;}
-keep class com.joye.jiang.common.sdk.FileExtensionKt{*;}
-keep class com.joye.jiang.common.sdk.DraweeViewExtensionKt{*;}
#-keep class com.joye.jiang.common.sdk.UtilsKt{*;}
-keep class com.joye.jiang.common.sdk.MathExtensionKt{*;}
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# If you use the byType method to obtain Service, add the following rules to protect the interface:
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# If single-type injection is used, that is, no interface is defined to implement IProvider, the following rules need to be added to protect the implementation
 # -keep class * implements com.alibaba.android.arouter.facade.template.IProvider
 -keep class com.joye.jiang.common.sdk.aidl.*