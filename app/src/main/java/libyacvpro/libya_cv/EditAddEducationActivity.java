package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import com.google.android.material.textfield.TextInputEditText;
import androidx.transition.TransitionManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import libyacvpro.libya_cv.entities.EducationPackage.Education;
import libyacvpro.libya_cv.entities.EducationPackage.EducationForEdit;
import libyacvpro.libya_cv.entities.GeneralPackage.Domain;
import libyacvpro.libya_cv.entities.GeneralPackage.EducationType;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.enums.ValidationInput;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

public class EditAddEducationActivity extends AppCompatActivity {
    private static final String TAG = "EditAddEducationActivity";

    ApiService service;
    TokenManager tokenManager;
    Call<Message> callMessage;
    Call<EducationForEdit> call;

    //Call<Message> callSave;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;


    Button imgWifi;


    TextInputEditText txtUniv;
    TextInputEditText txtFaculty;
    @BindView(R.id.txtSed)
    TextInputEditText txtSed;

    @BindView(R.id.txtAvg)
    TextInputEditText txtAvg;

    TextInputEditText txtStart;
    TextInputEditText txtEnd;

    @BindView(R.id.spDomain)
    Spinner spDomain;

    @BindView(R.id.spEdt)
    Spinner spEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add_education);


        txtEnd = (TextInputEditText) findViewById(R.id.txtEnd);
        txtStart = (TextInputEditText) findViewById(R.id.txtStart);
        txtFaculty = (TextInputEditText) findViewById(R.id.txtFaculty);
        txtUniv = (TextInputEditText) findViewById(R.id.txtUniv);
        imgWifi = (Button) findViewById(R.id.imgWifi);



        loadApi();
    }

    void loadApi(){
        Integer itemID = getIntent().getExtras().getInt("id");


        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken() == null){
            startActivity(new Intent(EditAddEducationActivity.this, EducationActivity.class));
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
            call = service.createEducation();
        }else{
            call = service.getEducation(itemID);
        }

        call.enqueue(new Callback<EducationForEdit>() {
            @Override
            public void onResponse(Call<EducationForEdit> call, Response<EducationForEdit> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){

                    List<Domain> cc = response.body().getDomain();
                    List<EducationType> nn = response.body().getEd_type();
                    Education objEdu = response.body().getEducation();
                    if(objEdu == null){
                        setData(cc,nn,null,null );

                    }else{
                        setData(cc,nn,objEdu.getDomain_name(),objEdu.getEdt_name() );
                        setDataInfo(objEdu);
                    }

                    showForm();

                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(EditAddEducationActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<EducationForEdit> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }

    private void setDataInfo(Education ii){
        txtUniv.setText(ii.getUniv_name());
        txtFaculty.setText(ii.getFaculty_name());
        txtSed.setText(ii.getSed_name());
        txtAvg.setText(ii.getAvg());
        txtStart.setText(ii.getStart_date());
        txtEnd.setText(ii.getEnd_date());
    }
    private void setData(List<Domain> cc, List<EducationType> nn,String myDomain,String myEduType) {


        String[] domainArray = new String[cc.size()];
        for (int i = 0; i < cc.size(); i++)
        {
            domainArray[i] = cc.get(i).getDomain_name();
        }

        String[] edtArray = new String[nn.size()];
        for (int i = 0; i < nn.size(); i++)
        {
            edtArray[i] = nn.get(i).getEdt_name();
        }
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, domainArray);
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view


        spDomain.setAdapter(spinnerArrayAdapter1);




        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, edtArray);
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spEdt.setAdapter(spinnerArrayAdapter2);

        if(myDomain != null && myEduType !=null){
        spDomain.setSelection(getIndex(spDomain, myDomain));
        spEdt.setSelection(getIndex(spEdt, myEduType));
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
        String pUniv = txtUniv.getText().toString();
        String pAvg = txtAvg.getText().toString();
        String pEnd = txtEnd.getText().toString();
        String pStart = txtStart.getText().toString();
        String pFaculty = txtFaculty.getText().toString();
        String pSed = txtSed.getText().toString();
        String pDoamin = spDomain.getSelectedItem().toString();
        String pEdt = spEdt.getSelectedItem().toString();


        Boolean isValid = true;
        if (!ValidationInput.isValidNOT_EMPTY(pUniv)) {
            txtUniv.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtUniv.setError(null);
        }


        if (!ValidationInput.isValidNOT_EMPTY(pFaculty)) {
            txtFaculty.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtFaculty.setError(null);
        }

        if (!ValidationInput.isValidNOT_EMPTY(pStart)) {
            txtStart.setError("الإدخال غير صحيح");
            isValid=false;
        }else{
            txtStart.setError(null);
        }

        if (!ValidationInput.isValidYear(pEnd)) {
            txtEnd.setError("الإدخال غير صحيح");
            isValid=false;
        }else{
            txtEnd.setError(null);
        }

        if(!isValid)
            return;

        showLoading();
        if(itemID == 0){
            callMessage = service.storeEducation(pEdt,pDoamin,pFaculty,pUniv,pSed,pAvg,pStart,pEnd,"POST");
        }else{
            callMessage = service.updateEducation(itemID,pEdt,pDoamin,pFaculty,pUniv,pSed,pAvg,pStart,pEnd,"PATCH");
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
                    startActivity(new Intent(EditAddEducationActivity.this, LoginActivity.class));
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
