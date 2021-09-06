package org.techtown.db_6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {


    Intent intent;
    ArrayList<MemberStore> mealMemberStore;
    ArrayList<MemberStore> sideMealMemberStore;
    ArrayList<MemberStore> eduMemberStore;
    private GoogleMap googleMap;
    View card_view;
    Button edu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        intent = getIntent();
        mealMemberStore=intent.getParcelableArrayListExtra("mealMemberStores");
        sideMealMemberStore=intent.getParcelableArrayListExtra("sideMealMemberStores");
        eduMemberStore=intent.getParcelableArrayListExtra("eduMemberStores");
        edu = (Button) findViewById(R.id.btn3);


//인텐트로 arraylist를 받아올 때 사용함

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        card_view=findViewById(R.id.card_view);
/*
        TextView textView3= findViewById(R.id.textView3);

            RecyclerView recyclerView = findViewById(R.id.recyclerview);
            recyclerView.addItemDecoration(
                    new DividerItemDecoration(this, R.drawable.line_divider));

            LinearLayoutManager manager
                    = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

            recyclerView.setLayoutManager(manager); // LayoutManager 등록
            recyclerView.setAdapter(new MyAdapter(dataList)); // Adapter 등록

*/

    /*    for(MemberStore store : mealMemberStore)
        {
            System.out.println("급식: " + store.getStore_type());
        }*/

        for(MemberStore store : sideMealMemberStore)
        {
            System.out.println("부식: " + store.getStore_type());
        }

        for(MemberStore store : eduMemberStore)
        {
            System.out.println("교육: " + store.getStore_type());
            System.out.println("교육: "+ store.getStore_address());
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //지도 객체 추출
        mapFragment.getMapAsync(this);
        //지도객체와 onMapReadyCallback객체를 연결함

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //map이 준비되면 호출되는 함수
        this.googleMap = googleMap;
        // 35.8691036023011, 128.59554606027856 중앙로 대백앞
        LatLng latLng = new LatLng(35.8691036023011, 128.59554606027856);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(13));
//지도는 카메라가 아래를 내려다보는 듯한 내용이 모델링됨

        for (MemberStore store : mealMemberStore) {
            MarkerOptions markerOptions = new MarkerOptions();
            //마커에 대한 정보를 갖고 있는 객체
            markerOptions.position(new LatLng(store.getLatitude(), store.getLongitude()))
                    .title(store.getStore_name())
                    .icon(BitmapDescriptorFactory.defaultMarker(210))
                    .snippet(store.getStore_num()+"/"+store.getStore_address());

            googleMap.addMarker(markerOptions).hideInfoWindow();
        }

        for(MemberStore store: sideMealMemberStore) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(store.getLatitude(),store.getLongitude()))
                    .title(store.getStore_name())
                    .icon(BitmapDescriptorFactory.defaultMarker(145))
                    .snippet(store.getStore_num()+"/"+store.getStore_address());

            googleMap.addMarker(markerOptions).hideInfoWindow();
        }

        for(MemberStore store: eduMemberStore) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(store.getLatitude(),store.getLongitude()))
                    .title(store.getStore_name())
                    .icon(BitmapDescriptorFactory.defaultMarker(55))
                    .snippet(store.getStore_num()+"/"+store.getStore_address());

            googleMap.addMarker(markerOptions).hideInfoWindow();

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            checkLocationPermissionWithRationale();
        }

        this.googleMap.setOnMarkerClickListener(markerClickListener);
        this.googleMap.setOnMapClickListener(mapClickListener);

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermissionWithRationale() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("위치정보")
                        .setMessage("이 앱을 사용하기 위해서는 위치정보에 접근이 필요합니다. 위치정보 접근을 허용하여 주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        googleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(@NonNull @NotNull LatLng latLng) {
            card_view.setVisibility(View.GONE);
        }


    };

    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            marker.hideInfoWindow();
            card_view.setVisibility(View.VISIBLE);
            TextView name = (TextView)findViewById(R.id.st_name);
            TextView num =(TextView)findViewById(R.id.st_num);
            TextView address=(TextView)findViewById(R.id.st_address);
            String[] arr= new String[2];
            arr=marker.getSnippet().toString().split("/");
            name.setText(marker.getTitle());
            num.setText(arr[0]);
            address.setText(arr[1]);

            return false;
        }
    };



}