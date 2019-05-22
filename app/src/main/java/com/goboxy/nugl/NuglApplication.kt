package com.goboxy.nugl

import android.app.Application
import org.greenrobot.eventbus.EventBus

class NuglApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        EventBus
                .builder()
                .throwSubscriberException(true)
                .installDefaultEventBus()
    }

}