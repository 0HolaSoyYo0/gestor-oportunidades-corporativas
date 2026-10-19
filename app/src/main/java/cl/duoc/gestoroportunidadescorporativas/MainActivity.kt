
package cl.duoc.gestoroportunidadescorporativas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import cl.duoc.gestoroportunidadescorporativas.ui.navegacion.AppNavigation
import cl.duoc.gestoroportunidadescorporativas.ui.theme.GestorOportunidadesCorporativasTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.enableEdgeToEdge(window)

        val container =
            (application as OportunidadesApplication).container

        setContent {
            GestorOportunidadesCorporativasTheme {
                AppNavigation(
                    repository = container.oportunidadRepository
                )
            }
        }
    }
}
