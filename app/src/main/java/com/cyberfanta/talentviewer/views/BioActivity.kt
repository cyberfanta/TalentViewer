package com.cyberfanta.talentviewer.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cyberfanta.talentviewer.R
import com.cyberfanta.talentviewer.databinding.ActivityBioBinding
import com.cyberfanta.talentviewer.models.APIService
import com.cyberfanta.talentviewer.models.Bios
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BioActivity : AppCompatActivity() {
    //To bind view with logical part
    private lateinit var viewBinding: ActivityBioBinding

    //Storage the device dimension
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

                    //

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
}