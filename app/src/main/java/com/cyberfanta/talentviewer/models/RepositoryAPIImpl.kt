package com.cyberfanta.talentviewer.models

import android.util.Log
import com.cyberfanta.talentviewer.presenters.FirebaseManager
import com.cyberfanta.talentviewer.presenters.PageData
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryAPIImpl (var interactor: Interactor) : RepositoryAPI{
    @Suppress("PrivatePropertyName", "unused")
    private val TAG = this::class.java.simpleName

    /**
     * Get opportunities from API
     */
    override fun getOpportunities(pageData: PageData) {
        val call = APIAdapter.getRetrofitOpportunities().create(APIService::class.java).getOpportunities(pageData.toString())

        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                FirebaseManager.logEvent("Error_Loading_Opportunities", "Error_Loading_Data")
                interactor.errorLoadingOpportunities()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val messageList = Gson().fromJson(response.body(), Opportunities::class.java)
                val opportunities = mutableMapOf<String, OpportunityItem>()
                for (result in messageList.results!!)
                    if (result != null)
                        result.id?.let { opportunities.put(it, result) }
                interactor.showOpportunities(opportunities)
            }
        })
    }

    /**
     * Get peoples from API
     */
    override fun getPeoples(pageData: PageData) {
        val call = APIAdapter.getRetrofitPeoples().create(APIService::class.java).getPeoples(pageData.toString())

        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                FirebaseManager.logEvent("Error_Loading_Peoples", "Error_Loading_Data")
                interactor.errorLoadingPeoples()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val messageList = Gson().fromJson(response.body(), Peoples::class.java)
                val peoples = mutableMapOf<String, PeopleItem>()
                for (result in messageList.results!!)
                    if (result != null)
                        result.username?.let { peoples.put(it, result) }
                interactor.showPeoples(peoples)
            }
        })
    }

    /**
     * Get a simple job from API
     */
    override fun getJob(id: String) {
        val call = APIAdapter.getRetrofitJob().create(APIService::class.java).getJob(id)

        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                FirebaseManager.logEvent("Error_Loading_Job", "Error_Loading_Data")
                interactor.errorLoadingJob()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.i(TAG, "id: $id")
                Log.i(TAG, "response.body(): " + response.body())
                Log.i(TAG, "Gson().fromJson(response.body(), Jobs::class.java): " + Gson().fromJson(response.body(), Jobs::class.java))
                if (response.body() != null) {
                    interactor.showJob(Gson().fromJson(response.body(), Jobs::class.java))
                } else {
                    interactor.errorLoadingJob()
                }
            }
        })
    }

    /**
     * Get a simple bio from API
     */
    override fun getBio(username: String) {
        val call = APIAdapter.getRetrofitBio().create(APIService::class.java).getBio(username)

        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                FirebaseManager.logEvent("Error_Loading_Bio", "Error_Loading_Data")
                interactor.errorLoadingBio()
//                Log.e("ERROR: ", t.message.toString())
//                t.stackTrace
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.body() != null) {
                    interactor.showBio(Gson().fromJson(response.body(), Bios::class.java))
                } else {
                    interactor.errorLoadingBio()
                }
            }
        })
    }
}