package com.joye.jiang.common.sdk.aidl

import android.app.Service
import android.content.Intent
import com.google.gson.Gson
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.annotation.Keep
import com.joye.jiang.common.sdk.aidl.model.Parameters
import com.joye.jiang.common.sdk.aidl.model.Request
import com.joye.jiang.common.sdk.aidl.model.Response
import java.lang.Exception

/**
 * 与 GPSService在同一个进程： 服务端
 * @author Lance
 * @date 2019/1/8
 */
@Keep
abstract class IPCService : Service() {
    private var gson = Gson()

    override fun onBind(intent: Intent): IBinder? {
        return object : IIPCService.Stub() {
            /**
             * 执行客户端的请求
             */
            @Throws(RemoteException::class)
            override fun send(request: Request): Response {
                //serviceid Location
                val serviceId = request.serviceId
                //从服务表中获得 对应的Class对象。
                //具体类型  Class<LocationManager>
                val instanceClass: Class<*> = Registry.instance.getService(serviceId)
                val parameters = request.parameters
                val objects = restoreParameters(parameters)

                //从方法表中获得 对应的Method对象
                val methodName = request.methodName
                val method = Registry.instance.getMethod(instanceClass, methodName, parameters)
                return when (request.type) {
                    Request.GET_INSTANCE -> try {
                        val instance = method?.invoke(null, *objects)
                        // 单例类的serviceId与 单例对象 保存
                        if (instance == null){
                            Response(null, false)
                        } else {
                            Registry.instance.putObject(serviceId, instance)
                            Response(null, true)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Response(null, false)
                    }
                    Request.GET_METHOD -> try {
                        val obj = Registry.instance.getObject(serviceId)
                        // getLocation 返回Location
                        val returnObject = method?.invoke(obj, *objects)
                        Response(gson.toJson(returnObject), true)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Response(null, false)
                    }
                    else -> Response(null, false)
                }
            }
        }
    }

    /**
     * 将参数数组转成对应类型的数组
     */
    protected fun restoreParameters(parameters: Array<Parameters?>?): Array<Any?> {
        if (parameters == null)return arrayOfNulls(0)
        val objects = arrayOfNulls<Any>(parameters.size)
        for (i in parameters.indices) {
            val parameter = parameters[i]
            //还原
            try {
                objects[i] = gson.fromJson(parameter!!.value, Class.forName(parameter.type))
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        }
        return objects
    }

    /**
     * 预留的aidl的服务进程，自己写的service需要跟下面其中一个的IPCService服务的process名保持一致即可，不是作为父类继承
     */
    class IPCService0 : IPCService()
    class IPCService1 : IPCService()
    class IPCService2 : IPCService()
    class IPCService3 : IPCService()
    class IPCService4 : IPCService()
}