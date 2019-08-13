package wallpl.example.vvvlad.walltherm;
public class Sklepy {
    private String wojewodstwo;
    private String nazwa;
    private String miasto;
    private String ulica;
    private String telefon;
    private String telefon1;
    private String telefon2;
    private String iD;
    private String status;
    private String adres;

    public String getWojewodstwo() {
        return wojewodstwo;
    }

    public void setWojewodstwo(String wojewodstwo) {
        this.wojewodstwo = wojewodstwo;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getMiasto() {
        return miasto;
    }

    public void setMiasto(String miasto) {
        this.miasto = miasto;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getTelefon1() {
        return telefon1;
    }

    public void setTelefon1(String telefon1) {
        this.telefon1 = telefon1;
    }

    public String getTelefon2() {
        return telefon2;
    }

    public void setTelefon2(String telefon2) {
        this.telefon2 = telefon2;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public Sklepy(String wojewodstwo, String nazwa, String miasto, String ulica, String telefon, String telefon1, String telefon2, String iD, String status, String adres) {
        this.wojewodstwo = wojewodstwo;
        this.nazwa = nazwa;
        this.miasto = miasto;
        this.ulica = ulica;
        this.telefon = telefon;
        this.telefon1 = telefon1;
        this.telefon2 = telefon2;
        this.iD = iD;
        this.status = status;
        this.adres = adres;
    }

    public Sklepy() {

    }


}
