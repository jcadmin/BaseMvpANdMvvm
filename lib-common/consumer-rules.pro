-keep class android.databinding.** { *; }
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.viewholder.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.viewholder.BaseViewHolder {
     <init>(...);
}
-keepattributes InnerClasses

-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn androidx.**