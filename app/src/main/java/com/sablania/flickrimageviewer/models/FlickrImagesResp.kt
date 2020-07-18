package com.sablania.flickrimageviewer.models

import com.google.gson.annotations.SerializedName

data class FlickrImagesRequest(
    @SerializedName("method") val method: String = "flickr.photos.search",
    @SerializedName("api_key") val apiKey: String = "062a6c0c49e4de1d78497d13a7dbb360",
    @SerializedName("text") val text: String,
    @SerializedName("format") val format: String = "json",
    @SerializedName("nojsoncallback") val noJsonCallback: Int = 1,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("page") val page: Int
)

data class FlickrImagesResp(
    @SerializedName("photos") val photos: FlickrImages,
    @SerializedName("stat") val status: String
)

data class FlickrImages(
    @SerializedName("page") val page: String,
    @SerializedName("pages") val pages: String,
    @SerializedName("perpage") val perPage: String,
    @SerializedName("total") val total: String,
    @SerializedName("photo") val photo: List<FlickrImage>
)

data class FlickrImage(
    @SerializedName("id") val id: String,
    @SerializedName("owner") val owner: String,
    @SerializedName("secret") val secret: String,
    @SerializedName("server") val server: String,
    @SerializedName("farm") val farm: Int,
    @SerializedName("title") val title: String,
    @SerializedName("ispublic") val isPublic: Int,
    @SerializedName("isfriend") val isFriend: Int,
    @SerializedName("isfamily") val isFamily: Int
)