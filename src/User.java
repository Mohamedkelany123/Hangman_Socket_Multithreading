public class User {
    private String name;
    private String username;
    private String password;
    private boolean loggedin;
    private double scoreHistory;
    public Object out;

    public User(String name, String username, String password, boolean loggedIn) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.loggedin = loggedIn;
        this.scoreHistory = 0;
    }


    
    public User(String name, String username, String password, boolean loggedIn, double scoreHistory) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.loggedin = loggedIn;
        this.scoreHistory = scoreHistory;
    }

    public double getScoreHistory() {
        return scoreHistory;
    }

    public void setScoreHistory(double score) {
        this.scoreHistory += score;
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
