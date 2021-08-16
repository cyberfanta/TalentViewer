package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.*
import com.cyberfanta.talentviewer.views.JobActivityInterface

class JobActivityPresenterImpl (var view: JobActivityInterface) : JobActivityPresenter {
    private var interactorImpl: Interactor = InteractorImpl ()

    init {
        interactorImpl.setJobActivityPresenter(this)
    }

    //View

    override fun showJob(job: Jobs) {
        TODO("Not yet implemented")
    }

    //View Errors

    override fun errorLoadingJob() {
        TODO("Not yet implemented")
    }

    //Interactor

    override fun getJob() {
        TODO("Not yet implemented")
    }
}