package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.Interactor
import com.cyberfanta.talentviewer.models.InteractorImpl
import com.cyberfanta.talentviewer.models.OpportunityItem
import com.cyberfanta.talentviewer.models.PeopleItem
import com.cyberfanta.talentviewer.views.MainActivityInterface

class MainActivityPresenterImpl (var view: MainActivityInterface) : MainActivityPresenter{
    var interactorImpl: Interactor = InteractorImpl (this)

    override fun showJobPage(peopleList: Map<String, OpportunityItem>) {
        TODO("Not yet implemented")
    }

    override fun showBioPage(peopleList: Map<String, PeopleItem>) {
        TODO("Not yet implemented")
    }

    override fun getJobPage() {
        TODO("Not yet implemented")
    }

    override fun getBioPage() {
        TODO("Not yet implemented")
    }
}