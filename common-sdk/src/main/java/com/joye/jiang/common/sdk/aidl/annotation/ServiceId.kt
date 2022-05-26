package com.joye.jiang.common.sdk.aidl.annotation

import androidx.annotation.Keep
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @author Lance
 * @date 2019/1/8
 */
//元注解： 注解上的注解
@Keep //保留
@Retention(RetentionPolicy.RUNTIME) //给反射用
//目标
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class ServiceId(val value: String)