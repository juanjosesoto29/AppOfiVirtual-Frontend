package com.example.ofivirtualapp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ofivirtualapp.data.remote.empresa.EmpresaRequest
import com.example.ofivirtualapp.data.remote.empresa.EmpresaResponse
import com.example.ofivirtualapp.viewmodel.EmpresaViewModel
import com.example.ofivirtualapp.data.local.ChileGeoData


@Composable
fun EmpresaScreenVm(
    vm: EmpresaViewModel,
    onGoBack: () -> Unit
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.loadEmpresa()
    }

    EmpresaScreen(
        isLoading = state.isLoading,
        errorMsg = state.errorMsg,
        empresaData = EmpresaFormData.fromResponse(state.empresa),
        onSubmit = { formData ->
            val req = formData.toRequest()
            vm.saveEmpresa(req)
        },
        onGoBack = onGoBack
    )

    if (state.savedOk) {
        LaunchedEffect(Unit) {
            vm.clearSavedFlag()
            onGoBack()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmpresaScreen(
    isLoading: Boolean,
    errorMsg: String?,
    empresaData: EmpresaFormData,
    onSubmit: (EmpresaFormData) -> Unit,
    onGoBack: () -> Unit
) {
    var form by remember { mutableStateOf(empresaData) }

    // Errores visuales
    var rutError by remember { mutableStateOf<String?>(null) }
    var razonSocialError by remember { mutableStateOf<String?>(null) }
    var tipoContError by remember { mutableStateOf<String?>(null) }

    // Opciones para dropdowns
    val tipoEmpresaOptions = listOf("SPA", "EIRL", "SP", "PNCG", "LTDA", "SA")
    val tipoContOptions = listOf("OFIVIRTUAL", "EXTERNA", "SIN_CONTABILIDAD")

    var expandedTipoEmpresa by remember { mutableStateOf(false) }
    var expandedTipoCont by remember { mutableStateOf(false) }

    fun validarYEnviar() {
        rutError = if (form.rut.isBlank()) "El RUT es obligatorio" else null
        razonSocialError = if (form.razonSocial.isBlank()) "La razón social es obligatoria" else null
        tipoContError = if (form.tipoContabilidad.isBlank()) "Selecciona un tipo de contabilidad" else null

        if (rutError == null && razonSocialError == null && tipoContError == null) {
            onSubmit(form)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos de la Empresa") },
                navigationIcon = {
                    IconButton(onClick = onGoBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    // ========= DATOS BÁSICOS =========
                    Text("Datos básicos", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = form.rut,
                        onValueChange = {
                            form = form.copy(rut = it)
                            rutError = null
                        },
                        label = { Text("RUT Empresa") },
                        singleLine = true,
                        isError = rutError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (rutError != null) {
                        Text(
                            text = rutError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = form.razonSocial,
                        onValueChange = {
                            form = form.copy(razonSocial = it)
                            razonSocialError = null
                        },
                        label = { Text("Razón Social") },
                        singleLine = true,
                        isError = razonSocialError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (razonSocialError != null) {
                        Text(
                            text = razonSocialError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = form.nombreFantasia,
                        onValueChange = { form = form.copy(nombreFantasia = it) },
                        label = { Text("Nombre de fantasía (opcional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = form.giro,
                        onValueChange = { form = form.copy(giro = it) },
                        label = { Text("Giro") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    // Tipo de empresa
                    ExposedDropdownMenuBox(
                        expanded = expandedTipoEmpresa,
                        onExpandedChange = { expandedTipoEmpresa = !expandedTipoEmpresa }
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = form.tipoEmpresa,
                            onValueChange = {},
                            label = { Text("Tipo de empresa") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipoEmpresa)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedTipoEmpresa,
                            onDismissRequest = { expandedTipoEmpresa = false }
                        ) {
                            tipoEmpresaOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        form = form.copy(tipoEmpresa = option)
                                        expandedTipoEmpresa = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // ========= UBICACIÓN =========
                    Text("Ubicación", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))

// Dirección exacta (se mantiene igual)
                    OutlinedTextField(
                        value = form.direccion,
                        onValueChange = { form = form.copy(direccion = it) },
                        label = { Text("Dirección exacta (calle, número, depto)") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()

                    )

                    Spacer(Modifier.height(12.dp))

// --- Variables para desplegables ---
                    var expandedRegion by remember { mutableStateOf(false) }
                    var expandedCiudad by remember { mutableStateOf(false) }
                    var expandedComuna by remember { mutableStateOf(false) }

// --- Datos desde ChileGeoData ---
                    val regiones = ChileGeoData.regiones
                    val ciudades = ChileGeoData.estructura[form.region]?.keys?.toList() ?: emptyList()
                    val comunas = ChileGeoData.estructura[form.region]?.get(form.ciudad) ?: emptyList()

// --- Región ---
                    ExposedDropdownMenuBox(
                        expanded = expandedRegion,
                        onExpandedChange = { expandedRegion = !expandedRegion }
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = form.region,
                            onValueChange = {},
                            label = { Text("Región") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRegion)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedRegion,
                            onDismissRequest = { expandedRegion = false }
                        ) {
                            regiones.forEach { region ->
                                DropdownMenuItem(
                                    text = { Text(region) },
                                    onClick = {
                                        form = form.copy(region = region, ciudad = "", comuna = "")
                                        expandedRegion = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

// --- Ciudad / Provincia ---
                    ExposedDropdownMenuBox(
                        expanded = expandedCiudad,
                        onExpandedChange = { expandedCiudad = !expandedCiudad }
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = form.ciudad,
                            onValueChange = {},
                            label = { Text("Ciudad / Provincia") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCiudad)
                            },
                            enabled = ciudades.isNotEmpty(),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedCiudad,
                            onDismissRequest = { expandedCiudad = false }
                        ) {
                            ciudades.forEach { ciudad ->
                                DropdownMenuItem(
                                    text = { Text(ciudad) },
                                    onClick = {
                                        form = form.copy(ciudad = ciudad, comuna = "")
                                        expandedCiudad = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

// --- Comuna ---
                    ExposedDropdownMenuBox(
                        expanded = expandedComuna,
                        onExpandedChange = { expandedComuna = !expandedComuna }
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = form.comuna,
                            onValueChange = {},
                            label = { Text("Comuna") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedComuna)
                            },
                            enabled = comunas.isNotEmpty(),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedComuna,
                            onDismissRequest = { expandedComuna = false }
                        ) {
                            comunas.forEach { comuna ->
                                DropdownMenuItem(
                                    text = { Text(comuna) },
                                    onClick = {
                                        form = form.copy(comuna = comuna)
                                        expandedComuna = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

// --- País (simple) ---
                    OutlinedTextField(
                        value = form.pais.ifBlank { "Chile" },
                        onValueChange = { form = form.copy(pais = it) },
                        label = { Text("País") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))


                    // ========= CONTACTO EMPRESA =========
                    Text("Contacto de la empresa", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = form.telefonoEmpresa,
                        onValueChange = { form = form.copy(telefonoEmpresa = it) },
                        label = { Text("Teléfono empresa") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = form.emailEmpresa,
                        onValueChange = { form = form.copy(emailEmpresa = it) },
                        label = { Text("Correo empresa") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    // ========= CONTABILIDAD =========
                    Text("Contabilidad", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = form.regimenTributario,
                        onValueChange = { form = form.copy(regimenTributario = it) },
                        label = { Text("Régimen tributario (opcional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Contabilidad completa", modifier = Modifier.weight(1f))
                        Switch(
                            checked = form.contabilidadCompleta,
                            onCheckedChange = { form = form.copy(contabilidadCompleta = it) }
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // Tipo de contabilidad
                    ExposedDropdownMenuBox(
                        expanded = expandedTipoCont,
                        onExpandedChange = { expandedTipoCont = !expandedTipoCont }
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = form.tipoContabilidad,
                            onValueChange = {},
                            label = { Text("Tipo de contabilidad") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipoCont)
                            },
                            isError = tipoContError != null,
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedTipoCont,
                            onDismissRequest = { expandedTipoCont = false }
                        ) {
                            tipoContOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        form = form.copy(tipoContabilidad = option)
                                        tipoContError = null
                                        expandedTipoCont = false
                                    }
                                )
                            }
                        }
                    }
                    if (tipoContError != null) {
                        Text(
                            text = tipoContError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // --- Sección dinámica para contabilidad EXTERNA ---
                    if (form.tipoContabilidad == "EXTERNA") {
                        Spacer(Modifier.height(8.dp))
                        Text("Datos del estudio contable externo", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = form.contaExternaNombreEstudio,
                            onValueChange = { form = form.copy(contaExternaNombreEstudio = it) },
                            label = { Text("Nombre estudio contable") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = form.contaExternaContactoNombre,
                            onValueChange = { form = form.copy(contaExternaContactoNombre = it) },
                            label = { Text("Nombre contacto") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = form.contaExternaEmail,
                            onValueChange = { form = form.copy(contaExternaEmail = it) },
                            label = { Text("Correo estudio contable") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = form.contaExternaTelefono,
                            onValueChange = { form = form.copy(contaExternaTelefono = it) },
                            label = { Text("Teléfono estudio contable") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = form.contaExternaObservaciones,
                            onValueChange = { form = form.copy(contaExternaObservaciones = it) },
                            label = { Text("Observaciones") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 80.dp)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    if (errorMsg != null) {
                        Text(
                            text = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    Button(
                        onClick = { validarYEnviar() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar Empresa")
                    }

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

// --- Data del formulario ---
data class EmpresaFormData(
    val rut: String = "",
    val razonSocial: String = "",
    val nombreFantasia: String = "",
    val giro: String = "",
    val tipoEmpresa: String = "",
    val direccion: String = "",
    val comuna: String = "",
    val ciudad: String = "",
    val region: String = "",
    val pais: String = "",
    val telefonoEmpresa: String = "",
    val emailEmpresa: String = "",
    val regimenTributario: String = "",
    val contabilidadCompleta: Boolean = false,
    val tipoContabilidad: String = "OFIVIRTUAL",
    val contaExternaNombreEstudio: String = "",
    val contaExternaContactoNombre: String = "",
    val contaExternaEmail: String = "",
    val contaExternaTelefono: String = "",
    val contaExternaObservaciones: String = ""
) {
    fun toRequest(): EmpresaRequest {
        return EmpresaRequest(
            userId = 0L, // El ViewModel lo sobrescribe con el userId real
            rut = rut,
            razonSocial = razonSocial,
            nombreFantasia = nombreFantasia.ifBlank { null },
            giro = giro.ifBlank { null },
            fechaInicioActividades = null,
            tipoEmpresa = tipoEmpresa.ifBlank { null },
            direccion = direccion.ifBlank { null },
            comuna = comuna.ifBlank { null },
            ciudad = ciudad.ifBlank { null },
            region = region.ifBlank { null },
            pais = pais.ifBlank { null },
            telefonoEmpresa = telefonoEmpresa.ifBlank { null },
            emailEmpresa = emailEmpresa.ifBlank { null },
            sitioWeb = null,
            regimenTributario = regimenTributario.ifBlank { null },
            contabilidadCompleta = contabilidadCompleta,
            tipoContabilidad = tipoContabilidad,
            contaFechaInicioOfivirtual = null,
            contaFechaTerminoOfivirtual = null,
            contaEstado = null,
            contaExternaNombreEstudio = contaExternaNombreEstudio.ifBlank { null },
            contaExternaContactoNombre = contaExternaContactoNombre.ifBlank { null },
            contaExternaEmail = contaExternaEmail.ifBlank { null },
            contaExternaTelefono = contaExternaTelefono.ifBlank { null },
            contaExternaObservaciones = contaExternaObservaciones.ifBlank { null }
        )
    }

    companion object {
        fun fromResponse(resp: EmpresaResponse?): EmpresaFormData {
            return if (resp == null) EmpresaFormData()
            else EmpresaFormData(
                rut = resp.rut,
                razonSocial = resp.razonSocial,
                nombreFantasia = resp.nombreFantasia ?: "",
                giro = resp.giro ?: "",
                tipoEmpresa = resp.tipoEmpresa ?: "",
                direccion = resp.direccion ?: "",
                comuna = resp.comuna ?: "",
                ciudad = resp.ciudad ?: "",
                region = resp.region ?: "",
                pais = resp.pais ?: "",
                telefonoEmpresa = resp.telefonoEmpresa ?: "",
                emailEmpresa = resp.emailEmpresa ?: "",
                regimenTributario = resp.regimenTributario ?: "",
                contabilidadCompleta = resp.contabilidadCompleta ?: false,
                tipoContabilidad = resp.tipoContabilidad,
                contaExternaNombreEstudio = resp.contaExternaNombreEstudio ?: "",
                contaExternaContactoNombre = resp.contaExternaContactoNombre ?: "",
                contaExternaEmail = resp.contaExternaEmail ?: "",
                contaExternaTelefono = resp.contaExternaTelefono ?: "",
                contaExternaObservaciones = resp.contaExternaObservaciones ?: ""
            )
        }
    }
}