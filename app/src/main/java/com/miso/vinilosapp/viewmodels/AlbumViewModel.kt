package com.miso.vinilosapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.miso.vinilosapp.models.Album
import com.miso.vinilosapp.network.NetworkServiceAdapter

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    private val _albums = MutableLiveData<List<Album>>()

    val albums: LiveData<List<Album>>
        get() = _albums

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        NetworkServiceAdapter.apiService.getAlbums()
            .enqueue(object : retrofit2.Callback<List<Album>> {
                override fun onResponse(
                    call: retrofit2.Call<List<Album>>,
                    response: retrofit2.Response<List<Album>>
                ) {
                    if (response.isSuccessful) {
                        _albums.postValue(response.body())
                        _eventNetworkError.value = false
                        _isNetworkErrorShown.value = false
                    } else {
                        _eventNetworkError.value = true
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<Album>>, t: Throwable) {
                    _eventNetworkError.value = true
                }
            })
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}