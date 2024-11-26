package com.miso.vinilosapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.repositories.AlbumRepository
import com.miso.vinilosapp.ui.viewmodels.AlbumViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class AlbumViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var albumRepositoryMock: AlbumRepository

    private lateinit var albumViewModel: AlbumViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val application = mock(Application::class.java)

        albumViewModel = AlbumViewModel(application, albumRepositoryMock)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cuando el repositorio devuelve datos, albums LiveData se actualiza`() = runTest {
        val albumList = listOf(
            Album(
                albumId = 100,
                name = "Buscando América",
                cover = "https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg",
                releaseDate = "1984-08-01T00:00:00.000Z",
                description = "Buscando América es el primer álbum de la banda de Rubén Blades y Seis del Solar lanzado en 1984. La producción, bajo el sello Elektra, fusiona diferentes ritmos musicales tales como la salsa, reggae, rock, y el jazz latino. El disco fue grabado en Eurosound Studios en Nueva York entre mayo y agosto de 1983.",
                genre = "Salsa",
                recordLabel = "Elektra",
                tracks = emptyList()
            ),
            Album(
                albumId = 101,
                name = "Poeta del pueblo",
                cover = "https://cdn.shopify.com/s/files/1/0275/3095/products/image_4931268b-7acf-4702-9c55-b2b3a03ed999_1024x1024.jpg",
                releaseDate = "1984-08-01T00:00:00.000Z",
                description = "Recopilación de 27 composiciones del cosmos Blades que los bailadores y melómanos han hecho suyas en estos 40 años de presencia de los ritmos y concordias afrocaribeños en múltiples escenarios internacionales. Grabaciones de Blades para la Fania con las orquestas de Pete Rodríguez, Ray Barreto, Fania All Stars y, sobre todo, los grandes éxitos con la Banda de Willie Colón.",
                genre = "Salsa",
                recordLabel = "Elektra",
                tracks = emptyList()
            )
        )

        `when`(albumRepositoryMock.getAlbums()).thenReturn(flowOf(albumList))

        val observer = mock(Observer::class.java) as Observer<List<Album>>
        albumViewModel.albums.observeForever(observer)

        albumViewModel.refreshDataFromRepository()

        testDispatcher.scheduler.advanceUntilIdle()

        verify(observer).onChanged(albumList)
        Assert.assertEquals(albumList, albumViewModel.albums.value)

        albumViewModel.albums.removeObserver(observer)
    }

    @Test
    fun `cuando el repositorio lanza una excepción, eventNetworkError LiveData es true`() =
        runTest {
            `when`(albumRepositoryMock.getAlbums()).thenThrow(RuntimeException("Network Error"))

            val observer = mock(Observer::class.java) as Observer<Boolean>
            albumViewModel.eventNetworkError.observeForever(observer)

            albumViewModel.refreshDataFromRepository()

            testDispatcher.scheduler.advanceUntilIdle()

            verify(observer).onChanged(true)
            Assert.assertTrue(albumViewModel.eventNetworkError.value == true)

            albumViewModel.eventNetworkError.removeObserver(observer)
        }
}
