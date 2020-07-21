package com.sablania.flickrimageviewer.viewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sablania.baseandroidlibrary.BaseViewModel
import com.sablania.flickrimageviewer.models.FlickrImagesResp
import com.sablania.flickrimageviewer.repositories.FlickrImagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FlickrImagesViewModel(application: Application) : BaseViewModel(application) {
    private val flickrImagesRepository = FlickrImagesRepository(application)
    private val PER_PAGE = 25
    private var searchText: String = "Random"
    private var currentPage = 1

    private val initialLoadImagesLiveData by lazy { MutableLiveData<FlickrImagesResp>() }
    private val loadMoreImagesLiveData by lazy { MutableLiveData<FlickrImagesResp>() }
    private val fullPageErrorLiveData by lazy { MutableLiveData<Boolean>() }
    private val inlineErrorLiveData by lazy { MutableLiveData<Boolean>() }
    private val progressLiveData by lazy { MutableLiveData<Boolean>() }
    private val noDataLiveData by lazy { MutableLiveData<Boolean>() }

    fun initialLoadImages(searchText: String) = viewModelScope.launch(Dispatchers.IO) {
        currentPage = 1
        this@FlickrImagesViewModel.searchText = searchText
        loadMoreImages()
    }

    fun loadMoreImages() = viewModelScope.launch(Dispatchers.IO) {
        progressLiveData.postValue(true)
        noDataLiveData.postValue(false)
        fullPageErrorLiveData.postValue(false)
        val responsePair = flickrImagesRepository.getFlickrImages(searchText, PER_PAGE, currentPage)
        if (responsePair.first) {
            val responseBody = responsePair.second as FlickrImagesResp
            if (currentPage == 1) {
                noDataLiveData.postValue(responseBody.photos.photo.isEmpty())
                initialLoadImagesLiveData.postValue(responseBody)
            } else {
                loadMoreImagesLiveData.postValue(responseBody)
            }
            currentPage++ //when call is successfull, increment page number
            progressLiveData.postValue(false)
        } else {
            if (currentPage == 1) {
                fullPageErrorLiveData.postValue(true)
            } else {
                inlineErrorLiveData.postValue(true)
            }
        }
    }

    fun restorePrevLoadIfAnyOrLoadRandom() {
        //since data in this viewmodel would survive configuration changes; simply make a loadMoreImage() call
        loadMoreImages()
    }

    fun getInitialLoadImagesLiveData(): LiveData<FlickrImagesResp> = initialLoadImagesLiveData

    fun getLoadMoreImagesLiveData(): LiveData<FlickrImagesResp> = loadMoreImagesLiveData

    fun getFullPageErrorLiveData(): LiveData<Boolean> = fullPageErrorLiveData

    fun getInlineErrorLiveData(): LiveData<Boolean> = inlineErrorLiveData

    fun getProgressLiveData(): LiveData<Boolean> = progressLiveData

    fun getNoDataLiveData(): LiveData<Boolean> = noDataLiveData
}
