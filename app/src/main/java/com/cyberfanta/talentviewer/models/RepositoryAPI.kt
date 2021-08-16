package com.cyberfanta.talentviewer.models

interface RepositoryAPI {
    //API
    fun getJobPage()
    fun getBioPage()
    fun getOpportunity(id: String)
    fun getPeople(username: String)
}