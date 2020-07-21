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

    /**
     * returns Pair<IsSuccessful, Body>
     */
    suspend fun getFlickrImages(searchText: String, perPage: Int, page: Int): Pair<Boolean, Any?> {
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
            Log.i("vipul", "call stop for $page")
            return Pair(call.isSuccessful, call.body())
        } catch (e: Exception) {
            //Type of error can be handled here such as NetworkError, ServerError
            return Pair(false, null)
        }
    }
}
