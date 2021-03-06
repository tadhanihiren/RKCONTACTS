package ml.app.rkcontacts.helpers;

public class Model {
    String name;
    String email;
    String icon;
    String mobile;
    String ext;
    String gender;
    String school;
    String branch;
    String role;


    //constructor
    public Model(String title, String desc, String icon, String mobile, String ext, String gender, String school, String branch, String role) {
        this.name = title;
        this.email = desc;
        this.icon = icon;
        this.mobile = mobile;
        this.ext = ext;
        this.gender = gender;
        this.school = school;
        this.branch = branch;
        this.role = role;
    }

    //getters


    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getIcon() {
        return this.icon;
    }
    public String getMobile() {
        return this.mobile;
    }
    public String getExt() {
        return this.ext;
    }
    public String getGender() {
        return this.gender;
    }
    public String getSchool() {
        return this.school;
    }
    public String getBranch() {
        return this.branch;
    }

    public String getRole() {
        return this.role;
    }
}