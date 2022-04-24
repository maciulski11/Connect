package com.example.connect.screens

import androidx.navigation.fragment.findNavController
import com.example.connect.R
import com.example.connect.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_find_people.*

class FindPeopleFragment: BaseFragment(){
    override val layout = R.layout.fragment_find_people

    override fun subscribeUi() {

        returnFind()
    }

    fun returnFind(){
        return_findBT.setOnClickListener {
            findNavController()
                .navigate(FindPeopleFragmentDirections.actionFindPeopleFragmentToFindFragment().actionId)
        }
    }

    override fun unsubscribeUi() {

    }
}