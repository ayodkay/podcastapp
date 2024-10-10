package com.example.podcastapp.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface PodcastService {
    @GET
    suspend fun getFeed(@Url url: String): Response<ResponseBody>
}