package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.Random;

import libyacvpro.libya_cv.entities.CompanyPackage.Company;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.app.ActivityCompat.startActivityForResult;

public class DashboardFragment extends Fragment {


     public final String TAG = "DashboardFragment";
    TokenManager tokenManager;

    SliderLayout sliderLayout;
    ApiService service;
    Call<Message> call;
    Call<Company> callWe;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);


        tokenManager = TokenManager.getInstance(this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken() == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            // finish();
        }
        /*sliderLayout = rootView.findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.FILL); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(1); //set scroll delay in seconds :

        setSliderViews();*/

              CardView btn_company= (CardView) rootView.findViewById(R.id.btn_company);
        btn_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment SearchFra =  new SearchCVFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (useToolbar())
                {
                    navigationView.setCheckedItem(R.id.nav_company);

                }
                ft.replace(R.id.content_main,SearchFra);
                ft.commit();


            }
        });




        CardView b = (CardView) rootView.findViewById(R.id.btn_movepage);
        CardView btn_note = (CardView) rootView.findViewById(R.id.btn_note);
        CardView btn_search = (CardView) rootView.findViewById(R.id.btn_search);
       CardView btn_search_company = (CardView) rootView.findViewById(R.id.btn_search_company);
        CardView btn_setting = (CardView) rootView.findViewById(R.id.btn_setting);

        CardView btn_services = (CardView) rootView.findViewById(R.id.btn_services);
        CardView btn_ser = (CardView) rootView.findViewById(R.id.btn_ser);

        //CardView btn_app = (CardView) rootView.findViewById(R.id.btn_app);
    //    CardView btn_comp = (CardView) rootView.findViewById(R.id.btn_comp);
         CardView btn_search_cv = (CardView) rootView.findViewById(R.id.btn_search_cv);



        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String company = prefs.getString("company","1");
        String ser = prefs.getString("ser","1");
        String cv = prefs.getString("cv","1");
        String job = prefs.getString("job","1");
        TextView txtCompany = (TextView) rootView.findViewById(R.id.badge_company);
        TextView txtCv = (TextView) rootView.findViewById(R.id.badge_cv);
        TextView txtJob = (TextView) rootView.findViewById(R.id.badge_job);
        TextView txtSer = (TextView) rootView.findViewById(R.id.badge_ser);


        txtCompany.setText(company);
        txtSer.setText(ser);

        txtCv.setText(cv);
        txtJob.setText(job);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try{
                    loadApi();
                    String company = prefs.getString("company","1");
                    String ser = prefs.getString("ser","1");
                    String cv = prefs.getString("cv","1");
                    String job = prefs.getString("job","1");
                    TextView txtCompany = (TextView) rootView.findViewById(R.id.badge_company);
                    TextView txtCv = (TextView) rootView.findViewById(R.id.badge_cv);
                    TextView txtJob = (TextView) rootView.findViewById(R.id.badge_job);
                    TextView txtSer = (TextView) rootView.findViewById(R.id.badge_ser);


                    txtCompany.setText(company);
                    txtSer.setText(ser);

                    txtCv.setText(cv);
                    txtJob.setText(job);

                    int ii =  new Random().nextInt(85) + 15;

                  /*  Toast.makeText(getContext() ,ii, Toast.LENGTH_LONG)
                            .show();*/
                }

                catch (Exception e) {
                    // TODO: handle exception
                }
                finally{
                    //also call the same runnable to call it at regular interval
                    //handler.postDelayed(this, 20000);
                }
            }
        };
        handler.postDelayed(runnable, 800000);






        btn_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   Intent intent = new Intent(v.getContext(), NoteFragment.class);

             //   startActivity(intent);


              /*  Fragment Note =  new SettingNoteActivity();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (useToolbar())
                {
                    navigationView.setCheckedItem(R.id.nav_note);

                }
                ft.replace(R.id.content_main,Note);
                ft.commit();
*/

               /* int badgeCount = 0;
                try {
                    badgeCount = Integer.parseInt(numInput.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Error input", Toast.LENGTH_SHORT).show();
                }

                boolean success = ShortcutBadger.applyCount(MainActivity.this, badgeCount);

                Toast.makeText(getApplicationContext(), "Set count=" + badgeCount + ", success=" + success, Toast.LENGTH_SHORT).show();*/

            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment SearchFra =  new SearchFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (useToolbar())
                {
                    navigationView.setCheckedItem(R.id.nav_search_job);

                }
                ft.replace(R.id.content_main,SearchFra);
                ft.commit();


            }
        });

        btn_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment SearchFra =  new ServicesFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (useToolbar())
                {
                    navigationView.setCheckedItem(R.id.nav_search_ser);

                }
                ft.replace(R.id.content_main,SearchFra);
                ft.commit();


            }
        });

        btn_ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), MyServicesActivity.class);

                startActivity(intent);
                /*Fragment SearchFra =  new MyServicesActivity();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (useToolbar())
                {
                    navigationView.setCheckedItem(R.id.nav_search_job);

                }
                ft.replace(R.id.content_main,SearchFra);
                ft.commit();
*/

            }
        });

         btn_search_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment SearchFra =  new SearchCompanyFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (useToolbar())
                {
                     navigationView.setCheckedItem(R.id.nav_search_company);

                }
                ft.replace(R.id.content_main,SearchFra);
                ft.commit();


            }
        });

         btn_search_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               //  startActivity(new Intent(getActivity(), Main3Activity.class));

                Fragment SearchFra =  new Main3Activity();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (useToolbar())
                {
                  navigationView.setCheckedItem(R.id.nav_search_cv);

                }
                ft.replace(R.id.content_main,SearchFra);
                ft.commit();


            }
        });

      /*  btn_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment app =  new ApplicationFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (useToolbar())
                {
                    navigationView.setCheckedItem(R.id.nav_job_application);

                }
                ft.replace(R.id.content_main,app);
                ft.commit();


            }
        });
*/
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment setting =  new SettingFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (useToolbar())
                {
                    navigationView.setCheckedItem(R.id.nav_setting);

                }
                ft.replace(R.id.content_main,setting);
                ft.commit();


            }
        });
        /*btn_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment compFra =  new AddJobActivity();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (useToolbar())
                {
                    navigationView.setCheckedItem(R.id.nav_company);

                }
                ft.replace(R.id.content_main,compFra);
                ft.commit();


            }
        });*/

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Fragment cv =  new MycvFargment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (useToolbar())
                {
                    navigationView.setCheckedItem(R.id.nav_view_cv);

                }
                ft.replace(R.id.content_main,cv);
                ft.commit();*/            }
        });

        return rootView;
    }
    protected boolean useToolbar()
    {
        return true;
    }

    private void setSliderViews() {

        for (int i = 0; i <= 3; i++) {

            SliderView sliderView = new SliderView(getContext());

            switch (i) {
                case 0:
                    sliderView.setImageUrl("https://images.pexels.com/photos/547114/pexels-photo-547114.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
                case 1:
                    sliderView.setImageUrl("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
                case 2:
                    sliderView.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
                    break;
                case 3:
                    sliderView.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setDescription("setDescription " + (i + 1));
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(getContext(), "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }


    public boolean isOnline() {


        try {
            ConnectivityManager cm =
                    (ConnectivityManager) getActivity()
                            .getSystemService(Context.CONNECTIVITY_SERVICE);

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
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( getContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("company", objEdu.getAbout());
                    editor.putString("ser", objEdu.getServices());
                    editor.putString("cv", objEdu.getAddress());
                    editor.putString("job", objEdu.getPhone());

                    editor.apply();
                    Toast.makeText(getContext(), objEdu.getAbout(), Toast.LENGTH_LONG)
                            .show();


                } else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(getContext(), LoginActivity.class));


                }
            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

}
