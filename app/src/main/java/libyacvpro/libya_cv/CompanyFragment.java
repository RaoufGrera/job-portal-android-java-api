package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.util.List;

import butterknife.BindView;
import libyacvpro.libya_cv.entities.CertificatePackage.Certificate;
import libyacvpro.libya_cv.entities.CertificatePackage.CertificateResponse;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;


public class CompanyFragment extends Fragment {


    public final String TAG = "CompanyFragment";

   /* @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;
*/
    Button imgWifi;


    @BindView(R.id.framcontainer)
    FrameLayout containerr;


    @BindView(R.id.form_container)
    RelativeLayout formContainer;

    ProgressBar loader;

    LinearLayout linerControlCompany;
    LinearLayout linerAddCompany;


    Button btnChangeCompany;
    Button btnAddCompany;
     CardView btnEditCompany;
     CardView btn_map;
    CardView btnImage;
    CardView btnJobList;
    List<String> ee;
    ApiService service;
    TokenManager tokenManager;
    Call<List<String>> call;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_company, container, false);
        tokenManager = TokenManager.getInstance(this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            // finish();
        }
        formContainer = (RelativeLayout) rootView.findViewById(R.id.form_container);
        containerr = (FrameLayout) rootView.findViewById(R.id.framcontainer);
        linerControlCompany = (LinearLayout) rootView.findViewById(R.id.linerControlCompany);
        linerAddCompany = (LinearLayout) rootView.findViewById(R.id.linerAddCompany);
        loader = (ProgressBar) rootView.findViewById(R.id.loader);
          btnAddCompany = (Button) rootView.findViewById(R.id.addCompany);
        btnEditCompany = (CardView) rootView.findViewById(R.id.btnEditCompany);
        btnJobList = (CardView) rootView.findViewById(R.id.btnJobList);
        btnChangeCompany = (Button) rootView.findViewById(R.id.btnChangeCompany);

        btnImage = (CardView) rootView.findViewById(R.id.btnImage);
        btn_map = (CardView) rootView.findViewById(R.id.btn_map);
        btnAddCompany.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(getActivity(), AddCompanyActivity.class));}});
        btnImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditImageCompanyActivity.class);
                intent.putExtra("user", btnChangeCompany.getText().toString());
                startActivityForResult(intent,0);
              //  startActivity(new Intent(getActivity(), AddCompanyActivity.class));
            }});
        btnEditCompany.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditCompanyActivity.class);
                intent.putExtra("user", btnChangeCompany.getText().toString());
                startActivityForResult(intent,0);

            }});

        btn_map.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("user", btnChangeCompany.getText().toString());
                startActivityForResult(intent,0);

            }});
        btnJobList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyJobActivity.class);
                intent.putExtra("user", btnChangeCompany.getText().toString());
                startActivityForResult(intent,0);

            }});
        imgWifi = (Button) rootView.findViewById(R.id.imgWifi);

        imgWifi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                apiLoad();

            }});


        registerForContextMenu(btnChangeCompany);

        btnChangeCompany.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                v.showContextMenu();


            }});


        apiLoad();
        return rootView;
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
    public void onResume() {
        super.onResume();
        getActivity().setTitle("إدارة الشركة");
    }

    private void apiLoad(){

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

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
         showLoading();
        call = service.getCompanyList();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){

                   // lvItems = (NonScrollListView) findViewById(R.id.lvItems);
                   ee = response.body();

                   if(ee.get(0) != null) {
                       btnChangeCompany.setText(ee.get(0).toString());
                       showControl();
                   }else{

                       showAdd();
                   }

                  //  showForm();
                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(getContext(), LoginActivity.class));


                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });

    }

    private void showAdd(){

        TransitionManager.beginDelayedTransition(containerr);
        formContainer.setVisibility(View.VISIBLE);

        linerAddCompany.setVisibility(View.VISIBLE);
        linerControlCompany.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
        imgWifi.setVisibility(View.GONE);

    }

    private void showControl(){
         TransitionManager.beginDelayedTransition(containerr);
        formContainer.setVisibility(View.VISIBLE);

        linerControlCompany.setVisibility(View.VISIBLE);
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

}

