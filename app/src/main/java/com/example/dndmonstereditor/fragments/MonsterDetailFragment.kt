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
import com.example.dndmonstereditor.roomdb.MonsterDBItem
import com.example.dndmonstereditor.viewmodel.MonsterViewModel
import com.example.dndmonstereditor.viewmodel.States
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MonsterDetailFragment : Fragment() {

    private lateinit var monsterFromList:MonsterFromList
    private var changes:MonsterDBItem? = null

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
            changes=it.getParcelable("Changes")
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

                    if (changes!=null){
                        val helper = MonsterDetailHelper(monster)
                        helper.putAttacksString(changes!!.monster.attacks)
                        monster.armor_class= changes!!.monster.ac
                        monster.hit_points= changes!!.monster.hp
                        monster.challenge_rating = changes!!.monster.cr.toFloat()
                        binding.saveName.setText(changes!!.uniqueName)
                    }

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

        val helper=MonsterDetailHelper(monsterDetails)
        binding.name.text=monsterDetails.name
        binding.ACET.setText(monsterDetails.armor_class.toString())
        binding.HPET.setText(monsterDetails.hit_points.toString())

        binding.CRET.setText(monsterDetails.challenge_rating.toString())

        binding.strET.setText((helper.getProficiency("Saving Throw: STR")?:((monsterDetails.strength-10)/2)).toString())
        binding.dexET.setText((helper.getProficiency("Saving Throw: DEX")?:((monsterDetails.dexterity-10)/2)).toString())
        binding.conET.setText((helper.getProficiency("Saving Throw: CON")?:((monsterDetails.constitution-10)/2)).toString())
        binding.intelET.setText((helper.getProficiency("Saving Throw: INT")?:((monsterDetails.intelligence-10)/2)).toString())
        binding.wisET.setText((helper.getProficiency("Saving Throw: WIS")?:((monsterDetails.wisdom-10)/2)).toString())
        binding.chaET.setText((helper.getProficiency("Saving Throw: CHA")?:((monsterDetails.charisma-10)/2)).toString())

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
                attacks,
                binding.CRET.text.toString()
            )

            monsterViewModel.saveToDataBase(MonsterDBItem(binding.saveName.text.toString(),changes))

            Toast.makeText(context,"Saved as "+binding.saveButton.text.toString(),Toast.LENGTH_LONG)

        }
    }

}