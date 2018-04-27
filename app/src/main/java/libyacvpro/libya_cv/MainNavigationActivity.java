package libyacvpro.libya_cv;

import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import retrofit2.Call;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.network.ApiService;

public class MainNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public final String TAG ="MainNavigationActivity";
    ApiService service;
    TokenManager tokenManager;
    Call<Message> call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_dashboard);

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




        if (getSupportFragmentManager().findFragmentByTag("DashboardFragment") != null) {

              CustomDialogClass cdd=new CustomDialogClass(this);
            cdd.show();
        } else {


            displaySelectedScreen(R.id.nav_dashboard);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

            navigationView.setCheckedItem(R.id.nav_dashboard);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected boolean useToolbar()
    {
        return true;
    }
    private void displaySelectedScreen(int id){
        Fragment fragment = null;

        String tag ="";

        switch (id){
            case R.id.nav_dashboard:
                fragment = new DashboardFragment();
                if (useToolbar())
                {
                    tag="DashboardFragment";
                    //setSupportActionBar(toolbar);
                    setTitle("الرئيسية");
                }
                break;
            case R.id.nav_view_cv:
                fragment = new MycvFargment();
                tag="MycvFargment";

                if (useToolbar())
                {

                    //setSupportActionBar(toolbar);
                    setTitle("السيرة الذاتية");
                }

                break;
            case R.id.nav_search_job:
                fragment = new SearchJobForm();
                tag="SearchJobForm";

                if (useToolbar())
                {
                 //   setSupportActionBar(toolbar);
                    setTitle("وظائف شاغرة");
                }
                break;
            case R.id.nav_search_company:
                fragment = new SearchCompanyFragment();
                tag="SearchCompanyFragment";

                if (useToolbar())
                {
                    //   setSupportActionBar(toolbar);
                    setTitle("الشركات");
                }
                break;

            case R.id.nav_company:
                fragment = new CompanyFragment();
                tag="CompanyFragment";

                if (useToolbar())
                {
                    setTitle("إدارة الشركة");
                }

                break;
            case R.id.nav_setting:
                fragment = new SettingFragment();
                tag="SettingFragment";

                if (useToolbar())
                {
                    //   setSupportActionBar(toolbar);
                    setTitle("الإعدادات");
                }

                break;

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
            case R.id.nav_job_application:
                fragment = new ApplicationFragment();
                tag="ApplicationFragment";

                if (useToolbar())
                {
                    //   setSupportActionBar(toolbar);
                    setTitle("طلبات التوظيف");
                }
                break;

            case R.id.nav_note:
                fragment = new NoteFragment();
                tag="NoteFragment";

                if (useToolbar())
                {
                    //   setSupportActionBar(toolbar);
                    setTitle("الإشعارات");
                }

                break;

            case R.id.nav_logout:
                tokenManager.deleteToken();
                startActivity(new Intent(MainNavigationActivity.this, LoginActivity.class));
                finish();

        }

        if(fragment !=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


            ft.replace(R.id.content_main,fragment,tag);
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
