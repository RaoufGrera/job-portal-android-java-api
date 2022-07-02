package libyacvpro.libya_cv;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import libyacvpro.libya_cv.adapter.TabAdapter;

public class Main3Activity extends Fragment {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public final String TAG = "Main3Activity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.activity_main3,null);

        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getChildFragmentManager());
        adapter.addFragment(new SearchFragment(), "وظائف");
        adapter.addFragment(new SearchCVFragment(), "سير");
        adapter.addFragment(new ServicesFragment(), "خدمات");

        adapter.addFragment(new SearchCompanyFragment(), "شركات");




        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager
                .addOnPageChangeListener (new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        // TODO Auto-generated method stub


                        switch (position){
                            case 0:
                                getActivity().setTitle("وظائف");
                                break;
                            case 1:
                                getActivity().setTitle("سير ذاتية");
                                break;
                            case 2:
                                getActivity().setTitle("خدمات");
                                break;
                            case 3:
                                getActivity().setTitle("شركات");
                                break;
                        }

                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onPageScrollStateChanged(int pos) {
                        // TODO Auto-generated method stub

                    }
                });
        tabLayout.getTabAt(0).setIcon(  R.drawable.ic_jobss);
        tabLayout.getTabAt(1).setIcon( R.drawable.ic_cvvv);
        tabLayout.getTabAt(2).setIcon( R.drawable.ic_14);
        tabLayout.getTabAt(3).setIcon( R.drawable.ic_compp);

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.DST_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.DST_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.DST_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.DST_IN);
        return v;
    }

}

