package libyacvpro.libya_cv.network;

import androidx.annotation.Keep;

import java.util.List;

import libyacvpro.libya_cv.entities.CompanyPackage.Company;
import libyacvpro.libya_cv.entities.CompanyPackage.CompanyForEdit;
import libyacvpro.libya_cv.entities.GeneralPackage.Domain;
import libyacvpro.libya_cv.entities.Seeker;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import libyacvpro.libya_cv.entities.AccessToken;
import libyacvpro.libya_cv.entities.CertificatePackage.CertificateForEdit;
import libyacvpro.libya_cv.entities.CertificatePackage.CertificateResponse;
import libyacvpro.libya_cv.entities.EducationPackage.EducationForEdit;
import libyacvpro.libya_cv.entities.EducationPackage.EducationResponse;
import libyacvpro.libya_cv.entities.ExperiencePackage.ExperienceForEdit;
import libyacvpro.libya_cv.entities.ExperiencePackage.ExperienceResponse;
import libyacvpro.libya_cv.entities.HobbyPackage.HobbyForEdit;
import libyacvpro.libya_cv.entities.HobbyPackage.HobbyResponse;
import libyacvpro.libya_cv.entities.InfoPackage.InfoForEdit;
import libyacvpro.libya_cv.entities.InfoPackage.InfoResponse;
import libyacvpro.libya_cv.entities.JobSearchPackage.Jobs;
import libyacvpro.libya_cv.entities.JobSearchPackage.JobsResponse;
import libyacvpro.libya_cv.entities.LangPackage.LanguageForEdit;
import libyacvpro.libya_cv.entities.LangPackage.LanguageResponse;
import libyacvpro.libya_cv.entities.Message;
import libyacvpro.libya_cv.entities.Model;
import libyacvpro.libya_cv.entities.NotificationEntity;
import libyacvpro.libya_cv.entities.PostResponse;
import libyacvpro.libya_cv.entities.RefreshedForEdit;
import libyacvpro.libya_cv.entities.SeekerCvPackage.ShowCvResponse;
import libyacvpro.libya_cv.entities.SeekerResponse;
import libyacvpro.libya_cv.entities.SettingForEdit;
import libyacvpro.libya_cv.entities.ShowJobPackage.ShowJob;
import libyacvpro.libya_cv.entities.ShowJobPackage.ShowParaJob;
import libyacvpro.libya_cv.entities.SkillsPackage.SkillsForEdit;
import libyacvpro.libya_cv.entities.SkillsPackage.SkillsResponse;
import libyacvpro.libya_cv.entities.SpecialtyPackage.SpecialtyForEdit;
import libyacvpro.libya_cv.entities.SpecialtyPackage.SpecialtyResponse;
import libyacvpro.libya_cv.entities.TrainingPackage.TrainingForEdit;
import libyacvpro.libya_cv.entities.TrainingPackage.TrainingResponse;

public interface ApiService {


    @GET("we")
    Call<Company> we();


    @POST("register2")
    @FormUrlEncoded
    Call<AccessToken> register( @Field("email") String email, @Field("password") String password);


    @POST("logout")
    @FormUrlEncoded
    Call<AccessToken> logout();



    @POST("login")
    @FormUrlEncoded
    Call<AccessToken> login(@Field("username") String username, @Field("password") String password);

    @POST("social_auth")
    @FormUrlEncoded
    Call<AccessToken> socialAuth(@Field("name") String name,
                                 @Field("email") String email,
                                 @Field("provider") String provider,
                                 @Field("provider_seeker_id") String providerUserId);

    @POST("refresh")
    @FormUrlEncoded
    Call<AccessToken> refresh(@Field("refresh_token") String refreshToken);

    @GET("posts")
    Call<PostResponse> posts();

    @GET("edit_refresh")
    Call<RefreshedForEdit> refreshed();

    @POST("edit_refresh")
    Call<RefreshedForEdit> afterRefreshed();

    //region SeekerInfo
    @GET("editinfo")
    Call<Model> seekers();

   @POST("editinfo")
    @FormUrlEncoded
    Call<Message> postInfo(@Field("fname") String fname,

                         @Field("about") String about,
                         @Field("city") String city,
                         @Field("edt") String edt,
                         @Field("sex") String sex,
                         @Field("goal") String goal,
                         @Field("address") String address,
                         @Field("birth_day") String birth_day,
                         @Field("phone") String phone,
                         @Field("domain") String domain,
                           @Field("email1") String email


    );
    @Multipart
    @POST("edit_image")
    Call<Message> postImageSeeker(@Part MultipartBody.Part file);
//endregion
    //region Education
    @GET("education")
    Call<EducationResponse> educations();

    @GET("education/create")
    Call<EducationForEdit> createEducation();

    @POST("education")
    @FormUrlEncoded
    Call<Message> storeEducation(
                                  @Field("ed_name") String ed_name,
                                  @Field("dom_name") String dom_name,
                                  @Field("faculty") String faculty,
                                  @Field("univ") String univ,
                                  @Field("specialty") String specialty,
                                  @Field("avg_num") String avg_num,
                                  @Field("start_date") String start_date,
                                  @Field("end_date") String end_date,
                                  @Field("_method") String _method);


    @GET("education/{id}/edit")
    Call<EducationForEdit> getEducation(@Path("id") int id);

    @POST("education/{id}")
    @FormUrlEncoded
    Call<Message> updateEducation(@Path("id") int id,
                                  @Field("ed_name") String ed_name,
                                  @Field("dom_name") String dom_name,
                                  @Field("faculty") String faculty,
                                  @Field("univ") String univ,
                                  @Field("specialty") String specialty,
                                  @Field("avg_num") String avg_num,
                                  @Field("start_date") String start_date,
                                  @Field("end_date") String end_date,
                                           @Field("_method") String _method);

    @POST("education/{id}")
    @FormUrlEncoded
    Call<Message> deleteEducation(@Path("id") int id,
                                  @Field("_method") String _method);

    //endregion
    //region Experience
    @GET("experience")
    Call<ExperienceResponse> experience();

    @GET("experience/create")
    Call<ExperienceForEdit> createExperience();

    @POST("experience")
    @FormUrlEncoded
    Call<Message> storeExperience(
            @Field("exp_comp") String exp_comp,
            @Field("exp_name") String exp_name,
            @Field("exp_desc") String exp_desc,
            @Field("dom_id") String dom_id,
            @Field("start_date") String start_date,
            @Field("end_date") String end_date,
            @Field("state") String state,

            @Field("_method") String _method);


    @GET("experience/{id}/edit")
    Call<ExperienceForEdit> getExperience(@Path("id") int id);

    @POST("experience/{id}")
    @FormUrlEncoded
    Call<Message> updateExp(@Path("id") int id,
                                  @Field("exp_comp") String exp_comp,
                                  @Field("exp_name") String exp_name,
                                  @Field("exp_desc") String exp_desc,
                                 @Field("dom_id") String dom_id,
                                 @Field("start_date") String start_date,
                                  @Field("end_date") String end_date,
                                  @Field("state") String state,
                                  @Field("_method") String _method);

 @POST("experience/{id}")
 @FormUrlEncoded
 Call<Message> deleteExperience(@Path("id") int id,
                              @Field("_method") String _method);

    //endregion
    //region Language

    @GET("language")
    Call<LanguageResponse> language();


    @GET("language/create")
    Call<LanguageForEdit> createLanguage();


    @POST("language")
    @FormUrlEncoded
    Call<Message> storeLanguage(
                                 @Field("lang_id") String lang_name,
                                 @Field("level_id") String level_name,
                                 @Field("_method") String _method);


   @POST("language/{id}")
   @FormUrlEncoded
   Call<Message> deleteLanguage(@Path("id") int id,
                             @Field("_method") String _method);

    @GET("language/{id}/edit")
    Call<LanguageForEdit> getLanguage(@Path("id") int id);

    @POST("language/{id}")
    @FormUrlEncoded
    Call<Message> updateLanguage(@Path("id") int id,
                            @Field("lang_id") String lang_name,
                            @Field("level_id") String level_name,
                            @Field("_method") String _method);

    //endregion
    //region Skills

    @GET("skills")
    Call<SkillsResponse> skills();


    @GET("skills/create")
    Call<SkillsForEdit> createSkills();


    @POST("skills")
    @FormUrlEncoded
    Call<Message> storeSkills(
            @Field("skills_name") String skills_name,
            @Field("level_name") String level_name,
            @Field("_method") String _method);


    @GET("skills/{id}/edit")
    Call<SkillsForEdit> getSkills(@Path("id") int id);

    @POST("skills/{id}")
    @FormUrlEncoded
    Call<Message> updateSkills(@Path("id") int id,
                                  @Field("skills_name") String skills_name,
                                  @Field("level_id") String level_name,
                                  @Field("_method") String _method);

    @POST("skills/{id}")
    @FormUrlEncoded
    Call<Message> deleteSkills(@Path("id") int id,
                                  @Field("_method") String _method);

//endregion
    //region Certificate

    @GET("certificate")
    Call<CertificateResponse> certificate();

    @GET("certificate/create")
    Call<CertificateForEdit> createCertificate();

    @POST("certificate")
    @FormUrlEncoded
    Call<Message> storeCertificate(
            @Field("cert_name") String cert_name,
            @Field("cert_date") String cert_date,
            @Field("_method") String _method);


    @GET("certificate/{id}/edit")
    Call<CertificateForEdit> getCertificate(@Path("id") int id);

    @POST("certificate/{id}")
    @FormUrlEncoded
    Call<Message> updateCertificate(@Path("id") int id,
                               @Field("cert_name") String cert_name,
                               @Field("cert_date") String cert_date,
                               @Field("_method") String _method);

    @POST("certificate/{id}")
    @FormUrlEncoded
    Call<Message> deleteCertificate(@Path("id") int id,
                               @Field("_method") String _method);

//endregion
    //region Hobby

    @GET("hobby")
    Call<HobbyResponse> hobby();



    @GET("myappjob")
    Call<JobsResponse> listmyjob();

    @GET("settingnote")
    Call<List<Domain>> listNote();
    @POST("settingnote")
    @FormUrlEncoded
    Call<Message> postNote(@Field("note[]") Integer[] note,

                                    @Field("_method") String _method);

   @GET("hobby/create")
   Call<HobbyForEdit> createHobby();

   @POST("hobby")
   @FormUrlEncoded
   Call<Message> storeHobby(@Field("hobby_name") String hobby_name,
                          @Field("_method") String _method);

    @GET("hobby/{id}/edit")
    Call<HobbyForEdit> getHobby(@Path("id") int id);

    @POST("hobby/{id}")
    @FormUrlEncoded
    Call<Message> updateHobby(@Path("id") int id,
                                    @Field("hobby_name") String hobby_name,
                                     @Field("_method") String _method);

    @POST("hobby/{id}")
    @FormUrlEncoded
    Call<Message> deleteHobby(@Path("id") int id,
                                    @Field("_method") String _method);



    //endregion
    //region Specialty

    @GET("specialty")
    Call<SpecialtyResponse> specialty();

    @GET("specialty/create")
    Call<SpecialtyForEdit> createSpecialty();

    @POST("specialty")
    @FormUrlEncoded
    Call<Message> storeSpecialty(
            @Field("spec_name") String spec_name,
            @Field("_method") String _method);


    @GET("specialty/{id}/edit")
    Call<SpecialtyForEdit> getSpecialty(@Path("id") int id);

    @POST("specialty/{id}")
    @FormUrlEncoded
    Call<Message> updateSpecialty(@Path("id") int id,
                              @Field("spec_name") String spec_name,
                              @Field("_method") String _method);

    @POST("specialty/{id}")
    @FormUrlEncoded
    Call<Message> deleteSpecialty(@Path("id") int id,
                              @Field("_method") String _method);

    //endregion
    //region Training

 @GET("training")
 Call<TrainingResponse> training();

    @GET("training/create")
    Call<TrainingForEdit> createTraining();

    @POST("training")
    @FormUrlEncoded
    Call<Message> storeTraining(
             @Field("train_name") String train_name,
            @Field("train_comp") String train_comp,
            @Field("train_date") String train_date,
            @Field("_method") String _method);

 @GET("training/{id}/edit")
 Call<TrainingForEdit> getTraining(@Path("id") int id);

 @POST("training/{id}")
 @FormUrlEncoded
 Call<Message> updateTraining(@Path("id") int id,
                                @Field("train_name") String train_name,
                               @Field("train_comp") String train_comp,
                               @Field("train_date") String train_date,
                               @Field("_method") String _method);

 @POST("training/{id}")
 @FormUrlEncoded
 Call<Message> deleteTraining(@Path("id") int id,
                               @Field("_method") String _method);

 //endregion
    //region Info

 @GET("info")
 Call<InfoResponse> info();

    @GET("info/create")
    Call<InfoForEdit> createInfo();

    @POST("info")
    @FormUrlEncoded
    Call<Message> storeInfo(
            @Field("info_name") String info_name,
            @Field("info_date") String info_date,
            @Field("info_text") String info_text,
            @Field("_method") String _method);


 @GET("info/{id}/edit")
 Call<InfoForEdit> getInfo(@Path("id") int id);

 @POST("info/{id}")
 @FormUrlEncoded
 Call<Message> updateInfo(@Path("id") int id,
                              @Field("info_name") String info_name,
                              @Field("info_date") String info_date,
                              @Field("info_text") String info_text,
                              @Field("_method") String _method);

 @POST("info/{id}")
 @FormUrlEncoded
 Call<Message> deleteInfo(@Path("id") int id,
                              @Field("_method") String _method);

 //endregion
 @GET("search/services")
 Call<List<Jobs>> getSearchServices(@Query("page") int pageNumber, @Query("string") String stringSearch, @Query("city") String cityName,
                                      @Query("domain") String domainName);


    @GET("search/company")
    Call<List<Company>> getSearchCompany(@Query("page") int pageNumber, @Query("string") String stringSearch, @Query("city") String cityName,
                                      @Query("domain") String domainName);
    //region SearchJob
    @GET("search/job")
    Call<List<Jobs>> getSearchJobs(@Query("page") int pageNumber,@Query("string") String stringSearch,@Query("city") String cityName,
                                   @Query("domain") String domainName);
    @GET("job/{user}")
    Call<ShowJob> getShowJob(@Path("user") int id);

    @GET("c/{user}")
    Call<Company> getShowCompany(@Path("user") String id);
    @GET("services/{user}")
    Call<Company> getShowServices(@Path("user") int id);


    @GET("c/{user}/add")
    Call<Message> addFollow(@Path("user") String id);

    @GET("c/{user}/remove")
    Call<Message> removeFollow(@Path("user") String id);

    @GET("search/showparajob")
    Call<ShowParaJob> getShowParaJob();
    //endregion

    @GET("profile")
    Call<ShowCvResponse> getSeekerCv();

    @GET("notifications")
    Call<List<NotificationEntity>> getNote();

    @GET("setting")
    Call<SettingForEdit> getSeekerSetting();

    @POST("setting")
    @FormUrlEncoded
    Call<Message> postChangeHide(@Field("hide") String pHide,@Field("phone") String pPhone,@Field("image") String pImage);

    @POST("setting/password")
    @FormUrlEncoded
    Call<Message> postChangePassword(@Field("password") String password,
                                     @Field("newpassword") String newpassword);


    @POST("postjob/{id}")
    @FormUrlEncoded
    Call<Message> postToJob(@Path("id") int id,
                            @Field("_method") String _method);




    @POST("deletejob/{id}")
    @FormUrlEncoded
    Call<Message> deleteToJob(@Path("id") int id,
                            @Field("_method") String _method);


    @POST("profile")
    @FormUrlEncoded
    Call<SeekerResponse> postSeeker(@Field("info_name") String info_name,
                                    @Field("info_date") String info_date,
                                    @Field("info_text") String info_text);


    @POST("__fire/addtoken")
    @FormUrlEncoded
    Call<Message> postFireBaseToken(@Field("fcmToken") String fcmToken,@Field("_method") String _method);

    @GET("company")
    Call<List<String>> getCompanyList();



    @GET("create_company")
    Call<CompanyForEdit> createCompany();
    @POST("create_company")
    @FormUrlEncoded
    Call<Message> storeCompany(@Field("comp_name") String txtCompName,
                           @Field("comp_user_name") String txtCompanyName,
                           @Field("address") String txtAddress,
                           @Field("url") String txtUrl,
                           @Field("city_id") String spCity,
                           @Field("domain_id") String spDomain,
                           @Field("compt_id") String spType


    );

    @GET("company/edit_info")
    Call<CompanyForEdit> getCompanyInfo();

    @POST("company/edit_info")
    @FormUrlEncoded
    Call<Message> storeCompanyInfo(
                                @Field("email") String email,
                                @Field("phone") String phone,
                               @Field("url") String txtUrl,
                               @Field("address") String address,
                               @Field("city_id") String spCity,
                               @Field("domain_id") String spDomain,
                               @Field("compt_id") String spType,
                               @Field("comp_desc") String comp_desc,
                               @Field("services") String services,
                               @Field("facebook") String facebook,
                               @Field("twitter") String twitter,
                               @Field("linkedin") String linkedin,
                               @Field("comp_name") String comp_name

    );

    @Multipart
    @POST("company/edit_image")
    Call<Message> postImage(@Part MultipartBody.Part file);

    @GET("company/edit_image")
    Call<Message> getImage();


    @GET("seeker/services")
    Call<List<Jobs>> getMyServices(@Query("page") int pageNumber);
    @GET("seeker/services/create")
    Call<ShowParaJob> getAddServices();




    @POST("seeker/services/{id}")
    @FormUrlEncoded
    Call<Message> removeServices(@Path("id") int job_id, @Field("_method") String _method);

    @POST("seeker/services")
    @FormUrlEncoded
    Call<Message> storeAddServices(
                              @Field("domain_id") String spDomain,

                              @Field("city_id") String spCity,
                              @Field("job_name") String pJobName,
                              @Field("job_desc") String pDesc,


                              @Field("_method") String _method

    );


    @GET("seeker/services/{id}/edit")
    Call<ShowParaJob> getEditServices(@Path("id") int id);


    @POST("seeker/services/{id}")
    @FormUrlEncoded
    Call<Message> updateAddServices(
                               @Path("id") int id,
                               @Field("domain_id") String spDomain,

                               @Field("city_id") String spCity,
                               @Field("job_name") String pJobName,
                               @Field("job_desc") String pDesc,

                               @Field("_method") String _method

    );

    @GET("company/job")
    Call<List<Jobs>> getMyJobs(@Query("page") int pageNumber);

    @GET("company/job/create")
    Call<ShowParaJob> getAddJob();


    @POST("company/job/{id}")
    @FormUrlEncoded
    Call<Message> removeJob(@Path("id") int job_id, @Field("_method") String _method);

    @POST("company/job")
    @FormUrlEncoded
    Call<Message> storeAddJob(
                              @Field("domain_id") String spDomain,

                              @Field("city_id") String spCity,
                              @Field("job_name") String pJobName,
                              @Field("job_desc") String pDesc,
                              @Field("job_skills") String pSkills,
                              @Field("email") String pEamil,
                              @Field("phone") String pPhone,
                              @Field("website") String pWebsite,
                              @Field("how_receive") Integer pHowRec,

                              @Field("_method") String _method

    );


    @GET("company/job/{id}/edit")
    Call<ShowParaJob> getEditJob(@Path("id") int id);


    @POST("company/job/{id}")
    @FormUrlEncoded
    Call<Message> updateAddJob(
                               @Path("id") int id,
                               @Field("domain_id") String spDomain,

                               @Field("city_id") String spCity,
                              @Field("job_name") String pJobName,
                              @Field("job_desc") String pDesc,
                              @Field("job_skills") String pSkills,
                               @Field("email") String pEamil,
                               @Field("phone") String pPhone,
                               @Field("website") String pWebsite,
                               @Field("how_receive") Integer pHowRec,
                              @Field("_method") String _method

    );

    @GET("company/{user}/job-application/{jobid}")
    Call<List<Seeker>> getSearchAppCvs(@Path("user") String user,@Path("jobid") Integer jobid);

    @GET("search/cv")
    Call<List<Seeker>> getSearchCvs(@Query("page") int pageNumber,@Query("string") String stringSearch,@Query("city") String cityName,
                                    @Query("domain") String domainName);

    @GET("edit/map/{user}")
    Call<String> getEditMap(@Path("user") String user);

    @POST("edit/map/{user}")
    @FormUrlEncoded
    Call<Message> updateCompanyMap(@Path("user") String user,@Field("lat") String  stLat,@Field("lng") String stLnt);

    @GET("show/seeker/{seeker_id}")
    Call<ShowCvResponse> getShowSeekerCv(@Path("seeker_id") String username);

    @GET("show/seeker")
    Call<ShowCvResponse> getShowSeekerCvAuth();


    @POST("accept/job/{jobid}/{seeker_id}")
    @FormUrlEncoded
    Call<Message> acceptSeeker(
                               @Path("jobid") int id,
                               @Path("seeker_id") String seeker_id,
                               @Field("job_skills") String pSkills

    );

    @POST("remove/job/{jobid}/{seeker_id}")
    @FormUrlEncoded
    Call<Message> removeSeeker(
            @Path("jobid") int id,
            @Path("seeker_id") String seeker_id,
            @Field("job_skills") String pSkills

    );

}
