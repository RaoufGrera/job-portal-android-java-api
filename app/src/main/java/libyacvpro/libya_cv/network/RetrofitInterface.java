package libyacvpro.libya_cv.network;

import libyacvpro.libya_cv.entities.Message;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Asasna on 3/26/2018.
 */

public interface RetrofitInterface {
    @FormUrlEncoded
    @POST("company/edit_image/{user}")
    Call<Message> postImage(@Path("user") String user, @Field("file") String file);
}
