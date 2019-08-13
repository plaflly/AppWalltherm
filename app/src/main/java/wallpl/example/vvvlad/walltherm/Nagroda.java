package wallpl.example.vvvlad.walltherm;

public class Nagroda {
    private String nazwa;
    private String krutkiOpis;
    private String opis;
    private String img;
    private String punkt;
    private String kay;
    private String userMailNagroda;
    private String idUser;
    private String timeNagroda;

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getKrutkiOpis() {
        return krutkiOpis;
    }

    public void setKrutkiOpis(String krutkiOpis) {
        this.krutkiOpis = krutkiOpis;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPunkt() {
        return punkt;
    }

    public void setPunkt(String punkt) {
        this.punkt = punkt;
    }

    public String getKay() {
        return kay;
    }

    public void setKay(String kay) {
        this.kay = kay;
    }

    public String getUserMailNagroda() {
        return userMailNagroda;
    }

    public void setUserMailNagroda(String userMailNagroda) {
        this.userMailNagroda = userMailNagroda;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getTimeNagroda() {
        return timeNagroda;
    }

    public void setTimeNagroda(String timeNagroda) {
        this.timeNagroda = timeNagroda;
    }

    public Nagroda(String nazwa, String krutkiOpis, String opis, String img, String punkt, String kay, String userMailNagroda, String idUser, String timeNagroda) {
        this.nazwa = nazwa;
        this.krutkiOpis = krutkiOpis;
        this.opis = opis;
        this.img = img;
        this.punkt = punkt;
        this.kay = kay;
        this.userMailNagroda = userMailNagroda;
        this.idUser = idUser;
        this.timeNagroda = timeNagroda;
    }

    public Nagroda() {
    }
}
