package com.example.dndmonstereditor.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dndmonstereditor.adapter.AbilityItemAdapter
import com.example.dndmonstereditor.adapter.ActionItemAdapter
import com.example.dndmonstereditor.adapter.LActionItemAdapter
import com.example.dndmonstereditor.databinding.FragmentMonsterDetailBinding
import com.example.dndmonstereditor.logic.CRCalculator
import com.example.dndmonstereditor.model.MonsterFromList
import com.example.dndmonstereditor.model.monsterDetails.MonsterDetails
import com.example.dndmonstereditor.modelhelpers.MonsterDetailHelper
import com.example.dndmonstereditor.modelhelpers.SavedMonsterChanges
import com.example.dndmonstereditor.viewmodel.MonsterViewModel
import com.example.dndmonstereditor.viewmodel.States
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MonsterDetailFragment : Fragment() {

    private lateinit var monsterFromList:MonsterFromList

    private val monsterViewModel by viewModels<MonsterViewModel>()

    private val binding by lazy{
        FragmentMonsterDetailBinding.inflate(layoutInflater)
    }

    private lateinit var abilityItemAdapter:AbilityItemAdapter
    private lateinit var lActionItemAdapter:LActionItemAdapter
    private lateinit var actionItemAdapter:ActionItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
           monsterFromList = it.getParcelable("Monster")!!
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        monsterViewModel.monstersLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is States.LOADING -> {
                    //Toast.makeText(requireContext(), "loading...", Toast.LENGTH_LONG).show()
                }
                is States.SUCCESSDET -> {
                    val monster = state.response
                    bind(monster)
                    abilityItemAdapter=AbilityItemAdapter(monster.special_abilities)
                    lActionItemAdapter=LActionItemAdapter(monster.legendary_actions)
                    actionItemAdapter= ActionItemAdapter(monster.actions)
                    binding.abilityRV.apply{
                        layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        adapter=abilityItemAdapter
                    }
                    binding.legendaryActionsRV.apply{
                        layoutManager=LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        adapter=lActionItemAdapter
                    }
                    binding.attacksRV.apply{
                        layoutManager=LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        adapter=actionItemAdapter
                    }

                }
                is States.ERROR -> {
                    Toast.makeText(requireContext(), state.error.localizedMessage, Toast.LENGTH_LONG).show()
                    Log.d("MDF", "onCreateView: "+state.error.localizedMessage)
                }
                else -> {}
            }

        }

        monsterViewModel.getMonsterDetails(monsterFromList.index)

        return binding.root
    }

    private fun bind(monsterDetails: MonsterDetails){
        binding.name.text=monsterDetails.name
        binding.ACET.setText(monsterDetails.armor_class.toString())
        binding.HPET.setText(monsterDetails.hit_points.toString())

        binding.CRET.setText(monsterDetails.challenge_rating.toString())

        binding.update.setOnClickListener {
            binding.CRET.setText(CRCalculator(monsterDetails).getCR().toString().take(4))
        }

        binding.ACET.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.ACET.text.toString()!="")monsterDetails.armor_class=binding.ACET.text.toString().toInt()
            }
            override fun afterTextChanged(p0: Editable?) {}

        })


        binding.HPET.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

               if (binding.HPET.text.toString()!="") monsterDetails.hit_points=binding.HPET.text.toString().toInt()
            }
            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.saveButton.setOnClickListener {

            var attacks = MonsterDetailHelper(monsterDetails).getAttacksString()

            var changes = SavedMonsterChanges(
                monsterDetails.index,
                monsterDetails.armor_class,
                monsterDetails.hit_points,
                attacks
            )



        }
    }

}