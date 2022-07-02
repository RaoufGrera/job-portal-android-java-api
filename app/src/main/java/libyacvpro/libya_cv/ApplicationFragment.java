package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import libyacvpro.libya_cv.adapter.AppAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import libyacvpro.libya_cv.entities.IntegrString;
import libyacvpro.libya_cv.entities.JobSearchPackage.Jobs;
import libyacvpro.libya_cv.entities.JobSearchPackage.JobsResponse;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

import static android.content.Context.MODE_PRIVATE;


public class ApplicationFragment extends Fragment {
    String TAG = "ApplicationFragment";
    private ListView lvItems;


  //  final Context context = getContext();
    Context c =null;

    ApiService service;
    TokenManager tokenManager;
    Call<JobsResponse> call;

    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application, container, false);
        lvItems = (ListView) view.findViewById(R.id.lvItems);

        apiLoad();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        c= context;
    }
    public void onResume() {
        super.onResume();
        getActivity().setTitle("طلبات التوظيف");

    }

    private void apiLoad() {
        ButterKnife.bind(getActivity());
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            //finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
         call = service.listmyjob();
        call.enqueue(new Callback<JobsResponse>() {
            @Override
            public void onResponse(Call<JobsResponse> call, Response<JobsResponse> response) {
                if (response.isSuccessful()) {

                     List<Jobs> ee = response.body().getJobsArray();
                     if(ee.size() > 0)
                       setData(ee);

                } else {
                    Log.e(TAG, " Response Error " + String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<JobsResponse> call, Throwable t) {
                Log.e(TAG, " Response Error " + t.getMessage());
            }
        });
    }

    private void setData(List<Jobs> ee){


        ArrayList<IntegrString> myLibrary = new ArrayList<IntegrString>();

        for (int i = 0; i < ee.size(); i++)
        {
            myLibrary.add(new IntegrString(ee.get(i).getDesc_id(),ee.get(i).getJob_name()));
        }

        if(myLibrary.size() > 0) {
            AppAdapter adapter = new AppAdapter(c, JobActivity.class, myLibrary);
            lvItems.setAdapter(adapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CustomAdpter.ViewHolder holder = (CustomAdpter.ViewHolder) view.getTag();
                IntegrString item = holder.getItem();
            }
        });
        }
    }




}
