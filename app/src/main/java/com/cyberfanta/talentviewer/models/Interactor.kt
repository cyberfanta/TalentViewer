package com.cyberfanta.talentviewer.models

import com.cyberfanta.talentviewer.presenters.BioActivityPresenter
import com.cyberfanta.talentviewer.presenters.JobActivityPresenter
import com.cyberfanta.talentviewer.presenters.MainActivityPresenter

interface Interactor {
    //Presenter
    fun showJobPage(opportunityList: Map<String, OpportunityItem>)
    fun showBioPage(peopleList: Map<String, PeopleItem>)
    fun showJob(job: Jobs)
    fun showBio(bio: Bios)

    //Presenter Errors
    fun errorLoadingJobPage()
    fun errorLoadingBioPage()
    fun errorLoadingJob()
    fun errorLoadingBio()

    //Repository
    fun getJobPage()
    fun getBioPage()
    fun getJob(id: String) : Jobs?
    fun getBio(publicId: String) : Bios?

    //Setters
    fun setMainActivityPresenter(mainActivityPresenter: MainActivityPresenter)
    fun setJobActivityPresenter(jobActivityPresenter: JobActivityPresenter)
    fun setBioActivityPresenter(bioActivityPresenter: BioActivityPresenter)
}