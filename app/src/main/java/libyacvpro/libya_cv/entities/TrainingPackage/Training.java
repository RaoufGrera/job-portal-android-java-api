package libyacvpro.libya_cv.entities.TrainingPackage;

/**
 * Created by Asasna on 9/28/2017.
 */

public class Training {
    Integer train_id;
    String train_name;
    String train_comp;
    String train_date;

    public Training(Integer train_id, String train_name, String train_comp, String train_date) {
        this.train_id = train_id;
        this.train_name = train_name;
        this.train_comp = train_comp;
        this.train_date = train_date;
    }

    public Integer getTrain_id() {
        return train_id;
    }

    public void setTrain_id(Integer train_id) {
        this.train_id = train_id;
    }

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }

    public String getTrain_comp() {
        return train_comp;
    }

    public void setTrain_comp(String train_comp) {
        this.train_comp = train_comp;
    }

    public String getTrain_date() {
        return train_date;
    }

    public void setTrain_date(String train_date) {
        this.train_date = train_date;
    }
}
