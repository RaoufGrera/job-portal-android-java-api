package libyacvpro.libya_cv.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

import libyacvpro.libya_cv.R;
import libyacvpro.libya_cv.SearchCVFragment;
import libyacvpro.libya_cv.SearchFragment;

import static libyacvpro.libya_cv.TabFragment.int_items;

public class ChartsAdapter extends FragmentPagerAdapter {

    Drawable myDrawable;
    String title ="";
    static Context _context;
    public ChartsAdapter(FragmentManager fm,Context context){
        super(fm);
        this._context= context;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SearchFragment();
            case 1:
                return  new SearchCVFragment();


        }
        return null;
    }

    @Override
    public int getCount() {
        return int_items;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                myDrawable = getDrawable(_context, R.drawable.ic_job_search_1);
                title =" ";
                break;
            case 1:

                myDrawable = getDrawable(_context, R.drawable.ic_search_in_malebox);
                title = " ";

                break;



        }
        SpannableStringBuilder sb = new SpannableStringBuilder("   " + title); // space added before text for convenience
        try {
            myDrawable.setBounds(5, 0, myDrawable.getIntrinsicWidth(), myDrawable.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(myDrawable, DynamicDrawableSpan.ALIGN_BOTTOM);
            sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if(title.equals(""))
                return null;
        } catch (Exception e) {
            return null;
        }

        return sb;

        //return null;
    }

    public static final Drawable getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return _context.getDrawable(id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }
}