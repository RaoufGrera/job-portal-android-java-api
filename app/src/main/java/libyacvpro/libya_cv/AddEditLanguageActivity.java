package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
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
import libyacvpro.libya_cv.entities.GeneralPackage.GeneralLang;
import libyacvpro.libya_cv.entities.GeneralPackage.Level;
import libyacvpro.libya_cv.entities.LangPackage.Language;
import libyacvpro.libya_cv.entities.LangPackage.LanguageForEdit;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

public class AddEditLanguageActivity extends AppCompatActivity {
    private static final String TAG = "AddEditLanguageActivity";

    ApiService service;
    TokenManager tokenManager;
    Call<Message> callMessage;
    Call<LanguageForEdit> call;

    //Call<Message> callSave;



    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;
    Button imgWifi;


    @BindView(R.id.spLang)
    Spinner spLang;

    @BindView(R.id.spLevel)
    Spinner spLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_language);

        imgWifi = (Button) findViewById(R.id.imgWifi);
        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken() == null){
            startActivity(new Intent(AddEditLanguageActivity.this, ExperienceActivity.class));
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
        loader.setVisibility(View.GONE);

        imgWifi.setVisibility(View.VISIBLE);
    }
    public boolean isOnline() {

            try {
                ConnectivityManager cm =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                return cm.getActiveNetworkInfo() != null &&
                        cm.getActiveNetworkInfo().isConnectedOrConnecting();


            } catch (Exception e) {
                return false;
            }

    }



    void loadApi() {

        boolean IsValid = isOnline();
        if (!IsValid) {
            showWifi();
            return;
        }
        Integer itemID = getIntent().getExtras().getInt("id");

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);


        if(itemID == 0){
            call = service.createLanguage();
        }else{
            call = service.getLanguage(itemID);
        }
        showLoading();

        call.enqueue(new Callback<LanguageForEdit>() {
            @Override
            public void onResponse(Call<LanguageForEdit> call, Response<LanguageForEdit> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){

                    List<GeneralLang> lstLangType = response.body().getLang_type();
                    List<Level> lstLevel = response.body().getLevel();
                    Language objLevel = response.body().getLanguage();
                    if(objLevel == null){
                        setData(lstLangType,lstLevel,null,null );
                    }else{
                        setData(lstLangType,lstLevel,objLevel.getLang_name(),objLevel.getLevel_name() );

                    }

                    showForm();
                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(AddEditLanguageActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<LanguageForEdit> call, Throwable t) {
                showWifi();
             //   Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });

    }
    private void showForm(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.VISIBLE);
        imgWifi.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
    }
    private void setData(List<GeneralLang> lstLangType,List<Level> lstLevel,String myLang,String myLevel) {


        String[] ItemArray = new String[lstLangType.size()];
        for (int i = 0; i < lstLangType.size(); i++)
        {
            ItemArray[i] = lstLangType.get(i).getLang_name();
        }

        String[] ItemArray2 = new String[lstLevel.size()];
        for (int i = 0; i < lstLevel.size(); i++)
        {
            ItemArray2[i] = lstLevel.get(i).getLevel_name();
        }


        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, ItemArray);
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spLang.setAdapter(spinnerArrayAdapter1);



        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, ItemArray2);
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spLevel.setAdapter(spinnerArrayAdapter2);

        if(myLang != null && myLevel != null ) {
            spLang.setSelection(getIndex(spLang, myLang));
            spLevel.setSelection(getIndex(spLevel, myLevel));
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

    private void showLoading(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.GONE);
        imgWifi.setVisibility(View.GONE);

        loader.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnSave)
    void getLang(){
        Integer itemID = getIntent().getExtras().getInt("id");

        String pLang = spLang.getSelectedItem().toString();
        String pLevel = spLevel.getSelectedItem().toString();

        if(itemID == 0){
            callMessage = service.storeLanguage(pLang,pLevel,"POST");
        }else{
            callMessage = service.updateLanguage(itemID,pLang,pLevel,"PATCH");
        }

        showLoading();
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
                    startActivity(new Intent(AddEditLanguageActivity.this, LoginActivity.class));
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
}
