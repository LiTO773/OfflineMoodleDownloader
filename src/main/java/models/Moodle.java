package models;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Moodle extends Downloadable implements Serializable {
    int id;
    String url;
    String username;
    String token;
    int userid;
    List<Course> courses;
    String diskLocation;

    public Moodle() {
    }

    public Moodle(String name, String url, String username, String token, int userid) {
        // By default it is true, however the user can later disable downloads from this Moodle
        super(name, true);
        this.url = url;
        this.username = username;
        this.token = token;
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public String getDiskLocation() {
        return diskLocation;
    }

    public void setDiskLocation(String diskLocation) {
        this.diskLocation = diskLocation;
    }

    @Override
    public String toString() {
        return "Moodle{" +
                "id=" + id +
                ", name='" + this.getName() + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", userid=" + userid +
                ", courses=" + courses +
                ", diskLocation='" + diskLocation + '\'' +
                '}';
    }
}
