package com.uqam.mentallys.view.ui.pathway.addeditevent

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentSavedEventBinding

class SavedEventFragment: Fragment(R.layout.fragment_saved_event) {

    private lateinit var binding: FragmentSavedEventBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSavedEventBinding.bind(view)

        binding.apply {
            fabContinueEvent.setOnClickListener {
                val action = SavedEventFragmentDirections.actionSavedEventFragmentToPathwayFragment()
                findNavController().navigate(action)
            }
        }
    }
}