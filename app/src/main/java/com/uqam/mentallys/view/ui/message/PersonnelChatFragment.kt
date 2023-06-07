package com.uqam.mentallys.view.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentPersonnelChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonnelChatFragment : Fragment(R.layout.fragment_personnel_chat) {

    private lateinit var binding: FragmentPersonnelChatBinding
    private val args:PersonnelChatFragmentArgs by navArgs()
    private var navController: NavController? = null
    private val viewModel: SharedChatUserInfo by activityViewModels()
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private var emailProfessional:String?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentPersonnelChatBinding.inflate(layoutInflater)
        viewPager2 = binding.viewPager2
        viewPager2
        tabLayout = binding.topMessagesMenu
        binding.chatPersonName.text  = args.SelectedResourceInfo.fullUserName
        emailProfessional = args.SelectedResourceInfo.email

        getListChatThreadClient()

        val id = requireContext().resources.getIdentifier(args.SelectedResourceInfo.userImage, "drawable", requireContext().packageName)
        binding.chatPersonImage.setImageResource(id)

       /* Picasso.get()
            .load(imagePath!!)
            .resize(300, 300)
            .centerCrop()
            .into(binding.chatPersonImage)*/
        viewModel.setSharedObject(SelectedUserInfo(args.SelectedResourceInfo.id,
            args.SelectedResourceInfo.fullUserName,
            args.SelectedResourceInfo.userImage, args.SelectedResourceInfo.email,
        args.SelectedResourceInfo.CommunicationUserId, args.SelectedResourceInfo.AccessToken,
        args.SelectedResourceInfo.AccessTokenExpiresOn))
        setupViewPager(viewPager2)
        //setTabs()
        setTabsClickEvents()
        return binding.root
    }
    private fun getListChatThreadClient() {
        viewModel.chatParticipantInfo.observe(viewLifecycleOwner) {
            binding.chatPersonName.text = it.name
            binding.chatPersonImage.setImageResource(it.icon)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        binding.backArrow.setOnClickListener {
           if(viewModel.participantListVisible.value == true)
           {
               viewModel.showParticipantList(false)
               emailProfessional?.let { it1 ->
                   val action = PersonnelChatFragmentDirections.actionPersonnelChatFragmentToIntervenantProfileFragment(
                       it1
                   )
                   navController!!.navigate(action)
               }
           }
            else {
               tabLayout.getTabAt(0)?.select()
               viewModel.showParticipantList(true)
           }
        }
    }
    private fun setTabsClickEvents() {
        binding.topMessagesMenu.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> {
                        /*val tabShape = tab.customView as LinearLayout
                        tabShape.findViewById<TextView>(R.id.first_tab_title)
                            .setTypeface(null, Typeface.BOLD)
                        tabShape.findViewById<ImageView>(R.id.first_tab_img)
                            .visibility = View.GONE*/
                    }
                    1 -> {
                        /*val tabShape = tab.customView as LinearLayout
                        tabShape.findViewById<TextView>(R.id.second_tab_title)
                            .setTypeface(null, Typeface.BOLD)
                        tabShape.findViewById<ImageView>(R.id.second_tab_img)
                            .visibility = View.GONE*/
                    }
                    2 -> {
                        /*val tabShape = tab.customView as LinearLayout
                        tabShape.findViewById<TextView>(R.id.third_tab_title)
                            .setTypeface(null, Typeface.BOLD)
                        tabShape.findViewById<ImageView>(R.id.third_tab_img)
                            .visibility = View.GONE*/
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Write code to handle tab reselect
                //Toast.makeText(context,tab!!.position, Toast.LENGTH_SHORT).show()
                // tab.customView?.findViewById<TextView>(R.id.first_tab_title)?.setTextColor(R.color.white)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> {
                        /*val tabShape = tab.customView as LinearLayout
                        tabShape.findViewById<TextView>(R.id.first_tab_title)
                            .setTypeface(null, Typeface.NORMAL)*/
                    }
                    1 -> {
                       /* val tabShape = tab.customView as LinearLayout
                        tabShape.findViewById<TextView>(R.id.second_tab_title)
                            .setTypeface(null, Typeface.NORMAL)*/
                    }
                    2 -> {
                       /* val tabShape = tab.customView as LinearLayout
                        tabShape.findViewById<TextView>(R.id.third_tab_title)
                            .setTypeface(null, Typeface.NORMAL)*/
                    }
                }
            }
        })
    }

    private fun setupViewPager(viewPager2: ViewPager2) {
        tabLayout = binding.topMessagesMenu

        ViewPager2Adapter(childFragmentManager,lifecycle, tabLayout, viewPager2)
            .apply {
                addFragment(PersonnelMessageFragment(),"Messages")
                addFragment(PersonnelNotificationsFragment(),"Notifications")
                addFragment(PersonnelDocumentsFragment(),"Document")
            }.attach()
    }
    /***************************End*******************/

}