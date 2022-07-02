package libyacvpro.libya_cv.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import libyacvpro.libya_cv.CVActivity;
import libyacvpro.libya_cv.R;
import libyacvpro.libya_cv.TokenManager;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.app.ActivityCompat.startActivityForResult;
import static libyacvpro.libya_cv.TokenManager.getInstance;


/**
 * Created by Asasna on 10/5/2017.
 */

public class AppCvAdapter extends BaseAdapter {

    ApiService service;
    TokenManager tokenManager;
    Call<Message> callMessage;
     int job_id;
    Context context;
    ArrayList<String> userID;
    ArrayList<String> tvName;
    ArrayList<String> tvDomain;
    ArrayList<String> tvCity ;
    ArrayList<String> tvEdt ;
    ArrayList<String> tvExp ;
    ArrayList<String> tvMathc ;
    ArrayList<String> tvimgView ;
    ArrayList<String> tvAbout ;
    ArrayList<Integer> req_event ;


    public AppCvAdapter(
            Context context2,
            int job_id,
            ArrayList<String> id,
            ArrayList<String> name,
            ArrayList<String> domain,
            ArrayList<String> city,
            ArrayList<String> edt,
            ArrayList<String> exp,
            ArrayList<String> mathc,
            ArrayList<String> img,
            ArrayList<String> about,
            ArrayList<Integer> req_event
    )
    {

        this.context = context2;
        this.job_id = job_id;

        this.userID = id;
        this.tvName = name;
        this.tvDomain = domain;
        this.tvCity = city;
         this.tvEdt = edt;
        this.tvExp = exp;
        this.tvMathc = mathc;
        this.tvimgView = img;
        this.tvAbout = about;
        this.req_event = req_event;
    }
    public int getCount() {
        // TODO Auto-generated method stub
        return userID.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(final int position, View child, ViewGroup parent) {

        final Holder holder;

        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.row_cvs, null);

            holder = new Holder();

             holder.tvName=(TextView)child.findViewById(R.id.lblName);
            holder.tvDomain=(TextView)child.findViewById(R.id.lblDomain);
            holder.tvCity=(TextView)child.findViewById(R.id.lblCity);
            holder.tvEdt=(TextView)child.findViewById(R.id.lblEdtName);
            holder.tvExp=(TextView)child.findViewById(R.id.lblExp);
            holder. tvMathc=(TextView)child.findViewById(R.id.lblMatch);
            holder. tvAbout=(TextView)child.findViewById(R.id.lblAbout);
            holder.tvItem    = (LinearLayout) child.findViewById(R.id.lvvIdtems);
            holder.tvimgView    = (ImageView) child.findViewById(R.id.imgCompany);


            holder.btnAccept    = (Button) child.findViewById(R.id.btnAccept);
            holder.btnDelete    = (Button) child.findViewById(R.id.btnDelete);

           // holder.initListeners();
            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }


        holder.userID = userID.get(position);
        holder.tvName.setText(tvName.get(position));
        holder.tvDomain.setText(tvDomain.get(position));
        holder.tvCity.setText(tvCity.get(position));
        holder.tvEdt.setText(tvEdt.get(position));
        holder.tvExp.setText(tvExp.get(position));
        holder.tvMathc.setText(tvMathc.get(position));



        holder.tvAbout.setText(tvAbout.get(position));

        if(req_event.get(position) == 1) {
            holder.btnAccept.setText("مقبول مبدئيا");
          holder.btnAccept.setEnabled(false);
       //   holder.btnAccept.setBackgroundResource(0);
        }

        Picasso.get().load(tvimgView.get(position)).into(holder.tvimgView);


           holder.tvItem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), CVActivity.class);
                    intent.putExtra("seeker_id", holder.userID);


                    startActivityForResult((Activity)context,intent,0,null);

                }
            });

            holder. btnDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if( holder.btnAccept.getText().equals("مقبول مبدئيا")){
                 AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("هل أنت متأكد من حذف الباحث؟");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "نعم",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    tokenManager = getInstance(context.getSharedPreferences("prefs", MODE_PRIVATE));



                                    service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
                                    callMessage = service.removeSeeker( job_id, holder.userID ,"");
                                    callMessage.enqueue(new Callback<Message>() {
                                        @Override
                                        public void onResponse(Call<Message> call, Response<Message> response) {
                                            if (response.isSuccessful()) {

                                                String msg = response.body().getMessage();
                                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

                                                holder.tvItem.setVisibility(View.GONE);

                                                //      holder.btnAccept.setBackgroundResource(0);
                                            } else {
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Message> call, Throwable t) {
                                            //  Log.e(TAG, " Response Error " + t.getMessage());
                                        }
                                    });
                               // }
                            }});

                  builder1.setNegativeButton(
                            "لا",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();}else{
                        tokenManager = getInstance(context.getSharedPreferences("prefs", MODE_PRIVATE));



                        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
                        callMessage = service.removeSeeker( job_id, holder.userID ,"");
                        callMessage.enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                if (response.isSuccessful()) {

                                    String msg = response.body().getMessage();
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

                                    holder.tvItem.setVisibility(View.GONE);

                                    //      holder.btnAccept.setBackgroundResource(0);
                                }
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                //  Log.e(TAG, " Response Error " + t.getMessage());
                            }
                        });
                    }


                }
            });


        holder. btnAccept.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    tokenManager = getInstance(context.getSharedPreferences("prefs", MODE_PRIVATE));

                    if (tokenManager.getToken() == null) {
                        // startActivity(new Intent(context, LoginActivity.class));
                        //finish();
                    }

                    service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
                    callMessage = service.acceptSeeker( job_id, holder.userID ,"");
                    callMessage.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            if (response.isSuccessful()) {

                                String msg = response.body().getMessage();
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

                                holder.btnAccept.setText("مقبول مبدئيا");
                                holder.btnAccept.setEnabled(false);
                          //      holder.btnAccept.setBackgroundResource(0);
                            } else {
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            //  Log.e(TAG, " Response Error " + t.getMessage());
                        }
                    });

                }
            });



        return child;
    }

    public class Holder {
        TextView tvName;
        String userID;
        TextView tvDomain;
        TextView tvCity;
        TextView tvEdt;
        TextView tvExp;
        TextView tvMathc;
        ImageView tvimgView;
         LinearLayout tvItem;
         TextView tvAbout;

        Button btnDelete;
        Button btnAccept;




    }






}
