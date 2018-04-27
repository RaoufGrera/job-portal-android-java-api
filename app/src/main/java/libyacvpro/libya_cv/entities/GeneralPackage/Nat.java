package libyacvpro.libya_cv.entities.GeneralPackage;

import java.util.HashMap;
import java.util.Map;
public class Nat {

    private Integer nat_id;
    private String nat_name;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getNatId() {
        return nat_id;
    }

    public void setNatId(Integer natId) {
        this.nat_id = natId;
    }

    public String getNatName() {
        return nat_name;
    }

    public void setNatName(String natName) {
        this.nat_name = natName;
    }



    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
