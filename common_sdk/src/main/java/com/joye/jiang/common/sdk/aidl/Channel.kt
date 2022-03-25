package com.joye.jiang.common.sdk.aidl

import com.google.gson.Gson
import android.content.ServiceConnection
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.annotation.Keep
import com.joye.jiang.common.sdk.aidl.annotation.ServiceId
import com.joye.jiang.common.sdk.aidl.model.Parameters
import com.joye.jiang.common.sdk.aidl.model.Request
import com.joye.jiang.common.sdk.aidl.model.Response
import java.util.concurrent.ConcurrentHashMap

@Keep
class Channel private constructor() {

    companion object {
        @JvmStatic
        @Volatile
        var instance = Channel()
    }

    //已经绑定过的
    private val binds = ConcurrentHashMap<Class<out IPCService>, Boolean>()

    //正在绑定的
    private val binding = ConcurrentHashMap<Class<out IPCService>, Boolean>()

    //已经绑定的服务对应的ServiceConnect
    private val serviceConnections =
        ConcurrentHashMap<Class<out IPCService>, IPCServiceConnection>()
    private val binders = ConcurrentHashMap<Class<out IPCService>, IIPCService>()
    private val gson = Gson()

    /**
     * 绑定
     */
    fun bind(context: Context, packageName: String?, service: Class<out IPCService>) {
        //是否已经绑定
        val isBound = binds[service]
        if (isBound != null && isBound) return
        //是否正在绑定
        val isBinding = binding[service]
        if (isBinding != null && isBinding) return
        //要绑定了
        binding[service] = true
        val ipcServiceConnection = IPCServiceConnection(service)
        serviceConnections[service] = ipcServiceConnection
        val intent: Intent
        if (packageName.isNullOrEmpty()) {
            intent = Intent(context, service)
        } else {
            intent = Intent()
            intent.setClassName(packageName, service.name)
        }
        context.bindService(intent, ipcServiceConnection, Context.BIND_AUTO_CREATE)
    }

    /**
     * 解绑
     */
    fun unbind(context: Context, service: Class<out IPCService>) {
        val bound = binds[service]
        if (bound != null && bound) {
            val connection = serviceConnections[service]
            if (connection != null) context.unbindService(connection)
            binds[service] = false
        }
    }

    private inner class IPCServiceConnection(private val mService: Class<out IPCService>) :
        ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val ipcService = IIPCService.Stub.asInterface(iBinder)
            binders[mService] = ipcService
            binds[mService] = true
            binding.remove(mService)
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            binders.remove(mService)
            binds.remove(mService)
        }
    }

    /**
     * 请求服务端执行对应的方法。
     */
    fun send(
        type: Int, service: Class<out IPCService>, classType: Class<*>,
        methodName: String, parameters: Array<Any>?
    ): Response {
        // ipcService: 绑定的服务中onbind返回的binder对象
        val ipcService = binders[service]
        ipcService?:return Response(null, false)//没有绑定服务
        //发送请求给服务器
        val annotation = classType.getAnnotation(ServiceId::class.java)
        val serviceId = annotation!!.value
        val request = Request(type, serviceId, methodName, makeParameters(parameters))
        return try {
            ipcService.send(request)
        } catch (e: RemoteException) {
            Response(null, false)
        }
    }

    /**
     * 将参数制作为Parameters
     */
    private fun makeParameters(parameters: Array<Any>?): Array<Parameters?>? =
        if (parameters == null) {
            null
        } else {
            val p = arrayOfNulls<Parameters>(parameters.size)
            for (i in parameters.indices) {
                val obj = parameters[i]
                p[i] = Parameters(obj.javaClass.name, gson.toJson(obj))
            }
            p
        }
}