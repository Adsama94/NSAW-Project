package com.anvil.adsama.nsaw.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anvil.adsama.nsaw.R;
import com.anvil.adsama.nsaw.analytics.NsawApp;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class LocationFragment extends Fragment {

    private MapView mMapView;
    private static final int REQUEST_CODE = 7070;
    LatLng currentCoordinates;
    SearchView locationSearchView;
    private GoogleMap googleMap;

    public LocationFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);
        locationSearchView = rootView.findViewById(R.id.sv_location);
        lolaf();
        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        setMap();
        return rootView;
    }

    private void checkLocationPermission() {
        if (getContext() != null && getActivity() != null)
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            }
    }

    private void setMap() {
        try {
            if (getContext() != null)
                MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                checkLocationPermission();
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                    @Override
                    public void onMyLocationClick(@NonNull Location location) {
                        currentCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(currentCoordinates).title("Current Position").snippet("Idhar marwa raha apni"));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentCoordinates).zoom(10).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void lolaf() {
        locationSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null || query.matches(""))
                    Toast.makeText(getContext(), "Lezl " + query, Toast.LENGTH_SHORT).show();
                getLocationFromQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getLocationFromQuery(String locationName) {
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(getContext());
        try {
            addressList = geocoder.getFromLocationName(locationName, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList != null) {
            Address address = addressList.get(0);
            LatLng updatedLatLng = new LatLng(address.getLatitude(), address.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(updatedLatLng).title("Updated Position").snippet("Idhar bhi marwa raha apni"));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(updatedLatLng).zoom(10).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        NsawApp.getInstance().trackScreenView("LOCATION FRAGMENT");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}