package com.cyberfanta.talentviewer.views

import com.cyberfanta.talentviewer.models.Jobs

interface JobActivityInterface {
    //View
    fun showJob(job: Jobs)
    fun errorLoadingJob()

    //Presenter
    fun getJob()
}