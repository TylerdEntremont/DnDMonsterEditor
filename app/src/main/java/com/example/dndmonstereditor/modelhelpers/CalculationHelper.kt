package com.example.dndmonstereditor.modelhelpers

object CalculationHelper {

    fun getAverageDamage(dice:Dice):Int{
        return ((dice.number*(dice.size+1))/2)+dice.bonus
    }

    fun getDice (diceString:String):Dice{

        val split1 = diceString.split("d")
        if (split1.size<2)return Dice(0,0, split1[0].toInt())
        var split2 = split1[1].split("+")
        var sign=1

        if (split2.size<2){
            split2=split1[1].split("-")
            sign=-1
        }

        val bonus = if (split2.size>1) split2[1].toInt() else 0

        return Dice(split1[0].toInt(),split2[0].toInt(),bonus*sign)
    }

}