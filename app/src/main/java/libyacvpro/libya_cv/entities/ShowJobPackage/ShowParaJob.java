package libyacvpro.libya_cv.entities.ShowJobPackage;

import java.util.List;

import libyacvpro.libya_cv.entities.GeneralPackage.City;
import libyacvpro.libya_cv.entities.GeneralPackage.Domain;
import libyacvpro.libya_cv.entities.GeneralPackage.StatusEntities;
import libyacvpro.libya_cv.entities.GeneralPackage.TypeEntities;

/**
 * Created by Asasna on 10/15/2017.
 */

public class ShowParaJob {
    ShowJob job;

    List<Domain> domain;
    List<City> city;
    List<StatusEntities> status;

    public ShowJob getJob() {
        return job;
    }

    public void setJob(ShowJob job) {
        this.job = job;
    }

    List<TypeEntities> type;

    public List<Domain> getDomain() {
        return domain;
    }

    public void setDomain(List<Domain> domain) {
        this.domain = domain;
    }

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }

    public List<StatusEntities> getStatus() {
        return status;
    }

    public void setStatus(List<StatusEntities> status) {
        this.status = status;
    }

    public List<TypeEntities> getType() {
        return type;
    }

    public void setType(List<TypeEntities> type) {
        this.type = type;
    }
}
