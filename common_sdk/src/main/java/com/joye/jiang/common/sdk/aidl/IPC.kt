package com.joye.jiang.common.sdk.aidl

import android.content.Context
import android.util.Log
import androidx.annotation.Keep
import com.google.gson.Gson
import com.joye.jiang.common.sdk.aidl.model.Request
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

@Keep
object IPC {
    /**
     * 服务端注册
     */
    fun register(service: Class<*>) {
        Registry.instance.register(service)
    }

    /**
     * 连接APK进程服务
     * @param packageName apk包名，服务在本app可以不传此参数，服务在其他app必须传
     */
    fun connect(
        context: Context,
        service: Class<out IPCService>,
        packageName: String?=null
    ) {
        Channel.instance.bind(context.applicationContext, packageName, service)
    }

    /**
     * 断开连接
     * @param context
     * @param service
     */
    fun disConnect(context: Context, service: Class<out IPCService>) {
        Channel.instance.unbind(context.applicationContext, service)
    }

    /**
     * 获取目标对象，此对象为单例，获取单例的方法是"getInstance"的
     * @param service 继承IPCService的class，比如IPCService.IPCService0
     * @param instanceClass 需要返回的对象，必须是interface
     * @param parameters 执行方法的参数
     * @return 目标instanceClass的实例
     */
    fun <T> getInstance(
        service: Class<out IPCService>, instanceClass: Class<T>,
        parameters: Array<Any>? = null
    ): T? {
        return getInstanceWithName(service, instanceClass, "getInstance", parameters)
    }

    /**
     * 获取目标对象，此对象为单例
     * @param service 继承IPCService的class，比如IPCService.IPCService0
     * @param instanceClass 需要返回的对象，必须是interface
     * @param methodName 请求的方法名，获取单例的方法名
     * @param parameters 执行方法的参数
     * @return 目标instanceClass的实例
     */
    fun <T> getInstanceWithName(
        service: Class<out IPCService>,
        instanceClass: Class<T>,
        methodName: String,
        parameters: Array<Any>?=null
    ): T? {
        require(instanceClass.isInterface) { "必须以interface接口进行通信。" }
        //服务器响应
        val response = Channel.instance.send(
            Request.GET_INSTANCE, service,
            instanceClass, methodName, parameters
        )
        // response： 成功，返回一个假的对象 动态代理
        return if (response.isSuccess) getProxy(instanceClass, service) else null
    }

    /**
     * 获取代理对象
     */
    private fun <T> getProxy(instanceClass: Class<T>, service: Class<out IPCService>): T {
        val classLoader = instanceClass.classLoader
        return Proxy.newProxyInstance(
            classLoader, arrayOf<Class<*>>(instanceClass),
            IPCInvocationHandler(instanceClass, service)) as T
    }

    /**
     * 获取代理对象的方法
     */
    internal class IPCInvocationHandler(
        private val instanceClass: Class<*>,
        private val service: Class<out IPCService>
    ) : InvocationHandler {
        var gson = Gson()
        @Throws(Throwable::class)
        override fun invoke(proxy: Any, method: Method,args: Array<Any>?): Any? {
            //请求服务端执行对应的方法。
            val response = Channel.instance.send(
                Request.GET_METHOD, service, instanceClass,
                method.name, args
            )
            if (response.isSuccess) {
                val returnType = method.returnType
                //不是返回null
                if (returnType != Void::class.java && returnType != Void.TYPE) {
                    //获取Location的json字符
                    val source = response.source
                    //反序列化 回 Location
                    return gson.fromJson(source, returnType)
                }
            }
            return null
        }
    }
}