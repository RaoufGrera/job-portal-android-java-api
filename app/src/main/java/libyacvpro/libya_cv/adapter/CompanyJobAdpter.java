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

import libyacvpro.libya_cv.AppJobCVActivity;
import libyacvpro.libya_cv.DeleteActivity;
import libyacvpro.libya_cv.R;
import libyacvpro.libya_cv.entities.IntegrString;

import static android.support.v4.app.ActivityCompat.startActivityForResult;


/**
 * Created by Asasna on 9/22/2017.
 */

public class CompanyJobAdpter extends ArrayAdapter<IntegrString> {

    private LayoutInflater lf;
    static Class cc;
    static Context tcc;
    static String name;
    static final int PICK_CONTACT_REQUEST = 0;

    public CompanyJobAdpter(Context context, Class ToContext, String pname, ArrayList<IntegrString> objects) {
        super(context, 0, objects);
        lf = LayoutInflater.from(context);
        cc = ToContext;
        tcc=context;
        name= pname;
     }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = lf.inflate(R.layout.list_company_job, parent, false);
            holder = new ViewHolder();
            holder.btn_Deleteh = (Button) convertView
                    .findViewById(R.id.btn_Delete);
            holder.btnOnlineData = (Button) convertView.findViewById(R.id.btnOnlineData);
            holder.btn_app = (Button) convertView.findViewById(R.id.btn_app);
            holder.tvItem = (TextView) convertView.findViewById(R.id.tvItem);
            holder.tvcount = (TextView) convertView.findViewById(R.id.tvcount);
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
        TextView tvcount;
        Button btnOnlineData;
        Button btn_Deleteh;
        Button btn_app;
        IntegrString mItem;
        int ItemID;

        Context FromContext,ToContext;


        public IntegrString getItem(){
            return mItem;
        }

        public void setData(IntegrString item) {
            mItem = item;
            ItemID = item.getId();


            String be = before(item.getName(), ":");
            String af = after(item.getName(), ":");
            af =  "عدد الطلبات: " + af +" طلب.";
            tvItem.setText(be);
            tvcount.setText(af);


        }

        public void initListeners() {
            btn_Deleteh.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), DeleteActivity.class);
                     intent.putExtra("id",ItemID);
                    intent.putExtra("user", name);
                    /*
                     Bundle b = new Bundle();
                    b.putInt("id",mItem.getId());
                    b.putString("name",name);
                    intent.putExtras(b);
                     */
                    startActivityForResult((Activity)tcc,intent,0,null);

                }
            });
            btnOnlineData.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), cc);
                    intent.putExtra("id",ItemID);
                    intent.putExtra("user", name);

                    startActivityForResult((Activity)tcc,intent,0,null);

                }
            });



            btn_app.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), AppJobCVActivity.class);
                    intent.putExtra("id",ItemID);
                    intent.putExtra("user", name);

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