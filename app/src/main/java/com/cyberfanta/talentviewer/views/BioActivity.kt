package com.cyberfanta.talentviewer.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.viewbinding.ViewBinding
import com.cyberfanta.talentviewer.R
import com.cyberfanta.talentviewer.databinding.ActivityBioBinding
import com.cyberfanta.talentviewer.models.*
import com.cyberfanta.talentviewer.presenters.*
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.Utils
import com.squareup.picasso.Picasso
import java.util.*

class BioActivity : AppCompatActivity(), MenuManager, BioActivityInterface {
    @Suppress("PrivatePropertyName", "unused")
    private val TAG = this::class.java.simpleName

    //To bind view with logical part
    private lateinit var viewBinding: ActivityBioBinding

    //UI variables
    private var deviceDimension = intArrayOf(0, 0)
    private var username = ""

    //Menu variables
    override var deviceWidth: Float = 0.0f
    override var authorOpened: Boolean = false
    override lateinit var contextMenu: Context
    override lateinit var viewBindingMenu: ViewBinding

    //MVP variables
    private lateinit var bioActivityPresenter: BioActivityPresenter

    /**
     * The initial point of this activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBioBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        DeviceUtils.setAnimation(viewBinding.loading, "rotation", 1000, true, 0f, 360f)

        getDeviceDimensions()
        setRandomKenBurnsBackground()

        //Load firebase manager
        FirebaseManager.logEvent("$TAG: Opened", "Activity_Bio")

        //Manu Interface
        initializeMenu(this, deviceDimension, viewBinding)
        bindClickListener(this)

        //MVP variables
        bioActivityPresenter = BioActivityPresenterImpl(this)

        getBio()
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
        username = intent.getStringExtra("username")!!
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
    override fun showBio(bio: Bios) {
        bio.person?.let { showPicture(bio, it) }
        bio.person?.name?.let { showName(bio, it) }
        bio.person?.professionalHeadline?.let { showProfessional(bio, it) }
        bio.person?.let { showSummary(bio, it) }
        bio.person?.let { bio.person.links?.let { it1 -> showLinks(bio, it, it1) } }
        bio.strengths?.let { showStrengths(bio, it) }
        bio.interests?.let { showInterests(bio, it) }
        bio.jobs?.let { showJobs(bio, it) }
        bio.personalityTraitsResults?.let { showPersonalityTraits(bio, it) }
        bio.languages?.let { showLanguages(bio, it) }

        viewBinding.applyNowButton.setOnClickListener {
            DeviceUtils.openURL(this@BioActivity,
                "https://torre.co/en/$username"
            )
        }
    }

    /**
     * Update person data
     */
    private fun showPicture(bio: Bios, person: Person2) {
        bio.person?.picture?.let {
            Picasso.get().load(person.picture)
                .into(viewBinding.picture)
        }
    }

    /**
     * Update name data
     */
    private fun showName(bio: Bios, name: String) {
        bio.person?.name?.let {
            viewBinding.name.text =
                name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
        }
    }

    /**
     * Update professionalHeadline data
     */
    private fun showProfessional(bio: Bios, professionalHeadline: String) {
        bio.person?.professionalHeadline?.let {
            viewBinding.professionalHeadline.text = professionalHeadline.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        }
    }

    /**
     * Update summary data
     */
    private fun showSummary(bio: Bios, person: Person2) {
        bio.person?.summaryOfBio?.let {
            var textView = TextView(
                ContextThemeWrapper(
                    this@BioActivity,
                    R.style.detail_scrollview_container_top
                )
            )
            textView.text = getString(R.string.detail_summaryOfBio)
            viewBinding.dataShower.addView(textView)

            textView = TextView(
                ContextThemeWrapper(
                    this@BioActivity,
                    R.style.detail_scrollview_container_middle
                )
            )
            val text = "• " + person.summaryOfBio
            textView.text = text
            viewBinding.dataShower.addView(textView)
        }
    }

    /**
     * Update links data
     */
    private fun showLinks(bio: Bios, person: Person2, links: List<LinksItem?>) {
        bio.person?.links?.let {
            val textView: TextView = if (person.summaryOfBio == null)
                TextView(
                    ContextThemeWrapper(
                        this@BioActivity,
                        R.style.detail_scrollview_container_top
                    )
                )
            else
                TextView(
                    ContextThemeWrapper(
                        this@BioActivity,
                        R.style.detail_scrollview_container_middle_dark
                    )
                )

            textView.text = getString(R.string.detail_contact)
            viewBinding.dataShower.addView(textView)

            val scrollView = ScrollView(
                ContextThemeWrapper(
                    this@BioActivity,
                    R.style.detail_scrollview_container_middle
                )
            )

            val linearLayout = LinearLayout(this@BioActivity)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.layoutParams =
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

            @SuppressLint("UseCompatLoadingForDrawables")
            for (link in links) {
                val imageView = ImageView(this@BioActivity)
                when (link?.name) {
                    "facebook" -> imageView.setImageResource(R.drawable.ic_facebook)
                    "github" -> imageView.setImageResource(R.drawable.ic_github)
                    "gitlab" -> imageView.setImageResource(R.drawable.ic_gitlab)
                    "instagram" -> imageView.setImageResource(R.drawable.ic_instagram)
                    "linkedin" -> imageView.setImageResource(R.drawable.ic_linkedin)
                    "medium" -> imageView.setImageResource(R.drawable.ic_medium)
                    "twitter" -> imageView.setImageResource(R.drawable.ic_twitter)
                    else -> imageView.setImageResource(R.drawable.ic_www)
                }
                val layoutParams = LinearLayout.LayoutParams(
                    DeviceUtils.convertDpToPx(this@BioActivity, 50f),
                    DeviceUtils.convertDpToPx(this@BioActivity, 50f)
                )
                layoutParams.setMargins(0, 0, DeviceUtils.convertDpToPx(this@BioActivity, 8f), 0)
                imageView.layoutParams = layoutParams

                link?.address?.let {
                    imageView.setOnClickListener {
                        try {
                            DeviceUtils.openURL(this@BioActivity, link.address)
                        } catch (exception: Exception) {
                            Toast.makeText(
                                this@BioActivity,
                                getString(R.string.error_loading),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                linearLayout.addView(imageView)
            }

            scrollView.addView(linearLayout)
            viewBinding.dataShower.addView(scrollView)
        }
    }

    /**
     * Update strengths data
     */
    private fun showStrengths(bio: Bios, strengths: List<StrengthsItem2?>) {
        bio.strengths?.let {
            var textView = TextView(
                ContextThemeWrapper(
                    this@BioActivity,
                    R.style.detail_scrollview_container_middle_dark
                )
            )
            textView.text = getString(R.string.detail_strengths_bio)
            viewBinding.dataShower.addView(textView)

            textView = TextView(
                ContextThemeWrapper(
                    this@BioActivity,
                    R.style.detail_scrollview_container_middle
                )
            )
            var text = ""
            for (detail in strengths)
                text += detail?.name + "\n"
            if (text.length > 1)
                textView.text = text.substring(0, text.length - 1)
            viewBinding.dataShower.addView(textView)
        }
    }

    /**
     * Update interests data
     */
    private fun showInterests(bio: Bios, interests: List<InterestsItem?>) {
        bio.interests?.let {
            var textView = TextView(
                ContextThemeWrapper(
                    this@BioActivity,
                    R.style.detail_scrollview_container_middle_dark
                )
            )
            textView.text = getString(R.string.detail_interests)
            viewBinding.dataShower.addView(textView)

            textView = TextView(
                ContextThemeWrapper(
                    this@BioActivity,
                    R.style.detail_scrollview_container_middle
                )
            )
            var text = ""
            for (detail in interests)
                text += detail?.name + "\n"
            if (text.length > 1)
                textView.text = text.substring(0, text.length - 1)
            viewBinding.dataShower.addView(textView)
        }
    }

    /**
     * Update jobs data
     */
    private fun showJobs(bio: Bios, jobs: List<JobsItem?>) {
        bio.jobs?.let {
            var textView = TextView(
                ContextThemeWrapper(
                    this@BioActivity,
                    R.style.detail_scrollview_container_middle_dark
                )
            )
            textView.text = getString(R.string.detail_jobs)
            viewBinding.dataShower.addView(textView)

            textView = TextView(
                ContextThemeWrapper(
                    this@BioActivity,
                    R.style.detail_scrollview_container_middle
                )
            )
            var text = ""
            var counter = 1
            for (detail in jobs) {
                text += "($counter)\n"
                text += getString(R.string.detail_jobs_name) + ": " + detail?.name + "\n"

                text += getString(R.string.detail_jobs_organizations) + ":\n"
                for (organization in detail?.organizations!!)
                    text += organization?.name + "\n"

                detail.responsibilities?.let {
                    text += getString(R.string.detail_jobs_responsibilities) + ":\n"
                    for (responsibilitie in detail.responsibilities)
                        text += "$responsibilitie\n"
                }

                detail.fromMonth?.let { text += getString(R.string.detail_jobs_from) + ": " + detail.fromMonth + " " + detail.fromYear + "\n" }
                detail.toMonth?.let { text += getString(R.string.detail_jobs_to) + ": " + detail.toMonth + " " + detail.toYear + "\n" }

                text += "\n"
                counter++
            }
            if (text.length > 1)
                textView.text = text.substring(0, text.length - 2)
            viewBinding.dataShower.addView(textView)
        }
    }

    /**
     * Update personalityTraits data
     */
    private fun showPersonalityTraits(bio: Bios, personalityTraitsResults: PersonalityTraitsResults) {
        bio.personalityTraitsResults?.let {
            var textView = TextView(
                ContextThemeWrapper(
                    this@BioActivity,
                    R.style.detail_scrollview_container_middle_dark
                )
            )
            textView.text = getString(R.string.detail_personality_traits_results)
            viewBinding.dataShower.addView(textView)

            val personalityTraitList: ArrayList<PersonalityTraitsData> = ArrayList()
            for (detail in personalityTraitsResults.groups!!) {
                val personalityTrait = PersonalityTraitsData(
                    detail?.id,
                    detail?.order,
                    detail?.median,
                    detail?.stddev,
                    null,
                    ""
                )
                personalityTraitList.add(personalityTrait)
            }
            for ((i, detail) in personalityTraitsResults.analyses!!.withIndex()) {
                if (i > 5) break
                when (detail?.groupId) {
                    "extraversion" -> personalityTraitList[i].analysis = detail.analysis
                    "openness-to-experience" -> personalityTraitList[i].analysis = detail.analysis
                    "conscientiousness" -> personalityTraitList[i].analysis = detail.analysis
                    "agreeableness" -> personalityTraitList[i].analysis = detail.analysis
                    "honesty-humility" -> personalityTraitList[i].analysis = detail.analysis
                    "emotionality" -> personalityTraitList[i].analysis = detail.analysis
                    "altruism" -> personalityTraitList[i].analysis = detail.analysis
                }
                personalityTraitList[i].group = detail?.groupId
            }

            Utils.init(this@BioActivity)
            val chartLinearLayout = LinearLayout(
                ContextThemeWrapper(
                    this@BioActivity,
                    R.style.detail_scrollview_container_middle
                )
            )
            chartLinearLayout.orientation = LinearLayout.VERTICAL
            chartLinearLayout.layoutParams =
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

            val sideItemWeight = 1.8f
            val middleItemWeight = 6.4f
            val itemWeightSum = 10f

            val detailPersonalityTraitsSideStart =
                resources.getStringArray(R.array.detail_personality_traits_side_start)
            val detailPersonalityTraitsSideEnd =
                resources.getStringArray(R.array.detail_personality_traits_side_end)

            for (i in 0..5) {
                val barLinearLayout = LinearLayout(this@BioActivity)
                barLinearLayout.orientation = LinearLayout.HORIZONTAL
                barLinearLayout.layoutParams =
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                barLinearLayout.weightSum = itemWeightSum

                textView = TextView(
                    ContextThemeWrapper(
                        this@BioActivity,
                        R.style.detail_chart_text_sides
                    )
                )
                textView.layoutParams =
                    LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        sideItemWeight
                    )
                textView.text = detailPersonalityTraitsSideStart[i]
                barLinearLayout.addView(textView)

                val entry: ArrayList<BarEntry> = ArrayList()
                personalityTraitList[i].analysis?.let { it1 ->
                    BarEntry(0f, it1)
                }?.let { it2 ->
                    entry.add(it2)
                }

                val datset =
                    BarDataSet(entry, getString(R.string.detail_personality_traits_analysis))
                datset.valueTextSize = 10f

                @RequiresApi(Build.VERSION_CODES.M)
                datset.color = getColor(R.color.black_torre)

                val data = BarData(datset)

                val chart = HorizontalBarChart(this@BioActivity)
                chart.layoutParams =
                    LinearLayout.LayoutParams(
                        0,
                        DeviceUtils.convertDpToPx(this@BioActivity, 60f),
                        middleItemWeight
                    )
                chart.data = data
                chart.description.isEnabled = false
                chart.xAxis.setDrawLabels(false)
                chart.xAxis.setDrawGridLines(false)
                chart.axisLeft.setDrawLabels(false)
                chart.axisRight.axisMinimum = 0f
                chart.axisRight.axisMaximum = 5.83f
                chart.axisLeft.axisMinimum = 0f
                chart.axisLeft.axisMaximum = 5.83f
                chart.legend.isEnabled = false
                chart.setScaleEnabled(false)
                chart.invalidate()

                barLinearLayout.addView(chart)

                textView = TextView(
                    ContextThemeWrapper(
                        this@BioActivity,
                        R.style.detail_chart_text_sides
                    )
                )
                textView.layoutParams =
                    LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        sideItemWeight
                    )
                textView.text = detailPersonalityTraitsSideEnd[i]
                barLinearLayout.addView(textView)


                chartLinearLayout.addView(barLinearLayout)
            }

            viewBinding.dataShower.addView(chartLinearLayout)
        }
    }

    /**
     * Update languages data
     */
    private fun showLanguages(bio: Bios, languages: List<LanguagesItem2?>) {
        bio.languages?.let {
            var textView = TextView(
                ContextThemeWrapper(
                    this@BioActivity,
                    R.style.detail_scrollview_container_middle_dark
                )
            )
            textView.text = getString(R.string.detail_languages)
            viewBinding.dataShower.addView(textView)

            textView = TextView(
                ContextThemeWrapper(
                    this@BioActivity,
                    R.style.detail_scrollview_container_bottom
                )
            )
            var text = ""
            for (detail in languages)
                text += detail?.language + ": " + (detail?.fluency
                    ?: getString(R.string.detail_languages_null)) + "\n"
            if (text.length > 1)
                textView.text = text.substring(0, text.length - 1)
            viewBinding.dataShower.addView(textView)
        }
    }

    /**
     * Show error message when fail loading data
     */
    override fun errorLoadingBio() {
        DeviceUtils.showToast(this, getString(R.string.error_loading_bio))
    }

    /**
     * Manege the obtain data
     */
    override fun getBio() {
        bioActivityPresenter.getBio(username)
    }
}