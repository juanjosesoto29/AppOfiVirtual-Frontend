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
import com.example.ofivirtualapp.data.local.storage.UserPreferences

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
        // 1. Crear los datos falsos (esto ya lo tienes)
        val fakeTickets = listOf(
            TicketResponse(1, 1, null, "Asunto 1", "Desc 1", "ABIERTO", "2025-11-19"),
            TicketResponse(2, 1, null, "Asunto 2", "Desc 2", "CERRADO", "2025-11-19")
        )

        // 2. Mockear el Repositorio (esto ya lo tienes)
        val repo = mockk<TicketRepository>()
        coEvery { repo.getAll() } returns Response.success(fakeTickets)

        // 3.  Mockear UserPreferences (el error era que faltaba esto)
        val userPrefs = mockk<UserPreferences>(relaxed = true)
        // "relaxed = true" hace que no tengas que configurar cada llamada,
        // simplemente devolverá valores por defecto si se llama a algo.

        // 4. Pasar AMBOS mocks al constructor
        val viewModel = TicketViewModel(
            userPreferences = userPrefs,
            repo = repo
        )

        // -------- ACT --------
        viewModel.loadTickets()
        advanceUntilIdle()

        // -------- ASSERT (esto sigue igual) --------
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.tickets.size)
        assertNull(state.errorMsg)
    }
    }
