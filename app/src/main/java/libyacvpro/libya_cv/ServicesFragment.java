package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import libyacvpro.libya_cv.adapter.JobsAdapter;
import libyacvpro.libya_cv.entities.JobSearchPackage.Jobs;
import libyacvpro.libya_cv.entities.ShowJobPackage.ShowParaJob;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class SearchFragment extends  Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    List<Jobs> jobsList;
    JobsAdapter adapter;
    //MoviesApi api;
    String TAG = "SearchFragment";
    Context context;

    SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean isVisible;
    private boolean isStarted;
    Button btnCity;
    Button btnDomain;

    ApiService service;
    TokenManager tokenManager;
    Call<List<Jobs>> call;
    Call<ShowParaJob> callPara;
    String clist[];
    String dlist[];

    String dPara;
    String cPara;
     RelativeLayout containerr;


    ConstraintLayout formContainer;
    ProgressBar loader;

    TextView lblInfo;
    Button imgWifi;

   // Context con=null;
  /*  @Override
    public void onStart() {
        super.onStart();
        isStarted = true;

    }

    @Override
    public void onStop() {
        super.onStop();
        isStarted = false;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        con= context;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        context=this.getActivity();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        formContainer =   view.findViewById(R.id.form_container);
        containerr =   view.findViewById(R.id.container);
        loader = (ProgressBar) view.findViewById(R.id.loader);



        imgWifi = (Button) view.findViewById(R.id.imgWifi);
        Drawable leftDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_keyboard_arrow_down_black_24dp);
        imgWifi.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
         recyclerView =  view.findViewById(R.id.recycler_view);
        btnDomain =  view.findViewById(R.id.btnDomain);
        btnCity =  view.findViewById(R.id.btnCity);
        btnCity.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
        btnDomain.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);

        jobsList = new ArrayList<>();

        final String stPara = "";//getIntent().getExtras().getString("string");
        String pCity = "";//getIntent().getExtras().getString("city");
        String pDomain = "";// getIntent().getExtras().getString("domain");

        final String cityPara = (pCity.equals("كل المدن")? "" :pCity);
        final String domainPara = (pDomain.equals("كل المجالات")? "" :pDomain);


        dPara = domainPara;
        cPara=cityPara;
        clist  =  new String[]{"طرابلس", "بنغازي", "مصراتة"};//getIntent().getExtras() .getStringArray("clist");
        dlist  =  new String[]{"هندسة", "تقنية المعلومات", "كل المجالات"};//getIntent().getExtras() .getStringArray("dlist");

        //showLoading();
        //loadApiPara();
        loadApi();


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        recyclerView.setAdapter(adapter);

        //api = ServiceGenerator.createService(MoviesApi.class);
      //  load(1,stPara,cPara,dPara);//,typePara,statusPara


       // registerForContextMenu(btnCity);
        registerForContextMenu(btnCity);
        registerForContextMenu(btnDomain);
        btnCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.showContextMenu();


            }});


        btnDomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.showContextMenu();


            }});

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                loadRecyclerViewData();
            }
        });
       // loadApi();

        return view;
    }

    private void loadRecyclerViewData(){
        mSwipeRefreshLayout.setRefreshing(true);

        //clear();
        loadApiPara();
        load(1,"",cPara,dPara);
        mSwipeRefreshLayout.setRefreshing(false);
    }
    private void loadApiPara(){

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
        callPara = service.getShowParaJob();
        callPara.enqueue(new Callback<ShowParaJob>() {
            @Override
            public void onResponse(Call<ShowParaJob> call, Response<ShowParaJob> response) {
                if(response.isSuccessful()){

                    ShowParaJob lstShowPara = response.body();
                    String[] cityArray = new String[lstShowPara.getCity().size()+1];
                    cityArray[0] = "كل المدن";
                    int pos = 1;
                    for (int i = 0; i < lstShowPara.getCity().size(); i++)
                    {
                        cityArray[pos] = lstShowPara.getCity().get(i).getCityName();
                        pos++;
                    }
                    clist = cityArray;

                    String[] domainArray = new String[lstShowPara.getDomain().size()+1];
                    domainArray[0] = "كل المجالات";
                     pos = 1;
                    for (int i = 0; i < lstShowPara.getDomain().size(); i++)
                    {
                        domainArray[pos] = lstShowPara.getDomain().get(i).getDomain_name();
                        pos++;
                    }
                    dlist = domainArray;

                    registerForContextMenu(btnCity);
                    registerForContextMenu(btnDomain);


                    showForm();
                }else{
                    Log.e(TAG," Response Error "+String.valueOf(response.code()));
                    showWifi();
                }
            }

            @Override
            public void onFailure(Call<ShowParaJob> call, Throwable t) {
                Log.e(TAG," Response Error "+t.getMessage());
               // showWifi();
            }
        });
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
    void loadApi(){
        adapter = new JobsAdapter(getContext(), jobsList);
        adapter.setLoadMoreListener(new JobsAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = (jobsList.size() / 10) +1;

                        loadMore(index,"",cPara,dPara); //                         loadMore(index,stPara,cityPara,domainPara,typePara,statusPara);
                    }
                });
                //Calling loadMore function in Runnable to fix the
                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
            }
        });

    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        if(v.getId() == R.id.btnCity){
            menu.setHeaderTitle("أختر المدينة");
            if(clist.length != 0) {

                for (String strTemp : clist){
                    menu.add(0, v.getId(), 0, strTemp);
                }
            }
        }


        else if(v.getId() == R.id.btnDomain){
            menu.setHeaderTitle("أختر المجال");

            if(dlist.length != 0) {
                for (String strTemp : dlist){
                    menu.add(0, v.getId(), 0, strTemp);
                }

            }
        }

    }
    public void onResume() {
        super.onResume();
        getActivity().setTitle("وظائف شاغرة");
    }

   /* @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        isVisible = visible;
        if (visible) {
            Log.i("Tag", "Reload fragment");
        }
    }*/
    /*@Override
    public boolean onContextItemSelected(MenuItem item){

        if (isVisible && isStarted)
        {
            if (item.getTitle() != "") {
                boolean contains = Arrays.asList(clist).contains(item.getTitle().toString());

                if (contains) {
                    btnCity.setText(item.getTitle());
                    cPara = (item.getTitle().toString().equals("كل المدن") ? "" : item.getTitle().toString());
                    clear();

                    load(1, "", cPara, dPara);//,typePara,statusPara

                } else {
                    btnDomain.setText(item.getTitle());
                    dPara = (item.getTitle().toString().equals("كل المجالات") ? "" : item.getTitle().toString());
                    clear();

                    load(1, "", cPara, dPara);

                }

            } else {
                return false;
            }
            return true;
        }
       return false;
    }*/

    @Override
    public boolean onContextItemSelected(MenuItem item){


            if (item.getTitle() != "") {
                boolean contains = Arrays.asList(clist).contains(item.getTitle().toString());

                if (contains) {
                    btnCity.setText(item.getTitle());
                    cPara = (item.getTitle().toString().equals("كل المدن") ? "" : item.getTitle().toString());
                    clear();

                    load(1, "", cPara, dPara);//,typePara,statusPara

                } else {
                    btnDomain.setText(item.getTitle());
                    dPara = (item.getTitle().toString().equals("كل المجالات") ? "" : item.getTitle().toString());
                    clear();

                    load(1, "", cPara, dPara);

                }

            } else {
                return false;
            }
            return true;

    }
    private void load(int index,String stName,String city,String domain){
        //add loading progress view
        jobsList.add(new Jobs("load"));
        adapter.notifyItemInserted(jobsList.size() - 1);

        adapter.setMoreDataAvailable(true);
        tokenManager = TokenManager.getInstance(this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            //finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        call = service.getSearchJobs(index,stName,city,domain);//,type,status

        if(index == 1)
            jobsList.remove(jobsList.size() - 1);

        call.enqueue(new Callback<List<Jobs>>() {
            @Override
            public void onResponse(Call<List<Jobs>> call, Response<List<Jobs>> response) {
                if(response.isSuccessful()){
                    List<Jobs> result = response.body();

                    int size = jobsList.size();
                    jobsList.clear();
                    adapter.notifyItemRangeRemoved(0, size);
//                    jobsList.remove(jobsList.size() - 1);
                    if (result.size() > 0) {

                            //jobsList.remove(jobsList.size() - 1);
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
                }else{
                    Log.e(TAG," Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Jobs>> call, Throwable t) {
                Log.e(TAG," Response Error "+t.getMessage());
            }
        });
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
    private void loadMore(int index,String stName,String city,String domain){ //,String type,String status


        //add loading progress view
        jobsList.add(new Jobs("load"));
        adapter.notifyItemInserted(jobsList.size() - 1);


        tokenManager = TokenManager.getInstance(this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            //finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        if(index !=0) {
            call = service.getSearchJobs(index, stName, city, domain); //, type, status
            call.enqueue(new Callback<List<Jobs>>() {
                @Override
                public void onResponse(Call<List<Jobs>> call, Response<List<Jobs>> response) {
                    if (response.isSuccessful()) {

                        //remove loading view
                 //       jobsList.remove(jobsList.size() - 1);

                        List<Jobs> result = response.body();
                        if (result.size() > 0) {
                            jobsList.remove(jobsList.size() - 1);
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

    public void clear() {
        int size = jobsList.size();
        jobsList.clear();
        adapter.notifyItemRangeRemoved(0, size);

    }

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }
}
