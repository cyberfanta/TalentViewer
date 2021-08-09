package com.cyberfanta.talentviewer.views

import junit.framework.TestCase
import org.junit.Assert

class DeviceUtilsTest : TestCase() {
    private lateinit var intList : List<Int>

    public override fun setUp() {
        super.setUp()
        intList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    }

    fun testGetRandomNumberOk (){
        Assert.assertTrue(intList.contains(DeviceUtils.getRandomNumber(1, 10)))
        Assert.assertFalse(intList.contains(DeviceUtils.getRandomNumber(-2, 0)))
        Assert.assertFalse(intList.contains(DeviceUtils.getRandomNumber(11, 14)))
    }
}