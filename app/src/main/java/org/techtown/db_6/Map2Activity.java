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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
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
import java.util.Map;

public class Map2Activity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    View card_view;
    ImageButton call;
    TextView choiceStoreName;
    MemberStore choiceStore;
    ImageView sv_location2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        choiceStore =getIntent().getParcelableExtra("choiceStore");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        //지도 객체 추출
        mapFragment.getMapAsync(this);
        //지도객체와 onMapReadyCallback객체를 연결함


        choiceStoreName=findViewById(R.id.choiceStoreName);
        choiceStoreName.setText(choiceStore.getStore_name());
        sv_location2=findViewById(R.id.sv_location2);
        sv_location2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sv_location2.getBackground().setAlpha(160);

        card_view=findViewById(R.id.card_view);
        card_view.setVisibility(View.VISIBLE);
        TextView name = (TextView)findViewById(R.id.st_name);
        TextView num =(TextView)findViewById(R.id.st_num);
        TextView address=(TextView)findViewById(R.id.st_address);

        name.setText(choiceStore.getStore_name());
        num.setText(choiceStore.getStore_num());
        address.setText(choiceStore.getStore_address());

        String st_num=choiceStore.getStore_num().toString();
        st_num=st_num.replace("-","");
        String tell;
        tell="tel:"+st_num;

        call=(ImageButton) findViewById(R.id.call);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(tell));
                startActivity(intent);
            }
        });


    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


        int hue;

        //map이 준비되면 호출되는 함수
        this.googleMap = googleMap;



        googleMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(choiceStore.getLatitude(),choiceStore.getLongitude())));

        //지도는 카메라가 아래를 내려다보는 듯한 내용이 모델링됨

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            checkLocationPermissionWithRationale();
        }

        if(choiceStore.getStore_type().equals("급식"))

            hue=210;
        else if(choiceStore.getStore_type().equals("부식"))
            hue=145;
        else
            hue=55;


        MarkerOptions markerOptions = new MarkerOptions();
        //마커에 대한 정보를 갖고 있는 객체
        markerOptions.position(new LatLng(choiceStore.getLatitude(), choiceStore.getLongitude()))
                .title(choiceStore.getStore_name())
                .icon(BitmapDescriptorFactory.defaultMarker(hue));

        Marker marker =googleMap.addMarker(markerOptions);


        marker.showInfoWindow();


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
                                ActivityCompat.requestPermissions(Map2Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
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




}
