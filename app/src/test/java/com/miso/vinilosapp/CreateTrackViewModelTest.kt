package com.miso.vinilosapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.models.Song
import com.miso.vinilosapp.data.repositories.AlbumRepository
import com.miso.vinilosapp.data.repositories.SongRepository
import com.miso.vinilosapp.ui.viewmodels.CreateTrackViewModel
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
class CreateTrackViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var albumRepositoryMock: AlbumRepository

    @Mock
    private lateinit var songRepositoryMock: SongRepository

    private lateinit var createTrackViewModel: CreateTrackViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val application = mock(Application::class.java)

        createTrackViewModel = CreateTrackViewModel(application, albumRepositoryMock, songRepositoryMock)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test successful album data load`() = runTest {
        val albumList = listOf(
            Album(
                albumId = 100,
                name = "Buscando América",
                cover = "https://example.com/album100.jpg",
                releaseDate = "1984-08-01T00:00:00.000Z",
                description = "Descripción del álbum 100",
                genre = "Salsa",
                recordLabel = "Elektra",
                tracks = emptyList()
            ),
            Album(
                albumId = 101,
                name = "Poeta del pueblo",
                cover = "https://example.com/album101.jpg",
                releaseDate = "1984-08-01T00:00:00.000Z",
                description = "Descripción del álbum 101",
                genre = "Salsa",
                recordLabel = "Elektra",
                tracks = emptyList()
            )
        )

        `when`(albumRepositoryMock.getAlbums()).thenReturn(albumList)

        val observer = mock(Observer::class.java) as Observer<List<Album>>
        createTrackViewModel.albums.observeForever(observer)

        createTrackViewModel.refreshDataFromRepository()

        testDispatcher.scheduler.advanceUntilIdle()

        verify(observer).onChanged(albumList)
        Assert.assertEquals(albumList, createTrackViewModel.albums.value)

        createTrackViewModel.albums.removeObserver(observer)
    }

    @Test
    fun `test network error when loading albums`() = runTest {
        `when`(albumRepositoryMock.getAlbums()).thenThrow(RuntimeException("Network Error"))

        val observer = mock(Observer::class.java) as Observer<Boolean>
        createTrackViewModel.eventNetworkError.observeForever(observer)

        createTrackViewModel.refreshDataFromRepository()

        testDispatcher.scheduler.advanceUntilIdle()

        verify(observer).onChanged(true)
        Assert.assertTrue(createTrackViewModel.eventNetworkError.value == true)

        createTrackViewModel.eventNetworkError.removeObserver(observer)
    }

    @Test
    fun `test successful song addition`() = runTest {
        val albumId = 100
        val songName = "Nueva Canción"
        val songDuration = "3:45"
        val newSong = Song(
            songId = 1,
            name = songName,
            duration = songDuration,
            albumId = albumId
        )

        `when`(songRepositoryMock.addSongToAlbum(albumId, songName, songDuration)).thenReturn(newSong)

        val observer = mock(Observer::class.java) as Observer<Song?>
        createTrackViewModel.newSong.observeForever(observer)

        createTrackViewModel.addSongToAlbum(albumId, songName, songDuration)

        testDispatcher.scheduler.advanceUntilIdle()

        verify(observer).onChanged(newSong)
        Assert.assertEquals(newSong, createTrackViewModel.newSong.value)

        createTrackViewModel.newSong.removeObserver(observer)
    }

    @Test
    fun `test network error when adding song`() = runTest {
        val albumId = 100
        val songName = "Nueva Canción"
        val songDuration = "3:45"

        `when`(songRepositoryMock.addSongToAlbum(albumId, songName, songDuration))
            .thenThrow(RuntimeException("Network Error"))

        val observer = mock(Observer::class.java) as Observer<Boolean>
        createTrackViewModel.eventNetworkError.observeForever(observer)

        createTrackViewModel.addSongToAlbum(albumId, songName, songDuration)

        testDispatcher.scheduler.advanceUntilIdle()

        verify(observer).onChanged(true)
        Assert.assertTrue(createTrackViewModel.eventNetworkError.value == true)

        createTrackViewModel.eventNetworkError.removeObserver(observer)
    }
}
