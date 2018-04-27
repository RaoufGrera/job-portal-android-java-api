package libyacvpro.libya_cv.entities.EducationPackage;

import java.util.List;

import libyacvpro.libya_cv.entities.GeneralPackage.Domain;
import libyacvpro.libya_cv.entities.GeneralPackage.EducationType;


/**
 * Created by Asasna on 9/23/2017.
 */

public class EducationForEdit {
    private List<Domain> domain = null;
    private List<EducationType> ed_type = null;
    private Education education = null;

    public List<Domain> getDomain() {
        return domain;
    }

    public void setDomain(List<Domain> domain) {
        this.domain = domain;
    }

    public List<EducationType> getEd_type() {
        return ed_type;
    }

    public void setEd_type(List<EducationType> ed_type) {
        this.ed_type = ed_type;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }
}
