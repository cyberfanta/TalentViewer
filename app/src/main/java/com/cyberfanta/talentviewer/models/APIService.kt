package com.cyberfanta.talentviewer.models

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface APIService {
    @Headers("Content-Type: application/json")
    @POST
    fun getOpportunities(@Url url: String) : Call<JsonObject>

    @Headers("Content-Type: application/json")
    @POST
    fun getPeoples(@Url url: String) : Call<JsonObject>

    @GET
    fun getJob(@Url url: String) : Call<JsonObject>

    @GET
    fun getBio(@Url url: String) : Call<JsonObject>
}

