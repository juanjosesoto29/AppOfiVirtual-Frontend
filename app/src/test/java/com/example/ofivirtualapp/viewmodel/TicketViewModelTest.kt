package com.example.ofivirtualapp.viewmodel

import com.example.ofivirtualapp.data.remote.ticket.TicketResponse
import com.example.ofivirtualapp.data.repository.TicketRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class TicketViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Redirigimos Dispatchers.Main al dispatcher de pruebas
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadTickets actualiza uiState con lista y sin error`() = runTest {
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

        val repo = mockk<TicketRepository>()
        // Tu repo real: getAll() → Response<List<TicketResponse>>
        coEvery { repo.getAll() } returns Response.success(fakeTickets)

        val viewModel = TicketViewModel(repo)

        // -------- ACT --------
        viewModel.loadTickets()
        // Dejamos que terminen las corrutinas del viewModelScope
        advanceUntilIdle()

        val state = viewModel.uiState.value

        // -------- ASSERT --------
        assertFalse(state.isLoading)                 // ya no está cargando
        assertEquals(2, state.tickets.size)         // cargó los 2 tickets
        assertNull(state.errorMsg)                  // no hubo error
        // successMsg no se valida aquí (no es necesario en este caso)
    }
}
