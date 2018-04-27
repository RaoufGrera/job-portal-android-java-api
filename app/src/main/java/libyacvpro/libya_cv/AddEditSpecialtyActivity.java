package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.entities.SpecialtyPackage.Specialty;
import libyacvpro.libya_cv.entities.SpecialtyPackage.SpecialtyForEdit;
import libyacvpro.libya_cv.enums.ValidationInput;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

public class AddEditSpecialtyActivity extends AppCompatActivity {

    private static final String TAG = "AddEditSpecialtyActivity";

    ApiService service;
    TokenManager tokenManager;
    Call<Message> callMessage;
    Call<SpecialtyForEdit> call;

    //Call<Message> callSave;

    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;


     EditText txtSpecName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_specialty);
        Integer itemID = getIntent().getExtras().getInt("id");
        txtSpecName = (EditText) findViewById(R.id.txtSpecName);


        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(AddEditSpecialtyActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        showLoading();
        if(itemID == 0){
            call = service.createSpecialty();

        }else{
            call = service.getSpecialty(itemID);

        }
        call.enqueue(new Callback<SpecialtyForEdit>() {
            @Override
            public void onResponse(Call<SpecialtyForEdit> call, Response<SpecialtyForEdit> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                     Specialty objEdu = response.body().getSpecialty();


                    if(objEdu !=null)
                        setDataInfo(objEdu);

                    showForm();

                } else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(AddEditSpecialtyActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<SpecialtyForEdit> call, Throwable t) {
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

    private void setDataInfo(Specialty ii){
        txtSpecName.setText(ii.getSpec_name());

    }

    @OnClick(R.id.btnSave)
    void changeData(){
        Integer itemID = getIntent().getExtras().getInt("id");

        String txtSpec = txtSpecName.getText().toString();

        Boolean isValid = true;
        if (!ValidationInput.isValidNOT_EMPTY(txtSpec)) {
            txtSpecName.setError("الحقل مطلوب");
            isValid=false;
        }else{
            txtSpecName.setError(null);

        }

        if(!isValid)
            return;

        if(itemID == 0){
            callMessage = service.storeSpecialty(txtSpec,"POST");
        }else{
            callMessage = service.updateSpecialty(itemID,txtSpec,"PATCH");
        }

        callMessage.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> callSave, Response<Message> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){

                    String msg = response.body().getMessage();
                    Context context = getApplicationContext();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK, null);
                    finish();

                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(AddEditSpecialtyActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });

    }


}
