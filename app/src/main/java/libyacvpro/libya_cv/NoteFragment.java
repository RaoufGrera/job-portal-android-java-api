package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.transition.TransitionManager;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import libyacvpro.libya_cv.entities.IntegrString;
import libyacvpro.libya_cv.entities.NotificationEntity;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

import static android.content.Context.MODE_PRIVATE;

public class NoteFragment extends Fragment {
    ApiService service;
    TokenManager tokenManager;
    Call<List<NotificationEntity>> call;
    String TAG = "NoteFragment";
    private ListView lvItems;

     private FrameLayout framcontainer;
     private  LinearLayout formContainer;
     private   ProgressBar loader;

     Button btnInfo;
    Context con=null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        con= context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        lvItems = (ListView) rootView.findViewById(R.id.lvItems);
        framcontainer = (FrameLayout) rootView.findViewById(R.id.framcontainer);
        formContainer = (LinearLayout) rootView.findViewById(R.id.form_container);
        loader = (ProgressBar) rootView.findViewById(R.id.loader);
        btnInfo = (Button) rootView.findViewById(R.id.btnInfo);

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingNoteActivity.class);
                startActivityForResult(intent,0);
            }

        });
        tokenManager = TokenManager.getInstance(this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            // finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        showLoading();

        call = service.getNote();
        call.enqueue(new Callback<List<NotificationEntity>>() {
            @Override
            public void onResponse(Call<List<NotificationEntity>> call, Response<List<NotificationEntity>> response) {
                if(response.isSuccessful()){
                    showForm();
                    List<NotificationEntity> objList = response.body();
                    setData(objList);

                }else{
                    Log.e(TAG," Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<NotificationEntity>> call, Throwable t) {
                Log.e(TAG," Response Error "+t.getMessage());
            }
        });

        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("الإشعارات");
     }

    private void showForm(){
        TransitionManager.beginDelayedTransition(framcontainer);
        formContainer.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
    }

    private void showLoading(){
        TransitionManager.beginDelayedTransition(framcontainer);
        formContainer.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }
    private void setData(List<NotificationEntity> ee){


        ArrayList<IntegrString> myLibrary = new ArrayList<IntegrString>();

        for (int i = 0; i < ee.size(); i++)
        {
            myLibrary.add(new IntegrString(1,ee.get(i).getData()));
        }
        if(ee.size() > 2) {

            NoteAdpter adapter = new NoteAdpter(con, myLibrary);
            lvItems.setAdapter(adapter);
        }

    }

}
