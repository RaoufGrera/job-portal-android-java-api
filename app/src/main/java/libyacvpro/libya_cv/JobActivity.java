package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.entities.ShowJobPackage.ShowJob;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

import static com.facebook.FacebookSdk.getApplicationContext;

public class JobActivity extends AppCompatActivity {

    private static final String TAG = "JobActivity";

    ApiService service;
    TokenManager tokenManager;
    Call<Message> callMessage;
    Call<ShowJob> call;

    //Call<Message> callSave;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;

    @BindView(R.id.imgCompany)
    ImageView imgCompany;
    @BindView(R.id.btnCompany)
    Button btnCompany;

    @BindView(R.id.lblJobName)
    TextView lblJobName;
    @BindView(R.id.lblCompanyName)
    TextView lblCompanyName;
    @BindView(R.id.lblCity)
    TextView lblCity;
    @BindView(R.id.lblEndDate)
    TextView lblEndDate;

    @BindView(R.id.txtDesc)
    TextView txtDesc;
    @BindView(R.id.txtSkillsJob)
    TextView txtSkillsJob;

    @BindView(R.id.btntojob)
    TextView btnToJob;

String UserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        Integer itemID = getIntent().getExtras().getInt("id");
        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(JobActivity.this, SearchActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        showLoading();


        btnCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowCompanyActivity.class);
                intent.putExtra("user",UserName);

                startActivityForResult(intent, 0);
            }});
            call = service.getShowJob(itemID);


        call.enqueue(new Callback<ShowJob>() {
            @Override
            public void onResponse(Call<ShowJob> call, Response<ShowJob> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    ShowJob objItem = response.body();


                    setDataInfo(objItem);
                    showForm();

                } else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(JobActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<ShowJob> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    @OnClick(R.id.btntojob)
    void postMyJob(){
        Integer itemID = getIntent().getExtras().getInt("id");


        showLoading();

        String st = btnToJob.getText().toString();
       boolean islocal = true;
        if(st.equals("تقدم للوظيفة")){
            callMessage = service.postToJob(itemID,"POST");

            islocal= true;
        }else {
            callMessage = service.deleteToJob(itemID,"POST");
            islocal= false;

        }

        final boolean  istrue= islocal;

        callMessage.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> callSave, Response<Message> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){

                    String msg = response.body().getMessage();
                    Context context = getApplicationContext();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG)
                            .show();


                    changeBtn(istrue);


                    showForm();
                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(JobActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });

    }

    private void setDataInfo(ShowJob ii){

        lblJobName.setText(ii.getJob_name());
        lblCompanyName.setText(ii.getComp_name());
        lblCity.setText(ii.getCity_name());
        lblEndDate.setText(ii.getJob_end());
        txtDesc.setText(ii.getJob_desc());
        txtSkillsJob.setText(ii.getJob_skills());
       // txtCityJob.setText(ii.getCity_name());
         String imgST = "140px_"+ii.getCode_image()+"_"+ii.getImage();

        UserName= ii.getComp_user_name();
        if(ii.getisreq()){
            btnToJob.setText("إلغاء التقدم");
            btnToJob.setBackgroundResource(R.drawable.btndeletestyle);


        }else{
            btnToJob.setText("تقدم للوظيفة");
            btnToJob.setBackgroundResource(R.drawable.btn_info);
         }

       // trustEveryone();
        String url ="https://www.libyacv.com/images/company/";
       /* Picasso.Builder builder = new Picasso.Builder(this);

        builder.downloader(new OkHttpDownloader(this));
        builder.build().load("https://www.libyacv.com/images/company/300px_CggnkmSjo5kvIT9_5.jpg").into(imgCompany);*/
        Picasso.get().load(url+imgST)  .into(imgCompany);

    }
    private void changeBtn(boolean istrue){
        if(istrue){
            btnToJob.setText("إلغاء التقدم");
            btnToJob.setBackgroundResource(R.drawable.btndeletestyle);


        }else{
            btnToJob.setText("تقدم للوظيفة");
            btnToJob.setBackgroundResource(R.drawable.btn_info);
        }
    }
    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }});
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }}}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
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
