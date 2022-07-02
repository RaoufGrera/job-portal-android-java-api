package libyacvpro.libya_cv.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import java.util.List;

import libyacvpro.libya_cv.R;
import libyacvpro.libya_cv.ShowCompanyActivity;
import libyacvpro.libya_cv.entities.CompanyPackage.Company;

import static androidx.core.app.ActivityCompat.startActivityForResult;


/**
 * Created by Asasna on 10/5/2017.
 */

public class CompanyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    public final int AD_TYPE = 2;

    private static final String APP_ID = "ca-app-pub-9929016091047307~2213947061";

    static Context _context;

    static Context context;
    static List<Company> movies;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    /*
    * isLoading - to set the remote loading and complete status to fix back to back load more call
    * isMoreDataAvailable - to set whether more data from server available or not.
    * It will prevent useless load more request even after all the server data loaded
    * */


    public CompanyAdapter(Context context, List<Company> jobses) {
        this.context = context;
        this.movies = jobses;
        this._context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if(viewType == AD_TYPE){
           // return new AdsHolder(inflater.inflate(R.layout.item_adview,parent,false));

        }
        if(viewType==TYPE_MOVIE){


            return new MovieHolder(inflater.inflate(R.layout.row_company,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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
    public int getItemViewType(int position) {
        if(movies.get(position).type.equals("ads"))
            return AD_TYPE;
        if(movies.get(position).type.equals("movie")){
            return TYPE_MOVIE;
        }else{
            return TYPE_LOAD;
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    static class MovieHolder extends RecyclerView.ViewHolder{
        TextView tvName;
       // TextView tvCompany;
        TextView tvCity;
        TextView tvDomain;
        TextView tvSeeIT;
        TextView tvServices;
        ImageView imgView;
        LinearLayout tvItem;



        public MovieHolder(View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.lblCompanyName);
          //  tvCompany=(TextView)itemView.findViewById(R.id.lblComptName);
            tvCity=(TextView)itemView.findViewById(R.id.lblCity);
             tvSeeIT=(TextView)itemView.findViewById(R.id.lblSeeIT);
            tvServices=(TextView)itemView.findViewById(R.id.lblServices);
            tvDomain=(TextView)itemView.findViewById(R.id.lblDomainName);
            tvItem    = (LinearLayout) itemView.findViewById(R.id.lvvIdtems);
            imgView    = (ImageView) itemView.findViewById(R.id.imgCompany);
            tvItem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(v.getContext(), ShowCompanyActivity.class);
                    intent.putExtra("user", movies.get(getAdapterPosition()).getComp_user_name());
                    startActivityForResult((Activity)context,intent,0,null);

                   /* Toast.makeText(v.getContext(),
                            "Online Data Clicked : "+ movies.get(getAdapterPosition()).getDesc_id(), Toast.LENGTH_LONG)
                            .show();*/
                }
            });
//            Picasso.setSingletonInstance(picasso);

        }

        void bindData(Company jobsModel){
            tvName.setText(jobsModel.getComp_name());
          //  tvCompany.setText(jobsModel.getCompt_name());
            tvCity.setText(jobsModel.getCity_name());
            switch(jobsModel.getCity_name()){
                case "طرابلس":
                    tvCity.setBackground(getDrawable(context, R.drawable.text_tripoli));
                    break;
                case "بنغازي":
                    tvCity.setBackground(getDrawable(context, R.drawable.text_bing));
                    break;
                case "مصراتة":
                    tvCity.setBackground(getDrawable(context, R.drawable.text_mis));
                    break;
                default:
                    tvCity.setBackground(getDrawable(context, R.drawable.text_other));
                    break;
            }

            tvDomain.setText(jobsModel.getDomain_name());
             tvSeeIT.setText(jobsModel.getSee_it().toString());



            tvServices.setText(jobsModel.getServices());

            if(jobsModel.getServices()!= null) {
                if (jobsModel.getServices().equals("")) {
                    tvServices.setVisibility(View.GONE);
                } else {
                    tvServices.setVisibility(View.VISIBLE);
                }

            } else {
                tvServices.setVisibility(View.GONE);
            }
       /*     new DownloadImageTask(imgView)
                    .execute(jobsModel.getImage());*/

            Picasso.get().load(jobsModel.getImage()).placeholder( R.drawable.pro)  .into(imgView);


        }

    }
    public static final Drawable getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return _context.getDrawable(id);
        } else {
            return context.getResources().getDrawable(id,null);
        }
    }
    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
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
        this.loadMoreListener = loadMoreListener;
    }

}
