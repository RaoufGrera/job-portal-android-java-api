package libyacvpro.libya_cv.entities;

public class Post {

    int id;
    String fname;
    String body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return fname;
    }

    public void setTitle(String title) {
        this.fname = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
