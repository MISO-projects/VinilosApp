package com.miso.vinilosapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.repositories.AlbumRepository
import com.miso.vinilosapp.viewmodels.AlbumDetailViewModel
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
class AlbumDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var albumRepository: AlbumRepository

    private lateinit var albumDetailViewModel: AlbumDetailViewModel

    @Before
    fun setUp() {

        Dispatchers.setMain(testDispatcher)

        val application = mock(Application::class.java)

        albumDetailViewModel = AlbumDetailViewModel(application, albumRepository, 100)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test successful album data load`() = runTest {
        val mockAlbum = Album(
            albumId = 100,
            name = "Buscando América",
            cover = "https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg",
            releaseDate = "1984-08-01T00:00:00.000Z",
            description = "Buscando América es el primer álbum de la banda de Rubén Blades y Seis del Solar lanzado en 1984. La producción, bajo el sello Elektra, fusiona diferentes ritmos musicales tales como la salsa, reggae, rock, y el jazz latino. El disco fue grabado en Eurosound Studios en Nueva York entre mayo y agosto de 1983.",
            genre = "Salsa",
            recordLabel = "Elektra",
            tracks = emptyList()
        )

        `when`(albumRepository.getAlbumById(albumDetailViewModel.id)).thenReturn(mockAlbum)

        val observer = mock(Observer::class.java) as Observer<Album>
        albumDetailViewModel.album.observeForever(observer)

        albumDetailViewModel.refreshDataFromNetwork()

        testDispatcher.scheduler.advanceUntilIdle()

        verify(observer).onChanged(mockAlbum)
        Assert.assertEquals(mockAlbum, albumDetailViewModel.album.value)

        albumDetailViewModel.album.removeObserver(observer)
    }

    @Test
    fun `test network error`() = runTest {
        `when`(albumRepository.getAlbumById(albumDetailViewModel.id)).thenThrow(RuntimeException("Network error"))

        val observer = mock(Observer::class.java) as Observer<Boolean>
        albumDetailViewModel.eventNetworkError.observeForever(observer)

        albumDetailViewModel.refreshDataFromNetwork()

        testDispatcher.scheduler.advanceUntilIdle()

        verify(observer).onChanged(true)
        Assert.assertTrue(albumDetailViewModel.eventNetworkError.value == true)

        albumDetailViewModel.eventNetworkError.removeObserver(observer)
    }

    @Test
    fun `test album not found`() = runTest {
        `when`(albumRepository.getAlbumById(albumDetailViewModel.id)).thenThrow(RuntimeException("HTTP 404 Not Found"))

        val observer = mock(Observer::class.java) as Observer<Boolean>
        albumDetailViewModel.eventNetworkError.observeForever(observer)

        albumDetailViewModel.refreshDataFromNetwork()

        testDispatcher.scheduler.advanceUntilIdle()

        verify(observer).onChanged(true)
        Assert.assertTrue(albumDetailViewModel.eventNetworkError.value == true)

        albumDetailViewModel.eventNetworkError.removeObserver(observer)
    }
}