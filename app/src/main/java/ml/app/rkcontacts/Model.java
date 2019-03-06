package ml.app.rkcontacts;

public class Model {
    String title;
    String desc;
    String icon;

    //constructor
    public Model(String title, String desc, String icon) {
        this.title = title;
        this.desc = desc;
        this.icon = icon;
    }

    //getters


    public String getTitle() {
        return this.title;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getIcon() {
        return this.icon;
    }
}