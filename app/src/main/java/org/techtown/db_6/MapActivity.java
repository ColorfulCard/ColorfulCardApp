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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    Button btn1,btn2,btn3;
    ImageButton call;
    ImageView searchimage;
    ArrayList<Marker> mealMarker = new ArrayList<Marker>();
    ArrayList<Marker> sideMealMarker = new ArrayList<Marker>();
    ArrayList<Marker> eduMarker = new ArrayList<Marker>();
    Boolean btn1Flag =false;
    Boolean btn2Flag =false;
    Boolean btn3Flag =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        intent = getIntent();
        mealMemberStore=intent.getParcelableArrayListExtra("mealMemberStores"); //인텐트로 arraylist를 받아올 때 사용함
        sideMealMemberStore=intent.getParcelableArrayListExtra("sideMealMemberStores");
        eduMemberStore=intent.getParcelableArrayListExtra("eduMemberStores");


        if(savedInstanceState!=null) {

            Log.d("tag", "들어옴 안들어옴?");
            String name = getIntent().getStringExtra("id");
            Log.d("tag", name);
            return;
        }

        for(MemberStore store : sideMealMemberStore)  //확인용
        {
            System.out.println("부식: " + store.getStore_type());
            System.out.println("부식: " + store.getStore_address());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        card_view=findViewById(R.id.card_view);
        btn1= (Button) findViewById(R.id.btn1);  //급식버튼
        btn2 = (Button) findViewById(R.id.btn2);  //부식버튼
        btn3 = (Button) findViewById(R.id.btn3);  //급식버튼
        searchimage = (ImageView) findViewById(R.id.sv_location);
        call=(ImageButton)findViewById(R.id.call);

        searchimage.getBackground().setAlpha(140);

        searchimage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchStoreActivity.class);
                startActivity(i);
            }


        });


        btn1.setOnClickListener(new View.OnClickListener() {  //급식클릭시
            @Override
            public void onClick(View v) {

                btn1Flag=!btn1Flag;
                if(btn1Flag) //true면 급식만 보여줌
                {
                    for (Marker marker : mealMarker) {
                        marker.setVisible(true);
                    }
                    for (Marker marker : sideMealMarker) {
                        marker.setVisible(false);
                    }
                    for (Marker marker : eduMarker) {
                        marker.setVisible(false);
                    }
                    btn1.setBackgroundColor(Color.parseColor("#133A55"));   //눌렀을 떄 색깔
                }
                else //false면 전체 다 보여줌
                {
                    if(btn2Flag==false && btn3Flag==false) {

                        for (Marker marker : sideMealMarker) {
                            marker.setVisible(true);
                        }
                        for (Marker marker : eduMarker) {
                            marker.setVisible(true);
                        }
                        for (Marker marker : mealMarker) {
                            marker.setVisible(true);
                        }
                    }
                    btn1.setBackgroundColor(Color.parseColor("#2980B9"));  //풀었을 때 버튼색깔
                }
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() { //부식클릭시
            @Override
            public void onClick(View v) {
                btn2Flag=!btn2Flag;
                if(btn2Flag)
                {
                    for(Marker marker : sideMealMarker) {
                        marker.setVisible(true);
                    }
                    for(Marker marker : mealMarker) {
                        marker.setVisible(false);
                    }
                    for(Marker marker : eduMarker){
                        marker.setVisible(false);
                    }

                    btn2.setBackgroundColor(Color.parseColor("#0B4D40"));
                }
                else{
                    if(btn1Flag==false && btn3Flag==false) {

                        for(Marker marker : sideMealMarker) {
                            marker.setVisible(true);
                        }
                        for (Marker marker : mealMarker) {
                            marker.setVisible(true);
                        }
                        for (Marker marker : eduMarker) {
                            marker.setVisible(true);
                        }
                    }
                    btn2.setBackgroundColor(Color.parseColor("#16A085"));

                }
            }

        });

        btn3.setOnClickListener(new View.OnClickListener() { //교육클릭시
            @Override
            public void onClick(View v) {

                btn3Flag = !btn3Flag;
                if (btn3Flag) {
                    for (Marker marker : eduMarker) {
                        marker.setVisible(true);
                    }
                    for (Marker marker : mealMarker) {
                        marker.setVisible(false);
                    }
                    for (Marker marker : sideMealMarker) {
                        marker.setVisible(false);
                    }

                    btn3.setBackgroundColor(Color.parseColor("#D9A800"));

                }else{

                    if(btn1Flag==false&& btn2Flag==false) {
                        for (Marker marker : mealMarker) {
                            marker.setVisible(true);
                        }
                        for (Marker marker : sideMealMarker) {
                            marker.setVisible(true);
                        }
                    }

                    btn3.setBackgroundColor(Color.parseColor("#FFDB58"));
                }

            }
        });


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
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //지도는 카메라가 아래를 내려다보는 듯한 내용이 모델링됨

        for (MemberStore store : mealMemberStore) {
            MarkerOptions markerOptions = new MarkerOptions();
            //마커에 대한 정보를 갖고 있는 객체
            markerOptions.position(new LatLng(store.getLatitude(), store.getLongitude()))
                    .title(store.getStore_name())
                    .icon(BitmapDescriptorFactory.defaultMarker(210));

            Marker marker =googleMap.addMarker(markerOptions);
            marker.setTag(store);
            mealMarker.add(marker);
        }

        for(MemberStore store: sideMealMemberStore) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(store.getLatitude(),store.getLongitude()))
                    .title(store.getStore_name())
                    .icon(BitmapDescriptorFactory.defaultMarker(90));

            Marker marker =googleMap.addMarker(markerOptions);
            marker.setTag(store);
            sideMealMarker.add(marker);
        }

        for(MemberStore store: eduMemberStore) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(store.getLatitude(),store.getLongitude()))
                    .title(store.getStore_name())
                    .icon(BitmapDescriptorFactory.defaultMarker(55));

            Marker marker =googleMap.addMarker(markerOptions);
            marker.setTag(store);
            eduMarker.add(marker);
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

            card_view.setVisibility(View.VISIBLE);
            TextView name = (TextView)findViewById(R.id.st_name);
            TextView num =(TextView)findViewById(R.id.st_num);
            TextView address=(TextView)findViewById(R.id.st_address);

            MemberStore store = (MemberStore) marker.getTag();
            name.setText(store.getStore_name());
            num.setText(store.getStore_num());
            address.setText(store.getStore_address());

            String st_num=store.getStore_num().toString();
            st_num=st_num.replace("-","");
            String tell;
            tell="tel:"+st_num;

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(tell));
                    startActivity(intent);
                }
            });

            return false;
        }
    };


}