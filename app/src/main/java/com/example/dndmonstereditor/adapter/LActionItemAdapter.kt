package com.example.dndmonstereditor.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dndmonstereditor.databinding.LegendaryActionListItemBinding
import com.example.dndmonstereditor.model.monsterDetails.LegendaryAction

//adapter for the legendary actions in the legendary actions recycle viewer of the details fragment
class LActionItemAdapter (
    private val actions: List<LegendaryAction>?
): RecyclerView.Adapter<LActionHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LActionHolder {
        val lActionBinding =
            LegendaryActionListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return LActionHolder(lActionBinding)
    }

    override fun onBindViewHolder(holder: LActionHolder, position: Int) {
        holder.bind(actions?.get(position))
    }

    override fun getItemCount(): Int = actions?.size?:0
}

class LActionHolder(private val binding: LegendaryActionListItemBinding): RecyclerView.ViewHolder(binding.root){

    fun bind (ability: LegendaryAction?){
        binding.lActionName.text=ability?.name ?: ""
        binding.lActionDescription.text=ability?.desc ?:""

    }
}