package libyacvpro.libya_cv.entities.ExperiencePackage;

/**
 * Created by Asasna on 9/25/2017.
 */

public class Experience {

    Integer exp_id;
    String exp_name;
    String start_date;
    String end_date;
    String state;
    String exp_desc;
    String specialty;
    String domain_name;
    String compe_name;

    public Experience(Integer exp_id, String exp_name, String start_date, String end_date, String state, String exp_desc, String specialty, String domain_name, String compe_name) {
        this.exp_id = exp_id;
        this.exp_name = exp_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.state = state;
        this.exp_desc = exp_desc;
        this.specialty = specialty;
        this.domain_name = domain_name;
        this.compe_name = compe_name;
    }

    public Integer getExp_id() {
        return exp_id;
    }

    public void setExp_id(Integer exp_id) {
        this.exp_id = exp_id;
    }

    public String getExp_name() {
        return exp_name;
    }

    public void setExp_name(String exp_name) {
        this.exp_name = exp_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExp_desc() {
        return exp_desc;
    }

    public void setExp_desc(String exp_desc) {
        this.exp_desc = exp_desc;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public String getCompe_name() {
        return compe_name;
    }

    public void setCompe_name(String compe_name) {
        this.compe_name = compe_name;
    }
}
