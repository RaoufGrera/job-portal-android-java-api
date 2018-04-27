package libyacvpro.libya_cv.entities.EducationPackage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Asasna on 9/22/2017.
 */

public class Education {
    Integer ed_id;
    String avg;
    String start_date;
    String end_date;
    String domain_name;
    String edt_name;
    String univ_name;
    String faculty_name;
    String sed_name;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Education(Integer ed_id, String avg, String start_date, String end_date, String domain_name, String edt_name, String univ_name, String faculty_name, String sed_name) {
        this.ed_id = ed_id;
        this.avg = avg;
        this.start_date = start_date;
        this.end_date = end_date;
        this.domain_name = domain_name;
        this.edt_name = edt_name;
        this.univ_name = univ_name;
        this.faculty_name = faculty_name;
        this.sed_name = sed_name;
    }

    public Integer getEd_id() {
        return ed_id;
    }

    public void setEd_id(Integer ed_id) {
        this.ed_id = ed_id;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
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

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public String getEdt_name() {
        return edt_name;
    }

    public void setEdt_name(String edt_name) {
        this.edt_name = edt_name;
    }

    public String getUniv_name() {
        return univ_name;
    }

    public void setUniv_name(String univ_name) {
        this.univ_name = univ_name;
    }

    public String getFaculty_name() {
        return faculty_name;
    }

    public void setFaculty_name(String faculty_name) {
        this.faculty_name = faculty_name;
    }

    public String getSed_name() {
        return sed_name;
    }

    public void setSed_name(String sed_name) {
        this.sed_name = sed_name;
    }
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
