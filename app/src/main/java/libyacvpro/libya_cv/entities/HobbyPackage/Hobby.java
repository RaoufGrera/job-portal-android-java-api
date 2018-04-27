package libyacvpro.libya_cv.entities.HobbyPackage;


public class Hobby {

    Integer job_hobby_id;
    Integer hobby_id;
    String hobby_name;

    public Hobby(Integer job_hobby_id,Integer hobby_id, String hobby_name) {
        this.job_hobby_id = job_hobby_id;
        this.hobby_id = hobby_id;
        this.hobby_name = hobby_name;
    }

    public Integer getJob_hobby_id() {
        return job_hobby_id;
    }

    public void setJob_hobby_id(Integer job_hobby_id) {
        this.job_hobby_id = job_hobby_id;
    }

    public Integer getHobby_id() {
        return hobby_id;
    }

    public void setHobby_id(Integer hobby_id) {
        this.hobby_id = hobby_id;
    }

    public String getHobby_name() {
        return hobby_name;
    }

    public void setHobby_name(String hobby_name) {
        this.hobby_name = hobby_name;
    }
}
