package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.transition.TransitionManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
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

public class ShowServicesActivity extends AppCompatActivity {
    private static final String TAG = "ShowServicesActivity";

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




    @BindView(R.id.lblEmail)
    TextView lblEmail;

    @BindView(R.id.lblPhone)
    TextView lblPhone;

    @BindView(R.id.txtServices)
    TextView txtServices;

    int itemID;


    @BindView(R.id.lblSeeIT)
    TextView lblSeeIT;



    String webUrl="";
    String faceUrl="";
    String userID="";

    String lat="";
    String lng="";
    String stPhone="";
    String stEmail="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_services);

        ButterKnife.bind(this);
        itemID = getIntent().getExtras().getInt("id");






     /*   btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent testIntent = new Intent(Intent.ACTION_VIEW);
                testIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                 Uri data = Uri.parse("mailto:?subject=" + "" + "&body=" + "" + "&to=" + stEmail);
                testIntent.setData(data);
                startActivity(testIntent);
            }});*/

        lblComptName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), CVActivity.class);
                intent.putExtra("seeker_id", userID);


                startActivity(intent);
            }});

       /* btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = stPhone;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }});*/
        showLoading();

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(  ShowServicesActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        // showLoading();


        call = service.getShowServices(itemID);


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
                    startActivity(new Intent(ShowServicesActivity.this, LoginActivity.class));
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



    private void setDataInfo(Company ii){

        lblSeeIT.setText(ii.getSee_it());
        userID = ii.getComp_id();
        lblCompanyName.setText(ii.getComp_name());
         lblComptName.setText(ii.getCompt_name());
        String Address ="";
        if(ii.getAddress() != null){
            if(!ii.getAddress().equals("")){
                Address=" - "+ii.getAddress();
            }
        }
        String aa= ii.getCity_name()+Address;
        lblCity.setText(aa);
        lblDomainName.setText(ii.getDomain_name());
         txtServices.setText(ii.getServices());
    /*    if (ii.getFacebook() != null ) {
            if (!ii.getFacebook().equals("")) {
                btnFacebook.setVisibility(View.VISIBLE);
                faceUrl = ii.getFacebook();


            }
        }
        if (ii.getUrl() != null ) {
            if(ii.getUrl().equals("")){
            webUrl =ii.getUrl();
            btnFacebook.setVisibility(View.VISIBLE);}

        }*/



        if (ii.getPhone() != null ) {

            if (!ii.getPhone().equals("")) {
             //  btnPhone.setVisibility(View.VISIBLE);
                stPhone = ii.getPhone();
                lblPhone.setText(stPhone);
            }
        }

        if (ii.getEmail() != null ) {

            if (!ii.getEmail().equals("")) {
             //   btnEmail.setVisibility(View.VISIBLE);
                lblEmail.setText( ii.getEmail());
             }
        }


        String imgST =  ii.getImage();




        // trustEveryone();
        //String url ="https://www.libyacv.com/images/company/";
       /* Picasso.Builder builder = new Picasso.Builder(this);

        builder.downloader(new OkHttpDownloader(this));
        builder.build().load("https://www.libyacv.com/images/company/300px_CggnkmSjo5kvIT9_5.jpg").into(imgCompany);*/
        Picasso.get().load(imgST).placeholder(R.drawable.pro).into(imgCompany);

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
