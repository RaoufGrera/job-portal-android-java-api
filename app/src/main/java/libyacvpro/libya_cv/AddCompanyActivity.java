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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import libyacvpro.libya_cv.entities.CompanyPackage.CompanyForEdit;
import libyacvpro.libya_cv.entities.GeneralPackage.City;
import libyacvpro.libya_cv.entities.GeneralPackage.Domain;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.enums.ValidationInput;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCompanyActivity extends AppCompatActivity {
    private static final String TAG = "AddCompanyActivity";

    ApiService service;
    TokenManager tokenManager;
    Call<Message> callMessage;
    Call<CompanyForEdit> call;

    //Call<Message> callSave;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;


    Button imgWifi;


    EditText txtUrl;
    EditText txtAddress;

    TextView txtCompName;

    EditText txtCompanyUserName;

     Spinner spDomain;

    Spinner spCity;
    //Spinner spType;
    private static final String APP_ID = "ca-app-pub-9929016091047307~2213947061";
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);
        txtUrl = (EditText) findViewById(R.id.txtUrl);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtCompanyUserName = (EditText) findViewById(R.id.txtCompanyUserName);
        txtCompName = (EditText) findViewById(R.id.txtCompName);
        spDomain = (Spinner) findViewById(R.id.spDomain);
        spCity = (Spinner) findViewById(R.id.spCity);
       // spType = (Spinner) findViewById(R.id.spType);
        imgWifi = (Button) findViewById(R.id.imgWifi);

       // MobileAds.initialize(this, APP_ID);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-9929016091047307/3960713000");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        loadApi();
    }

        void loadApi(){


            ButterKnife.bind(this);
            tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

            if(tokenManager.getToken() == null){
                startActivity(new Intent(AddCompanyActivity.this, MainNavigationActivity.class));
                finish();
            }

            boolean IsValid =  isOnline();
            if(!IsValid){
                showWifi();
                return;
            }


            service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

            showLoading();

                call = service.createCompany();


            call.enqueue(new Callback<CompanyForEdit>() {
                @Override
                public void onResponse(Call<CompanyForEdit> call, Response<CompanyForEdit> response) {
                    Log.w(TAG, "onResponse: " + response );

                    if(response.isSuccessful()){

                        List<Domain> cc = response.body().getDomain();
                        List<City> nn = response.body().getCity();
                       // List<TypeCompanyEntities> tt = response.body().getType();
                       // Education objEdu = response.body().get();
                         setData(cc,nn );



                        showForm();

                    }else {
                        tokenManager.deleteToken();
                        startActivity(new Intent(AddCompanyActivity.this, LoginActivity.class));
                        finish();

                    }
                }

                @Override
                public void onFailure(Call<CompanyForEdit> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage() );
                }
            });
        }



    private void setData(List<Domain> cc, List<City> nn) {


        String[] domainArray = new String[cc.size()];
        for (int i = 0; i < cc.size(); i++)
        {
            domainArray[i] = cc.get(i).getDomain_name();
        }

        String[] CityArray = new String[nn.size()];
        for (int i = 0; i < nn.size(); i++)
        {
            CityArray[i] = nn.get(i).getCityName();
        }

       /* String[] TypeArray = new String[tt.size()];
        for (int i = 0; i < tt.size(); i++)
        {
            TypeArray[i] = tt.get(i).getCompt_name();
        }
*/
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, domainArray);
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spDomain.setAdapter(spinnerArrayAdapter1);




        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, CityArray);
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spCity.setAdapter(spinnerArrayAdapter2);


       /* ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, TypeArray);
        spinnerArrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spType.setAdapter(spinnerArrayAdapter3);*/
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

    @OnClick(R.id.btnSave)
    void changeData(){


         String pCompName = txtCompName.getText().toString();
        String pUser = txtCompanyUserName.getText().toString();
        String pAddress = txtAddress.getText().toString();
        String pUrl = txtUrl.getText().toString();

        String pDoamin = spDomain.getSelectedItem().toString();
        String pCity = spCity.getSelectedItem().toString();
       // String pType = spType.getSelectedItem().toString();


        Boolean isValid = true;
        if (!ValidationInput.isValidNOT_EMPTY(pCompName)) {
            txtCompName.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtCompName.setError(null);
        }


        if (!ValidationInput.isValidNOT_EMPTY(pUser)) {
            txtCompanyUserName.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtCompanyUserName.setError(null);
        }

        if (!ValidationInput.isValidNOT_EMPTY(pAddress)) {
            txtAddress.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtAddress.setError(null);
        }

        if(!isValid)
            return;

        showLoading();

            callMessage = service.storeCompany(pCompName,pUser,pAddress,pUrl,pCity,pDoamin,"1");



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
                    startActivity(new Intent(AddCompanyActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
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
    private void showForm(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
        imgWifi.setVisibility(View.GONE);

    }

    private void showLoading(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.GONE);
        imgWifi.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

    }
}
