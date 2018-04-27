package libyacvpro.libya_cv.entities.JobSearchPackage;

import java.io.Serializable;

/**
 * Created by Asasna on 10/4/2017.
 */

public class Jobs implements Serializable {


        private String job_name;
        private Integer desc_id;
        private String comp_name;
        private String image;
        private String image_code;
        private String comp_user_name;
        private String domain_name;
        private String city_name;
        private Integer see_it;
        private String url;
        private String job_desc;
        private String job_start;
        private String job_end;
    public String type;

    public String getJob_desc() {
        return job_desc;
    }

    public void setJob_desc(String job_desc) {
        this.job_desc = job_desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Jobs(String type) {
        this.type = type;
    }
/*
    public Jobs(String job_name, Integer desc_id, String comp_name, String image, String image_code, String comp_user_name, String domain_name, String city_name, Integer see_it, String url, String job_start, String job_end) {
        this.job_name = job_name;
        this.desc_id = desc_id;
        this.comp_name = comp_name;
        this.image = image;
        this.image_code = image_code;
        this.comp_user_name = comp_user_name;
        this.domain_name = domain_name;
        this.city_name = city_name;
        this.see_it = see_it;
        this.url = url;
        this.job_start = job_start;
        this.job_end = job_end;
    }
*/
    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public Integer getDesc_id() {
        return desc_id;
    }

    public void setDesc_id(Integer desc_id) {
        this.desc_id = desc_id;
    }

    public String getComp_name() {
        return comp_name;
    }

    public void setComp_name(String comp_name) {
        this.comp_name = comp_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_code() {
        return image_code;
    }

    public void setImage_code(String image_code) {
        this.image_code = image_code;
    }

    public String getComp_user_name() {
        return comp_user_name;
    }

    public void setComp_user_name(String comp_user_name) {
        this.comp_user_name = comp_user_name;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public Integer getSee_it() {
        return see_it;
    }

    public void setSee_it(Integer see_it) {
        this.see_it = see_it;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJob_start() {
        return job_start;
    }

    public void setJob_start(String job_start) {
        this.job_start = job_start;
    }

    public String getJob_end() {
        return job_end;
    }

    public void setJob_end(String job_end) {
        this.job_end = job_end;
    }
}
