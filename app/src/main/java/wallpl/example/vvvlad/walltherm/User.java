package wallpl.example.vvvlad.walltherm;

public class User {
    private String emailUser;
    private String pktUser;

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getPktUser() {
        return pktUser;
    }

    public void setPktUser(String pktUser) {
        this.pktUser = pktUser;
    }

    public User(String emailUser, String pktUser) {
        this.emailUser = emailUser;
        this.pktUser = pktUser;
    }

    public User() {
    }
}
