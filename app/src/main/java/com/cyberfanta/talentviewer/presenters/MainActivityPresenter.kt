package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.Bios
import com.cyberfanta.talentviewer.models.Jobs
import com.cyberfanta.talentviewer.models.OpportunityItem
import com.cyberfanta.talentviewer.models.PeopleItem

interface MainActivityPresenter {
    //View
    fun showJobPage(peopleList: Map<String, OpportunityItem>, current: Int)
    fun showBioPage(peopleList: Map<String, PeopleItem>, current: Int)
    fun errorLoadingJobPage()
    fun errorLoadingBioPage()

    //Interactor
    fun getJobPage()
    fun getBioPage()
}