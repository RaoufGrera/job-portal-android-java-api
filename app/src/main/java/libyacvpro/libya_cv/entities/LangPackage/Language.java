package libyacvpro.libya_cv.entities.LangPackage;

/**
 * Created by Asasna on 9/27/2017.
 */

public class Language {

    Integer lang_id;
    Integer job_lang_id;
    String lang_name;
    String level_name;

    public Language(Integer lang_id, Integer job_lang_id, String lang_name, String level_name) {
        this.lang_id = lang_id;
        this.job_lang_id = job_lang_id;
        this.lang_name = lang_name;
        this.level_name = level_name;
    }

    public Integer getLang_id() {
        return lang_id;
    }

    public void setLang_id(Integer lang_id) {
        this.lang_id = lang_id;
    }

    public Integer getJob_lang_id() {
        return job_lang_id;
    }

    public void setJob_lang_id(Integer job_lang_id) {
        this.job_lang_id = job_lang_id;
    }

    public String getLang_name() {
        return lang_name;
    }

    public void setLang_name(String lang_name) {
        this.lang_name = lang_name;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }
}
