package com.cyberfanta.talentviewer.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyberfanta.talentviewer.R
import com.cyberfanta.talentviewer.databinding.ActivityMainBinding
import com.cyberfanta.talentviewer.models.*
import com.cyberfanta.talentviewer.presenters.PageData
import com.cyberfanta.talentviewer.views.cards.OpportunitiesAdapter
import com.cyberfanta.talentviewer.views.cards.PeoplesAdapter
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), android.widget.SearchView.OnQueryTextListener {
    //To bind view with logical part
    private lateinit var viewBinding: ActivityMainBinding

    //Storage the device dimension
    private lateinit var deviceDimension: IntArray

    //Manage RecyclerViews
    private lateinit var peopleAdapter: PeoplesAdapter
    private lateinit var opportunityAdapter: OpportunitiesAdapter
    private val peopleList = mutableListOf<PeopleItem>()
    private val opportunityList = mutableListOf<OpportunityItem>()

    //Manage query sizes
    private var querySize = 30
    private var peopleOffset = 0
    private var currentAggregators = ""
    private var opportunityOffset = 0
    private var opportunitySwitch = true //true: Opportunity - false: Peoples
    private var currentIdSearch = -1

    //To control coroutines
    private var loadingJobs = false
    private var loadingBios = false

    /**
     * The initial point of this app
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //Set view binding
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        //Set search view listeners
        viewBinding.searchView.setOnQueryTextListener(this)

        //Calculate device dimensions
        deviceDimension = DeviceUtils.calculateDeviceDimensions(this)

        initializeLoadingArrowAnimations()
        fillRecyclerViewBios()
        fillRecyclerViewJobs()
        bindOnClickListener()


    }

    private fun fillRecyclerViewBios() {
        peopleAdapter = PeoplesAdapter (peopleList)
        viewBinding.recyclerViewBios.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerViewBios.adapter = peopleAdapter

        getPeoples(PageData(peopleOffset.toString(), querySize.toString(), currentAggregators))
        DeviceUtils.setAnimation(viewBinding.recyclerViewBios, "translationX", 300, false, 0f, -1f * deviceDimension[0])
    }

    private fun fillRecyclerViewJobs() {
        opportunityAdapter = OpportunitiesAdapter (opportunityList)
        viewBinding.recyclerViewJobs.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerViewJobs.adapter = opportunityAdapter

        getOpportunities(PageData(opportunityOffset.toString(), querySize.toString(), currentAggregators))

        opportunityAdapter.setOnItemClickListener(object:
            OpportunitiesAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
//                FirebaseManager.logEvent("Character Detail: " + (position+1) + " - " + queryManager.getCharacterDetail(position+1).name, "Get_Character_Detail")

                //Activating Loading Arrow
                viewBinding.jobsLoading.visibility = View.VISIBLE

//                currentIdSearch = position + 1
                opportunityList[position].id?.let { getJob(it) }
            }
        })

        opportunityAdapter.setOnBottomReachedListener(object:
            OpportunitiesAdapter.OnBottomReachedListener {
            override fun onBottomReached(position: Int) {
                if (!loadingJobs) {
                    //Activating Loading Arrow
                    viewBinding.jobsLoading.visibility = View.VISIBLE

//                FirebaseManager.logEvent("Character Page: $characterPagesLoaded", "Get_Character_Page")
                    getOpportunities(
                        PageData(
                            opportunityOffset.toString(),
                            querySize.toString(),
                            currentAggregators
                        )
                    )
                }
            }
        })

        viewBinding.recyclerViewJobs.addOnScrollListener(object:
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!loadingJobs) {
                        //Activating Loading Arrow
                        viewBinding.jobsLoading.visibility = View.VISIBLE

//                    FirebaseManager.logEvent("Character Page: $characterPagesLoaded", "Get_Character_Page")
                        getOpportunities(
                            PageData(
                                opportunityOffset.toString(),
                                querySize.toString(),
                                currentAggregators
                            )
                        )
                    }
                }
            }
        })

    }

    /**
     * Load all onClick functions for all views on MainActivity
     */
    private fun bindOnClickListener() {
        viewBinding.jobsButton.setOnClickListener { jobsButtonPressed() }
        viewBinding.biosButton.setOnClickListener { biosButtonPressed() }

    }

    /**
     * Load all initial loading arrow animations
     */
    private fun initializeLoadingArrowAnimations() {
        DeviceUtils.setAnimation(viewBinding.biosLoading, "rotation", 1000, true, 0f, 360f)
        DeviceUtils.setAnimation(viewBinding.jobsLoading, "rotation", 1000, true, 0f, 360f)
    }

    /**
     * The finishing action of this app
     */
    override fun onDestroy() {
        super.onDestroy()

        //This way don't wait for Android Garbage collection. However, Android Studio work better with this.
        exitProcess(0)
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
                    opportunityOffset += querySize
                    opportunityAdapter.notifyDataSetChanged()
                } else {
                    showError()
                }
                viewBinding.jobsLoading.visibility = View.INVISIBLE
                loadingJobs = false
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
        val intent = Intent(this, JobActivity::class.java)
        intent.putExtra("deviceWidth", deviceDimension[0].toString())
        intent.putExtra("deviceHeight", deviceDimension[1].toString())
        intent.putExtra("id", id)
        startActivity(intent)

        //Deactivating Loading Arrow
        viewBinding.jobsLoading.visibility = View.INVISIBLE
    }

    private fun showError() {
        Toast.makeText(this, getString(R.string.error_loading), Toast.LENGTH_SHORT).show()
    }

//    ---

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            hideKeyboard()

            if (opportunitySwitch) {
                opportunityList.clear()
                opportunityOffset = 0
                currentAggregators = query.lowercase(Locale.getDefault())

                //Activating Loading Arrow
                viewBinding.jobsLoading.visibility = View.VISIBLE

                getOpportunities(
                    PageData(
                        opportunityOffset.toString(),
                        querySize.toString(),
                        currentAggregators
                    )
                )
            } else {
                peopleList.clear()
                peopleOffset = 0
                currentAggregators = query.lowercase(Locale.getDefault())

                //Activating Loading Arrow
                viewBinding.biosLoading.visibility = View.VISIBLE

                getPeoples(
                    PageData(
                        peopleOffset.toString(),
                        querySize.toString(),
                        currentAggregators
                    )
                )
            }
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

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(viewBinding.mainLayout.windowToken, 0)
    }

//    ---

    private fun jobsButtonPressed () {
        if (!opportunitySwitch) {
            DeviceUtils.setAnimation(viewBinding.recyclerViewJobs, "translationX", 300, false, 1f * deviceDimension[0], 0f)
            DeviceUtils.setAnimation(viewBinding.recyclerViewBios, "translationX", 300, false, 0f, -1f * deviceDimension[0])
            opportunitySwitch = true
        } else {
            YoYo.with(Techniques.Shake)
                .duration(500)
                .playOn(viewBinding.jobsButton)
        }
    }

    private fun biosButtonPressed () {
        if (opportunitySwitch) {
            DeviceUtils.setAnimation(viewBinding.recyclerViewJobs, "translationX", 300, false, 0f, 1f * deviceDimension[0])
            DeviceUtils.setAnimation(viewBinding.recyclerViewBios, "translationX", 300, false, -1f * deviceDimension[0], 0f)
            opportunitySwitch = false
        } else {
            YoYo.with(Techniques.Shake)
                .duration(500)
                .playOn(viewBinding.biosButton)
        }
    }
}
