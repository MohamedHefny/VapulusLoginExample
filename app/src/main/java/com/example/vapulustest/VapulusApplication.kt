package com.example.vapulustest

import android.app.Application
import com.example.vapulustest.utils.DomainIntegration

class VapulusApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DomainIntegration.with(this)
    }
}