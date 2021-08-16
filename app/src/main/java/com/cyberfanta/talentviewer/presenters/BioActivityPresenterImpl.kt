package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.*

class BioActivityPresenterImpl (var view: BioActivityInterface) : BioActivityPresenter {
    private var interactorImpl: Interactor = InteractorImpl ()

    init {
        interactorImpl.setBioActivityPresenter(this)
    }

    override fun showBio(bio: Bios) {
        TODO("Not yet implemented")
    }

    override fun getBio() {
        TODO("Not yet implemented")
    }
}