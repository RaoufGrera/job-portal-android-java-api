package libyacvpro.libya_cv;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import static android.content.Context.MODE_PRIVATE;

public class DashboardFragment extends Fragment {

    TokenManager tokenManager;


    public final String TAG = "DashboardFragment";


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


        CardView b = (CardView) rootView.findViewById(R.id.btn_movepage);
        CardView btn_note = (CardView) rootView.findViewById(R.id.btn_note);
        CardView btn_search = (CardView) rootView.findViewById(R.id.btn_search);
        CardView btn_search_company = (CardView) rootView.findViewById(R.id.btn_search_company);
        CardView btn_setting = (CardView) rootView.findViewById(R.id.btn_setting);
        CardView btn_app = (CardView) rootView.findViewById(R.id.btn_app);
        CardView btn_comp = (CardView) rootView.findViewById(R.id.btn_comp);

        btn_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment Note =  new NoteFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (useToolbar())
                {
                    navigationView.setCheckedItem(R.id.nav_note);

                }
                ft.replace(R.id.content_main,Note);
                ft.commit();


            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment SearchFra =  new SearchJobForm();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (useToolbar())
                {
                    navigationView.setCheckedItem(R.id.nav_search_job);

                }
                ft.replace(R.id.content_main,SearchFra);
                ft.commit();


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

        btn_app.setOnClickListener(new View.OnClickListener() {
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
        btn_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment setting =  new SettingFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (useToolbar())
                {
                    navigationView.setCheckedItem(R.id.nav_company);

                }
                ft.replace(R.id.content_main,setting);
                ft.commit();


            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment cv =  new MycvFargment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                if (useToolbar())
                {
                    navigationView.setCheckedItem(R.id.nav_view_cv);

                }
                ft.replace(R.id.content_main,cv);
                ft.commit();            }
        });

        return rootView;
    }
    protected boolean useToolbar()
    {
        return true;
    }




}
