package cl.duoc.gestoroportunidadescorporativas.di

import androidx.room.Database
import androidx.room.RoomDatabase
import cl.duoc.gestoroportunidadescorporativas.data.local.OportunidadDAO
import cl.duoc.gestoroportunidadescorporativas.data.local.OportunidadEntity


@Database(
    entities = [OportunidadEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun oportunidadDAO(): OportunidadDAO
}