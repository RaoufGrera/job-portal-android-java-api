package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import androidx.transition.TransitionManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import libyacvpro.libya_cv.adapter.CompanyJobAdpter;
import libyacvpro.libya_cv.entities.JobSearchPackage.Jobs;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyJobActivity extends AppCompatActivity {
    String TAG = "MyJobActivity";
     Context context;



    String pUser;

    Button imgWifi;
    ApiService service;
    TokenManager tokenManager;
    Call<List<Jobs>> call;

    RecyclerView recyclerView;
    List<Jobs> jobsList;
    CompanyJobAdpter adapter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job);
        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        jobsList = new ArrayList<>();
         pUser = getIntent().getExtras().getString("user");
        Button btnJobList = (Button)  findViewById(R.id.btnNew);

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
                Intent intent = new Intent(MyJobActivity.this, AddJobActivity.class);
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
    }
    private void loadMore(int index){ //,String type,String status


        //add loading progress view
        jobsList.add(new Jobs("load"));
        adapter.notifyItemInserted(jobsList.size() - 1);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(MyJobActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        if(index == 1)
            jobsList.remove(jobsList.size() - 1);

        if(index !=0) {
            call = service.getMyJobs(index);
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



/*
    private void apiLoad(){


        boolean IsValid = isOnline();
        if (!IsValid) {
            showWifi();
            return;
        }
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        if(tokenManager.getToken() == null){
            startActivity(new Intent(MyJobActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        showLoading();
        call = service.getMyJobs(pUser);
        call.enqueue(new Callback<List<Jobs>>() {
            @Override
            public void onResponse(Call<List<Jobs>> call, Response<List<Jobs>> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){

                    lvItems = (RecyclerView) findViewById(R.id.lvItems);
                    List<Jobs> ee = response.body();
                    setData(ee);
                    showForm();

                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(MyJobActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<List<Jobs>> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
                showWifi();
            }
        });

    }
*/



    public boolean isOnline() {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null &&
                    cm.getActiveNetworkInfo().isConnectedOrConnecting();


        } catch (Exception e) { return false; }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data){
       /* if(resultCode == RESULT_OK)
            loadMore(0);*/
        finish();
        startActivity(getIntent());

    }
/*
    private void setData(List<Jobs> ee){




        ArrayList<IntegrString> myLibrary = new ArrayList<IntegrString>();

        for (int i = 0; i < ee.size(); i++)
        {
            myLibrary.add(new IntegrString(ee.get(i).getDesc_id(),ee.get(i).getJob_name()));
        }
        lvItems.setLayoutManager(new LinearLayoutManager(this));

        CompanyJobAdpter adapter = new CompanyJobAdpter(this,AddJobActivity.class, pUser, myLibrary);
        lvItems.setAdapter(adapter);


    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.side_bar,menu);
        return true;
    }

    @OnClick(R.id.btnNew)
    void addNewItem(){
        Intent intent = new Intent(MyJobActivity.this, AddJobActivity.class);
        intent.putExtra("id", 0);
        intent.putExtra("user", pUser);
        startActivityForResult(intent,0);
    }
}
