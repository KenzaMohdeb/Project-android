package com.uqam.mentallys.view.ui.homepage

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentHomeBinding
import com.uqam.mentallys.viewmodels.homepage.*
import dagger.hilt.android.AndroidEntryPoint


@Suppress("DEPRECATION")
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    //    private val args: HomeFragmentArgs by navArgs()
    private val medicViewModel: MedicViewModel by viewModels()
    private val supportViewModel: SupportViewModel by viewModels()
    private val privateViewModel: PrivateViewModel by viewModels()
    private val suggestionViewModel: SuggestionViewModel by viewModels()
    private val medicAdapter = MedicAdapter()
    private val supportAdapter = SupportAdapter()
    private val privateAdapter = PrivateAdapter()
    private val suggestionAdapter = SuggestionAdapter()

    private val viewModel by viewModels<HomeViewModel>()

    private var navController: NavController?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.light_grey)
        val binding = FragmentHomeBinding.bind(view)
        navController = Navigation.findNavController(view)
        viewModel.getUserInfo()
        // Observe the login info, passing in this activity as the LifecycleOwner and the observer.
        viewModel.userInfo.observe(viewLifecycleOwner) { loginUserInfo ->
            if(loginUserInfo.firstName != null) {
                // Update the UI, in this case, the user login name.
                binding.apply {
                    homeToolbar.menu.getItem(3).title = loginUserInfo.firstName
                    homeToolbar.menu.getItem(3).isVisible = true
                    homeToolbar.menu.getItem(2).isVisible = false
                    homeToolbar.menu.getItem(4).isVisible = false
                    homeToolbar.menu.getItem(5).isVisible = true
                }


            }
        }

        // Menu loading
        binding.apply {
            homeToolbar.inflateMenu(R.menu.top_menu_home_fragment)
            homeToolbar.setNavigationIcon(R.drawable.ic_baseline_account_circle_full_24)
            homeToolbar.navigationIcon?.setTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.slate
                )
            )
            homeToolbar.setNavigationOnClickListener { // view ->
             //   findNavController().navigate(R.id.loginFragment)
            }

            homeToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.login -> {
                        navController!!.navigate(R.id.loginFragment)
                        true
                    }
                    R.id.Logout -> {
                        viewModel.logout()
                        navController!!.navigate(R.id.homeFragment)
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

        //Search section loading
        suggestionViewModel.fetchSuggestion()

        binding.apply {
            // Bind the search bar to the bottom sheet modal
            // Play a sound when the button is pressed
            homepageSearchSectionButton.isSoundEffectsEnabled = false
            homepageSearchSectionButton.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                bottomSheetDialog.setContentView(R.layout.fragment_bottom_sheet_search)
                bottomSheetDialog.show()
                val mp: MediaPlayer = MediaPlayer.create(requireContext(), R.raw.click)
                mp.start()
            }
            // Connect the suggestion recyclerView to its Adapter and ViewModel
            // homepage_search_section_recycler
            homepageSearchSectionRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = suggestionAdapter
                setHasFixedSize(false)
                canScrollHorizontally(0)
            }
        }

        // Homepage content loading
        medicViewModel.fetchMedic()
        supportViewModel.fetchSupport()
        privateViewModel.fetchPrivate()

        binding.apply {
            // Connect the medic section recyclers to its Adapter and ViewModel
            homepageMedicSectionRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = medicAdapter
                setHasFixedSize(true)
            }
            // Connect the support section recyclers to its Adapter and ViewModel
            homepageSupportSectionRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = supportAdapter
                setHasFixedSize(true)
            }
            // Connect the private section recyclers to its Adapter and ViewModel
            homepagePrivateSectionRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = privateAdapter
                setHasFixedSize(true)
            }
        }

        observeViewModels()

        // Bottom sheet setup code
        binding.apply {
            // Retrieve system short animation duration
            val animationDuration =
                resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            // Retrieve instance of the bottomSheetModal
            val bottomSheetPermanentBehavior = BottomSheetBehavior.from(bottomSheetPermanentLayout)
            // Create a view used to create a darkening effect when the modal is expanded
            val darkEffectView = View(requireContext())
            darkEffectView.apply {
                this.setBackgroundColor(Color.parseColor("#66000000"))
                this.alpha = 0f
                this.animate().apply {
                    this.interpolator = LinearInterpolator()
                    this.duration = animationDuration
                }
            }
            // Open the bottom sheet modal 4 second after the fragment view creation
            bottomSheetPermanentBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            Handler(Looper.getMainLooper()).postDelayed({
                bottomSheetPermanentBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                homepageConstraintLayout.apply {
                    this.setPaddingRelative(0, 0, 0, bottomSheetPermanentBehavior.peekHeight)
                }
            }, 4000)
            // Define expand and collapse function
            fun collapse() {
                bottomSheetPermanentBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                darkEffectView.animate().apply { this.alpha(0f); this.start() }
                Handler(Looper.getMainLooper()).postDelayed({
                    homepageLayout.removeView(darkEffectView)
                }, animationDuration)
            }

            fun expand() {
                bottomSheetPermanentBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (!homepageLayout.allViews.contains(darkEffectView)) {
                    homepageLayout.addView(darkEffectView)
                    darkEffectView.animate().apply { this.alpha(1f);this.start() }
                }
            }
            // Set opening and closing event listener
            darkEffectView.setOnClickListener { collapse() }
            bottomSheetPermanentLayout.setOnClickListener {
                when (bottomSheetPermanentBehavior.state) {
                    BottomSheetBehavior.STATE_EXPANDED -> collapse()
                    else -> expand()
                }

            }
        }
    }

    private fun observeViewModels() {
        medicViewModel.list.observe(viewLifecycleOwner) { medics ->
            medicAdapter.submitList(medics)
        }
        supportViewModel.list.observe(viewLifecycleOwner) { supports ->
            supportAdapter.submitList(supports)
        }
        privateViewModel.list.observe(viewLifecycleOwner) { privates ->
            privateAdapter.submitList(privates)
        }
        suggestionViewModel.list.observe(viewLifecycleOwner) { suggestions ->
            suggestionAdapter.submitList(suggestions)
        }

    }
}