package libyacvpro.libya_cv.entities.CompanyPackage;

import java.util.List;

import libyacvpro.libya_cv.entities.GeneralPackage.City;
import libyacvpro.libya_cv.entities.GeneralPackage.Domain;
import libyacvpro.libya_cv.entities.GeneralPackage.TypeCompanyEntities;

/**
 * Created by Asasna on 3/23/2018.
 */

public class CompanyForEdit {
    private List<Domain> domain = null;
    private List<City> city = null;
    private List<TypeCompanyEntities> type = null;
    private Company company = null;

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

    public List<TypeCompanyEntities> getType() {
        return type;
    }

    public void setType(List<TypeCompanyEntities> type) {
        this.type = type;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
