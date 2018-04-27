package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import libyacvpro.libya_cv.adapter.JobsAdapter;
import libyacvpro.libya_cv.entities.JobSearchPackage.Jobs;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Jobs> jobsList;
    JobsAdapter adapter;
    //MoviesApi api;
    String TAG = "SearchActivity";
    Context context;





    ApiService service;
    TokenManager tokenManager;
    Call<List<Jobs>> call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        jobsList = new ArrayList<>();

        final String stPara = "";//getIntent().getExtras().getString("string");
        String pCity = getIntent().getExtras().getString("city");
        String pDomain = getIntent().getExtras().getString("domain");

        final String cityPara = (pCity.equals("كل المدن")? "" :pCity);
        final String domainPara = (pDomain.equals("كل المجالات")? "" :pDomain);


        adapter = new JobsAdapter(this, jobsList);
        adapter.setLoadMoreListener(new JobsAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                         int index = (jobsList.size() / 10) +1;
                         loadMore(index,stPara,cityPara,domainPara); //                         loadMore(index,stPara,cityPara,domainPara,typePara,statusPara);
                    }
                });
                //Calling loadMore function in Runnable to fix the
                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
            }
        });



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        recyclerView.setAdapter(adapter);

        //api = ServiceGenerator.createService(MoviesApi.class);
        loadMore(0,stPara,cityPara,domainPara);//,typePara,statusPara
    }

    private void load(int index,String stName,String city,String domain,String type,String status){
        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(SearchActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        call = service.getSearchJobs(index,stName,city,domain);//,type,status
        call.enqueue(new Callback<List<Jobs>>() {
            @Override
            public void onResponse(Call<List<Jobs>> call, Response<List<Jobs>> response) {
                if(response.isSuccessful()){
                    List<Jobs> result = response.body();

                    if(result.size()>0){
                        //add loaded data
                        jobsList.addAll(result);
                        adapter.notifyDataChanged();

                    }else{//result size 0 means there is no more data available at server
                        adapter.setMoreDataAvailable(false);
                        //telling adapter to stop calling load more as no more server data available
                        Toast.makeText(context,"أنتهت نتائج البحث.",Toast.LENGTH_LONG).show();
                    }
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

    private void loadMore(int index,String stName,String city,String domain){ //,String type,String status


            //add loading progress view
            jobsList.add(new Jobs("load"));
            adapter.notifyItemInserted(jobsList.size() - 1);

            ButterKnife.bind(this);
            tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

            if (tokenManager.getToken() == null) {
                startActivity(new Intent(SearchActivity.this, LoginActivity.class));
                finish();
            }

            service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        if(index == 1)
            jobsList.remove(jobsList.size() - 1);

        if(index !=0) {
            call = service.getSearchJobs(index, stName, city, domain); //, type, status
            call.enqueue(new Callback<List<Jobs>>() {
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
}
