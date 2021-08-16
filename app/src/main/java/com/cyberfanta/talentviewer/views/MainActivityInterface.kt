package com.cyberfanta.talentviewer.views

import com.cyberfanta.talentviewer.models.OpportunityItem
import com.cyberfanta.talentviewer.models.PeopleItem

interface MainActivityInterface {
    //View
    fun showJobPage(peopleList: Map<String, OpportunityItem>)
    fun showBioPage(peopleList: Map<String, PeopleItem>)
    fun errorLoadingJobPage()
    fun errorLoadingBioPage()

    //Presenter
    fun getJobPage()
    fun getBioPage()
}