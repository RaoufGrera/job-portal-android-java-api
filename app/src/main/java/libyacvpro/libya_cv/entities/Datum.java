package libyacvpro.libya_cv.entities;

import java.util.List;

import libyacvpro.libya_cv.entities.GeneralPackage.City;
import libyacvpro.libya_cv.entities.GeneralPackage.Domain;
import libyacvpro.libya_cv.entities.GeneralPackage.Nat;

public class Datum {
    private List<City> city = null;
    private List<Nat> nat = null;
    private List<Domain> domain = null;

    private  Seeker  info = null;

    public Seeker getInfo() {
        return info;
    }

    public void setInfo(Seeker info) {
        this.info = info;
    }

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }

    public List<Nat> getNat() {
        return nat;
    }

    public void setNat(List<Nat> nat) {
        this.nat = nat;
    }

    public List<Domain> getDomain() {
        return domain;
    }

    public void setDomain(List<Domain> domain) {
        this.domain = domain;
    }
}
