package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.transition.TransitionManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import libyacvpro.libya_cv.entities.CompanyPackage.Company;
import libyacvpro.libya_cv.entities.Message;
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



    @BindView(R.id.lblCompanyName)
    TextView lblCompanyName;

    TextView lblDomain,lblCity,lblEmail,lblPhone,lblWeb,lblFace,lblDesc,lblName,lblSeeIT;


    @BindView(R.id.txtServices)
    TextView txtServices;


    String itemID;


  /*  @BindView(R.id.lblSeeIT)
    TextView lblSeeIT;*/

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
        lblDomain =   findViewById(R.id.lblDomain);
        lblCity =   findViewById(R.id.lblCity);
        lblPhone =   findViewById(R.id.lblPhone);
        lblEmail =   findViewById(R.id.lblEmail);
        lblWeb =   findViewById(R.id.lblWeb);
        lblFace =   findViewById(R.id.lblFace);
        lblDesc =   findViewById(R.id.lblDesc);
        lblName =   findViewById(R.id.lblName);
        lblSeeIT =   findViewById(R.id.lblSeeIT);

        lblFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // PackageManager packageManager = getApplicationContext().getPackageManager();
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(getApplicationContext(),faceUrl);
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }});

        lblWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                String wwwUrl =  webUrl ;
                websiteIntent.setData(Uri.parse(wwwUrl));
                startActivity(websiteIntent);
            }});

     /*   btnMap.setOnClickListener(new View.OnClickListener() {
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
                Uri data = Uri.parse("mailto:?subject=" + "" + "&body=" + "" + "&to=" + stEmail);
                testIntent.setData(data);
                startActivity(testIntent);
            }});

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = stPhone;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }});*/
        showLoading();

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(  ShowCompanyActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        // showLoading();


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

    public String getFacebookPageURL(Context context , String FACEBOOK_URL) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katanurla", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL; //"https://www.facebook.com/"+
            } else { //older versions of fb app
                return "fb://page/" + "LibyaCV";
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
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


    /*@OnClick(R.id.btntojob)
    void postMyJob(){
        String itemID = getIntent().getExtras().getString("user");


       //showLoading();

        String st = btnToJob.getText().toString();
        boolean islocal = true;
        if(st.equals("متابعة")){
            callMessage = service.addFollow(itemID);

            islocal= true;
        }else {
            callMessage = service.removeFollow(itemID);
            islocal= false;

        }

       // final boolean  istrue= islocal;

        callMessage.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> callSave, Response<Message> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){

                    String msg = response.body().getMessage();
                    Context context = getApplicationContext();
                    Toast.makeText(context, msg, Toast.LENGTH_LONG)
                            .show();


                 //   changeBtn(istrue);


                //    showForm();
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

    }*/
    private void setDataInfo(Company ii){

       // lblSeeIT.setText(ii.getSee_it());
        lblCompanyName.setText(ii.getComp_name());
        //lblComptName.setText(ii.getCompt_name());
        String Address ="";
        if(ii.getAddress() != null){
            if(!ii.getAddress().equals("")){
                Address=" - "+ii.getAddress();
            }
        }
        String aa= ii.getCity_name()+Address;
        lblCity.setText(aa);
        lblDomain.setText(ii.getDomain_name());
       // txtDesc.setText(ii.getComp_desc());
        txtServices.setText(ii.getServices());
String JobDesc =ii.getServices();
        if (JobDesc != null ) {
            if (!JobDesc.equals("")) {
                if (JobDesc.length() > 16)
                    JobDesc = JobDesc.substring(0, 15);

                boolean valid = isProbablyArabic(JobDesc);


                if (!valid)
                    txtServices.setGravity(Gravity.LEFT);
            }
        }

        if (ii.getFacebook() != null ) {
            if (!ii.getFacebook().equals("")) {
                lblFace.setVisibility(View.VISIBLE);
                faceUrl = ii.getFacebook();
                lblFace.setText(faceUrl);


            }
        }
        if (ii.getUrl() != null ) {
            if(!ii.getUrl().equals("")){
            webUrl =ii.getUrl();
            lblWeb.setVisibility(View.VISIBLE);
                lblWeb.setText(webUrl);
            }

        }


      /*  if (ii.getLat() != null ) {

            if (!ii.getLat().equals("") && !ii.getLng().equals("")) {
                btnMap.setVisibility(View.VISIBLE);

                lat = ii.getLat();
                lng = ii.getLng();

            }
        }*/
        if (ii.getPhone() != null ) {

            if (!ii.getPhone().equals("")) {
                lblPhone.setVisibility(View.VISIBLE);
                stPhone = ii.getPhone();
                lblPhone.setText(stPhone);
            }
        }

        if (ii.getEmail() != null ) {

            if (!ii.getEmail().equals("")) {
                lblEmail.setVisibility(View.VISIBLE);
                stEmail = ii.getEmail();
                lblEmail.setText(stEmail);
             }
        }


        String imgST =  ii.getImage();

  /*      if(ii.getisreq()){
            btnToJob.setText("إلغاء المتابعة");
            btnToJob.setBackgroundResource(R.drawable.btndeletestyle);


        }else{
            btnToJob.setText("متابعة");
            btnToJob.setBackgroundResource(R.drawable.btn_info);
        }
*/


        // trustEveryone();
        //String url ="https://www.libyacv.com/images/company/";
       /* Picasso.Builder builder = new Picasso.Builder(this);

        builder.downloader(new OkHttpDownloader(this));
        builder.build().load("https://www.libyacv.com/images/company/300px_CggnkmSjo5kvIT9_5.jpg").into(imgCompany);*/
        Picasso.get().load(imgST).placeholder(R.drawable.pro).into(imgCompany);

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
   /* private void changeBtn(boolean istrue){
        if(istrue){
            btnToJob.setText("إلغاء المتابعة");
            btnToJob.setBackgroundResource(R.drawable.btndeletestyle);


        }else{
            btnToJob.setText("متابعة");
            btnToJob.setBackgroundResource(R.drawable.btn_info);
        }
    }*/

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
