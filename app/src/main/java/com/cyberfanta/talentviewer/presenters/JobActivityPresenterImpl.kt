package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.*
import com.cyberfanta.talentviewer.views.JobActivityInterface

class JobActivityPresenterImpl (var view: JobActivityInterface) : JobActivityPresenter {
    @Suppress("PrivatePropertyName", "unused")
    private val TAG = this::class.java.simpleName
    private var interactorImpl: Interactor = InteractorImpl ()

    init {
        interactorImpl.setJobActivityPresenter(this)
    }

    //View

    /**
     * Send the single job obtained
     */
    override fun showJob(job: Jobs) {
        view.showJob(job)
    }

    //View Errors

    /**
     * Show error when loading a job
     */
    override fun errorLoadingJob() {
        view.errorLoadingJob()
    }

    //Interactor

    /**
     * Manage the single job obtain
     */
    override fun getJob(id: String): Jobs? {
        return interactorImpl.getJob(id)
    }
}