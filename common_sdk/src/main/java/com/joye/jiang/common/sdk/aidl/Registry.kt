package com.joye.jiang.common.sdk.aidl

import android.util.Log
import androidx.annotation.Keep
import com.joye.jiang.common.sdk.aidl.annotation.ServiceId
import com.joye.jiang.common.sdk.aidl.model.Parameters
import java.lang.StringBuilder
import java.lang.reflect.Method
import java.util.HashMap
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Lance
 * @date 2019/1/8
 */
@Keep
class Registry private constructor() {
    companion object {
        @Volatile
        var instance = Registry()
    }

    /**
     * 服务表
     */
    private val services = ConcurrentHashMap<String?, Class<*>>()

    /**
     * 方法表
     */
    private val methods = ConcurrentHashMap<Class<*>, MutableMap<String, Method>>()

    /**
     * 类id：实例对象
     */
    private val objects: MutableMap<String?, Any> = HashMap()

    /**
     * 做两张表
     * 1、服务表 Class的标记 ：Class<？>
     * 2、方法表 Class : ["getLocation":Method,"setLocation":Method]
     * @param clazz
     */
    fun register(clazz: Class<*>) {
        //注册服务表
        //通过反射获得类的标记
        val annotation = clazz.getAnnotation(ServiceId::class.java)
        services.putIfAbsent(annotation!!.value, clazz)

        //注册服务对应的方法表
        methods.putIfAbsent(clazz, HashMap())
        val methods = methods[clazz]!!
        //class中所有的方法
        for (method in clazz.methods) {
            val sb = StringBuilder()
            sb.append(method.name)
            sb.append("(")
            val parameterTypes = method.parameterTypes
            if (parameterTypes.isNotEmpty())sb.append(parameterTypes[0].name)
            for (i in 1 until parameterTypes.size) {
                sb.append(",").append(parameterTypes[i].name)
            }
            sb.append(")")
            methods[sb.toString()] = method
        }
    }

    fun getService(serviceId: String?): Class<*> = services[serviceId]!!

    fun getMethod(
        clazz: Class<*>,
        methodName: String,
        parameters: Array<Parameters?>?
    ): Method? {
        val methods: Map<String, Method> = methods[clazz]!!
        val sb = StringBuilder()
        sb.append(methodName)
        sb.append("(")
        parameters?.let {
            if (it.isNotEmpty())sb.append(it[0]?.type)
            for (i in 1 until it.size) {
                sb.append(",").append(it[i]?.type)
            }
        }
        sb.append(")")
        return methods[sb.toString()]
    }

    fun putObject(serviceId: String?, any: Any) {
        objects[serviceId] = any
    }

    fun getObject(serviceId: String?): Any? {
        return objects[serviceId]
    }
}