package com.cyberfanta.talentviewer.models

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getBio(@Url id: String) : Response<Bios>

    @GET
    suspend fun getJob(@Url id: String) : Response<Jobs>

    @POST
    suspend fun getBios(@Url offset: Int, @Url size: Int, @Url aggregators: String) : Response<Peoples>

    @POST
    suspend fun getJobs(@Url offset: Int, @Url size: Int, @Url aggregators: String) : Response<Opportunities>
}

