package storm.skyline.config;

import java.io.Serializable;

public class QueueConfig implements Serializable {

    private static final long serialVersionUID = 4857404373646620748L;

    private String name;

    private String username;

    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}