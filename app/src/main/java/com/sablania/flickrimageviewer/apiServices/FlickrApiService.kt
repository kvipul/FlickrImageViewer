package com.sablania.flickrimageviewer.apiServices

import com.sablania.flickrimageviewer.models.FlickrImagesResp
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FlickrApiService {
    @GET("/services/rest/")
    suspend fun getFlickrImages(@QueryMap reqMap: HashMap<String, Any>): Response<FlickrImagesResp>
}
