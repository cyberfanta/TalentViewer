package com.cyberfanta.talentviewer.models

import com.cyberfanta.talentviewer.presenters.BioActivityPresenter
import com.cyberfanta.talentviewer.presenters.JobActivityPresenter
import com.cyberfanta.talentviewer.presenters.MainActivityPresenter
import com.cyberfanta.talentviewer.presenters.PageData

class InteractorImpl : Interactor {
    companion object {
        //Class Connectors
        var mainPresenter: MainActivityPresenter? = null
        var jobPresenter: JobActivityPresenter? = null
        var bioPresenter: BioActivityPresenter? = null

        //Data Containers
        var opportunities = mutableMapOf<String, OpportunityItem>()
        var peoples = mutableMapOf<String, PeopleItem>()
        var jobs  = mutableMapOf<String, Jobs>()
        var bios = mutableMapOf<String, Bios>()
    }

    private var pageSize = 30
//    private var currentAggregators = false

    private var repositoryAPI: RepositoryAPI = RepositoryAPIImpl(this)
    private var repositoryDB: RepositoryDB = RepositoryDBImpl(this)

    //Presenter

    /**
     * Send the job page obtained
     */
    override fun showJobPage(opportunityList: Map<String, OpportunityItem>) {
        val current = opportunities.size
        opportunities.putAll(opportunityList)
        mainPresenter?.showJobPage(opportunities, current)
    }

    /**
     * Send the bio page obtained
     */
    override fun showBioPage(peopleList: Map<String, PeopleItem>) {
        val current = peoples.size
        peoples.putAll(peopleList)
        mainPresenter?.showBioPage(peoples, current)
    }

    /**
     * Send the single job obtained
     */
    override fun showJob(job: Jobs) {
        job.id?.let { jobs.put(it, job) }
        jobPresenter?.showJob(job)
    }

    /**
     * Send the single bio obtained
     */
    override fun showBio(bio: Bios) {
        bio.person?.publicId?.let { bios.put(it, bio) }
        bioPresenter?.showBio(bio)
    }

    //Presenter errors

    /**
     * Show error when loading a job page
     */
    override fun errorLoadingJobPage() {
        mainPresenter?.errorLoadingJobPage()
    }

    /**
     * Show error when loading a bio page
     */
    override fun errorLoadingBioPage() {
        mainPresenter?.errorLoadingBioPage()
    }

    /**
     * Show error when loading a job
     */
    override fun errorLoadingJob() {
        jobPresenter?.errorLoadingJob()
    }

    /**
     * Show error when loading a bio
     */
    override fun errorLoadingBio() {
        bioPresenter?.errorLoadingBio()
    }

    //Repository

    /**
     * Manage the job page obtain
     */
    override fun getJobPage() {
        repositoryAPI.getJobPage(PageData(opportunities.size.toString(), pageSize.toString(), false))
    }

    /**
     * Manage the bio page obtain
     */
    override fun getBioPage() {
        repositoryAPI.getBioPage(PageData(peoples.size.toString(), pageSize.toString(), false))
    }

    /**
     * Manage the single job obtain
     */
    override fun getJob(id: String) : Jobs? {
        return if (jobs.containsKey(id))
            jobs[id]!!
        else {
            repositoryAPI.getJob(id)
            null
        }
    }

    /**
     * Manage the single bio obtain
     */
    override fun getBio(publicId: String) : Bios? {
        return if (bios.containsKey(publicId))
            bios[publicId]!!
        else {
            repositoryAPI.getBio(publicId)
            null
        }
    }

    //Setters

    /**
     * Connect interactor with MainActivityPresenter
     */
    override fun setMainActivityPresenter(mainActivityPresenter: MainActivityPresenter) {
        mainPresenter.let { mainPresenter = mainActivityPresenter }
    }

    /**
     * Connect interactor with JobActivityPresenter
     */
    override fun setJobActivityPresenter(jobActivityPresenter: JobActivityPresenter) {
        jobPresenter.let { jobPresenter = jobActivityPresenter }
    }

    /**
     * Connect interactor with BioActivityPresenter
     */
    override fun setBioActivityPresenter(bioActivityPresenter: BioActivityPresenter) {
        bioPresenter.let { bioPresenter = bioActivityPresenter }
    }
}