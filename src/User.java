public class User {
    private String name;
    private String username;
    private String password;
    private boolean loggedin;

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.loggedin = false;
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

    public boolean isLoggedIn() {
        return loggedin;
    }

    public void setLoggedIn(boolean loggedin) {
        this.loggedin = loggedin;
    }
}
