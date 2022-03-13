package com.example.mapsandroid;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapsandroid.databinding.ActivityMapsBinding;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ArrayList<Place> places;
    private Spinner spinnerMapType;
    private SeekBar seekBarZoom;
    private LatLng defaultLatLng = new LatLng(13.842257577203549, -89.09451935033549);
    private FollowPosition followPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when
        //the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        seekBarZoom = (SeekBar) findViewById(R.id.seekBarZoom);
        //HAGA USO DEL ASISTENTE PARA CREAR setOnSeekBarChangeListener. El único método que modificará es onProgressChanged
        seekBarZoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean
                    fromUser) {
                chooseMoveCamera(mMap, defaultLatLng, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
                                                       });
        spinnerMapType = (Spinner) findViewById(R.id.spinnerMapType);
        //HAGA USO DEL ASISTENTE PARA CREAR setOnItemSelectedListener
        spinnerMapType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {
                String mapType = spinnerMapType.getSelectedItem().toString();
                if (mMap == null) return;
                if (mapType.equals("MAP_TYPE_NORMAL")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else if (mapType.equals("MAP_TYPE_SATELLITE")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else if (mapType.equals("MAP_TYPE_HYBRID")) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //Broadcast Receiver. Permanecerá escuchando por actualizaciones de FetchPlacesService
    // (Servicio que intentará descargar los datos) HAGA USO DEL ASISTENTE PARA CREAR BroadcastReceiver
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                places = (ArrayList<Place>)
                        bundle.getSerializable(FetchPlacesService.RESULT);
                if (places != null && places.size() > 0) {
                    if (mMap != null) {
                        for (Place tmp : places) {
                            LatLng tmpLatLng = new LatLng(tmp.getLat(),
                                    tmp.getLon());
                            mMap.addMarker(new MarkerOptions().
                                    position(tmpLatLng).title(tmp.getPlaceName())
                            );
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(FetchPlacesService.NOTIFICATION));
        /**/
        Intent intent = new Intent(this, FetchPlacesService.class);
        startService(intent);
        if (followPosition != null) {
            followPosition.register(MapsActivity.this);
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(broadcastReceiver);
        if (followPosition != null)
            followPosition.unRegister(MapsActivity.this);
        super.onPause();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the
     * camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be
     * prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once
     * the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        followPosition = new FollowPosition(this.mMap, MapsActivity.this);
        followPosition.register(MapsActivity.this);
        Marker markerPerth,markerPerth2,markerPerth3,markerPerth4,markerPerth5;
        final LatLng Alcaldia = new LatLng(13.838134913979871, -89.09484657982068);
        final LatLng Parque = new LatLng(13.83798386112871, -89.09535083509635);
        final LatLng iglesia = new LatLng(13.838097150766268, -89.09647199845465);
        final LatLng cementerio = new LatLng(13.839430578792904, -89.09565124252718);
        final LatLng mercado= new LatLng(13.837457779733437, -89.09586581924025);
        //Moveremos la cámara a la Universidad Don Bosco
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLatLng));
        chooseMoveCamera(mMap, defaultLatLng, 30);
        markerPerth = mMap.addMarker(new MarkerOptions().position(Alcaldia).title("Alcaldia Municipal"));
        markerPerth2 = mMap.addMarker(new MarkerOptions().position(Parque).title("Parque") );
        markerPerth3= mMap.addMarker(new MarkerOptions().position(iglesia).title("Iglesia San Josè Guayabal") );
        markerPerth4 = mMap.addMarker(new MarkerOptions().position(cementerio).title("Cementerio Municipal") );
        markerPerth5 = mMap.addMarker(new MarkerOptions().position(mercado).title("Mercado Municipal") );
        drawShapes();

        mMap.setOnMarkerClickListener(marker -> {
            if (markerPerth.getTitle().equals(marker.getTitle())) {
                Toast.makeText(MapsActivity.this, markerPerth.getTitle(), Toast.LENGTH_SHORT).show();
            }
            if (markerPerth2.getTitle().equals(marker.getTitle())) {
                Toast.makeText(MapsActivity.this, markerPerth2.getTitle(), Toast.LENGTH_SHORT).show();
            }
            if (markerPerth3.getTitle().equals(marker.getTitle())) {
                Toast.makeText(MapsActivity.this, markerPerth3.getTitle(), Toast.LENGTH_SHORT).show();
            }
            if (markerPerth4.getTitle().equals(marker.getTitle())) {
                Toast.makeText(MapsActivity.this, markerPerth4.getTitle(), Toast.LENGTH_SHORT).show();
            }
            if (markerPerth5.getTitle().equals(marker.getTitle())) {
                Toast.makeText(MapsActivity.this, markerPerth4.getTitle(), Toast.LENGTH_SHORT).show();
            }


            Intent intent = new Intent(MapsActivity.this,PopupImgActivity.class);
            Bundle b = new Bundle();
            b.putString("Click", marker.getTitle());
            intent.putExtras(b);
            startActivity(intent);
            return true;
        });
    }



    //El siguiente método permitirá movernos de manera animada a una posición del mapa

    private void chooseMoveCamera(GoogleMap googleMap, LatLng tmpLatLng, int
            zoom) {
        CameraPosition cameraPosition = new
                CameraPosition.Builder().zoom(zoom).target(tmpLatLng).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    //El siguiente método custom permite agregar diferentes figuras
    private void drawShapes() {
        ShapesMap shapesMap = new ShapesMap(this.mMap);
        //PolyLines
        ArrayList<LatLng> lines = new ArrayList<>();
        lines.add(new LatLng(13.715777, -89.152472));
        lines.add(new LatLng(13.715342, -89.152437));
        lines.add(new LatLng(13.715389, -89.151542));
        lines.add(new LatLng(13.715768, -89.151579));
        lines.add(new LatLng(13.715777, -89.152472));
        //Llamado al método custom drawLine de shapesMap
        shapesMap.drawLine(lines, 5, Color.RED);
        ArrayList<LatLng> linesD = new ArrayList<>();
        ArrayList<LatLng> poligon = new ArrayList<>();
        poligon.add(new LatLng(13.836494162229263, -89.09450325711234));
        poligon.add(new LatLng(13.840051714043014, -89.09439060433796));
        poligon.add(new LatLng(13.840187139537644, -89.09567806461634));
        poligon.add(new LatLng(13.84033819095785, -89.09642371869421));
        poligon.add(new LatLng(13.839478759154677, -89.09661683773598));
        poligon.add(new LatLng(13.838603698055714, -89.0967455837638));
        poligon.add(new LatLng(13.837686963849515, -89.09687969420948));
        poligon.add(new LatLng(13.837176508009614, -89.09612331129593));
        poligon.add(new LatLng(13.836634798546218, -89.09533474187543));
        //Transparencia
        //Valor Hexadecimal, transparencia + color
        //0x: Valor hexadecimal
        //2F: Trasparencia
        //00FF00: Color Hexadecimal
        shapesMap.drawPoligon(poligon, 5, Color.GREEN, 0x2F00FF00);
        //Agregando Circulo
        LatLng circlePoint = new LatLng(13.714966, -89.155755);
        shapesMap.drawCircle(circlePoint, 50, Color.BLUE, 2, Color.TRANSPARENT);
    }



}