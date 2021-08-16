package com.cyberfanta.talentviewer.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.viewbinding.ViewBinding
import com.cyberfanta.talentviewer.R
import com.cyberfanta.talentviewer.databinding.ActivityJobBinding
import com.cyberfanta.talentviewer.models.APIAdapter
import com.cyberfanta.talentviewer.models.APIService
import com.cyberfanta.talentviewer.models.Jobs
import com.cyberfanta.talentviewer.presenters.FirebaseManager
import com.cyberfanta.talentviewer.presenters.JobActivityPresenter
import com.cyberfanta.talentviewer.presenters.JobActivityPresenterImpl
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class JobActivity : AppCompatActivity(), MenuManager, JobActivityInterface {
    @Suppress("PrivatePropertyName", "unused")
    private val TAG = this::class.java.simpleName

    //To bind view with logical part
    private lateinit var viewBinding: ActivityJobBinding

    //UI variables
    private var deviceDimension = intArrayOf(0, 0)
    private var id = ""

    //Menu variables
    override var deviceWidth: Float = 0.0f
    override var authorOpened: Boolean = false
    override lateinit var contextMenu: Context
    override lateinit var viewBindingMenu: ViewBinding

    //MVP variables
    private lateinit var jobActivityPresenter: JobActivityPresenter

    /**
     * The initial point of this activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityJobBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        DeviceUtils.setAnimation(viewBinding.loading, "rotation", 1000, true, 0f, 360f)

        getDeviceDimensions()
        setRandomKenBurnsBackground()
        bindOnClickListener()

        //Load firebase manager
        FirebaseManager.logEvent("$TAG: Opened", "Activity_Job")

        //Manu Interface
        initializeMenu(this, deviceDimension, viewBinding)

        //MVP variables
        jobActivityPresenter = JobActivityPresenterImpl(this)

        loadObject()
    }

    /**
     * Load all onClick functions for all views on MainActivity
     */
    private fun bindOnClickListener() {
        bindClickListener(this)
    }

    /**
     * Process the behavior of the app when user press back button
     */
    override fun onBackPressed() {
        if (backPressed())
            super.onBackPressed()
    }

    /**
     * Get the device dimension
     */
    private fun getDeviceDimensions(){
        deviceDimension = DeviceUtils.getDeviceDimensions()
        id = intent.getStringExtra("id")!!
    }

    /**
     * Set a random background each run
     */
    private fun setRandomKenBurnsBackground(){
        when (DeviceUtils.getRandomNumber(1, 7)) {
            1 -> viewBinding.pictureBackground.setImageResource(R.drawable.background_1)
            2 -> viewBinding.pictureBackground.setImageResource(R.drawable.background_2)
            3 -> viewBinding.pictureBackground.setImageResource(R.drawable.background_3)
            4 -> viewBinding.pictureBackground.setImageResource(R.drawable.background_4)
            5 -> viewBinding.pictureBackground.setImageResource(R.drawable.background_5)
            6 -> viewBinding.pictureBackground.setImageResource(R.drawable.background_6)
            7 -> viewBinding.pictureBackground.setImageResource(R.drawable.background_7)
        }
    }

    /**
     * Load the view data
     */
    private fun loadObject() {
        CoroutineScope(Dispatchers.IO).launch {
            val call : Response<Jobs> = APIAdapter.getRetrofitJob().create(APIService::class.java).getJob(id)
            val response : Jobs? = call.body()
            runOnUiThread {
                if (call.isSuccessful) {
                    response?.organizations?.get(0)?.picture?.let {
                        Picasso.get().load(response.organizations[0]?.picture)
                        .into(viewBinding.picture)
                    }
                    response?.objective?.let{ viewBinding.objective.text =
                        response.objective.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        }
                    }
                    response?.organizations?.get(0)?.name?.let{ viewBinding.organizationsName.text =
                        response.organizations[0]?.name?.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        }
                    }

                    var string = getString(R.string.card_compensation_hidden)
                    response?.compensation?.visible?.let{
                        if (response.compensation.visible) {
                            string = ""
                            response.compensation.minAmount?.let { string += response.compensation.minAmount.toString() + " " + response.compensation.currency }
                            response.compensation.maxAmount?.let {
                                if (response.compensation.maxAmount > 0)
                                    string += " - " + response.compensation.maxAmount.toString() + " " + response.compensation.currency
                            }
                            response.compensation.periodicity?.let { string += " " + response.compensation.periodicity }
                        }
                    }
                    viewBinding.compensation.text = string.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    }

                    response?.details?.let {
                        var textView = TextView (ContextThemeWrapper(this@JobActivity, R.style.detail_scrollview_container_top))
                        textView.text = getString(R.string.detail_details)
                        viewBinding.dataShower.addView(textView)

                        textView = TextView (ContextThemeWrapper(this@JobActivity, R.style.detail_scrollview_container_middle))
                        var text = ""
                        for (detail in response.details)
                            text += detail?.content + "\n"
                        if (text.length > 1)
                            textView.text = text.substring(0, text.length - 1)
                        viewBinding.dataShower.addView(textView)
                    }

                    response?.strengths?.let {
                        var textView = TextView (ContextThemeWrapper(this@JobActivity, R.style.detail_scrollview_container_middle_dark))
                        textView.text = getString(R.string.detail_strengths)
                        viewBinding.dataShower.addView(textView)

                        textView = TextView (ContextThemeWrapper(this@JobActivity, R.style.detail_scrollview_container_middle))
                        var text = ""
                        for (detail in response.strengths)
                            text += "• " + detail?.name + ": " + detail?.experience + "\n"
                        if (text.length > 1)
                            textView.text = text.substring(0, text.length - 1)
                        viewBinding.dataShower.addView(textView)
                    }

                    response?.languages?.let {
                        var textView = TextView (ContextThemeWrapper(this@JobActivity, R.style.detail_scrollview_container_middle_dark))
                        textView.text = getString(R.string.detail_languages)
                        viewBinding.dataShower.addView(textView)

                        textView = TextView (ContextThemeWrapper(this@JobActivity, R.style.detail_scrollview_container_middle))
                        var text = ""
                        for (detail in response.languages)
                            text += "• " + detail?.language?.name + ": " + detail?.fluency + "\n"
                        if (text.length > 1)
                            textView.text = text.substring(0, text.length - 1)
                        viewBinding.dataShower.addView(textView)
                    }

                    if (response?.place?.remote != null || response?.commitment?.code != null) {
                        var textView = TextView(
                            ContextThemeWrapper(
                                this@JobActivity,
                                R.style.detail_scrollview_container_middle_dark
                            )
                        )
                        textView.text = getString(R.string.detail_others)
                        viewBinding.dataShower.addView(textView)

                        textView = TextView(
                            ContextThemeWrapper(
                                this@JobActivity,
                                R.style.detail_scrollview_container_bottom
                            )
                        )
                        var text = "• "
                        if (response.place?.remote != null) {
                            if (!response.place.remote) {
                                text += "No "
                            }
                            text += getString(R.string.detail_remote) +  "\n• "
                        }

                        if (response.commitment?.code != null)
                            text += response.commitment.code
                        else
                            text = text.substring(0, text.length - 3)

                        if (text.length > 2)
                            textView.text = text
                        viewBinding.dataShower.addView(textView)
                    }

                    viewBinding.applyNowButton.setOnClickListener {
                        DeviceUtils.openURL(this@JobActivity,
                            "https://torre.co/jobs/$id"
                        )
                    }
                } else {
                    showError()
                }

                //Deactivating Loading Arrow
                viewBinding.loading.visibility = View.INVISIBLE
            }
        }
    }

    /**
     * Show error message when device have a problem with the internet
     */
    private fun showError() {
        Toast.makeText(this, getString(R.string.error_loading), Toast.LENGTH_SHORT).show()
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
     * Update activity data
     */
    override fun showJob(job: Jobs) {
        TODO("Not yet implemented")
    }

    /**
     * Show error message when fail loading data
     */
    override fun errorLoadingJob() {
        DeviceUtils.showToast(this, getString(R.string.error_loading_job))
    }

    /**
     * Manege the obtain data
     */
    override fun getJob() {
        jobActivityPresenter.getJob()
    }
}