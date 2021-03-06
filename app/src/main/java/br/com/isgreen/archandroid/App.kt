package br.com.isgreen.archandroid

import android.app.Application
import br.com.isgreen.archandroid.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created by Éverdes Soares on 08/04/2019.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appComponent)
        }
    }

}