package libyacvpro.libya_cv.entities.CompanyPackage;

import java.io.Serializable;

/**
 * Created by Asasna on 3/23/2018.
 */

public class Company   { //implements Serializable

     String comp_name;
    String comp_user_name;
    String services;
    String facebook;
    String twitter;
    String linkedin;
    String comp_id;
     String url;
    String about;
    String address;
    String email;
    String lat;
    String lng;
    String see_it;
    String comp_desc;
    String phone;
    String domain_name;
    String city_name;
    String compt_name;
    public String type;
     private String image;
    boolean isreq;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public boolean getisreq() {
        return isreq;
    }
    public String getSee_it() {
        return see_it;
    }

    public void setSee_it(String see_it) {
        this.see_it = see_it;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public Company(String type) {
        this.type = type;
    }


    public String getComp_name() {
        return comp_name;
    }

    public void setComp_name(String comp_name) {
        this.comp_name = comp_name;
    }

    public String getComp_user_name() {
        return comp_user_name;
    }

    public void setComp_user_name(String comp_user_name) {
        this.comp_user_name = comp_user_name;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComp_desc() {
        return comp_desc;
    }

    public void setComp_desc(String comp_desc) {
        this.comp_desc = comp_desc;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getCompt_name() {
        return compt_name;
    }

    public void setCompt_name(String compt_name) {
        this.compt_name = compt_name;
    }
}
