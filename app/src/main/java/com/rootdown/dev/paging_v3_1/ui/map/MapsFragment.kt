package com.rootdown.dev.paging_v3_1.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.awaitMap
import com.rootdown.dev.paging_v3_1.R
import com.rootdown.dev.paging_v3_1.ThisApp
import com.rootdown.dev.paging_v3_1.api.DatabaseCoordinates
import com.rootdown.dev.paging_v3_1.databinding.FragmentMapsBinding
import com.rootdown.dev.paging_v3_1.repo.LatLngController
import com.rootdown.dev.paging_v3_1.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    private val viewModel: MapsViewModel by viewModels()
    private val REQUEST_LOCATION_PERMISSION = 1
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private var latCurr: Double = 0.0
    private var lngCurr: Double = 0.0
    private var latlngLs: List<DatabaseCoordinates> = emptyList()

    private var latLs: List<Double> = emptyList()
    private var lngLs: List<Double> = emptyList()
    private var searchJob: Job? = null
    private val options = MarkerOptions()
    private val latlngs: ArrayList<LatLng> = ArrayList()

    // Declare a variable for the cluster manager.
    private lateinit var clusterManager: ClusterManager<MyItem>


    private lateinit var googleMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_options, menu)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = ""
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
        setHasOptionsMenu(true)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())

    }
    override fun onResume() {
        super.onResume()

        // Set title bar
        (activity as MainActivity?)
            ?.setActionBarTitle("")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val view = binding.root
        val fab: View = binding.fabMapProfiles
        viewModel.latlng.observe(viewLifecycleOwner, { latlng ->
            latlng?.let {
                latlngLs = it
            }
        })
        fab.setOnClickListener{
            val app = ThisApp()
            val controller = LatLngController()
            lifecycleScope.launch {
                controller.go()
            }
            val test = controller.result
            //Toast.makeText(this.requireContext(), "TEST $test", Toast.LENGTH_LONG).show()
            setUpClusterer()

            //var latList: List<Double> = emptyList()
            //var lngList: List<Double> = emptyList()

            //latlngLs.forEach {
                //latList += it.lat
                //lngList += it.lng
                //latlngs.add(LatLng(it.lat, it.lng))
            //}
            //for (point in latlngs) {
                //options.position(point!!)
                //options.title("someTitle")
                //options.snippet("someDesc")
                //googleMap.addMarker(options)
            //}
            //latlngLs.onEach {
                //googleMap.addMarker(MarkerOptions().position(LatLng(latList.get(it.id), lngList.get(it.id))).title("X"))
            //}

            //val intent = Intent(this.context, MapActivity::class.java)
            //startActivity(intent)
            //Toast.makeText(this.requireContext(), "TEST ${latlngLs.toString()}", Toast.LENGTH_LONG).show()
        }
        return view
    }
    private fun setUpClusterer() {

        // Position the map.
        latCurr.notNull {
            lngCurr.notNull {
                setCameraOnLocation()
            }
        }


        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = ClusterManager(context, googleMap)

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        googleMap.setOnCameraIdleListener(clusterManager)
        googleMap.setOnMarkerClickListener(clusterManager)

        // Add cluster items (markers) to the cluster manager.
        addItems()
    }
    fun <T : Any> T?.notNull(f: (it: T) -> Unit) {
        if (this != null) f(this)
    }
    fun setCameraOnLocation(){
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(39.7193329, -105.027629), 10f))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latCurr, lngCurr), 10f))
    }

    private fun addItems() {
        var latList: List<Double> = emptyList()
        var lngList: List<Double> = emptyList()
        latlngLs.forEach {
            latList += it.lat
            lngList += it.lng
            latlngs.add(LatLng(it.lat, it.lng))
            val offsetItem =
                    MyItem(it.lat, it.lng, "X", "Snippet")
            clusterManager.addItem(offsetItem)
        }

        //for (point in latlngs) {
            //val offsetItem =
                //MyItem(, lng, "Title $i", "Snippet $i")
            //clusterManager.addItem(offsetItem)
            //options.position(point!!)
            //options.title("someTitle")
            //options.snippet("someDesc")
            //googleMap.addMarker(options)
        //}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        //mapFragment?.getMapAsync(callback)
        val args: MapsFragmentArgs by navArgs()
        val lat = args.lat.toDouble()
        val lng = args.lng.toDouble()
        val title = args.title
        val bbbrands = LatLng(lat, lng)
        val point1 = LatLng(40.6974034, -74.1197638) // NewYork

        val point2 = LatLng(40.7440702, -73.9353235) // Seattle


        lifecycle.coroutineScope.launchWhenCreated {
            googleMap = mapFragment?.awaitMap()!!
            googleMap.uiSettings.isZoomControlsEnabled = true
            enableMyLocation()
            setUpMap()
            googleMap.addMarker(MarkerOptions().position(bbbrands).title(title))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bbbrands, 12.0f))
        }
        //Log.w("$$$$$$$", latlngLs.toString())
        //val controller = ChangeController()
        //controller.start()
    }
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        googleMap.isMyLocationEnabled = true
        // 2
        fusedLocationClient.lastLocation.addOnSuccessListener(this.requireActivity()) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location
                lngCurr = location.longitude
                latCurr = location.latitude
                val currentLatLng = LatLng(location.latitude, location.longitude)
                latCurr = currentLatLng.latitude
                lngCurr = currentLatLng.longitude
                placeMarkerOnMap(currentLatLng)
                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // Change the map type based on the user's selection.
        R.id.normal_map -> {
            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }


    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            googleMap.isMyLocationEnabled = true

        }
        else {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }
    private fun placeMarkerOnMap(location: LatLng) {
        // 1
        val markerOptions = MarkerOptions().position(location)
        // 2
        googleMap.addMarker(markerOptions)
    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        TODO("Not yet implemented")
    }

    inner class MyItem(
        lat: Double,
        lng: Double,
        title: String,
        snippet: String
    ) : ClusterItem {

        private val position: LatLng
        private val title: String
        private val snippet: String

        override fun getPosition(): LatLng {
            return position
        }

        override fun getTitle(): String? {
            return title
        }

        override fun getSnippet(): String? {
            return snippet
        }

        init {
            position = LatLng(lat, lng)
            this.title = title
            this.snippet = snippet
        }
    }

}