package com.example.dndmonstereditor.logic


import android.util.Log
import com.example.dndmonstereditor.model.monsterDetails.MonsterDetails
import com.example.dndmonstereditor.modelhelpers.MonsterDetailHelper
import java.util.Collections.max

class CRCalculator(private val monster:MonsterDetails) {

    //calculates the CR of the monster given the currently set stats
    fun getCR (): Double {
        val helper = MonsterDetailHelper(monster)
        val damage = helper.parseMultiAttack(helper.findMultiAttack())
        val ac= monster.armor_class
        val hp= monster.hit_points
        val toHit= helper.maxToHit()

        val strSV=((helper.getProficiency("Saving Throw: STR")?:((monster.strength-10)/2)))
        val dexSV =((helper.getProficiency("Saving Throw: DEX")?:((monster.dexterity-10)/2)))
        val conSV=((helper.getProficiency("Saving Throw: CON")?:((monster.constitution-10)/2)))
        val intSV=((helper.getProficiency("Saving Throw: INT")?:((monster.intelligence-10)/2)))
        val wisSV=((helper.getProficiency("Saving Throw: WIS")?:((monster.wisdom-10)/2)))
        val chaSV=((helper.getProficiency("Saving Throw: CHA")?:((monster.charisma-10)/2)))


        val acCR = when (ac) {
            in 0..12 -> 0.0
            13-> 0.5
            else-> (ac - 13) * 3.0
        }
        val hpCR = when (hp) {
            in 0..3 -> 0.0
            in 4..9 ->0.125
            in 10..15 ->0.25
            in 16..24 ->0.5
            in 25..30 ->1.0
            else-> hp / 15.0
        }

        val damageCR =  when (damage) {
            in 0..1 -> 0.0
            in 2..3 -> 0.125
            in 4..5 -> 0.25
            in 6..8 ->0.5
            in 9..10 -> 1.0
            else -> damage / 5.0
        }
        val toHitCR= when (toHit){
            in 0..2 -> 0.0
            3 ->0.25
            4 ->1.0
            else->(toHit-4)*2.0
        }

        val saveCR = when (val save:Int = max(listOf(strSV,dexSV,conSV,intSV,wisSV,chaSV))){
            in -3..1 -> 0.0
            2 -> 0.25
            3 -> 1.0
            else -> (save-3)*2.0
        }

        return ((acCR+hpCR+saveCR)/3.0+(damageCR+toHitCR)/2.0)/2.0
    }
}