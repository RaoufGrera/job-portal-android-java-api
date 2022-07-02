package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;

import androidx.transition.TransitionManager;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG ="MapsActivity" ;
    private GoogleMap mMap;
    Marker now;

    ApiService service;
    TokenManager tokenManager;
    Call<Message> callMessage;
    Call<String> call;
    String UserCompName;


    @BindView(R.id.btn_save)
    Button btn_save;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;
    SupportMapFragment mapFragment;
    double lat =  32.8941887202 ;double lng= 13.1812374293 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
      //  contact_form_title = findViewById(R.id.contact_form_title);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
          UserCompName = getIntent().getExtras().getString("user");
        ButterKnife.bind(this);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(MapsActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeData(true);

            }});
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeData(false);

            }});
    }


    void changeData(boolean isSave){

        String stLat ="";
        String stLng ="";
        if(isSave){
           stLat = String.valueOf(lat);
          stLng = String.valueOf(lng) ;

        }

             callMessage = service.updateCompanyMap(UserCompName,stLat,stLng);


        showLoading();
        callMessage.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> callSave, Response<Message> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {

                    String msg = response.body().getMessage();
                    Context context = getApplicationContext();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG)
                            .show();
                    setResult(RESULT_OK, null);
                    finish();

                } else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(MapsActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.side_bar,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.toolback:
                onBackPressed();

                return true;

            default:
                return true;//super.onOptionsItemSelected(item);
        }
    }


    static String before(String value, String a) {
        // Return substring containing all characters before a string.
        int posA = value.indexOf(a);
        if (posA == -1) {
            return "";
        }
        return value.substring(0, posA);
    }
    static String after(String value, String a) {
        // Returns a substring containing all characters after a string.
        int posA = value.lastIndexOf(a);
        if (posA == -1) {
            return "";
        }
        int adjustedPosA = posA + a.length();
        if (adjustedPosA >= value.length()) {
            return "";
        }
        return value.substring(adjustedPosA);
    }
    private void showForm(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
    }

    private void showLoading(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        showLoading();

        call = service.getEditMap(UserCompName);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    String objLatLng = response.body();


                    showForm();

                    if( objLatLng.equals("")) {


                    }else{
                        String be = before(objLatLng, ":");
                        String af = after(objLatLng, ":");
                        lat = Double.parseDouble(be);
                        lng = Double.parseDouble(af);
                    }
                    LatLng sydney = new LatLng(  lat,lng);
                    MarkerOptions markerOptions = new MarkerOptions().position(sydney).title("قم بالضغظ على العلامة لتغيير الموقع الجغرافي.").draggable(true);
                    Marker marker =  mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    marker.showInfoWindow();
                } else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(MapsActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });


     mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                   lat = marker.getPosition().latitude;
                  lng = marker.getPosition().longitude ;
              //  contact_form_title.setText(total2 + " - " +total3);
               // marker.setSnippet(total2);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

        });
    }






}
