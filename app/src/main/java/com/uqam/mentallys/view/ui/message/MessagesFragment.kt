package com.uqam.mentallys.view.ui.message

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentMessagesBinding
import com.uqam.mentallys.model.ChatResource
import com.uqam.mentallys.network.Resource
import com.uqam.mentallys.utils.handleApiError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MessagesFragment : Fragment(R.layout.fragment_messages) , ResourceAdapter.OnItemClickListener {

    private val viewModel: ResourceViewModel by viewModels()
    private val resourceAdapter = ResourceAdapter(this)
    private lateinit var binding:FragmentMessagesBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.statusBarColor = resources.getColor(R.color.white)



        navController = Navigation.findNavController(view)

        binding = FragmentMessagesBinding.bind(view)
        binding.chatSection.visibility = View.GONE
        binding.apply {
            recyclerViewEventTypes.apply {
                adapter = resourceAdapter
                layoutManager = LinearLayoutManager(requireContext(),
                    LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)

            }

            choisirIntervenant.setOnClickListener {
                navController.navigate(R.id.intervenantsFragment)
            }
        }
        observeEventTypesViewModel()
    }
    private fun observeEventTypesViewModel() {
       // viewModel.resource.observe(viewLifecycleOwner){ eventTypes ->
           // resourceAdapter.submitList(eventTypes)*/
          viewModel.fetchResourcesResponse.observe(viewLifecycleOwner){
           when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        resourceAdapter.submitList(it.value.items)
                        resourceAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.Failure -> handleApiError(it) { Toast.makeText(context, it.errorBody.toString(), Toast.LENGTH_LONG).show()}
                else ->  {
                    Toast.makeText(context, "System error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    override fun onItemClick(resource: ChatResource) {

        binding.chatSection.visibility=View.GONE
        binding.newChatSection.visibility=View.GONE

        Toast.makeText(context,resource.firstname, Toast.LENGTH_SHORT).show()


    }

}