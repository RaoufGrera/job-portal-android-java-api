package libyacvpro.libya_cv.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import libyacvpro.libya_cv.CustomAdpter;
import libyacvpro.libya_cv.DeleteActivity;
import libyacvpro.libya_cv.JobActivity;
import libyacvpro.libya_cv.R;
import libyacvpro.libya_cv.entities.IntegrString;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Asasna on 3/21/2018.
 */

public class AppAdapter  extends ArrayAdapter<IntegrString> {
    private LayoutInflater lf;
    static Class cc;
    static Context tcc;
    static final int PICK_CONTACT_REQUEST = 0;
//tvcItem
    public AppAdapter(Context context,Class ToContext, ArrayList<IntegrString> objects) {
        super(context, 0, objects);
        lf = LayoutInflater.from(context);
        cc = ToContext;
        tcc=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

         ViewHolder holder = null;
        if (convertView == null) {
            convertView = lf.inflate(R.layout.item_app, parent, false);
            holder = new  ViewHolder();

            holder.btnOnlineData = (Button) convertView
                    .findViewById(R.id.btnOnlineData);
            holder.tvItem = (TextView) convertView.findViewById(R.id.tvItem);

            holder.tvcItem =(TextView)   convertView.findViewById(R.id.tvcItem);
            holder.initListeners();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setData(getItem(position));

        return convertView;
    }

    public static class ViewHolder {
        TextView tvItem;
        Button btnOnlineData;
        IntegrString mItem;
        TextView tvcItem;
        Context FromContext,ToContext;


        public IntegrString getItem(){
            return mItem;
        }

        public void setData(IntegrString item) {
            mItem = item;

            String be = before(item.getName(), ":");
            String af = after(item.getName(), ":");
            af =  "الحالة: " + af ;
            tvItem.setText(be);
            tvcItem.setText(af);

        }

        public void initListeners() {

            btnOnlineData.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), JobActivity.class);
                    intent.putExtra("id", mItem.getId());
                    startActivityForResult((Activity)tcc,intent,0,null);

                }
            });


        }

    }


    static String before(String value, String a) {
        // Return substring containing all characters before a string.
        int posA = value.indexOf(a);
        if (posA == -1) {
            return "";
        }
        return value.substring(0, posA);
    }
    static String after(String value, String a) {
        // Returns a substring containing all characters after a string.
        int posA = value.lastIndexOf(a);
        if (posA == -1) {
            return "";
        }
        int adjustedPosA = posA + a.length();
        if (adjustedPosA >= value.length()) {
            return "";
        }
        return value.substring(adjustedPosA);
    }
}
