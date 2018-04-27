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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import libyacvpro.libya_cv.entities.CompanyPackage.Company;
import libyacvpro.libya_cv.entities.CompanyPackage.CompanyForEdit;
import libyacvpro.libya_cv.entities.EducationPackage.Education;
import libyacvpro.libya_cv.entities.GeneralPackage.City;
import libyacvpro.libya_cv.entities.GeneralPackage.Domain;
import libyacvpro.libya_cv.entities.GeneralPackage.TypeCompanyEntities;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.enums.ValidationInput;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCompanyActivity extends AppCompatActivity {
    private static final String TAG = "EditCompanyActivity";


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
    TextView contact_form_title;

    TextView txtCompanyUserName;
    EditText txtCompDesc;
    EditText txtFacebook;
  //  EditText txtTwitter;
 //   EditText txtLinkedin;
    EditText txtServices;
    EditText txtPhone;
    EditText txtEmail;

    Spinner spDomain;
    String pUser;
    Spinner spCity;
    Spinner spType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company);
        txtUrl = (EditText) findViewById(R.id.txtUrl);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
       txtCompName = (TextView) findViewById(R.id.txtCompName);
        txtCompanyUserName = (TextView) findViewById(R.id.txtCompanyUserName);
        contact_form_title = (TextView) findViewById(R.id.contact_form_title);
        txtCompDesc = (EditText) findViewById(R.id.txtCompDesc);
      //  txtLinkedin = (EditText) findViewById(R.id.txtLinkedin);
        txtFacebook = (EditText) findViewById(R.id.txtFacebook);
      //  txtTwitter = (EditText) findViewById(R.id.txtTwitter);
        txtServices = (EditText) findViewById(R.id.txtServices);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        spDomain = (Spinner) findViewById(R.id.spDomain);
        spCity = (Spinner) findViewById(R.id.spCity);
        spType = (Spinner) findViewById(R.id.spType);
        imgWifi = (Button) findViewById(R.id.imgWifi);
        pUser = getIntent().getExtras().getString("user");



        loadApi();
    }

    void loadApi(){


        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken() == null){
            startActivity(new Intent(EditCompanyActivity.this, MainNavigationActivity.class));
            finish();
        }

        boolean IsValid =  isOnline();
        if(!IsValid){
            showWifi();
            return;
        }


        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        showLoading();

        call = service.getCompanyInfo(pUser);


        call.enqueue(new Callback<CompanyForEdit>() {
            @Override
            public void onResponse(Call<CompanyForEdit> call, Response<CompanyForEdit> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){

                    List<Domain> cc = response.body().getDomain();
                    List<City> nn = response.body().getCity();
                    List<TypeCompanyEntities> tt = response.body().getType();
                    Company objCompany = response.body().getCompany();




                        setData(cc,nn,tt,objCompany.getDomain_name(),objCompany.getCity_name(),objCompany.getCompt_name() );
                        setDataInfo(objCompany);




                    showForm();

                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(EditCompanyActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<CompanyForEdit> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }

    private void setDataInfo(Company ii){
        txtAddress.setText(ii.getAddress());
        txtCompName.setText(ii.getComp_name());
         txtCompanyUserName.setText(ii.getComp_user_name()) ;
        txtCompDesc.setText(ii.getComp_desc());
        txtFacebook.setText(ii.getFacebook());
     //   txtTwitter.setText(ii.getTwitter());
      //  txtLinkedin.setText(ii.getLinkedin());
        txtEmail.setText(ii.getEmail());
        txtPhone.setText(ii.getPhone());
        txtServices.setText(ii.getServices());
        txtUrl.setText(ii.getUrl());
        contact_form_title.setText(ii.getComp_name());
    }

    private void setData(List<Domain> cc, List<City> nn,List<TypeCompanyEntities> tt,String stDomain,String stCity,String stType) {


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

        String[] TypeArray = new String[tt.size()];
        for (int i = 0; i < tt.size(); i++)
        {
            TypeArray[i] = tt.get(i).getCompt_name();
        }

        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, domainArray);
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spDomain.setAdapter(spinnerArrayAdapter1);




        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, CityArray);
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spCity.setAdapter(spinnerArrayAdapter2);


        ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, TypeArray);
        spinnerArrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spType.setAdapter(spinnerArrayAdapter3);
        if(stDomain != null && stCity !=null && stType!=null){
            spDomain.setSelection(getIndex(spDomain, stDomain));
            spCity.setSelection(getIndex(spCity, stCity));
            spType.setSelection(getIndex(spType, stType));
        }

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


          String pAddress = txtAddress.getText().toString();
        String pUrl = txtUrl.getText().toString();
        String pCompDesc = txtCompDesc.getText().toString();
        String pFacebook = txtFacebook.getText().toString();
        String pTwitter = "";
        String pLinkedin = "";
        String pEmail = txtEmail.getText().toString();
        String pPhone = txtPhone.getText().toString();
        String pServices = txtServices.getText().toString();

        String pDoamin = spDomain.getSelectedItem().toString();
        String pCity = spCity.getSelectedItem().toString();
        String pType = spType.getSelectedItem().toString();


        Boolean isValid = true;


        if (!ValidationInput.isValidNOT_EMPTY(pAddress)) {
            txtAddress.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtAddress.setError(null);
        }

        if(!isValid)
            return;

        showLoading();
         pUser = getIntent().getExtras().getString("user");

        callMessage = service.storeCompanyInfo(pUser,pEmail,pPhone,pUrl,pAddress,pCity,pDoamin,pType,pCompDesc,pServices,pFacebook,pTwitter,pLinkedin);



        callMessage.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
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
                    startActivity(new Intent(EditCompanyActivity.this, LoginActivity.class));
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
