package libyacvpro.libya_cv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "BroadcastReceiver caught conditional SMS: ", Toast.LENGTH_LONG).show();

        Intent background = new Intent(context, FCMCallbackService.class);
        context.startService(background);
    }

}