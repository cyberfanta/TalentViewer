package com.cyberfanta.talentviewer.views

import com.cyberfanta.talentviewer.models.Bios

interface BioActivityInterface {
    //View
    fun showBio(bio: Bios)
    fun errorLoadingBio()

    //Interactor
    fun getBio()
}