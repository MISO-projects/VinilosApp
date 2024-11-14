package com.miso.vinilosapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.miso.vinilosapp.data.models.Collector
import com.miso.vinilosapp.data.repositories.CollectorRepository
import com.miso.vinilosapp.ui.viewmodels.CollectorViewModel
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
class CollectorViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var collectorRepositoryMock: CollectorRepository

    private lateinit var collectorViewModel: CollectorViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val application = mock(Application::class.java)

        collectorViewModel = CollectorViewModel(application, collectorRepositoryMock)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test successful collector data load`() = runTest {
        val collectorListMock = listOf(
            Collector(
                id = 100,
                name = "Manolo Bellon",
                telephone = "3502457896",
                email = "manollo@caracol.com.co",
                collectorAlbums = emptyList()
            ),
            Collector(
                id = 101,
                name = "Jaime Monsalve",
                telephone = "3012357936",
                email = "jmonsalve@rtvc.com.co",
                collectorAlbums = emptyList()
            )
        )

        `when`(collectorRepositoryMock.getCollectors()).thenReturn(collectorListMock)

        val observer = mock(Observer::class.java) as Observer<List<Collector>>
        collectorViewModel.collectors.observeForever(observer)

        collectorViewModel.refreshDataFromRepository()

        testDispatcher.scheduler.advanceUntilIdle()

        verify(observer).onChanged(collectorListMock)

        Assert.assertEquals(collectorListMock, collectorViewModel.collectors.value)

        collectorViewModel.collectors.removeObserver(observer)
    }

    @Test
    fun `test network error`() = runTest {
        `when`(collectorRepositoryMock.getCollectors()).thenThrow(RuntimeException("Network error"))

        val observer = mock(Observer::class.java) as Observer<Boolean>
        collectorViewModel.eventNetworkError.observeForever(observer)

        collectorViewModel.refreshDataFromRepository()

        testDispatcher.scheduler.advanceUntilIdle()

        verify(observer).onChanged(true)
        Assert.assertTrue(collectorViewModel.eventNetworkError.value == true)

        collectorViewModel.eventNetworkError.removeObserver(observer)
    }
}