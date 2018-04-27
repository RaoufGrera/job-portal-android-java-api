package libyacvpro.libya_cv.entities.LangPackage;

import java.util.List;

import libyacvpro.libya_cv.entities.GeneralPackage.GeneralLang;
import libyacvpro.libya_cv.entities.GeneralPackage.Level;

/**
 * Created by Asasna on 9/27/2017.
 */

public class LanguageForEdit {

    private List<GeneralLang> lang_type = null;
    private List<Level> level = null;
    private Language language = null;

    public List<GeneralLang> getLang_type() {
        return lang_type;
    }

    public void setLang_type(List<GeneralLang> lang_type) {
        this.lang_type = lang_type;
    }

    public List<Level> getLevel() {
        return level;
    }

    public void setLevel(List<Level> level) {
        this.level = level;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
