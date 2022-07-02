package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.TransitionManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import libyacvpro.libya_cv.adapter.CompanyJobAdpter;
import libyacvpro.libya_cv.entities.CompanyPackage.Company;
import libyacvpro.libya_cv.entities.CompanyPackage.CompanyForEdit;
import libyacvpro.libya_cv.entities.JobSearchPackage.Jobs;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class CompanyFragment extends AppCompatActivity {


    public final String TAG = "CompanyFragment";

   /* @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;
*/
    Call<List<Jobs>> callJobs;

    RecyclerView recyclerView;
    List<Jobs> jobsList;
    CompanyJobAdpter adapter;


    TextView lblDomain,lblCity,lblEmail,lblPhone,lblWeb,lblFace,lblDesc,lblName,lblSeeIT;

    Button imgWifi;
    String pUser;



    @BindView(R.id.container)
    ConstraintLayout containerr;

    Context context;
    @BindView(R.id.form_container)
    LinearLayout formContainer;

    ProgressBar loader;

  //  LinearLayout linerControlCompany;
  RelativeLayout linerAddCompany;

    @BindView(R.id.imgCompany)
    ImageView imgCompany;

    Button btnChangeCompany;
    Button btnAddCompany;
    Button btnEditCompany;
     CardView btn_map;
    Button btnImage;
    CardView btnJobList;
    List<String> ee;
    ApiService service;
    TokenManager tokenManager;
    Call<CompanyForEdit> call;


     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_company);

         tokenManager = TokenManager.getInstance(this.getSharedPreferences("prefs", MODE_PRIVATE));

       // setHasOptionsMenu(true);inflater


        if (tokenManager.getToken() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            // finish();
        }

//jobs **********
        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        jobsList = new ArrayList<>();
        pUser = getIntent().getExtras().getString("user");
        Button btnJobList = (Button)  findViewById(R.id.btnNew);
        lblDomain =   findViewById(R.id.lblDomain);
        lblCity =   findViewById(R.id.lblCity);
        lblPhone =   findViewById(R.id.lblPhone);
        lblEmail =   findViewById(R.id.lblEmail);
        lblWeb =   findViewById(R.id.lblWeb);
        lblFace =   findViewById(R.id.lblFace);
        lblDesc =   findViewById(R.id.lblDesc);
        lblName =   findViewById(R.id.lblName);
        lblSeeIT =   findViewById(R.id.lblSeeIT);

        containerr =   findViewById(R.id.container);
        imgCompany =   findViewById(R.id.imgCompany);

        adapter = new CompanyJobAdpter(this,pUser, jobsList);
        adapter.setLoadMoreListener(new CompanyJobAdpter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = (jobsList.size() / 10) +1;
                        loadMore(index); //                         loadMore(index,stPara,cityPara,domainPara,typePara,statusPara);
                    }
                });
                //Calling loadMore function in Runnable to fix the
                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
            }
        });

        btnJobList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyFragment.this, AddJobActivity.class);
                intent.putExtra("id", 0);
                intent.putExtra("user", pUser);
                startActivityForResult(intent,0);

            }});



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        recyclerView.setAdapter(adapter);

        //api = ServiceGenerator.createService(MoviesApi.class);
        loadMore(0);//,typePara,statusPara
        //*******
       /* MobileAds.initialize(this, APP_ID);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-9929016091047307/3212815535");



        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/


        formContainer =   findViewById(R.id.form_container);
          linerAddCompany = (RelativeLayout) findViewById(R.id.linerAddCompany);
        loader = (ProgressBar) findViewById(R.id.loader);
          btnAddCompany = (Button) findViewById(R.id.addCompany);
        btnEditCompany =   findViewById(R.id.btnEditCompany);


        btnImage =    findViewById(R.id.btnImage);
         btnAddCompany.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {startActivityForResult(new Intent(CompanyFragment.this, AddCompanyActivity.class),0);}});
        btnImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyFragment.this, EditImageCompanyActivity.class);

                startActivityForResult(intent,0);
              //  startActivity(new Intent(getActivity(), AddCompanyActivity.class));
            }});
        btnEditCompany.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyFragment.this, EditCompanyActivity.class);

                startActivityForResult(intent,0);

            }});

       /* btn_map.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyFragment.this, MapsActivity.class);
                intent.putExtra("user", btnChangeCompany.getText().toString());
                startActivityForResult(intent,0);

            }});*/
    /*    btnJobList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyFragment.this, MyJobActivity.class);
                intent.putExtra("user", btnChangeCompany.getText().toString());
                startActivityForResult(intent,0);

            }});*/
        imgWifi = (Button) findViewById(R.id.imgWifi);

        imgWifi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                apiLoad();

            }});


     /*   registerForContextMenu(btnChangeCompany);

        btnChangeCompany.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                v.showContextMenu();


            }});
*/



        apiLoad();

    }



    private void loadMore(int index){ //,String type,String status


        //add loading progress view
        jobsList.add(new Jobs("load"));
        adapter.notifyItemInserted(jobsList.size() - 1);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(CompanyFragment.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        if(index == 1)
            jobsList.remove(jobsList.size() - 1);

        if(index !=0) {
            callJobs = service.getMyJobs(index);
            callJobs.enqueue(new Callback<List<Jobs>>() {
                @Override
                public void onResponse(Call<List<Jobs>> call, Response<List<Jobs>> response) {
                    if (response.isSuccessful()) {

                        //remove loading view
                        jobsList.remove(jobsList.size() - 1);

                        List<Jobs> result = response.body();
                        if (result.size() > 0) {

                            jobsList.addAll(result);
                            if(result.size() < 10){
                                adapter.setMoreDataAvailable(false);
                                Toast.makeText(context, "أنتهت نتائج البحث.", Toast.LENGTH_LONG).show();

                            }
                        } else {//result size 0 means there is no more data available at server
                            adapter.setMoreDataAvailable(false);
                            //telling adapter to stop calling load more as no more server data available
                            Toast.makeText(context, "أنتهت نتائج البحث.", Toast.LENGTH_LONG).show();
                        }
                        adapter.notifyDataChanged();
                        //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                    } else {
                        Log.e(TAG, " Load More Response Error " + String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<List<Jobs>> call, Throwable t) {
                    Log.e(TAG, " Load More Response Error " + t.getMessage());
                }
            });
        }
    }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("أختر الشركة");

        if(ee.get(0) != null) {

            for (int i = 0; i < ee.size(); i++) {
                menu.add(0, v.getId(), 0, ee.get(i).toString());
            }
        }

    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
         if(item.getTitle()!=""){
            btnChangeCompany.setText(item.getTitle());

        }else{
            return false;
        }
        return true;
    }


    private void apiLoad(){

         boolean IsValid = isOnline();
        if (!IsValid) {
            showWifi();
            return;
        }
        tokenManager = TokenManager.getInstance(this.getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(CompanyFragment.this, LoginActivity.class));
            //finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
         showLoading();
        call = service.getCompanyInfo();
        call.enqueue(new Callback<CompanyForEdit>() {
            @Override
            public void onResponse(Call<CompanyForEdit> call, Response<CompanyForEdit> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){

                   // lvItems = (NonScrollListView) findViewById(R.id.lvItems);
                    Company objCompany = response.body().getCompany();

                    if(objCompany != null){
                        //ff
                        setDataInfo(objCompany);
                        showControl();
                    } else{

                       showAdd();
                   }

                  //  showForm();
                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(CompanyFragment.this, LoginActivity.class));


                }
            }

            @Override
            public void onFailure(Call<CompanyForEdit> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });

    }
    private void setDataInfo(Company ii){
        lblCity.setText(ii.getCity_name());
        lblDomain.setText(ii.getDomain_name());
        lblDomain.setText(ii.getDomain_name());
        //  txtCompanyUserName.setText(ii.getComp_user_name()) ;
        lblPhone.setText(ii.getPhone());
        lblFace.setText(ii.getFacebook());
        //   txtTwitter.setText(ii.getTwitter());
        //  txtLinkedin.setText(ii.getLinkedin());
        String g ="";

        if(ii.getUrl() != null ){

            g= ii.getUrl();

        }else{
            g="";
        }

        if(g.length() > 18) {
            g = g.substring(0, 18);
            g=g+"...";
        }

        lblWeb.setText(g);
        lblEmail.setText(ii.getEmail());
        lblDesc.setText(ii.getServices());
        lblName.setText(ii.getComp_name());
        lblSeeIT.setText(ii.getSee_it());
        Picasso.get().load(ii.getImage())  .into(imgCompany);

        // contact_form_title.setText(ii.getComp_name());
    }
    private void showAdd(){

        TransitionManager.beginDelayedTransition(containerr);
        formContainer.setVisibility(View.GONE);

        linerAddCompany.setVisibility(View.VISIBLE);
      //  linerControlCompany.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
        imgWifi.setVisibility(View.GONE);

    }

    private void showControl(){
         TransitionManager.beginDelayedTransition(containerr);
        formContainer.setVisibility(View.VISIBLE);

     //   linerControlCompany.setVisibility(View.VISIBLE);
        linerAddCompany.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
        imgWifi.setVisibility(View.GONE);
    }
/*
    private void showForm(){
        TransitionManager.beginDelayedTransition(containerr);
        formContainer.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
        imgWifi.setVisibility(View.GONE);

    }
*/
    private void showLoading(){
        TransitionManager.beginDelayedTransition(containerr);
        formContainer.setVisibility(View.GONE);
        imgWifi.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }


    public boolean isOnline() {


        try {
            ConnectivityManager cm =
                    (ConnectivityManager) CompanyFragment.this
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


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data){
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK)
            apiLoad();

    }

    @Override
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
    @OnClick(R.id.btnNew)
    void addNewItem(){
        Intent intent = new Intent(CompanyFragment.this, AddJobActivity.class);
        intent.putExtra("id", 0);
        intent.putExtra("user", pUser);
        startActivityForResult(intent,0);
    }

}

