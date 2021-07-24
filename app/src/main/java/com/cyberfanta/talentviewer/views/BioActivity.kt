package com.cyberfanta.talentviewer.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cyberfanta.talentviewer.databinding.ActivityBioBinding

class BioActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityBioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBioBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}