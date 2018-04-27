package libyacvpro.libya_cv.entities;


public class Refreshed {
    String update;
    Boolean check_date;

    public Refreshed(String update, Boolean check_date) {
        this.update = update;
        this.check_date = check_date;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public Boolean getCheck_date() {
        return check_date;
    }

    public void setCheck_date(Boolean check_date) {
        this.check_date = check_date;
    }
}
