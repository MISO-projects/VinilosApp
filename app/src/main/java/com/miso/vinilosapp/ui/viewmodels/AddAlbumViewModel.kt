package com.miso.vinilosapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.models.AlbumRequest
import com.miso.vinilosapp.data.repositories.AlbumRepository
import kotlinx.coroutines.launch

class AddAlbumViewModel(application: Application, private val albumRepository: AlbumRepository) :
    AndroidViewModel(application) {
    private val _album = MutableLiveData<Album>()

    val album: MutableLiveData<Album>
        get() = _album

    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private val _albumCreated = MutableLiveData<Boolean>(false)
    val albumCreated: LiveData<Boolean>
        get() = _albumCreated

    fun addAlbum(album: AlbumRequest) {
        viewModelScope.launch {
            try {
                albumRepository.addAlbum(album)
                _albumCreated.postValue(true)
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            } catch (e: Exception) {
                _eventNetworkError.postValue(true)
            }
        }
    }

    fun onAlbumCreatedHandled() {
        _albumCreated.value = false
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(
        val app: Application,
        private val albumRepository: AlbumRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddAlbumViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AddAlbumViewModel(app, albumRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
