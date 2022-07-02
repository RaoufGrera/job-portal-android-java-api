package libyacvpro.libya_cv.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import libyacvpro.libya_cv.JobActivity;
import libyacvpro.libya_cv.R;
import libyacvpro.libya_cv.entities.IntegrString;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class EducationAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> ArrFirst;
    ArrayList<String> ArrSecond;
    ArrayList<String> ArrThird;
    ArrayList<String> ArrForth ;



    public EducationAdapter(
            Context context2,
            ArrayList<String> first,
            ArrayList<String> second,
            ArrayList<String> third,
            ArrayList<String> forth

    )
    {

        this.context = context2;
        this.ArrFirst = first;
        this.ArrSecond = second;
        this.ArrThird = third;
        this.ArrForth = forth;

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
            child = layoutInflater.inflate(R.layout.list_education, null);

            holder = new Holder();

            holder.lblFirst = (TextView) child.findViewById(R.id.lblFirst);
            holder.lblSecond = (TextView) child.findViewById(R.id.lblSecond);
            holder.lblThird = (TextView) child.findViewById(R.id.lblThird);
            holder.lblForth = (TextView) child.findViewById(R.id.lblForth);


            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
        holder.lblFirst.setText(ArrFirst.get(position));
        holder.lblSecond.setText(ArrSecond.get(position));
        holder.lblThird.setText(ArrThird.get(position));

        if(ArrSecond.get(position).equals(""))
            holder.lblSecond.setVisibility(View.GONE);
        else
            holder.lblSecond.setText(ArrSecond.get(position));


        if(ArrThird.get(position).equals(""))
            holder.lblThird.setVisibility(View.GONE);
        else
            holder.lblThird.setText(ArrThird.get(position));

        if(ArrForth.get(position).equals(""))
            holder.lblForth.setVisibility(View.GONE);
        else
            holder.lblForth.setText(ArrForth.get(position));



        return child;
    }

    public class Holder {
        TextView lblFirst;
        TextView lblSecond;
        TextView lblThird;
        TextView lblForth;

    }
}
