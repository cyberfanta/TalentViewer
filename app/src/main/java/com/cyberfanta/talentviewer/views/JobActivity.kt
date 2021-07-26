package com.cyberfanta.talentviewer.views

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import com.cyberfanta.talentviewer.R
import com.cyberfanta.talentviewer.databinding.ActivityJobBinding
import com.cyberfanta.talentviewer.models.APIService
import com.cyberfanta.talentviewer.models.Jobs
import com.cyberfanta.talentviewer.presenters.AdsManager
import com.cyberfanta.talentviewer.presenters.FirebaseManager
import com.cyberfanta.talentviewer.presenters.RateAppManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class JobActivity : AppCompatActivity() {
    @Suppress("PrivatePropertyName", "unused")
    private val TAG = this::class.java.simpleName

    //To bind view with logical part
    private lateinit var viewBinding: ActivityJobBinding

    //UI variables
    private var authorOpened: Boolean = false
    private var deviceDimension = intArrayOf(0, 0)
    private var id = ""

    /**
     * The initial point of this activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityJobBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        getDeviceDimensions()

        DeviceUtils.setAnimation(viewBinding.loading, "rotation", 1000, true, 0f, 360f)

        setRandomKenBurnsBackground()
        loadObject()

        bindOnClickListener()

//        AdsManager.attachBannerAd (adView)

        //Load firebase manager
        FirebaseManager.logEvent("$TAG: Opened", "Activity_Jobs")
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
        id = intent.getStringExtra("id")!!
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
            val call : Response<Jobs> = getRetrofitJob().create(APIService::class.java).getJob(id)
            val response : Jobs? = call.body()
            runOnUiThread {
                if (call.isSuccessful) {
                    Picasso.get().load(response?.organizations?.get(0)?.picture)
                        .into(viewBinding.picture)

                    response?.objective?.let{ viewBinding.objective.text = response.objective }
                    response?.organizations?.get(0)?.name?.let{ viewBinding.organizationsName.text = response.organizations[0]?.name }

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
                    viewBinding.compensation.text = string

                    response?.details?.let {
                        var textView = TextView (ContextThemeWrapper(this@JobActivity, R.style.detail_scrollview_container_top))
                        textView.text = getString(R.string.detail_details)
                        viewBinding.dataShower.addView(textView)

                        textView = TextView (ContextThemeWrapper(this@JobActivity, R.style.detail_scrollview_container_middle))
                        var text = ""
                        for (detail in response.details)
                            text += detail?.content + "\n"
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
     * Retrofit implementation to get a job detail
     */
    private fun getRetrofitJob(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://torre.co/api/opportunities/")
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