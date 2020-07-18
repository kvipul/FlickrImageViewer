package com.sablania.flickrimageviewer.viewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sablania.baseandroidlibrary.BaseViewModel
import com.sablania.flickrimageviewer.models.FlickrImagesResp
import com.sablania.flickrimageviewer.repositories.FlickrImagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FlickrImagesViewModel(application: Application) : BaseViewModel(application) {
    private val flickrImagesRepository = FlickrImagesRepository(application)

    fun getFlickrImages(searchText: String, perPage: Int, page: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            flickrImagesRepository.getFlickrImages(searchText, perPage, page)
        }

    fun getFlickrImagesLiveData(): LiveData<FlickrImagesResp> {
        return flickrImagesRepository.getFlickrImagesLiveData()
    }

}
