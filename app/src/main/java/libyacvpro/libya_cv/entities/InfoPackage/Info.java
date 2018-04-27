package libyacvpro.libya_cv.entities.InfoPackage;

/**
 * Created by Asasna on 9/28/2017.
 */

public class Info {
    Integer info_id;
    String info_name;
    String info_date;
    String info_text;

    public Info(Integer info_id, String info_name, String info_date, String info_text) {
        this.info_id = info_id;
        this.info_name = info_name;
        this.info_date = info_date;
        this.info_text = info_text;
    }

    public Integer getInfo_id() {
        return info_id;
    }

    public void setInfo_id(Integer info_id) {
        this.info_id = info_id;
    }

    public String getInfo_name() {
        return info_name;
    }

    public void setInfo_name(String info_name) {
        this.info_name = info_name;
    }

    public String getInfo_date() {
        return info_date;
    }

    public void setInfo_date(String info_date) {
        this.info_date = info_date;
    }

    public String getInfo_text() {
        return info_text;
    }

    public void setInfo_text(String info_text) {
        this.info_text = info_text;
    }
}
