package com.bantoo.babooo.Pages.LocationPage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.bantoo.babooo.R;
import com.bantoo.babooo.Utilities.BaseActivity;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class DefineLocationActivity extends BaseActivity implements OnMapReadyCallback, LocationListener {

    //if request search location success
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    //make it private so other classes can't access these variables
    //prevent bugs or crashed
    private ImageView closeLocationIV;
    private EditText searchLocationET, notesLocationET;
    private Button searchLocationBTN;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Double latitudeLocation, longitudeLocation;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this one is to configure MapBox with access token
        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_define_location);

        //findviewbyid
        initView();
        handleAction();

        //this one to save state if application/activity get paused or destroyed
        mapView.onCreate(savedInstanceState);
        //real-time update the location.
        mapView.getMapAsync(this);
    }

    private String getCountryName() {
        Geocoder geocoder = new Geocoder(DefineLocationActivity.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitudeLocation, longitudeLocation, 1);
            if(addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getCountryName();
            }
            return null;
        } catch (IOException ignores) {}
        return "";
    }

    private void getCurrentLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    //get the location address from autocomplete
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            // Retrieve selected location's CarmenFeature
            //CarmenFeature is place information. Data type from mapbox SDK
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            //stop getting current location
            locationManager.removeUpdates(this);

            // Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
            // Then retrieve and update the source designated for showing a selected location's symbol layer icon

            searchLocationET.setText(""+selectedCarmenFeature.placeName());
            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs("geojsonSourceLayerId");

                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

                    longitudeLocation = ((Point) selectedCarmenFeature.geometry()).longitude();
                    latitudeLocation = ((Point) selectedCarmenFeature.geometry()).latitude();

                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);
                }
            }
        }
    }

    private void initView() {
        closeLocationIV = findViewById(R.id.close_location_IV);
        searchLocationET = findViewById(R.id.search_location_ET);
        searchLocationBTN = findViewById(R.id.search_loaction_BTN);
        notesLocationET = findViewById(R.id.notes_location_ET);
        mapView = findViewById(R.id.mapView);
    }

    private void handleAction(){
        //because mapbox needs new activity to search a location, here I created the onClickListener
        //when the editText is tapped, it will redirect to mapbox search activity
        searchLocationET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(DefineLocationActivity.this);
                //after finish activity, there will be a result (value/address). we'll receive this data later in onActivityResult method
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }
        });
        closeLocationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchLocationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Apply location
                if(longitudeLocation == null && latitudeLocation == null) {
                    Toast.makeText(DefineLocationActivity.this, "Harap masukan lokasi anda", Toast.LENGTH_SHORT).show();
                } else if (getCountryName().contains("Indonesia")) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("address", searchLocationET.getText().toString());
                    resultIntent.putExtra("latitude", latitudeLocation);
                    resultIntent.putExtra("longitude", longitudeLocation);
                    resultIntent.putExtra("notes", notesLocationET.getText().toString());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(DefineLocationActivity.this, "Bantoo tidak tersedia di luar Indonesia", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUpSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource("geojsonSourceLayerId"));
    }

    private void setupLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", "geojsonSourceLayerId").withProperties(
                iconImage("symbolIconId"),
                iconOffset(new Float[] {0f, -8f})
        ));
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                //set on click listener after style of the map loaded

                style.addImage("symbolIconId", BitmapFactory.decodeResource(DefineLocationActivity.this.getResources(), R.drawable.blue_marker_view));

                // Create an empty GeoJSON source using the empty feature collection
                setUpSource(style);
                // Set up a new symbol layer for displaying the searched location's feature coordinates
                setupLayer(style);
                getCurrentLocation();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitudeLocation = location.getLatitude();
        longitudeLocation = location.getLongitude();
        MapboxGeocoding reverseGeocode = MapboxGeocoding.builder()
                .accessToken(getString(R.string.mapbox_access_token))
                .query(Point.fromLngLat(longitudeLocation, latitudeLocation))
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .build();
        reverseGeocode.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                if (response.body() != null) {
                    //CarmenFeature is a place information
                    List<CarmenFeature> results = response.body().features();
                    if (results.size() > 0) { //place name recognized by mapbox
                        for(int i = 0; i<results.size(); i++) {
                            Log.d("DefineLocation", "onResponse: carmenFeature= "+results.get(i).address());
                        }

                        // Get the first Feature from the successful geocoding response
                        CarmenFeature feature = results.get(0);

                        // Get the address string from the CarmenFeature
                        String carmenFeatureAddress = feature.placeName();
                        searchLocationET.setText(carmenFeatureAddress);
                    } else { //place not detected by mapbox
                        searchLocationET.setText(location.getLongitude()+", "+location.getLatitude());
                    }
                }
            }
            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {

            }
        });
        if (mapboxMap != null) {
            Style style = mapboxMap.getStyle();
            if (style != null) {
                GeoJsonSource source = style.getSourceAs("geojsonSourceLayerId");
                source.setGeoJson(FeatureCollection.fromFeatures(new Feature[] {Feature.fromGeometry(Point.fromLngLat(longitudeLocation, latitudeLocation))}));
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder()
                                .target(new LatLng(location.getLatitude(),
                                        location.getLongitude()))
                                .zoom(14)
                                .build()), 4000);
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(DefineLocationActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
}
