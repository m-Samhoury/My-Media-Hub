package com.moustafa.mymediahub.features.imagegallerylistscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moustafa.mymediahub.models.PhotoInfo
import com.moustafa.mymediahub.repository.Repository
import com.moustafa.mymediahub.repository.network.StateMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author moustafasamhoury
 * created on Wednesday, 18 Sep, 2019
 */
data class MyHubGalleryState(val imagesStateMonitor: StateMonitor<List<PhotoInfo>>)

class MyHubGalleryViewModel(
    private val repository: Repository,
    private val myHubGalleryState: MyHubGalleryState = MyHubGalleryState(StateMonitor.Init)
) : ViewModel() {

    private val _stateLiveData: MutableLiveData<MyHubGalleryState> = MutableLiveData()
    val stateLiveData: LiveData<MyHubGalleryState> = _stateLiveData

    init {
        fetchGalleryImages()
    }

    private fun fetchGalleryImages() {
        _stateLiveData.value =
            myHubGalleryState.copy(imagesStateMonitor = StateMonitor.Loading)
        viewModelScope.launch(Dispatchers.Main) {
            val response = repository.fetchHubImages {
                _stateLiveData.value =
                    myHubGalleryState.copy(imagesStateMonitor = StateMonitor.Failed(failed = it))
            }
            if (response != null) {
                _stateLiveData.value =
                    myHubGalleryState.copy(imagesStateMonitor = StateMonitor.Loaded(response))
            }
        }
    }
}
