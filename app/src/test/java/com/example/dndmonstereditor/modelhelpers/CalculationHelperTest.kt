package com.example.dndmonstereditor.modelhelpers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.clearAllMocks
import junit.framework.TestCase
import org.junit.After
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class CalculationHelperTest : TestCase(){

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @After
    fun shutdown(){
        clearAllMocks()
    }

    @Test
    fun `test get dice on standard dice string` (){
        val diceString = "2d4+3"

        val results = CalculationHelper.getDice(diceString)

        assertThat(results).isNotNull()
        assertThat(results.number).isEqualTo(2)
        assertThat(results.size).isEqualTo(4)
        assertThat(results.bonus).isEqualTo(3)

    }

    @Test
    fun `test get average damage using proper dice set up`(){
        val dice = Dice(2,6,1)

        val results = CalculationHelper.getAverageDamage(dice)

        assertThat(results).isNotNull()
        assertThat(results).isEqualTo(8)
    }


}