package libyacvpro.libya_cv;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import libyacvpro.libya_cv.adapter.AppCvAdapter;
import libyacvpro.libya_cv.entities.Seeker;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppJobCVActivity extends AppCompatActivity {

    private static final String TAG = "AppJobCVActivity";

    NonScrollListView LISTVIEW;
    AppCvAdapter ListAdapter ;

    ArrayList<String> userID= new ArrayList<String>();
    ArrayList<String> tvName= new ArrayList<String>();
    ArrayList<String> tvDomain= new ArrayList<String>();
    ArrayList<String> tvCity= new ArrayList<String>() ;
    ArrayList<String> tvEdt= new ArrayList<String>() ;
    ArrayList<String> tvExp= new ArrayList<String>() ;
    ArrayList<String> tvMathc = new ArrayList<String>();
    ArrayList<String> tvimgView = new ArrayList<String>();
    ArrayList<String> tvAbout = new ArrayList<String>();
    ArrayList<Integer> tvReq_event = new ArrayList<Integer>();
    ApiService service;
    TokenManager tokenManager;
    Call<List<Seeker>> call;
    Button imgWifi;
    Button btnReload;

    String pUser;
    Integer itemID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_job_cv);
        LISTVIEW = (NonScrollListView) findViewById(R.id.listView2);
        btnReload = (Button) findViewById(R.id.btnReload);

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSQLiteDBdata() ;
            }});
        pUser = getIntent().getExtras().getString("user");

        itemID = getIntent().getExtras().getInt("id");


    }
    @Override
    protected void onResume() {

        ShowSQLiteDBdata() ;

        super.onResume();
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
    private void ShowSQLiteDBdata() {


        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        if(tokenManager.getToken() == null){
            startActivity(new Intent(AppJobCVActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        call = service.getSearchAppCvs(pUser,itemID);
        call.enqueue(new Callback<List<Seeker>>() {
            @Override
            public void onResponse(Call<List<Seeker>> call, Response<List<Seeker>> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){

                     List<Seeker> ee = response.body();
                    setData(ee);

                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(AppJobCVActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<List<Seeker>> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }


    private void setData(List<Seeker> s){
        userID.clear();
        tvName.clear();
        tvDomain.clear();
        tvCity.clear();
        tvEdt.clear();
        tvExp.clear();
        tvMathc.clear();
        tvimgView.clear();
        tvAbout.clear();
        tvReq_event.clear();

        for (int i = 0; i < s.size(); i++)
        {
            userID.add((s.get(i).getSeeker_id().toString()));
            tvName.add((s.get(i).getFname() +s.get(i).getLname()));
            tvDomain.add((s.get(i).getDomain_name()));
            tvCity.add((s.get(i).getCity_name()));
            tvEdt.add((s.get(i).getEdt_name()));
            tvExp.add((s.get(i).getExp()));
            tvMathc.add((s.get(i).getMatch()));
            tvimgView.add((s.get(i).getImage()));
            tvAbout.add((s.get(i).getAbout()));
            tvReq_event.add((s.get(i).getReq_event()));
        }


        ListAdapter = new AppCvAdapter(AppJobCVActivity.this,
                itemID,
                userID,
        tvName,
        tvDomain,
        tvCity,
        tvEdt,
        tvExp,
        tvMathc,
        tvimgView,
                tvAbout,
                tvReq_event
        );

        LISTVIEW.setAdapter(ListAdapter);

        LISTVIEW.invalidateViews();

    }


}
