package com.cyberfanta.talentviewer.views

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberfanta.talentviewer.R
import com.cyberfanta.talentviewer.databinding.ActivityMainBinding
import com.cyberfanta.talentviewer.models.*
import com.cyberfanta.talentviewer.presenters.PageData
import com.cyberfanta.talentviewer.views.cards.OpportunitiesAdapter
import com.cyberfanta.talentviewer.views.cards.PeoplesAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity(), android.widget.SearchView.OnQueryTextListener {
    //To bind view with logical part
    private lateinit var viewBinding: ActivityMainBinding

    //Manage RecyclerViews
    private lateinit var peopleAdapter: PeoplesAdapter
    private lateinit var opportunityAdapter: OpportunitiesAdapter
    private val peopleList = mutableListOf<PeopleItem>()
    private val opportunityList = mutableListOf<OpportunityItem>()

    //Manage query sizes
    private var querySize = 20
    private var peopleOffset = 0
    private var opportunityOffset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.searchView.setOnQueryTextListener(this)

        fillRecyclerViewBios()
        fillRecyclerViewJobs()
    }

    private fun fillRecyclerViewBios() {
        peopleAdapter = PeoplesAdapter (peopleList)
        viewBinding.recyclerViewBios.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerViewBios.adapter = peopleAdapter

        getPeoples(PageData(peopleOffset.toString(), querySize.toString(), ""))
    }

    private fun fillRecyclerViewJobs() {
        opportunityAdapter = OpportunitiesAdapter (opportunityList)
        viewBinding.recyclerViewBios.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerViewBios.adapter = opportunityAdapter

        getOpportunities(PageData(opportunityOffset.toString(), querySize.toString(), ""))
    }

//    ---

    private fun getPeoples(pageData: PageData) {
        CoroutineScope(Dispatchers.IO).launch {
            val call : Response<Peoples> = getRetrofitPeoples().create(APIService::class.java).getPeoples(pageData.toString())
            val response : Peoples? = call.body()
            runOnUiThread {
                if (call.isSuccessful){
                    for (result in response?.results!!)
                        if (!peopleList.contains(result))
                            result?.let {
                                peopleList.add(it)
                            }
                    peopleAdapter.notifyDataSetChanged()
                } else {
                    showError()
                }
            }
        }
    }

    private fun getRetrofitPeoples(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://search.torre.co/people/_search/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getOpportunities(pageData: PageData) {
        CoroutineScope(Dispatchers.IO).launch {
            val call : Response<Opportunities> = getRetrofitOpportunities().create(APIService::class.java).getOpportunities(pageData.toString())
            val response : Opportunities? = call.body()
            runOnUiThread {
                if (call.isSuccessful){
                    for (result in response?.results!!)
                        if (!opportunityList.contains(result))
                            result?.let {
                                opportunityList.add(it)
                            }
                    opportunityAdapter.notifyDataSetChanged()
                } else {
                    showError()
                }
            }
        }
    }

    private fun getRetrofitOpportunities(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://search.torre.co/opportunities/_search/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getBio(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call : Response<Bios> = getRetrofitBio().create(APIService::class.java).getBio(id)
            val response : Bios? = call.body()
            if (call.isSuccessful){
                TODO("Not yet implemented")
            } else {
                showError()
            }
        }
    }

    private fun getRetrofitBio(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://torre.bio/api/bios/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getJob(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call : Response<Jobs> = getRetrofitJob().create(APIService::class.java).getJob(id)
            val response : Jobs? = call.body()
            if (call.isSuccessful){
                TODO("Not yet implemented")
            } else {
                showError()
            }
        }
    }

    private fun getRetrofitJob(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://torre.co/api/opportunities/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun showError() {
        Toast.makeText(this, getString(R.string.error_loading), Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            hideKeyboard()

            opportunityList.clear()
            opportunityOffset = 0
            getOpportunities(PageData(opportunityOffset.toString(), querySize.toString(), query.lowercase(Locale.getDefault())))
//            TODO("getPeoples")
        }
        return true
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param newText the new content of the query text field.
     *
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener.
     */
    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

//    ---

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(viewBinding.mainLayout.windowToken, 0)
    }
}
