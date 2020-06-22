package com.example.vapulustest.utils

import android.app.Application
import java.lang.ref.WeakReference

/**
 * This class is used to expose the application context to domain layer from anywhere.
 * */
object DomainIntegration {

    private lateinit var applicationReference: WeakReference<Application>

    fun with(application: Application) {
        applicationReference = WeakReference(application)
    }

    fun getApplication() = applicationReference.get()

}