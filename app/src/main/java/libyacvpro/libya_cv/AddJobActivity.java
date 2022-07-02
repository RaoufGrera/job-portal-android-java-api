package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import com.google.android.material.textfield.TextInputLayout;
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
import libyacvpro.libya_cv.entities.GeneralPackage.City;
import libyacvpro.libya_cv.entities.GeneralPackage.Domain;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.entities.ShowJobPackage.ShowJob;
import libyacvpro.libya_cv.entities.ShowJobPackage.ShowParaJob;
import libyacvpro.libya_cv.enums.ValidationInput;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddJobActivity extends AppCompatActivity {
    private static final String TAG = "AddJobActivity";

    ApiService service;
    TokenManager tokenManager;
    Call<Message> callMessage;
    Call<ShowParaJob> call;

    //Call<Message> callSave;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;

    TextView txtEmail,txtWebsite,txtPhone;
    TextInputLayout lblEmail,lblWebsite,lblPhone;


     Button imgWifi;
    String pUser;

    EditText txtDesc;
    EditText txtJobName;
     Spinner spDomain;
    Spinner spCity;
    private static final String APP_ID = "ca-app-pub-9929016091047307~2213947061";
    private AdView mAdView;
    Integer itemID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtWebsite = (EditText) findViewById(R.id.txtWebsite);
        txtPhone = (EditText) findViewById(R.id.txtPhone);


      //  lblEmail = (TextInputLayout) findViewById(R.id.lblEmail);
      //  lblWebsite = (TextInputLayout) findViewById(R.id.lblWebsite);
       // lblPhone = (TextInputLayout) findViewById(R.id.lblPhone);

       // checkbox_meat = (CheckBox) findViewById(R.id.checkbox_meat);



        txtJobName = (EditText) findViewById(R.id.txtJobName);
        txtJobName.requestFocus();

        txtDesc = (EditText) findViewById(R.id.txtDesc);
         spDomain = (Spinner) findViewById(R.id.spDomain);
        spCity = (Spinner) findViewById(R.id.spCity);
         imgWifi = (Button) findViewById(R.id.imgWifi);
        pUser = getIntent().getExtras().getString("user");

          itemID = getIntent().getExtras().getInt("id");

     //   MobileAds.initialize(this, APP_ID);
        ButterKnife.bind(this);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-9929016091047307/3960713000");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    protected void onResume() {

        loadApi();

        super.onResume();
    }
   /* public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) findViewById(R.id.checkbox_meat)).isChecked();

        if (checked){
            lblEmail.setVisibility(View.INVISIBLE);
            lblPhone.setVisibility(View.INVISIBLE);
            lblWebsite.setVisibility(View.INVISIBLE);
        }else{
            lblEmail.setVisibility(View.VISIBLE);
            lblPhone.setVisibility(View.VISIBLE);
            lblWebsite.setVisibility(View.VISIBLE);
        }
    }*/

    void loadApi(){

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken() == null){
            startActivity(new Intent(AddJobActivity.this, MyJobActivity.class));
            finish();
        }

        boolean IsValid =  isOnline();
        if(!IsValid){
            showWifi();
            return;
        }


        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

     //   showLoading();
        if(itemID == 0){
            call = service.getAddJob();
        }else{
            call = service.getEditJob(itemID);
        }

        call.enqueue(new Callback<ShowParaJob>() {
            @Override
            public void onResponse(Call<ShowParaJob> call, Response<ShowParaJob> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){

                    List<Domain> cc = response.body().getDomain();
                    List<City> nn = response.body().getCity();
                    ShowJob objJob = response.body().getJob();
                    if(objJob == null){
                        setData(cc,nn,null,null );

                    }else{
                        setData(cc,nn,objJob.getDomain_name(),objJob.getCity_name() );
                        setDataInfo(objJob);
                    }

                  //  showForm();

                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(AddJobActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<ShowParaJob> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }

    private void setDataInfo(ShowJob ii){
        txtJobName.setText(ii.getJob_name());
        txtDesc.setText(ii.getJob_desc());
       /*  if(ii.getHow_receive().equals(0)) {
            checkbox_meat.setChecked(true);
            lblEmail.setVisibility(View.INVISIBLE);
            lblPhone.setVisibility(View.INVISIBLE);isChecked
        //    lblWebsite.setVisibility(View.INVISIBLE);
        }else{*/
       //     lblEmail.setVisibility(View.VISIBLE);
         //   lblPhone.setVisibility(View.VISIBLE);
        //    lblWebsite.setVisibility(View.VISIBLE);

          //  checkbox_meat.setChecked(false);
            txtEmail.setText(ii.getEmail());
            txtPhone.setText(ii.getPhone());
            txtWebsite.setText(ii.getWebsite());
        //}



    }
    private void setData(List<Domain> cc, List<City> nn,String myDomain,String myCity) {


        String[] domainArray = new String[cc.size()];
        for (int i = 0; i < cc.size(); i++)
        {
            domainArray[i] = cc.get(i).getDomain_name();
        }

        String[] cityArray = new String[nn.size()];
        for (int i = 0; i < nn.size(); i++)
        {
            cityArray[i] = nn.get(i).getCityName();
        }
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, domainArray);
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view


        spDomain.setAdapter(spinnerArrayAdapter1);




        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, cityArray);
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spCity.setAdapter(spinnerArrayAdapter2);

        if(myDomain != null && myCity !=null){
            spDomain.setSelection(getIndex(spDomain, myDomain));
            spCity.setSelection(getIndex(spCity, myCity));
        }
        //spinner.setOnItemSelectedListener(new SelectingItem() );

    }
    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
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


        Integer itemID = getIntent().getExtras().getInt("id");
        String pJobName = txtJobName.getText().toString();
        String pDesc = txtDesc.getText().toString();
        String pSkills ="";

        String pEamil = txtEmail.getText().toString();
        String pPhone = txtPhone.getText().toString();
        String pWebsite = txtWebsite.getText().toString();
        Integer pHowRec = 1;
        //if(checkbox_meat.isChecked()){
         //pHowRec = 0;
         /*}
       /* else{
         pHowRec = 1;}*/

        String pDoamin = spDomain.getSelectedItem().toString();
        String pCity = spCity.getSelectedItem().toString();


        Boolean isValid = true;
        if (!ValidationInput.isValidNOT_EMPTY(pJobName)) {
            txtJobName.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtJobName.setError(null);
        }


        if (!ValidationInput.isValidNOT_EMPTY(pDesc)) {
            txtDesc.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtDesc.setError(null);
        }
        Boolean isOk = false;

            if(!pEamil.equals("")) {
                if (!ValidationInput.isValidEmail(pEamil)) {
                    txtEmail.setError("البريد الإلكتروني غير صحيح");
                    isValid = false;
                } else {
                    txtEmail.setError(null);
                    isOk = true;
                }
            }

            if(!pPhone.equals("")) {
                if (!ValidationInput.isValidPhone(pPhone)) {
                    txtPhone.setError("رقم الهاتف غير صحيح");
                    isValid = false;
                } else {
                    txtPhone.setError(null);
                    isOk = true;
                }
            }
            if(!pWebsite.equals("")) {
                if (!ValidationInput.isValidWebSite(pWebsite)) {
                    txtWebsite.setError("موقع الويب غير صحيح");
                    isValid = false;
                } else {
                    txtWebsite.setError(null);
                    isOk = true;
                }
            }



        if(!isValid)
            return;

        if(!isOk)
            return;
        showLoading();
        if(itemID == 0){
            callMessage = service.storeAddJob(pDoamin,pCity,pJobName,pDesc,pSkills,pEamil,pPhone,pWebsite,pHowRec,"POST");
        }else{
            callMessage = service.updateAddJob(itemID,pDoamin,pCity,pJobName,pDesc,pSkills,pEamil,pPhone,pWebsite,pHowRec,"PATCH");
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
                    startActivity(new Intent(AddJobActivity.this, LoginActivity.class));
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
