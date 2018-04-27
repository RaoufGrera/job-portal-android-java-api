package libyacvpro.libya_cv.entities.SpecialtyPackage;

/**
 * Created by Asasna on 9/28/2017.
 */

public class Specialty {

    Integer spec_seeker_id;
    String spec_name;

    public Specialty(Integer spec_seeker_id, String spec_name) {
        this.spec_seeker_id = spec_seeker_id;
        this.spec_name = spec_name;
    }

    public Integer getSpec_seeker_id() {
        return spec_seeker_id;
    }

    public void setSpec_seeker_id(Integer spec_seeker_id) {
        this.spec_seeker_id = spec_seeker_id;
    }

    public String getSpec_name() {
        return spec_name;
    }

    public void setSpec_name(String spec_name) {
        this.spec_name = spec_name;
    }
}
