package com.example.dndmonstereditor.adapter


import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dndmonstereditor.databinding.ActionListItemBinding
import com.example.dndmonstereditor.databinding.AttackListItemBinding
import com.example.dndmonstereditor.databinding.AttackWithAdditionalListItemBinding
import com.example.dndmonstereditor.model.monsterDetails.Action
import com.example.dndmonstereditor.modelhelpers.ActionHelper
import com.example.dndmonstereditor.modelhelpers.CalculationHelper


//adapter class for the actions recycle viewer in the details fragment
//holds attacks and standard actions
class ActionItemAdapter (
    private val actionList: List<Action>?
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    private object Const{
        const val ACTION =0
        const val ATTACK =1
        const val ATTACKPLUS=2
    }

    override fun getItemViewType(position: Int): Int {
       val helper = ActionHelper(actionList?.get(position))

        return if (helper.isAttack()){
            if (helper.findAdditionalEffects()!=null) {
                Const.ATTACKPLUS
            }else Const.ATTACK
        }
        else Const.ACTION
    }

    //creates a viewholder either a standard action or an attack one as needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder {

        if (viewType==Const.ACTION) {
            val actionBinding =
                ActionListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return ActionHolder(actionBinding)
        }
        if (viewType==Const.ATTACK) {
            val attackBinding =
                AttackListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return AttackHolder(attackBinding)
        }
        if (viewType==Const.ATTACKPLUS){
            val attackBinding =
                AttackWithAdditionalListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return AttackAdditionalEffectsHolder(attackBinding)
        }
        val actionBinding =
            ActionListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ActionHolder(actionBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val helper = ActionHelper(actionList?.get(position))

        //binds either standard action or attack
        if (helper.isAttack())
                if (helper.findAdditionalEffects()==null) (holder as AttackHolder).bind(actionList?.get(position))
                else (holder as AttackAdditionalEffectsHolder).bind(actionList?.get(position))
        else (holder as ActionHolder).bind(actionList?.get(position))
    }

    override fun getItemCount(): Int = actionList?.size?:0
}

//viewholder for standard actions in the recycle viewer
class ActionHolder(private val binding: ActionListItemBinding): RecyclerView.ViewHolder(binding.root){

    fun bind (ability: Action?){
        binding.actionName.text=ability?.name ?: ""
        binding.actionDescription.text=ability?.desc ?:""

    }
}


//viewholder for attack actions in the recycle viewer
class AttackHolder (private val binding: AttackListItemBinding): RecyclerView.ViewHolder(binding.root){

     fun bind (attack: Action?){
         val helper = ActionHelper(attack)
         binding.attackName.text=attack?.name.toString()
         val dice = helper.getDice()
         binding.toHitET.setText(attack?.attack_bonus.toString())
         binding.diceNumber.setText(dice.number.toString())
         binding.diceSize.setText(dice.size.toString())
         binding.bonus.setText(dice.bonus.toString())

         binding.toHitET.addTextChangedListener(object:TextWatcher{
             override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

             override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                 if (attack != null) {
                     if (binding.toHitET.text.toString()!="")attack.attack_bonus=binding.toHitET.text.toString().toInt()
                 }
             }
             override fun afterTextChanged(p0: Editable?) {}

         })

         binding.diceNumber.addTextChangedListener(object:TextWatcher{
             override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

             override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                 if (attack != null) {
                     attack.damage?.get(0)?.damage_dice = diceString()
                 }
             }
             override fun afterTextChanged(p0: Editable?) {}

         })

         binding.diceSize.addTextChangedListener(object:TextWatcher{
             override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

             override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                 if (attack != null) {
                     attack.damage?.get(0)?.damage_dice = diceString()
                 }
             }
             override fun afterTextChanged(p0: Editable?) {}

         })

         binding.bonus.addTextChangedListener(object:TextWatcher{
             override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

             override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                 if (attack != null) {
                     attack.damage?.get(0)?.damage_dice = diceString()

                 }
             }
             override fun afterTextChanged(p0: Editable?) {}

         })


     }

     fun diceString():String{
         return binding.diceNumber.text.toString()+"d"+binding.diceSize.text.toString()+"+"+binding.bonus.text.toString()
     }
 }


//viewholder for attack actions with additional effects in the recycle viewer
class AttackAdditionalEffectsHolder (private val binding: AttackWithAdditionalListItemBinding): RecyclerView.ViewHolder(binding.root){

    fun bind (attack: Action?){
        val helper = ActionHelper(attack)
        binding.attackName.text=attack?.name.toString()
        val dice = helper.getDice()
        binding.toHitET.setText(attack?.attack_bonus.toString())
        binding.diceNumber.setText(dice.number.toString())
        binding.diceSize.setText(dice.size.toString())
        binding.bonus.setText(dice.bonus.toString())

        val addedEffects=helper.findAdditionalEffects()
        val addedDice = addedEffects?.let { CalculationHelper.getDice(it.damage)}

        binding.dcET.setText(addedEffects!!.dc.toString())
        binding.diceNumberAE.setText(addedDice!!.number.toString())
        binding.diceSizeAE.setText(addedDice.size.toString())
        binding.bonusAE.setText(addedDice.bonus.toString())
        binding.desc.text=addedEffects.desc
        binding.saveType.text=addedEffects.saveType

        binding.toHitET.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (attack != null) {
                    if (binding.toHitET.text.toString()!="")attack.attack_bonus=binding.toHitET.text.toString().toInt()
                }
            }
            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.diceNumber.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (attack != null) {
                    attack.damage?.get(0)?.damage_dice = diceString()
                }
            }
            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.diceSize.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (attack != null) {
                    attack.damage?.get(0)?.damage_dice = diceString()
                }
            }
            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.bonus.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (attack != null) {
                    attack.damage?.get(0)?.damage_dice = diceString()

                }
            }
            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.diceNumberAE.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (attack != null) {
                    val aHelper=ActionHelper(attack)
                    aHelper.changeAdditionalEffectsDamage(addedDiceString())
                }
            }
            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.diceSizeAE.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (attack != null) {
                    val aHelper=ActionHelper(attack)
                    aHelper.changeAdditionalEffectsDamage(addedDiceString())
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.bonusAE.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (attack != null) {
                    val aHelper=ActionHelper(attack)
                    aHelper.changeAdditionalEffectsDamage(addedDiceString())
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.dcET.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (attack != null) {
                    val aHelper=ActionHelper(attack)
                    if (binding.dcET.text.toString()!="") {
                        aHelper.changeAdditionalEffectsDC(binding.dcET.text.toString())
                    }
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    fun diceString():String{
        return binding.diceNumber.text.toString()+"d"+binding.diceSize.text.toString()+"+"+binding.bonus.text.toString()
    }
    fun addedDiceString():String{
        return binding.diceNumberAE.text.toString()+"d"+binding.diceSizeAE.text.toString()+"+"+binding.bonusAE.text.toString()
    }
}