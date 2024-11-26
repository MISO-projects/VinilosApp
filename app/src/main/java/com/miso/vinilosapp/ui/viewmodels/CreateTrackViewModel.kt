package com.miso.vinilosapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.models.Song
import com.miso.vinilosapp.data.repositories.AlbumRepository
import com.miso.vinilosapp.data.repositories.SongRepository
import kotlinx.coroutines.launch

class CreateTrackViewModel(
    application: Application,
    val albumRepository: AlbumRepository,
    private val songRepository: SongRepository
) : AndroidViewModel(application) {

    private val _albums = MutableLiveData<List<Album>>()

    private val _newSong = MutableLiveData<Song?>()
    val newSong: LiveData<Song?>
        get() = _newSong

    val albums: LiveData<List<Album>>
        get() = _albums

    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromRepository()
    }

    fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                albumRepository.getAlbums().collect { albumsList ->
                    _albums.postValue(albumsList)
                }
            } catch (e: Exception) {
                _eventNetworkError.postValue(true)
            }
        }
    }

    fun addSongToAlbum(albumId: Int, name: String, duration: String) {
        viewModelScope.launch {
            try {
                val song = songRepository.addSongToAlbum(albumId, name, duration)
                _newSong.postValue(song)
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
        private val songRepository: SongRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateTrackViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CreateTrackViewModel(app, albumRepository, songRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
