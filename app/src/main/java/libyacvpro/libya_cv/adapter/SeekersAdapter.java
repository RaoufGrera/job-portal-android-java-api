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

import libyacvpro.libya_cv.CVActivity;
import libyacvpro.libya_cv.R;
import libyacvpro.libya_cv.entities.Seeker;

import static androidx.core.app.ActivityCompat.startActivityForResult;


public class SeekersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    public final int AD_TYPE = 2;

    private static final String APP_ID = "ca-app-pub-9929016091047307~2213947061";


    static Context context;
    static List<Seeker> movies;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    static Context _context;


    public SeekersAdapter(Context context, List<Seeker> jobses) {
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


            return new MovieHolder(inflater.inflate(R.layout.row_cvs2,parent,false));
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
            ((MovieHolder)holder).bindData(movies.get(position), (MovieHolder)holder);
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
            //MobileAds.initialize(context, APP_ID);

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
        TextView tvSpec;
        String userID;
        TextView tvDomain;
        TextView tvCity;
        TextView tvEdt;
        TextView tvExp;
        TextView tvMathc;
        ImageView tvimgView;
        LinearLayout tvItem;
        TextView tvAbout;


         MovieHolder(View child) {
            super(child);

              tvName=(TextView)child.findViewById(R.id.lblName);
            tvSpec=(TextView)child.findViewById(R.id.lblSpec);
            tvDomain=(TextView)child.findViewById(R.id.lblDomain);
           tvCity=(TextView)child.findViewById(R.id.lblCity);
           tvEdt=(TextView)child.findViewById(R.id.lblEdtName);
           tvExp=(TextView)child.findViewById(R.id.lblExp);
            tvMathc=(TextView)child.findViewById(R.id.lblMatch);
           tvAbout=(TextView)child.findViewById(R.id.lblAbout);
            tvItem    = (LinearLayout) child.findViewById(R.id.lvvIdtems);
            tvimgView    = (ImageView) child.findViewById(R.id.imgCompany);
            tvItem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(v.getContext(), CVActivity.class);
                    intent.putExtra("seeker_id", movies.get(getAdapterPosition()).getSeeker_id().toString());
                    startActivityForResult((Activity)context,intent,0,null);

                }
            });
//            Picasso.setSingletonInstance(picasso);

        }

        void bindData(Seeker seekerModel, MovieHolder holder){

            holder.userID = seekerModel.getSeeker_id().toString();
            holder.tvName.setText(seekerModel.getFname());
            holder.tvDomain.setText(seekerModel.getDomain_name());
            holder.tvCity.setText(seekerModel.getCity_name());
            switch(seekerModel.getCity_name()){
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

            holder.tvEdt.setText(seekerModel.getEdt_name());
            holder.tvExp.setText(seekerModel.getExp());
            holder.tvAbout.setText(seekerModel.getAbout());
            holder.tvMathc.setText(seekerModel.getSee_it());

            holder.tvSpec.setText(seekerModel.getSpec());
            Picasso.get().load(seekerModel.getImage()).placeholder( AppCompatResources.getDrawable(context, R.drawable.pro))  .into(tvimgView);

             if(seekerModel.getSpec().equals(""))
                holder.tvSpec.setVisibility(View.GONE);
             else
                 holder.tvSpec.setVisibility(View.VISIBLE);





            //  tvMathc.setText(tvMathc.get(position));

            Picasso.get().load(seekerModel.getImage()).into(holder.tvimgView);

           /* tvJob_desc.setText(jobsModel.getJob_desc());

            if(jobsModel.getJob_desc().isEmpty()){
                tvJob_desc.setVisibility(View.GONE);
            }*/
       /*     new DownloadImageTask(imgView)
                    .execute(jobsModel.getImage());*/



        }
//"http://192.168.1.20/libyacv/public/images/company/"+

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
