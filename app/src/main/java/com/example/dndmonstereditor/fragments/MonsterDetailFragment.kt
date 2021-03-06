package com.example.dndmonstereditor.fragments


import android.app.AlertDialog
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
import com.example.dndmonstereditor.R
import com.example.dndmonstereditor.adapter.AbilityItemAdapter
import com.example.dndmonstereditor.adapter.ActionItemAdapter
import com.example.dndmonstereditor.adapter.LActionItemAdapter
import com.example.dndmonstereditor.databinding.FragmentMonsterDetailBinding
import com.example.dndmonstereditor.logic.CRCalculator
import com.example.dndmonstereditor.model.MonsterFromList
import com.example.dndmonstereditor.model.monsterDetails.MonsterDetails
import com.example.dndmonstereditor.model.monsterDetails.Proficiency
import com.example.dndmonstereditor.model.monsterDetails.ProficiencyDetails
import com.example.dndmonstereditor.modelhelpers.MonsterDetailHelper
import com.example.dndmonstereditor.modelhelpers.SavedMonsterChanges
import com.example.dndmonstereditor.roomdb.MonsterDBItem
import com.example.dndmonstereditor.viewmodel.MonsterViewModel
import com.example.dndmonstereditor.viewmodel.States
import dagger.hilt.android.AndroidEntryPoint


//fragment with all the monsters details which can be edited to create a new CR
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
    private lateinit var actionItemAdapter: ActionItemAdapter

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

        //listens for change in information returned from api calls
        monsterViewModel.monstersLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is States.LOADING -> {
                    binding.loadingImage.visibility=View.VISIBLE
                    binding.loadingBackground.visibility=View.VISIBLE
                }
                is States.SUCCESSDET -> {
                    binding.loadingImage.visibility=View.GONE
                    binding.loadingBackground.visibility=View.GONE
                    val monster = state.response

                    //if this is a modified monster changes the data from the api to match the changes
                    if (changes!=null){
                        setDelete()
                        val helper = MonsterDetailHelper(monster)
                        helper.putAttacksString(changes!!.monster.attacks)
                        helper.putAdditionalsString(changes!!.monster.additionals)
                        monster.armor_class= changes!!.monster.ac
                        monster.hit_points= changes!!.monster.hp
                        monster.challenge_rating = changes!!.monster.cr.toFloat()
                        binding.saveName.setText(changes!!.uniqueName)

                        setSaves(helper,monster, changes!!.monster.str,"STR")
                        setSaves(helper,monster, changes!!.monster.dex,"DEX")
                        setSaves(helper,monster, changes!!.monster.con,"CON")
                        setSaves(helper,monster, changes!!.monster.intel,"INT")
                        setSaves(helper,monster, changes!!.monster.wis,"WIS")
                        setSaves(helper,monster, changes!!.monster.cha,"CHA")

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

        //binds the displayed information
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

        //notifies user if the monster is a spellcaster since that has not been completed in the logic

        if (helper.findAbilityByPartialName("Spellcasting")!=null){
            AlertDialog.Builder(requireContext()).setMessage(R.string.isSpellCaster).show()
        }

        //sets on click and on text change listener

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


        binding.strET.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try{
                    setSaves(helper,monsterDetails, binding.strET.text.toString().toInt(),"STR")
                }catch(e:Exception){
                    Log.w("MDF", "onTextChanged: ", e)
                } //do nothing if not a valid number
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.dexET.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try{
                    setSaves(helper,monsterDetails, binding.dexET.text.toString().toInt(),"DEX")
                }catch(e:Exception){
                    Log.w("MDF", "onTextChanged: ", e)
                } //do nothing if not a valid number
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.conET.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try{
                    setSaves(helper,monsterDetails, binding.conET.text.toString().toInt(),"CON")
                }catch(e:Exception){
                    Log.w("MDF", "onTextChanged: ", e)
                } //do nothing if not a valid number
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.intelET.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try{
                    setSaves(helper,monsterDetails, binding.intelET.text.toString().toInt(),"INT")
                }catch(e:Exception){
                    Log.w("MDF", "onTextChanged: ", e)
                } //do nothing if not a valid number
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.wisET.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try{
                    setSaves(helper,monsterDetails, binding.wisET.text.toString().toInt(),"WIS")
                }catch(e:Exception){
                    Log.w("MDF", "onTextChanged: ", e)
                } //do nothing if not a valid number
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.chaET.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try{
                    setSaves(helper,monsterDetails, binding.chaET.text.toString().toInt(),"CHA")
                }catch(e:Exception){
                    Log.w("MDF", "onTextChanged: ", e)
                } //do nothing if not a valid number
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        //saves the changes to the monster to the database upon user clicking the save button
        binding.saveButton.setOnClickListener {
            val helper = MonsterDetailHelper(monsterDetails)
            val attacks = helper.getAttacksString()
            val additionals = helper.getAdditionalsString()

            val changes = SavedMonsterChanges(
                monsterDetails.index,
                monsterDetails.armor_class,
                monsterDetails.hit_points,
                attacks,
                binding.CRET.text.toString(),
                helper.getProficiency("Saving Throw: STR")?:((monsterDetails.strength-10)/2),
            helper.getProficiency("Saving Throw: DEX")?:((monsterDetails.dexterity-10)/2),
            helper.getProficiency("Saving Throw: CON")?:((monsterDetails.constitution-10)/2),
            helper.getProficiency("Saving Throw: INT")?:((monsterDetails.intelligence-10)/2),
            helper.getProficiency("Saving Throw: WIS")?:((monsterDetails.wisdom-10)/2),
            helper.getProficiency("Saving Throw: CHA")?:((monsterDetails.charisma-10)/2),
                additionals
            )

            monsterViewModel.saveToDataBase(MonsterDBItem(binding.saveName.text.toString(),changes))

            Toast.makeText(context,"Saved as "+binding.saveName.text.toString(),Toast.LENGTH_LONG).show()

        }
    }

    private fun setSaves(helper: MonsterDetailHelper, monster:MonsterDetails, value:Int, saveType:String){
        val save = helper.getProficiencyObject("Saving Throw: $saveType")
        if (save !=null){
            save.value=value
        }
        else {
            monster.proficiencies.add(Proficiency(ProficiencyDetails("", "Saving Throw: $saveType", ""),value))
        }
    }

    private fun setDelete(){
        binding.deleteButton.visibility=View.VISIBLE
        binding.deleteButton.setOnClickListener {
            AlertDialog.Builder(requireContext()).setMessage(getText(R.string.deleteQuestion).toString()+changes!!.uniqueName)
                .setPositiveButton(R.string.yes){ _, _ ->
                    monsterViewModel.delete(changes!!.uniqueName)
                    parentFragmentManager.popBackStackImmediate()
                }
                .setNegativeButton(R.string.no){_,_-> }
                .show()
        }
    }

}