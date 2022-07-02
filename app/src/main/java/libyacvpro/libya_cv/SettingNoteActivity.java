package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.TransitionManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.OnClick;
import libyacvpro.libya_cv.adapter.SettingNoteAdapter;
import libyacvpro.libya_cv.entities.GeneralPackage.Domain;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class SettingNoteActivity extends AppCompatActivity {
    String TAG = "SettingNoteActivity";

    private ListView lvItems;
    SettingNoteAdapter adapter;

    //  final Context context = getContext();
    Context c =null;

    ApiService service;
    TokenManager tokenManager;
    Call<List<Domain>> call;

     ConstraintLayout _container;



    Button button4;

    Button imgWifi;
     Call<Message> callMessage;
    Integer[] arr ;

     ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_note);
        // Inflate the layout for this fragment
         lvItems = (ListView) findViewById(R.id.lvItems);
        _container= findViewById(R.id.container);
        imgWifi= findViewById(R.id.imgWifi);
        loader= findViewById(R.id.loader);
        tokenManager = TokenManager.getInstance(this.getSharedPreferences("prefs", MODE_PRIVATE));
         ;

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(SettingNoteActivity.this, LoginActivity.class));}

        button4 = (Button) findViewById(R.id.button4);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arr = new Integer[adapter.list.size()];

                for (int i = 0; i < adapter.list.size(); i++){
                    if(adapter.list.get(i).isSelected()) {

                        Integer rr = adapter.list.get(i).getDomain_id();
                        arr[i] =rr;


                    }
                }

                callMessage = service.postNote(arr,"POST");



        callMessage.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> callSave, Response<Message> response) {
                    Log.w(TAG, "onResponse: " + response );

                    if(response.isSuccessful()){

                        String msg = response.body().getMessage();
                        Context context =SettingNoteActivity.this;
                        Toast.makeText(context, msg, Toast.LENGTH_LONG)
                                .show();
                        /*setResult(RESULT_OK, null);
                        finish();*/



                    }else {
                        tokenManager.deleteToken();
                        startActivity(new Intent(SettingNoteActivity.this, LoginActivity.class));
                      //  finish();

                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage() );
                }
            });

        }
        });
        apiLoad();


    }
    @OnClick(R.id.imgWifi)
    void refreshActivity(){
        apiLoad();

    }

    private void showWifi(){
        TransitionManager.beginDelayedTransition(_container);

        lvItems.setVisibility(View.GONE);
        button4.setVisibility(View.GONE);
        //txt.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
        imgWifi.setVisibility(View.VISIBLE);
    }
    public boolean isOnline() {


        try {
            ConnectivityManager cm =
                    (ConnectivityManager) this
                            .getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null &&
                    cm.getActiveNetworkInfo().isConnectedOrConnecting();


        } catch (Exception e) { return false; }
    }

  /*  public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.side_bar,menu);
        return true;
    }*/

   /* @Override
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
*/
    private void showForm(){
        TransitionManager.beginDelayedTransition(_container);
        lvItems.setVisibility(View.VISIBLE);
        button4.setVisibility(View.VISIBLE);
        imgWifi.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
    }

    private void showLoading(){
        TransitionManager.beginDelayedTransition(_container);
        lvItems.setVisibility(View.GONE);
        button4.setVisibility(View.GONE);
        imgWifi.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }
    private void apiLoad() {
        boolean IsValid = isOnline();
        if (!IsValid) {
            showWifi();
            return;
        }
        tokenManager = TokenManager.getInstance(this.getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(SettingNoteActivity.this, LoginActivity.class));
            //finish();
        }
        showLoading();

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        call = service.listNote();
        call.enqueue(new Callback<List<Domain>>() {
            @Override
            public void onResponse(Call<List<Domain>> call, Response<List<Domain>> response) {
                if (response.isSuccessful()) {

                    List<Domain> ee = response.body();
                    if(ee.size() > 0)
                        setData(ee);
                    showForm();
                } else {
                    Log.e(TAG, " Response Error " + String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Domain>> call, Throwable t) {
                Log.e(TAG, " Response Error " + t.getMessage());
            }
        });
    }

    private void setData(List<Domain> ee){

              adapter = new SettingNoteAdapter(SettingNoteActivity.this,  ee);
            lvItems.setAdapter(adapter);

    }

}
