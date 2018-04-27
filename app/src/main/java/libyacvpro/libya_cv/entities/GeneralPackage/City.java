package libyacvpro.libya_cv.entities.GeneralPackage;
import java.util.HashMap;
import java.util.Map;
public class City {
    private String city_id;
    private String city_name;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getCityId() {
        return city_id.toString();
    }

    public void setCityId(String cityId) {
        this.city_id = cityId;
    }

    public String getCityName() {
        return city_name;
    }

    public void setCityName(String cityName) {
        this.city_name = cityName;
    }



    public City(String city_id, String city_name) {
        this.city_id = city_id;
        this.city_name = city_name;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}

