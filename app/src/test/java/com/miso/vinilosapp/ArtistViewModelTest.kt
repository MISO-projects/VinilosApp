package com.miso.vinilosapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.models.Artist
import com.miso.vinilosapp.data.repositories.ArtistRepository
import com.miso.vinilosapp.ui.viewmodels.ArtistViewModel
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
class ArtistViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var artistRepositoryMock: ArtistRepository

    private lateinit var artistViewModel: ArtistViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val application = mock(Application::class.java)

        artistViewModel = ArtistViewModel(application, artistRepositoryMock)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cuando el repositorio devuelve datos, artists LiveData se actualiza`() = runTest {
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
            )
        )

        val artistList = listOf(
            Artist(
                artistId = 100,
                name = "John Doe",
                image = "https://example.com/john_doe.jpg",
                description = "A talented artist known for blending genres. Has a unique sound that mixes jazz and pop.",
                birthDate = "1990-05-01",
                albums = albumList
            ),
            Artist(
                artistId = 101,
                name = "Jane Smith",
                image = "https://example.com/jane_smith.jpg",
                description = "A groundbreaking musician in the indie rock scene with several chart-topping hits.",
                birthDate = "1985-11-22",
                albums = emptyList()
            )
        )

        `when`(artistRepositoryMock.getArtists()).thenReturn(artistList)

        val observer = mock(Observer::class.java) as Observer<List<Artist>>
        artistViewModel.artists.observeForever(observer)

        artistViewModel.refreshDataFromRepository()

        testDispatcher.scheduler.advanceUntilIdle()

        verify(observer).onChanged(artistList)
        Assert.assertEquals(artistList, artistViewModel.artists.value)

        artistViewModel.artists.removeObserver(observer)
    }

    @Test
    fun `cuando el repositorio lanza una excepción, eventNetworkError LiveData es true`() =
        runTest {
            `when`(artistRepositoryMock.getArtists()).thenThrow(RuntimeException("Network Error"))

            val observer = mock(Observer::class.java) as Observer<Boolean>
            artistViewModel.eventNetworkError.observeForever(observer)

            artistViewModel.refreshDataFromRepository()

            testDispatcher.scheduler.advanceUntilIdle()

            verify(observer).onChanged(true)
            Assert.assertTrue(artistViewModel.eventNetworkError.value == true)

            artistViewModel.eventNetworkError.removeObserver(observer)
        }
}
