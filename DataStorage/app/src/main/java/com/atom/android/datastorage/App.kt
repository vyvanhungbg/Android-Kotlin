package com.atom.android.datastorage

import android.app.Application
import com.facebook.stetho.Stetho
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmMigration


class App : Application() {
    private val mRealmMigration: RealmMigration =
        RealmMigration { realm, oldVersion, newVersion -> }
    override fun onCreate() {
        super.onCreate()
        //Stetho.initializeWithDefaults(this)
            Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
               // .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                .build());

        Realm.init(this)
        val realmConfiguration: RealmConfiguration = RealmConfiguration.Builder()
            //.migration(mRealmMigration)
            .build()
        Realm.setDefaultConfiguration (realmConfiguration)
    }


}