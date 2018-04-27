package libyacvpro.libya_cv.entities.SeekerCvPackage;

import java.util.List;

import libyacvpro.libya_cv.entities.CertificatePackage.Certificate;
import libyacvpro.libya_cv.entities.EducationPackage.Education;
import libyacvpro.libya_cv.entities.ExperiencePackage.Experience;
import libyacvpro.libya_cv.entities.HobbyPackage.Hobby;
import libyacvpro.libya_cv.entities.InfoPackage.Info;
import libyacvpro.libya_cv.entities.Seeker;
import libyacvpro.libya_cv.entities.LangPackage.Language;
 import libyacvpro.libya_cv.entities.SkillsPackage.Skills;
import libyacvpro.libya_cv.entities.SpecialtyPackage.Specialty;
import libyacvpro.libya_cv.entities.TrainingPackage.Training;

/**
 * Created by Asasna on 10/10/2017.
 */

public class ShowCv {

    Seeker job_seeker;

    List<Education> seeker_ed;
    List<Experience> seeker_exp;
    List<Language> seeker_lang;
    List<Specialty> seeker_spec;
    List<Skills> seeker_skills;
    List<Certificate> seeker_cert;
    List<Training> seeker_train;
    List<Hobby> seeker_hobby;
    List<Info> seeker_info;

    public ShowCv(Seeker job_seeker, List<Education> seeker_ed, List<Experience> seeker_exp, List<Language> seeker_lang, List<Specialty> seeker_spec, List<Skills> seeker_skills, List<Certificate> seeker_cert, List<Training> seeker_train, List<Hobby> seeker_hobby, List<Info> seeker_info) {
        this.job_seeker = job_seeker;
        this.seeker_ed = seeker_ed;
        this.seeker_exp = seeker_exp;
        this.seeker_lang = seeker_lang;
        this.seeker_spec = seeker_spec;
        this.seeker_skills = seeker_skills;
        this.seeker_cert = seeker_cert;
        this.seeker_train = seeker_train;
        this.seeker_hobby = seeker_hobby;
        this.seeker_info = seeker_info;
    }

    public Seeker getJob_seeker() {
        return job_seeker;
    }

    public void setJob_seeker(Seeker job_seeker) {
        this.job_seeker = job_seeker;
    }

    public List<Education> getSeeker_ed() {
        return seeker_ed;
    }

    public void setSeeker_ed(List<Education> seeker_ed) {
        this.seeker_ed = seeker_ed;
    }

    public List<Experience> getSeeker_exp() {
        return seeker_exp;
    }

    public void setSeeker_exp(List<Experience> seeker_exp) {
        this.seeker_exp = seeker_exp;
    }

    public List<Language> getSeeker_lang() {
        return seeker_lang;
    }

    public void setSeeker_lang(List<Language> seeker_lang) {
        this.seeker_lang = seeker_lang;
    }

    public List<Specialty> getSeeker_spec() {
        return seeker_spec;
    }

    public void setSeeker_spec(List<Specialty> seeker_spec) {
        this.seeker_spec = seeker_spec;
    }

    public List<Skills> getSeeker_skills() {
        return seeker_skills;
    }

    public void setSeeker_skills(List<Skills> seeker_skills) {
        this.seeker_skills = seeker_skills;
    }

    public List<Certificate> getSeeker_cert() {
        return seeker_cert;
    }

    public void setSeeker_cert(List<Certificate> seeker_cert) {
        this.seeker_cert = seeker_cert;
    }

    public List<Training> getSeeker_train() {
        return seeker_train;
    }

    public void setSeeker_train(List<Training> seeker_train) {
        this.seeker_train = seeker_train;
    }

    public List<Hobby> getSeeker_hobby() {
        return seeker_hobby;
    }

    public void setSeeker_hobby(List<Hobby> seeker_hobby) {
        this.seeker_hobby = seeker_hobby;
    }

    public List<Info> getSeeker_info() {
        return seeker_info;
    }

    public void setSeeker_info(List<Info> seeker_info) {
        this.seeker_info = seeker_info;
    }
}
