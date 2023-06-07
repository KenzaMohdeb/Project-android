package com.uqam.mentallys.view.ui.resource.map

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Animatable2.AnimationCallback
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentSearchMapBinding
import com.uqam.mentallys.model.Country
import com.uqam.mentallys.model.resource.Resource
import com.uqam.mentallys.utils.SharedState
import com.uqam.mentallys.view.MainActivity
import com.uqam.mentallys.view.ui.resource.common.ResourcePreviewAdapter
import com.uqam.mentallys.view.ui.resource.search.SearchFragment
import com.uqam.mentallys.viewmodels.resource.FilterViewModel
import com.uqam.mentallys.viewmodels.resource.ResourceViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_search_map),
    OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    private val sharedState: SharedState = SharedState
    private val resourceViewModel: ResourceViewModel by viewModels({ requireParentFragment() })
    private val filtersViewModel: FilterViewModel by viewModels({ requireParentFragment() })
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var resourceClusterManager: ResourceClusterManager
    private val resourcePreviewAdapter = ResourcePreviewAdapter()
    private lateinit var binding: FragmentSearchMapBinding
    private lateinit var googleMap: GoogleMap
    private var onPermissionActivityEnds: () -> Unit = {}
    private var isLocationLoadingEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isMapLoadingEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    private var height: Int = Int.MAX_VALUE
    private var width: Int = Int.MAX_VALUE
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onPermissionActivityEnds()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchMapBinding.bind(view)
        view.viewTreeObserver?.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (view.height > 0 && view.width > 0) {
                    height = view.height
                    width = view.width
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })
        displayMapLoading()
        if (!this::googleMap.isInitialized) {
            val supportMapFragment: SupportMapFragment? =
                childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment?
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            MapRendererOptInApplication()
            supportMapFragment!!.getMapAsync(this)
        } else {
            setupMap()
            refreshFilterMapCorners()
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
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (view != null) {
            this.googleMap = googleMap
            setupMap()
        }
    }

    private fun setupMap() {
        if (sharedState.getState("MapFragmentCameraPosition") != null) {
            val savedState = sharedState.getState("MapFragmentCameraPosition") as CameraPosition
            setMapLocation(googleMap, savedState.target, savedState.zoom, true)
        } else {
            try {
                val country = Country.valueOf(fetchLocale())
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(country.coordinate, country.zoom)
                )
                refreshFilterMapCorners()
            } catch (e: IllegalArgumentException) {
                // Try to find a country, if not found just ditch the settings
            }
        }
        requestPermission()
        setupMapStyle()
        setupCluster()
        setupPositionRefreshButton()
        if (sharedState.getState("MapFragmentSelectedMarker") != null) {
            val item = sharedState.getState("MapFragmentSelectedMarker") as Resource
            resourcePreviewAdapter.submitList(listOf(item))
        }
        googleMap.setOnMapLoadedCallback {
            refreshFilterMapCorners()
            hideMapLoading()
        }
    }


    private fun requestPermission() {
        // Evaluate current permission and ask for geolocation permission if not enable
        val permissionRequestCode = 99
        val grantsResult = IntArray(2)
        grantsResult[0] = PackageManager.PERMISSION_GRANTED
        grantsResult[1] = PackageManager.PERMISSION_GRANTED
        val permissionArray = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        // Check if permission are accorded and if not ask for them
        if (checkPermission()) {
            binding.apply {
                mapRecenter.iconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
            }
            if (sharedState.getState("MapFragmentCameraPosition") == null) {
                fetchLastLocation(true)
            }
        } else {
            activity?.requestPermissions(permissionArray, permissionRequestCode)
            (activity as MainActivity).setOnRequestPermissionResultCallback { a: Int, b: Array<out String>, c: IntArray ->
                if (a == permissionRequestCode && b.contentEquals(permissionArray) && (c[0] == grantsResult[0] || c[1] == grantsResult[1])) {
                    binding.apply {
                        mapRecenter.iconTint =
                            ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
                    }
                    if (sharedState.getState("MapFragmentCameraPosition") == null) {
                        fetchLastLocation(true)
                    }
                }
            }

        }
    }

    private fun checkPermission(): Boolean {
        val permissionArray = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val fineLocationPermission: Int
        val coarseLocationPermission: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fineLocationPermission =
                requireContext().checkSelfPermission(permissionArray[0])
            coarseLocationPermission =
                requireContext().checkSelfPermission(permissionArray[1])
        } else {
            fineLocationPermission =
                PermissionChecker.checkSelfPermission(requireContext(), permissionArray[0])
            coarseLocationPermission =
                PermissionChecker.checkSelfPermission(requireContext(), permissionArray[1])
        }
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED || coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun setupMapStyle() {
        if (checkPermission()) {
            googleMap.isMyLocationEnabled = true
        }
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.uiSettings.isTiltGesturesEnabled = false
        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.map_style
            )
        )
        googleMap.uiSettings.isRotateGesturesEnabled = false
        googleMap.setOnMapClickListener {
            requireParentFragment().view?.clearFocus()
            resourcePreviewAdapter.submitList(listOf())
            if (sharedState.getState("MapFragmentSelectedMarker") != null) {
                val item = sharedState.getState("MapFragmentSelectedMarker") as Resource
                (resourceClusterManager.renderer as IconRenderer).setMarkerAsUnselected(item)
                sharedState.eraseState("MapFragmentSelectedMarker")
            }
        }
    }

    private fun setupPositionRefreshButton() {
        binding.apply {
            if (checkPermission()) {
                mapRecenter.iconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
            }
            mapRecenter.setOnClickListener {
                if (!checkPermission()) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", activity?.packageName, null)
                    intent.data = uri
                    onPermissionActivityEnds = {
                        if (checkPermission()) {
                            binding.apply {
                                mapRecenter.iconTint =
                                    ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
                            }
                        }
                    }
                    resultLauncher.launch(intent)
                }
                fetchLastLocation(false)
            }
        }
    }

    private fun setupCluster() {
        if (!this::resourceClusterManager.isInitialized) {
            resourceClusterManager = ResourceClusterManager(requireContext(), googleMap)
        }
        resourceClusterManager.setAnimation(false)
        resourceClusterManager.clearItems()
        googleMap.setOnCameraIdleListener(this)
        googleMap.setOnMarkerClickListener(resourceClusterManager)
        // Override default onclick event for the cluster
        resourceClusterManager.setOnClusterClickListener {

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                it.position, (googleMap.cameraPosition.zoom + 2)), 300,
                null)
            refreshFilterMapCorners()
            true
        }
        resourceClusterManager.setOnClusterItemClickListener {
            requireParentFragment().view?.clearFocus()
            if (it != null) {
                sharedState.getState("MapFragmentSelectedMarker")?.let { it1 ->
                    deselectMaker(it1 as Resource)
                }
                (resourceClusterManager.renderer as IconRenderer).setMarkerAsSelected(it)
                sharedState.saveState("MapFragmentSelectedMarker", it)
                resourcePreviewAdapter.submitList(listOf(it))
                sharedState.saveState("MapFragmentSelectedMarker", it)
                activity?.onBackPressedDispatcher?.addCallback(this) {
                    if (sharedState.getState("MapFragmentSelectedMarker") != null) {
                        deselectMaker(it)
                    }
                    this.remove()
                }
            }
            setMapLocation(googleMap, it.position, 14F, false)
            true
        }
        if (sharedState.getState("MapFragmentSelectedMarker") != null) {
            activity?.onBackPressedDispatcher?.addCallback(this) {
                val item = sharedState.getState("MapFragmentSelectedMarker")
                if (item != null) {
                    deselectMaker(item as Resource)
                    this.remove()
                }
            }
        }
        setupViewModelListerForCluster()
    }

    private fun setupViewModelListerForCluster() {
        resourceViewModel.list.observe(viewLifecycleOwner) { resources ->
            // Unselect the current preview and marker if its not among the displayed point
            sharedState.getState("MapFragmentSelectedMarker").let {
                if (it != null && !resources.contains(it as Resource)) {
                    resourcePreviewAdapter.submitList(listOf())
                    (resourceClusterManager.renderer as IconRenderer).setMarkerAsUnselected(it)
                    sharedState.eraseState("MapFragmentSelectedMarker")
                }
            }
            if (isMapLoadingEnabled.value!!) {
                binding.fragmentSearchNoResult.visibility = View.GONE
                isMapLoadingEnabled.observeOnce(viewLifecycleOwner) {
                    when (resources.isEmpty()) {
                        true -> binding.fragmentSearchNoResult.visibility = View.VISIBLE
                        false -> binding.fragmentSearchNoResult.visibility = View.GONE
                    }
                }
            } else {
                when (resources.isEmpty()) {
                    true -> binding.fragmentSearchNoResult.visibility = View.VISIBLE
                    false -> binding.fragmentSearchNoResult.visibility = View.GONE
                }
            }

            resourceClusterManager.clearItems()
            resourceClusterManager.addItems(resources)
            resourceClusterManager.cluster()
            displayMapLoading()
            googleMap.setOnMapLoadedCallback {
                hideMapLoading()
            }
        }
    }

    private fun fetchLastLocation(instant: Boolean) {
        displayLocationLoading()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                googleMap.isMyLocationEnabled = true
                if (location != null) {
                    hideLocationLoading()
                    setMapLocation(googleMap, location, 14F, instant)
                } else {
                    fetchCurrentLocation()
                }
            }
    }

    private fun fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener {
                if (it != null) {
                    hideLocationLoading()
                    setMapLocation(googleMap, it, 14F, false)
                } else {
                    hideLocationLoading()
                }
            }
    }

    private fun setMapLocation(
        googleMap: GoogleMap,
        location: LatLng,
        zoom: Float,
        instant: Boolean,
    ) {
        if (instant) {
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    location, zoom
                )
            )
            refreshFilterMapCorners()
        } else {
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    location, zoom
                ), 500, null
            )
            Handler(Looper.getMainLooper()).postDelayed({ refreshFilterMapCorners() }, 1000)
        }

    }

    @Suppress("SameParameterValue")
    private fun setMapLocation(
        googleMap: GoogleMap,
        location: Location,
        zoom: Float,
        instant: Boolean,
    ) {
        setMapLocation(googleMap, LatLng(location.latitude, location.longitude), zoom, instant)
    }

    @Suppress("DEPRECATION")
    // Suppress deprecation since it is still used in old API
    private fun fetchLocale(): String {
        var locale = "CAN"
        val temp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requireActivity().resources?.configuration?.locales?.get(0)?.isO3Country
        } else {
            requireContext().resources.configuration.locale.isO3Country
        }
        if (temp != null) {
            locale = temp
        }
        return locale
    }

    fun getCurrentPointer(): LatLng? {
        if (this::googleMap.isInitialized) {
            return googleMap.cameraPosition.target
        }
        return null
    }

    fun centerOnResource(resource: Resource) {
        setMapLocation(googleMap, resource.position, 14F, false)
    }

    // Enable the new render engine for the map
    // https://developers.google.com/maps/documentation/android-sdk/renderer?hl=fr
    internal class MapRendererOptInApplication : Application(), OnMapsSdkInitializedCallback {
        override fun onCreate() {
            super.onCreate()
            MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST, this)
        }

        override fun onMapsSdkInitialized(renderer: MapsInitializer.Renderer) {
            when (renderer) {
                MapsInitializer.Renderer.LATEST -> Log.d("MapsDemo",
                    "The latest version of the renderer is used.")
                MapsInitializer.Renderer.LEGACY -> Log.d("MapsDemo",
                    "The legacy version of the renderer is used.")
            }
        }
    }

    override fun onCameraIdle() {
        refreshFilterMapCorners()
        googleMap.setOnCameraIdleListener {
            resourceClusterManager.onCameraIdle()
            sharedState.saveState("MapFragmentCameraPosition", googleMap.cameraPosition)
            displayLocationLoading()
            refreshFilterMapCorners()
            resourceViewModel.list.observeOnce(viewLifecycleOwner) {
                hideLocationLoading()
            }
        }
    }

    private fun displayLocationLoading() {
        isLocationLoadingEnabled.value = true
        Handler(Looper.getMainLooper()).postDelayed(
            {
                if (isLocationLoadingEnabled.value!!) {
                    binding.fragmentMapLoading.visibility = View.VISIBLE
                    val d: Drawable = binding.fragmentMapLoading.drawable
                    (d as AnimatedVectorDrawable).registerAnimationCallback(object :
                        AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            d.start()
                        }
                    })
                    (d as? AnimatedVectorDrawable)?.start()
                }
            }, 500
        )
    }

    private fun hideLocationLoading() {
        isLocationLoadingEnabled.value = false
        binding.fragmentMapLoading.visibility = View.GONE
        val d: Drawable = binding.fragmentMapLoading.drawable
        (d as? AnimatedVectorDrawable)?.stop()
    }

    private fun displayMapLoading() {
        isMapLoadingEnabled.value = true
        Handler(Looper.getMainLooper()).postDelayed(
            {
                if (isMapLoadingEnabled.value!!) {
                    binding.fragmentMapLoading.visibility = View.VISIBLE
                    val d: Drawable = binding.fragmentMapLoading.drawable
                    (d as AnimatedVectorDrawable).registerAnimationCallback(object :
                        AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            d.start()
                        }
                    })
                    (d as? AnimatedVectorDrawable)?.start()
                }
            }, 1000
        )
    }

    private fun hideMapLoading() {
        isMapLoadingEnabled.value = false
        binding.fragmentMapLoading.visibility = View.GONE
        val d: Drawable = binding.fragmentMapLoading.drawable
        (d as? AnimatedVectorDrawable)?.stop()
    }

    fun applyBounds(latLngBounds: LatLngBounds) {
        if (this::googleMap.isInitialized) {
            val padding = (width * 0.05).toInt() // offset from edges of the map 10% of screen
            val cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, width, height, padding)
            refreshFilterMapCorners()
            googleMap.animateCamera(cu)
        }
    }

    private fun refreshFilterMapCorners() {
        val currentMap = googleMap.projection.visibleRegion.latLngBounds
        val filter = filtersViewModel.filter.value
        filter?.southWestPoint = currentMap.southwest
        filter?.northEastPoint = currentMap.northeast
        filtersViewModel.filter.postValue(filter)
    }

    private fun deselectMaker(item: Resource) {
        if (sharedState.getState("MapFragmentSelectedMarker") != null) {
            resourcePreviewAdapter.submitList(listOf())
            (resourceClusterManager.renderer as IconRenderer).setMarkerAsUnselected(item)
            sharedState.eraseState("MapFragmentSelectedMarker")
        }
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}


