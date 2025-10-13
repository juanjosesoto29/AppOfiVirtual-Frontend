package com.example.ofivirtualapp.ui.theme.screen

// --- IMPORTS NECESARIOS ---
import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.ofivirtualapp.viewmodel.PerfilUiState
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/* ================== PANTALLA PRINCIPAL ================== */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    uiState: PerfilUiState, // Recibimos el estado completo del ViewModel
    onAvatarChange: (Uri?) -> Unit,
    onEditarPerfil: () -> Unit = {},
    onVerMisDatos: () -> Unit = {},
    onVerContratos: () -> Unit = {},
    onMetodosPago: () -> Unit = {},
    onNotificaciones: () -> Unit = {},
    onAyuda: () -> Unit = {},
    onRenovarPlan: () -> Unit = {},
    onCerrarSesion: () -> Unit = {}
) {
    val OfiBlue = Color(0xFF071290)
    val BadgeGreen = Color(0xFF34C759)
    val context = LocalContext.current

    // --- ESTADO Y LANZADORES PARA SELECCIÓN DE IMAGEN ---

    // 1. Estado para mostrar el menú de selección (BottomSheet)
    var showSheet by remember { mutableStateOf(false) }

    // 2. URI temporal para la foto tomada con la cámara
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // 3. Lanzador para obtener una imagen de la galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                onAvatarChange(uri)
                showSheet = false
            }
        }
    )

    // 4. Lanzador para tomar una foto con la cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onAvatarChange(tempCameraUri)
                showSheet = false
            }
        }
    )

    // 5. Lanzadores para los permisos de Cámara y Galería
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                tempCameraUri = context.createImageUri() // Crear URI antes de lanzar la cámara
                cameraLauncher.launch(tempCameraUri)
            }
        }
    )

    // La gestión del permiso de galería es automática en versiones recientes de Android
    // al usar GetContent, pero lo dejamos explícito por buenas prácticas.
    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                galleryLauncher.launch("image/*")
            }
        }
    )

    // --- UI ---

    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            Column(modifier = Modifier.padding(bottom = 32.dp)) {
                ListTile(
                    icon = Icons.Outlined.PhotoCamera,
                    text = "Tomar foto",
                    onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }
                )
                ListTile(
                    icon = Icons.Outlined.PhotoLibrary,
                    text = "Elegir de la galería",
                    onClick = {
                        // En Android 13+ el permiso es más granular. Por simplicidad, GetContent es suficiente.
                        galleryLauncher.launch("image/*")
                    }
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil", fontWeight = FontWeight.SemiBold) },
                actions = {
                    IconButton(onClick = onEditarPerfil) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Editar perfil")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /* --- Header: Avatar + datos --- */
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // --- AVATAR INTERACTIVO ---
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(OfiBlue)
                            .clickable { showSheet = true }, // ¡HACE EL AVATAR CLICABLE!
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.avatarUri != null) {
                            // Si hay un URI, lo cargamos con Coil
                            AsyncImage(
                                model = uiState.avatarUri,
                                contentDescription = "Avatar de perfil",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop // Asegura que la imagen llene el círculo
                            )
                        } else {
                            // Si no, mostramos las iniciales
                            val initials = remember(uiState.user.nombre) { uiState.user.nombre.toInitials() }
                            Text(
                                text = initials,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(Modifier.weight(1f)) {
                        Text(uiState.user.nombre, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(uiState.user.email, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (uiState.user.telefono.isNotBlank()) {
                            Text(uiState.user.telefono, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            // ... El resto del código de la pantalla no necesita cambios ...
            // (Plan actual, Opciones, Cerrar sesión, etc.)

            Spacer(Modifier.height(16.dp))
            /* --- Plan actual --- */
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.weight(1f)) {
                            Text("Mi Plan", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(uiState.user.planNombre, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                            val vence = if (uiState.user.planVence.isNotBlank()) "Vence ${uiState.user.planVence}" else ""
                            if (vence.isNotBlank()) {
                                Text(vence, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        if (uiState.user.planEstadoVigente) {
                            StatusChip(label = "VIGENTE", bg = BadgeGreen, fg = Color.White)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = onRenovarPlan, shape = RoundedCornerShape(24.dp)) {
                            Text("Renovar")
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            /* --- Opciones --- */
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                PerfilItem( icon = Icons.Outlined.Person, title = "Mis datos", subtitle = "Nombre, email y teléfono", onClick = onVerMisDatos)
                PerfilItem( icon = Icons.Outlined.Description, title = "Contratos", subtitle = "Documentos y estados", onClick = onVerContratos)
                PerfilItem( icon = Icons.Outlined.CreditCard, title = "Métodos de pago", subtitle = "Tarjetas y medios guardados", onClick = onMetodosPago)
                PerfilItem( icon = Icons.Outlined.NotificationsNone, title = "Notificaciones", subtitle = "Preferencias y alertas", onClick = onNotificaciones)
                PerfilItem( icon = Icons.Outlined.HelpOutline, title = "Ayuda", subtitle = "Centro de soporte", onClick = onAyuda)
            }
            Spacer(Modifier.height(16.dp))

            /* --- Cerrar sesión (zona de peligro) --- */
            OutlinedButton(
                onClick = onCerrarSesion,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Cerrar sesión", fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.weight(1f))

            Text(
                "OfiVirtual App • v1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}


/* ================== SUBCOMPONENTES Y UTILS ================== */

// Un Composable simple para los ítems del BottomSheet
@Composable
private fun ListTile(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(16.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

// Función para crear un URI para la cámara
private fun Context.createImageUri(): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFile = File(filesDir, "JPEG_${timeStamp}_.jpg")
    return FileProvider.getUriForFile(
        this,
        "${applicationContext.packageName}.provider",
        imageFile
    )
}


// --- Componentes que ya tenías (sin cambios) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PerfilItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun StatusChip(label: String, bg: Color, fg: Color) {
    Box(modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(bg).padding(horizontal = 10.dp, vertical = 6.dp), contentAlignment = Alignment.Center) {
        Text(label, color = fg, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

data class UserProfile(val nombre: String, val email: String, val telefono: String = "", val planNombre: String = "", val planEstadoVigente: Boolean = false, val planVence: String = "")

private fun String.toInitials(): String {
    val parts = trim().split(" ").filter { it.isNotBlank() }
    if (parts.isEmpty()) return "?"
    val first = parts.first().firstOrNull()?.uppercaseChar() ?: ' '
    val last = if (parts.size > 1) parts.last().firstOrNull()?.uppercaseChar() ?: ' ' else ' '
    return if (parts.size > 1) "$first$last" else "$first"
}
