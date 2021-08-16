package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.Bios

interface BioActivityPresenter {
    //View
    fun showBio(bio: Bios)

    //Interactor
    fun getBio()
}