package com.uqam.mentallys.view.ui.message

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentListIntervenantsBinding
import com.uqam.mentallys.model.ChatResourceLoc
import com.uqam.mentallys.view.ui.resource.common.ResourcePreviewAdapter
import com.uqam.mentallys.view.ui.resource.search.SearchFragment
import com.uqam.mentallys.viewmodels.resource.ResourceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListIntervenantsFragment : Fragment(R.layout.fragment_list_intervenants),
    IntervenantAdapter.OnItemClickListener, IntervenantAdapter.OnButtonClickListener {

    private val resourceViewModel: ResourceViewModel by viewModels({ requireParentFragment() })
    private val viewModel: IntervenantViewModel by viewModels()
    private val intervenantAdapter = IntervenantAdapter(this,this)
    private lateinit var binding: FragmentListIntervenantsBinding
    private var navController: NavController?=null
    private val resourcePreviewAdapter = ResourcePreviewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        binding = FragmentListIntervenantsBinding.bind(view)


        binding.apply {
            recyclerViewEventTypes.apply {
                adapter = intervenantAdapter
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL, false
                )
                setHasFixedSize(true)
            }
            viewModel.fetchResourcesLocal()
        }

        // Get the resource from the view model
        binding.apply {
            actionSearchMapFragmentToSearchListFragment.setOnClickListener {
                (requireParentFragment() as? SearchFragment)?.applyListFragment()
            }
            fragmentSearchPreview.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = resourcePreviewAdapter
                setHasFixedSize(false)
            }
        }
        resourceViewModel.fetch()
        observeEventTypesViewModel()
    }

    private fun observeEventTypesViewModel() {

        viewModel.resource.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                intervenantAdapter.submitList(it)
                intervenantAdapter.notifyDataSetChanged()
            }
        }
    }
    override fun onItemClick(resource: ChatResourceLoc) {
        findNavController().navigate(IntervenantsFragmentDirections.intervenantToProfile(resource.mail))
    }
    override fun onButtonClick(view: View) {
        view.setOnClickListener{
            required_login_message()
        }
    }
    private fun required_login_message() {
        val dialog = context?.let { it1 -> BottomSheetDialog(it1) }
        val view = layoutInflater.inflate(R.layout.login_required_dialog, null)
        val btnClose = view.findViewById<View>(R.id.item1_resource_preview_separator)

        val connectionLink = view.findViewById<TextView>(R.id.connexion_link)
        val inscriptionLink = view.findViewById<TextView>(R.id.inscription_link)
        connectionLink.setOnClickListener {
            dialog?.dismiss()
            navController!!.navigate(R.id.loginFragment)

        }

        inscriptionLink.setOnClickListener {
            dialog?.dismiss()
            navController!!.navigate(R.id.registerFragment)
        }
        btnClose.setOnClickListener {
            dialog?.dismiss()
        }
        dialog?.setCancelable(true)
        dialog?.setContentView(view)
        dialog?.show()
    }

}