package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.*
import com.cyberfanta.talentviewer.views.MainActivityInterface

class MainActivityPresenterImpl (var view: MainActivityInterface) : MainActivityPresenter {
    private var interactorImpl: Interactor = InteractorImpl ()

    init {
        interactorImpl.setMainActivityPresenter(this)
    }

    //View

    override fun showJobPage(peopleList: Map<String, OpportunityItem>, current: Int) {
        TODO("Not yet implemented")
    }

    override fun showBioPage(peopleList: Map<String, PeopleItem>, current: Int) {
        TODO("Not yet implemented")
    }

    //View Errors

    override fun errorLoadingJobPage() {
        TODO("Not yet implemented")
    }

    override fun errorLoadingBioPage() {
        TODO("Not yet implemented")
    }

    //Interactor

    /**
     * Manege the obtain of a job page
     */
    override fun getJobPage() {
        interactorImpl.getJobPage()
    }

    /**
     * Manege the obtain of a bio page
     */
    override fun getBioPage() {
        interactorImpl.getBioPage()
    }
}