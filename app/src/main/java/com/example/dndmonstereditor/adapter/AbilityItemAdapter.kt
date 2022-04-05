package com.example.dndmonstereditor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dndmonstereditor.databinding.SpecialAbilityListItemBinding
import com.example.dndmonstereditor.model.monsterDetails.SpecialAbility

class AbilityItemAdapter (
    private val abilityList: List<SpecialAbility>?
    ):RecyclerView.Adapter<AbilityHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbilityHolder {
        val abilityBinding =
            SpecialAbilityListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return AbilityHolder(abilityBinding)
    }

    override fun onBindViewHolder(holder: AbilityHolder, position: Int) {
        holder.bind(abilityList?.get(position))
    }

    override fun getItemCount(): Int = abilityList?.size?:0
}

class AbilityHolder(private val binding: SpecialAbilityListItemBinding): RecyclerView.ViewHolder(binding.root){

    fun bind (ability: SpecialAbility?){
        binding.abilityName.text=ability?.name ?: ""
        binding.abilityDescription.text=ability?.desc ?:""

    }
}