package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.OpportunityItem
import com.cyberfanta.talentviewer.models.PeopleItem

interface MainActivityPresenter {
    //View
    fun showJobPage(peopleList: Map<String, OpportunityItem>)
    fun showBioPage(peopleList: Map<String, PeopleItem>)

    //Interactor
    fun getJobPage()
    fun getBioPage()
}