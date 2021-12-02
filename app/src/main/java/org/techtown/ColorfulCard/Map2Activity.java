package org.techtown.ColorfulCard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.techtown.ColorfulCard.MapActivity.favorMemberStore;

public class Map2Activity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    View card_view;
    ImageButton call;
    TextView choiceStoreName;
    MemberStore choiceStore;
    ArrayList<DataItem.MapData> mapDataList;
    ArrayList<Marker> searchResultMarkers= new ArrayList<>();
    ImageView sv_location2;
    ImageButton emptyStar, fullStar;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        userID= MapActivity.userID;
        choiceStore =getIntent().getParcelableExtra("choiceStore");
        mapDataList = SearchStoreListAdapter.mapDataList;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);


        choiceStoreName=findViewById(R.id.choiceStoreName);
        choiceStoreName.setText(choiceStore.getSname());

        sv_location2=findViewById(R.id.sv_location2);
        sv_location2.getBackground().setAlpha(160);
        sv_location2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        card_view=findViewById(R.id.card_view);
        card_view.setVisibility(View.VISIBLE);

        TextView name = (TextView)findViewById(R.id.st_name);
        TextView num =(TextView)findViewById(R.id.st_num);
        TextView address=(TextView)findViewById(R.id.st_address);

        emptyStar= findViewById(R.id.emptyStar);
        fullStar= findViewById(R.id.fullStar);

        name.setText(choiceStore.getSname());
        num.setText(choiceStore.getSnum());
        address.setText(choiceStore.getSaddress());

        String st_num=choiceStore.getSnum().toString();
        st_num=st_num.replace("-","");
        String tell;
        tell="tel:"+st_num;

        for(int i=0;i<favorMemberStore.size();i++){

            if(favorMemberStore.get(i).getSid()==choiceStore.getSid()){
                emptyStar.setVisibility(View.GONE);
                fullStar.setVisibility(View.VISIBLE);
            }else{

                emptyStar.setVisibility(View.VISIBLE);
                fullStar.setVisibility(View.GONE);
            }
            break;
        }
        //즐찾은 가능한데 아직 마커 받아오기 전이라 마커 아이콘 변경이 불가능..할껄? 반복문돌려서 찾으면 가능할지도..
        emptyStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"해당 마커 클릭 후 즐겨찾기를 등록해주세요.",Toast.LENGTH_SHORT).show();
            }
        });

        call=(ImageButton) findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(tell));
                startActivity(intent);
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


        this.googleMap = googleMap;

        googleMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(choiceStore.getLatitude(),choiceStore.getSlongitude())));


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            checkLocationPermissionWithRationale();
        }


        for(DataItem.MapData mapData: mapDataList){


            MemberStore store= mapData.getStore();

            if(choiceStore.getSid()==store.getSid()){   //같으면 안넣어줌
                continue;
            }

            Log.d("tag","mapData Name"+ store.getSname());
            String storeType = store.getStype();

            BitmapDrawable bitmapDraw=null;

            for(int i=0;i<favorMemberStore.size();i++){

                if(favorMemberStore.get(i).getSid()==store.getSid()){
                    bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.favormarker);
                }else{

                    if (storeType.equals("급식")) {
                        bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.bluemarker);

                    } else if (storeType.equals("교육")) {
                        bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.yellowmarker);

                    } else {
                        bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.greenmarker);
                    }
                }
            }

            Bitmap b4=bitmapDraw.getBitmap();
            Bitmap resultCustomMarker = Bitmap.createScaledBitmap(b4, 60, 60, false);

            MarkerOptions markerOptions2 = new MarkerOptions();
            markerOptions2.position(new LatLng(store.getLatitude(),store.getSlongitude()))
                    .title(store.getSname())
                    .icon(BitmapDescriptorFactory.fromBitmap(resultCustomMarker));      //수정갈겨야함

            Marker resultMarker =googleMap.addMarker(markerOptions2);
            resultMarker.setTag(store);

            searchResultMarkers.add(resultMarker);
        }



        String stype= choiceStore.getStype();
        BitmapDrawable bitmapDrawChoice=null;

        for(int i=0;i<favorMemberStore.size();i++) {

            if (favorMemberStore.get(i).getSid() == choiceStore.getSid()) {

                bitmapDrawChoice = (BitmapDrawable) getResources().getDrawable(R.drawable.redgagul);
            } else {
                if (stype.equals("급식")) {

                    bitmapDrawChoice = (BitmapDrawable) getResources().getDrawable(R.drawable.bluegagul);

                } else if (stype.equals("교육")) {

                    bitmapDrawChoice = (BitmapDrawable) getResources().getDrawable(R.drawable.yellowgagul);
                } else {
                    bitmapDrawChoice = (BitmapDrawable) getResources().getDrawable(R.drawable.greengagul);
                }
            }
        }

        MarkerOptions markerOptions = new MarkerOptions();
        Bitmap b=bitmapDrawChoice.getBitmap();
        Bitmap customMarker = Bitmap.createScaledBitmap(b, 100, 100, false);

        markerOptions.position(new LatLng(choiceStore.getLatitude(), choiceStore.getSlongitude()))
                .title(choiceStore.getSname())
                .icon((BitmapDescriptorFactory.fromBitmap(customMarker)));

        Marker marker =googleMap.addMarker(markerOptions);
        marker.setTag(choiceStore);
        marker.showInfoWindow();

        this.googleMap.setOnMarkerClickListener(markerClickListener);

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


    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {


            card_view.setVisibility(View.VISIBLE);
            TextView name = (TextView)findViewById(R.id.st_name);
            TextView num =(TextView)findViewById(R.id.st_num);
            TextView address=(TextView)findViewById(R.id.st_address);

            MemberStore store = (MemberStore) marker.getTag();


            name.setText(store.getSname());
            num.setText(store.getSnum());
            address.setText(store.getSaddress());

            String st_num=store.getSnum().toString();
            st_num=st_num.replace("-","");
            String tell;
            tell="tel:"+st_num;


            for(int i=0;i<favorMemberStore.size();i++) {

                if (favorMemberStore.get(i).getSid() == store.getSid()) {

                    emptyStar.setVisibility(View.GONE);
                    fullStar.setVisibility(View.VISIBLE);
                } else {
                    emptyStar.setVisibility(View.VISIBLE);
                    fullStar.setVisibility(View.GONE);
                }
            }

            //빈 별을 눌렀다 -> 즐겨찾기를 등록할거다
            emptyStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //emptyStar-> gone , fullStar-> visible
                    emptyStar.setVisibility(View.GONE);
                    fullStar.setVisibility(View.VISIBLE);

                    //favorMemberStore에 해당 즐찾 추가하기
                    favorMemberStore.add(store);
                    BitmapDrawable bitmapDraw4;

                    //해당 마커 즐찾마커로 변경하기
                    //마커이미지 결정
                    if( store.getSid() == choiceStore.getSid()){

                        bitmapDraw4=(BitmapDrawable)getResources().getDrawable(R.drawable.redgagul);
                        Bitmap b4=bitmapDraw4.getBitmap();
                        Bitmap favorCustomMarker = Bitmap.createScaledBitmap(b4, 100, 100, false);
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(favorCustomMarker));

                    }else{
                        bitmapDraw4=(BitmapDrawable)getResources().getDrawable(R.drawable.favormarker);
                        Bitmap b4=bitmapDraw4.getBitmap();
                        Bitmap favorCustomMarker = Bitmap.createScaledBitmap(b4, 60, 60, false);
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(favorCustomMarker));
                    }

                    registerFavoriteStore(userID,store.getSid());

                }
            });


            // 꽉찬 별을 눌렀다 -> 즐겨찾기를 해제할거다
            fullStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fullStar.setVisibility(View.GONE);
                    emptyStar.setVisibility(View.VISIBLE);

                    favorMemberStore.remove(store);

                    String stype= store.getStype();
                    BitmapDrawable bitmapDraw;

                    if( store.getSid() == choiceStore.getSid()) {

                        if (stype.equals("급식")) {
                            bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.bluegagul);

                        } else if (stype.equals("교육")) {
                            bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.yellowgagul);

                        } else {
                            bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.greengagul);
                        }
                    }

                    else{

                        if(stype.equals("급식")){
                            bitmapDraw=(BitmapDrawable)getResources().getDrawable(R.drawable.bluemarker);

                        }else if(stype.equals("교육")){
                            bitmapDraw=(BitmapDrawable)getResources().getDrawable(R.drawable.yellowmarker);

                        }else{
                            bitmapDraw=(BitmapDrawable)getResources().getDrawable(R.drawable.greenmarker);
                        }
                    }


                    Bitmap b4=bitmapDraw.getBitmap();
                    Bitmap customMarker;
                    if( store.getSid() == choiceStore.getSid()){
                        customMarker= Bitmap.createScaledBitmap(b4, 100, 100, false);
                    }else{
                        customMarker= Bitmap.createScaledBitmap(b4, 60, 60, false);
                    }

                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(customMarker));

                    //DB에 즐겨찾기 해제
                    releaseFavoriteStore(userID,store.getSid());

                }
            });

            return false;
        }
    };


    void registerFavoriteStore(String userId, int sid){
        Server server = new Server();
        RetrofitService service= server.getRetrofitService();
        Call<Integer> call=  service.postFavoriteStore(userID,sid);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {

                if(response.isSuccessful()){
                    Log.d("tag",response.body()+"개 즐찾 추가후");
                    if(response.body().intValue()==1){
                        Toast.makeText(getApplicationContext(), "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void releaseFavoriteStore(String userID, int sid){
        Server server = new Server();
        RetrofitService service= server.getRetrofitService();
        Call<Integer> call= service.deleteFavoriteStore(userID,sid);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    if(response.body().intValue()==1){
                        Toast.makeText(getApplicationContext(), "즐겨찾기가 해제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
    }
}
