package com.cyberfanta.talentviewer.views

import com.cyberfanta.talentviewer.models.OpportunityItem
import com.cyberfanta.talentviewer.models.PeopleItem

interface MainActivityInterface {
    //View
    fun showOpportunities(opportunityList: Map<String, OpportunityItem>)
    fun showPeoples(peopleList: Map<String, PeopleItem>)
    fun errorLoadingOpportunities()
    fun errorLoadingPeoples()

    //Presenter
    fun getOpportunities()
    fun getPeoples()
}