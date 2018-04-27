package libyacvpro.libya_cv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import libyacvpro.libya_cv.entities.IntegrString;

import java.util.ArrayList;

import static android.support.v4.app.ActivityCompat.startActivityForResult;


/**
 * Created by Asasna on 9/22/2017.
 */

public class CustomAdpter extends ArrayAdapter<IntegrString> {

    private LayoutInflater lf;
    static Class cc;
    static Context tcc;
    static Character name;
    static final int PICK_CONTACT_REQUEST = 0;

    public CustomAdpter(Context context,Class ToContext,Character pname, ArrayList<IntegrString> objects) {
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
            convertView = lf.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.btn_Deleteh = (Button) convertView
                    .findViewById(R.id.btn_Delete);
            holder.btnOnlineData = (Button) convertView
                    .findViewById(R.id.btnOnlineData);
            holder.tvItem = (TextView) convertView.findViewById(R.id.tvItem);
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
        Button btn_Deleteh;
        IntegrString mItem;

        Context FromContext,ToContext;


        public IntegrString getItem(){
            return mItem;
        }

        public void setData(IntegrString item) {
            mItem = item;
            tvItem.setText(item.getName());
        }

        public void initListeners() {
            btn_Deleteh.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), DeleteActivity.class);
                     intent.putExtra("id", mItem.getId());
                    intent.putExtra("name", name);
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
                    intent.putExtra("id", mItem.getId());
                    startActivityForResult((Activity)tcc,intent,0,null);

                }
            });


        }

    }

}