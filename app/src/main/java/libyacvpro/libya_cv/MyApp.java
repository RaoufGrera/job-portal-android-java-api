package libyacvpro.libya_cv;


 //import com.facebook.stetho.Stetho;
 //import com.facebook.FacebookSdk;
 import com.facebook.FacebookSdk;
 import com.facebook.appevents.AppEventsLogger;
 import com.facebook.stetho.Stetho;
 import com.google.android.gms.ads.MobileAds;

 import android.app.Application;

 import android.content.Context;
 import android.content.res.Configuration;
 import android.content.res.Resources;
 import android.os.Build;
 import android.support.multidex.MultiDex;

 import java.util.Locale;

public class MyApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
     //   MultiDex.install(this);

        Resources res = getResources();
        Configuration newConfig = new Configuration( res.getConfiguration() );
        Locale locale = new Locale( "ar" );
        newConfig.locale = locale;
        if (Build.VERSION.SDK_INT >= 17) {
            newConfig.setLayoutDirection( locale );
        }
        res.updateConfiguration( newConfig, null );
        Stetho.initializeWithDefaults(this);

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        // Normal app init code...

    }
}
