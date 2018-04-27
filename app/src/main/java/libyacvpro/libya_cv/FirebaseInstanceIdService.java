package libyacvpro.libya_cv;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;

class FCMInitializationService extends FirebaseInstanceIdService {
    private static final String TAG = "FCMInitializationService";
    ApiService service;
    TokenManager tokenManager;
    Call<Message> call;
    @Override
    public void onTokenRefresh() {
        String fcmToken = FirebaseInstanceId.getInstance().getToken();


        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        call = service.postFireBaseToken(fcmToken,"POST");
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Log.w(TAG, "onResponseToken: " + response );

                if(response.isSuccessful()){

                    Toast.makeText(FCMInitializationService.this, "respons: "+ response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(FCMInitializationService.this, LoginActivity.class));
                   // finish();

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });
        Log.d(TAG, "FCM Device Token:" + fcmToken);
        //Save or send FCM registration token


    }
}
