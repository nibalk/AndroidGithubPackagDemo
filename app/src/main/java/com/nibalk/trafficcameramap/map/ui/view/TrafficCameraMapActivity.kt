package com.nibalk.trafficcameramap.map.ui.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.nibalk.framework.base.data.BaseResult
import com.nibalk.framework.base.ui.hide
import com.nibalk.framework.base.ui.show
import com.nibalk.trafficcameramap.R
import com.nibalk.trafficcameramap.databinding.ActivityTrafficCameraMapBinding
import com.nibalk.trafficcameramap.map.data.api.ResultsResponse
import com.nibalk.trafficcameramap.map.data.api.TrafficCamerasService
import com.nibalk.trafficcameramap.map.data.model.Camera
import com.nibalk.trafficcameramap.map.data.model.Item
import com.nibalk.trafficcameramap.map.ui.viewmodel.TrafficCameraMapViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject


class TrafficCameraMapActivity : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener
{

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: TrafficCameraMapViewModel
    private lateinit var binding: ActivityTrafficCameraMapBinding
    private lateinit var googleMap: GoogleMap

    private var markersBuilder: LatLngBounds.Builder = LatLngBounds.Builder()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)

        viewModel = ViewModelProviders.of(this@TrafficCameraMapActivity,
            viewModelFactory)[TrafficCameraMapViewModel::class.java]
        binding = DataBindingUtil.setContentView(this@TrafficCameraMapActivity,
            R.layout.activity_traffic_camera_map)

        setMapView()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.setOnMarkerClickListener(this@TrafficCameraMapActivity)

        fetchTrafficImages()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        setBottomSheet(marker!!.tag.toString())
        return false
    }

    private fun fetchTrafficImages() {

        viewModel.fetchTrafficImages().observe(this@TrafficCameraMapActivity, Observer { result ->
            when (result.status) {
                BaseResult.Status.SUCCESS -> {
                    binding.loading.hide()
                    Timber.d("~~~ networkResponse = %s", result.data)
                    result.data?.let {
                        onFetchTrafficImagesSuccess(it)
                    }
                }
                BaseResult.Status.LOADING -> {
                    binding.loading.show()
                }
                BaseResult.Status.ERROR -> {
                    binding.loading.hide()
                    onFetchTrafficImagesFail(result.message!!)
                }
            }
        })
    }

    private fun onFetchTrafficImagesSuccess(response: ResultsResponse<Item>) {
        if (response.api_info.status == TrafficCamerasService.API_STATUS) {
            val responseItem = response.items
            if (response.items.isNotEmpty() && responseItem[0].cameras.isNotEmpty() ) {
                responseItem[0].cameras.forEach { camera -> showCamerasOnMap(camera) }
                zoomToFitMarkers()
            } else {
                onFetchTrafficImagesFail(getString(R.string.msg_no_cameras_available))
            }
        } else {
            onFetchTrafficImagesFail(getString(R.string.msg_api_status_not_healthy))
        }
    }

    private fun onFetchTrafficImagesFail(errorMessage: String) {
        val snackBar = Snackbar.make(binding.container, errorMessage, Snackbar.LENGTH_LONG)

        val textView = snackBar.view.findViewById<TextView>(R.id.snackbar_text)
        textView.maxLines = 7
        textView.setSingleLine(false)

        snackBar.show()
    }

    private fun setMapView() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setBottomSheet(imageUrl: String) {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_camera_image, null)

        Picasso.get()
            .load(imageUrl)
            .into(view.findViewById<ImageView>(R.id.trafficImage),
                object: Callback {
                    override fun onSuccess() {
                        (view.findViewById<ProgressBar>(R.id.trafficImageLoader)).visibility = View.GONE
                    }
                    override fun onError(e: java.lang.Exception?) {
                        (view.findViewById<ProgressBar>(R.id.trafficImageLoader)).visibility = View.GONE
                        onFetchTrafficImagesFail(getString(R.string.msg_no_camera_photos_available))
                    }
                })

        val bottomSheet = BottomSheetDialog(this@TrafficCameraMapActivity)
        bottomSheet.setContentView(view)
        bottomSheet.show()

        view.findViewById<Button>(R.id.trafficImageClose).setOnClickListener{ bottomSheet.dismiss() }
    }

    private fun showCamerasOnMap(camera: Camera) {
        val location = LatLng(camera.location.latitude, camera.location.longitude)
        markersBuilder.include(location)

        val marker = googleMap.addMarker(MarkerOptions()
            .position(location)
            .title(camera.camera_id))
        marker.tag = camera.image
    }

    private fun zoomToFitMarkers() {
        val latLngBounds = markersBuilder.build()
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100), 2000, null)
    }
}
