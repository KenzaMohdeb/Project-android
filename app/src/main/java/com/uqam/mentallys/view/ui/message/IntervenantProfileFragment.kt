package com.uqam.mentallys.view.ui.message

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import com.uqam.mentallys.R
import com.uqam.mentallys.data.responses.LoginUserInfo
import com.uqam.mentallys.databinding.FragmentIntervenantProfileBinding
import com.uqam.mentallys.view.ui.login.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@Suppress("NAME_SHADOWING")
@AndroidEntryPoint
class IntervenantProfileFragment : Fragment(R.layout.fragment_intervenant_profile){

    private val receivedArg : IntervenantProfileFragmentArgs by navArgs()
    private val viewModel: IntervenantViewModel by viewModels()
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var binding: FragmentIntervenantProfileBinding
    private var navController: NavController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        binding = FragmentIntervenantProfileBinding.bind(view)

        viewModel.fetchResourceByEmail(receivedArg.emailIntervenant)
        binding.apply {
           intervenantBackIcon.setOnClickListener{
               findNavController().navigate(IntervenantProfileFragmentDirections.ProfileToIntervenantsFragment())
           }
            professionalIcon.setOnClickListener{
                // on below line we are creating a new bottom sheet dialog.
                val dialog = context?.let { it1 -> BottomSheetDialog(it1) }
                val view = layoutInflater.inflate(R.layout.professional_more_info, null)
                val btnClose = view.findViewById<View>(R.id.item1_resource_preview_separator)
                btnClose.setOnClickListener {
                    dialog?.dismiss()
                }
                dialog?.setCancelable(true)
                dialog?.setContentView(view)
                dialog?.show()
            }
            addToFavorite.setOnClickListener{
                // on below line we are creating a new bottom sheet dialog.
                if(authViewModel.userInfo.value?.id == null) {
                    required_login_message()
                }
            }
            sendMessageBtn.setOnClickListener {
                // on below line we are creating a new bottom sheet dialog.
                if (authViewModel.userInfo.value?.id == null) {
                    required_login_message()
                } else{
                    binding.progressbar.visibility = View.VISIBLE
                    val selectedUserInfo = SelectedUserInfo(
                        viewModel.intervenantProfile.value?.idResource,
                        viewModel.intervenantProfile.value?.name,
                        viewModel.intervenantProfile.value?.icon.toString(),
                        viewModel.intervenantProfile.value?.mail,
                        viewModel.intervenantProfile.value?.CommunicationUserId,
                        viewModel.intervenantProfile.value?.AccessToken, viewModel.intervenantProfile.value?.AccessTokenExpiresOn,
                    )

                    val action = IntervenantProfileFragmentDirections.actionIntervenantProfileFragmentToPersonnelChatFragment(selectedUserInfo)
                    navController!!.navigate(action)
                }
            }

            }

        authViewModel.getUserInfo()
            // Observe the login info, passing in this activity as the LifecycleOwner and the observer.
        authViewModel.userInfo.observe(viewLifecycleOwner, Observer<LoginUserInfo> { loginUserInfo ->
                if(loginUserInfo.firstName != null) {
                    // Update the UI, in this case, the user login name.
                    //val name = loginUserInfo.firstName
                }
            })
        observeEventTypesViewModel()
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
     private fun observeEventTypesViewModel() {
         viewModel.intervenantProfile.observe(viewLifecycleOwner) {
             binding.apply {
                 //na.text= resource.id.toString()
                 /*  Picasso.get()
                       .load(resource.icon!!)
                       .resize(14, 14)
                       .centerCrop()
                       .into(itemSectorIcon)*/
                 // itemSectorName.text = toDo
                 intervenantName.text = it.name
                 // itemProfessional.text = resource.activities.joinToString()
                 intervenantSexe.text = it.sexe
                 resourceKlm.text = it.distance
                 resourceDisponibiliry.text =  it.disponible
                 Picasso.get()
                     .load(it.icon)
                     .resize(55, 55)
                     .centerCrop()
                     .into(intervenantImg)
                 intervenantDescription.text = it.description
                 //itemKlm.text= resource.distance
                 //itemDisponibiliry.text = resource.disponible
                 // eventTypeImage.setImageURI(Uri.parse(resource.profileImageUrl!!))
             }
         }

     }
}