package libyacvpro.libya_cv.entities;

/**
 * Created by Asasna on 9/21/2017.
 */

public class Seeker {

    private Integer seeker_id;
    private String match;
    private String hide_cv;
    private String price;
    private String about;
    private String city_name;
    private String nat_name;
    private String fname;
    private String lname;
    private String edt_name;
    private String website;
    private String facebook;
    private String twitter;
    private String instagram;
    private String goodreads;
    private String email;
    private String image;
    private String code_image;
    private String goal_text;
    private String user_name;
    private String phone;
    private String domain_name;
    private String birth_day;
    private String address;
    private String gender;
    public String type;
    public String exp;
    public Integer req_event;
    public String see_it;
    public String image_view;
    public String phone_view;
    public String spec;

    public String getSee_it() {
        return see_it;
    }

    public void setSee_it(String see_it) {
        this.see_it = see_it;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Seeker(String type) {
        this.type = type;
    }
    public String getImage_view() {
        return image_view;
    }

    public void setImage_view(String image_view) {
        this.image_view = image_view;
    }

    public String getPhone_view() {
        return phone_view;
    }

    public void setPhone_view(String phone_view) {
        this.phone_view = phone_view;
    }

    public Integer getReq_event() {
        return req_event;
    }

    public void setReq_event(Integer req_event) {
        this.req_event = req_event;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEdt_name() {
        return edt_name;
    }

    public void setEdt_name(String edt_name) {
        this.edt_name = edt_name;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public Seeker(Integer seeker_id, String match, String hide_cv, String price, String about, String city_name, String nat_name, String fname, String lname, String edt_name, String website, String facebook, String twitter, String instagram, String goodreads, String email, String image, String code_image, String goal_text, String user_name, String phone, String domain_name, String birth_day, String address, String gender) {
        this.seeker_id = seeker_id;
        this.match = match;
        this.hide_cv = hide_cv;
        this.price = price;
        this.about = about;
        this.city_name = city_name;
        this.nat_name = nat_name;
        this.fname = fname;
        this.lname = lname;
        this.edt_name = edt_name;
        this.website = website;
        this.facebook = facebook;
        this.twitter = twitter;
        this.instagram = instagram;
        this.goodreads = goodreads;
        this.email = email;
        this.image = image;
        this.code_image = code_image;
        this.goal_text = goal_text;
        this.user_name = user_name;
        this.phone = phone;
        this.domain_name = domain_name;
        this.birth_day = birth_day;
        this.address = address;
        this.gender = gender;
    }

    public String getAbout() {
        return about;
    }


    public void setAbout(String about) {
        this.about = about;
    }

    public Integer getSeeker_id() {
        return seeker_id;
    }

    public void setSeeker_id(Integer seeker_id) {
        this.seeker_id = seeker_id;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getHide_cv() {
        return hide_cv;
    }

    public void setHide_cv(String hide_cv) {
        this.hide_cv = hide_cv;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getNat_name() {
        return nat_name;
    }

    public void setNat_name(String nat_name) {
        this.nat_name = nat_name;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getGoodreads() {
        return goodreads;
    }

    public void setGoodreads(String goodreads) {
        this.goodreads = goodreads;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCode_image() {
        return code_image;
    }

    public void setCode_image(String code_image) {
        this.code_image = code_image;
    }

    public String getGoal_text() {
        return goal_text;
    }

    public void setGoal_text(String goal_text) {
        this.goal_text = goal_text;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}