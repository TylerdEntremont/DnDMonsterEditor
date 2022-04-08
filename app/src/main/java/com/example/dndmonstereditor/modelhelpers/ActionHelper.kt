package com.example.dndmonstereditor.modelhelpers

import com.example.dndmonstereditor.model.monsterDetails.Action

//helper functions for use with action objects
class ActionHelper (private val action: Action?) {

    fun isAttack():Boolean{
        return action?.attack_bonus != null
    }

    fun getDice():Dice{
        if (isAttack()){

            if (action==null) return Dice (0,0,0)
            if (action.damage==null) return Dice (0,0,0)
            return if (action.damage[0].damage_dice!=null) {
                CalculationHelper.getDice(action.damage[0].damage_dice!!)
            } else Dice(0,0,0)
        }
        else{
            return Dice(0,0,0)
        }
    }

}