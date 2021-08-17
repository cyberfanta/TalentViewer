package com.cyberfanta.talentviewer.models

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIAdapter {
    companion object {
        //Client timeout value
        private val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build()

        /**
         * Retrofit implementation to get a opportunity list
         */
        fun getRetrofitOpportunities(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://search.torre.co/opportunities/_search/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build()
        }

        /**
         * Retrofit implementation to get a people list
         */
        fun getRetrofitPeoples(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://search.torre.co/people/_search/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build()
        }

        /**
         * Retrofit implementation to get a job detail
         */
        fun getRetrofitJob(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://torre.co/api/opportunities/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build()
        }

        /**
         * Retrofit implementation to get a bio detail
         */
        fun getRetrofitBio(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://torre.bio/api/bios/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build()
        }
    }
}