package com.miso.vinilosapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.repositories.AlbumRepository
import kotlinx.coroutines.launch

class CollectorAlbumsDetailViewModel(
    application: Application,
    private val albumRepository: AlbumRepository,
    collectorId: Int
) : AndroidViewModel(application) {

    val collectorId: Int = collectorId

    private val _collectorAlbums = MutableLiveData<List<Album>>()

    val collectorAlbums: LiveData<List<Album>>
        get() = _collectorAlbums

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
                Log.d("CollectorAlbumsDetail", "collectorId: $collectorId")
                val data = albumRepository.getAlbumsByCollectorId(collectorId)
                _collectorAlbums.postValue(data)
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            } catch (e: Exception) {
                _eventNetworkError.postValue(true)
            }
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(
        val app: Application,
        private val albumRepository: AlbumRepository,
        private val albumId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorAlbumsDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CollectorAlbumsDetailViewModel(app, albumRepository, albumId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
