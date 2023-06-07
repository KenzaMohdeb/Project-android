package com.uqam.mentallys.view.ui.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentViewPagerBinding

class ViewPagerFragment : Fragment(R.layout.fragment_view_pager) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentViewPagerBinding.bind(view)
        binding.apply {
            viewpageButton.setOnClickListener {
                val action = ViewPagerFragmentDirections.actionViewPagerFragmentToMainChatFragment()
                findNavController().navigate(action)
            }
        }
    }
}