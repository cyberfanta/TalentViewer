package com.cyberfanta.talentviewer.presenters

import com.cyberfanta.talentviewer.models.*
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Assert

class ApiManagerTest : TestCase() {

    fun testGetRetrofitBioOk () {
        CoroutineScope(Dispatchers.IO).launch {
            Assert.assertSame(
                ApiManager.getRetrofitBio().create(APIService::class.java).getBio("julioleon2004")
                    .body(),
                Bios::class.java
            )
        }
    }

    fun testGetRetrofitJobOk () {
        CoroutineScope(Dispatchers.IO).launch {
            Assert.assertSame(
                ApiManager.getRetrofitJob().create(APIService::class.java).getJob("NrJ67KAr")
                    .body(),
                Bios::class.java
            )
        }
    }

    fun testGetRetrofitPeoplesOk () {
        CoroutineScope(Dispatchers.IO).launch {
            Assert.assertSame(
                ApiManager.getRetrofitPeoples().create(APIService::class.java).getPeoples(PageData("0", "20", false).toString())
                    .body(),
                Peoples::class.java
            )
        }
    }

    fun testGetRetrofitOpportunitiesOk () {
        CoroutineScope(Dispatchers.IO).launch {
            Assert.assertSame(
                ApiManager.getRetrofitOpportunities().create(APIService::class.java).getOpportunities(PageData("0", "20", false).toString())
                    .body(),
                Opportunities::class.java
            )
        }
    }
}