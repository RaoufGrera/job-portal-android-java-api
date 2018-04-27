package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import libyacvpro.libya_cv.entities.ExperiencePackage.Experience;
import libyacvpro.libya_cv.entities.ExperiencePackage.ExperienceResponse;
import libyacvpro.libya_cv.entities.IntegrString;
import libyacvpro.libya_cv.enums.SectionEnum;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

public class ExperienceActivity extends AppCompatActivity {

    private NonScrollListView lvItems;
    private static final String TAG = "ExperienceActivity";

    ApiService service;
    TokenManager tokenManager;
    Call<ExperienceResponse> call;
     Button imgWifi;

    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);
        imgWifi = (Button) findViewById(R.id.imgWifi);
         ButterKnife.bind(this);



        apiLoad();
    }


    private void apiLoad(){

        boolean IsValid = isOnline();
        if (!IsValid) {
            showWifi();
            return;
        }
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        if(tokenManager.getToken() == null){
            startActivity(new Intent(ExperienceActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        showLoading();

        call = service.experience();
        call.enqueue(new Callback<ExperienceResponse>() {
            @Override
            public void onResponse(Call<ExperienceResponse> call, Response<ExperienceResponse> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){

                    lvItems = (NonScrollListView) findViewById(R.id.lvItems);
                    List<Experience> ee = response.body().getExperience();
                    setData(ee);

                    showForm();
                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(ExperienceActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<ExperienceResponse> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }

    @OnClick(R.id.imgWifi)
    void refreshActivity(){
        apiLoad();

    }
    private void showWifi(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
        imgWifi.setVisibility(View.VISIBLE);
    }
    public boolean isOnline() {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null &&
                    cm.getActiveNetworkInfo().isConnectedOrConnecting();


        } catch (Exception e) { return false; }
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
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data){

        apiLoad();

    }
    private void setData(List<Experience> ee){
/*
        if(ee.size() <= 0)
            lblInfo.setVisibility(View.VISIBLE);*/


        ArrayList<IntegrString> myLibrary = new ArrayList<IntegrString>();

        for (int i = 0; i < ee.size(); i++)
        {
            myLibrary.add(new IntegrString(ee.get(i).getExp_id(),ee.get(i).getExp_name()));
        }

        CustomAdpter adapter = new CustomAdpter(this,AddEditExperienceActivity.class, SectionEnum.EXPERIENCE.getSectionLetter(), myLibrary);
        lvItems.setAdapter(adapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CustomAdpter.ViewHolder holder = (CustomAdpter.ViewHolder) view.getTag();
                IntegrString item = holder.getItem();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.side_bar,menu);
        return true;
    }

    private void showForm(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.VISIBLE);
        imgWifi.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
    }

    private void showLoading(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.GONE);
        imgWifi.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnNew)
    void addNewItem(){
        Intent intent = new Intent(ExperienceActivity.this, AddEditExperienceActivity.class);
        intent.putExtra("id", 0);
        startActivityForResult(intent,0);
    }

}
