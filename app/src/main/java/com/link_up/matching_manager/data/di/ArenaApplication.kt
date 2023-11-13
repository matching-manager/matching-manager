package com.link_up.matching_manager.data.di

import android.app.Application
import dagger.Component


class ArenaApplication : Application() {
    lateinit var applicationComponent : ApplicationComponent

    override fun onCreate() {
        super.onCreate()
         applicationComponent = DaggerApplicationComponent.create()
    }
}