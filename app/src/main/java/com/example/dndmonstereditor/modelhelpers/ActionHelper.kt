package com.example.dndmonstereditor.modelhelpers


import android.util.Log
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

    fun findAdditionalEffects():AdditionalEffects?{
        val additionalEffects=AdditionalEffects()
        if (isAttack()){
            if (action==null) return null
            val split = action.desc.split("DC ")
            if (split.size>1){
                val split2 = split[1].split(" ")
                if (split2.size>1) {
                    additionalEffects.dc = split2[0].take(2).toInt()
                    additionalEffects.saveType = split2[1]

                    val split3 = split[1].split("saving throw")

                    if(split3.size > 1) {
                        val split4 = split3[1].split(" (")[1].split(") ")
                        additionalEffects.damage = split4[0]
                        additionalEffects.desc = split4[1]
                        return additionalEffects
                    }
                    else{
                        additionalEffects.damage = "0d0+0"
                        additionalEffects.desc = split3[0].split(")")[1]
                        return additionalEffects
                    }
                }
                return null
            }
            return null
        }
        return null
    }

    fun changeAdditionalEffectsDamage(newDamage:String){
        if (isAttack()) {
            if (action == null) return
            val split = action.desc.split("DC ")
            if (split.size > 1) {

                val split3 = split[1].split("saving throw")

                if (split3.size > 1) {
                    val holdSplit = split3[1].split(" (")
                    val split4 = holdSplit[1].split(") ")
                    action.desc =
                        split[0] + "DC " + split3[0] + "saving throw" + holdSplit[0] + " (" + newDamage + ") " + split4[1]
                }
            }
        }
        Log.d("AH", "changeAdditionalEffectsDamage: "+ (action?.desc ?: ""))
    }

    fun changeAdditionalEffectsDC(newDC:String){
        if (isAttack()){
            if (action==null) return
            val split = action.desc.split("DC ")
            val split2= split[1].split(" ")

            var holdString=split[0]+"DC "+newDC+" "
            for (x in 1 until split2.size){
                holdString+=split2[x]+" "
            }

            action.desc=holdString
        }
        Log.d("AH", "changeAdditionalEffectsDamage: "+ (action?.desc ?: ""))
    }

}