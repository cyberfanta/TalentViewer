package com.cyberfanta.talentviewer.views

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

                    //TODO: Add more data here

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