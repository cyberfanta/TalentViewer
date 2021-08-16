package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.*
import com.cyberfanta.talentviewer.views.MainActivityInterface

class MainActivityPresenterImpl (var view: MainActivityInterface) : MainActivityPresenter {
    private var interactorImpl: Interactor = InteractorImpl ()

    init {
        interactorImpl.setMainActivityPresenter(this)
    }

    override fun showJobPage(peopleList: Map<String, OpportunityItem>, current: Int) {
        TODO("Not yet implemented")
    }

    override fun showBioPage(peopleList: Map<String, PeopleItem>, current: Int) {
        TODO("Not yet implemented")
    }

    override fun getJobPage() {
        TODO("Not yet implemented")
    }

    override fun getBioPage() {
        TODO("Not yet implemented")
    }
}