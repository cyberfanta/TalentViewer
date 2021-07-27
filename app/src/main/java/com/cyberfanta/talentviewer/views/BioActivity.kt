package com.cyberfanta.talentviewer.views

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.cyberfanta.talentviewer.presenters.RateAppManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
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
                    Picasso.get().load(response?.person?.picture)
                        .into(viewBinding.picture)

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
                        val textView = TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_middle_dark))
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

                            text += getString(R.string.detail_jobs_responsibilities) + ":\n"
                            for (responsibilitie in detail.responsibilities!!)
                                text += "$responsibilitie\n"


                            text += getString(R.string.detail_jobs_from) + ": " + detail.fromMonth + " " + detail.fromYear + "\n"
                            detail.toMonth?.let { text += getString(R.string.detail_jobs_to) + ": " + detail.toMonth + " " + detail.toYear + "\n" }

                            text += "\n"
                            counter++
                        }
                        textView.text = text.substring(0, text.length - 2)
                        viewBinding.dataShower.addView(textView)
                    }

                    response?.languages?.let {
                        var textView = TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_middle_dark))
                        textView.text = getString(R.string.detail_languages)
                        viewBinding.dataShower.addView(textView)

                        textView = TextView (ContextThemeWrapper(this@BioActivity, R.style.detail_scrollview_container_bottom))
                        var text = ""
                        for (detail in response.languages)
                            text += detail?.language + ": " + detail?.fluency + "\n"
                        textView.text = text.substring(0, text.length - 1)
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