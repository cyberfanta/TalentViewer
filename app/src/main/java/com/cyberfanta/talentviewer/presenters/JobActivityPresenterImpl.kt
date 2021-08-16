package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.*

class JobActivityPresenterImpl (var view: JobActivityInterface) : JobActivityPresenter {
    private var interactorImpl: Interactor = InteractorImpl ()

    init {
        interactorImpl.setJobActivityPresenter(this)
    }

    override fun showJob(job: Jobs) {
        TODO("Not yet implemented")
    }

    override fun getJob() {
        TODO("Not yet implemented")
    }
}