package com.muhammad.lumina

import android.app.Application
import com.muhammad.lumina.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class LuminaApplication : Application(){
    companion object{
        lateinit var INSTANCE : LuminaApplication
    }
    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        startKoin {
            androidContext(this@LuminaApplication)
            androidLogger()
            modules(appModule)
        }
    }
}