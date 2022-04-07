package com.example.dndmonstereditor.modelhelpers

import com.example.dndmonstereditor.model.monsterDetails.Action
import com.example.dndmonstereditor.model.monsterDetails.MonsterDetails
import com.example.dndmonstereditor.model.monsterDetails.Proficiency
import com.example.dndmonstereditor.model.monsterDetails.SpecialAbility

//helper class for actions performed on the full set of monster details
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

    //returns a list of all the attacks performed in the multiattack action
    fun parseMultiAttack(multiAttack: Action?):List<String>{

        if (multiAttack!=null) {
            var attacks = mutableListOf<String>()
            var maxAttacks = mutableListOf<String>()

            for (options in multiAttack.options.from!!) {
                for (x in 1..options.a.count) attacks.add(options.a.name)
                for (x in 1..options.b.count) attacks.add(options.b.name)
                for (x in 1..options.c.count) attacks.add(options.c.name)
                for (x in 1..options.d.count) attacks.add(options.d.name)
                for (x in 1..options.e.count) attacks.add(options.e.name)

                if (maxAttacks!=null){
                    if (damagePerTurn(attacks)>damagePerTurn((maxAttacks)))maxAttacks=attacks
                }
                else maxAttacks=attacks

                attacks= mutableListOf()
            }

            return maxAttacks
        }
            for (action in monster.actions){
                if (action.attack_bonus!=null){
                    return listOf(action.name)
                }
            }
        return listOf()

    }

    //using the list of attacks from a multiattack calculates average damage per turn
    fun damagePerTurn(attacks:List<String>): Int{
        var damage=0
        for (attack in attacks){
           damage+= CalculationHelper.getAverageDamage(ActionHelper(findActionByName(attack)).getDice())
        }

        return damage
    }


    private fun findActionByName(name:String): Action? {
            for (action in monster.actions) {
                if (action.name ==name) return action
            }
        return null
    }


    fun findAbilityByPartialName(name:String): SpecialAbility?{
        for (ability in monster.special_abilities){
            if (ability.name?.contains(name,true)==true) return ability
        }
        return null
    }

    //gets the string which represents all the attacks for this monster
    //which is in a form which can be stored to the database
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

    //takes the attack string and resets all the attacks to match that information
    fun putAttacksString(attacks:String){
        val split1 = attacks.split(":")
        var i =0

        for (action in monster.actions){
            if (action.attack_bonus!=null){
                val split2=split1[i].split(",")
                action.attack_bonus=split2[0].toInt()
                action.damage[0].damage_dice=split2[1]
                i++
            }
        }

    }

    //get the value of a proficiency from the monster by the proficiencies name
    fun getProficiency(prof:String):Int?{
        for (item in monster.proficiencies){
            if (item.proficiency?.name == prof){
                return item.value
            }
        }
        return null
    }

    //get the whole object representing the proficiency from the monster by the proficiencies name
    fun getProficiencyObject(prof:String):Proficiency?{
        for (item in monster.proficiencies){
            if (item.proficiency?.name == prof){
                return item
            }
        }
        return null
    }

}