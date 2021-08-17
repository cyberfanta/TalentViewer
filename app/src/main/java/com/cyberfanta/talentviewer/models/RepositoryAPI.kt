package com.cyberfanta.talentviewer.models

import com.cyberfanta.talentviewer.presenters.PageData

interface RepositoryAPI {
    //API
    fun getOpportunities(pageData: PageData)
    fun getPeoples(pageData: PageData)
    fun getJob(id: String)
    fun getBio(username: String)
}