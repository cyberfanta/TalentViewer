package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.Bios

interface BioActivityPresenter {
    //View
    fun showBio(bio: Bios)
    fun errorLoadingBio()

    //Interactor
    fun getBio(publicId: String): Bios?
}