package com.miso.vinilosapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.models.Collector
import com.miso.vinilosapp.data.models.Song
import com.miso.vinilosapp.data.repositories.CollectorRepository
import com.miso.vinilosapp.ui.viewmodels.CollectorDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CollectorDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var collectorRepository: CollectorRepository

    private lateinit var collectorDetailViewModel: CollectorDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val application = mock(Application::class.java)

        collectorDetailViewModel = CollectorDetailViewModel(application, collectorRepository, 100)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test successful collector data load`() = runTest {
        val mockCollector = Collector(
            collectorId = 100,
            name = "Juan Vargas",
            telephone = "3203362420",
            email = "js.vargasq1@uniandes.edu.co",
            collectorAlbums = listOf(
                Album(
                    albumId = 100,
                    name = "Buscando América",
                    cover = "https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg",
                    releaseDate = "1984-08-01T00:00:00.000Z",
                    description = "Buscando América es el primer álbum de la banda de Rubén Blades y Seis del Solar lanzado en 1984. La producción, bajo el sello Elektra, fusiona diferentes ritmos musicales tales como la salsa, reggae, rock, y el jazz latino. El disco fue grabado en Eurosound Studios en Nueva York entre mayo y agosto de 1983.",
                    genre = "Salsa",
                    recordLabel = "Elektra",
                    tracks = listOf(Song(songId = 1, name = "Buscando América", duration = "4:00"), Song(songId = 2, name = "América", duration = "3:22"))
                )
            )
        )

        `when`(collectorRepository.getCollectorById(collectorDetailViewModel.id)).thenReturn(mockCollector)

        val observer = mock(Observer::class.java) as Observer<Collector>
        collectorDetailViewModel.collector.observeForever(observer)

        collectorDetailViewModel.refreshDataFromNetwork()

        testDispatcher.scheduler.advanceUntilIdle()

        verify(observer).onChanged(mockCollector)
        Assert.assertEquals(mockCollector, collectorDetailViewModel.collector.value)

        collectorDetailViewModel.collector.removeObserver(observer)
    }

    @Test
    fun `test network error`() = runTest {
        `when`(collectorRepository.getCollectorById(collectorDetailViewModel.id)).thenThrow(RuntimeException("Network error"))

        val observer = mock(Observer::class.java) as Observer<Boolean>
        collectorDetailViewModel.eventNetworkError.observeForever(observer)

        collectorDetailViewModel.refreshDataFromNetwork()

        testDispatcher.scheduler.advanceUntilIdle()

        verify(observer).onChanged(true)
        Assert.assertTrue(collectorDetailViewModel.eventNetworkError.value == true)

        collectorDetailViewModel.eventNetworkError.removeObserver(observer)
    }

    @Test
    fun `test collector not found`() = runTest {
        `when`(collectorRepository.getCollectorById(collectorDetailViewModel.id)).thenThrow(RuntimeException("HTTP 404 Not Found"))

        val observer = mock(Observer::class.java) as Observer<Boolean>
        collectorDetailViewModel.eventNetworkError.observeForever(observer)

        collectorDetailViewModel.refreshDataFromNetwork()

        testDispatcher.scheduler.advanceUntilIdle()

        verify(observer).onChanged(true)
        Assert.assertTrue(collectorDetailViewModel.eventNetworkError.value == true)

        collectorDetailViewModel.eventNetworkError.removeObserver(observer)
    }
}
