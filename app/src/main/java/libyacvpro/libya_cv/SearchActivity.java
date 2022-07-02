package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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



    Button btnCity;
    Button btnDomain;

    ApiService service;
    TokenManager tokenManager;
    Call<List<Jobs>> call;
    String clist[];
    String dlist[];

    String dPara;
    String cPara;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;
        recyclerView =  findViewById(R.id.recycler_view);
        btnDomain =  findViewById(R.id.btnDomain);
        btnCity =  findViewById(R.id.btnCity);
        jobsList = new ArrayList<>();

        final String stPara = "";//getIntent().getExtras().getString("string");
        String pCity = getIntent().getExtras().getString("city");
        String pDomain = getIntent().getExtras().getString("domain");

        final String cityPara = (pCity.equals("كل المدن")? "" :pCity);
        final String domainPara = (pDomain.equals("كل المجالات")? "" :pDomain);


          dPara = domainPara;
          cPara=cityPara;
          clist  = getIntent().getExtras() .getStringArray("clist");
          dlist  = getIntent().getExtras() .getStringArray("dlist");

       loadApi();


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        recyclerView.setAdapter(adapter);

        //api = ServiceGenerator.createService(MoviesApi.class);
        loadMore(0,stPara,cPara,dPara);//,typePara,statusPara


        registerForContextMenu(btnCity);

        btnCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.showContextMenu();


            }});

        registerForContextMenu(btnDomain);

        btnDomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.showContextMenu();


            }});
    }
    void loadApi(){
        adapter = new JobsAdapter(this, jobsList);
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
        super.onCreateContextMenu(menu, v, menuInfo);
        super.onCreateContextMenu(menu, v, menuInfo);
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
    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()!=""){
            boolean contains = Arrays.asList(clist).contains(item.getTitle().toString());

            if(contains) {
                btnCity.setText(item.getTitle());
                cPara = (item.getTitle().toString().equals("كل المدن")? "" :item.getTitle().toString());
                clear();

                load(1,"",cPara,dPara);//,typePara,statusPara

            }else {
                btnDomain.setText(item.getTitle());
                dPara = (item.getTitle().toString().equals("كل المجالات")? "" :item.getTitle().toString());
                clear();

                load(1,"",cPara,dPara);

            }

        }else{
            return false;
        }
        return true;
    }
    private void load(int index,String stName,String city,String domain){
        jobsList.add(new Jobs("load"));
        adapter.notifyItemInserted(jobsList.size() - 1);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        adapter.setMoreDataAvailable(true);
        if (tokenManager.getToken() == null) {
            startActivity(new Intent(SearchActivity.this, LoginActivity.class));
            finish();
        }
        if(index == 1)
            jobsList.remove(jobsList.size() - 1);
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        call = service.getSearchJobs(index,stName,city,domain);//,type,status
        call.enqueue(new Callback<List<Jobs>>() {
            @Override
            public void onResponse(Call<List<Jobs>> call, Response<List<Jobs>> response) {
                if(response.isSuccessful()){
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.side_bar,menu);
        return true;
    }
    public void clear() {
        int size = jobsList.size();
        jobsList.clear();
        adapter.notifyItemRangeRemoved(0, size);

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
}
