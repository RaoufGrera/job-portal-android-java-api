package libyacvpro.libya_cv.entities.SkillsPackage;

/**
 * Created by Asasna on 9/28/2017.
 */

public class Skills {

    Integer skills_id;
    String skills_name;
    String level_name;

    public Skills(Integer skills_id, String skills_name, String level_name) {
        this.skills_id = skills_id;
        this.skills_name = skills_name;
        this.level_name = level_name;
    }

    public Integer getSkills_id() {
        return skills_id;
    }

    public void setSkills_id(Integer skills_id) {
        this.skills_id = skills_id;
    }

    public String getSkills_name() {
        return skills_name;
    }

    public void setSkills_name(String skills_name) {
        this.skills_name = skills_name;
    }



    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }
}
