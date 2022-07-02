package libyacvpro.libya_cv.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import libyacvpro.libya_cv.LoginActivity;
import libyacvpro.libya_cv.R;
import libyacvpro.libya_cv.TokenManager;
import libyacvpro.libya_cv.entities.IntegrString;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.app.ActivityCompat.startActivityForResult;
import static libyacvpro.libya_cv.TokenManager.getInstance;

public class EducationAuthAdapter extends BaseAdapter {
    static Context context;
    ArrayList<String> ArrFirst;
    ArrayList<String> ArrSecond;
    ArrayList<String> ArrThird;
    ArrayList<String> ArrForth ;
    ArrayList<IntegrString> ArrId ;

     Class cc;
     Character name;


    static Call<Message> call;

    public EducationAuthAdapter(
            Context context2,
            Class ToContext,
            Character pname,
            ArrayList<String> first,
            ArrayList<String> second,
            ArrayList<String> third,
            ArrayList<String> forth,
            ArrayList<IntegrString> id

    )
    {

        this.context = context2;
        this.ArrFirst = first;
        this.ArrSecond = second;
        this.ArrThird = third;
        this.ArrForth = forth;
        this.ArrId = id;
        cc = ToContext;
        name= pname;
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
            child = layoutInflater.inflate(R.layout.list_education_auth, null);

            holder = new Holder(cc,name);

            holder.lblFirst = (TextView) child.findViewById(R.id.lblFirst);
            holder.lblSecond = (TextView) child.findViewById(R.id.lblSecond);
            holder.lblThird = (TextView) child.findViewById(R.id.lblThird);
            holder.lblForth = (TextView) child.findViewById(R.id.lblForth);
            holder.imgButton = child.findViewById(R.id.imgButton);
            holder.tvItem    = (LinearLayout) child.findViewById(R.id.tvItem);

            holder.tvvItem    = (RelativeLayout) child.findViewById(R.id.tvvItem);

            holder.init(holder);
            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
        holder.lblFirst.setText(ArrFirst.get(position));
        holder.lblSecond.setText(ArrSecond.get(position));
        holder.lblThird.setText(ArrThird.get(position));
        holder.setData(ArrId.get(position));



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




    public static class Holder {
        Class ccc;
        Character namee;
        public Holder(
                Class ToContext,
                Character pname


        )
        {


            ccc = ToContext;
            namee= pname;
        }

        TextView lblFirst;
        TextView lblSecond;
        TextView lblThird;
        TextView lblForth;
        ImageButton imgButton;
        LinearLayout tvItem ;
        RelativeLayout tvvItem ;

        IntegrString mItem;

        public IntegrString getItem(int position){
            return mItem;
        }

        public void setData(IntegrString item) {
            mItem = item;

        }

        public void init(final Holder holder){
          imgButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View v) {

                    PopupMenu popup = new PopupMenu(v.getContext(), v);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                        public boolean onMenuItemClick(MenuItem item){

                            switch (item.getItemId()) {
                                case R.id.menu_edit:

                                    Intent intent = new Intent(v.getContext(), ccc);
                                    intent.putExtra("id", mItem.getId());
                                    startActivityForResult((Activity)context,intent,0,null);

                                    return true;
                                case R.id.menu_delete:

                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                    builder1.setMessage("هل أنت متأكد من الحذف؟");
                                    builder1.setCancelable(true);

                                    builder1.setPositiveButton(
                                            "نعم",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    final TokenManager tokenManager = getInstance(context.getSharedPreferences("prefs", MODE_PRIVATE));



                                                    ApiService service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);


                                                    switch (namee){
                                                        case 'E':
                                                            call = service.deleteEducation(mItem.getId(),"DELETE");
                                                            break;
                                                        case 'X':
                                                            call = service.deleteExperience(mItem.getId(),"DELETE");
                                                            break;
                                                        case 'L':
                                                            call = service.deleteLanguage(mItem.getId(),"DELETE");
                                                            break;
                                                        case 'S':
                                                            call = service.deleteSpecialty(mItem.getId(),"DELETE");
                                                            break;
                                                        case 'K':
                                                            call = service.deleteSkills(mItem.getId(),"DELETE");
                                                            break;
                                                        case 'I':
                                                            call = service.deleteInfo(mItem.getId(),"DELETE");
                                                            break;
                                                        case 'C':
                                                            call = service.deleteCertificate(mItem.getId(),"DELETE");
                                                            break;
                                                        case 'T':
                                                            call = service.deleteTraining(mItem.getId(),"DELETE");
                                                            break;
                                                        case 'H':
                                                            call = service.deleteHobby(mItem.getId(),"DELETE");
                                                            break;
                                                    }
                                                    call.enqueue(new Callback<Message>() {
                                                        @Override
                                                        public void onResponse(Call<Message> call, Response<Message> response) {

                                                            if(response.isSuccessful()){

                                                                String msg = response.body().getMessage();
                                                                 Toast.makeText(context, msg, Toast.LENGTH_LONG)
                                                                        .show();
                                                                holder.tvItem.setVisibility(View.GONE);
                                                                holder.tvvItem.setVisibility(View.GONE);

                                                            }else {
                                                                tokenManager.deleteToken();

                                                                startActivityForResult((Activity)context,new Intent(context, LoginActivity.class),0,null);

                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Message> call, Throwable t) {

                                                        }
                                                    });
                                                }
                                            });

                                    builder1.setNegativeButton(
                                            "لا",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();


                                    return true;
                                default:
                                    return false;
                            }

                        }
                    });
                    popup.inflate(R.menu.menu_cv);
                    popup.show();
                }

            });
        }




    }
}
