package libyacvpro.libya_cv.entities.GeneralPackage;

public class TypeCompanyEntities {

    private String compt_id;
    private String compt_name;

    public TypeCompanyEntities(String compt_id, String compt_name) {
        this.compt_id = compt_id;
        this.compt_name = compt_name;
    }

    public String getCompt_id() {
        return compt_id;
    }

    public void setCompt_id(String compt_id) {
        this.compt_id = compt_id;
    }

    public String getCompt_name() {
        return compt_name;
    }

    public void setCompt_name(String compt_name) {
        this.compt_name = compt_name;
    }
}
