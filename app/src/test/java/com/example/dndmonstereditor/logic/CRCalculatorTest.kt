package com.example.dndmonstereditor.logic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.dndmonstereditor.model.monsterDetails.*
import io.mockk.clearAllMocks
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.After
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import io.mockk.every

class CRCalculatorTest : TestCase() {

    @get:Rule var rule = InstantTaskExecutorRule()

    @After
    fun shutdown(){
        clearAllMocks()
    }

    @Test
    fun `test get CR with all elements in CR 0` (){

        val monster = createMockkMonster(0,0,0,"0d0+1",0,0,0,0,0,0)

        val cr = CRCalculator(monster).getCR()

        assertThat(cr).isNotNull()
        assertThat(cr).isEqualTo(0)
    }

    @Test
    fun `test get CR with all elements in calculation level` (){

        val monster = createMockkMonster(14,45,5,"2d6+5",5,0,0,0,0,0)

        val cr = CRCalculator(monster).getCR()

        assertThat(cr).isNotNull()
        assertThat(cr).isAtLeast(2.6)
        assertThat(cr).isAtMost(2.8)
    }

    private fun createMockkMonster (ac:Int,hp:Int,toHit:Int,diceString:String,str:Int,dex:Int,con:Int,intel:Int,wis:Int,cha:Int): MonsterDetails{
        return mockk<MonsterDetails>(){
            every{armor_class} returns ac
            every{hit_points} returns hp
            every{actions} returns listOf(mockk<Action>(){
                every{attack_bonus} returns toHit
                every{name} returns ""
                every{damage} returns listOf(mockk<Damage>(){
                    every{damage_dice} returns diceString
                })
            })
            every {proficiencies} returns mutableListOf(mockk<Proficiency>(){
                every{proficiency} returns mockk<ProficiencyDetails>(){
                    every{name} returns "Saving Throw: STR"
                }
                every{value} returns str
            },mockk<Proficiency>(){
                every{proficiency} returns mockk<ProficiencyDetails>(){
                    every{name} returns "Saving Throw: DEX"
                }
                every{value} returns dex
            },mockk<Proficiency>(){
                every{proficiency} returns mockk<ProficiencyDetails>(){
                    every{name} returns "Saving Throw: CON"
                }
                every{value} returns con
            },mockk<Proficiency>(){
                every{proficiency} returns mockk<ProficiencyDetails>(){
                    every{name} returns "Saving Throw: INT"
                }
                every{value} returns intel
            },mockk<Proficiency>(){
                every{proficiency} returns mockk<ProficiencyDetails>(){
                    every{name} returns "Saving Throw: WIS"
                }
                every{value} returns wis
            },mockk<Proficiency>(){
                every{proficiency} returns mockk<ProficiencyDetails>(){
                    every{name} returns "Saving Throw: CHA"
                }
                every{value} returns cha
            })
        }

    }
}