package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import libyacvpro.libya_cv.entities.Refreshed;
import libyacvpro.libya_cv.entities.RefreshedForEdit;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

public class RefreshActivity extends AppCompatActivity {
    private static final String TAG = "RefreshActivity";


    ApiService service;
    TokenManager tokenManager;
    Call<RefreshedForEdit> callMessage;
    Call<RefreshedForEdit> call;

    //Call<Message> callSave;

    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;



    @BindView(R.id.lablLastUpdated)
    TextView lblLastUpdated;
    @BindView(R.id.btnSaveRefresh)
    Button btnSaveRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);

        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(RefreshActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        showLoading();

        call = service.refreshed();
        call.enqueue(new Callback<RefreshedForEdit>() {
            @Override
            public void onResponse(Call<RefreshedForEdit> call, Response<RefreshedForEdit> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){



                    Refreshed ii = response.body().getRefreshed();
                     setDataInfo(ii);
                    showForm();


                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(RefreshActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<RefreshedForEdit> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });

    }

    private void setDataInfo(Refreshed ii){
        lblLastUpdated.setText(ii.getUpdate());
        if(!ii.getCheck_date()){
            btnSaveRefresh.setVisibility(View.GONE);
        }


    }
    @OnClick(R.id.btnSaveRefresh)
    void getSeeker(){

        showLoading();
        callMessage = service.afterRefreshed();
        callMessage.enqueue(new Callback<RefreshedForEdit>() {
            @Override
            public void onResponse(Call<RefreshedForEdit> call, Response<RefreshedForEdit> response) {
                Log.w(TAG, "onResponse: " + response );


                if (response.isSuccessful()) {

                    String msg = "قد تم تنفيذ العملية بنجاح.";
                    Context context = getApplicationContext();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG)
                            .show();
                    Refreshed ii = response.body().getRefreshed();

                    setDataInfo(ii);
                    showForm();


                } else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(RefreshActivity.this, LoginActivity.class));
                    finish();

                }

            }

            @Override
            public void onFailure(Call<RefreshedForEdit> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null){
            call.cancel();
            call = null;
        }
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
}
