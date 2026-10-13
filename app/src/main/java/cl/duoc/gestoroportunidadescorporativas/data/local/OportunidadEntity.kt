
package cl.duoc.gestoroportunidadescorporativas.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "oportunidades")
data class OportunidadEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val nombre: String,
    val tipo: String,
    val descripcion: String,
    val fuente: String,
    val prioridad: String,
    val potencial: String,
    val estado: String = "Evento identificado",
    val observaciones: String = "",

    val latitud: Double? = null,
    val longitud: Double? = null
)
