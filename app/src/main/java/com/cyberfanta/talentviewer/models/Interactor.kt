package com.cyberfanta.talentviewer.models

interface Interactor {
    //Repository
    fun getJobPage()
    fun getBioPage()
    fun getOpportunity(id: String)
    fun getPeople(username: String)
}