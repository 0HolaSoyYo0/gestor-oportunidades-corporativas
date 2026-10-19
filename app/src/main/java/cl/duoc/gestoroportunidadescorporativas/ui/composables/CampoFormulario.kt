package cl.duoc.gestoroportunidadescorporativas.ui.composables

data class CampoFormulario(
    val nombre: String,
    val etiqueta: String,
    val placeholder: String = "",
    val obligatorio: Boolean = true
)

val campos = listOf(
    CampoFormulario(
        nombre = "nombre",
        etiqueta = "Nombre",
        placeholder = "Ingrese el nombre de la oportunidad",
        obligatorio = true
    ),
    CampoFormulario(
        nombre = "empresa",
        etiqueta = "Empresa",
        placeholder = "Ingrese el nombre de la empresa"
    )
)