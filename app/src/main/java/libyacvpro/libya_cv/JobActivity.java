package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.transition.TransitionManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.content.res.AppCompatResources;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.entities.ShowJobPackage.ShowJob;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static com.facebook.FacebookSdk.getApplicationContext;

public class JobActivity extends AppCompatActivity {

    private static final String TAG = "JobActivity";
    private FirebaseAnalytics analytics;
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


    @BindView(R.id.lblJobName)
    TextView lblJobName;
    @BindView(R.id.lblDomain)
    TextView lblDomain;
    @BindView(R.id.lblCity)
    TextView lblCity;
    @BindView(R.id.lblEndDate)
    TextView lblEndDate;

    /*@BindView(R.id.lblJob)
    TextView lblJob;
*/
    @BindView(R.id.vJob)
    View vJob;

    @BindView(R.id.vweb)
    View vweb;
    @BindView(R.id.vphone)
    View vphone;
    @BindView(R.id.btnEmail)
    TextView btnEmail;
    @BindView(R.id.llJob)
    LinearLayout llJob;

    @BindView(R.id.btnWebsite)
    Button btnWebsite;

   /* @BindView(R.id.btnTrans)
    Button btnTrans;*/

    @BindView(R.id.btnPhone)
    Button btnPhone;

    @BindView(R.id.txtDesc)
    TextView txtDesc;


    @BindView(R.id.lblSeeIT)
    TextView lblSeeIT;
    @BindView(R.id.lblCompName)
    TextView lblCompName;

    @BindView(R.id.btntojob)
    TextView btnToJob;

    String webUrl="";
    String DescString="";

    String stPhone="";
    String JobName="";
    String JobDesc="";
    String stEmail="";
    String stComp="";
    Integer itemID=null;
    String UserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

         itemID = getIntent().getExtras().getInt("id");
        ButterKnife.bind(this);

        Drawable leftDrawable = AppCompatResources.getDrawable(this, R.drawable.ic_favorite_black_14dp);
        lblSeeIT.setCompoundDrawablesWithIntrinsicBounds(null, null, leftDrawable, null);


        Drawable emailDrawable = AppCompatResources.getDrawable(this, R.drawable.ic_email_black_24dp);
        btnEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, emailDrawable, null);

        Drawable webDrawable = AppCompatResources.getDrawable(this, R.drawable.ic_website);
        btnWebsite.setCompoundDrawablesWithIntrinsicBounds(null, null, webDrawable, null);


        Drawable phoneDrawable = AppCompatResources.getDrawable(this, R.drawable.ic_phone_iphone_black_24dp);
        btnPhone.setCompoundDrawablesWithIntrinsicBounds(null, null, phoneDrawable, null);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(JobActivity.this, SearchActivity.class));
            finish();
        }



        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        showLoading();

    /*    btnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translate(DescString,"ar");
            }});
*/

        btnWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                String wwwUrl =  webUrl ;
                websiteIntent.setData(Uri.parse(wwwUrl));
                startActivity(websiteIntent);
            }});

        lblCompName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(JobActivity.this, ShowCompanyActivity.class);
                intent.putExtra("user", stComp);
                startActivity(intent);

            }});



     /*   btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent testIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" +  "&body=" + "&to=" + stEmail);
                testIntent.setData(data);
                startActivity(testIntent);
            }});*/

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = stPhone;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }});


            call = service.getShowJob(itemID);


        call.enqueue(new Callback<ShowJob>() {
            @Override
            public void onResponse(Call<ShowJob> call, Response<ShowJob> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    ShowJob objItem = response.body();


                    if(objItem.getJob_name() == null) {
                         String msg = "الوظيفة لم تعد متاحة";
                        Context context = getApplicationContext();
                        Toast.makeText(context, msg, Toast.LENGTH_LONG)
                                .show();
                        startActivity(new Intent(JobActivity.this, MainNavigationActivity.class));
                        finish();
                        return;
                    }
                    DescString = objItem.getJob_desc();
                    JobDesc = DescString;


                    txtDesc.setText(JobDesc);

                    if(JobDesc.length() > 16)
                        JobDesc = JobDesc.substring(0,15);

                    boolean valid =  isProbablyArabic(JobDesc);


                    if(!valid)
                        txtDesc.setGravity(Gravity.LEFT);


                    setDataInfo(objItem);
                    showForm();

                        // If all other cases we should just load the full-size image now




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

    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length();) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }
 /*   private void translate(String textToTranslate, String targetLanguage) { //, TranslateCallback callback
        try {


            //txtDesc.setText("hello");
         /*    TranslateOptions options = TranslateOptions.newBuilder()
                    .setApiKey()
						.build();
*/
          /* TranslateOptions options =
                    TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyCxQf45p5mk_v22xzcJ0tvWClmFS08ETPA")
                            .setCredentials(NoCredentials.getInstance())
                            .build();
            Translate trService = options.getService();
            Translation translation = trService.translate(textToTranslate, Translate.TranslateOption.targetLanguage("ar"));

            JobDesc = translation.getTranslatedText();
            txtDesc.setText("222");*/
           // callback.onSuccess(translation.getTranslatedText());

      /*    final   String textToTranslate2 = txtDesc.getText().toString();

            final Handler textViewHandler = new Handler();
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... voids) {
                    TranslateOptions options = TranslateOptions.newBuilder().setApiKey("AIzaSyCxQf45p5mk_v22xzcJ0tvWClmFS08ETPA").build();
                    final Translate translate = options.getService();
                    final Translation translation = translate.translate(textToTranslate2, Translate.TranslateOption.targetLanguage("ar"));
                    textViewHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            txtDesc.setText(Html.fromHtml(translation.getTranslatedText()));
                        }
                    });
                    return null;
                }
            }.execute();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }*/

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.side_bar_share,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.toolback:
                onBackPressed();

                return true;

            case R.id.toolshare:
                Intent intent = new Intent();
                String msg = "للإطلاع على الوظيفة: " + buildDynamicLink();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                intent.setType("text/plain");
                startActivity(intent);

                return true;

            default:
                return true;
        }
    }



    private String buildDynamicLink(/*String link, String description, String titleSocial, String source*/) {


        return "https://www.libyacv.com/job/"+ itemID;
    }


    /* void postMyJob(){
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

    }*/

    private void setDataInfo(ShowJob ii){

        lblJobName.setText(ii.getJob_name());



        JobName = ii.getJob_name();
        lblDomain.setText(ii.getDomain_name());
        lblCity.setText(ii.getCity_name());
        lblEndDate.setText(ii.getJob_start());
        lblCompName.setText(ii.getComp_name());
        stComp = ii.getComp_user_name();



        JobDesc = ii.getJob_desc();
         lblSeeIT.setText(ii.getSee_it().toString());
       // txtCityJob.setText(ii.gebtnEmailtCity_name());
      //   String imgST = "140px_"+ii.getCode_image()+"_"+ii.getImage();

        String imgST =  ii.getImage();
        UserName= ii.getComp_user_name();
        if(ii.getisreq()){
            btnToJob.setText("إلغاء التقدم");
            btnToJob.setBackgroundResource(R.drawable.btndeletestyle);


        }else{
            btnToJob.setText("تقدم للوظيفة");
            btnToJob.setBackgroundResource(R.drawable.btn_info);
         }


         if(ii.getHow_receive().equals(1)){
             btnToJob.setVisibility(View.GONE);
            // lblJob.setVisibility(View.VISIBLE);
             llJob.setVisibility(View.VISIBLE);
             if(!ii.getEmail().equals("")){
                 btnEmail.setVisibility(View.VISIBLE);
                 vJob.setVisibility(View.VISIBLE);

                 stEmail = ii.getEmail();
                 btnEmail.setText(stEmail);
             }
             if(!ii.getWebsite().equals("")){
                 btnWebsite.setVisibility(View.VISIBLE);
                 webUrl = ii.getWebsite();
                 vweb.setVisibility(View.VISIBLE);
                 String g =webUrl;
                 if(webUrl.length() > 28) {
                     g = webUrl.substring(0, 28);
                     g=g+"...";
                 }
                 btnWebsite.setText(g);
             }
             if(!ii.getPhone().equals("")){
                 if(!ii.getPhone().equals(null)) {
                     btnPhone.setVisibility(View.VISIBLE);
                     stPhone = ii.getPhone();
                     if (!stPhone.substring(0, 1).equals("0"))
                         stPhone = "0" + ii.getPhone();

                     btnPhone.setText(stPhone);
                     vphone.setVisibility(View.VISIBLE);


                 }
             }

         }
       // trustEveryone();
        /* Picasso.Builder builder = new Picasso.Builder(this);

        builder.downloader(new OkHttpDownloader(this));
        builder.build().load("https://www.libyacv.com/images/company/300px_CggnkmSjo5kvIT9_5.jpg").into(imgCompany);*/
        Picasso.get().load(imgST)  .into(imgCompany);

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
       // TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
    }

    private void showLoading(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }

}
