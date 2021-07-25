package com.cyberfanta.talentviewer.models

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getBio(@Url url: String) : Response<Bios>

    @GET
    suspend fun getJob(@Url url: String) : Response<Jobs>

    @Headers("Content-Type: application/json")
    @POST
    suspend fun getPeoples(@Url url: String) : Response<Peoples>

    @Headers("Content-Type: application/json")
    @POST
    suspend fun getOpportunities(@Url url: String) : Response<Opportunities>
}

