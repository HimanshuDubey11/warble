package warble.project.com.warbleandroid.models;

public class User {

    String uId;
    String uName;
    String uImage;

    public User() {
    }

    public User(String uId, String uName, String uImage) {
        this.uId = uId;
        this.uName = uName;
        this.uImage = uImage;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuImage() {
        return uImage;
    }

    public void setuImage(String uImage) {
        this.uImage = uImage;
    }
}
