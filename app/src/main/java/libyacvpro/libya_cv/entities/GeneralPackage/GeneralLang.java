package libyacvpro.libya_cv.entities.GeneralPackage;

public class GeneralLang {

    Integer lang_id;
    String lang_name;

    public GeneralLang(Integer lang_id, String lang_name) {
        this.lang_id = lang_id;
        this.lang_name = lang_name;
    }

    public Integer getLang_id() {
        return lang_id;
    }

    public void setLang_id(Integer lang_id) {
        this.lang_id = lang_id;
    }

    public String getLang_name() {
        return lang_name;
    }

    public void setLang_name(String lang_name) {
        this.lang_name = lang_name;
    }
}
