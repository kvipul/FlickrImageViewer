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
    private val PER_PAGE = 20
    private var searchText: String = "Random"

    fun getFlickrImages(searchText: String, page: Int) = viewModelScope.launch(Dispatchers.IO) {
        this@FlickrImagesViewModel.searchText = searchText
        flickrImagesRepository.getFlickrImages(searchText, PER_PAGE, page)
    }

    fun getFlickrImagesLiveData(): LiveData<FlickrImagesResp> {
        return flickrImagesRepository.getFlickrImagesLiveData()
    }

    fun getErrorLiveData(): LiveData<Boolean> {
        return flickrImagesRepository.getErrorImagesLiveData()
    }

    fun getProgressLiveData(): LiveData<Boolean> {
        return flickrImagesRepository.getProgressImagesLiveData()
    }

    fun loadMoreImages(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        flickrImagesRepository.getFlickrImages(
            searchText,
            PER_PAGE,
            page
        )
    }
}
