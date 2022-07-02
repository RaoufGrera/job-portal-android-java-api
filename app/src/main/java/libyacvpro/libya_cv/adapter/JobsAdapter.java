package libyacvpro.libya_cv.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.appcompat.content.res.AppCompatResources;
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

import libyacvpro.libya_cv.JobActivity;
import libyacvpro.libya_cv.R;
import libyacvpro.libya_cv.entities.JobSearchPackage.Jobs;

import static androidx.core.app.ActivityCompat.startActivityForResult;


public class JobsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    public final int AD_TYPE = 2;

    private static final String APP_ID = "ca-app-pub-9929016091047307~2213947061";


    static Context context;
     static Context _context;
    static List<Jobs> movies;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;


    public JobsAdapter(Context context, List<Jobs> jobses) {
        this.context = context;
        this._context = context;
        this.movies = jobses;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);


        if(viewType == AD_TYPE){
            return new AdsHolder(inflater.inflate(R.layout.item_adview,parent,false));

        }

        if(viewType==TYPE_MOVIE){


            return new MovieHolder(inflater.inflate(R.layout.row_jobs,parent,false));
        }
        if(viewType==TYPE_LOAD){
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
        return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));

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
        if(movies.get(position).type.equals("load")){
            return TYPE_LOAD;
        }
        if(movies.get(position).type.equals("ads"))
            return AD_TYPE;
        if(movies.get(position).type.equals("movie")){

            return TYPE_MOVIE;
        }

        return TYPE_LOAD;

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }



    /* VIEW HOLDERS */
    static class AdsHolder extends RecyclerView.ViewHolder{

        private AdView mAdView;

        public AdsHolder(View itemView) {
            super(itemView);
         //   MobileAds.initialize(context, APP_ID);

            AdView adView = new AdView(context);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId("ca-app-pub-9929016091047307/7651484578");

            mAdView = (AdView) itemView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
    }
    static class MovieHolder extends RecyclerView.ViewHolder  {
        TextView tvName;
        TextView tvCompany;
        TextView tvCity;
        TextView tvDomain;
        TextView tvDate;
        TextView tvSeeIT;
       // TextView tvJob_desc;
        ImageView imgView;
        LinearLayout tvItem;

        Drawable leftDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_favorite_black_14dp);
        Drawable leftDrawablecity = AppCompatResources.getDrawable(context, R.drawable.ic_place_white_18dp);

        public MovieHolder(View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.lblJobName);
            tvCity=(TextView)itemView.findViewById(R.id.lblCity);
         //   tvDomain=(TextView)itemView.findViewById(R.id.lblDomain);
            tvDate=(TextView)itemView.findViewById(R.id.lblStartDate);
            tvSeeIT=(TextView)itemView.findViewById(R.id.lblSeeIT);
            tvSeeIT.setCompoundDrawablesWithIntrinsicBounds(null, null, leftDrawable, null);
            tvCity.setCompoundDrawablesWithIntrinsicBounds(null, null, leftDrawablecity, null);

            //  tvJob_desc=(TextView)itemView.findViewById(R.id.lblTextJob);
            tvItem    = (LinearLayout) itemView.findViewById(R.id.lvvIdtems);
            imgView    = (ImageView) itemView.findViewById(R.id.imgCompany);
            tvItem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(v.getContext(), JobActivity.class);
                    intent.putExtra("id", movies.get(getAdapterPosition()).getDesc_id());
                    startActivityForResult((Activity)context,intent,0,null);

                }
            });
//            Picasso.setSingletonInstance(picasso);

        }

        void bindData(Jobs jobsModel){
            tvName.setText(jobsModel.getJob_name());
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

           // tvDomain.setText(jobsModel.getDomain_name());
            tvDate.setText(jobsModel.getJob_start());
            tvSeeIT.setText(jobsModel.getSee_it().toString());
           /* tvJob_desc.setText(jobsModel.getJob_desc());

            if(jobsModel.getJob_desc().isEmpty()){
                tvJob_desc.setVisibility(View.GONE);
            }*/
       /*     new DownloadImageTask(imgView)
                    .execute(jobsModel.getImage());*/

            Picasso.get().load(jobsModel.getImage()).placeholder( AppCompatResources.getDrawable(context, R.drawable.pro))  .into(imgView);


        }
//"http://192.168.1.20/libyacv/public/images/company/"+

    }

    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
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
