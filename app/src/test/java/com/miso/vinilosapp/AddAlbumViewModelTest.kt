package com.miso.vinilosapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.models.AlbumRequest
import com.miso.vinilosapp.data.repositories.AlbumRepository
import com.miso.vinilosapp.ui.viewmodels.AddAlbumViewModel
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
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddAlbumViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var albumRepositoryMock: AlbumRepository

    private lateinit var addAlbumViewModel: AddAlbumViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val application = mock(Application::class.java)

        addAlbumViewModel = AddAlbumViewModel(application, albumRepositoryMock)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addAlbum should call addAlbum from repository`() = runTest {
        val albumId = 1
        val name = "Album Test"
        val cover = "https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg"
        val releaseDate = "2020-12-12"
        val description = "Album Description"
        val genre = "Salsa"
        val recordLabel = "Sony Music"
        val albumToCreate = AlbumRequest(
            name,
            cover,
            releaseDate,
            description,
            genre,
            recordLabel
        )
        val newAlbum = Album(
            albumId,
            name,
            cover,
            releaseDate,
            description,
            genre,
            recordLabel,
            emptyList()
        )

        `when`(albumRepositoryMock.addAlbum(albumToCreate)).thenReturn(newAlbum)

        val albumObserver = mock(Observer::class.java) as Observer<Album?>
        val albumCreatedObserver = mock(Observer::class.java) as Observer<Boolean>

        addAlbumViewModel.album.observeForever(albumObserver)
        addAlbumViewModel.albumCreated.observeForever(albumCreatedObserver)

        addAlbumViewModel.addAlbum(albumToCreate)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(albumObserver).onChanged(newAlbum)
        verify(albumCreatedObserver).onChanged(true)

        Assert.assertEquals(newAlbum, addAlbumViewModel.album.value)
        addAlbumViewModel.album.removeObserver(albumObserver)
        addAlbumViewModel.albumCreated.removeObserver(albumCreatedObserver)
    }

    @Test
    fun `addAlbum should call addAlbum from repository and return null when it fails`() = runTest {
        val name = "Album Test"
        val cover = "https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg"
        val releaseDate = "2020-12-12"
        val description = "Album Description"
        val genre = "Salsa"
        val recordLabel = "Sony Music"
        val albumToCreate = AlbumRequest(
            name,
            cover,
            releaseDate,
            description,
            genre,
            recordLabel
        )

        `when`(albumRepositoryMock.addAlbum(albumToCreate))
            .thenThrow(RuntimeException("Network error"))

        val albumCreatedObserver = mock(Observer::class.java) as Observer<Boolean>
        val networkErrorObserver = mock(Observer::class.java) as Observer<Boolean>

        addAlbumViewModel.albumCreated.observeForever(albumCreatedObserver)
        addAlbumViewModel.eventNetworkError.observeForever(networkErrorObserver)

        addAlbumViewModel.addAlbum(albumToCreate)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(albumCreatedObserver).onChanged(false)
        verify(networkErrorObserver).onChanged(true)

        Assert.assertEquals(null, addAlbumViewModel.album.value)
        addAlbumViewModel.albumCreated.removeObserver(albumCreatedObserver)
        addAlbumViewModel.eventNetworkError.removeObserver(networkErrorObserver)
    }
}
