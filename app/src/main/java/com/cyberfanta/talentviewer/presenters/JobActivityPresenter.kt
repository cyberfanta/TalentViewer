package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.Jobs

interface JobActivityPresenter {
    //View
    fun showJob(job: Jobs)
    fun errorLoadingJob()

    //Interactor
    fun getJob(id: String)
}