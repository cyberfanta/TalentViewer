package com.cyberfanta.talentviewer.models

import com.cyberfanta.talentviewer.presenters.FirebaseManager
import com.cyberfanta.talentviewer.presenters.PageData
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryAPIImpl (var interactor: Interactor) : RepositoryAPI{

    /**
     * Get a job page from API
     */
    override fun getJobPage(pageData: PageData) {
        val call = APIAdapter.getRetrofitOpportunities().create(APIService::class.java).getOpportunities(pageData.toString())

        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                FirebaseManager.logEvent("Error_Loading_Job_Page", "Error_Loading_Data")
                interactor.errorLoadingJobPage()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val messageList = Gson().fromJson(response.body(), Opportunities::class.java)
                val opportunities = mutableMapOf<String, OpportunityItem>()
                for (result in messageList.results!!)
                    if (result != null)
                        result.id?.let { opportunities.put(it, result) }
                interactor.showJobPage(opportunities)
            }
        })
    }

    /**
     * Get a bio page from API
     */
    override fun getBioPage(pageData: PageData) {
        val call = APIAdapter.getRetrofitPeoples().create(APIService::class.java).getPeoples(pageData.toString())

        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                FirebaseManager.logEvent("Error_Loading_Bio_Page", "Error_Loading_Data")
                interactor.errorLoadingBioPage()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val messageList = Gson().fromJson(response.body(), Peoples::class.java)
                val peoples = mutableMapOf<String, PeopleItem>()
                for (result in messageList.results!!)
                    if (result != null)
                        result.username?.let { peoples.put(it, result) }
                interactor.showBioPage(peoples)
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
                interactor.showJob(Gson().fromJson(response.body(), Jobs::class.java))
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
                interactor.showBio(Gson().fromJson(response.body(), Bios::class.java))
            }
        })
    }
}