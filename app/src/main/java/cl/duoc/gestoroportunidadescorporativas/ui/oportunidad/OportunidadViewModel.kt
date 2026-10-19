package cl.duoc.gestoroportunidadescorporativas.ui.oportunidad


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope

import cl.duoc.gestoroportunidadescorporativas.data.local.OportunidadEntity
import cl.duoc.gestoroportunidadescorporativas.data.repository.OportunidadRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class OportunidadViewModel(private val repository: OportunidadRepository): ViewModel() {

    private val _listaUiState = MutableStateFlow(ListaOportunidadesUiState())
    val listaUiState: StateFlow<ListaOportunidadesUiState> = _listaUiState.asStateFlow()

    private val _formularioUiState = MutableStateFlow(OportunidadFormUiState())
    val formularioUiState: StateFlow<OportunidadFormUiState> = _formularioUiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.obtenerOportunidades().collect { oportunidades ->
                _listaUiState.update {
                    it.copy(oportunidades = oportunidades)
                }
            }
        }
    }

    fun prepararFormulario(id: Long?) {
        if (id == null || id == 0L) {
            _formularioUiState.value = OportunidadFormUiState()
            return
        }

        viewModelScope.launch {
            val oportunidad = repository.obtenerPorId(id)

            if (oportunidad != null) {
                _formularioUiState.value = OportunidadFormUiState(
                    id = oportunidad.id,
                    nombre = oportunidad.nombre,
                    tipo = oportunidad.tipo,
                    descripcion = oportunidad.descripcion,
                    fuente = oportunidad.fuente,
                    prioridad = oportunidad.prioridad,
                    potencial = oportunidad.potencial,
                    estado = oportunidad.estado,
                    observaciones = oportunidad.observaciones,
                    latitud = oportunidad.latitud,
                    longitud = oportunidad.longitud
                )
            }
        }
    }

    fun actualizarNombre(valor: String) {
        _formularioUiState.update {
            it.copy(
                nombre = valor,
                nombreError = if (valor.isBlank()) "El nombre es requerido" else null
            )
        }
    }

    fun actualizarTipo(valor: String) {
        _formularioUiState.update {
            it.copy(
                tipo = valor,
                tipoError = if (valor.isBlank()) "El tipo es requerido" else null
            )
        }
    }

    fun actualizarDescripcion(valor: String) {
        _formularioUiState.update {
            it.copy(
                descripcion = valor,
                descripcionError = if (valor.isBlank()) "La descripción es requerida" else null
            )
        }
    }

    fun actualizarFuente(valor: String) {
        _formularioUiState.update {
            it.copy(
                fuente = valor,
                fuenteError = if (valor.isBlank()) "La fuente es requerida" else null
            )
        }
    }

    fun actualizarPrioridad(valor: String) {
        _formularioUiState.update {
            it.copy(
                prioridad = valor,
                prioridadError = if (valor.isBlank()) "La prioridad es requerida" else null
            )
        }
    }

    fun actualizarPotencial(valor: String) {
        _formularioUiState.update {
            it.copy(
                potencial = valor,
                potencialError = if (valor.isBlank()) "El potencial es requerido" else null
            )
        }
    }

    fun actualizarEstado(valor: String) {
        _formularioUiState.update {
            it.copy(
                estado = valor,
                estadoError = if (valor.isBlank()) "El estado es requerido" else null
            )
        }
    }

    fun actualizarObservaciones(valor: String) {
        _formularioUiState.update {
            it.copy(observaciones = valor)
        }
    }

    fun actualizarUbicacion(latitud: Double?, longitud: Double?) {
        _formularioUiState.update {
            it.copy(latitud = latitud, longitud = longitud, errorGeneral = null)
        }
    }

    fun mostrarError(mensaje: String) {
        _formularioUiState.update {
            it.copy(errorGeneral = mensaje)
        }
    }

    private fun validarCampos(): Boolean {
        val actual = _formularioUiState.value

        val nuevoEstado = actual.copy(
            nombreError = if (actual.nombre.isBlank()) {
                "El nombre es obligatorio."
            } else null,

            tipoError = if (actual.tipo !in OportunidadOpciones.tipos) {
                "El tipo es inválido."
            } else null,

            descripcionError = if (actual.descripcion.isBlank()) {
                "La descripción es obligatoria."
            } else null,

            fuenteError = if (actual.fuente.isBlank()) {
                "La fuente es información obligatoria"
            } else null,

            prioridadError = if (actual.prioridad !in OportunidadOpciones.prioridades) {
                "Selecciona un prioridad valido."
            } else null,

            potencialError = if (actual.potencial !in OportunidadOpciones.potenciales) {
                "Selecciona un potencial valido."
            } else null,

            estadoError = if (actual.estado !in OportunidadOpciones.estados) {
                "Selecciona un estado valido."
            } else null
        )

        _formularioUiState.value = nuevoEstado

        return listOf(
            nuevoEstado.nombreError,
            nuevoEstado.tipoError,
            nuevoEstado.descripcionError,
            nuevoEstado.fuenteError,
            nuevoEstado.prioridadError,
            nuevoEstado.potencialError,
            nuevoEstado.estadoError
        ).all { it == null }
    }

    fun guardar() {
        if (!validarCampos()) return

        viewModelScope.launch {
            _formularioUiState.update {
                it.copy(guardando = true, guardadoExitosamente = false, errorGeneral = null)
            }

            try {
                val form = _formularioUiState.value

                val oportunidad = OportunidadEntity(
                    id = form.id,
                    nombre = form.nombre.trim(),
                    tipo = form.tipo,
                    descripcion = form.descripcion.trim(),
                    fuente = form.fuente.trim(),
                    prioridad = form.prioridad,
                    potencial = form.potencial,
                    estado = form.estado,
                    observaciones = form.observaciones.trim(),
                    latitud = form.latitud,
                    longitud = form.longitud
                )

                repository.guardar(oportunidad)

                _formularioUiState.update {
                    it.copy(
                        guardando = false,
                        guardadoExitosamente = true
                    )
                }
            } catch (e: Exception) {
                _formularioUiState.update {
                    it.copy(guardando = false, errorGeneral = "No se pudo guardar la oportunidad")
                }
            }
        }
    }
}

class OportunidadViewModelFactory(
    private val repository: OportunidadRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(OportunidadViewModel::class.java)) {
            return OportunidadViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}