
package cl.duoc.gestoroportunidadescorporativas.ui.oportunidad

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices

@Composable
fun OportunidadFormScreen(
    viewModel: OportunidadViewModel,
    onVolver: () -> Unit,
    onGuardado: () -> Unit
) {
    val estado by viewModel.formularioUiState.collectAsState()
    val context = LocalContext.current

    val lanzadorPermisos =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permisos ->
            val autorizado =
                permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (autorizado) {
                leerUltimaUbicacion(context, viewModel)
            } else {
                viewModel.mostrarError(
                    "No se concedió permiso para obtener la ubicación."
                )
            }
        }

    LaunchedEffect(estado.guardadoExitosamente) {
        if (estado.guardadoExitosamente) {
            onGuardado()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = if (estado.id == 0L) {
                "Nueva oportunidad"
            } else {
                "Editar oportunidad"
            },
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = estado.nombre,
            onValueChange = viewModel::actualizarNombre,
            label = { Text("Nombre") },
            isError = estado.nombreError != null,
            supportingText = {
                estado.nombreError?.let { Text(it) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Selector(
            etiqueta = "Tipo",
            valor = estado.tipo,
            opciones = OportunidadOpciones.tipos,
            error = estado.tipoError,
            alSeleccionar = viewModel::actualizarTipo
        )

        OutlinedTextField(
            value = estado.descripcion,
            onValueChange = viewModel::actualizarDescripcion,
            label = { Text("Descripción") },
            isError = estado.descripcionError != null,
            supportingText = {
                estado.descripcionError?.let { Text(it) }
            },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.fuente,
            onValueChange = viewModel::actualizarFuente,
            label = { Text("Fuente de información") },
            isError = estado.fuenteError != null,
            supportingText = {
                estado.fuenteError?.let { Text(it) }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Selector(
            etiqueta = "Prioridad",
            valor = estado.prioridad,
            opciones = OportunidadOpciones.prioridades,
            error = estado.prioridadError,
            alSeleccionar = viewModel::actualizarPrioridad
        )

        Selector(
            etiqueta = "Potencial comercial",
            valor = estado.potencial,
            opciones = OportunidadOpciones.potenciales,
            error = estado.potencialError,
            alSeleccionar = viewModel::actualizarPotencial
        )

        Selector(
            etiqueta = "Estado de gestión",
            valor = estado.estado,
            opciones = OportunidadOpciones.estados,
            error = estado.estadoError,
            alSeleccionar = viewModel::actualizarEstado
        )

        OutlinedTextField(
            value = estado.observaciones,
            onValueChange = viewModel::actualizarObservaciones,
            label = { Text("Observaciones (opcional)") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = if (
                estado.latitud != null &&
                estado.longitud != null
            ) {
                "Ubicación: ${estado.latitud}, ${estado.longitud}"
            } else {
                "Sin referencia geográfica"
            }
        )

        Button(
            onClick = {
                val tienePermiso =
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED

                if (tienePermiso) {
                    leerUltimaUbicacion(context, viewModel)
                } else {
                    lanzadorPermisos.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Obtener ubicación de prueba")
        }

        estado.errorGeneral?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = viewModel::guardar,
            enabled = !estado.guardando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (estado.guardando) {
                    "Guardando..."
                } else {
                    "Guardar oportunidad"
                }
            )
        }

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar y volver")
        }
    }
}

@Composable
private fun Selector(
    etiqueta: String,
    valor: String,
    opciones: List<String>,
    error: String?,
    alSeleccionar: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Button(
            onClick = { expandido = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (valor.isBlank()) etiqueta else "$etiqueta: $valor"
            )
        }

        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        alSeleccionar(opcion)
                        expandido = false
                    }
                )
            }
        }

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@SuppressLint("MissingPermission")
private fun leerUltimaUbicacion(
    context: Context,
    viewModel: OportunidadViewModel
) {
    val cliente =
        LocationServices.getFusedLocationProviderClient(context)

    cliente.lastLocation
        .addOnSuccessListener { ubicacion ->
            if (ubicacion != null) {
                viewModel.actualizarUbicacion(
                    latitud = ubicacion.latitude,
                    longitud = ubicacion.longitude
                )
            } else {
                viewModel.mostrarError(
                    "No hay una ubicación disponible. " +
                            "Configura una ubicación de prueba en el emulador."
                )
            }
        }
        .addOnFailureListener {
            viewModel.mostrarError(
                "No fue posible obtener la ubicación del dispositivo."
            )
        }
}
