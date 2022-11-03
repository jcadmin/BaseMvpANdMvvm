package com.joye.jiang.biz.mvp

import android.content.Context
import com.joey.jiang.common.router.provider.LogService
import com.joey.jiang.common.router.router.ActionRouterConstants
import com.therouter.TheRouter
import com.therouter.app.flowtask.lifecycle.FlowTask
import com.therouter.flow.TheRouterFlowTask

@FlowTask(
    taskName = ActionRouterConstants.ACTION_MVP_INIT, dependsOn = TheRouterFlowTask.APP_ONCREATE
)
fun initModule(context: Context) {
    TheRouter.get(LogService::class.java)?.trackLog("mvp init")
}