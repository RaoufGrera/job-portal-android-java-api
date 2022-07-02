package libyacvpro.libya_cv.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import libyacvpro.libya_cv.AddJobActivity;
import libyacvpro.libya_cv.AppJobCVActivity;
import libyacvpro.libya_cv.DeleteActivity;
import libyacvpro.libya_cv.ItemClickListener;
import libyacvpro.libya_cv.R;
import libyacvpro.libya_cv.TokenManager;
import libyacvpro.libya_cv.entities.IntegrString;
import libyacvpro.libya_cv.entities.JobSearchPackage.Jobs;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.network.ApiService;
import libyacvpro.libya_cv.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static libyacvpro.libya_cv.TokenManager.getInstance;


/**
 * Created by Asasna on 9/22/2017.
 */

public class CompanyJobAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    static Context context;
    static Context tcc;
    static String name;
    static List<Jobs> movies;

    private  View.OnClickListener mOnClickListener ;

    CompanyJobAdpter.OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    public CompanyJobAdpter(Context context,String pname, List<Jobs> jobses) {
        this.context = context;
        this.tcc = context;
        this.movies = jobses;
        this.name = pname;
      }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);


        if(viewType==TYPE_MOVIE){


            return new MovieHolder(inflater.inflate(R.layout.list_company_job,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }
    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if(getItemViewType(position)==TYPE_MOVIE){
            ((MovieHolder)holder).bindData(movies.get(position));
        }


        //No else part needed as load holder doesn't bind any data
    }
    @Override
    public int getItemCount() {
        return movies.size();
    }
    @Override
    public int getItemViewType(int position) {

        if(movies.get(position).type.equals("movie")){

            return TYPE_MOVIE;
        }else{
            return TYPE_LOAD;
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener =  loadMoreListener;
    }
    static class MovieHolder extends RecyclerView.ViewHolder {
        TextView tvName;
      //  TextView tvCount;
        Integer descID;

        Button btnOnlineData;
        Button btn_Deleteh;
        Button btn_app;

        RelativeLayout RItem;




        public MovieHolder(View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.jobName);
        //    tvCount=(TextView)itemView.findViewById(R.id.jobCount);

             btn_Deleteh = (Button) itemView
                    .findViewById(R.id.btn_Delete);
            btnOnlineData = (Button) itemView.findViewById(R.id.btnOnlineData);
          //  btn_app = (Button) itemView.findViewById(R.id.btn_app);

            RItem = (RelativeLayout) itemView.findViewById(R.id.RItem);
            btn_Deleteh.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(tcc);
                    builder1.setMessage("هل أنت متأكد من حذف الوظيفة؟");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "نعم",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    TokenManager   tokenManager = getInstance(tcc.getSharedPreferences("prefs", MODE_PRIVATE));



                                    ApiService  service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
                                    Call<Message>   callMessage = service.removeJob( name,descID,"DELETE" );
                                    callMessage.enqueue(new Callback<Message>() {
                                        @Override
                                        public void onResponse(Call<Message> call, Response<Message> response) {
                                            if (response.isSuccessful()) {

                                                String msg = response.body().getMessage();
                                                Toast.makeText(tcc, msg, Toast.LENGTH_LONG).show();

                                                RItem.setVisibility(View.GONE);

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

                    builder1.setNegativeButton(
                            "لا",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();


                }
            });
            btn_Deleteh.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(tcc);
                    builder1.setMessage("هل أنت متأكد من حذف الوظيفة؟");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "نعم",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    TokenManager   tokenManager = getInstance(tcc.getSharedPreferences("prefs", MODE_PRIVATE));



                                    ApiService  service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
                                    Call<Message>   callMessage = service.removeJob( name,descID,"DELETE" );
                                    callMessage.enqueue(new Callback<Message>() {
                                        @Override
                                        public void onResponse(Call<Message> call, Response<Message> response) {
                                            if (response.isSuccessful()) {

                                                String msg = response.body().getMessage();
                                                Toast.makeText(tcc, msg, Toast.LENGTH_LONG).show();

                                                RItem.setVisibility(View.GONE);

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

                    builder1.setNegativeButton(
                            "لا",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();


                }
            });

            btnOnlineData.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), AddJobActivity.class);
                    intent.putExtra("id",descID);
                    intent.putExtra("user", name);

                    startActivityForResult((Activity)tcc,intent,0,null);

                }
            });



            /*btn_app.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), AppJobCVActivity.class);
                    intent.putExtra("id",descID);
                    intent.putExtra("user", name);

                    startActivityForResult((Activity)tcc,intent,0,null);

                }
            });*/
        }

        void bindData(Jobs jobsModel){
            tvName.setText(jobsModel.getJob_name());
            descID = jobsModel.getDesc_id();

         //   tvCount.setText(jobsModel.getSee_it().toString());



        }



    }


}