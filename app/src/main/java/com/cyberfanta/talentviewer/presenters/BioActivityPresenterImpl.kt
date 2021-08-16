package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.*
import com.cyberfanta.talentviewer.views.BioActivityInterface

class BioActivityPresenterImpl (var view: BioActivityInterface) : BioActivityPresenter {
    private var interactorImpl: Interactor = InteractorImpl ()

    init {
        interactorImpl.setBioActivityPresenter(this)
    }

    //View

    override fun showBio(bio: Bios) {
        TODO("Not yet implemented")
    }

    //View Errors

    override fun errorLoadingBio() {
        TODO("Not yet implemented")
    }

    //Interactor

    override fun getBio() {
        TODO("Not yet implemented")
    }
}