package com.miso.vinilosapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.miso.vinilosapp.data.models.Collector
import com.miso.vinilosapp.data.repositories.CollectorRepository
import kotlinx.coroutines.launch

class CollectorViewModel(application: Application, val collectorRepository: CollectorRepository) :
    AndroidViewModel(application) {

    private val _collectors = MutableLiveData<List<Collector>>()

    val collectors: LiveData<List<Collector>>
        get() = _collectors

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
                val data = collectorRepository.getCollectors()
                _collectors.postValue(data)
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

    class Factory(val app: Application, val collectorRepository: CollectorRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CollectorViewModel(app, collectorRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
