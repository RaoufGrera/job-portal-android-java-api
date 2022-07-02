package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.transition.Fade;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import libyacvpro.libya_cv.adapter.EducationAdapter;
import libyacvpro.libya_cv.adapter.ExpAdapter;
import libyacvpro.libya_cv.entities.CertificatePackage.Certificate;
import libyacvpro.libya_cv.entities.EducationPackage.Education;
import libyacvpro.libya_cv.entities.ExperiencePackage.Experience;
import libyacvpro.libya_cv.entities.HobbyPackage.Hobby;
import libyacvpro.libya_cv.entities.InfoPackage.Info;
import libyacvpro.libya_cv.entities.LangPackage.Language;
import libyacvpro.libya_cv.entities.Seeker;
import libyacvpro.libya_cv.entities.SeekerCvPackage.ShowCvResponse;
import libyacvpro.libya_cv.entities.SkillsPackage.Skills;
import libyacvpro.libya_cv.entities.SpecialtyPackage.Specialty;
import libyacvpro.libya_cv.entities.TrainingPackage.Training;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EdtiCVActivity extends AppCompatActivity {
    private static final String TAG = "EdtiCVActivity";

    NonScrollListView LISTVIEW,LISTVIEWEXP,LISTVIEWLANG,LISTVIEWSPEC,LISTVIEWSKILLS,LISTVIEWCERT,LISTVIEWTRA,LISTVIEWHOBBY,LISTVIEWINFO;
    EducationAdapter ListAdapter ;
    ExpAdapter ListAdapterSExp ;
    TextView txtInfo,txtEdu,txtExp,txtSpec,txtSkills,txtHobby,txtTra,txtCert,txtLang,lblName,lblMatch,lblCity,lblPhone,lblEmail,lblDomain;
    View InfoCard, ExpCard, SpecCard, SkillsCard, HobbyCard, TraCard, CertCard, LangCard;

    View EduCard;
    ArrayList<String> first= new ArrayList<String>();
    ArrayList<String> second= new ArrayList<String>();
    ArrayList<String> third= new ArrayList<String>();
    ArrayList<String> forth= new ArrayList<String>() ;
    String  a="";
    String  f="";


    @BindView(R.id.container)
    RelativeLayout container;


    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;

    ImageView tvimgView;

    ApiService service;
    TokenManager tokenManager;
    Call<ShowCvResponse> call;
    Button imgWifi;
    String pUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edti_cv);

        lblName = (TextView) findViewById(R.id.lblName);
        lblMatch = (TextView) findViewById(R.id.lblMatch);
        lblPhone =   findViewById(R.id.lblPhone);
        lblEmail =   findViewById(R.id.lblEmail);
        lblDomain = (TextView) findViewById(R.id.lblDomain);
        lblCity = (TextView) findViewById(R.id.lblCity);
        container = (RelativeLayout) findViewById(R.id.container);
        formContainer = (LinearLayout) findViewById(R.id.form_container);
        loader = (ProgressBar) findViewById(R.id.loader);
        imgWifi = (Button) findViewById(R.id.imgWifi);



        pUser = getIntent().getExtras().getString("seeker_id");
        LISTVIEW = (NonScrollListView) findViewById(R.id.eduItem);
        LISTVIEWEXP = (NonScrollListView) findViewById(R.id.expItem);
        LISTVIEWLANG = (NonScrollListView) findViewById(R.id.langItem);
        LISTVIEWSPEC = (NonScrollListView) findViewById(R.id.specItem);
        LISTVIEWSKILLS = (NonScrollListView) findViewById(R.id.skillsItem);
        LISTVIEWCERT = (NonScrollListView) findViewById(R.id.certItem);
        LISTVIEWTRA = (NonScrollListView) findViewById(R.id.traItem);
        LISTVIEWHOBBY = (NonScrollListView) findViewById(R.id.hobbyItem);
        LISTVIEWINFO = (NonScrollListView) findViewById(R.id.infoItem);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtEdu = (TextView) findViewById(R.id.txtEdu);

        txtExp = (TextView) findViewById(R.id.txtExp);
        txtSpec = (TextView) findViewById(R.id.txtSpec);
        txtSkills = (TextView) findViewById(R.id.txtSkills);
        txtHobby = (TextView) findViewById(R.id.txtHobby);
        txtTra = (TextView) findViewById(R.id.txtTra);
        txtCert = (TextView) findViewById(R.id.txtCert);
        txtLang = (TextView) findViewById(R.id.txtLang);


        tvimgView    = (ImageView)  findViewById(R.id.imgCompany);



        InfoCard = (View) findViewById(R.id.infoCard);
        EduCard = (View) findViewById(R.id.eduCard);
        ExpCard = (View) findViewById(R.id.expCard);
        SpecCard = (View) findViewById(R.id.specCard);
        SkillsCard = (View) findViewById(R.id.skillsCard);
        HobbyCard = (View) findViewById(R.id.hobbyCard);
        TraCard = (View) findViewById(R.id.traCard);
        CertCard = (View) findViewById(R.id.certCard);
        LangCard = (View) findViewById(R.id.langCard);
        apiLoad();

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
    private void apiLoad(){
        boolean IsValid =  isOnline();
        if(!IsValid){
            showWifi();
            return;
        }

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        if(tokenManager.getToken() == null){
            startActivity(new Intent(EdtiCVActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        call = service.getShowSeekerCv(pUser);
        call.enqueue(new Callback<ShowCvResponse>() {
            @Override
            public void onResponse(Call<ShowCvResponse> call, Response<ShowCvResponse> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){
                  //  showForm();

                    ShowCvResponse  objSeeker = response.body();
                    setData(objSeeker);

                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(EdtiCVActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<ShowCvResponse> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );

            }
        });



    }




    @OnClick(R.id.imgWifi)
    void refreshActivity(){
        apiLoad();

    }
    private void showWifi(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.GONE);
        imgWifi.setVisibility(View.VISIBLE);
    }

    private void showForm(){
        Transition transition = new Fade();
        transition.setDuration(1000);
        TransitionManager.beginDelayedTransition(container, transition);
        formContainer.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
    }

    private void showLoading(){
        Transition transition = new Fade();
        transition.setDuration(1000);
        TransitionManager.beginDelayedTransition(container, transition);
        formContainer.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }
    public boolean isOnline() {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null &&
                    cm.getActiveNetworkInfo().isConnectedOrConnecting();


        } catch (Exception e) { return false; }
    }
    void clearArray(){
        first.clear();
        second.clear();
        third.clear();
        forth.clear();
    }

    private void setData(ShowCvResponse objSeeker){

        clearArray();


        Seeker objJobSeeker = objSeeker.getData().getJob_seeker();

        lblName.setText(objJobSeeker.getFname()+" "+ objJobSeeker.getLname());
        lblDomain.setText(objJobSeeker.getDomain_name());
        lblCity.setText(objJobSeeker.getCity_name());
        lblPhone.setText(objJobSeeker.getPhone());
        lblEmail.setText(objJobSeeker.getEmail());
        Picasso.get().load(objJobSeeker.getImage()).placeholder( R.drawable.pro).into(tvimgView);




        List<Education> lstEdu = objSeeker.getData().getSeeker_ed();
        if( lstEdu.size() > 0) {

            for (int i = 0; i < lstEdu.size(); i++)
            {

                String Sed_name="";
                String avg="";
                String a = objSeeker.getData().getSeeker_ed().get(i).getEdt_name();
                String stEdt =  a.replace("\\","\\\\");
                first.add(lstEdu.get(i).getUniv_name() + " ، " + lstEdu.get(i).getFaculty_name());
                if(lstEdu.get(i).getSed_name() !="") {
                    Sed_name= " ، " + lstEdu.get(i).getSed_name();
                }

                if(lstEdu.get(i).getAvg() !="" && lstEdu.get(i).getAvg() !=null ) {
                    avg= " ، "+ lstEdu.get(i).getAvg()+"%" ;
                }
                second.add(a + Sed_name +avg);
                third.add( lstEdu.get(i).getStart_date() + "   -  "+ lstEdu.get(i).getEnd_date());
                forth.add("");


            }

            ListAdapter = new EducationAdapter(
                    EdtiCVActivity.this,
                    first,second,third,forth
            );

            LISTVIEW.setAdapter(ListAdapter);
        }else{
            LISTVIEW.setVisibility(View.GONE);
            txtEdu.setVisibility(View.GONE);
            EduCard.setVisibility(View.GONE);
        }

        ArrayList<String> firstExp= new ArrayList<String>();
        ArrayList<String> secondExp= new ArrayList<String>();
        ArrayList<String> thirdExp= new ArrayList<String>();
        ArrayList<String> forthExp= new ArrayList<String>() ;

        List<Experience> lstExp = objSeeker.getData().getSeeker_exp();
        if( lstExp.size() > 0) {

            for (int i = 0; i < lstExp.size(); i++)
            {


                String newHtml="";
                if(lstExp.get(i).getExp_desc() !=null) {
                    if (lstExp.get(i).getExp_desc().equals(""))
                        newHtml = "";
                    else
                        newHtml = lstExp.get(i).getExp_desc();
                }else{newHtml = "";}


                String enddateExp = lstExp.get(i).getEnd_date();
                if(lstExp.get(i).getState().equals("1"))
                    enddateExp ="الى الأن";
                firstExp.add(lstExp.get(i).getExp_name());
                secondExp.add(lstExp.get(i).getCompe_name());
                thirdExp.add(newHtml);
                forthExp.add(lstExp.get(i).getStart_date() + " - " + enddateExp);



            }

            ListAdapter = new EducationAdapter(
                    EdtiCVActivity.this,
                    firstExp,secondExp,thirdExp,forthExp
            );
            LISTVIEWEXP.setAdapter(ListAdapter);
        }else{
            LISTVIEWEXP.setVisibility(View.GONE);
            txtExp.setVisibility(View.GONE);
            ExpCard.setVisibility(View.GONE);
        }



        ArrayList<String> firstLang= new ArrayList<String>();
        ArrayList<String> secondLang= new ArrayList<String>();


        List<Language> lstLang = objSeeker.getData().getSeeker_lang();
        if( lstLang.size() > 0) {

            for (int i = 0; i < lstLang.size(); i++)
            {

                firstLang.add(lstLang.get(i).getLang_name());
                secondLang.add(" ("+lstLang.get(i).getLevel_name()+")");




            }

            ListAdapterSExp = new ExpAdapter(
                    EdtiCVActivity.this,
                    firstLang,secondLang
            );
            LISTVIEWLANG.setAdapter(ListAdapterSExp);

        }else{
            LISTVIEWLANG.setVisibility(View.GONE);
            txtLang.setVisibility(View.GONE);
            LangCard.setVisibility(View.GONE);
        }

        ArrayList<String> firstSpec= new ArrayList<String>();
        ArrayList<String> secondSpec= new ArrayList<String>();
        ArrayList<String> thirdSpec= new ArrayList<String>();
        ArrayList<String> forthSpec= new ArrayList<String>() ;

        List<Specialty> lstSpec = objSeeker.getData().getSeeker_spec();
        if( lstSpec.size() > 0) {

            for (int i = 0; i < lstSpec.size(); i++)
            {
                firstSpec.add(lstSpec.get(i).getSpec_name());
                secondSpec.add("");
                thirdSpec.add("");
                forthSpec.add("");
            }

            ListAdapter = new EducationAdapter(
                    EdtiCVActivity.this,
                    firstSpec,secondSpec,thirdSpec,forthSpec
            );
            LISTVIEWSPEC.setAdapter(ListAdapter);
        }else{
            LISTVIEWSPEC.setVisibility(View.GONE);
            txtSpec.setVisibility(View.GONE);
            SpecCard.setVisibility(View.GONE);
        }
        /*-------------- Skills ---------------*/

        ArrayList<String> firstSkills= new ArrayList<String>();
        ArrayList<String> secondSkills= new ArrayList<String>();
        ArrayList<String> thirdSkills= new ArrayList<String>();
        ArrayList<String> forthSkills= new ArrayList<String>() ;

        List<Skills> lstSkills = objSeeker.getData().getSeeker_skills();
        if( lstSkills.size() > 0) {

            for (int i = 0; i < lstSkills.size(); i++)
            {
                firstSkills.add(lstSkills.get(i).getSkills_name()+ " ("+lstSkills.get(i).getLevel_name()+")");
                secondSkills.add("");
                thirdSkills.add("");
                forthSkills.add("");
            }


            ListAdapterSExp = new ExpAdapter(
                    EdtiCVActivity.this,
                    firstSkills,secondSkills
            );
            LISTVIEWSKILLS.setAdapter(ListAdapterSExp);
        }else{
            LISTVIEWSKILLS.setVisibility(View.GONE);
            txtSkills.setVisibility(View.GONE);
            SkillsCard.setVisibility(View.GONE);
        }
        /*-------------- Cert ---------------*/

        ArrayList<String> firstCert= new ArrayList<String>();
        ArrayList<String> secondCert= new ArrayList<String>();
        ArrayList<String> thirdCert= new ArrayList<String>();
        ArrayList<String> forthCert= new ArrayList<String>() ;

        List<Certificate> lstCert = objSeeker.getData().getSeeker_cert();
        if( lstCert.size() > 0) {

            for (int i = 0; i < lstCert.size(); i++)
            {
                firstCert.add(lstCert.get(i).getCert_name());
                secondCert.add(lstCert.get(i).getCert_date());
                thirdCert.add("");
                forthCert.add("");
            }

            ListAdapter = new EducationAdapter(
                    EdtiCVActivity.this,
                    firstCert,secondCert,thirdCert,forthCert
            );
            LISTVIEWCERT.setAdapter(ListAdapter);
        }else{
            LISTVIEWCERT.setVisibility(View.GONE);
            txtCert.setVisibility(View.GONE);
            CertCard.setVisibility(View.GONE);
        }
        /*-------------- Tra ---------------*/
        ArrayList<String> firstTra= new ArrayList<String>();
        ArrayList<String> secondTra= new ArrayList<String>();
        ArrayList<String> thirdTra= new ArrayList<String>();
        ArrayList<String> forthTra= new ArrayList<String>() ;

        List<Training> lstTra = objSeeker.getData().getSeeker_train();
        if( lstTra.size() > 0) {

            for (int i = 0; i < lstTra.size(); i++)
            {
                firstTra.add(lstTra.get(i).getTrain_name());
                secondTra.add(lstTra.get(i).getTrain_comp());
                thirdTra.add(lstTra.get(i).getTrain_date());
                forthTra.add("");
            }

            ListAdapter = new EducationAdapter(
                    EdtiCVActivity.this,
                    firstTra,secondTra,thirdTra,forthTra
            );
            LISTVIEWTRA.setAdapter(ListAdapter);

        }else{
            LISTVIEWTRA.setVisibility(View.GONE);
            txtTra.setVisibility(View.GONE);
            TraCard.setVisibility(View.GONE);
        }

        /*-------------- Hobby ---------------*/

        ArrayList<String> firstHobby= new ArrayList<String>();
        ArrayList<String> secondHobby= new ArrayList<String>();
        ArrayList<String> thirdHobby= new ArrayList<String>();
        ArrayList<String> forthHobby= new ArrayList<String>() ;

        List<Hobby> lstHobby = objSeeker.getData().getSeeker_hobby();
        if( lstHobby.size() > 0) {

            int ToEnd = lstHobby.size();
            for (int i = 0; i < lstHobby.size(); i++)
            {
                String Content = lstHobby.get(i).getHobby_name();
                a =  a.concat(Content);

                if (i < ToEnd - 1) {
                    a =  a.concat("، ");
                }



            }
            if(!a.equals("")) {
                firstHobby.add(a);
                secondHobby.add("");
                thirdHobby.add("");
                forthHobby.add("");
            }


            ListAdapter = new EducationAdapter(
                    EdtiCVActivity.this,
                    firstHobby,secondHobby,thirdHobby,forthHobby
            );
            LISTVIEWHOBBY.setAdapter(ListAdapter);

        }else{
            LISTVIEWHOBBY.setVisibility(View.GONE);
            txtHobby.setVisibility(View.GONE);
            HobbyCard.setVisibility(View.GONE);
        }



        /*-------------- Info ---------------*/
        ArrayList<String> firstInfo= new ArrayList<String>();
        ArrayList<String> secondInfo= new ArrayList<String>();
        ArrayList<String> thirdInfo= new ArrayList<String>();
        ArrayList<String> forthInfo= new ArrayList<String>() ;

        List<Info> lstInfo = objSeeker.getData().getSeeker_info();
        if( lstInfo.size() > 0) {
            for (int i = 0; i < lstInfo.size(); i++) {
                firstInfo.add(lstInfo.get(i).getInfo_name());
                secondInfo.add(lstInfo.get(i).getInfo_text());
                thirdInfo.add(lstInfo.get(i).getInfo_date());
                forthInfo.add("");
            }

            ListAdapter = new EducationAdapter(
                    EdtiCVActivity.this,
                    firstInfo, secondInfo, thirdInfo, forthInfo
            );
            LISTVIEWINFO.setAdapter(ListAdapter);

        }else{
            LISTVIEWINFO.setVisibility(View.GONE);
            InfoCard.setVisibility(View.GONE);
            txtInfo.setVisibility(View.GONE);
        }



    }
    public static String nl2br(String text) {
        if(text ==null)
            return "";
        return text.replace("\n\n", "<p/>").replace("\n", "<br/>");
    }
}