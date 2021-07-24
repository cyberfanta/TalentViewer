package com.cyberfanta.talentviewer.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cyberfanta.talentviewer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}