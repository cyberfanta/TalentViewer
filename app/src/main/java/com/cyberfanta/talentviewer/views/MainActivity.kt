package com.cyberfanta.talentviewer.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.cyberfanta.talentviewer.R
import com.cyberfanta.talentviewer.databinding.ActivityMainBinding
import com.cyberfanta.talentviewer.models.OpportunityItem
import com.cyberfanta.talentviewer.models.PeopleItem
import com.cyberfanta.talentviewer.presenters.FirebaseManager
import com.cyberfanta.talentviewer.presenters.MainActivityPresenter
import com.cyberfanta.talentviewer.presenters.MainActivityPresenterImpl
import com.cyberfanta.talentviewer.presenters.RateAppManager
import com.cyberfanta.talentviewer.views.cards.OpportunitiesAdapter
import com.cyberfanta.talentviewer.views.cards.PeoplesAdapter
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import java.util.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), android.widget.SearchView.OnQueryTextListener, MenuManager, MainActivityInterface {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG = this::class.java.simpleName

    //To bind view with logical part
    private lateinit var viewBinding: ActivityMainBinding

    //UI variables
    private lateinit var deviceDimension: IntArray
    private var filteringRV = false

    //Menu variables
    override var deviceWidth: Float = 0.0f
    override var authorOpened: Boolean = false
    override lateinit var contextMenu: Context
    override lateinit var viewBindingMenu: ViewBinding

    //Manage RecyclerViews
    private lateinit var peopleAdapter: PeoplesAdapter
    private lateinit var opportunityAdapter: OpportunitiesAdapter
    private var opportunityList = mutableMapOf<String, OpportunityItem>()
    private var peopleList = mutableMapOf<String, PeopleItem>()

    //Manage query sizes
    private var opportunitySwitch = true //true: Opportunity - false: Peoples

    //To control coroutines
    private var loadingJobs = false
    private var loadingBios = false

    //MVP variables
    private lateinit var mainActivityPresenter: MainActivityPresenter

    /**
     * The initial point of this app
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //Set view binding
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        //Calculate device dimensions
        deviceDimension = DeviceUtils.calculateDeviceDimensions(this)

        //Obtain the FirebaseAnalytics instance
        FirebaseManager(this)
        FirebaseManager.logEvent("$TAG: Opened", "App_Opened")

        //Menu Interface
        initializeMenu(this, deviceDimension, viewBinding)

        //Obtain rate my app instance
        RateAppManager(this)

        //Set search view listeners
        viewBinding.searchView.setOnQueryTextListener(this)

        //MVP variables
        mainActivityPresenter = MainActivityPresenterImpl(this)

        initializeLoadingArrowAnimations()
        fillRecyclerViewJobs()
        fillRecyclerViewBios()
        bindOnClickListener()
        initializeRecyclerViewHelpers()
    }

    /**
     * The initialize the jobs recyclerView
     */
    private fun fillRecyclerViewJobs() {
        opportunityAdapter = OpportunitiesAdapter (opportunityList)
        viewBinding.recyclerViewJobs.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerViewJobs.adapter = opportunityAdapter

        getOpportunities()

        opportunityAdapter.setOnItemClickListener(object :
            OpportunitiesAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                //Activating Loading Arrow
                viewBinding.jobsLoading.visibility = View.VISIBLE

                FirebaseManager.logEvent("Job Detail: $position", "Get_Job_Detail")
//                opportunityList.values.elementAt(position).id?.let { getJob(it) } todo
            }
        })

//        opportunityAdapter.setOnBottomReachedListener(object:
//            OpportunitiesAdapter.OnBottomReachedListener {
//            override fun onBottomReached(position: Int) {
////                if (!loadingJobs)
//                    getOpportunities()
//            }
//        })

        viewBinding.recyclerViewJobs.addOnScrollListener(object:
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
//                    if (!loadingJobs)
                        getOpportunities()
                }
            }
        })
    }

    /**
     * The initialize the bios recyclerView
     */
    private fun fillRecyclerViewBios() {
        peopleAdapter = PeoplesAdapter (peopleList)
        viewBinding.recyclerViewBios.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerViewBios.adapter = peopleAdapter

        getPeoples()
        DeviceUtils.setAnimation(viewBinding.recyclerViewBios, "translationX", 300, false, 0f, -1f * deviceDimension[0])

        peopleAdapter.setOnItemClickListener(object:
            PeoplesAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                //Activating Loading Arrow
                viewBinding.biosLoading.visibility = View.VISIBLE

                FirebaseManager.logEvent("Bio Detail: $position", "Get_Bio_Detail")
//                peopleList.values.elementAt(position).username?.let { getBio(it) } todo
            }
        })

//        peopleAdapter.setOnBottomReachedListener(object:
//            PeoplesAdapter.OnBottomReachedListener {
//            override fun onBottomReached(position: Int) {
////                if (!loadingBios)
//                    getPeoples()
//            }
//        })

        viewBinding.recyclerViewBios.addOnScrollListener(object:
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
//                    if (!loadingBios)
                        getPeoples()
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

        bindClickListener(this)
    }

    /**
     * Load all onClick functions for search views helpers on MainActivity
     */
    private fun initializeRecyclerViewHelpers() {
        viewBinding.searchView.setOnClickListener {
            viewBinding.searchView.requestFocus()
            if (opportunitySwitch)
                viewBinding.helperJobs.visibility = View.VISIBLE
            else
                viewBinding.helperBios.visibility = View.VISIBLE
        }

        val helperListBios = resources.getStringArray(R.array.search_view_helper_bios)
        var text = getString(R.string.search_view_helper_message) + " " + helperListBios[0] + ":"
        viewBinding.helperBios1.text = text
        viewBinding.helperBios1.setOnClickListener {
            setHelperList(helperListBios[0])
            viewBinding.helperBios.visibility = View.GONE
            viewBinding.searchView.requestFocus()
        }

        text = getString(R.string.search_view_helper_message) + " " + helperListBios[1] + ":"
        viewBinding.helperBios2.text = text
        viewBinding.helperBios2.setOnClickListener {
            setHelperList(helperListBios[1])
            viewBinding.helperBios.visibility = View.GONE
            viewBinding.searchView.requestFocus()
        }

        text = getString(R.string.search_view_helper_message) + " " + helperListBios[2] + ":"
        viewBinding.helperBios3.text = text
        viewBinding.helperBios3.setOnClickListener {
            setHelperList(helperListBios[2])
            viewBinding.helperBios.visibility = View.GONE
            viewBinding.searchView.requestFocus()
        }

        val helperListJobs = resources.getStringArray(R.array.search_view_helper_jobs)
        text = getString(R.string.search_view_helper_message) + " " + helperListJobs[0] + ":"
        viewBinding.helperJobs1.text = text
        viewBinding.helperJobs1.setOnClickListener {
            setHelperList(helperListJobs[0])
            viewBinding.helperJobs.visibility = View.GONE
            viewBinding.searchView.requestFocus()
        }

        text = getString(R.string.search_view_helper_message) + " " + helperListJobs[1] + ":"
        viewBinding.helperJobs2.text = text
        viewBinding.helperJobs2.setOnClickListener {
            setHelperList(helperListJobs[1])
            viewBinding.helperJobs.visibility = View.GONE
            viewBinding.searchView.requestFocus()
        }

        text = getString(R.string.search_view_helper_message) + " " + helperListJobs[2] + ":"
        viewBinding.helperJobs3.text = text
        viewBinding.helperJobs3.setOnClickListener {
            setHelperList(helperListJobs[2])
            viewBinding.helperJobs.visibility = View.GONE
            viewBinding.searchView.requestFocus()
        }
    }

    /**
     * Set text on search view according to the helper
     */
    private fun setHelperList(helperList: String) {
        val querySplited = viewBinding.searchView.query.split(":")
        if (querySplited.size > 1)
            viewBinding.searchView.setQuery("$helperList:" + querySplited[1], false)
        else
            viewBinding.searchView.setQuery("$helperList:" + querySplited[0], false)
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
        FirebaseManager.logEvent("App Closed", "App_Closed")

        //This way don't wait for Android Garbage collection. However, Android Studio work better with this.
        exitProcess(0)
    }

    /**
     * Process the behavior of the app when user press back button
     */
    override fun onBackPressed() {
        if (backPressed())
            super.onBackPressed()
    }

    /**
     * Open the JobActivity to show the details of a job
     */
    fun getJob(id: String) {
        val intent = Intent(this, JobActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)

        //Deactivating Loading Arrow
        viewBinding.jobsLoading.visibility = View.INVISIBLE
    }

    /**
     * Open the BioActivity to show the details of a bio
     */
    fun getBio(username: String) {
        val intent = Intent(this, BioActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)

        //Deactivating Loading Arrow
        viewBinding.biosLoading.visibility = View.INVISIBLE
    }

    /**
     * Called when user clicks on submit button
     */
    override fun onQueryTextSubmit(query: String?): Boolean {
        hideKeyboard()
        return true
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param query the new content of the query text field.
     *
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener.
     */
    @SuppressLint("NotifyDataSetChanged")
    //TODO: Revisar
    override fun onQueryTextChange(query: String?): Boolean {
        filteringRV = true
        if (!query.isNullOrEmpty()) {
            updateFilteredRV(query)
            return true
        }
        filteringRV = false
        if (opportunitySwitch) {
            opportunityAdapter = OpportunitiesAdapter(opportunityList)
            viewBinding.recyclerViewJobs.adapter = opportunityAdapter
            opportunityAdapter.setOnItemClickListener(object :
                OpportunitiesAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    FirebaseManager.logEvent("Job Detail: $position", "Get_Job_Detail")

                    //Activating Loading Arrow
                    viewBinding.jobsLoading.visibility = View.VISIBLE

                    opportunityList.values.elementAt(position).id?.let { getJob(it) }
                }
            })
            opportunityAdapter.notifyDataSetChanged()
        } else {
            peopleAdapter = PeoplesAdapter(peopleList)
            viewBinding.recyclerViewBios.adapter = peopleAdapter
            peopleAdapter.setOnItemClickListener(object:
                PeoplesAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    FirebaseManager.logEvent("Bio Detail: $position", "Get_Bio_Detail")

                    //Activating Loading Arrow
                    viewBinding.biosLoading.visibility = View.VISIBLE

                    peopleList.values.elementAt(position).username?.let { getBio(it) }
                }
            })
            peopleAdapter.notifyDataSetChanged()
        }
        return true
    }

    /**
     * Update recycler view during searching
     *
     * @param query the new content of the query text field.
     **/
    @SuppressLint("NotifyDataSetChanged")
    //TODO: Revisar
    private fun updateFilteredRV(query: String) {
        if (opportunitySwitch) {
            val filter = mutableMapOf<String, OpportunityItem>()
            for (item in opportunityList.values) {
                val jobsHelper = resources.getStringArray(R.array.search_view_helper_jobs)
                if (query.lowercase(Locale.getDefault()).contains(jobsHelper[0] + ":")) {
                    val querySplited = query.split(jobsHelper[0] + ":").toMutableList()
                    removeFirstChar(querySplited)
                    if (item.objective?.lowercase()
                            ?.contains(querySplited[1].lowercase(Locale.getDefault())) == true
                    )
                        item.objective.let { filter.put(it, item) }
                } else if (query.lowercase(Locale.getDefault()).contains(jobsHelper[1] + ":")) {
                    val querySplited = query.split(jobsHelper[1] + ":").toMutableList()
                    removeFirstChar(querySplited)
                    if (item.organizations?.get(0)?.name?.lowercase()
                            ?.contains(querySplited[1].lowercase(Locale.getDefault())) == true
                    )
                        item.organizations[0]?.name?.let { filter.put(it, item) }
                } else if (query.lowercase(Locale.getDefault()).contains(jobsHelper[2] + ":")) {
                    val querySplited = query.split(jobsHelper[2] + ":").toMutableList()
                    removeFirstChar(querySplited)
                    for (skill in item.skills!!)
                        if (skill?.name?.lowercase()
                                ?.contains(querySplited[1].lowercase(Locale.getDefault())) == true
                        )
                            skill.name.let { filter.put(it, item) }
                } else if ((item.objective?.lowercase()
                        ?.contains(query.lowercase(Locale.getDefault())) == true)
                )
                    item.objective.let { filter.put(it, item) }
            }
            opportunityAdapter = OpportunitiesAdapter(filter)
            viewBinding.recyclerViewJobs.adapter = opportunityAdapter
            opportunityAdapter.setOnItemClickListener(object :
                OpportunitiesAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    FirebaseManager.logEvent("Job Detail: $position", "Get_Job_Detail")

                    //Activating Loading Arrow
                    viewBinding.jobsLoading.visibility = View.VISIBLE

                    filter.values.elementAt(position).id?.let { getJob(it) }
                }
            })
            opportunityAdapter.notifyDataSetChanged()
        } else {
            val filter = mutableMapOf<String, PeopleItem>()
            for (item in peopleList.values) {
                val biosHelper = resources.getStringArray(R.array.search_view_helper_bios)
                if (query.lowercase(Locale.getDefault()).contains(biosHelper[0] + ":")) {
                    val querySplited = query.split(biosHelper[0] + ":").toMutableList()
                    removeFirstChar(querySplited)
                    if (item.name?.lowercase()
                            ?.contains(querySplited[1].lowercase(Locale.getDefault())) == true
                    )
                        item.name.let { filter.put(it, item) }
                } else if (query.lowercase(Locale.getDefault()).contains(biosHelper[1] + ":")) {
                    val querySplited = query.split(biosHelper[1] + ":").toMutableList()
                    removeFirstChar(querySplited)
                    if (item.professionalHeadline?.lowercase()
                            ?.contains(querySplited[1].lowercase(Locale.getDefault())) == true
                    )
                        item.professionalHeadline.let { filter.put(it, item) }
                } else if (query.lowercase(Locale.getDefault()).contains(biosHelper[2] + ":")) {
                    val querySplited = query.split(biosHelper[2] + ":").toMutableList()
                    removeFirstChar(querySplited)
                    if (item.username?.lowercase()
                            ?.contains(querySplited[1].lowercase(Locale.getDefault())) == true
                    )
                        item.username.let { filter.put(it, item) }
                } else if ((item.name?.lowercase()
                        ?.contains(query.lowercase(Locale.getDefault())) == true)
                )
                    item.name.let { filter.put(it, item) }
            }
            peopleAdapter = PeoplesAdapter(filter)
            viewBinding.recyclerViewBios.adapter = peopleAdapter
            peopleAdapter.setOnItemClickListener(object :
                PeoplesAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    FirebaseManager.logEvent("Bio Detail: $position", "Get_Bio_Detail")

                    //Activating Loading Arrow
                    viewBinding.biosLoading.visibility = View.VISIBLE

                    filter.values.elementAt(position).username?.let { getBio(it) }
                }
            })
            peopleAdapter.notifyDataSetChanged()
        }
    }

    /**
     * Remove start characters from query if this is a space character
     */
    private fun removeFirstChar(querySplited: MutableList<String>) {
        if (querySplited[1].isNotEmpty())
            querySplited[1] = querySplited[1].trimStart()
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

            if (viewBinding.helperBios.isVisible) {
                viewBinding.helperBios.visibility = View.GONE
                viewBinding.helperJobs.visibility = View.VISIBLE
            }

            //Firebase Data Collection
            FirebaseManager.logEvent("Footer Button: Jobs", "Footer_Button")
        } else {
            YoYo.with(Techniques.Shake)
                .duration(500)
                .playOn(viewBinding.jobsButton)

            DeviceUtils.showToast(this, getString(R.string.error_you_are_in_job))

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

            if (viewBinding.helperJobs.isVisible) {
                viewBinding.helperJobs.visibility = View.GONE
                viewBinding.helperBios.visibility = View.VISIBLE
            }

            //Firebase Data Collection
            FirebaseManager.logEvent("Footer Button: Bios", "Footer_Button")
        } else {
            YoYo.with(Techniques.Shake)
                .duration(500)
                .playOn(viewBinding.biosButton)

            DeviceUtils.showToast(this, getString(R.string.error_you_are_in_bio))

            //Firebase Data Collection
            FirebaseManager.logEvent("Footer Button: Bios - Dance", "Footer_Button")
        }
    }

    /**
     * Create the setting menu of the application
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return createOptionsMenu(menu, menuInflater)
    }

    /**
     * Handle the setting menu of the application
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        optionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    /**
     * Update recyclerViewJobs
     */
    override fun showOpportunities(opportunityList: Map<String, OpportunityItem>) {
        val current = this.opportunityList.size
        this.opportunityList.clear()
        this.opportunityList.putAll(opportunityList)
        opportunityAdapter.notifyItemRangeInserted(current, this.opportunityList.size - current)
        viewBinding.jobsLoading.visibility = View.INVISIBLE
    }

    /**
     * Update recyclerViewBios
     */
    override fun showPeoples(peopleList: Map<String, PeopleItem>) {
        val current = this.peopleList.size
        this.peopleList.clear()
        this.peopleList.putAll(peopleList)
        peopleAdapter.notifyItemRangeInserted(current, this.peopleList.size - current)
        viewBinding.biosLoading.visibility = View.INVISIBLE
    }

    /**
     * Show error message when fail loading Opportunities
     */
    override fun errorLoadingOpportunities() {
        DeviceUtils.showToast(this, getString(R.string.error_loading_jobs))
    }

    /**
     * Show error message when fail loading Peoples
     */
    override fun errorLoadingPeoples() {
        DeviceUtils.showToast(this, getString(R.string.error_loading_bios))
    }

    /**
     * Manege the obtain of a Opportunities
     */
    override fun getOpportunities() {
        //Activating Loading Arrow
        viewBinding.jobsLoading.visibility = View.VISIBLE

        FirebaseManager.logEvent("Opportunity Page", "Get_Opportunity_Page")
        mainActivityPresenter.getOpportunities()
    }

    /**
     * Manege the obtain of Peoples
     */
    override fun getPeoples() {
        //Activating Loading Arrow
        viewBinding.biosLoading.visibility = View.VISIBLE

        FirebaseManager.logEvent("People Page", "Get_People_Page")
        mainActivityPresenter.getPeoples()
    }

}
