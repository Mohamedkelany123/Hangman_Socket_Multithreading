public class User {
    private String name;
    private String username;
    private String password;
    private boolean loggedin;
    private double scoreHistory;
    private int attempts;

    public User(String name, String username, String password, boolean loggedIn) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.loggedin = loggedIn;
        this.scoreHistory = 0;
        this.attempts = 6;
    }


    
    public User(String name, String username, String password, boolean loggedIn, double scoreHistory, int attempts) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.loggedin = loggedIn;
        this.scoreHistory = scoreHistory;
        this.attempts = attempts;
    }

    public double getScoreHistory() {
        return scoreHistory;
    }

    public void setScoreHistory(double score) {
        this.scoreHistory += score;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public void reduceAttempts(){
        if(this.attempts != 0){
            this.attempts -= 1;
        }
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
