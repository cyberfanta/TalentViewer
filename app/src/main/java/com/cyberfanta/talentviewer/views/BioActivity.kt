package com.cyberfanta.talentviewer.views

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import com.cyberfanta.talentviewer.R
import com.cyberfanta.talentviewer.databinding.ActivityBioBinding
import com.cyberfanta.talentviewer.models.APIService
import com.cyberfanta.talentviewer.models.Bios
import com.cyberfanta.talentviewer.presenters.FirebaseManager
import com.cyberfanta.talentviewer.presenters.PersonalityTraitsData
import com.cyberfanta.talentviewer.presenters.RateAppManager
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class BioActivity : AppCompatActivity() {
    @Suppress("PrivatePropertyName", "unused")
    private val TAG = this::class.java.simpleName

    //To bind view with logical part
    private lateinit var viewBinding: ActivityBioBinding

    //UI variables
    private var authorOpened: Boolean = false
    private var deviceDimension = intArrayOf(0, 0)
    private var username = ""

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

//        AdsManager.attachBannerAd (adView)

        //Load firebase manager
        FirebaseManager.logEvent("$TAG: Opened", "Activity_Bio")
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
        when (DeviceUtils.getRandomNumber(1, 8)) {
            1 -> viewBinding.pictureBackground.setImageResource(R.drawable.background_1)
            2 -> viewBinding.pictureBackground.setImageResource(R.drawable.background_2)
            3 -> viewBinding.pictureBackground.setImageResource(R.drawable.background_3)
            4 -> viewBinding.pictureBackground.setImageResource(R.drawable.background_4)
            5 -> viewBinding.pictureBackground.setImageResource(R.drawable.background_5)
            6 -> viewBinding.pictureBackground.setImageResource(R.drawable.background_6)
            7 -> viewBinding.pictureBackground.setImageResource(R.drawable.background_7)
            8 -> viewBinding.pictureBackground.setImageResource(R.drawable.background_8)
        }
    }

    /**
     * Load the view data
     */
    private fun loadObject() {
        CoroutineScope(Dispatchers.IO).launch {
            val call : Response<Bios> = getRetrofitBio().create(APIService::class.java).getBio(username)
            val response : Bios? = call.body()
            runOnUiThread {
                if (call.isSuccessful) {
                    response?.person?.picture?.let {
                        Picasso.get().load(response.person.picture)
                            .into(viewBinding.picture)
                    }
                    response?.person?.name?.let { viewBinding.name.text = response.person.name }
                    response?.person?.professionalHeadline?.let {
                        viewBinding.professionalHeadline.text = response.person.professionalHeadline
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

//                    experiences
//                    awards
//                    projects
//                    publications
//                    education
//                    opportunities
//                    personalityTraitsResults
//                    professionalCultureGenomeResults

//                    response?.person?.summaryOfBio?.let {
//                        var textView = TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_middle_dark))
//                        textView.text = getString(R.string.detail_summaryOfBio)
//                        viewBinding.dataShower.addView(textView)
//
//                        textView = TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_middle))
//                        var text = ""
//                        for (detail in response.details)
//                            text += detail?.content + "\n"
//                        textView.text = text.substring(0, text.length - 1)
//                        viewBinding.dataShower.addView(textView)
//                    }

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
                            Log.i(
                                TAG,
                                "** personalityTraitList?.get($i)?.analysis ****** " + personalityTraitList[i].analysis + " ****** " + personalityTraitList[i].group + "**** entry:" + entry.get(0).y
                            )
                            Log.i(
                                TAG,
                                "** personalityTraitList?.get($i)?.median ****** " + personalityTraitList[i].median
                            )
                            Log.i(
                                TAG,
                                "** personalityTraitList.get($i).stddev!! ****** " + personalityTraitList[i].stddev!!
                            )

                            val datset = BarDataSet(entry, getString(R.string.detail_personality_traits_analysis))
                            datset.valueTextSize = 10f

//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            @SuppressLint("NewApi")
                                datset.color = getColor(R.color.black_torre)
//                            else
//                                datset.color = Color.BLACK

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
     * Retrofit implementation to get a bio detail
     */
    private fun getRetrofitBio(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://torre.bio/api/bios/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Show error message when device have a problem with the internet
     */
    private fun showError() {
        Toast.makeText(this, getString(R.string.error_loading), Toast.LENGTH_SHORT).show()
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

//    /**
//     * Actions made when app start
//     */
//    override fun onStart() {
//        AdsManager.attachBannerAd(viewBinding.adView)
//        super.onStart()
//    }
}