package libyacvpro.libya_cv;

import android.app.DatePickerDialog;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import libyacvpro.libya_cv.entities.ExperiencePackage.Experience;
import libyacvpro.libya_cv.entities.ExperiencePackage.ExperienceForEdit;
import libyacvpro.libya_cv.entities.GeneralPackage.Domain;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.enums.ValidationInput;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.DatePickerDialog.OnDateSetListener;
import android.view.View.OnClickListener;
import android.text.InputType;


public class AddEditExperienceActivity extends AppCompatActivity implements  OnClickListener {
    private static final String TAG = "AddEditExperienceActivity";

    ApiService service;
    TokenManager tokenManager;
    Call<Message> callMessage;
    Call<ExperienceForEdit> call;

    //Call<Message> callSave;
    private EditText fromDateEtxt;
    private EditText toDateEtxt;

    @BindView(R.id.checkbox_meat)
   CheckBox checkbox_meat;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;



    @BindView(R.id.txtGoals)
    TextView txtGoals;

    EditText txtCompName;
    EditText txtJobName;
     EditText txtStart;
     EditText txtEnd;

    @BindView(R.id.spDomain)
    Spinner spDomain;

    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;

    Button imgWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_experience);


        txtCompName = (EditText) findViewById(R.id.txtCompName);
        txtJobName = (EditText) findViewById(R.id.txtJobName);
        txtStart = (EditText) findViewById(R.id.txtStart);
        txtEnd = (EditText) findViewById(R.id.txtEnd);
        imgWifi = (Button) findViewById(R.id.imgWifi);



        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken() == null){
            startActivity(new Intent(AddEditExperienceActivity.this, LoginActivity.class));
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
        if(itemID == 0){
            call = service.createExperience();
        }else{
            call = service.getExperience(itemID);
        }


        call.enqueue(new Callback<ExperienceForEdit>() {
            @Override
            public void onResponse(Call<ExperienceForEdit> call, Response<ExperienceForEdit> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){

                    List<Domain> cc = response.body().getDomain();
                    Experience objEdu = response.body().getExperience();
                    if(objEdu ==null){
                        setData(cc,null );

                    }else{
                        setData(cc,objEdu.getDomain_name() );
                        setDataInfo(objEdu);
                    }
                    showForm();

                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(AddEditExperienceActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<ExperienceForEdit> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        findViewsById();

        setDateTimeField();
    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.txtStart);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (EditText) findViewById(R.id.txtEnd);
        toDateEtxt.setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }



    @Override
    public void onClick(View view) {
        if(view == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(view == toDateEtxt) {
            toDatePickerDialog.show();
        }
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) findViewById(R.id.checkbox_meat)).isChecked();

                if (checked){
                    toDateEtxt.setVisibility(View.INVISIBLE);
                }else{
                    toDateEtxt.setVisibility(View.VISIBLE);
                }
    }
    private void setDataInfo(Experience ii){
        txtCompName.setText(ii.getCompe_name());
        txtJobName.setText(ii.getExp_name());
         txtGoals.setText(ii.getExp_desc());
        txtStart.setText(ii.getStart_date());
        final  String  stDate = ii.getStart_date();
        txtEnd.setText(ii.getEnd_date());
        if(ii.getState().equals("0")) {
            checkbox_meat.setChecked(false);
            toDateEtxt.setVisibility(View.VISIBLE);

        }
        else{
            toDateEtxt.setVisibility(View.INVISIBLE);
            checkbox_meat.setChecked(true);
        }

    }
    private void setData(List<Domain> cc,String myDomain) {


        String[] domainArray = new String[cc.size()];
        for (int i = 0; i < cc.size(); i++)
        {
            domainArray[i] = cc.get(i).getDomain_name();
        }
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, domainArray);
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spDomain.setAdapter(spinnerArrayAdapter1);

        if(myDomain != null){
            spDomain.setSelection(getIndex(spDomain, myDomain));
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





    @OnClick(R.id.btnSave)
    void getSeeker(){
        Integer itemID = getIntent().getExtras().getInt("id");
        String pComp = txtCompName.getText().toString();
        String pExp = txtJobName.getText().toString();
        String pEnd = txtEnd.getText().toString();
        String pStart = txtStart.getText().toString();
        String pGoals = txtGoals.getText().toString();
         String pDoamin = spDomain.getSelectedItem().toString();
        String pState ="0";

        Boolean isValid = true;
        if (!ValidationInput.isValidNOT_EMPTY(pComp)) {
            txtCompName.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtCompName.setError(null);
        }

        if (!ValidationInput.isValidNOT_EMPTY(pExp)) {
            txtJobName.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtJobName.setError(null);
        }

        if (!ValidationInput.isValidNOT_EMPTY(pStart)) {
            txtStart.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtStart.setError(null);
        }

        if(checkbox_meat.isChecked()) {
            pState = "1";
            txtEnd.setError(null);
        }else{
            if (!ValidationInput.isValidNOT_EMPTY(pEnd)) {
                txtEnd.setError("الحقل مطلوب");
                isValid=false;
            }else{
                txtEnd.setError(null);
            }
        }

        if(!isValid)
            return;

        showLoading();
        if(itemID == 0){
            callMessage = service.storeExperience(pComp,pExp,pGoals,pDoamin,pStart,pEnd,pState,"POST");
        }else{
            callMessage = service.updateExp(itemID,pComp,pExp,pGoals,pDoamin,pStart,pEnd,pState,"PATCH");
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
                    startActivity(new Intent(AddEditExperienceActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });

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
}

