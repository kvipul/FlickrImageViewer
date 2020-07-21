package com.sablania.flickrimageviewer.models

import com.google.gson.annotations.SerializedName

data class FlickrImagesResp(
    @SerializedName("photos") val photos: FlickrImages,
    @SerializedName("stat") val status: String
)

data class FlickrImages(
    @SerializedName("page") val page: String,
    @SerializedName("pages") val pages: String,
    @SerializedName("perpage") val perPage: String,
    @SerializedName("total") val total: String,
    @SerializedName("photo") val photo: ArrayList<FlickrImage>
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
) {
    fun getImageSquareThumbnail(): String {
        return "https://farm${farm}.staticflickr.com/${server}/${id}_${secret}_s.jpg"
    }

    fun getImageLarge(): String {
        return "https://farm${farm}.staticflickr.com/${server}/${id}_${secret}_b.jpg"
    }
}