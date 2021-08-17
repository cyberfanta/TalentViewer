package com.cyberfanta.talentviewer.models

import com.cyberfanta.talentviewer.presenters.BioActivityPresenter
import com.cyberfanta.talentviewer.presenters.JobActivityPresenter
import com.cyberfanta.talentviewer.presenters.MainActivityPresenter
import com.cyberfanta.talentviewer.presenters.PageData

class InteractorImpl : Interactor {
    @Suppress("PrivatePropertyName", "unused")
    private val TAG = this::class.java.simpleName

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
     * Send Opportunities obtained
     */
    override fun showOpportunities(opportunityList: Map<String, OpportunityItem>) {
        opportunities.putAll(opportunityList)
        mainPresenter?.showOpportunities(opportunities)
    }

    /**
     * Send Peoples obtained
     */
    override fun showPeoples(peopleList: Map<String, PeopleItem>) {
        peoples.putAll(peopleList)
        mainPresenter?.showPeoples(peoples)
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
     * Show error when loading Opportunities
     */
    override fun errorLoadingOpportunities() {
        mainPresenter?.errorLoadingOpportunities()
    }

    /**
     * Show error when loading Peoples
     */
    override fun errorLoadingPeoples() {
        mainPresenter?.errorLoadingPeoples()
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
     * Manage the opportunities obtain
     */
    override fun getOpportunities() {
        repositoryAPI.getOpportunities(PageData("0", opportunities.size.toString() + pageSize.toString(), false))
    }

    /**
     * Manage the peoples obtain
     */
    override fun getPeoples() {
        repositoryAPI.getPeoples(PageData("0", peoples.size.toString() + pageSize.toString(), false))
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