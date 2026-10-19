package cl.duoc.gestoroportunidadescorporativas

import android.app.Application
import cl.duoc.gestoroportunidadescorporativas.data.local.AppContainer


class OportunidadesApplication: Application() {

    lateinit var container: AppContainer private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}