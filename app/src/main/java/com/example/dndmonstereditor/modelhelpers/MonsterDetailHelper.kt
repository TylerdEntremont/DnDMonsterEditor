package com.example.dndmonstereditor.modelhelpers

import com.example.dndmonstereditor.model.monsterDetails.Action
import com.example.dndmonstereditor.model.monsterDetails.MonsterDetails

class MonsterDetailHelper (val monster: MonsterDetails) {

    fun findMultiAttack():Action?{
        return findActionByName("Multiattack")
    }

    fun maxToHit():Int{
        var maxToHit=0

        for (action in monster.actions){
            if (action.attack_bonus!=null){
                if (action.attack_bonus!! >maxToHit) maxToHit= action.attack_bonus!!
            }
        }

        return maxToHit
    }

    fun parseMultiAttack(multiAttack: Action?):List<String>{

        if (multiAttack!=null) {
            val attacks = mutableListOf<String>()

            for (options in multiAttack.options?.from!!) {
                if (options.a != null) for (x in 1..options.a.count) attacks.add(options.a.name)
                if (options.b != null) for (x in 1..options.b.count) attacks.add(options.b.name)
                if (options.c != null) for (x in 1..options.c.count) attacks.add(options.c.name)
                if (options.d != null) for (x in 1..options.d.count) attacks.add(options.d.name)
                if (options.e != null) for (x in 1..options.e.count) attacks.add(options.e.name)
            }

            return attacks
        }
            for (action in monster.actions){
                if (action.attack_bonus!=null){
                    return listOf(action.name!!)
                }
            }
        return listOf()

    }

    fun damagePerTurn(attacks:List<String>): Int{
        var damage=0
        for (attack in attacks){
           damage+= CalculationHelper.getAverageDamage(ActionHelper(findActionByName(attack)).getDice())
        }

        return damage
    }


    fun findActionByName(name:String): Action? {
        if (monster.actions!=null) {
            for (action in monster.actions) {
                if (action.name ==name) return action
            }
        }
        return null
    }

    fun getAttacksString():String{
         var attacks=""

        for (action in monster.actions){
            if (action.attack_bonus!=null){
                attacks+=action.attack_bonus.toString()+","
                attacks+=action.damage[0].damage_dice+":"
            }
        }
        return attacks
    }

    fun putAttacksString(attacks:String){
        val split1 = attacks.split(":")
        var i =0

        for (action in monster.actions){
            if (action.attack_bonus!=null){
                val split2=split1[i].split(",")
                action.attack_bonus=split2[0].toInt()
                action.damage[0].damage_dice=split2[1]
            }
        }

    }

}