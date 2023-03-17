public class User {
    private String name;
    private String username;
    private String password;
    private boolean loggedIn;

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.loggedIn = false;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean state) {
        this.loggedIn = state;
    }
}
