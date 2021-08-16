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
import com.cyberfanta.talentviewer.models.APIService
import com.cyberfanta.talentviewer.models.Bios
import com.cyberfanta.talentviewer.models.APIAdapter
import com.cyberfanta.talentviewer.presenters.FirebaseManager
import com.cyberfanta.talentviewer.presenters.PersonalityTraitsData
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class BioActivity : AppCompatActivity(), MenuManager {
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

    /**
     * The initial point of this activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBioBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        getDeviceDimensions()

        DeviceUtils.setAnimation(viewBinding.loading, "rotation", 1000, true, 0f, 360f)

        setRandomKenBurnsBackground()
        loadObject()

        bindOnClickListener()

        //Load firebase manager
        FirebaseManager.logEvent("$TAG: Opened", "Activity_Bio")

        //Manu Interface
        initializeMenu(this, deviceDimension, viewBinding)
    }

    /**
     * Load all onClick functions for all views on MainActivity
     */
    private fun bindOnClickListener() {
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
        deviceDimension[0] = intent.getStringExtra("deviceWidth")!!.toInt()
        deviceDimension[1] = intent.getStringExtra("deviceHeight")!!.toInt()
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
     * Load the view data
     */
    private fun loadObject() {
        CoroutineScope(Dispatchers.IO).launch {
            val call : Response<Bios> = APIAdapter.getRetrofitBio().create(APIService::class.java).getBio(username)
            val response : Bios? = call.body()
            runOnUiThread {
                if (call.isSuccessful) {
                    response?.person?.picture?.let {
                        Picasso.get().load(response.person.picture)
                            .into(viewBinding.picture)
                    }
                    response?.person?.name?.let { viewBinding.name.text =
                        response.person.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        }
                    }
                    response?.person?.professionalHeadline?.let {
                        viewBinding.professionalHeadline.text = response.person.professionalHeadline.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        }
                    }

                    response?.person?.summaryOfBio?.let {
                        var textView = TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_top))
                        textView.text = getString(R.string.detail_summaryOfBio)
                        viewBinding.dataShower.addView(textView)

                        textView = TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_middle))
                        val text = "• " + response.person.summaryOfBio
                        textView.text = text
                        viewBinding.dataShower.addView(textView)
                    }

                    response?.person?.links?.let {
                        val textView: TextView = if (response.person.summaryOfBio == null)
                            TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_top))
                        else
                            TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_middle_dark))

                        textView.text = getString(R.string.detail_contact)
                        viewBinding.dataShower.addView(textView)

                        val scrollView = ScrollView(ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_middle))

                        val linearLayout = LinearLayout(this@BioActivity)
                        linearLayout.orientation = LinearLayout.HORIZONTAL
                        linearLayout.layoutParams =
                            LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )

                        @SuppressLint("UseCompatLoadingForDrawables")
                        for (link in response.person.links){
                            val imageView = ImageView(this@BioActivity)
                            when (link?.name){
                                "facebook" ->  imageView.setImageResource(R.drawable.ic_facebook)
                                "github" ->  imageView.setImageResource(R.drawable.ic_github)
                                "gitlab" ->  imageView.setImageResource(R.drawable.ic_gitlab)
                                "instagram" ->  imageView.setImageResource(R.drawable.ic_instagram)
                                "linkedin" ->  imageView.setImageResource(R.drawable.ic_linkedin)
                                "medium" ->  imageView.setImageResource(R.drawable.ic_medium)
                                "twitter" ->  imageView.setImageResource(R.drawable.ic_twitter)
                                else ->  imageView.setImageResource(R.drawable.ic_www)
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
                                        Toast.makeText(this@BioActivity, getString(R.string.error_loading), Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                            linearLayout.addView(imageView)
                        }

                        scrollView.addView(linearLayout)
                        viewBinding.dataShower.addView(scrollView)
                    }

                    response?.strengths?.let {
                        var textView = TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_middle_dark))
                        textView.text = getString(R.string.detail_strengths_bio)
                        viewBinding.dataShower.addView(textView)

                        textView = TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_middle))
                        var text = ""
                        for (detail in response.strengths)
                            text += detail?.name + "\n"
                        if (text.length > 1)
                            textView.text = text.substring(0, text.length - 1)
                        viewBinding.dataShower.addView(textView)
                    }

                    response?.interests?.let {
                        var textView = TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_middle_dark))
                        textView.text = getString(R.string.detail_interests)
                        viewBinding.dataShower.addView(textView)

                        textView = TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_middle))
                        var text = ""
                        for (detail in response.interests)
                            text += detail?.name + "\n"
                        if (text.length > 1)
                            textView.text = text.substring(0, text.length - 1)
                        viewBinding.dataShower.addView(textView)
                    }

                    response?.jobs?.let {
                        var textView = TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_middle_dark))
                        textView.text = getString(R.string.detail_jobs)
                        viewBinding.dataShower.addView(textView)

                        textView = TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_middle))
                        var text = ""
                        var counter = 1
                        for (detail in response.jobs) {
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

                    response?.personalityTraitsResults?.let {
                        var textView = TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_middle_dark))
                        textView.text = getString(R.string.detail_personality_traits_results)
                        viewBinding.dataShower.addView(textView)

                        val personalityTraitList: ArrayList<PersonalityTraitsData> = ArrayList()
                        for (detail in response.personalityTraitsResults.groups!!){
                            val personalityTrait = PersonalityTraitsData(detail?.id, detail?.order, detail?.median, detail?.stddev, null, "")
                            personalityTraitList.add(personalityTrait)
                        }
                        for ((i, detail) in response.personalityTraitsResults.analyses!!.withIndex()){
                            if (i>5) break
                            when(detail?.groupId){
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
                        val chartLinearLayout = LinearLayout(ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_middle))
                        chartLinearLayout.orientation = LinearLayout.VERTICAL
                        chartLinearLayout.layoutParams =
                            LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )

                        val sideItemWeight = 1.8f
                        val middleItemWeight = 6.4f
                        val itemWeightSum = 10f

                        val detailPersonalityTraitsSideStart = resources.getStringArray(R.array.detail_personality_traits_side_start)
                        val detailPersonalityTraitsSideEnd = resources.getStringArray(R.array.detail_personality_traits_side_end)

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
                            personalityTraitList[i].analysis?.let {
                                    it1 -> BarEntry(0f, it1)
                            }?.let {
                                    it2 -> entry.add(it2)
                            }

                            val datset = BarDataSet(entry, getString(R.string.detail_personality_traits_analysis))
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

                    response?.languages?.let {
                        var textView = TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_middle_dark))
                        textView.text = getString(R.string.detail_languages)
                        viewBinding.dataShower.addView(textView)

                        textView = TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_bottom))
                        var text = ""
                        for (detail in response.languages)
                            text += detail?.language + ": " + (detail?.fluency ?: getString(R.string.detail_languages_null)) + "\n"
                        if (text.length > 1)
                            textView.text = text.substring(0, text.length - 1)
                        viewBinding.dataShower.addView(textView)
                    }

                    viewBinding.applyNowButton.setOnClickListener {
                        DeviceUtils.openURL(this@BioActivity,
                            "https://torre.co/en/$username"
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
}