package com.cyberfanta.talentviewer.models

import com.cyberfanta.talentviewer.presenters.BioActivityPresenter
import com.cyberfanta.talentviewer.presenters.JobActivityPresenter
import com.cyberfanta.talentviewer.presenters.MainActivityPresenter

interface Interactor {
    //Presenter
    fun showOpportunities(opportunityList: Map<String, OpportunityItem>)
    fun showPeoples(peopleList: Map<String, PeopleItem>)
    fun showJob(job: Jobs)
    fun showBio(bio: Bios)

    //Presenter Errors
    fun errorLoadingOpportunities()
    fun errorLoadingPeoples()
    fun errorLoadingJob()
    fun errorLoadingBio()

    //Repository
    fun getOpportunities()
    fun getPeoples()
    fun getJob(id: String) : Jobs?
    fun getBio(publicId: String) : Bios?

    //Setters
    fun setMainActivityPresenter(mainActivityPresenter: MainActivityPresenter)
    fun setJobActivityPresenter(jobActivityPresenter: JobActivityPresenter)
    fun setBioActivityPresenter(bioActivityPresenter: BioActivityPresenter)
}