package com.uqam.mentallys.view.ui.message


import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentMainChatBinding
import com.uqam.mentallys.viewmodels.homepage.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainChatFragment : Fragment(R.layout.fragment_main_chat) {

    private lateinit var binding: FragmentMainChatBinding

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private val viewModel by viewModels<HomeViewModel>()
    private var navController: NavController?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {

        binding = FragmentMainChatBinding.inflate(layoutInflater)

        viewPager = binding.chatFragmentsContainer

        setToolBarMenu()
        setupViewPager(viewPager!!)
        setTabs()
        setTabsClickEvents()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel.getUserInfo()

        binding.apply {
            homeToolbar.inflateMenu(R.menu.top_menu_home_fragment)
            homeToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.login -> {
                        navController!!.navigate(R.id.loginFragment)
                        true
                    }
                    R.id.Logout -> {
                        viewModel.logout()
                        navController!!.navigate(R.id.mainChatFragment)
                        true
                    }
                    R.id.createAccount -> {
                        navController!!.navigate(R.id.registerFragment)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setToolBarMenu() {
        // Observe the login info, passing in this activity as the LifecycleOwner and the observer.
        viewModel.userInfo.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                if(it.firstName != null) {
                    // Update the UI, in this case, the user login name.
                    binding.apply {
                        homeToolbar.menu.getItem(3).title = "Bonjour " + it.firstName
                        homeToolbar.menu.getItem(3).isVisible = true
                        homeToolbar.menu.getItem(2).isVisible = false
                        homeToolbar.menu.getItem(4).isVisible = false
                        homeToolbar.menu.getItem(5).isVisible = true
                    }
                }
            }
        }
    }

    private fun setTabsClickEvents() {
        binding.topMessagesMenu.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> {
                        val tabShape = tab.customView as LinearLayout
                        tabShape.findViewById<TextView>(R.id.first_tab_title)
                            .setTypeface(null, Typeface.BOLD)
                        tabShape.findViewById<ImageView>(R.id.first_tab_img)
                            .visibility = View.GONE
                    }
                    1 -> {
                        val tabShape = tab.customView as LinearLayout
                        tabShape.findViewById<TextView>(R.id.second_tab_title)
                            .setTypeface(null, Typeface.BOLD)
                        tabShape.findViewById<ImageView>(R.id.second_tab_img)
                            .visibility = View.GONE
                    }
                    2 -> {
                        val tabShape = tab.customView as LinearLayout
                        tabShape.findViewById<TextView>(R.id.third_tab_title)
                            .setTypeface(null, Typeface.BOLD)
                        tabShape.findViewById<ImageView>(R.id.third_tab_img)
                            .visibility = View.GONE
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
                        val tabShape = tab.customView as LinearLayout
                        tabShape.findViewById<TextView>(R.id.first_tab_title)
                            .setTypeface(null, Typeface.NORMAL)
                    }
                    1 -> {
                        val tabShape = tab.customView as LinearLayout
                        tabShape.findViewById<TextView>(R.id.second_tab_title)
                            .setTypeface(null, Typeface.NORMAL)
                    }
                    2 -> {
                        val tabShape = tab.customView as LinearLayout
                        tabShape.findViewById<TextView>(R.id.third_tab_title)
                            .setTypeface(null, Typeface.NORMAL)
                    }
                }
            }
        })
    }

    private fun setTabs() {
        tabLayout = binding.topMessagesMenu
        tabLayout!!.setupWithViewPager(viewPager)

        val headerView = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        val tab1 = headerView.findViewById(R.id.first_tab) as LinearLayout
        val tab2 = headerView.findViewById(R.id.second_tab) as LinearLayout
        val tab3 = headerView.findViewById(R.id.third_tab) as LinearLayout

        tab1.findViewById<TextView>(R.id.first_tab_title).setTypeface(null, Typeface.BOLD)
        tab1.findViewById<TextView>(R.id.first_tab_title).setTextSize(
            TypedValue.COMPLEX_UNIT_DIP,
            16F
        )
        tab2.findViewById<TextView>(R.id.second_tab_title).setTextSize(
            TypedValue.COMPLEX_UNIT_DIP,
            16F
        )
        tab3.findViewById<TextView>(R.id.third_tab_title).setTextSize(
            TypedValue.COMPLEX_UNIT_DIP,
            16F
        )

        tabLayout!!.getTabAt(0)!!.customView = tab1
        tabLayout!!.getTabAt(1)!!.customView = tab2
        tabLayout!!.getTabAt(2)!!.customView = tab3
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(MessagesFragment(), R.string.messages.toString())
        adapter.addFragment(MessengesNotifications(), R.string.notification.toString())
        adapter.addFragment(MessengesDocuments(), R.string.documents.toString())
        viewPager.adapter = adapter
    }
}