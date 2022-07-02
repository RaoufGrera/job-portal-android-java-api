package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.transition.TransitionManager;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import libyacvpro.libya_cv.entities.GeneralPackage.City;
import libyacvpro.libya_cv.entities.GeneralPackage.Domain;
import libyacvpro.libya_cv.entities.ShowJobPackage.ShowParaJob;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

import static android.content.Context.MODE_PRIVATE;


public class SearchJobForm extends Fragment {

    String TAG = "SearchJobForm";
    Context context;

    ApiService service;
    TokenManager tokenManager;
    Call<ShowParaJob> call;

     TextView txtString;

     Spinner spCity;

     Spinner spDomain;
   //  Spinner spStatus;

  //  Spinner spType;

    Button btnSearch;
    Button btnSearchComp;



    @BindView(R.id.container)
    RelativeLayout containerr;

    String[] dList;
    String[] cList;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;

    TextView lblInfo;
    Button imgWifi;

    Context con=null;
    private static final String APP_ID = "ca-app-pub-9929016091047307~2213947061";

    private AdView mAdView;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        con= context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_job_form, container, false);
        spCity = (Spinner)  view.findViewById(R.id.spCitySearch);
        spDomain = (Spinner)  view.findViewById(R.id.spDomainSearch);
     //   spStatus = (Spinner)  view.findViewById(R.id.spStatusSearch);
      //  spType = (Spinner)  view.findViewById(R.id.spTypeSearch);
        btnSearch = (Button) view.findViewById(R.id.btnSearchJob);
        formContainer = (LinearLayout) view.findViewById(R.id.form_container);
        containerr = (RelativeLayout) view.findViewById(R.id.container);
        loader = (ProgressBar) view.findViewById(R.id.loader);



        imgWifi = (Button) view.findViewById(R.id.imgWifi);

        //MobileAds.initialize(getContext(), APP_ID);

        AdView adView = new AdView(getContext());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-9929016091047307/3212815535");

        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        imgWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadApi();
            }
        });
       // txtString = (TextView) view.findViewById(R.id.editSearchString);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),SearchActivity.class);
               // String g = txtString.getText().toString();
               // intent.putExtra("string","");
                intent.putExtra("city", spCity.getSelectedItem().toString());
                intent.putExtra("domain", spDomain.getSelectedItem().toString());
                intent.putExtra("clist", cList);
                intent.putExtra("dlist",  dList);
            //    intent.putExtra("type", spType.getSelectedItem().toString());
            //    intent.putExtra("status", spStatus.getSelectedItem().toString());

                 startActivityForResult(intent,0);

            }
        });


         loadApi();
        return view;
    }



    public void onResume() {
        super.onResume();
        getActivity().setTitle("وظائف شاغرة");
    }


    private void showForm(){
        TransitionManager.beginDelayedTransition(containerr);
        formContainer.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
        imgWifi.setVisibility(View.GONE);

    }

    private void showLoading(){
        TransitionManager.beginDelayedTransition(containerr);
        formContainer.setVisibility(View.GONE);
        imgWifi.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }

    public boolean isOnline() {


        try {
            ConnectivityManager cm =
                    (ConnectivityManager) getActivity()
                            .getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null &&
                    cm.getActiveNetworkInfo().isConnectedOrConnecting();


        } catch (Exception e) { return false; }
    }
    private void showWifi(){
        TransitionManager.beginDelayedTransition(containerr);
        formContainer.setVisibility(View.GONE);

        imgWifi.setVisibility(View.VISIBLE);
    }
    private void loadApi(){

        boolean IsValid = isOnline();
        if (!IsValid) {
            showWifi();
            return;
        }


        tokenManager = TokenManager.getInstance(this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            //finish();
        }

        showLoading();
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        call = service.getShowParaJob();
        call.enqueue(new Callback<ShowParaJob>() {
            @Override
            public void onResponse(Call<ShowParaJob> call, Response<ShowParaJob> response) {
                if(response.isSuccessful()){

                    ShowParaJob lstShowPara = response.body();

                    setData(lstShowPara.getCity(),lstShowPara.getDomain());
                    showForm();
                }else{
                    Log.e(TAG," Response Error "+String.valueOf(response.code()));
                    showWifi();
                }
            }

            @Override
            public void onFailure(Call<ShowParaJob> call, Throwable t) {
                Log.e(TAG," Response Error "+t.getMessage());
                showWifi();
            }
        });
    }

    private void setData(List<City> c , List<Domain> d){


        String[] domainArray = new String[d.size()+1];
        domainArray[0] = "كل المجالات";
        int pos = 1;
        for (int i = 0; i < d.size(); i++)
        {
            domainArray[pos] = d.get(i).getDomain_name();
            pos++;
        }
        dList = domainArray;
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(con, android.R.layout.simple_spinner_item, domainArray);
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spDomain.setAdapter(spinnerArrayAdapter1);

        String[] cityArray = new String[c.size()+1];
        cityArray[0] = "كل المدن";
        pos = 1;
        for (int i = 0; i < c.size(); i++)
        {
            cityArray[pos] = c.get(i).getCityName();
            pos++;
        }
        cList = cityArray;

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(con, android.R.layout.simple_spinner_item, cityArray);
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spCity.setAdapter(spinnerArrayAdapter2);



    }




}
