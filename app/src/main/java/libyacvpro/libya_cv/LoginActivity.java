package libyacvpro.libya_cv;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.transition.TransitionManager;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import libyacvpro.libya_cv.enums.ValidationInput;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import libyacvpro.libya_cv.entities.AccessToken;
import libyacvpro.libya_cv.entities.ApiError;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

     EditText tilEmail;
     EditText tilPassword;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;

    ApiService service;
    TokenManager tokenManager;
    AwesomeValidation validator;
    Call<AccessToken> call;
    FacebookManager facebookManager;
GoogleApiClient mGoogleApiClient;




    Call<Message> callFireBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        tilEmail = (EditText) findViewById(R.id.til_email);
        tilPassword = (EditText) findViewById(R.id.til_password);
        service = RetrofitBuilder.createService(ApiService.class);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        facebookManager = new FacebookManager(service, tokenManager);
        setupRules();

        CustomDialogPrivacyClass dialogPrivacy = new CustomDialogPrivacyClass(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        Button privacyInButton = (Button) findViewById(R.id.privacy_in_button);
        privacyInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.libyacv.com/privacy_policy"));
                startActivity(new Intent(LoginActivity.this,privacy.class));

            }
        });
        Button signInButton = (Button) findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signIn();
            }
        });

        if(tokenManager.getToken().getAccessToken() != null){
           // getFireBaseToken();
            startActivity(new Intent(LoginActivity.this, MainNavigationActivity.class));
            finish();
        }

        ////  if(!dialogPrivacy.isShowing())
        ////  dialogPrivacy.show();

    }

    int RC_SIGN_IN =10;
    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        showLoading();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        showForm();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else{
            facebookManager.onActivityResult(requestCode, resultCode, data);

        }

    }

    private void handleSignInResult(GoogleSignInResult result) {


            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                String id = account.getId();
                String name = account.getDisplayName();
                String email = account.getEmail();
                String provider = "google";
              getTokenFromBackend(name,email,provider,id);


            }else{
                showForm();
                Toast.makeText(this,"يرجي المحاولة مرة أخرى",Toast.LENGTH_LONG).show();

            }
            // Signed in successfully, show authenticated UI.
           // updateUI(account);

            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.


    }


    private void showLoading(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }

    private void showForm(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_facebook)
    void loginFacebook(){
        showLoading();
        facebookManager.login(this, new FacebookManager.FacebookLoginListener() {
            @Override
            public void onSuccess() {
                facebookManager.clearSession();
                getFireBaseToken();
                startActivity(new Intent(LoginActivity.this, MainNavigationActivity.class));
                finish();
            }

            @Override
            public void onError(String message) {
                showForm();
                Toast.makeText(LoginActivity.this, "يرجي الأتصال بشبكة الأنترنت", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getFireBaseToken(){





        final String[] fcmToken = new String[1];
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        String token = task.getResult();
                        fcmToken[0] =token;

                    }
                });
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        callFireBase = service.postFireBaseToken( fcmToken[0],"POST");
        callFireBase.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Log.w(TAG, "onResponseToken: " + response );

                if(response.isSuccessful()){

                    Toast.makeText(LoginActivity.this, ""+ response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }else {
                    showForm();
                    tokenManager.deleteToken();
                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));
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
    public void onBackPressed()
    {
        // code here to show dialog

        showForm();
        facebookManager.onDestroy();
        CustomDialogClass cdd = new CustomDialogClass(this);
        cdd.show();

    }
    private void getTokenFromBackend(String name, String email, String provider, String providerUserId){
        showLoading();
        call = service.socialAuth(name, email, provider, providerUserId);
        call.enqueue(new Callback<libyacvpro.libya_cv.entities.AccessToken>() {
            @Override
            public void onResponse(Call<libyacvpro.libya_cv.entities.AccessToken> call, Response<libyacvpro.libya_cv.entities.AccessToken> response) {
                if(response.isSuccessful()){



                    tokenManager.saveToken(response.body());
                    getFireBaseToken();
                    startActivity(new Intent(LoginActivity.this, MainNavigationActivity.class));
                    finish();
                }else{
                    showForm();
                    Toast.makeText(LoginActivity.this, "يرجي المحاولة مرة أخري", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<libyacvpro.libya_cv.entities.AccessToken> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
                showForm();

            }
        });

    }

    @OnClick(R.id.btn_login)
    void login() {

        String email = tilEmail.getText().toString();
        String password = tilPassword.getText().toString();


        Boolean isValid = true;
        if (!ValidationInput.isValidNOT_EMPTY(email)) {
            tilEmail.setError("الادخال غير صحيح.");
            isValid=false;
        }else{
            tilEmail.setError(null);
        }


        if (!ValidationInput.isValidNOT_EMPTY(password)) {
            tilPassword.setError("الادخال غير صحيح."); isValid=false;
        }else{
            tilPassword.setError(null);
        }

        if(!isValid)
            return;

            showLoading();
            call = service.login(email, password);
            call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                    Log.w(TAG, "onResponse: " + response);

                    if (response.isSuccessful()) {
                        tokenManager.saveToken(response.body());
                        getFireBaseToken();
                        startActivity(new Intent(LoginActivity.this, MainNavigationActivity.class));
                        finish();
                    } else {
                        if (response.code() == 422) {
                            handleErrors(response.errorBody());
                        }
                        if (response.code() == 401) {
                            ApiError apiError = Utils.converErrors(response.errorBody());
                            Toast.makeText(LoginActivity.this, "يرجي الأتصال بشبكة الأنترنت", Toast.LENGTH_LONG).show();
                        }
                        showForm();
                    }

                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());
                    showForm();
                }
            });

        }

/*
    @OnClick(R.id.go_to_register)
    void goToRegister(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
*/
    private void handleErrors(ResponseBody response) {

        ApiError apiError = Utils.converErrors(response);

        for (Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()) {
            if (error.getKey().equals("username")) {
                tilEmail.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("password")) {
                tilPassword.setError(error.getValue().get(0));
            }
        }

    }

    public void setupRules() {

        validator.addValidation(this, R.id.til_email, RegexTemplate.NOT_EMPTY, R.string.err_name);
        validator.addValidation(this, R.id.til_password, RegexTemplate.NOT_EMPTY, R.string.err_password);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.cancel();
            showForm();
            call = null;
        }
        showForm();
        facebookManager.onDestroy();
    }
}
