package com.cyberfanta.talentviewer.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.viewbinding.ViewBinding
import com.cyberfanta.talentviewer.R
import com.cyberfanta.talentviewer.databinding.ActivityBioBinding
import com.cyberfanta.talentviewer.databinding.ActivityJobBinding
import com.cyberfanta.talentviewer.databinding.ActivityMainBinding
import com.cyberfanta.talentviewer.presenters.FirebaseManager
import com.cyberfanta.talentviewer.presenters.RateAppManager

interface MenuManager {
    var authorOpened: Boolean
    var contextMenu: Context
    var deviceWidth: Float
    var viewBindingMenu: ViewBinding

    /**
     * Handle the initialization of the menu
     */
    fun initializeMenu(context: Context, deviceDimension: IntArray, viewBinding: ViewBinding) {
        authorOpened = false
        contextMenu = context
        deviceWidth = deviceDimension[0].toFloat()
        viewBindingMenu = viewBinding
    }

    /**
     * Handle the setting menu of the application
     */
    fun optionsItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.item_policy -> itemPolicy()
            R.id.item_rate -> itemRate()
            R.id.item_about -> itemAbout()
        }
    }

    /**
     * Actions for Policy Option Menu
     */
    private fun itemPolicy() {
        FirebaseManager.logEvent("Menu: Policy", "Open_Menu")
        val uri = Uri.parse(contextMenu.getString(R.string.item_policy_page))
        val intent = Intent(Intent.ACTION_VIEW, uri)
        contextMenu.startActivity(intent)
    }

    /**
     * Actions for Rate Option Menu
     */
    private fun itemRate() {
        FirebaseManager.logEvent("Menu: Rate App", "Open_Menu")
        RateAppManager.requestReview(contextMenu)
    }

    /**
     * Actions for About Option Menu
     */
    private fun itemAbout() {
        FirebaseManager.logEvent("Menu: Author", "Open_Menu")
        when (viewBindingMenu){
            is ActivityMainBinding -> {
                val viewBinding = viewBindingMenu as ActivityMainBinding
                viewBinding.author.visibility = View.VISIBLE
                DeviceUtils.setAnimation(viewBinding.author, "translationX", 300, false, deviceWidth, 0f)
            }
            is ActivityJobBinding -> {
                val viewBinding = viewBindingMenu as ActivityJobBinding
                viewBinding.author.visibility = View.VISIBLE
                DeviceUtils.setAnimation(viewBinding.author, "translationX", 300, false, deviceWidth, 0f)
            }
            is ActivityBioBinding -> {
                val viewBinding = viewBindingMenu as ActivityBioBinding
                viewBinding.author.visibility = View.VISIBLE
                DeviceUtils.setAnimation(viewBinding.author, "translationX", 300, false, deviceWidth, 0f)
            }
        }
        authorOpened = true
    }

    /**
     * Show the developer info
     */
    fun authorSelected(view: View) {
        DeviceUtils.setAnimation(view, "translationX", 300, false, 0f, deviceWidth)
    }

    /**
     * Process the behavior of the app when user press back button
     */
    fun backPressed(): Boolean {
        if (authorOpened) {
            when (viewBindingMenu) {
                is ActivityMainBinding -> { authorSelected((viewBindingMenu as ActivityMainBinding).author) }
                is ActivityJobBinding -> { authorSelected((viewBindingMenu as ActivityJobBinding).author) }
                is ActivityBioBinding -> { authorSelected((viewBindingMenu as ActivityBioBinding).author) }
            }
            authorOpened = false

            FirebaseManager.logEvent("Device Button: Back", "Device_Button")
            return false
        }
        FirebaseManager.logEvent(viewBindingMenu.javaClass.simpleName + ": Return", "Return_Main_Activity")
        return true
    }

    /**
     * Create the setting menu of the application
     */
    fun createOptionsMenu(menu: Menu?, menuInflater: MenuInflater): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

}