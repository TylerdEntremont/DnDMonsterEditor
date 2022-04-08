package com.example.dndmonstereditor.modelhelpers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.dndmonstereditor.model.monsterDetails.*
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import org.junit.After
import io.mockk.every
import junit.framework.TestCase

class MonsterDetailHelperTest : TestCase() {

    @get:Rule var rule = InstantTaskExecutorRule()

    @After
    fun shutdown(){
        clearAllMocks()
    }

    @Test
    fun `test get multiattack with no multiattack`(){
        val monster = mockk<MonsterDetails>(){
            every{actions} returns listOf(mockk<Action>(){
                    every{name} returns ""
            })
        }

        val multiattack = MonsterDetailHelper(monster).findMultiAttack()

        assertThat(multiattack).isNull()
    }

    @Test
    fun `test find multiattack finds multiattack`(){
        val monster = mockk<MonsterDetails>(){
            every{actions} returns listOf(mockk<Action>(){
                every{name} returns "Multiattack"
            })
        }

        val multiattack = MonsterDetailHelper(monster).findMultiAttack()

        assertThat(multiattack).isNotNull()
        assertThat(multiattack?.name).isEqualTo("Multiattack")

    }


    @Test
    fun `test get Proficiency Object with found object`(){
        val monster = mockk<MonsterDetails>(){
            every{proficiencies} returns mutableListOf(mockk<Proficiency>(){
                every{value} returns 2
                every{proficiency} returns mockk<ProficiencyDetails>(){
                    every{name} returns "testName"
                }
            })
        }

        val prof = MonsterDetailHelper(monster).getProficiencyObject("testName")

        assertThat(prof).isNotNull()
        assertThat(prof?.proficiency?.name).isEqualTo("testName")
        assertThat(prof?.value).isEqualTo(2)

    }

    @Test
    fun `test get Proficiency Object without found object`(){
        val monster = mockk<MonsterDetails>(){
            every{proficiencies} returns mutableListOf(mockk<Proficiency>(){
                every{value} returns 2
                every{proficiency} returns mockk<ProficiencyDetails>(){
                    every{name} returns "randomName"
                }
            })
        }

        val prof = MonsterDetailHelper(monster).getProficiencyObject("testName")

        assertThat(prof).isNull()

    }


    @Test
    fun `test get Proficiency Value with found value`(){
        val monster = mockk<MonsterDetails>(){
            every{proficiencies} returns mutableListOf(mockk<Proficiency>(){
                every{value} returns 2
                every{proficiency} returns mockk<ProficiencyDetails>(){
                    every{name} returns "testName"
                }
            })
        }

        val prof = MonsterDetailHelper(monster).getProficiency("testName")

        assertThat(prof).isNotNull()
        assertThat(prof).isEqualTo(2)

    }

    @Test
    fun `test get Proficiency Value without found value`(){
        val monster = mockk<MonsterDetails>(){
            every{proficiencies} returns mutableListOf(mockk<Proficiency>(){
                every{value} returns 2
                every{proficiency} returns mockk<ProficiencyDetails>(){
                    every{name} returns "nameless"
                }
            })
        }

        val prof = MonsterDetailHelper(monster).getProficiency("testName")

        assertThat(prof).isNull()
    }


    @Test
    fun `test max To Hit with multiple to Hit values`(){
        val monster = mockk<MonsterDetails>(){
            every{actions} returns listOf(mockk<Action>(){
                every{attack_bonus} returns 2
            },mockk<Action>(){
                every{attack_bonus} returns 4
            })
        }

        val toHit = MonsterDetailHelper(monster).maxToHit()

        assertThat(toHit).isNotNull()
        assertThat(toHit).isEqualTo(4)
    }


    @Test
    fun `test find ability by partial name with found name`(){
        val monster = mockk<MonsterDetails>(){
            every{special_abilities} returns listOf(mockk<SpecialAbility>{
                every{name} returns "noiseNamenoise"
            })
        }

        val result = MonsterDetailHelper(monster).findAbilityByPartialName("Name")

        assertThat(result).isNotNull()
        assertThat(result?.name).isEqualTo("noiseNamenoise")

    }

    @Test
    fun `test find ability by partial name without found name`(){
        val monster = mockk<MonsterDetails>(){
            every{special_abilities} returns listOf(mockk<SpecialAbility>{
                every{name} returns "randomStuff"
            })
        }

        val result = MonsterDetailHelper(monster).findAbilityByPartialName("Name")

        assertThat(result).isNull()

    }

    @Test
    fun `test put attacks string with two items`(){
        var action1=Action(0,listOf(),listOf(Damage("",mockk<DamageType>())),mockk<Dc>(),"","",mockk<Options>(),mockk())
        var action2=Action(0,listOf(),listOf(Damage("",mockk<DamageType>())),mockk<Dc>(),"","",mockk<Options>(),mockk())
        var monster = mockk<MonsterDetails>(){
            every{actions} returns mutableListOf(action1,action2)
        }
        val attacksString = "4,2d4+1:3,3d6+5"

        MonsterDetailHelper(monster).putAttacksString(attacksString)

        assertThat(monster.actions[0].attack_bonus).isEqualTo(4)
        assertThat(monster.actions[0].damage[0].damage_dice).isEqualTo("2d4+1")
        assertThat(monster.actions[1].attack_bonus).isEqualTo(3)
        assertThat(monster.actions[1].damage[0].damage_dice).isEqualTo("3d6+5")
    }

    @Test
    fun `test get AttackString with multiple actions`(){
        var action1=Action(4,listOf(),listOf(Damage("2d4+1",mockk<DamageType>())),mockk<Dc>(),"","",mockk<Options>(),mockk())
        var action2=Action(3,listOf(),listOf(Damage("1d6+3",mockk<DamageType>())),mockk<Dc>(),"","",mockk<Options>(),mockk())
        var monster = mockk<MonsterDetails>(){
            every{actions} returns mutableListOf(action1,action2)
        }

        val attackString = MonsterDetailHelper(monster).getAttacksString()

        assertThat(attackString).isNotNull()
        assertThat(attackString).isEqualTo("4,2d4+1:3,1d6+3:")

    }


    @Test
    fun `test parse multiattack with no multiattack`(){
        var action1=Action(4,listOf(),listOf(Damage("2d4+1",mockk<DamageType>())),mockk<Dc>(),"","",mockk<Options>(),mockk())
        var action2=Action(3,listOf(),listOf(Damage("1d6+3",mockk<DamageType>())),mockk<Dc>(),"","",mockk<Options>(),mockk())
        var monster = mockk<MonsterDetails>(){
            every{actions} returns mutableListOf(action1,action2)
        }

        val helper= MonsterDetailHelper(monster)
        val damage=helper.parseMultiAttack(helper.findMultiAttack())

        assertThat(damage).isNotNull()
        assertThat(damage).isEqualTo(6)
    }

    @Test
    fun `test parse multiattack with multiattack`(){
        var action1=Action(4,listOf(),listOf(Damage("2d4+1",mockk<DamageType>())),mockk<Dc>(),"","one",mockk<Options>(),mockk())
        var action2=Action(3,listOf(),listOf(Damage("1d6+3",mockk<DamageType>())),mockk<Dc>(),"","two",mockk<Options>(),mockk())
        var action3=Action(3,listOf(),listOf(Damage("",mockk<DamageType>())),mockk<Dc>(),"","Multiattack",mockk<Options>(){
                                                                                                                          every{from} returns listOf(From(X(1,"one",""),X(1,"two","")))
        },mockk())
        var monster = mockk<MonsterDetails>(){
            every{actions} returns mutableListOf(action1,action2,action3)
        }

        val helper= MonsterDetailHelper(monster)
        val damage=helper.parseMultiAttack(helper.findMultiAttack())

        assertThat(damage).isNotNull()
        assertThat(damage).isEqualTo(12)
    }


}