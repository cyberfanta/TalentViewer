package com.cyberfanta.talentviewer.models

interface RepositoryDB {
    //DB
    fun getJobPage()
    fun getBioPage()
    fun getOpportunity(id: String)
    fun getPeople(username: String)
}