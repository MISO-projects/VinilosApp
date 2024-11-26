package com.miso.vinilosapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.miso.vinilosapp.data.models.Song
import com.miso.vinilosapp.data.repositories.SongRepository
import kotlinx.coroutines.launch

class SongViewModel(
    application: Application,
    private val songRepository: SongRepository,
    private val albumId: Int
) : AndroidViewModel(application) {

    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> get() = _songs

    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        viewModelScope.launch {
            songRepository.getSongsByAlbumId(albumId)
                .collect { songList ->
                    _songs.postValue(songList)
                }
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                songRepository.getSongsByAlbumId(albumId)
                    .collect { songList ->
                        _songs.postValue(songList)
                    }
            } catch (e: Exception) {
                _eventNetworkError.postValue(true)
            }
        }
    }

    class Factory(
        private val app: Application,
        private val songRepository: SongRepository,
        private val albumId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SongViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SongViewModel(app, songRepository, albumId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
