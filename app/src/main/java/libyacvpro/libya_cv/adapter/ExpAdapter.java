package libyacvpro.libya_cv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import libyacvpro.libya_cv.R;

public class ExpAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> ArrFirst;
    ArrayList<String> ArrSecond;




    public ExpAdapter(
            Context context2,
            ArrayList<String> first,
            ArrayList<String> second


    )
    {

        this.context = context2;
        this.ArrFirst = first;
        this.ArrSecond = second;


    }

    public int getCount() {
        // TODO Auto-generated method stub
        return ArrFirst.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int position, View child, ViewGroup parent) {

        Holder holder;

        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.list_skills, null);

            holder = new Holder();

            holder.lblFirst = (TextView) child.findViewById(R.id.lblFirst);
            holder.lblSecond = (TextView) child.findViewById(R.id.lblSecond);



            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
        holder.lblFirst.setText(ArrFirst.get(position));
        holder.lblSecond.setText(ArrSecond.get(position));




        return child;
    }

    public class Holder {
        TextView lblFirst;
        TextView lblSecond;


    }
}
