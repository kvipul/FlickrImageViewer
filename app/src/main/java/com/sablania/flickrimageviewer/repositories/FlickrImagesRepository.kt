package com.sablania.flickrimageviewer.repositories

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sablania.baseandroidlibrary.BaseRepository
import com.sablania.flickrimageviewer.apiServices.FlickrApiService
import com.sablania.flickrimageviewer.models.FlickrImagesResp

class FlickrImagesRepository(application: Application) : BaseRepository(application) {

    private val flickrApiService =
        getRetrofitClient("https://api.flickr.com").create(FlickrApiService::class.java)

    private val imagesLiveData by lazy { MutableLiveData<FlickrImagesResp>() }

    private val errorLiveData by lazy { MutableLiveData<Boolean>() }
    private val progressLiveData by lazy { MutableLiveData<Boolean>() }

    suspend fun getFlickrImages(searchText: String, perPage: Int, page: Int) {
        val reqMap = HashMap<String, Any>()
        reqMap["method"] = "flickr.photos.search" //don't change
        reqMap["api_key"] = "062a6c0c49e4de1d78497d13a7dbb360" //don't change
        reqMap["text"] = searchText
        reqMap["per_page"] = perPage
        reqMap["page"] = page
        reqMap["format"] = "json"
        reqMap["nojsoncallback"] = 1

        Log.i("vipul", "call start for $page")
        try {
            val call = flickrApiService.getFlickrImages(reqMap)
            if (call.isSuccessful) {
                imagesLiveData.postValue(call.body())
                errorLiveData.postValue(false)
            } else {
                errorLiveData.postValue(true)
            }
            Log.i("vipul", "call stop for $page")
        } catch (e: Exception) {
            errorLiveData.postValue(true)
        }
    }

    fun getFlickrImagesLiveData(): LiveData<FlickrImagesResp> = imagesLiveData

    fun getErrorImagesLiveData(): LiveData<Boolean> = errorLiveData

    fun getProgressImagesLiveData(): LiveData<Boolean> = progressLiveData
}
