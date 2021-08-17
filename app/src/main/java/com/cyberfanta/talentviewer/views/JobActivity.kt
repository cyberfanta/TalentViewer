package com.cyberfanta.talentviewer.views

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.viewbinding.ViewBinding
import com.cyberfanta.talentviewer.R
import com.cyberfanta.talentviewer.databinding.ActivityJobBinding
import com.cyberfanta.talentviewer.models.*
import com.cyberfanta.talentviewer.presenters.FirebaseManager
import com.cyberfanta.talentviewer.presenters.JobActivityPresenter
import com.cyberfanta.talentviewer.presenters.JobActivityPresenterImpl
import com.squareup.picasso.Picasso
import java.util.*

class JobActivity : AppCompatActivity(), MenuManager, JobActivityInterface {
    @Suppress("PrivatePropertyName", "unused")
    private val TAG = this::class.java.simpleName

    //To bind view with logical part
    private lateinit var viewBinding: ActivityJobBinding

    //UI variables
    private var deviceDimension = intArrayOf(0, 0)
    private lateinit var id : String

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

        //Load firebase manager
        FirebaseManager.logEvent("$TAG: Opened", "Activity_Job")

        //Menu Interface
        initializeMenu(this, deviceDimension, viewBinding)
        bindClickListener(this)

        //MVP variables
        jobActivityPresenter = JobActivityPresenterImpl(this)

        getJob()
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
        job.organizations?.let { showPicture(job, it) }
        job.objective?.let { showObjective(job, it) }
        job.organizations?.let { showOrganizations(job, it) }

        job.compensation?.minAmount?.let {
            job.compensation.visible?.let { it1 ->
                job.compensation.maxAmount?.let { it2 ->
                    showCompensation(job, it1, job.compensation, it, it2)
                }
            }
        }

        job.details?.let { showDetails(job, it) }
        job.strengths?.let { showStrengths(job, it) }
        job.languages?.let { showLanguages(job, it) }
        job.place?.remote?.let { job.commitment?.let { it1 -> showExtras(job, it, it1) } }

        viewBinding.applyNowButton.setOnClickListener {
            DeviceUtils.openURL(this@JobActivity,
                "https://torre.co/jobs/$id"
            )
        }

        //Deactivating Loading Arrow
        viewBinding.loading.visibility = View.INVISIBLE
    }

    /**
     * Update picture data
     */
    private fun showPicture(job: Jobs, organizations: List<OrganizationsItem?>) {
        job.organizations?.get(0)?.picture?.let {
            Picasso.get().isLoggingEnabled = true
            Picasso.get().load(organizations[0]?.picture)
                .into(viewBinding.picture)
        }
    }

    /**
     * Update objective data
     */
    private fun showObjective(job: Jobs, objective: String) {
        job.objective?.let {
            viewBinding.objective.text =
                objective.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
        }
    }

    /**
     * Update organizations data
     */
    private fun showOrganizations(job: Jobs, organizations: List<OrganizationsItem?>) {
        job.organizations?.get(0)?.name?.let {
            viewBinding.organizationsName.text =
                organizations[0]?.name?.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
        }
    }

    /**
     * Update compensation data
     */
    private fun showCompensation(job: Jobs, visible: Boolean, compensation: Compensation, minAmount: Double, maxAmount: Double) {
        var string = getString(R.string.card_compensation_hidden)
        job.compensation?.visible?.let {
            if (visible) {
                string = ""
                compensation.minAmount?.let { string += minAmount.toString() + " " + compensation.currency }
                compensation.maxAmount?.let {
                    if (maxAmount > 0)
                        string += " - " + maxAmount.toString() + " " + compensation.currency
                }
                compensation.periodicity?.let { string += " " + compensation.periodicity }
            }
        }
        viewBinding.compensation.text = string.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }

    /**
     * Update details data
     */
    private fun showDetails(job: Jobs, details: List<DetailsItem?>) {
        job.details?.let {
            var textView = TextView(
                ContextThemeWrapper(
                    this@JobActivity,
                    R.style.detail_scrollview_container_top
                )
            )
            textView.text = getString(R.string.detail_details)
            viewBinding.dataShower.addView(textView)

            textView = TextView(
                ContextThemeWrapper(
                    this@JobActivity,
                    R.style.detail_scrollview_container_middle
                )
            )
            var text = ""
            for (detail in details)
                text += detail?.content + "\n"
            if (text.length > 1)
                textView.text = text.substring(0, text.length - 1)
            viewBinding.dataShower.addView(textView)
        }
    }

    /**
     * Update strengths data
     */
    private fun showStrengths(job: Jobs, strengths: List<StrengthsItem?>) {
        job.strengths?.let {
            var textView = TextView(
                ContextThemeWrapper(
                    this@JobActivity,
                    R.style.detail_scrollview_container_middle_dark
                )
            )
            textView.text = getString(R.string.detail_strengths)
            viewBinding.dataShower.addView(textView)

            textView = TextView(
                ContextThemeWrapper(
                    this@JobActivity,
                    R.style.detail_scrollview_container_middle
                )
            )
            var text = ""
            for (detail in strengths)
                text += "• " + detail?.name + ": " + detail?.experience + "\n"
            if (text.length > 1)
                textView.text = text.substring(0, text.length - 1)
            viewBinding.dataShower.addView(textView)
        }
    }

    /**
     * Update languages data
     */
    private fun showLanguages(job: Jobs, languages: List<LanguagesItem?>) {
        job.languages?.let {
            var textView = TextView(
                ContextThemeWrapper(
                    this@JobActivity,
                    R.style.detail_scrollview_container_middle_dark
                )
            )
            textView.text = getString(R.string.detail_languages)
            viewBinding.dataShower.addView(textView)

            textView = TextView(
                ContextThemeWrapper(
                    this@JobActivity,
                    R.style.detail_scrollview_container_middle
                )
            )
            var text = ""
            for (detail in languages)
                text += "• " + detail?.language?.name + ": " + detail?.fluency + "\n"
            if (text.length > 1)
                textView.text = text.substring(0, text.length - 1)
            viewBinding.dataShower.addView(textView)
        }
    }

    /**
     * Update extra data
     */
    private fun showExtras(job: Jobs, remote: Boolean, commitment: Commitment) {
        if (job.place?.remote != null || job.commitment?.code != null) {
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
            if (job.place?.remote != null) {
                if (!remote) {
                    text += "No "
                }
                text += getString(R.string.detail_remote) + "\n• "
            }

            if (job.commitment?.code != null)
                text += commitment.code
            else
                text = text.substring(0, text.length - 3)

            if (text.length > 2)
                textView.text = text
            viewBinding.dataShower.addView(textView)
        }
    }

    /**
     * Show error message when fail loading data
     */
    override fun errorLoadingJob() {
        DeviceUtils.showToast(this, getString(R.string.error_loading_job))
        viewBinding.loading.visibility = View.INVISIBLE
    }

    /**
     * Manege the obtain data
     */
    override fun getJob() {
        val job = jobActivityPresenter.getJob(id)
        if (job != null) {
            showJob(job)
        }
    }
}