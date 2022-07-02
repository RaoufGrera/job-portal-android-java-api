package libyacvpro.libya_cv;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import me.leolin.shortcutbadger.ShortcutBadger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.google.firebase.iid.FirebaseInstanceIdService; //Commented FirebaseInstanceIdService

/**
 * Created by Asasna on 1/8/2018.
 */

public class FCMCallbackService extends FirebaseMessagingService {
    private static final String TAG = "FCMCallbackService";
    ApiService service;
    TokenManager tokenManager;
    Call<Message> call;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        Log.d(TAG, "Message Notification is :xxxxxxxxx");

        //"Title","Message","NotyType",   "hotelStatus"

        String title = "Libya CV";
      /*  if (remoteMessage.getNotification().getTitle() != null){
            title = remoteMessage.getNotification().getTitle();
        }*/

        String message = "";
       /* if (remoteMessage.getNotification().getBody() != null){
            message = remoteMessage.getNotification().getBody();
        }*/
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        String send ="";
        String id ="";

        Map<String, String> data = remoteMessage.getData();
        if (data != null) {

            if (data.containsKey("title")) {
                title = data.get("title");
                message = data.get("body");
            }
            if (data.containsKey("send")) {
                send = data.get("send");
                id = data.get("id");
            }
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " +id+" " +send);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

// Read previous value. If not found, use 0 as default value.
        int count = prefs.getInt("count_key", 0);

// because you need to show the badge again, it means you need
// to increment the count
        count = count + 1;

// Then apply it
        ShortcutBadger.applyCount(this.getApplicationContext(), count);

// After that, you need to save the value again for another badge count
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("count_key", count);
        editor.apply();


      /*  boolean success = ShortcutBadger.applyCount(getApplicationContext(), count);
        Log.d(TAG, "Message Notification is : " + success);*/

        sendNotification(title, message,send,id);

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private   void sendNotification(String title, String message,String send,String id) {

        Intent intent = new Intent();

        if(send.equals("job")){
            if(id != null) {
                int foo = Integer.parseInt(id);
                intent = new Intent(this,JobActivity.class);

                intent.putExtra("id", foo);
            }else{ intent = new Intent(this,MainNavigationActivity.class);}

        }else{
            intent = new Intent(this,MainNavigationActivity.class);

        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_jobs)
                //.setLargeIcon(R.drawable.ic_logolibyacv)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(getRequestCode(this.getApplicationContext()), notificationBuilder.build());


    }

    private  int getRequestCode(Context context) {
        Random rnd = new Random();
     //   Toast.makeText(context, "BroadcastReceiver caught conditional SMS: ", Toast.LENGTH_LONG).show();

        return 100 + rnd.nextInt(900000);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        final String[] fcmToken = new String[1];
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                         String token = task.getResult();
                         fcmToken[0] =token;

                    }
                });

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        call = service.postFireBaseToken(fcmToken[0],"POST");
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                //    Log.w(TAG, "onResponseToken: " + response );

                if(response.isSuccessful()){

                    Toast.makeText(FCMCallbackService.this, "respons: "+ response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(FCMCallbackService.this, LoginActivity.class));
                    // finish();

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                //     Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }
}

