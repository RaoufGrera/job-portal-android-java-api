package libyacvpro.libya_cv.entities.GeneralPackage;

/**
 * Created by Asasna on 10/15/2017.
 */

public class StatusEntities {
    private String status_id;
    private String status_name;

    public StatusEntities(String status_id, String status_name) {
        this.status_id = status_id;
        this.status_name = status_name;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }
}
