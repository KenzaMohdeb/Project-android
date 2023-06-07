package com.uqam.mentallys.view.ui.resource.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentSearchListBinding
import com.uqam.mentallys.view.MainActivity
import com.uqam.mentallys.view.ui.resource.common.ResourcePreviewAdapter
import com.uqam.mentallys.view.ui.resource.search.SearchFragment
import com.uqam.mentallys.viewmodels.resource.ResourceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_search_list) {

    private val resourceViewModel: ResourceViewModel by viewModels({ requireParentFragment() })
    private var resourcePreviewAdapter = ResourcePreviewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSearchListBinding.bind(view)
        binding.apply {
            // Implement the navigation on the floating action button
            actionSearchListFragmentToSearchMapFragment.setOnClickListener {
                (requireParentFragment() as? SearchFragment)?.applyMapFragment()
            }
            // Connect the medic section recyclers to its Adapter and ViewModel
            fragmentSearchListRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = resourcePreviewAdapter
                setHasFixedSize(true)
            }
            fragmentSearchMissingResourceContainer.setOnClickListener{
                val mailIntent = Intent(Intent.ACTION_SENDTO)
                mailIntent.data = Uri.parse("mailto:admin@mentallys.com")
                mailIntent.putExtra(Intent.EXTRA_SUBJECT,
                    context?.getText(R.string.email_missing_subject).toString())
                mailIntent.putExtra(Intent.EXTRA_TEXT, context?.getText(R.string.email_missing_subject))
                if (mailIntent.resolveActivity(context?.packageManager!!) != null) {
                    startActivity(mailIntent)
                }
            }
        }
        resourceViewModel.list.observe(viewLifecycleOwner) { resources ->
            resourcePreviewAdapter.submitList(resources)
            when (resources.isEmpty()) {
                false -> binding.fragmentSearchNoResultContainer.visibility = View.GONE
                true -> binding.fragmentSearchNoResultContainer.visibility = View.VISIBLE
            }
        }

    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).restoreDefaultNavigationBehavior()
    }

}