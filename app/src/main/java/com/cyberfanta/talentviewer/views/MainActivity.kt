package com.cyberfanta.talentviewer.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cyberfanta.talentviewer.databinding.ActivityMainBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        fillRecyclerViewBios()
        fillRecyclerViewJobs()
    }

    private fun fillRecyclerViewJobs() {
        TODO("Not yet implemented")
    }

    private fun fillRecyclerViewBios() {
        TODO("Not yet implemented")
    }
}