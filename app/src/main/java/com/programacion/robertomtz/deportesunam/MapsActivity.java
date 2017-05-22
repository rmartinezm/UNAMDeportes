package com.programacion.robertomtz.deportesunam;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements PermissionsListener {

    // https://www.mapbox.com/android-sdk/examples/user-location/

    private MapView mapView;
    private MapboxMap map;

    private FloatingActionButton floatingActionButton;
    private LocationEngine locationEngine;
    private LocationEngineListener locationEngineListener;
    private PermissionsManager permissionsManager;

    private double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getResources().getString(R.string.accessTokenMapBox));
        setContentView(R.layout.activity_maps);

        getSupportActionBar().hide();

        // obtenemos el location engine para usarlo despues
        locationEngine = LocationSource.getLocationEngine(this);
        locationEngine.activate();

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        lat = 0;
        lon = 0;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            lat = (double) bundle.get("lat");
            lon = (double) bundle.get("lon");
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;

                if (lat == 0 || lon == 0) {
                    startActivity(new Intent(MapsActivity.this, InfoEventoActivity.class));
                    finish();
                }

                LatLng miLatLng = new LatLng(lat, lon);

                MarkerViewOptions markerViewOptions = new MarkerViewOptions().position(miLatLng);

                CameraPosition position = new CameraPosition.Builder()
                        .target(miLatLng) // Coloca la camara en la posicion deseada
                        .zoom(14) // Coloca el zoom que daremos
                        .bearing(180) // Rota la camara
                        .tilt(30) // Coloca la inclinacion de la camara
                        .build(); // Creamos la posicion con nuestras caracteristicas

                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 7000);

                mapboxMap.addMarker(markerViewOptions);

            }
        });

        floatingActionButton = (FloatingActionButton) findViewById(R.id.location_toggle_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (map != null)
                    toggleGps(!map.isMyLocationEnabled());

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
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

        // Quitamos el listener
        if (locationEngineListener != null) {
            locationEngine.removeLocationEngineListener(locationEngineListener);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    // Recibe true si la localizacion esta deshabilitada
    private void toggleGps(boolean enableGps) {
        if (enableGps) {
            // Verificamos si tiene los permisos aceptados
            permissionsManager = new PermissionsManager(this);

            if (!PermissionsManager.areLocationPermissionsGranted(this))
                // Solicitamos permiso
                permissionsManager.requestLocationPermissions(this);
            else
                // Encendemos la localizacion
                enableLocation(true);

        } else
            // Como la localizacion esta habilitada entonces la quitamos
            enableLocation(false);
    }

    private void enableLocation(boolean enabled) {
        // Si queremos encender el gps
        if (enabled) {
            // Si tenemos la ultima localizacion del usuario nos movemos a ella, pero necesitamos otro permiso
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            // Obtenemos la ultima localizacion
            Location lastLocation = locationEngine.getLastLocation();

            if (lastLocation != null)
                // Nos movemos hacia la ultima localizacion
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 16));

            // Agregamos un listener para actualizar la localizacion
            locationEngineListener = new LocationEngineListener() {
                @Override
                public void onConnected() {
                    // No action needed here.
                }

                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        // Move the map camera to where the user location is and then remove the
                        // listener so the camera isn't constantly updating when the user location
                        // changes. When the user disables and then enables the location again, this
                        // listener is registered again and will adjust the camera once again.
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location), 16));
                        locationEngine.removeLocationEngineListener(this);
                    }
                }
            };
            locationEngine.addLocationEngineListener(locationEngineListener);
            floatingActionButton.setImageResource(R.drawable.ic_location_disabled_24dp);

        }
        // Si queremos quitar el gps
        else
            // Solo tenemos que cambiar el icono
            floatingActionButton.setImageResource(R.drawable.ic_my_location_24dp);

        // Habilitamos o quitamos la localizacion del mapa
        map.setMyLocationEnabled(enabled);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Esta app necesita permiso de localizacion para funcionar correctamente", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted)
            enableLocation(true);
        else
            Toast.makeText(this, "No aceptaste los permisos de ubicación", Toast.LENGTH_LONG).show();
    }
}