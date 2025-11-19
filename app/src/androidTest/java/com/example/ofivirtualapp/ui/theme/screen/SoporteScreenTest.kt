package com.example.ofivirtualapp.ui.theme.screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.ofivirtualapp.data.remote.ticket.TicketResponse
import com.example.ofivirtualapp.ui.theme.OfiVirtualV3Theme // ⬅️ AJUSTA si tu tema se llama distinto
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class SoporteScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun soporteScreen_muestraTituloYListadoDeTickets() {

        // -------- ARRANGE --------
        val fakeTickets = listOf(
            TicketResponse(
                id = 1,
                userId = 1,
                empresaId = null,
                asunto = "Error de acceso",
                descripcion = "No puedo iniciar sesión",
                estado = "ABIERTO",
                fechaCreacion = "2025-11-19"
            ),
            TicketResponse(
                id = 2,
                userId = 1,
                empresaId = null,
                asunto = "Problema de pago",
                descripcion = "No se procesa el pago",
                estado = "CERRADO",
                fechaCreacion = "2025-11-19"
            )
        )

        composeRule.setContent {
            OfiVirtualV3Theme {
                SoporteScreen(
                    isLoading = false,
                    tickets = fakeTickets,
                    errorMsg = null,
                    successMsg = null,
                    onCreateTicket = { _, _ -> },
                    onDeleteTicket = {},
                    onUpdateTicket = { _, _, _ -> },
                    onClearMessages = {},
                    onNavigateBack = {},
                    onGoToFaq = {}
                )
            }
        }

        // -------- ASSERT --------

        // 1. El título principal de tu pantalla
        composeRule.onNodeWithText("Soporte y Tickets")
            .assertIsDisplayed()

        // 2. El encabezado "Mis tickets"
        composeRule.onNodeWithText("Mis tickets")
            .assertIsDisplayed()

        // 3. Ticket 1
        composeRule.onNodeWithText("Error de acceso")
            .assertIsDisplayed()

        // 4. Ticket 2
        composeRule.onNodeWithText("Problema de pago")
            .assertIsDisplayed()
    }
}
