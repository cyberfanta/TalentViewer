package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.APIService
import com.cyberfanta.talentviewer.models.Opportunities
import com.cyberfanta.talentviewer.models.Peoples
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class APIData {
    private fun getRetrofitBio() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://torre.bio/api/bios/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getRetrofitJob() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://torre.co/api/opportunities/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getRetrofitPeoples() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://search.torre.co/people/_search/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getRetrofitOpportunities() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://search.torre.co/opportunities/_search/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

//    ---

    fun getPeoples(offset: Int, size: Int, aggregators: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call : Response<Peoples> = getRetrofitPeoples().create(APIService::class.java).getBios(offset, size, aggregators)
            val response : Peoples? = call.body()
            if (call.isSuccessful){
                //show recycler
                TODO("Not yet implemented")
            } else {
                //show error
                TODO("Not yet implemented")
            }
        }
    }

    fun getOpportunities(offset: Int, size: Int, aggregators: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call : Response<Opportunities> = getRetrofitOpportunities().create(APIService::class.java).getJobs(offset, size, aggregators)
            val response : Opportunities? = call.body()
            if (call.isSuccessful){
                //show recycler
                TODO("Not yet implemented")
            } else {
                //show error
                TODO("Not yet implemented")
            }
        }
    }

    fun getBio(offset: Int, size: Int, aggregators: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call : Response<Opportunities> = getRetrofitOpportunities().create(APIService::class.java).getJobs(offset, size, aggregators)
            val response : Opportunities? = call.body()
            if (call.isSuccessful){
                //show recycler
                TODO("Not yet implemented")
            } else {
                //show error
                TODO("Not yet implemented")
            }
        }
    }

    fun getJob(offset: Int, size: Int, aggregators: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call : Response<Opportunities> = getRetrofitOpportunities().create(APIService::class.java).getJobs(offset, size, aggregators)
            val response : Opportunities? = call.body()
            if (call.isSuccessful){
                //show recycler
                TODO("Not yet implemented")
            } else {
                //show error
                TODO("Not yet implemented")
            }
        }
    }
}