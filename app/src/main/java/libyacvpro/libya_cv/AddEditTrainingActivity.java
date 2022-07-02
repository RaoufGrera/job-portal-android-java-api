package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import androidx.transition.TransitionManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.entities.TrainingPackage.Training;
import libyacvpro.libya_cv.entities.TrainingPackage.TrainingForEdit;
import libyacvpro.libya_cv.enums.ValidationInput;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

public class AddEditTrainingActivity extends AppCompatActivity {
    private static final String TAG = "AddEditTrainingActivity";

    ApiService service;
    TokenManager tokenManager;
    Call<Message> callMessage;
    Call<TrainingForEdit> call;

    //Call<Message> callSave;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;


    private EditText  txtTrainDate;
    private EditText  txtTrainName;

    private EditText txtTrainComp;
    private static final String APP_ID = "ca-app-pub-9929016091047307~2213947061";
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_training);
        Integer itemID = getIntent().getExtras().getInt("id");


        txtTrainComp = (EditText) findViewById(R.id.txtTrainComp);
        txtTrainName = (EditText) findViewById(R.id.txtTrainName);
        txtTrainDate = (EditText) findViewById(R.id.txtTrainDate);


        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

      //  MobileAds.initialize(this, APP_ID);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-9929016091047307/3960713000");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if (tokenManager.getToken() == null) {
            startActivity(new Intent(AddEditTrainingActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        showLoading();

        if(itemID == null){
            call = service.createTraining();

        }else{
            call = service.getTraining(itemID);
        }
        call.enqueue(new Callback<TrainingForEdit>() {
            @Override
            public void onResponse(Call<TrainingForEdit> call, Response<TrainingForEdit> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                     Training objEdu = response.body().getTraining();

                    if(objEdu != null)
                        setDataInfo(objEdu);
                    showForm();

                } else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(AddEditTrainingActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<TrainingForEdit> call, Throwable t) {
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
    private void setDataInfo(Training ii){
        txtTrainName.setText(ii.getTrain_name());
        txtTrainComp.setText(ii.getTrain_comp());
        txtTrainDate.setText(ii.getTrain_date());

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

    @OnClick(R.id.btnSave)
    void changeData(){
        Integer itemID = getIntent().getExtras().getInt("id");

         String pTrain = txtTrainName.getText().toString();
        String pTrainComp = txtTrainComp.getText().toString();
        String pTrainDate = txtTrainDate.getText().toString();
        Boolean isValid = true;
        if (!ValidationInput.isValidNOT_EMPTY(pTrain)) {
            txtTrainName.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtTrainName.setError(null);
        }
        if (!ValidationInput.isValidNOT_EMPTY(pTrainComp)) {
            txtTrainComp.setError("الحقل مطلوب"); isValid=false;
        }else{
            txtTrainComp.setError(null);

        }

        if (!ValidationInput.isValidYear(pTrainDate)) {
            txtTrainDate.setError("الادخال غير صحيح"); isValid=false;
        }else{
            txtTrainDate.setError(null);
        }

        if(!isValid)
            return;

            if (itemID == 0) {
                callMessage = service.storeTraining(pTrain, pTrainComp, pTrainDate, "POST");
            } else {
                callMessage = service.updateTraining(itemID, pTrain, pTrainComp, pTrainDate, "PATCH");
            }

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
                        startActivity(new Intent(AddEditTrainingActivity.this, LoginActivity.class));
                        finish();

                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());
                }
            });

    }

}

