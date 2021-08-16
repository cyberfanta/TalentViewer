package com.cyberfanta.talentviewer.models

import com.cyberfanta.talentviewer.presenters.MainActivityPresenter

class InteractorImpl(var mainActivityPresenter: MainActivityPresenter) : Interactor{
    var repositoryAPI: RepositoryAPI = RepositoryAPIImpl()
    var repositoryDB: RepositoryDB = RepositoryDBImpl()

    override fun getJobPage() {
        TODO("Not yet implemented")
    }

    override fun getBioPage() {
        TODO("Not yet implemented")
    }

    override fun getOpportunity(id: String) {
        TODO("Not yet implemented")
    }

    override fun getPeople(username: String) {
        TODO("Not yet implemented")
    }
}