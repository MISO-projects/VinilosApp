package com.miso.vinilosapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.models.Collector
import com.miso.vinilosapp.data.repositories.CollectorRepository
import kotlinx.coroutines.launch

class CollectorDetailViewModel(
    application: Application,
    private val collectorRepository: CollectorRepository,
    collectorId: Int
) : AndroidViewModel(application) {

    val id: Int = collectorId

    private val _collector = MutableLiveData<Collector>()

    val collector: LiveData<Collector>
        get() = _collector

    private val _albumsCollector = MutableLiveData<List<Album>>()

    val albumsCollector: LiveData<Collector>
        get() = _collector


    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromNetwork()
    }

    fun refreshDataFromNetwork() {
        viewModelScope.launch {
            try {
                val data = collectorRepository.getCollectorById(id)
                _collector.postValue(data)

                val albums = data.collectorAlbums
                _albumsCollector.postValue(albums)

                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            } catch (e: Exception) {
                _eventNetworkError.value?.let { currentValue ->
                    if (!currentValue) {
                        _eventNetworkError.postValue(true)
                    }
                }
            }
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(
        val app: Application,
        private val collectorRepository: CollectorRepository,
        private val collectorId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CollectorDetailViewModel(app, collectorRepository, collectorId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
