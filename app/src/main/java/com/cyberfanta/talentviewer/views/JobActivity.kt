package com.cyberfanta.talentviewer.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cyberfanta.talentviewer.databinding.ActivityJobBinding

class JobActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityJobBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityJobBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}