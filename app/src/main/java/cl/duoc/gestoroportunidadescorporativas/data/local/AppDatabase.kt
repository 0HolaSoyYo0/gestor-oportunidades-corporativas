package cl.duoc.gestoroportunidadescorporativas.data.local

import android.content.Context
import androidx.room.Room
import cl.duoc.gestoroportunidadescorporativas.data.repository.OportunidadRepository
import cl.duoc.gestoroportunidadescorporativas.data.repository.RoomOportunidadRepository
import cl.duoc.gestoroportunidadescorporativas.di.AppDatabase

class AppContainer(context: Context) {
    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "oportunidades_db"
    ).build()

    val oportunidadRepository: OportunidadRepository = RoomOportunidadRepository(
        database.oportunidadDAO()
    )
}