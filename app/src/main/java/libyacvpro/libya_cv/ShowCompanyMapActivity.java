package libyacvpro.libya_cv;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowCompanyMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG ="ShowCompanyMapActivity" ;
    private GoogleMap mMap;
    Marker now;
    SupportMapFragment mapFragment;

    double lat =  32.8941887202 ;double lng= 13.1812374293 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_company_map);


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String stLat = getIntent().getExtras().getString("lat");
        String stLng = getIntent().getExtras().getString("lng");

        lat = Double.parseDouble(stLat);
        lng = Double.parseDouble(stLng);
        LatLng sydney = new LatLng(  lat,lng);
        MarkerOptions markerOptions = new MarkerOptions().position(sydney).draggable(false);
         mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

}
