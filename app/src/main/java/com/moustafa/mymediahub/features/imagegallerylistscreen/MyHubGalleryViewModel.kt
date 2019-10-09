package com.moustafa.mymediahub.features.imagegallerylistscreen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moustafa.mymediahub.models.PhotoInfo
import com.moustafa.mymediahub.repository.Repository
import com.moustafa.mymediahub.repository.network.StateMonitor
import com.moustafa.mymediahub.utils.RealPathUtil
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


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

    fun compressImageAndUpload(context: Context, imageUri: Uri) {
        viewModelScope.launch(Dispatchers.Main) {
            _stateLiveData.value =
                myHubGalleryState.copy(imagesStateMonitor = StateMonitor.Loading)

            val compressedBitmap = compressImageAsync(context, imageUri)
            val response = uploadImage(compressedBitmap!!) {
                withContext(Dispatchers.Main) {
                    _stateLiveData.value =
                        myHubGalleryState.copy(imagesStateMonitor = StateMonitor.Failed(failed = it))
                }
            }
            if (response == true) {
                fetchGalleryImages()
            }
        }
    }

    private suspend fun compressImageAsync(
        context: Context,
        imageUri: Uri
    ): File? = withContext(Dispatchers.Default) {
        val path = RealPathUtil.getRealPath(context, imageUri)
        Compressor(context).compressToFile(File(path))
    }


    private suspend fun uploadImage(
        bitmap: File,
        onError: suspend (Exception) -> Unit
    ): Boolean? = withContext(Dispatchers.IO) {
        repository.uploadImage(bitmap, onError)
    }

}
