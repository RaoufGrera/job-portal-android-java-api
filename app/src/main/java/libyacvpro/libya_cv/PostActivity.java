package libyacvpro.libya_cv;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
//import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import libyacvpro.libya_cv.entities.PostResponse;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "PostActivity";
    private AdView mAdView;



    ApiService service;
    TokenManager tokenManager;
    Call<PostResponse> call;
   // private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
       /* MobileAds.initialize(this, "ca-app-pub-9929016091047307~2213947061");



        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9929016091047307/9404728293");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken() == null){
            startActivity(new Intent(PostActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
    }


    @OnClick(R.id.btn_movepage)
    void movePage(){
        startActivity(new Intent(PostActivity.this, SeekerActivity.class));
    }

    @OnClick(R.id.btn_Education)
    void toedu(){


   // Log.d(TAG,"InstanceID token " + FirebaseInstanceId.getInstance().getToken());

        //startActivity(new Intent(PostActivity.this, EducationActivity.class));
    }
/*
    @OnClick(R.id.btn_Exp)
    void toExp(){
        startActivity(new Intent(PostActivity.this, ExperienceActivity.class));
    }
    */
    @OnClick(R.id.btn_lang)
    void toLang(){
        startActivity(new Intent(PostActivity.this, LanguageActivity.class));
    }

    @OnClick(R.id.btnHobby)
    void toHobby(){startActivity(new Intent(PostActivity.this, HobbyActivity.class));}

    @OnClick(R.id.btnTrain)
    void toTrain(){
        startActivity(new Intent(PostActivity.this, TrainingActivity.class));
    }
    @OnClick(R.id.btnInfo)
    void toInfo(){
        startActivity(new Intent(PostActivity.this, InfoActivity.class));
    }

    @OnClick(R.id.btnSpec)
    void toSpec(){
        startActivity(new Intent(PostActivity.this, SpecialtyActivity.class));
    }

    @OnClick(R.id.btnCert)
    void toCert(){
      //  startActivity(new Intent(PostActivity.this, CertificateActivity.class));
    }
    @OnClick(R.id.btnPDF)
    void toPDF(){
        startActivity(new Intent(PostActivity.this, SeekerPDFActivity.class));
    }
    @OnClick(R.id.btnRefresh)
    void toRefrewsh(){
        startActivity(new Intent(PostActivity.this, RefreshActivity.class));
    }
/*
    @OnClick(R.id.btnmovejob)
    void toMovePagesd(){
        startActivity(new Intent(PostActivity.this, JobsActivity.class));
    }

    @OnClick(R.id.btnSkills)
    void toSkills(){
        startActivity(new Intent(PostActivity.this, SkillsActivity.class));
    }
    @OnClick(R.id.btnLogout)
    void toLogout(){
        //tokenManager.deleteToken();
        startActivity(new Intent(PostActivity.this, MainNavigationActivity.class));
        //finish();
    }
*/

/*
    @OnClick(R.id.btnSkiflls2)
    void toMain(){
        startActivity(new Intent(PostActivity.this, MainTabActivity.class));
    }
*/





    @OnClick(R.id.btn_Exp)
    void toLogout(){
        tokenManager.deleteToken();
        startActivity(new Intent(PostActivity.this, LoginActivity.class));
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null){
            call.cancel();
            call = null;
        }
    }
}
