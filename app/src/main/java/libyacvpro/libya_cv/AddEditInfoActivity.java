package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import androidx.transition.TransitionManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import libyacvpro.libya_cv.entities.InfoPackage.Info;
import libyacvpro.libya_cv.entities.InfoPackage.InfoForEdit;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.enums.ValidationInput;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

public class AddEditInfoActivity extends AppCompatActivity {

    private static final String TAG = "AddEditSkillsActivity";

    ApiService service;
    TokenManager tokenManager;
    Call<Message> callMessage;
    Call<InfoForEdit> call;

    //Call<Message> callSave;

    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;


     EditText txtInfoName;

    @BindView(R.id.txtInfoText)
    TextView txtInfoText;

     EditText txtInfoDate;
     Button imgWifi;

    private static final String APP_ID = "ca-app-pub-9929016091047307~2213947061";
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_info);

        txtInfoName = (EditText) findViewById(R.id.txtInfoName);
        txtInfoDate = (EditText) findViewById(R.id.txtInfoDate);
        imgWifi = (Button) findViewById(R.id.imgWifi);

       // MobileAds.initialize(this, APP_ID);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-9929016091047307/3960713000");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(AddEditInfoActivity.this, LoginActivity.class));
            finish();
        }


        loadApi();
    }


    @OnClick(R.id.imgWifi)
    void refreshActivity(){
        loadApi();

    }
    private void showWifi(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.GONE);
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

    void loadApi() {

        boolean IsValid = isOnline();
        if (!IsValid) {
            showWifi();
            return;
        }
        Integer itemID = getIntent().getExtras().getInt("id");
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        showLoading();
        if(itemID ==null){
            call = service.createInfo();
        }else{
            call = service.getInfo(itemID);
        }
        call.enqueue(new Callback<InfoForEdit>() {
            @Override
            public void onResponse(Call<InfoForEdit> call, Response<InfoForEdit> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    Info objEdu = response.body().getInfo();

                    if(objEdu != null)
                        setDataInfo(objEdu);
                    showForm();

                } else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(AddEditInfoActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<InfoForEdit> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });

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

    private void setDataInfo(Info ii) {
        txtInfoName.setText(ii.getInfo_name());
        txtInfoDate.setText(ii.getInfo_date());
        txtInfoText.setText(ii.getInfo_text());

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

    @OnClick(R.id.btnSave)
    void saveInfo(){
        Integer itemID = getIntent().getExtras().getInt("id");
        String pInfoName = txtInfoName.getText().toString();
        String pInfoDate = txtInfoDate.getText().toString();
        String pInfoText = txtInfoText.getText().toString();

        Boolean isValid = true;
        if (!ValidationInput.isValidNOT_EMPTY(pInfoName)) {
            txtInfoName.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtInfoName.setError(null);
        }


        if (!ValidationInput.isValidYear(pInfoDate)) {
            txtInfoDate.setError("الادخال غير صحيح"); isValid=false;
        }else{
            txtInfoDate.setError(null);
        }

        if(!isValid)
            return;


        showLoading();
        if(itemID == 0){
            callMessage = service.storeInfo(pInfoName,pInfoDate,pInfoText,"POST");
        }else{
            callMessage = service.updateInfo(itemID,pInfoName,pInfoDate,pInfoText,"PATCH");
        }


        callMessage.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> callSave, Response<Message> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){

                    String msg = response.body().getMessage();
                    Context context = getApplicationContext();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG)
                            .show();
                    setResult(RESULT_OK, null);
                    finish();



                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(AddEditInfoActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });

    }


}