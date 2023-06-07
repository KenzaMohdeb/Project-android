package com.uqam.mentallys.view

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.ActivityMainBinding
import com.uqam.mentallys.viewmodels.homepage.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel by viewModels<HomeViewModel>()
    private var permissionsResultCallback: MutableList<(Int, Array<out String>, IntArray) -> Unit> =
        mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.bottomNavMenu.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.viewPagerFragment,
                R.id.selectEventTypeFragment,
                R.id.addEventMaintenanceFragment,
                R.id.addEventConsultationFragment,
                R.id.addEventPsychotherapyFragment,
                R.id.addEventHospitalizationFragment,
                R.id.addEventCommunitySupportFragment,
                R.id.addEventAccommodationFragment,
                R.id.addEventOtherFragment,
                R.id.savedEventFragment,
                -> {
                    binding.bottomNavMenu.visibility = View.GONE
                }
                R.id.login -> {
                    navController.navigate(R.id.loginFragment)
                }
                else -> {
                    binding.bottomNavMenu.visibility = View.VISIBLE
                }
            }
        }

    }

    fun setOnRequestPermissionResultCallback(function: (requestCode: Int, permission: Array<out String>, grantResults: IntArray) -> Unit) {
        permissionsResultCallback.add(function)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        permissionsResultCallback.forEach {
            it(requestCode, permissions, grantResults)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun performLogout() {
        viewModel.performLogout()
    }

    fun restoreDefaultNavigationBehavior() {
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.popBackStack()
                if (navController.backQueue.size == 0){
                    finish()
                }
            }
        })
    }

}