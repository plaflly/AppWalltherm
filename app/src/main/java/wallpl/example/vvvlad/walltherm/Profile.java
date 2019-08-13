package wallpl.example.vvvlad.walltherm;

public class Profile {
    private String status;
    private String email;
    private String firma;
    private String id;
    private String name;
    private String city;
    private String surname;
    private String pass;
    private String telefon;
    private String street;
    private String nip;
    private String lastEnter;
    private String dataEnt;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getLastEnter() {
        return lastEnter;
    }

    public void setLastEnter(String lastEnter) {
        this.lastEnter = lastEnter;
    }

    public String getDataEnt() {
        return dataEnt;
    }

    public void setDataEnt(String dataEnt) {
        this.dataEnt = dataEnt;
    }

    public Profile(String status, String email, String firma, String id, String name, String city, String surname, String pass, String telefon, String street, String nip, String lastEnter, String dataEnt) {
        this.status = status;
        this.email = email;
        this.firma = firma;
        this.id = id;
        this.name = name;
        this.city = city;
        this.surname = surname;
        this.pass = pass;
        this.telefon = telefon;
        this.street = street;
        this.nip = nip;
        this.lastEnter = lastEnter;
        this.dataEnt = dataEnt;
    }

    public Profile() {
    }
}