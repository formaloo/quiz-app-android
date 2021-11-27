package com.formaloo.quizapp


import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.formaloo.quizapp.di.appComponent
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import java.lang.Exception
import android.database.CursorWindow;
import com.formaloo.quizapp.BuildConfig
import java.lang.reflect.Field;

open class App : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        configureDi()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
        }

        AppCenter.start(
            this, "657491c6-00b7-478b-985c-d1f6c86981e0",
            Analytics::class.java, Crashes::class.java
        )

        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.isAccessible = true
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
//            if (DEBUG_MODE) {
//                e.printStackTrace()
//            }
        }
    }

    //     CONFIGURATION ---
    open fun configureDi() =
        startKoin {
            androidContext(this@App)
            // your modules
            modules(provideComponent())

        }

    //     PUBLIC API ---
    open fun provideComponent() = appComponent

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()
}