package models;

/**
 * Helper class for registering new Moodles
 */
public class TokenID {
    private String token;
    private int userid;

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
}
