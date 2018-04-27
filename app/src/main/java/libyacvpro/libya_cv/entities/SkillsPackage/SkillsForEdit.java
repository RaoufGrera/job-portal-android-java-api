package libyacvpro.libya_cv.entities.SkillsPackage;

import java.util.List;

import libyacvpro.libya_cv.entities.GeneralPackage.Level;

/**
 * Created by Asasna on 9/28/2017.
 */

public class SkillsForEdit {
    private List<Level> level = null;
    private Skills skills = null;

    public List<Level> getLevel() {
        return level;
    }

    public void setLevel(List<Level> level) {
        this.level = level;
    }

    public Skills getSkills() {
        return skills;
    }

    public void setSkills(Skills skills) {
        this.skills = skills;
    }
}
