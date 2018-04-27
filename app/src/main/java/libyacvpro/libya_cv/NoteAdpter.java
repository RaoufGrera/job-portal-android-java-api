package libyacvpro.libya_cv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import libyacvpro.libya_cv.entities.IntegrString;

/**
 * Created by Asasna on 1/7/2018.
 */

public class NoteAdpter extends ArrayAdapter<IntegrString> {

private LayoutInflater lf;
static Class cc;
static Context tcc;
static Character name;
static final int PICK_CONTACT_REQUEST = 0;

public NoteAdpter(Context context, ArrayList<IntegrString> objects) {
        super(context, 0, objects);
        lf = LayoutInflater.from(context);
         tcc=context;
        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
        convertView = lf.inflate(R.layout.list_note, parent, false);
        holder = new ViewHolder();

        holder.tvItem = (TextView) convertView.findViewById(R.id.tvItem);
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




    }

}


