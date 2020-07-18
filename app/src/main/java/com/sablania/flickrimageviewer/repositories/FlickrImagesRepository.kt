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

    suspend fun getFlickrImages(searchText: String, perPage: Int, page: Int) {
        val reqMap = HashMap<String, Any>()
        reqMap["method"] = "flickr.photos.search"
        reqMap["api_key"] = "062a6c0c49e4de1d78497d13a7dbb360"
        reqMap["text"] = searchText
        reqMap["per_page"] = perPage
        reqMap["page"] = page
        reqMap["format"] = "json"
        reqMap["nojsoncallback"] = 1

        val call = flickrApiService.getFlickrImages(reqMap)
        if (call.isSuccessful) {
            imagesLiveData.postValue(call.body())
        } else {
            Log.e("vipul", call.errorBody().toString())
        }
    }

    fun getFlickrImagesLiveData(): LiveData<FlickrImagesResp> = imagesLiveData

}
