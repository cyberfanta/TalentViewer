package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.Jobs

interface JobActivityPresenter {
    //View
    fun showJob(job: Jobs)

    //Interactor
    fun getJob()
}