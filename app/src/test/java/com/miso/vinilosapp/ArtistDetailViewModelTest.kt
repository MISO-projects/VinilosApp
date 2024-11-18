package com.miso.vinilosapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.miso.vinilosapp.data.models.Artist
import com.miso.vinilosapp.data.repositories.ArtistRepository
import com.miso.vinilosapp.ui.viewmodels.ArtistDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
class ArtistDetailViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var artistRepository: ArtistRepository

    private lateinit var artistDetailViewModel: ArtistDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val application = mock(Application::class.java)
        artistDetailViewModel = ArtistDetailViewModel(application, artistRepository, 100)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test successful artist data load`() = runTest {
        val mockArtist = Artist(
            artistId = 100,
            name = "Joan Manuel Serrat Teresa",
            image = "https://upload.wikimedia.org/wikipedia/commons/e/e3/Serrat.jpg",
            description = "Es un cantautor, compositor, actor, escritor, poeta y músico español.",
            birthDate = "1943-12-27T00:00:00.000Z",
            albums = emptyList()
        )

        `when`(artistRepository.getArtistById(100)).thenReturn(mockArtist)

        val observer = mock<Observer<Artist>>()
        artistDetailViewModel.artist.observeForever(observer)

        artistDetailViewModel.refreshDataFromRepository()

        advanceUntilIdle()

        verify(observer).onChanged(mockArtist)

        Assert.assertEquals(mockArtist, artistDetailViewModel.artist.value)

        artistDetailViewModel.artist.removeObserver(observer)
    }
}
