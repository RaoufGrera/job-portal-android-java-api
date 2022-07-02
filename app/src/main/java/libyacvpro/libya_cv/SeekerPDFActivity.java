package libyacvpro.libya_cv;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.CountDownTimer;
import androidx.transition.TransitionManager;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.os.Build;
import android.os.Environment;
        import androidx.core.app.ActivityCompat;
        import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
        import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
//import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
        import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import libyacvpro.libya_cv.entities.CertificatePackage.Certificate;
import libyacvpro.libya_cv.entities.EducationPackage.Education;
import libyacvpro.libya_cv.entities.ExperiencePackage.Experience;
import libyacvpro.libya_cv.entities.HobbyPackage.Hobby;
import libyacvpro.libya_cv.entities.InfoPackage.Info;
import libyacvpro.libya_cv.entities.LangPackage.Language;
import libyacvpro.libya_cv.entities.SeekerCvPackage.ShowCvResponse;
import libyacvpro.libya_cv.entities.SkillsPackage.Skills;
import libyacvpro.libya_cv.entities.SpecialtyPackage.Specialty;
import libyacvpro.libya_cv.entities.TrainingPackage.Training;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;


public class SeekerPDFActivity extends AppCompatActivity  {
    private static final String TAG = "SeekerPDFActivity";


    ApiService service;
    TokenManager tokenManager;
    Call<ShowCvResponse> call;

    TextView edit_text_content;
    //Call<Message> callSave;

   @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;
    Button imgWifi;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    public static final String CSS =   "               " +
            "                    .printthis{" +
            "                        display: block !important;" +
            "                        opacity: 1 !important ;" +

            "                    }" +

            "                        thead {display: table-header-group;}" +
            "                    .notprint{" +
            "                        display: none;" +
            "                    }" +

            "                    #print-head {border-bottom: 1px solid #000000;}" +

            "                    body {direction: rtl;" +
            "                        line-height: 1.8em;font-size: 16px; overflow: auto;" +
            "                        float: none;background-color: #fff;}" +
            "                    #education{" +
            "                        border-right:4px solid #dd1144;}" +
            "                    table td tr {margin-bottom: 13px;line-height: 1.8;}" +
            "                    .hr {border-color: #BDBDBD;" +
            "                    }" +

            "                    .numb{" +
            "                        color: rgb(54, 54, 54);" +
            "                        font-size: 100%;" +
            "                        float: left;" +
            "                    }" +
            "                    .textb {" +
            "                        font-size: 20px;    line-height: 2em;" +
            "                    }" +
            "                    .texts {" +
            "                        color: rgb(54, 54, 54);" +
            "                        font-size: 16px; line-height: 2em;}"+
            "                    .infop { text-align: center;" +
            "                        color: rgb(54, 54, 54);" +
            "                        font-size: 85%;" +

            "                    }" +

            "                    .posttitle {" +

            "                        color: #464646 !important;" +
            "                        font-size: 22px;" +
            "                        padding-right: 4px;" +
            "                        line-height: 1.2;" +
            "                    }" +

            "                    .infocont {" +
            "                        vertical-align: top;" +
            "                        font-size: 16px;" +
            "                    }" +

            "                    .tdcontent{" +
            "                        padding-top: 10px;" +
            "                    }" +
            "                    table.first {" +

            "                    }" +
            "                    table.firstinfo {" +
            "                        border-top: 1px solid #eaeaea;" +
            "                    }" +
            "                    hr{" +
            "                        margin-top: 6px;" +
            "                        margin-bottom: 6px;" +
            "                    }" +
            "                    table.firstcont {" +

            "                    }" +
            "" +
            "     .top{padding-top:8px}               .imgseeker{" +
            "                        border: 1px solid #999;" +
            "                        max-height: 225px;" +
            "                        max-width: 200px;" +
            "                        padding: 2px;" +
            "" +
            "" +
            "                    }" +

            "                    .printthis{" +
            "                       /* opacity: 0;*/" +
            "                        display: block;" +
            "                    }" +
            "" +
            "                    table .top > span:first-child {" +
            "                        float: right;" +
            "                        border: 9px solid #569480;" +
            "                        border-left: 0;" +
            "                        border-right: 5px solid #569480;       }" +
            "" +
            "                 ";

    public static final String CSS2 =   "               " +
            "                    .printthis{" +
            "                        display: block !important;" +
            "                        opacity: 1 !important ;" +

            "                    }" +

            "                        thead {display: table-header-group;}" +
            "                    .notprint{" +
            "                        display: none;" +
            "                    }" +

            "                    #print-head {border-bottom: 1px solid #000000;}" +

            "                    body {direction: rtl;" +
            "                        line-height: 1.8em;font-size: 12px; overflow: auto;" +
            "                        float: none;background-color: #fff;}" +
            "                    #education{" +
            "                        border-right:4px solid #dd1144;}" +
            "                    table td tr {margin-bottom: 13px;line-height: 1.8;}" +
            "                    .hr {border-color: #BDBDBD;" +
            "                    }" +

            "                    .numb{" +
            "                        color: rgb(54, 54, 54);" +
            "                        font-size: 100%;" +
            "                        float: left;" +
            "                    }" +
            "                    .textb {" +
            "                        font-size: 14px;    line-height: 1.8;" +
            "                    }" +
            "                    .texts {" +
            "                        color: rgb(54, 54, 54);" +
            "                        font-size: 12px; line-height: 1.8;}"+
            "                    .infop { text-align: center;" +
            "                        color: rgb(54, 54, 54);" +
            "                        font-size: 12px;line-height: 1.8;" +

            "                    }" +

            "                    .posttitle {" +

            "                        color: #464646 !important;" +
            "                        font-size: 16px;" +
            "                        padding-right: 4px;" +
            "                        line-height: 1.5;" +
            "                    }" +

            "                    .infocont {" +
            "                        vertical-align: top;" +
            "                        font-size: 12px;" +
            "                    }" +

            "                    .tdcontent{" +
            "                        padding-top: 10px;" +
            "                    }" +
            "                    table.first {" +

            "                    }" +
            "                    table.firstinfo {" +
            "                        border-top: 1px solid #eaeaea;" +
            "                    }" +
            "                    hr{" +
            "                        margin-top: 6px;" +
            "                        margin-bottom: 6px;" +
            "                    }" +
            "                    table.firstcont {" +

            "                    }" +
            "" +
            "     .top{padding-top:8px}               .imgseeker{" +
            "                        border: 1px solid #999;" +
            "                        max-height: 225px;" +
            "                        max-width: 200px;" +
            "                        padding: 2px;" +
            "" +
            "" +
            "                    }" +

            "                    .printthis{" +
            "                       /* opacity: 0;*/" +
            "                        display: block;" +
            "                    }" +
            "" +
            "                    table .top > span:first-child {" +
            "                        float: right;" +
            "                        border: 9px solid #569480;" +
            "                        border-left: 0;" +
            "                        border-right: 5px solid #569480;       }" +
            "" +
            "                 ";

    Button bt_generar;
    public static final String DEST = Environment.getExternalStorageDirectory() + "/Documents/LibyaCV/PDF";
    public static final String SRC = "./src/test/resources/pdfs/nameddestinations.pdf";
    public static String HTML =  "<html>" +
            "<body style=\"font-family:hacen liner print-out , Droid Sans, sans-serif;\">" +
            "<table>" +
            "<tr>" +
            "<td dir=\"rtl\">الرجاء إعادة حفظ السيرة الذاتية من جديد</td>" +
             "</tr>" +
            "</table>" +
            "</body>" +
            "</html>"; ;
   // private RewardedVideoAd mRewardedVideoAd;

     private static final String APP_ID = "ca-app-pub-9929016091047307~2213947061";
    private AdView mAdView;

    private InterstitialAd interstitialAd;
    private CountDownTimer countDownTimer;
    private Button retryButton;
    private boolean gameIsInProgress;
    private long timerMilliseconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_pdf);
       // MobileAds.initialize(this, APP_ID);
        imgWifi = (Button) findViewById(R.id.imgWifi);
        Drawable leftDrawable = AppCompatResources.getDrawable(this, R.drawable.ic_wifi);
        imgWifi.setCompoundDrawablesWithIntrinsicBounds(null, null, leftDrawable, null);
        edit_text_content = (TextView) findViewById(R.id.edit_text_content);



      //  interstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml

       // interstitialAd.setAdUnitId("ca-app-pub-9929016091047307/2689662234");
        //interstitialAd.loadAd(new AdRequest.Builder().build());

       // showInterstitial();

        ButterKnife.bind(this);

        apiLoad();
       bt_generar =  findViewById(R.id.button_create);
        bt_generar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                try {
                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // generarPDFOnclick();
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
                return true;
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
    private void showWifi(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
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
    @OnClick(R.id.imgWifi)
    void refreshActivity(){
        apiLoad();

    }
    private void apiLoad(){
       // showLoading();

        boolean IsValid = isOnline();
        if (!IsValid) {
            showWifi();
            return;
        }
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        if(tokenManager.getToken() == null){
            startActivity(new Intent(SeekerPDFActivity.this, LoginActivity.class));
            finish();
        }
        showLoading();
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        showLoading();
        call = service.getSeekerCv();
        call.enqueue(new Callback<ShowCvResponse>() {
            @Override
            public void onResponse(Call<ShowCvResponse> call, Response<ShowCvResponse> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){

                    ShowCvResponse  objSeeker = response.body();


                    setData(objSeeker);
                   showForm();

                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(SeekerPDFActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<ShowCvResponse> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
                showForm();
            }
        });

    }

    public  void setData(ShowCvResponse objSeeker){
        String string =objSeeker.getData().getJob_seeker().getFname() +" "+ objSeeker.getData().getJob_seeker().getLname();
        edit_text_content.setText(string);



        HTML = "<html>" +
                "<body dir=\"rtl\" style=\"font-family:JF Flat , Droid Sans, sans-serif;font-size: 16px; line-height: 1.8;\"  >";


        HTML +=
                "<table width=\"100%\" class=\"printthis\">";

        HTML+=

                "<tr>"+

                "<td style=\" width:650px;\"><table style=\" width:650px;\"><tr><td style=\"width:38%;\">&nbsp;&nbsp;<span style=\"font-size:18px;\"> » </span><span class=\"infop\">"+objSeeker.getData().getJob_seeker().getCity_name();

        if (objSeeker.getData().getJob_seeker().getAddress() != "")
            HTML += " - " + objSeeker.getData().getJob_seeker().getAddress();

        HTML += ". </span></td><td style=\"width:25%\"><span style=\"font-size:18px;\">}</span><span style=\"vertical-align: middle;\" class=\"infop\" > "+objSeeker.getData().getJob_seeker().getBirth_day() +" سنة "+" </span></td>" ;


        if(objSeeker.getData().getJob_seeker().getPhone() != "")
            HTML += "<td><span style=\"font-size:18px;vertical-align: middle;\">{</span> <span style=\"width:10%\" class=\"infop\">"+ objSeeker.getData().getJob_seeker().getPhone() +" </span></td>";

            HTML +=
                "<td style=\"width:32%;text-align: center;\"><span style=\"font-size:18px;vertical-align: middle;\">«</span><span style=\"vertical-align: middle;\" class=\"infop\"> "+ objSeeker.getData().getJob_seeker().getEmail() + "</span>" +
                "</td>" +
                "  </tr></table></td></tr>";


        if(objSeeker.getData().getJob_seeker().getGoal_text() != "" &&  objSeeker.getData().getJob_seeker().getGoal_text() != null) {

            HTML +=
                    "                    <tr>" +
                            "                        <td height=\"7\" style=\"border-bottom:1px solid #949494;\"></td><tr><td height=\"20\"></td></tr>" +
                            "                    </tr>" +
                            "                    <tr>" +
                            "                        <td class=\"top\">" +

                            "                            <table><tr><td  style=\"border-right:4px solid #218a75;padding-right:6px;\"><span class=\"posttitle\">الهدف الوظيفي</span>" +
                            "                        </td></tr></table></td>" +
                            "                    </tr>" +
                            "                    <tr>" +
                            "                        <td class=\"tdcontent\">" +
                            "                            <span class=\"texts\">";
            HTML += objSeeker.getData().getJob_seeker().getGoal_text() + "</span>" +
                    "" +
                    "                        </td>" +
                    "                    </tr>";
        }
        if (objSeeker.getData().getSeeker_ed().size() > 0){






            HTML +=   "                    <tr>" +
                    "                        <td height=\"8\" style=\"border-bottom:1px solid #949494;\"></td><tr><td height=\"20\"></td></tr>" +
                "                    </tr>" +
                "                    <tr>" +
                    "                        <td class=\"top\">" +

                    "                            <table><tr><td  style=\"border-right:4px solid #218a75;padding-right:6px;\"><span class=\"posttitle\">المؤهل العلمي</span>" +
                    "                        </td></tr></table></td>" +
                "                    </tr>" +
                "" +
                "<tr><td height=\"4\" ><span></span></td></tr>";

        int ToEnd = objSeeker.getData().getSeeker_ed().size();

        List<Education> lstEdu = objSeeker.getData().getSeeker_ed();
            for (int i = 0; i <= ToEnd; i++)
            {

                String a = objSeeker.getData().getSeeker_ed().get(i).getEdt_name();
                String stEdt =  a.replace("\\","\\\\");
                HTML +=
                    "<tr><td height=\"5\" ><span> </span></td></tr>" +
                    "                    <tr>" +
                    "" +
                    "                        <td  ><table><tr><td style=\" width:18%;\"><span class=\"textb\">";
                            HTML +=  lstEdu.get(i).getUniv_name() + " ، " + lstEdu.get(i).getFaculty_name() +"</span></td>"+

                    "                            <td style=\" width:82%;\"><span style=\"  color: #696969;font-size: 90%; \" class=\"numb\">"+ lstEdu.get(i).getStart_date() + "   -  "+ lstEdu.get(i).getEnd_date() +"</span></td>" +
                    "                        </tr></table></td>" +
                    "" +
                    "                    </tr>" +
                    "                    <tr>" +
                    "                        <td  height=\"30\">" +
                    "<span class=\"texts\">"+ a; if(lstEdu.get(i).getSed_name() !=""){
                    HTML+=" ، "+ lstEdu.get(i).getSed_name();}
                if(lstEdu.get(i).getAvg() !="" && lstEdu.get(i).getAvg() !=null ){
                HTML += " ، "+ lstEdu.get(i).getAvg()+"%" ;
                }

                HTML +=
                        "</span>" +
                    "                        </td>" +
                    "                    </tr>" ;


               if (0 !=  --ToEnd) {
                   HTML += "<tr> <td height=\"10\" ><table><tr><td  style=\" width:50%;border-top: 1px solid #ddd;\"></td><td  style=\"width:50%;\"></td></tr></table></td></tr>";
                }

              }



        }



        if (objSeeker.getData().getSeeker_exp().size() > 0) {

            HTML += "<tr>" +
                    "                        <td height=\"7\" style=\"border-bottom:1px solid #949494;\"></td><tr><td height=\"20\"></td></tr>" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td class=\"top\">" +

                    "                            <table><tr><td  style=\"border-right:4px solid #218a75;padding-right:6px;\"><span class=\"posttitle\">الخبرة</span>" +
                    "                        </td></tr></table></td>" +
                    "                    </tr>"+
                    "<tr><td height=\"4\" ><span></span></td></tr>";



            int ToEnd = objSeeker.getData().getSeeker_exp().size();


            List<Experience> lstExp = objSeeker.getData().getSeeker_exp();


            for (int i = 0; i <= ToEnd; i++) {
                String newHtml="";
                if(lstExp.get(i).getExp_desc() ==null || lstExp.get(i).getExp_desc()!="")
                    newHtml = nl2br(lstExp.get(i).getExp_desc());

                String enddateExp = lstExp.get(i).getEnd_date();
                if(lstExp.get(i).getState().equals("1"))
                    enddateExp ="الى الأن";
                HTML += "<tr><td height=\"5\" ><span> </span></td></tr>" +

                        "                    <tr>\n" +
                        "                        <td><table><tr><td style=\" width:18%;\"><span class=\"textb\">"+lstExp.get(i).getExp_name()+"</span></td>" +
                        "                            <td style=\" width:82%;\"><span style=\"  color: #696969;font-size: 90%;float: left; \" class=\"numb\">" + lstExp.get(i).getStart_date() + " - " + enddateExp + "</span></td>" +
                        "                        </tr></table></td>\n" +
                        "                    </tr> <tr>\n" +
                "                        <td height=\"30\" ><span class=\"texts\">"+lstExp.get(i).getCompe_name()+ "</span></td>\n" +
                        "                    </tr>\n";

                if (newHtml != "") {


                    HTML += "                    <tr>\n" +
                            "                        <td height=\"30\"><span class=\"texts\" style=\"    line-height: 1.8;     \">" + newHtml + "</span></td>\n" +
                            "                    </tr>";

                }
                if (0 != --ToEnd) {
                    HTML += "<tr> <td height=\"8\" ><table><tr><td  style=\" width:50%;border-top: 1px solid #ddd;\"></td><td  style=\"width:50%;\"></td></tr></table></td></tr>";
                }
            }
        }


        if (objSeeker.getData().getSeeker_lang().size() > 0) {

            HTML += "<tr>" +
                    "                        <td height=\"10\" style=\"border-bottom:1px solid #949494;\"></td><tr><td height=\"20\"></td></tr>" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td class=\"top\">" +

                    "                            <table><tr><td  style=\"border-right:4px solid #218a75;padding-right:6px;\"><span class=\"posttitle\">اللغات</span>" +
                    "                        </td></tr></table></td>" +
                    "                    </tr>"+
                    "<tr><td height=\"8\" ><span></span></td></tr>";
            HTML += "<tr><td height=\"8\" ><span> </span></td></tr><tr><td>";


        int ToEnd = objSeeker.getData().getSeeker_lang().size();

        List<Language> lstLang = objSeeker.getData().getSeeker_lang();
        for (int i = 0; i < ToEnd; i++) {


            HTML += "<span class=\"textb\">"+lstLang.get(i).getLang_name() +" : </span><span class=\"texts\">( "+ lstLang.get(i).getLevel_name()+" )"  ;

          if (i < ToEnd-1) {
                   HTML += " ، ";

          }
            HTML +="</span>";
               }
        HTML +="</td>\n" +
                "</tr>";


    }


        if (objSeeker.getData().getSeeker_spec().size() > 0) {

            HTML += "<tr>" +
                    "                        <td height=\"10\" style=\"border-bottom:1px solid #949494;\"></td><tr><td height=\"20\"></td></tr>" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td class=\"top\">" +

                    "                            <table><tr><td  style=\"border-right:4px solid #218a75;padding-right:6px;\"><span class=\"posttitle\">التخصصات</span>" +
                    "                        </td></tr></table></td>" +
                    "                    </tr>"+
                    "<tr><td height=\"4\" ><span></span></td></tr>";
            HTML += "<tr><td height=\"5\" ><span> </span></td></tr><tr><td>";

            int ToEnd = objSeeker.getData().getSeeker_spec().size();

            List<Specialty> lstSpec = objSeeker.getData().getSeeker_spec();
            for (int i = 0; i < ToEnd; i++) {
                boolean validd = isArabic(lstSpec.get(i).getSpec_name());


                if(validd) {
                    HTML += "<span class=\"textb\">" + lstSpec.get(i).getSpec_name();
                }else{
                    HTML += "<span class=\"textb\">" + lstSpec.get(i).getSpec_name();

                }

                if (i < ToEnd - 1) {
                    HTML += "<span> ، </span>";

                }
                HTML += "</span>";
            }
            HTML += "</td>\n" +
                    "</tr>";
        }





        if (objSeeker.getData().getSeeker_skills().size() > 0) {

            HTML += "<tr>" +
                    "                        <td height=\"10\" style=\"border-bottom:1px solid #949494;\"></td><tr><td height=\"20\"></td></tr>" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td class=\"top\">" +

                    "                            <table><tr><td  style=\"border-right:4px solid #218a75;padding-right:6px;\"><span class=\"posttitle\">المهارات</span>" +
                    "                        </td></tr></table></td>" +
                    "                    </tr>"+
                    "<tr><td height=\"4\" ><span></span></td></tr>";

            int ToEnd = objSeeker.getData().getSeeker_skills().size();

            List<Skills> lstSkills = objSeeker.getData().getSeeker_skills();
            for (int i = 0; i < ToEnd; i++) {


                HTML += "<tr><td height=\"5\" ><span> </span></td></tr>" +
                                "                    <tr>\n" +
                                "                        <td  height=\"30\"><span class=\"textb\">" + lstSkills.get(i).getSkills_name() + " </span><span class=\"texts\"> ( " + lstSkills.get(i).getLevel_name() + " )</span></td>\n" +
                                "                    </tr>";

                if (i !=  (ToEnd -1)) {
                    HTML += "<tr> <td height=\"8\" ><table><tr><td  style=\" width:50%;border-top: 1px solid #ddd;\"></td><td  style=\"width:50%;\"></td></tr></table></td></tr>";
                }
            }
        }




        if (objSeeker.getData().getSeeker_cert().size() > 0) {

            HTML += "<tr>" +
                    "                        <td height=\"10\" style=\"border-bottom:1px solid #949494;\"></td><tr><td height=\"20\"></td></tr>" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td class=\"top\">" +

                    "                            <table><tr><td  style=\"border-right:4px solid #218a75;padding-right:6px;\"><span class=\"posttitle\">الشهادات</span>" +
                    "                        </td></tr></table></td>" +
                    "                    </tr>"+
                    "<tr><td height=\"4\" ><span></span></td></tr>";

            int ToEnd = objSeeker.getData().getSeeker_cert().size();

            List<Certificate> lstCert = objSeeker.getData().getSeeker_cert();
            for (int i = 0; i <= ToEnd; i++) {

                HTML += "<tr><td height=\"8\" ><span> </span></td></tr>" +
                        "                    <tr>\n" +
                        "                        <td><table><tr><td style=\" line-height: 1.8; width:18%;\"><span class=\"textb\">"+lstCert.get(i).getCert_name()+"</span></td>" +
                        "                            <td style=\" width:82%;\"><span style=\"  color: #696969;font-size: 90%;float: left; \" class=\"numb\">" + lstCert.get(i).getCert_date() +"</span></td>" +
                        "                        </tr></table></td>\n" +
                        "                    </tr>";
                if (0 != --ToEnd) {
                    HTML += "<tr> <td height=\"8\" ><table><tr><td  style=\" width:50%;border-top: 1px solid #ddd;\"></td><td  style=\"width:50%;\"></td></tr></table></td></tr>";
                }
            }
        }
        if (objSeeker.getData().getSeeker_train().size() > 0) {

            HTML += "<tr>" +
                    "                        <td height=\"10\" style=\"border-bottom:1px solid #949494;\"></td><tr><td height=\"20\"></td></tr>" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td class=\"top\">" +

                    "                            <table><tr><td  style=\"border-right:4px solid #218a75;padding-right:6px;\"><span class=\"posttitle\">التدريب والدورات</span>" +
                    "                        </td></tr></table></td>" +
                    "                    </tr>"+
                    "<tr><td height=\"4\" ><span></span></td></tr>";

            int ToEnd = objSeeker.getData().getSeeker_train().size();

            List<Training> lstTrain = objSeeker.getData().getSeeker_train();
            for (int i = 0; i <= ToEnd; i++) {

                HTML += "<tr><td height=\"5\" ><span> </span></td></tr>" +
                        "                    <tr>\n" +
                        "                        <td><table><tr><td style=\" width:18%;\"><span class=\"textb\">"+lstTrain.get(i).getTrain_name()+"</span></td>" +
                        "                            <td style=\" width:82%;\"><span style=\"  color: #696969;font-size: 90%;float: left; \" class=\"numb\">" + lstTrain.get(i).getTrain_date() +"</span></td>" +
                        "                        </tr></table></td>\n" +
                        "                    </tr>"+
                "                    <tr>\n" +
                "                        <td height=\"30\" ><span class=\"texts\">الجهة:  "+lstTrain.get(i).getTrain_comp()+"</span></td>\n" +
                "                    </tr>\n";
                if (0 != --ToEnd) {
                    HTML += "<tr> <td height=\"8\" ><table><tr><td  style=\" width:50%;border-top: 1px solid #ddd;\"></td><td  style=\"width:50%;\"></td></tr></table></td></tr>";
                }
            }
        }

        if (objSeeker.getData().getSeeker_hobby().size() > 0) {

            HTML += "<tr>" +
                    "                        <td height=\"10\" style=\"border-bottom:1px solid #949494;\"></td><tr><td height=\"20\"></td></tr>" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td class=\"top\">" +

                    "                            <table><tr><td  style=\"border-right:4px solid #218a75;padding-right:6px;\"><span class=\"posttitle\">الهويات</span>" +
                    "                        </td></tr></table></td>" +
                    "                    </tr>"+
                    "<tr><td height=\"4\" ><span></span></td></tr>";
            HTML += "<tr><td height=\"8\" ><span> </span></td></tr><tr><td>";
            int ToEnd = objSeeker.getData().getSeeker_hobby().size();

            List<Hobby> lstHobby = objSeeker.getData().getSeeker_hobby();
            for (int i = 0; i < ToEnd; i++) {

                HTML += "<span class=\"textb\">" + lstHobby.get(i).getHobby_name();


                if (i < ToEnd - 1) {
                    HTML += " ، ";

                }
                HTML += "</span>";
            }
            HTML += "</td>\n" +
                    "</tr>";
        }

        if (objSeeker.getData().getSeeker_info().size() > 0) {

            HTML += "<tr>" +
                    "                        <td height=\"10\" style=\"border-bottom:1px solid #949494;\"></td><tr><td height=\"20\"></td></tr>" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td class=\"top\">" +

                    "                            <table><tr><td  style=\"border-right:4px solid #218a75;padding-right:6px;\"><span class=\"posttitle\">معلومات إضافية</span>" +
                    "                        </td></tr></table></td>" +
                    "                    </tr>"+
                    "<tr><td height=\"4\" ><span></span></td></tr>";
            int ToEnd = objSeeker.getData().getSeeker_info().size();

            List<Info> lstInfo = objSeeker.getData().getSeeker_info();
            for (int i = 0; i <= ToEnd; i++) {

                HTML += "<tr><td  ><span> </span></td></tr>" +
                        "                    <tr>\n" +
                        "                        <td style=\" line-height: 1.8;\" height=\"38\"><table><tr><td style=\" width:18%;line-height: 1.8;\"><span class=\"textb\">"+lstInfo.get(i).getInfo_name()+"</span></td>" +
                        "                            <td style=\" width:82%;\"><span style=\"  color: #696969;font-size: 90%;float: left; \" class=\"numb\">" + lstInfo.get(i).getInfo_date() +"</span></td>" +
                        "                        </tr></table></td>\n" +
                        "                    </tr>"+
                "                    <tr>\n" +
                "                        <td height=\"25\" style=\" line-height: 1.8;\" ><span class=\"texts\">"+lstInfo.get(i).getInfo_text() +"</span></td>\n" +
                "                    </tr>\n" ;
                if (0 != --ToEnd) {
                    HTML += "<tr> <td height=\"10\" ><table><tr><td  style=\" width:50%;border-top: 1px solid #ddd;\"></td><td  style=\"width:50%;\"></td></tr></table></td></tr>";
                }
            }
        }

        HTML +=

                        "                </table>" +

              "</body>" +
                        "</html>";



    }



    private  boolean isArabic(String text){
        String textWithoutSpace = text.trim().replaceAll(" ","");
        for(int i =0; i < textWithoutSpace.length();){
            int c = textWithoutSpace.codePointAt(i);

            if(c >= 0x0600 && c <= 0x06FF || (c >= 0xFE70 && c<=0xFEFF))
                i +=Character.charCount(c);
            else
                return  false;

        }
        return true;
    }

    public static String nl2br(String text) {
        if(text ==null)
            return "";
        return text.replace("\n\n", "<p/>").replace("\n", "<br/>");
    }
   /* private void showForm(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
    }

    private void showLoading(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }*/

     public void generarPDFOnclick() throws IOException, DocumentException{
         File file = new File(DEST);
         file.getParentFile().mkdirs();
          createPdf(DEST);


    }

    class TableHeader extends PdfPageEventHelper {
         String header;
         PdfTemplate total;

        public void setHeader(String header) {
            this.header = header;
        }

        public void onOpenDocument(PdfWriter writer, Document document) {
            total = writer.getDirectContent().createTemplate(30, 16);
        }

        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable table = new PdfPTable(2);

            try {
                table.setWidths(new int[]{20, 20});
                table.setTotalWidth(527);



                table.setLockedWidth(true);
                 table.getDefaultCell().setFixedHeight(25);
                 table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                table.addCell(header);
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                Font f = FontFactory.getFont("assets/JF-Flat-regular123.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                f.setSize(16);
                f.setColor(new BaseColor(68,146,127));


                Phrase phrase = new Phrase();

                String string = edit_text_content.getText().toString();

                phrase.add(new Chunk(string, f));
                PdfPCell cell = new PdfPCell(phrase);

                cell.setUseDescender(true);
                cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                cell.setBorder(Rectangle.BOTTOM);

                table.addCell(cell);
               /* try {
                    // get input stream
                    InputStream ims = getAssets().open("lcv3.png");
                    Bitmap bmp = BitmapFactory.decodeStream(ims);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Image image = Image.getInstance(stream.toByteArray());
                    table.addCell(image);
                }
                catch(IOException ex)
                {
                    return;
                }
*/


                 PdfPCell cell1 = new PdfPCell(Image.getInstance(total));
                 cell1.setBorder(Rectangle.BOTTOM);
                table.addCell(cell1);




                table.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
             }
            catch(DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        /**
         * Fills out the total number of pages before the document is closed.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber() - 1)),
                    2, 2, 0);
        }
    }

    public void createPdf(String file) throws IOException, DocumentException {
        // step 1


        File docsFolder = new File(DEST);
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }

     /*   File files = new File(docsFolder, "libyacv.pdf");
        FileOutputStream fOut = new FileOutputStream(file);*/

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

        pdfFile = new File(DEST, "LibyaCV_PDF "+sdf.format(Calendar.getInstance().getTime()) + ".pdf");
        FileOutputStream ffOut = new FileOutputStream(pdfFile);


        //Document document = new Document(PageSize.A4, 20, 20, 50, 25);
        //Document document = new Document(PageSize.A4, 36, 36, 36, 72);

        Document document = new Document(PageSize.A4, 36, 36, 70
                , 36);






        //Document document = new Document(PageSize.A4);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        TableHeader event = new TableHeader();
        writer.setPageEvent(event);
        // step 3
        document.open();
        // step 4
        // Styles
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        CssFile cssFile = XMLWorkerHelper.getCSS(new ByteArrayInputStream(CSS2.getBytes()));
        cssResolver.addCss(cssFile);


        //CSSResolver cssResolver = new StyleAttrCSSResolver();
        XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
        fontProvider.register("assets/JF-Flat-regular123.otf");

        //document.add(new Paragraph("الموقع الإلكتروني",fontProvider));


        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

        // Pipelines
        PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
        HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);


        // XML Worker
        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);
        StringReader xmlString = new StringReader(HTML);

       p.parse(xmlString);


        // step 5
        document.close();

        previewPdf();
    }


    public  void muestraPDF(String archivo, Context context){


        Toast.makeText(context,";eydno",Toast.LENGTH_LONG).show();
        File file = new File(archivo);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(context,"No fdsfds",Toast.LENGTH_LONG).show();
        }





    }
    private void createPdfWrapper() throws IOException, DocumentException{

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
            generarPDFOnclick();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        showLoading();
                        createPdfWrapper();
                        showForm();
                    } catch (FileNotFoundException e) {
                        showForm();
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        showForm();
                        e.printStackTrace();
                    } catch (IOException e) {
                        showForm();
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void previewPdf() {

        //PackageManager packageManager = getPackageManager();
       // Intent testIntent = new Intent(Intent.ACTION_VIEW);
       // testIntent.setType("application/pdf");
       // List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
      //  if (list.size() > 0) {
            Intent target  = new Intent();
        target .setAction(Intent.ACTION_VIEW);
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID +".provider", pdfFile);

// I am opening a PDF file so I give it a valid MIME type
        target.setDataAndType(uri, "application/pdf");


// validate that the device can open your File!
        PackageManager pm = this.getPackageManager();
        if (target.resolveActivity(pm) != null) {
            startActivity(target);
        }
       // Uri uri = Uri.fromFile(pdfFile);
       // target .setDataAndType(uri, "application/pdf");

       // target.setDataAndType(Uri.fromFile(pdfFile),"application/pdf");

      /*  Intent intent = Intent.createChooser(target, "Open File");

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
            Toast.makeText(this,"Download a PDF Viewer to see the generated PDF",Toast.LENGTH_SHORT).show();
        }
      //  startActivity(intent);
        /*}else{
            Toast.makeText(this,"Download a PDF Viewer to see the generated PDF",Toast.LENGTH_SHORT).show();
        }*/
    }


}
