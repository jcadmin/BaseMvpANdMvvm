package com.joey.biz.log.entity

import io.realm.DynamicRealm
import io.realm.RealmMigration
import io.realm.RealmSchema

class LogInfoMigration : RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        var version = oldVersion

        val schema: RealmSchema = realm.schema

    }
}