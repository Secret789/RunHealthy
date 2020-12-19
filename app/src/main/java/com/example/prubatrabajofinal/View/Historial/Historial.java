package com.example.prubatrabajofinal.View.Historial;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prubatrabajofinal.MapsActivity;
import com.example.prubatrabajofinal.Model.Historial.TrayectoriaModel;
import com.example.prubatrabajofinal.Model.Historial.UbicacionModel;
import com.example.prubatrabajofinal.Presenter.Historial.HistorialPresenterCompl;
import com.example.prubatrabajofinal.Presenter.Historial.IHistorialPresenter;
import com.example.prubatrabajofinal.R;
import com.example.prubatrabajofinal.View.Autenticacion.Registrarse;
import com.example.prubatrabajofinal.View.Usuario.Usuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Historial extends AppCompatActivity implements IHistorialView, OnMapReadyCallback {
    private static final int REQUEST_CODE_ASK_PERMISSIONS=123;
    private GoogleMap mMap;

    IHistorialPresenter iHistorialPresenter;
    TextView tvFec,tvDur,tvIni,tvFin,tvDur2;
    ArrayList<UbicacionModel> lista;
    View consLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvFec=findViewById(R.id.textView2);
        tvDur=findViewById(R.id.textView4);
        tvIni=findViewById(R.id.textView5);
        tvFin=findViewById(R.id.textView6);
        tvDur2=findViewById(R.id.textView22);
        consLay=findViewById(R.id.constraintLayout8);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            consLay.setClipToOutline(true);
        }

        Bundle bundle=getIntent().getExtras();
        int traId=-1;
        if(bundle!=null)
            traId=bundle.getInt("id_trayectoria");
        //Toast.makeText(this,getString(R.string.cloud_data)+"obtener_trayectoria_id.php?"+traId,Toast.LENGTH_LONG).show();
        iHistorialPresenter =new HistorialPresenterCompl(this,findViewById(android.R.id.content));
        iHistorialPresenter.ObtenerTrayectoria(traId,getString(R.string.cloud_data)+"obtener_trayectoria_id.php");

        iHistorialPresenter.ObtenerUbicaciones(traId,getString(R.string.cloud_data)+"obtener_ubicacion.php");
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        //mapFragment.getMapAsync(this);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
    public void updateData(TrayectoriaModel tm){
        tvFec.setText("Fecha: "+tm.traFec);
        tvDur.setText("Duracion: "+tm.traDur+" min");
        tvIni.setText("Inicio: "+tm.traIni);
        tvFin.setText("Fin: "+tm.traFin);
        tvDur2.setText(tm.traDur+" min");

    }
    public void updateMapa(ArrayList<UbicacionModel> ubi){
        lista=ubi;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }

        googleMap.setContentDescription("Google Map with polylines.");

        /*PolylineOptions options = new PolylineOptions()
                .add(new LatLng(-16.399112013797804, -71.53753760296304))
                .add(new LatLng(-16.399088855965932, -71.53773340422933))
                .add(new LatLng(-16.39896020129426, -71.53811159571627))
                .add(new LatLng(-16.398895873922566, -71.53830203253268))
                .add(new LatLng(-16.398754353638772, -71.53869899948351))
                .color(Color.RED);*/

        PolylineOptions options = new PolylineOptions().color(Color.RED);
        for (int i=0;i<lista.size();i++){
            options.add(new LatLng(Double.parseDouble(lista.get(i).ubiLat),Double.parseDouble(lista.get(i).ubiLon)));
        }
        mMap.addPolyline(options);
        LatLng miUbicacion=new LatLng(Double.parseDouble(lista.get(0).ubiLat),Double.parseDouble(lista.get(0).ubiLon));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));

        googleMap.addMarker(new MarkerOptions().position(miUbicacion).title("Inicio")).showInfoWindow();
        //googleMap.addMarker(new MarkerOptions().position(new LatLng(-16.398754353638772, -71.53869899948351)).title("Final")).showInfoWindow();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(miUbicacion)
                .zoom(16)
                .bearing(90)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));







        //mMap.setMyLocationEnabled(true);

        //mMap.getUiSettings().setMyLocationButtonEnabled(false);

        /*LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng miUbicacion = new LatLng(-18.4115, -71.4746);
                mMap.addMarker(new MarkerOptions().position(miUbicacion).title("ubicacion actual"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(miUbicacion)
                        .zoom(14)
                        .bearing(90)
                        .tilt(45)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
*/

    }
}
