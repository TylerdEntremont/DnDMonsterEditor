package com.example.dndmonstereditor.modelhelpers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.dndmonstereditor.model.monsterDetails.Action
import com.example.dndmonstereditor.model.monsterDetails.Damage
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.After
import org.junit.Rule
import org.junit.Test

import com.google.common.truth.Truth.assertThat

class ActionHelperTest : TestCase() {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @After
    fun shutdown(){
        clearAllMocks()
    }

    @Test
    fun `test is attack on an attack`(){
        val action = mockk<Action>(){
            every {attack_bonus} returns 2
        }

        val result = ActionHelper(action).isAttack()

        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(true)

    }

    @Test
    fun `test is attack on no attack`(){
        val action = mockk<Action>(){
            every {attack_bonus} returns null
        }

        val result = ActionHelper(action).isAttack()

        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(false)

    }

    @Test
    fun `test get Dice on attack`(){
        val action = mockk<Action>(){
            every{attack_bonus} returns 2
            every{damage} returns listOf(mockk<Damage>(){
                every{damage_dice} returns "2d4+1"
            })
        }

        val result = ActionHelper(action).getDice()

        assertThat(result).isNotNull()
        assertThat(result.number).isEqualTo(2)
        assertThat(result.size).isEqualTo(4)
        assertThat(result.bonus).isEqualTo(1)

    }

    @Test
    fun `test get Dice on not attack`(){
        val action = mockk<Action>(){
            every{attack_bonus} returns null
        }

        val result = ActionHelper(action).getDice()

        assertThat(result).isNotNull()
        assertThat(result.number).isEqualTo(0)
        assertThat(result.size).isEqualTo(0)
        assertThat(result.bonus).isEqualTo(0)

    }


}