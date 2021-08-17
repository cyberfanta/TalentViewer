package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.OpportunityItem
import com.cyberfanta.talentviewer.models.PeopleItem

interface MainActivityPresenter {
    //View
    fun showOpportunities(peopleList: Map<String, OpportunityItem>)
    fun showPeoples(peopleList: Map<String, PeopleItem>)
    fun errorLoadingOpportunities()
    fun errorLoadingPeoples()

    //Interactor
    fun getOpportunities()
    fun getPeoples()
}