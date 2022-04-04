package com.example.dndmonstereditor.logic

import android.util.Log
import com.example.dndmonstereditor.model.monsterDetails.MonsterDetails
import com.example.dndmonstereditor.modelhelpers.MonsterDetailHelper

class CRCalculator(private val monster:MonsterDetails) {

    fun getCR (): Double {
        val helper = MonsterDetailHelper(monster)
        val damage =  helper.parseMultiAttack(helper.findMultiAttack()) ?.let { helper.damagePerTurn(it) }
        val ac= monster?.armor_class ?:0
        val hp= monster?.hit_points ?: 0
        val toHit= helper.maxToHit()

        Log.d("CRC", "damage: " + damage)
        Log.d("CRC", "ac: " + ac)
        Log.d("CRC", "hp: " + hp)
        Log.d("CRC", "toHit: " + toHit)

        val acCR = when (ac) {
            in 0..12 -> 0.125
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

        return (acCR+hpCR+damageCR+toHitCR)/4.0
    }
}