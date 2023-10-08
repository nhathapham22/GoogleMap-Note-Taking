package com.example.finalproject;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import com.example.finalproject.databinding.FragmentMapBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

// map fragment, based on lab 5, simplified, only implementing the click feature
// onResume and onPause and some others removed if only using marker
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;

    private MarkerOptions markerOpts;
    private Marker currentMarker;
    private FragmentMapBinding binding;

    // copied from lab 5
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private android.location.Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;

    // this is a reference to journalForm, use this to populate the form when marker location change
    private JournalFormFragment journalFormFragment;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Get a reference to FragmentA
        journalFormFragment = ((MapsActivity) context).getJournalFormFragment();
    }


    // order of execution: onCreateView => onViewCreated => onMapReady
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // just copy pasted from lab5,
        // rewritten to fit Fragment instead of FragmentActivity

        // instantiate the main location classes
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mSettingsClient = LocationServices.getSettingsClient(getContext());

        mRequestingLocationUpdates = true;

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.

        // createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        return binding.getRoot();
    }


    //cleanup purposes and is called when the fragment's view is being destroyed
    // to avoid memory leaks and unnecessary resource usage.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    // on map ready
    // init with the location of Saskpoly MJ campus

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //add a marker at Saskpoly MJ campus
        LatLng SaskpolyMJ = new LatLng(50.4042, -105.5497);
        markerOpts = new MarkerOptions().position(SaskpolyMJ) // position LatLng
                .title("Saskpoly MJ") // title displayed in "infor window" tooltip when marker is tapped
                .snippet("Marker at Saskpolytech in MJ");
        // pass the marker options to the map object to get a marker object returned
        // this also put the marker on the map
        currentMarker = mMap.addMarker(markerOpts);

        //tag the marker with any object
        currentMarker.setTag(false); // false = infor not shown initially
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(SaskpolyMJ, 14.5f));

        // when click on the map, move marker to the clicked location, get geocode result and display it
        // call method to fill journalForm
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                // This code runs when the map is tapped / clicked
                // point variable has coords

                // lookup the address of the tapped point (put into addtring) variablle
                String addr="";
                // get a Geocoder object with the current locale settings
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                try {

                    // attempt to get a list address from the tapped point (limited to 1 here)
                    List<Address> addresses = geocoder.getFromLocation((double) point.latitude, (double) point.longitude, 1);

                    // build a string from the address and usse that as a snippet on the marker

                    //check if an address was found
                    if (addresses.size() > 0) {
                        //add at least 1 address found
                        //loop over all the lines in the first address and collect them into the add string

                        // get the first address from the List<address>
                        Address address = addresses.get(0);

                        // get the index of the last address line in this address

                        int lastlineIndex = address.getMaxAddressLineIndex();
                        //loop over all the lines in the address, and collect them into a string with line breaks
                        for (int i = 0; i <= lastlineIndex; i++) {
                            addr += address.getAddressLine(i) + "\n";
                        }

                    } else {
                        // no addresses were found
                        addr = "No address found";
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // call updateData method to get data for the journal form fragment
                if (journalFormFragment != null) {
                    journalFormFragment.updateData(point.latitude, point.longitude, addr);
                }

                Log.d("DEBUG", "Map clicked [" + point.latitude + " / " + point.longitude + "]");

                //toast address to the user
//                Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();

                //set tje address string on the current marker snippet
                currentMarker.setTitle(addr);
                currentMarker.setSnippet("");
                currentMarker.showInfoWindow();

                //move location/ use the current marker to the new (tapped) location
                currentMarker.setPosition(point);
                // move the map camera to the new point
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,14.5f));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 14.5f));


            }

        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                // false = allow the default map clck behaviour, return true
                //if we handled (overridden) the behavior - suppress the default

                // toggle the info windows on/off with clicks
                //marker.isInfoWindowShown();
                //so we use a work-around by tagging the marker

                boolean onOff = (boolean) marker.getTag();

                if (onOff == false) {
                    // the info window is hidden, so show it
                    marker.showInfoWindow();
                    marker.setTag(true);
                } else {
                    marker.hideInfoWindow();
                    marker.setTag(false);
                }
                return true;
            }
        });
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest() {
        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.

        LocationRequest.Builder locationRequestBuilder =
                new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest = locationRequestBuilder
                .setMinUpdateIntervalMillis(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
                .build();
    }
    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */

    /**
     * Creates a callback for receiving location events.
     */

}
