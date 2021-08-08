package com.cyberfanta.talentviewer.views

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyberfanta.talentviewer.R
import com.cyberfanta.talentviewer.databinding.ActivityMainBinding
import com.cyberfanta.talentviewer.models.*
import com.cyberfanta.talentviewer.presenters.FirebaseManager
import com.cyberfanta.talentviewer.presenters.PageData
import com.cyberfanta.talentviewer.presenters.RateAppManager
import com.cyberfanta.talentviewer.views.cards.OpportunitiesAdapter
import com.cyberfanta.talentviewer.views.cards.PeoplesAdapter
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), android.widget.SearchView.OnQueryTextListener {
    @Suppress("PrivatePropertyName", "unused")
    private val TAG = this::class.java.simpleName

    //To bind view with logical part
    private lateinit var viewBinding: ActivityMainBinding

    //UI variables
    private var authorOpened: Boolean = false
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

        //Obtain the FirebaseAnalytics instance
        FirebaseManager(this)
        FirebaseManager.logEvent("$TAG: Opened", "App_Opened")

        //Obtain rate my app instance
        RateAppManager(this)

        //Set search view listeners
        viewBinding.searchView.setOnQueryTextListener(this)

        //Calculate device dimensions
        deviceDimension = DeviceUtils.calculateDeviceDimensions(this)

        initializeLoadingArrowAnimations()
        fillRecyclerViewBios()
        fillRecyclerViewJobs()
        bindOnClickListener()

        //Loading ads manager
//        AdsManager.loadBannerAds(applicationContext, deviceDimension[0].toFloat())
    }

    /**
     * The initialize the bios recyclerView
     */
    private fun fillRecyclerViewBios() {
        peopleAdapter = PeoplesAdapter (peopleList)
        viewBinding.recyclerViewBios.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerViewBios.adapter = peopleAdapter

        viewBinding.biosLoading.visibility = View.VISIBLE
        getPeoples(PageData(peopleOffset.toString(), querySize.toString(), currentAggregators))
        DeviceUtils.setAnimation(viewBinding.recyclerViewBios, "translationX", 300, false, 0f, -1f * deviceDimension[0])

        peopleAdapter.setOnItemClickListener(object:
            PeoplesAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
//                FirebaseManager.logEvent("Character Detail: " + (position+1) + " - " + queryManager.getCharacterDetail(position+1).name, "Get_Character_Detail")

                //Activating Loading Arrow
                viewBinding.biosLoading.visibility = View.VISIBLE

                peopleList[position].username?.let { getBio(it) }
            }
        })

        peopleAdapter.setOnBottomReachedListener(object:
            PeoplesAdapter.OnBottomReachedListener {
            override fun onBottomReached(position: Int) {
                if (!loadingBios) {
                    //Activating Loading Arrow
                    viewBinding.biosLoading.visibility = View.VISIBLE

//                FirebaseManager.logEvent("Character Page: $characterPagesLoaded", "Get_Character_Page")
                    getPeoples(
                        PageData(
                            peopleOffset.toString(),
                            querySize.toString(),
                            currentAggregators
                        )
                    )
                }
            }
        })

        viewBinding.recyclerViewBios.addOnScrollListener(object:
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!loadingBios) {
                        //Activating Loading Arrow
                        viewBinding.biosLoading.visibility = View.VISIBLE

//                    FirebaseManager.logEvent("Character Page: $characterPagesLoaded", "Get_Character_Page")
                        getPeoples(
                            PageData(
                                peopleOffset.toString(),
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
     * The initialize the jobs recyclerView
     */
    private fun fillRecyclerViewJobs() {
        opportunityAdapter = OpportunitiesAdapter (opportunityList)
        viewBinding.recyclerViewJobs.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerViewJobs.adapter = opportunityAdapter

        viewBinding.jobsLoading.visibility = View.VISIBLE
        getOpportunities(PageData(opportunityOffset.toString(), querySize.toString(), currentAggregators))

        opportunityAdapter.setOnItemClickListener(object:
            OpportunitiesAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
//                FirebaseManager.logEvent("Character Detail: " + (position+1) + " - " + queryManager.getCharacterDetail(position+1).name, "Get_Character_Detail")

                //Activating Loading Arrow
                viewBinding.jobsLoading.visibility = View.VISIBLE

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
        viewBinding.author.setOnClickListener {
            authorSelected(viewBinding.author)
            authorOpened = false
        }
        viewBinding.authorId.setOnClickListener {
            FirebaseManager.logEvent("Sending email: Author", "Send_Email")
            @Suppress("SpellCheckingInspection")
            @SuppressLint("SimpleDateFormat")
            val dateHour = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            DeviceUtils.sendAuthorEmail(
                this,
                "masterjulioleon@gmail.com",
                getString(R.string.app_name) + " --- " + getString(R.string.authorEmailSubject) + " --- " + dateHour,
                getString(R.string.authorEmailBody) + "",
                getString(R.string.authorEmailChooser) + ""
            )
        }
        viewBinding.poweredId.setOnClickListener {
            FirebaseManager.logEvent("Open website: API - " + getString(R.string.poweredByUrl), "Open_Api")
            DeviceUtils.openURL(this, getString(R.string.poweredByUrl))
        }
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

    /**
     * Process the behavior of the app when user press back button
     */
    override fun onBackPressed() {
        val constraintLayout : ConstraintLayout = findViewById(R.id.author)
        if (authorOpened) {
            authorSelected(constraintLayout)
            authorOpened = false

            FirebaseManager.logEvent("Device Button: Back", "Device_Button")
            return
        }

        FirebaseManager.logEvent("$TAG: Closed", "App_Closed")
        super.onBackPressed()
    }

//    /**
//     * Actions made when app start
//     */
//    override fun onStart() {
//        AdsManager.attachBannerAd(viewBinding.adView)
//        super.onStart()
//    }
//
    /**
     * Load people list into bio recycler view
     */
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
                    peopleOffset += querySize
                    peopleAdapter.notifyItemRangeInserted(peopleOffset, querySize)
                } else {
                    showError()
                }
                viewBinding.biosLoading.visibility = View.INVISIBLE
                loadingBios = false
            }
        }
    }

    /**
     * Retrofit implementation to get a people list
     */
    private fun getRetrofitPeoples(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .baseUrl("https://search.torre.co/people/_search/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build()
    }

    /**
     * Load opportunity list into job recycler view
     */
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
                    opportunityAdapter.notifyItemRangeInserted(opportunityOffset, querySize)
                    opportunityOffset += querySize
                } else {
                    showError()
                }
                viewBinding.jobsLoading.visibility = View.INVISIBLE
                loadingJobs = false
            }
        }
    }

    /**
     * Retrofit implementation to get a opportunity list
     */
    private fun getRetrofitOpportunities(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .baseUrl("https://search.torre.co/opportunities/_search/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build()
    }

    /**
     * Open the BioActivity to show the details of a bio
     */
    fun getBio(username: String) {
        val intent = Intent(this, BioActivity::class.java)
        intent.putExtra("deviceWidth", deviceDimension[0].toString())
        intent.putExtra("deviceHeight", deviceDimension[1].toString())
        intent.putExtra("username", username)
        startActivity(intent)

        //Deactivating Loading Arrow
        viewBinding.biosLoading.visibility = View.INVISIBLE
    }

    /**
     * Open the JobActivity to show the details of a job
     */
    fun getJob(id: String) {
        val intent = Intent(this, JobActivity::class.java)
        intent.putExtra("deviceWidth", deviceDimension[0].toString())
        intent.putExtra("deviceHeight", deviceDimension[1].toString())
        intent.putExtra("id", id)
        startActivity(intent)

        //Deactivating Loading Arrow
        viewBinding.jobsLoading.visibility = View.INVISIBLE
    }

    /**
     * Show error message when device have a problem with the internet
     */
    private fun showError() {
        Toast.makeText(this, getString(R.string.error_loading), Toast.LENGTH_SHORT).show()
    }

    /**
     * Called when user clicks on submit button
     */
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

    /**
     * Hide the keyboard after the searchView submit the text
     */
    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(viewBinding.mainLayout.windowToken, 0)
    }

    /**
     * Manage the swap animation when press jobs button
     */
    private fun jobsButtonPressed () {
        if (!opportunitySwitch) {
            DeviceUtils.setAnimation(viewBinding.recyclerViewJobs, "translationX", 300, false, 1f * deviceDimension[0], 0f)
            DeviceUtils.setAnimation(viewBinding.recyclerViewBios, "translationX", 300, false, 0f, -1f * deviceDimension[0])
            opportunitySwitch = true

            //Firebase Data Collection
            FirebaseManager.logEvent("Footer Button: Jobs", "Footer_Button")
        } else {
            YoYo.with(Techniques.Shake)
                .duration(500)
                .playOn(viewBinding.jobsButton)

            //Firebase Data Collection
            FirebaseManager.logEvent("Footer Button: Jobs - Dance", "Footer_Button")
        }
    }

    /**
     * Manage the swap animation when press bios button
     */
    private fun biosButtonPressed () {
        if (opportunitySwitch) {
            DeviceUtils.setAnimation(viewBinding.recyclerViewJobs, "translationX", 300, false, 0f, 1f * deviceDimension[0])
            DeviceUtils.setAnimation(viewBinding.recyclerViewBios, "translationX", 300, false, -1f * deviceDimension[0], 0f)
            opportunitySwitch = false

            //Firebase Data Collection
            FirebaseManager.logEvent("Footer Button: Bios", "Footer_Button")
        } else {
            YoYo.with(Techniques.Shake)
                .duration(500)
                .playOn(viewBinding.biosButton)

            //Firebase Data Collection
            FirebaseManager.logEvent("Footer Button: Bios - Dance", "Footer_Button")
        }
    }

    //Main Menu
    /**
     * Show the developer info
     */
    private fun authorSelected(view: View) {
        DeviceUtils.setAnimation(view, "translationX", 300, false, 0f, deviceDimension[0].toFloat())
    }

    /**
     * Create the setting menu of the application
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    /**
     * Handle the setting menu of the application
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_policy -> {
                FirebaseManager.logEvent("Menu: Policy", "Open_Menu")
                val uri = Uri.parse(getString(R.string.item_policy_page))
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
                return true
            }
            R.id.item_rate -> {
                FirebaseManager.logEvent("Menu: Rate App", "Open_Menu")
                RateAppManager.requestReview(applicationContext)
                return true
            }
            R.id.item_about -> {
                FirebaseManager.logEvent("Menu: Author", "Open_Menu")
                viewBinding.author.visibility = View.VISIBLE
                DeviceUtils.setAnimation(
                    viewBinding.author,
                    "translationX",
                    300,
                    false,
                    deviceDimension[0].toFloat(),
                    0f
                )
                authorOpened = true
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
