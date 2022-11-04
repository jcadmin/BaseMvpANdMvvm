package com.joey.biz.log

import android.content.Context
import com.apkfuns.logutils.LogUtils
import com.joey.biz.log.entity.LogInfoMigration
import com.joey.biz.log.entity.LogInfoModule
import com.joey.jiang.common.router.router.ActionRouterConstants
import com.therouter.app.flowtask.lifecycle.FlowTask
import com.therouter.flow.TheRouterFlowTask
import com.yh.krealmextensions.RealmConfigManager
import io.realm.Realm
import io.realm.RealmConfiguration

@FlowTask(
    taskName = ActionRouterConstants.ACTION_BIZ_LOG_INIT, dependsOn = TheRouterFlowTask.APP_ONCREATE
)
fun initModule(context: Context) {
    initDataBase(context)
    initLogUtil(context)
}

private fun initLogUtil(context: Context) {
    LogUtils.getLogConfig().configAllowLog(true).configShowBorders(false)
}

/**
 * 初始化数据库
 */
private fun initDataBase(context: Context) {
    initRealm(context)
}

/**
 * 初始化realm数据库
 */
private fun initRealm(context: Context) {
    Realm.init(context)

    val logInfoConfig = RealmConfiguration.Builder().schemaVersion(2)
        .deleteRealmIfMigrationNeeded()
//        .migration(LogInfoMigration())
    RealmConfigManager.initModule(LogInfoModule::class.java, logInfoConfig)
}