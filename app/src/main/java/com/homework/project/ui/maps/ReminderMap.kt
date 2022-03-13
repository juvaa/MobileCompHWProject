package com.homework.project.ui.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.homework.project.util.rememberMapViewWithLifecycle
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun ReminderMap(
    navController: NavController
) {
    val viewModel: ReminderMapViewModel = viewModel()
    val viewState by viewModel.state.collectAsState()
    val mapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    val currentLatitude = navController
        .previousBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Double>("latitude")
        ?.value

    val currentLongitude = navController
        .previousBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Double>("longitude")
        ?.value

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.Black)
        .padding(bottom = 36.dp)
    ) {
        AndroidView({mapView}) { mapView ->
            coroutineScope.launch {
                val map = mapView.awaitMap()
                map.uiSettings.isZoomControlsEnabled = true
                val location = LatLng(currentLatitude ?: 65.10, currentLongitude ?: 25.47)

                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(location, 10f)
                )

                for (reminder in viewState.reminders) {
                    val reminderLocation = LatLng(reminder.location_y!!, reminder.location_x!!)
                    val markerOptions = MarkerOptions()
                        .title(reminder.message)
                        .position(reminderLocation)
                    map.addMarker(markerOptions)
                }

                setMapLongClick(map = map, navController = navController)
            }
        }
    }
}

private fun setMapLongClick(
    map: GoogleMap,
    navController: NavController

) {
    map.setOnMapLongClickListener { latlng ->
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.2f, Lng: %2.2f",
            latlng.latitude,
            latlng.longitude
        )

        map.addMarker(
            MarkerOptions().position(latlng).title("New reminder location").snippet(snippet)
        ).apply {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("location_data", latlng)
        }
    }
}