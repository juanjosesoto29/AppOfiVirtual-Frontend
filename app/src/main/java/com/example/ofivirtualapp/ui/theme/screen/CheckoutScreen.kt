package com.example.ofivirtualapp.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    totalAPagar: Int,
    onNavigateBack: () -> Unit,
    onPaymentSuccess: () -> Unit,
    onClearCart: () -> Unit
) {
    val OfiBlue = Color(0xFF071290)
    val context = LocalContext.current


    var isPaying by remember { mutableStateOf(false) }
    var paymentSuccess by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen de Pago", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    if (!isPaying) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                }
            )
        },
        bottomBar = {

            Surface(shadowElevation = 8.dp) {
                Column(Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            if (!paymentSuccess) {
                                isPaying = true
                            }
                        },
                        enabled = !isPaying,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OfiBlue)
                    ) {
                        if (isPaying) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Pagar ${totalAPagar.toCLP()}", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isPaying) {
                PaymentProcessing(
                    onSuccess = {
                        paymentSuccess = true
                        onClearCart()

                    },
                    onNavigateHome = onPaymentSuccess
                )
            } else {
                CheckoutContent(total = totalAPagar)
            }
        }
    }
}


@Composable
private fun CheckoutContent(total: Int) {
    Icon(
        imageVector = Icons.Outlined.ReceiptLong,
        contentDescription = null,
        modifier = Modifier.size(64.dp),
        tint = MaterialTheme.colorScheme.primary
    )
    Spacer(Modifier.height(16.dp))
    Text(
        "Total a Pagar",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
        total.toCLP(),
        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(Modifier.height(24.dp))

    // Simulación de selección de método de pago
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.CreditCard, null, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Método de pago", style = MaterialTheme.typography.bodySmall)
                Text("Webpay / Mercado Pago", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            }
        }
    }
    Spacer(Modifier.height(8.dp))
    Text(
        "Serás redirigido a una pasarela de pago segura para completar tu compra.",
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun PaymentProcessing(
    onSuccess: () -> Unit,
    onNavigateHome: () -> Unit
) {
    val context = LocalContext.current
    var message by remember { mutableStateOf("Procesando pago...") }
    var showSuccessIcon by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        delay(2500)
        message = "¡Pago exitoso!"
        showSuccessIcon = true
        onSuccess()
        Toast.makeText(context, "¡Gracias por tu compra!", Toast.LENGTH_LONG).show()
        delay(2000)
        onNavigateHome()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (showSuccessIcon) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Pago Exitoso",
                modifier = Modifier.size(80.dp),
                tint = Color(0xFF4CAF50) // Verde éxito
            )
        } else {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun Int.toCLP(): String {
    if (this == 0) return "$0"
    val s = this.toString()
    val sb = StringBuilder()
    var c = 0
    for (i in s.length - 1 downTo 0) {
        sb.append(s[i]); c++
        if (c == 3 && i != 0) { sb.append('.'); c = 0 }
    }
    return "$" + sb.reverse().toString()
}

