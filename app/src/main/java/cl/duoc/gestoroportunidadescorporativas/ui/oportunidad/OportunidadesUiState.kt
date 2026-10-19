package cl.duoc.gestoroportunidadescorporativas.ui.oportunidad

import cl.duoc.gestoroportunidadescorporativas.data.local.OportunidadEntity


data class ListaOportunidadesUiState(
    val oportunidades: List<OportunidadEntity> = emptyList()
)

data class OportunidadFormUiState(
    val id: Long = 0L,

    val nombre: String = "",
    val tipo: String = "",
    val descripcion: String = "",
    val fuente: String = "",
    val prioridad: String = "",
    val potencial: String = "",
    val estado: String = "Evento identificado",
    val observaciones: String = "",

    val latitud: Double? = null,
    val longitud: Double? = null,

    val nombreError: String? = null,
    val tipoError: String? = null,
    val descripcionError: String? = null,
    val fuenteError: String? = null,
    val prioridadError: String? = null,
    val potencialError: String? = null,
    val estadoError: String? = null,

    val guardando: Boolean = false,
    val guardadoExitosamente: Boolean = false,
    val errorGeneral: String? = null
)

object OportunidadOpciones {
    val estados = listOf("Evento identificado", "En evaluación", "En gestion", "Cerrada", "Descartada")
    val tipos = listOf("Evento", "Alianza", "Activación", "Patrocinio")
    val prioridades = listOf("Alta", "Media", "Baja")
    val potenciales = listOf("Alto", "Medio", "Bajo")
}
