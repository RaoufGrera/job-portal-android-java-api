package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import libyacvpro.libya_cv.entities.CompanyPackage.Company;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.entities.ShowJobPackage.ShowJob;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowCompanyActivity extends AppCompatActivity {
    private static final String TAG = "ShowCompanyActivity";

    ApiService service;
    TokenManager tokenManager;
    Call<Message> callMessage;
    Call<Company> call;

    //Call<Message> callSave;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;

    @BindView(R.id.imgCompany)
    ImageView imgCompany;


    @BindView(R.id.lblComptName)
    TextView lblComptName;
    @BindView(R.id.lblCompanyName)
    TextView lblCompanyName;
    @BindView(R.id.lblCity)
    TextView lblCity;
    @BindView(R.id.lblDomainName)
    TextView lblDomainName;

    @BindView(R.id.btnFacebook)
    ImageView btnFacebook;
    @BindView(R.id.btnWebsite)
    ImageView btnWebsite;
    @BindView(R.id.btnMap)
    ImageView btnMap;
    @BindView(R.id.btnEmail)
    ImageView btnEmail;

    @BindView(R.id.btnPhone)
    Button btnPhone;

    @BindView(R.id.txtServices)
    TextView txtServices;
    @BindView(R.id.txtDesc)
    TextView txtDesc;
    String itemID;
    @BindView(R.id.btntojob)
    TextView btnToJob;

    String webUrl="";
    String faceUrl="";

    String lat="";
    String lng="";
    String stPhone="";
    String stEmail="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_company);

        ButterKnife.bind(this);
        itemID = getIntent().getExtras().getString("user");


        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // PackageManager packageManager = getApplicationContext().getPackageManager();
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(getApplicationContext(),faceUrl);
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }});

        btnWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                String wwwUrl =  webUrl ;
                websiteIntent.setData(Uri.parse(wwwUrl));
                startActivity(websiteIntent);
            }});

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ShowCompanyMapActivity.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng", lng);
                startActivityForResult(intent, 0);
            }});

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent testIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + "موضوع الرسالة" + "&body=" + "محتوي الرسالة" + "&to=" + stEmail);
                testIntent.setData(data);
                startActivity(testIntent);
            }});

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = stPhone;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }});

        apiLoad();

    }

    public String getFacebookPageURL(Context context , String FACEBOOK_URL) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" +"https://www.facebook.com/"+ FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + "libyacv";
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    void apiLoad(){


        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(  ShowCompanyActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        showLoading();


        call = service.getShowCompany(itemID);


        call.enqueue(new Callback<Company>() {
            @Override
            public void onResponse(Call<Company> call, Response<Company> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    Company objItem = response.body();


                    setDataInfo(objItem);
                    showForm();

                } else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(ShowCompanyActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
    @OnClick(R.id.btntojob)
    void postMyJob(){
        String itemID = getIntent().getExtras().getString("user");


        showLoading();

        String st = btnToJob.getText().toString();
        boolean islocal = true;
        if(st.equals("متابعة")){
            callMessage = service.addFollow(itemID);

            islocal= true;
        }else {
            callMessage = service.removeFollow(itemID);
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
                    startActivity(new Intent(ShowCompanyActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });

    }
    private void setDataInfo(Company ii){

        lblCompanyName.setText(ii.getComp_name());
        lblComptName.setText(ii.getCompt_name());
        String Address ="";
        if(ii.getAddress() == null){
            if(!ii.getAddress().equals("")){
                Address=" - "+ii.getAddress();
            }
        }
        String aa= ii.getCity_name()+Address;
        lblCity.setText(aa);
        lblDomainName.setText(ii.getDomain_name());
        txtDesc.setText(ii.getComp_desc());
        txtServices.setText(ii.getServices());
        if (ii.getFacebook() != null ) {
            if (!ii.getFacebook().equals("")) {
                btnFacebook.setVisibility(View.VISIBLE);
                faceUrl = ii.getFacebook();


            }
        }
        if (ii.getUrl() != null ) {
            if(ii.getUrl().equals("")){
            webUrl =ii.getUrl();
            btnFacebook.setVisibility(View.VISIBLE);}

        }


        if (ii.getLat() != null ) {

            if (!ii.getLat().equals("") && !ii.getLng().equals("")) {
                btnMap.setVisibility(View.VISIBLE);

                lat = ii.getLat();
                lng = ii.getLng();

            }
        }
        if (ii.getPhone() != null ) {

            if (!ii.getPhone().equals("")) {
                btnPhone.setVisibility(View.VISIBLE);
                stPhone = ii.getPhone();
                btnPhone.setText(stPhone);
            }
        }

        if (ii.getEmail() != null ) {

            if (!ii.getEmail().equals("")) {
                btnEmail.setVisibility(View.VISIBLE);
                stEmail = ii.getEmail();
             }
        }


        String imgST =  ii.getImage();

        if(ii.getisreq()){
            btnToJob.setText("إلغاء المتابعة");
            btnToJob.setBackgroundResource(R.drawable.btndeletestyle);


        }else{
            btnToJob.setText("متابعة");
            btnToJob.setBackgroundResource(R.drawable.btn_info);
        }



        // trustEveryone();
        //String url ="https://www.libyacv.com/images/company/";
       /* Picasso.Builder builder = new Picasso.Builder(this);

        builder.downloader(new OkHttpDownloader(this));
        builder.build().load("https://www.libyacv.com/images/company/300px_CggnkmSjo5kvIT9_5.jpg").into(imgCompany);*/
        Picasso.get().load(imgST)  .into(imgCompany);

    }
    private void changeBtn(boolean istrue){
        if(istrue){
            btnToJob.setText("إلغاء المتابعة");
            btnToJob.setBackgroundResource(R.drawable.btndeletestyle);


        }else{
            btnToJob.setText("متابعة");
            btnToJob.setBackgroundResource(R.drawable.btn_info);
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
