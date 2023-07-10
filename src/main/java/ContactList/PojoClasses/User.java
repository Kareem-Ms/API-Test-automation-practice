package ContactList.PojoClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private UserInfo user;
    private String token;

    public void setUser(UserInfo user) {
        this.user = user;
    }
    public UserInfo getUser() {
        return user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }


}
