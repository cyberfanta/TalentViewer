package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.*
import com.cyberfanta.talentviewer.views.BioActivityInterface

class BioActivityPresenterImpl (var view: BioActivityInterface) : BioActivityPresenter {
    @Suppress("PrivatePropertyName", "unused")
    private val TAG = this::class.java.simpleName
    private var interactorImpl: Interactor = InteractorImpl ()

    init {
        interactorImpl.setBioActivityPresenter(this)
    }

    //View

    /**
     * Send the single bio obtained
     */
    override fun showBio(bio: Bios) {
        view.showBio(bio)
    }

    //View Errors

    /**
     * Show error when loading a bio
     */
    override fun errorLoadingBio() {
        view.errorLoadingBio()
    }

    //Interactor

    /**
     * Manage the single bio obtain
     */
    override fun getBio(publicId: String): Bios? {
        return interactorImpl.getBio(publicId)
    }
}