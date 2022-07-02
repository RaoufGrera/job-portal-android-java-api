package libyacvpro.libya_cv.adapter;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import libyacvpro.libya_cv.R;
import libyacvpro.libya_cv.entities.GeneralPackage.Domain;

import static androidx.core.app.ActivityCompat.startActivityForResult;

/**
 * Created by Asasna on 3/21/2018.
 */

public class SettingNoteAdapter extends ArrayAdapter<Domain> {
    private LayoutInflater lf;
    static Class cc;
    static Context tcc;
    public final List<Domain> list;

    static boolean pp = false;
    static final int PICK_CONTACT_REQUEST = 0;
//tvcItem
    public SettingNoteAdapter(Context context, List<Domain> list) {
        super(context, 0, list);
        lf = LayoutInflater.from(context);
        this.list = list;
        tcc=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

         ViewHolder holder = null;
        if (convertView == null) {
              convertView = lf.inflate(R.layout.setting_item2, parent, false);
            holder = new  ViewHolder();

            holder.tvImg   = (ImageView) convertView.findViewById(R.id.imgCompany);
            holder.tvItem = (TextView) convertView.findViewById(R.id.tvItem);
            holder.ckbItem = (CheckBox) convertView.findViewById(R.id.checkbox_meat);


      holder.ckbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                        list.get(getPosition).setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
                    }
                });

            convertView.setTag(holder);
             convertView.setTag(R.id.checkbox_meat, holder.ckbItem);




            } else {
            holder = (ViewHolder) convertView.getTag();
            }
        holder.ckbItem.setTag(position); // This line is important.

        holder.tvItem.setText(list.get(position).getDomain_name());
        holder.ckbItem.setChecked(list.get(position).isSelected());

        Picasso.get().load(list.get(position).getImage()).placeholder(AppCompatResources.getDrawable(tcc, R.drawable.pro))  .into(holder.tvImg);

            convertView.setTag(holder);
        /*} else {
            holder = (ViewHolder) convertView.getTag();
        }

      holder.ckbItem.setTag(getItem(position).getDomain_id()); // This line is important.

        setData(getItem(position),holder);*/

        return convertView;
    }
    private void awesomeButtonClicked() {

    }
    public static class ViewHolder {
        TextView tvItem;
        CheckBox ckbItem;
        Domain mItem;
        ImageView tvImg;
        TextView tvcItem;
        Context FromContext,ToContext;


        public Domain getItem(){
            return mItem;
        }





    }
    public void setData(Domain item,final  ViewHolder hold) {
      hold.mItem = item;

        String be =  hold.mItem.getDomain_name() ;
        boolean af = hold.mItem.isSelected();

        hold.tvItem.setText(be);
        Picasso.get().load(hold.mItem.getImage()).placeholder( R.drawable.pro)  .into(hold.tvImg);

        if (hold.ckbItem.isChecked())
            hold.ckbItem.setChecked(true);
        else
            hold.ckbItem.setChecked(false);
        /*if (af.equals("0")) {
            ckbItem.setChecked(false);
        } else {
            ckbItem.setChecked(true);
        }*/


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
