package com.cyberfanta.talentviewer.presenters

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.cyberfanta.talentviewer.R
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

class RateAppManager (context: Context) {
    companion object {
        @Suppress("PrivatePropertyName", "unused")
        private val TAG = this::class.java.simpleName

        var reviewManager : ReviewManager? = null

        fun requestReview (context: Context) {
            val request = reviewManager?.requestReviewFlow()
            request?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo = task.result
                    FirebaseManager.logEvent("Menu: Rate App - Success", "Open_Menu")
                    FirebaseManager.logEvent(reviewInfo.toString(), "Rate_App")
                    Toast.makeText(
                        context,
                        R.string.item_rate_success,
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.i(TAG, "Rate App Result: $reviewInfo")
                } else {
                    FirebaseManager.logEvent("Menu: Rate App - Fail", "Open_Menu")
                    Toast.makeText(context, R.string.item_rate_fail, Toast.LENGTH_SHORT)
                        .show()
                    Log.i(TAG, "Rate App Result: " + task.exception.toString())
                }
            }
        }

    }
    init {
        if (reviewManager == null)
            reviewManager = ReviewManagerFactory.create(context)
    }
}