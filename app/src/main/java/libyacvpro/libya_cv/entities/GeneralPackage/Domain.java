package libyacvpro.libya_cv.entities.GeneralPackage;

/**
 * Created by Asasna on 9/23/2017.
 */

public class Domain {
    Integer domain_id;
    String domain_name;
    String image;
    boolean selected;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSelected() {

            return selected;

     }

    public void setSelected(boolean selected) {

        this.selected = selected;
    }

    public Integer getDomain_id() {
        return domain_id;
    }

    public void setDomain_id(Integer domain_id) {
        this.domain_id = domain_id;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }
}
