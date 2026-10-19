package cl.duoc.gestoroportunidadescorporativas.ui.navegacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.gestoroportunidadescorporativas.data.repository.OportunidadRepository
import cl.duoc.gestoroportunidadescorporativas.ui.oportunidad.ListaOportunidadesScreen
import cl.duoc.gestoroportunidadescorporativas.ui.oportunidad.OportunidadFormScreen
import cl.duoc.gestoroportunidadescorporativas.ui.oportunidad.OportunidadViewModel
import cl.duoc.gestoroportunidadescorporativas.ui.oportunidad.OportunidadViewModelFactory

@Composable
fun AppNavigation(
    repository: OportunidadRepository,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val factory = remember(repository) {
        OportunidadViewModelFactory(repository)
    }

    NavHost(
        navController = navController,
        startDestination = "inicio",
        modifier = modifier
    ) {
        composable("inicio") {
            PantallaSimple(
                titulo = "Gestor de Oportunidades",
                descripcion = "Bienvenido a la aplicación",
                textoBoton = "Continuar"
            ) {
                navController.navigate("menu")
            }
        }

        composable("menu") {
            PantallaSimple(
                titulo = "Menú principal",
                descripcion = "Selecciona el módulo que deseas utilizar",
                textoBoton = "Gestionar oportunidades"
            ) {
                navController.navigate("lista")
            }
        }

        composable("lista") {
            val viewModel: OportunidadViewModel =
                viewModel(factory = factory)

            ListaOportunidadesScreen(
                viewModel = viewModel,
                onAgregar = {
                    navController.navigate("formulario/0")
                },
                onEditar = { id ->
                    navController.navigate("formulario/$id")
                }
            )
        }

        composable(
            route = "formulario/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { entrada ->
            val id = entrada.arguments?.getLong("id") ?: 0L

            val viewModel: OportunidadViewModel =
                viewModel(factory = factory)

            LaunchedEffect(id) {
                viewModel.prepararFormulario(
                    id.takeIf { it != 0L }
                )
            }

            OportunidadFormScreen(
                viewModel = viewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onGuardado = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
private fun PantallaSimple(
    titulo: String,
    descripcion: String,
    textoBoton: String,
    alContinuar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(titulo)
        Text(
            text = descripcion,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Button(onClick = alContinuar) {
            Text(textoBoton)
        }
    }
}
