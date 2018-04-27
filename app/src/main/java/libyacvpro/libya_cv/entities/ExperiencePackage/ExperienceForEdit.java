package libyacvpro.libya_cv.entities.ExperiencePackage;

import java.util.List;

import libyacvpro.libya_cv.entities.GeneralPackage.Domain;

/**
 * Created by Asasna on 9/25/2017.
 */

public class ExperienceForEdit {

    private List<Domain> domain = null;
     private Experience experience = null;

    public List<Domain> getDomain() {
        return domain;
    }

    public void setDomain(List<Domain> domain) {
        this.domain = domain;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }
}
