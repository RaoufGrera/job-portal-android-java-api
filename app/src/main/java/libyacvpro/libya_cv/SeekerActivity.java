package libyacvpro.libya_cv;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.textfield.TextInputEditText;
import androidx.transition.TransitionManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import libyacvpro.libya_cv.entities.GeneralPackage.EducationType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import libyacvpro.libya_cv.entities.GeneralPackage.City;
import libyacvpro.libya_cv.entities.GeneralPackage.Domain;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.entities.Seeker;
import libyacvpro.libya_cv.entities.Model;
import libyacvpro.libya_cv.enums.ValidationInput;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

public class SeekerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SeekerActivity";


    TextInputEditText txtFname,txtPhone;

    @BindView(R.id.txtAbout)
    TextInputEditText txtAbout;
    @BindView(R.id.txtGoals)
    TextInputEditText txtGoals;

    @BindView(R.id.txtEmail)
    TextInputEditText txtEmail;

    private TextInputEditText txtBirthDay;

    @BindView(R.id.txtAddress)
    TextInputEditText txtAddress;


    @BindView(R.id.spCity)
    Spinner spinner;

    @BindView(R.id.spNat)
    Spinner spnats;

    @BindView(R.id.spDomain)
    Spinner spdomain;

    @BindView(R.id.spSex)
    Spinner spsexs;

    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;

    private DatePickerDialog BirthDayDatePickerDialog;

    private SimpleDateFormat dateFormatter;



    ApiService service;
    TokenManager tokenManager;
    Call<Model> call;
    Call<Message> callMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker);
        getSupportActionBar().setElevation(0);

        txtPhone = (TextInputEditText) findViewById(R.id.txtPhone);
        txtFname = (TextInputEditText) findViewById(R.id.txtFname);


        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken() == null){
            startActivity(new Intent(SeekerActivity.this, LoginActivity.class));
            finish();
        }



        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        showLoading();

        call = service.seekers();
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){

                    List<City> cc = response.body().getData().get(0).getCity();
                    List<EducationType> nn = response.body().getData().get(0).getEdt();
                    List<Domain> dd = response.body().getData().get(0).getDomain();

                    Seeker ii = response.body().getData().get(0).getInfo();
                    setData(cc,nn,ii.getCity_name(),ii.getEdt_name(),ii.getGender(),dd,ii.getDomain_name() );
                    setDataInfo(ii);
                    showForm();


                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(SeekerActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        findViewsById();

        setDateTimeField();
    }

    private void findViewsById() {
        txtBirthDay = (TextInputEditText) findViewById(R.id.txtBirthDay);
        txtBirthDay.setInputType(InputType.TYPE_NULL);
        txtBirthDay.requestFocus();
    }


    private void setDateTimeField() {
        txtBirthDay.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        BirthDayDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtBirthDay.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }



    @Override
    public void onClick(View view) {
        if(view == txtBirthDay) {
            BirthDayDatePickerDialog.show();
        }
    }

    private void setDataInfo(Seeker ii){
        txtEmail.setText(ii.getEmail());

        txtAbout.setText(ii.getAbout());
        String stFn = ii.getFname()+" "+ii.getLname();
        txtFname.setText(stFn);
                txtGoals.setText(ii.getGoal_text());
        txtBirthDay.setText(ii.getBirth_day());
        txtAddress.setText(ii.getAddress());
        txtPhone.setText(ii.getPhone());

    }
     private void setData(List<City> cc, List<EducationType> nn, String myCity, String myNat, String mySex, List<Domain> dd, String myDoamin) {


         String[] spinnerArray = new String[cc.size()];
          for (int i = 0; i < cc.size(); i++)
         {
              spinnerArray[i] = cc.get(i).getCityName();
         }

         String[] natArray = new String[nn.size()];
         for (int i = 0; i < nn.size(); i++)
         {
             natArray[i] = nn.get(i).getEdt_name();
         }

         String[] domainArray = new String[dd.size()];
         for (int i = 0; i < dd.size(); i++)
         {
             domainArray[i] = dd.get(i).getDomain_name();
         }


         if(mySex.equals("m")){mySex ="ذكر";}else {mySex ="أنثي";}
         String gender[] = {"ذكر","أنثي"};

         ArrayAdapter<String> spinnerArrayAdapterSex = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, gender);
         spinnerArrayAdapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
         spsexs.setAdapter(spinnerArrayAdapterSex);
         spsexs.setSelection(getIndex(spsexs, mySex));


         ArrayAdapter<String> spinnerArrayAdapternat = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, natArray);
         spinnerArrayAdapternat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view


         spnats.setAdapter(spinnerArrayAdapternat);

         spnats.setSelection(getIndex(spnats, myNat));


          ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, spinnerArray);
         spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
         spinner.setAdapter(spinnerArrayAdapter);
          spinner.setSelection(getIndex(spinner, myCity));



         ArrayAdapter<String> spinnerArrayAdapterDomain = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, domainArray);
         spinnerArrayAdapterDomain.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
         spdomain.setAdapter(spinnerArrayAdapterDomain);
         spdomain.setSelection(getIndex(spdomain, myDoamin));

         //spinner.setOnItemSelectedListener(new SelectingItem() );

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
                return true;
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
    @OnClick(R.id.btnSave)
    void getSeeker(){

        String fname = txtFname.getText().toString();
        String email =  txtEmail.getText().toString();
        String about = txtAbout.getText().toString();
        String pcity = spinner.getSelectedItem().toString();
        String pedt = spnats.getSelectedItem().toString();
        String pdomain = spdomain.getSelectedItem().toString();
        String psex = spsexs.getSelectedItem().toString();
        String pgoal = txtGoals.getText().toString();
        String paddress = txtAddress.getText().toString();
        String pBirthDay = txtBirthDay.getText().toString();
        String pPhone = txtPhone.getText().toString();

        Boolean isValid = true;
        if (!ValidationInput.isValidNOT_EMPTY(fname)) {
            txtFname.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtFname.setError(null);
        }


        if(!pPhone.equals("")) {
            if (!ValidationInput.isValidPhone(pPhone)) {
                txtPhone.setError("الادخال غير صحيح");
                isValid = false;
            } else {
                txtPhone.setError(null);
            }
        }else{
            //txtPhone.setError(null);
        }
        if(!isValid)
            return;

        showLoading();
        callMessage = service.postInfo(fname,about,pcity,pedt,psex,pgoal,paddress,pBirthDay,pPhone,pdomain,email);
        callMessage.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Log.w(TAG, "onResponse: " + response );


                    if (response.isSuccessful()) {

                        String msg = response.body().getMessage();
                        Context context = getApplicationContext();
                        Toast.makeText(context, msg, Toast.LENGTH_LONG)
                                .show();

                        setResult(RESULT_OK, null);
                        finish();

                    } else {
                        tokenManager.deleteToken();
                        startActivity(new Intent(SeekerActivity.this, LoginActivity.class));
                        finish();

                    }

            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
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
