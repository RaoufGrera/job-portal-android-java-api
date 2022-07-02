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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import libyacvpro.libya_cv.entities.GeneralPackage.Level;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.entities.SkillsPackage.Skills;
import libyacvpro.libya_cv.entities.SkillsPackage.SkillsForEdit;
import libyacvpro.libya_cv.enums.ValidationInput;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

public class AddEditSkillsActivity extends AppCompatActivity {

    private static final String TAG = "AddEditSkillsActivity";

    ApiService service;
    TokenManager tokenManager;
    Call<Message> callMessage;
    Call<SkillsForEdit> call;

    //Call<Message> callSave;

    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;
    AwesomeValidation validator;

    private static final String APP_ID = "ca-app-pub-9929016091047307~2213947061";
    private AdView mAdView;
     private EditText txtSkills_name;


    @BindView(R.id.spLevel)
    Spinner spLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_skills);
        Integer itemID = getIntent().getExtras().getInt("id");


        txtSkills_name = (EditText) findViewById(R.id.txtSkillsName);


        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

       // MobileAds.initialize(this, APP_ID);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-9929016091047307/3960713000");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if (tokenManager.getToken() == null) {
            startActivity(new Intent(AddEditSkillsActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        if(itemID == 0){
            call = service.createSkills();

        }else{
            call = service.getSkills(itemID);

        }
        showLoading();
        call.enqueue(new Callback<SkillsForEdit>() {
            @Override
            public void onResponse(Call<SkillsForEdit> call, Response<SkillsForEdit> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    List<Level> lstLevel = response.body().getLevel();
                    Skills objEdu = response.body().getSkills();

                    if(objEdu == null){
                        setData(lstLevel,null );
                    }else{
                        setData(lstLevel,objEdu.getLevel_name() );
                        setDataInfo(objEdu);

                    }

                    showForm();


                } else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(AddEditSkillsActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<SkillsForEdit> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }



    private void setDataInfo(Skills ii){
        txtSkills_name.setText(ii.getSkills_name());

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
    private void setData(List<Level> cc,String myLevel) {


        String[] ItemArray = new String[cc.size()];
        for (int i = 0; i < cc.size(); i++)
        {
            ItemArray[i] = cc.get(i).getLevel_name();
        }


        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, ItemArray);
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view


        spLevel.setAdapter(spinnerArrayAdapter1);

        if(myLevel != null)
            spLevel.setSelection(getIndex(spLevel, myLevel));



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
    void changeData(){
        Integer itemID = getIntent().getExtras().getInt("id");

        String txtSkills = txtSkills_name.getText().toString();
        String pLevel = spLevel.getSelectedItem().toString();

         Boolean isValid = true;
        if (!ValidationInput.isValidNOT_EMPTY(txtSkills)) {
            txtSkills_name.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtSkills_name.setError(null);

        }

        if(!isValid)
            return;

        if(itemID == 0){
            callMessage = service.storeSkills(txtSkills,pLevel,"POST");
        }else{
            callMessage = service.updateSkills(itemID,txtSkills,pLevel,"PATCH");
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
                        startActivity(new Intent(AddEditSkillsActivity.this, LoginActivity.class));
                        finish();

                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
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

}

