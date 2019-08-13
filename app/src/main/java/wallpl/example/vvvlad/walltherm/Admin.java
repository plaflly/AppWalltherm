package wallpl.example.vvvlad.walltherm;

public class Admin {
   private String email;
   private String id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Admin(String email, String id) {
        this.email = email;
        this.id = id;
    }

    public Admin() {
    }
}
