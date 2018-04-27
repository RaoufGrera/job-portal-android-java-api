package libyacvpro.libya_cv;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import libyacvpro.libya_cv.entities.HobbyPackage.Hobby;
import libyacvpro.libya_cv.entities.HobbyPackage.HobbyForEdit;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.enums.ValidationInput;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;


public class AddEditHobbyActivity extends AppCompatActivity {
    private static final String TAG = "AddEditHobbyActivity";

    ApiService service;
    TokenManager tokenManager;
    Call<Message> callMessage;
    Call<HobbyForEdit> call;

    //Call<Message> callSave;

    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;

    Button imgWifi;

     EditText txtHobby_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_hobby);
        txtHobby_name = (EditText) findViewById(R.id.txtHobbyName);
        imgWifi = (Button) findViewById(R.id.imgWifi);


        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(AddEditHobbyActivity.this, LoginActivity.class));
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
            call = service.createHobby();
        }else{
            call = service.getHobby(itemID);
        }


        call.enqueue(new Callback<HobbyForEdit>() {
            @Override
            public void onResponse(Call<HobbyForEdit> call, Response<HobbyForEdit> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    Hobby objEdu = response.body().getHobby();

                    if(objEdu != null)
                        setDataInfo(objEdu);

                    showForm();

                } else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(AddEditHobbyActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<HobbyForEdit> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private void setDataInfo(Hobby ii){
        txtHobby_name.setText(ii.getHobby_name());

    }

    @OnClick(R.id.btnSave)
    void saveHobby(){
        Integer itemID = getIntent().getExtras().getInt("id");
        String pHobbyName = txtHobby_name.getText().toString();

        Boolean isValid = true;
        if (!ValidationInput.isValidNOT_EMPTY(pHobbyName)) {
            txtHobby_name.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtHobby_name.setError(null);

        }

        if(!isValid)
            return;

        showLoading();
        if(itemID == 0){
            callMessage = service.storeHobby(pHobbyName,"POST");
        }else{
            callMessage = service.updateHobby(itemID,pHobbyName,"PATCH");
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
                    startActivity(new Intent(AddEditHobbyActivity.this, LoginActivity.class));
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