package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;


import com.google.firebase.analytics.FirebaseAnalytics;


import libyacvpro.libya_cv.entities.CompanyPackage.Company;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import me.leolin.shortcutbadger.ShortcutBadger;
import retrofit2.Call;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.network.ApiService;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class MainNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    public final String TAG = "MainNavigationActivity";
    ApiService service;
    TokenManager tokenManager;
    Call<Message> call;
    Call<Company> callWe;

    private FirebaseAnalytics analytics;
    Fragment fragment = null;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new TabFragment();
                    break;
                case R.id.navigation_dashboard:
                    //fragment = new DashboardFragment();
                    break;
                case R.id.navigation_notifications:
                    //fragment = new MycvFargment();
                    break;
            }
            return loadFragment(fragment);

        }
    };
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }*/
        String send = "";
        String id = "";
        Bundle extras = getIntent().getExtras();
        Intent intent = new Intent();
        if (extras != null) {

            if (extras.containsKey("send")) {
                send = extras.getString("send");
                if(send != null) {
                    if (send.equals("job")) {
                        if (extras.containsKey("id")) {
                            id = extras.getString("id");
                            int foo = Integer.parseInt(id);
                            intent = new Intent(this,JobActivity.class);

                            intent.putExtra("id", foo);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }


        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("count_key", 0);
        editor.apply();
       // getSharedPreferences("count_key", Activity.MODE_PRIVATE).edit().putInt("count_key", 0).apply();

        ShortcutBadger.removeCount(this);
        boolean isLogin = false ;
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        if (tokenManager.getToken().getAccessToken() == null) {
            // getFireBaseToken();
            isLogin=true;
            startActivity(new Intent(MainNavigationActivity.this, LoginActivity.class));
            finish();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
         displaySelectedScreen(R.id.nav_dashboard);
        //loadFragment(new TabFragment());
     //   Drawable d = Drawable.createFromStream(getAssets().open(path_in_assets), null);

        //getting bottom navigation view and attaching the listener

      //  BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
      //  navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
     /*  if(!isLogin && !getSharedPreferences("showNote", Activity.MODE_PRIVATE).getBoolean("showNote", false)){
            getSharedPreferences("showNote", Activity.MODE_PRIVATE).edit().putBoolean("showNote", true).apply();

            intent = new Intent(this,SettingNoteActivity.class);

             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);

          //  getSharedPreferences("showNote", Activity.MODE_PRIVATE).edit().putBoolean("showNote", true).apply();
        }*/

     /*   final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try{*/
                    loadApi();
            /*    }
                catch (Exception e) {
                    // TODO: handle exception
                }
                finally{
                    //also call the same runnable to call it at regular interval
                    handler.postDelayed(this, 20000);
                }
            }
        };
        handler.postDelayed(runnable, 20000);*/
    }




    public boolean isOnline() {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null &&
                    cm.getActiveNetworkInfo().isConnectedOrConnecting();


        } catch (Exception e) { return false; }
    }

    public void loadApi(){
        boolean IsValid = isOnline();
        if (!IsValid) {



            return;
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);



        callWe = service.we();



        callWe.enqueue(new Callback<Company>() {
            @Override
            public void onResponse(Call<Company> call, Response<Company> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    Company objEdu = response.body();
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainNavigationActivity.this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("company", objEdu.getAbout());
                    editor.putString("ser", objEdu.getServices());
                    editor.putString("cv", objEdu.getAddress());
                    editor.putString("job", objEdu.getPhone());

                    editor.apply();
                    /*Toast.makeText(MainNavigationActivity.this, objEdu.getAbout(), Toast.LENGTH_LONG)
                            .show();*/


                } else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(MainNavigationActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    /*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
*/

    @Override
    public void onBackPressed() {


        if (getSupportFragmentManager().findFragmentByTag("Main3Activity") != null) {

            CustomDialogClass cdd = new CustomDialogClass(this);
            cdd.show();
        } else {


            displaySelectedScreen(R.id.nav_dashboard);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

            navigationView.setCheckedItem(R.id.nav_dashboard);

        }

    }
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navigation, menu);
       // getMenuInflater().inflate(R.menu.note_bar, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();
        Fragment fragment = null;

       /* if (id == R.id.toolnote) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            fragment = new SettingNoteActivity();
            String tag = "SettingNoteActivity";

            if (useToolbar()) {
                //   setSupportActionBar(toolbar);
                setTitle("الإشعارات");
            }
            ft.replace(R.id.content_main, fragment, tag);
            ft.commit();
            return true;
        }
        /*if (id == R.id.toolsearch) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            fragment = new SearchFragment();
            String tag = "SearchFragment";

            if (useToolbar()) {
                //   setSupportActionBar(toolbar);
                setTitle("وظائف شاغرة");
            }
            ft.replace(R.id.content_main, fragment, tag);
            ft.commit();

            return true;
        }

        if (id == R.id.toolprofile) {

         *//*   Intent intent = new Intent(this, SeekerActivity.class);
            startActivityForResult(intent,0);*//*

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            fragment = new MycvFargment();
            String tag = "MycvFargment";

            if (useToolbar()) {
                //   setSupportActionBar(toolbar);
                setTitle("السيرة الذاتية");
            }
            ft.replace(R.id.content_main, fragment, tag);
            ft.commit();


            return true;
        }/


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about_us) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);


            return true;
        }
        if (id == R.id.action_settings) {

            fragment = new SettingFragment();
            String tag = "SettingFragment";

            if (useToolbar()) {
                //   setSupportActionBar(toolbar);
                setTitle("الإعدادات");
            }
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


                ft.replace(R.id.content_main, fragment, tag);
                ft.commit();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);


            return true;
        }

        if (id == R.id.action_rate) {
            Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
            }
        }

        if (id == R.id.action_contact_us) {
            Uri data = Uri.parse("mailto:?subject=" + "موضوع الرسالة" + "&body=" + "محتوي الرسالة" + "&to=" + "info@libyacv.com");

            Intent goToMarket = new Intent(Intent.ACTION_VIEW, data);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                String msg = "لم يتم تنفيذ العملية بنجاح";
                Context context = getApplicationContext();
                Toast.makeText(context, msg, Toast.LENGTH_LONG)
                        .show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
*/
    protected boolean useToolbar() {
        return true;
    }

    private void displaySelectedScreen(int id) {
        Fragment fragment = null;

        String tag = "";

        switch (id) {
            case R.id.nav_dashboard:
                fragment = new Main3Activity();
                if (useToolbar()) {
                    tag = "Main3Activity";
                    //setSupportActionBar(toolbar);
                   // setTitle("وظائف");
                }
                break;
            case R.id.nav_view_cv:

                Intent intent = new Intent(getApplicationContext(), MycvFargment.class);


                startActivity(intent);


                break;

            case R.id.nav_privacy:

                startActivity(new Intent(getApplicationContext(), privacy.class));

                break;
          /*  case R.id.nav_search_job:
                fragment = new SearchFragment();
                tag = "SearchFragment";

                if (useToolbar()) {
                    //   setSupportActionBar(toolbar);
                    setTitle("وظائف شاغرة");
                }
                break;
                case R.id.nav_search_cv:
                fragment = new SearchCVFragment();
                tag = "SearchCVFragment";

                if (useToolbar()) {
                    //   setSupportActionBar(toolbar);
                    setTitle("السير الذاتية");
                }
                break;
             case R.id.nav_search_company:
                fragment = new SearchCompanyFragment();
                tag = "SearchCompanyFragment";

                if (useToolbar()) {
                    //   setSupportActionBar(toolbar);
                    setTitle("الشركات");
                }
                break;

*/
              case R.id.nav_company:
                  Intent intentc = new Intent(getApplicationContext(), CompanyFragment.class);


                  startActivity(intentc);


                break;
       /*     case R.id.nav_setting:
                fragment = new SettingFragment();
                tag = "SettingFragment";

                if (useToolbar()) {
                    //   setSupportActionBar(toolbar);
                    setTitle("الإعدادات");
                }

                break;*/
          case R.id.nav_search_ser:


              Intent intents = new Intent(getApplicationContext(), MyServicesActivity.class);


              startActivity(intents);
                break;
  /*
            case R.id.nav_rate:
                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }


                break;
            /*case R.id.nav_job_application:
                fragment = new ApplicationFragment();
                tag = "ApplicationFragment";

                if (useToolbar()) {
                    //   setSupportActionBar(toolbar);
                    setTitle("طلبات التوظيف");
                }
                break;*/

            case R.id.nav_note:

                Intent intentt = new Intent(getApplicationContext(), SettingNoteActivity.class);


                startActivity(intentt);



                break;

            case R.id.nav_logout:
                tokenManager.deleteToken();
                Intent intenttt = new Intent(getApplicationContext(), LoginActivity.class);

                intenttt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finishAffinity();
                startActivity(intenttt);
                //  finish();

        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


            ft.replace(R.id.content_main, fragment, tag);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);
        return true;

       /* if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/


    }
}
