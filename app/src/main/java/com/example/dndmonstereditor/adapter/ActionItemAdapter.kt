package com.example.dndmonstereditor. adapter


import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dndmonstereditor.databinding.ActionListItemBinding
import com.example.dndmonstereditor.databinding.AttackListItemBinding
import com.example.dndmonstereditor.model.monsterDetails.Action
import com.example.dndmonstereditor.modelhelpers.ActionHelper



class ActionItemAdapter (
    private val actionList: List<Action>?
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    private object Const{
        const val ACTION =0
        const val ATTACK =1
    }

    override fun getItemViewType(position: Int): Int {
       val helper = ActionHelper(actionList?.get(position))

        return if (helper.isAttack()) Const.ATTACK else Const.ACTION
    }

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
        else {
            val attackBinding =
                AttackListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return AttackHolder(attackBinding)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val helper = ActionHelper(actionList?.get(position))

        if (helper.isAttack()) (holder as AttackHolder).bind(actionList?.get(position))
        else (holder as ActionHolder).bind(actionList?.get(position))
    }

    override fun getItemCount(): Int = actionList?.size?:0
}

class ActionHolder(private val binding: ActionListItemBinding): RecyclerView.ViewHolder(binding.root){

    fun bind (ability: Action?){
        binding.actionName.text=ability?.name ?: ""
        binding.actionDescription.text=ability?.desc ?:""

    }
}

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