package libyacvpro.libya_cv;


 //import com.facebook.FacebookSdk;
 import com.facebook.appevents.AppEventsLogger;
 import com.facebook.stetho.Stetho;


 import android.app.Activity;
 import android.app.Application;

 import android.content.Context;
 import android.content.Intent;
 import android.content.res.Configuration;
 import android.content.res.Resources;

 import androidx.multidex.MultiDex;
 import androidx.appcompat.app.AppCompatDelegate;

 import java.util.Locale;

public class MyApp extends Application {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
      //  MultiDex.install(this);
      //  TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "droid.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf

        Resources res = getResources();
        Configuration newConfig = new Configuration( res.getConfiguration() );
        Locale locale = new Locale( "ar" );
        newConfig.locale = locale;

            newConfig.setLayoutDirection( locale );

        res.updateConfiguration( newConfig, null );
        Stetho.initializeWithDefaults(this);

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        // Normal app init code...
        if(!getSharedPreferences("shortCut", Activity.MODE_PRIVATE).getBoolean("shortCut", false)){
            addShortcut();
            getSharedPreferences("shortCut", Activity.MODE_PRIVATE).edit().putBoolean("shortCut", true).apply();
        }
    }



    private void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(),
                MainNavigationActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Libya CV");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.drawable.ic_launcher_foreground));

        addIntent
                .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addIntent.putExtra("duplicate", false);  //may it's already there so don't duplicate
        getApplicationContext().sendBroadcast(addIntent);
    }
}
