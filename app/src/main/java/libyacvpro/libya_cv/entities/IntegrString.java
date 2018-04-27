package libyacvpro.libya_cv.entities;

/**
 * Created by Asasna on 9/22/2017.
 */

public class IntegrString {

    Integer id;
    String name;

    public IntegrString(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
