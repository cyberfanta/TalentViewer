package com.cyberfanta.talentviewer.models

import com.cyberfanta.talentviewer.presenters.PageData

interface RepositoryAPI {
    //API
    fun getJobPage(pageData: PageData)
    fun getBioPage(pageData: PageData)
    fun getJob(id: String)
    fun getBio(username: String)
}