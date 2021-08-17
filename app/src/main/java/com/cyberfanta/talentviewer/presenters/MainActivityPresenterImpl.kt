package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.*
import com.cyberfanta.talentviewer.views.MainActivityInterface

class MainActivityPresenterImpl (var view: MainActivityInterface) : MainActivityPresenter {
    @Suppress("PrivatePropertyName", "unused")
    private val TAG = this::class.java.simpleName
    private var interactorImpl: Interactor = InteractorImpl ()

    init {
        interactorImpl.setMainActivityPresenter(this)
    }

    //View

    /**
     * Send Opportunities obtained
     */
    override fun showOpportunities(peopleList: Map<String, OpportunityItem>) {
        view.showOpportunities(peopleList)
    }

    /**
     * Send Peoples obtained
     */
    override fun showPeoples(peopleList: Map<String, PeopleItem>) {
        view.showPeoples(peopleList)
    }

    //View Errors

    /**
     * Show error when loading Opportunities
     */
    override fun errorLoadingOpportunities() {
        view.errorLoadingOpportunities()
    }

    /**
     * Show error when loading Peoples
     */
    override fun errorLoadingPeoples() {
        view.errorLoadingPeoples()
    }

    //Interactor

    /**
     * Manage the obtain of Opportunities
     */
    override fun getOpportunities() {
        interactorImpl.getOpportunities()
    }

    /**
     * Manage the obtain of Peoples
     */
    override fun getPeoples() {
        interactorImpl.getPeoples()
    }
}