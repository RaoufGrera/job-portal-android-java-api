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
import libyacvpro.libya_cv.entities.EducationPackage.Education;
import libyacvpro.libya_cv.entities.EducationPackage.EducationForEdit;
import libyacvpro.libya_cv.entities.GeneralPackage.City;
import libyacvpro.libya_cv.entities.GeneralPackage.Domain;
import libyacvpro.libya_cv.entities.GeneralPackage.EducationType;
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


    Button imgWifi;
    String pUser;

    EditText txtDesc;
    EditText txtJobName;
    EditText txtSkills;
    Spinner spDomain;
    Spinner spCity;
    Integer itemID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        txtJobName = (EditText) findViewById(R.id.txtJobName);
        txtDesc = (EditText) findViewById(R.id.txtDesc);
        txtSkills = (EditText) findViewById(R.id.txtSkills);
        spDomain = (Spinner) findViewById(R.id.spDomain);
        spCity = (Spinner) findViewById(R.id.spCity);
         imgWifi = (Button) findViewById(R.id.imgWifi);
        pUser = getIntent().getExtras().getString("user");

          itemID = getIntent().getExtras().getInt("id");



        loadApi();
    }

    void loadApi(){

        ButterKnife.bind(this);
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

        showLoading();
        if(itemID == 0){
            call = service.getAddJob(pUser);
        }else{
            call = service.getEditJob(pUser,itemID);
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

                    showForm();

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
        txtSkills.setText(ii.getJob_skills());


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
        String pSkills = txtSkills.getText().toString();

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

        if (!ValidationInput.isValidNOT_EMPTY(pSkills)) {
            txtSkills.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtSkills.setError(null);
        }


        if(!isValid)
            return;

        showLoading();
        if(itemID == 0){
            callMessage = service.storeAddJob(pUser,pDoamin,pCity,pJobName,pDesc,pSkills,"POST");
        }else{
            callMessage = service.updateAddJob(pUser,itemID,pDoamin,pCity,pJobName,pDesc,pSkills,"PATCH");
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
