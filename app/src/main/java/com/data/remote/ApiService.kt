package com.data.remote

import com.data.model.PostResponse
import com.data.model.UploadResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @GET("products")
    fun getAll(): Call<PostResponse>

    @Multipart
    @POST("upload")
    fun uploadData(
        @Part("thumbnail") thumbnail: String,
        @Part("email") email: String,
        @Part("description") description: String
    ): Call<UploadResponse>
}