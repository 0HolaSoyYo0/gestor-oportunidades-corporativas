package cl.duoc.gestoroportunidadescorporativas.ui.oportunidad

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ListaOportunidadesScreen(
    viewModel: OportunidadViewModel,
    onAgregar: () -> Unit,
    onEditar: (Long) -> Unit
) {
    val estado by viewModel.listaUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Oportunidades", style = MaterialTheme.typography.headlineMedium)

        Button(onClick = onAgregar, modifier = Modifier.fillMaxWidth()) {
            Text("Agregar oportunidades")
        }

        if (estado.oportunidades.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("No hay oportunidades registradas.")
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(estado.oportunidades, key = { it.id }) { oportunidad ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEditar(oportunidad.id)
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = oportunidad.nombre, style = MaterialTheme.typography.titleMedium)

                            Text("Tipo: ${oportunidad.tipo}")
                            Text("Prioridad: ${oportunidad.prioridad}")
                            Text("Estado: ${oportunidad.estado}")
                            Text("Potencial: ${oportunidad.potencial}")

                            Text(text = "Tocar para editar", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}